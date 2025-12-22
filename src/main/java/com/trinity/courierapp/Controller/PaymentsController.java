package com.trinity.courierapp.Controller;

import com.stripe.exception.StripeException;
import com.trinity.courierapp.DTO.CreateIntentDto;
import com.trinity.courierapp.DTO.OrderInitResponseDto;
import com.trinity.courierapp.DTO.PaymentIntentResponse;
import com.trinity.courierapp.DTO.SavedMethodsDto;
import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Entity.PaymentDetail;
import com.trinity.courierapp.Entity.PaymentMethodEnum;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.OrderRepository;
import com.trinity.courierapp.Repository.PaymentDetailRepository;
import com.trinity.courierapp.Repository.UserRepository;
import com.trinity.courierapp.Service.OrderService;
import com.trinity.courierapp.Service.PaymentService;
import com.trinity.courierapp.Util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final RedisCache redisCache;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final RedisTemplate<String, Object> redis;
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

//    @Autowired
//    private RedisCache redisCache;
//
//    @Autowired
//    private RedisTemplate<String, Object> redis;

    public PaymentsController(PaymentService paymentService, UserRepository userRepository, OrderService orderService, RedisCache redisCache, OrderRepository orderRepository , RedisTemplate<String, Object> redis, PaymentDetailRepository paymentDetailRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.redisCache = redisCache;
        this.orderRepository = orderRepository;
        this.redis = redis;
        this.paymentDetailRepository = paymentDetailRepository;
    }


    @PostMapping("/intent")
    public ResponseEntity<?> createIntent(@RequestBody CreateIntentDto intentDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email);
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findByUser(user);
            if(paymentDetail.isEmpty()){
                return  ResponseEntity.status(400).body("No saved payment method found");
            }
            OrderInitResponseDto dto = redisCache.get(intentDto.getOrderToken(),OrderInitResponseDto.class);
            String response = paymentService.createIntentAndPayWithSavedMethod(dto.getPrice(), intentDto.getPaymentMethodId(),paymentDetail.get().getStripeCustomerId());
            dto.setPaymentDetailId(paymentDetail.get().getId());
            dto.setPaymentMethod(PaymentMethodEnum.TRANSFER);
            Order order = orderService.createOrder(dto,user);
            dto.setIntentStatus(response);
            orderRepository.save(order);
            redisCache.save(intentDto.getOrderToken(), dto, (long) dto.getFinalDurationMins()/1000 + 6000);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment"+e.getMessage());
        }

    }

    @PostMapping("/save_method") /// send me the paymentmethodId that you should generate in the frontend after getting the key
    public ResponseEntity<?> savePaymentMethod(@RequestBody SaveMethodDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email);
            paymentService.saveStripeCustomerAndMethod(user, dto.paymentMethodId);
            return ResponseEntity.ok("Saved successfully, email is:"+ email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving payment method");
        }
    }

    @GetMapping("/methods") /// just send me the jwt token in the headers that's all, nothing in the body
    public List<SavedMethodsDto> getSavedMethods(@AuthenticationPrincipal UserDetails userDetails) throws StripeException {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        return paymentService.getSavedMethods(user);
    }

    public record SaveMethodDto(String paymentMethodId){};

}






//@PostMapping("/intent")
//public ResponseEntity<?> createIntent(@RequestParam Long amount) {
//    try {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        String clientSecret = paymentService.createIntentAndPayWithSavedMethod(amount, email);
//
//        return ResponseEntity.ok(clientSecret);
//    } catch (Exception e) {
//        return ResponseEntity.status(500).body("Error creating payment");
//    }
//}