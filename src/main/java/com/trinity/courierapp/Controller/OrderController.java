package com.trinity.courierapp.Controller;

import com.trinity.courierapp.DTO.OrderInitRequestDto;
import com.trinity.courierapp.DTO.OrderInitResponseDto;
import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Entity.OrderStatusEnum;
import com.trinity.courierapp.Entity.PaymentMethodEnum;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Repository.OrderRepository;
import com.trinity.courierapp.Repository.UserRepository;
import com.trinity.courierapp.Service.CourierService;
import com.trinity.courierapp.Service.OrderService;
import com.trinity.courierapp.Util.CommonUtils;
import com.trinity.courierapp.Util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourierService courierService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<String, Object> redis;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private UserRepository userRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ExecutorService workerExecutor = Executors.newSingleThreadExecutor();

    @PostMapping("/cancelOrderInit")
    public ResponseEntity<?> cancelOrderInit(@RequestParam String orderToken) {




        return ResponseEntity.ok(orderRepository);
    }

    @PostMapping("/initialize")
    public ResponseEntity<OrderInitResponseDto> initOrder(OrderInitRequestDto orderRequest) {
        OrderInitResponseDto orderResponse = new OrderInitResponseDto();
        OrderService.CalcResult calcResult = orderService.calculatePrice(orderRequest.getSrcAddress(),orderRequest.getDestAddress());
        orderResponse.setPrice(calcResult.price());
        orderResponse.setOrderType(calcResult.orderType());
        orderResponse.setPriceKmRate(calcResult.priceRate());
        orderResponse.setDistanceMeters(commonUtils.getDistanceAtoBMeters(orderRequest.getSrcAddress(),orderRequest.getDestAddress()));
        orderResponse.setDurationMinutes(commonUtils.getDurationAtoBMinutes(orderRequest.getSrcAddress(),orderRequest.getDestAddress()));
        orderResponse.setRoute(commonUtils.getRouteAtoB(orderRequest.getSrcAddress(),orderRequest.getDestAddress()));

        //existing info
        orderResponse.setRecipientFullName(orderRequest.getRecipientFullName());
        orderResponse.setRecipientPhoneNumber(orderRequest.getRecipientPhoneNumber());
        orderResponse.setSrcAddress(orderRequest.getSrcAddress());
        orderResponse.setDestAddress(orderRequest.getDestAddress());
        orderResponse.setVehicleType(orderRequest.getVehicleType());

        String orderToken = UUID.randomUUID().toString();
        orderResponse.setOrderToken(orderToken);
        // store in cache
        redisCache.save("OrderInitResponseDto:" + orderToken, orderResponse, 660);

        //so we wait 10 minutes, then user opts to continue with the order, we wait 10 minutes, if it counts down cancel order
        //after he opts tho, we reset the timer
        return ResponseEntity.ok(orderResponse);
    }


    @PostMapping("/find_courier")
public CompletableFuture< ResponseEntity<?> > findCourier(@RequestParam String orderToken, @AuthenticationPrincipal UserDetails userDetails) {


        return CompletableFuture.supplyAsync(() -> {
            boolean found = false;
            long startTime = System.currentTimeMillis();
            int countdownSeconds = 600;
            long countdownMillis = countdownSeconds * 1000;

            redis.expire("OrderInitResponseDto:" + orderToken, 1260, TimeUnit.SECONDS);
            OrderInitResponseDto responseDto = redisCache.get(orderToken, OrderInitResponseDto.class);

            while (!found && (System.currentTimeMillis() - startTime) < countdownMillis) {
                try {
                    Future<CourierService.FindCourierResult> futureResult = workerExecutor.submit(() -> courierService.findNearestCourier(responseDto));
                    //The search starts here
                    CourierService.FindCourierResult findCourierResult = futureResult.get(500, TimeUnit.MILLISECONDS);
                    if (findCourierResult.found()) {
                        responseDto.setFinalDurationMins(findCourierResult.newDuration());
                        responseDto.setPrice(findCourierResult.newPrice());
                        responseDto.setCourierToARoute(findCourierResult.courierToARoute());
                        responseDto.setCourierToMins(findCourierResult.courierToAMinutes());
                        responseDto.setCourierToARouteMeter(findCourierResult.routeCourierToADist());
                        responseDto.setCourierId(findCourierResult.courierId());
                        found = true;
                        break;

                    }
                    //will it actually keep searching after there is no one there?
                }catch (TimeoutException e) {
                    continue;
                } catch (InterruptedException|ExecutionException e) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(500).body("Courier searching interrupted");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //gotta think about how this works, does stop right away when it finds someone
            workerExecutor.shutdownNow();
            if (found) {
                return ResponseEntity.ok(responseDto);
            } else {
                return ResponseEntity.status(408).body("Courier not found in time");
            }
        });
    }



    // if the user opts to pay extra
    @PostMapping("/find_courier_far")
    public CompletableFuture< ResponseEntity<?> > findCourierFar(@RequestParam String orderToken, @AuthenticationPrincipal UserDetails userDetails) {
        return CompletableFuture.supplyAsync(() -> {
            boolean found = false;
            long startTime = System.currentTimeMillis();
            int countdownSeconds = 600;
            long countdownMillis = countdownSeconds * 1000;

            redis.expire("OrderInitResponseDto:" + orderToken, 1260, TimeUnit.SECONDS);
            OrderInitResponseDto responseDto = redisCache.get(orderToken, OrderInitResponseDto.class);

            while (!found && (System.currentTimeMillis() - startTime) < countdownMillis) {
                try {
                    Future<CourierService.FindCourierResult> futureResult = workerExecutor.submit(() -> courierService.findNearestCourierFurther(responseDto));
                    //The search starts here
                    CourierService.FindCourierResult findCourierResult = futureResult.get(500, TimeUnit.MILLISECONDS);
                    if (findCourierResult.found()){
                        responseDto.setFinalDurationMins(findCourierResult.newDuration());
                        responseDto.setPrice(findCourierResult.newPrice());
                        responseDto.setCourierToARoute(findCourierResult.courierToARoute());
                        responseDto.setCourierToMins(findCourierResult.courierToAMinutes());
                        responseDto.setCourierToARouteMeter(findCourierResult.routeCourierToADist());
                        responseDto.setCourierId(findCourierResult.courierId());
                        found = true;
                        break;
                    };
                    //search ends
                }catch (TimeoutException e) {
                    continue;
                } catch (InterruptedException|ExecutionException e) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(500).body("Courier searching interrupted");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            workerExecutor.shutdownNow();
            if (found) {
                return ResponseEntity.ok("Courier found");
            } else {
                return ResponseEntity.status(408).body("Courier not found in time");
            }
        });
    }

    @PostMapping("/create_order_cash")
    public ResponseEntity<?> createOrder(@RequestParam String orderToken, @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        OrderInitResponseDto dto = redisCache.get(orderToken,OrderInitResponseDto.class);
        dto.setPaymentMethod(PaymentMethodEnum.CASH);
        Order order = orderService.createOrder(dto,user);
        orderRepository.save(order);
        return ResponseEntity.ok(dto);
    }

}



//public FindCourierResponseDto findCourierFar(@RequestBody FindCourierRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
//    String orderToken = requestDto.getOrderToken();
//    redis.expire("OrderInitResponseDto:" + orderToken, 1260, TimeUnit.SECONDS);
//    OrderInitResponseDto responseDto = redisCache.get(requestDto.getOrderToken(), OrderInitResponseDto.class);
//
//    courierService.findNearestCourier(responseDto);
//    return new FindCourierResponseDto();
//}



//public CompletableFuture< ResponseEntity<?> > findCourierFar(@RequestParam String orderToken, @AuthenticationPrincipal UserDetails userDetails) {
//
//    CompletableFuture<ResponseEntity<?>> future = CompletableFuture.supplyAsync(() -> {
//        boolean found = false;
//        long startTime = System.currentTimeMillis();
//        int countdownSeconds = 600;
//        long countdownMillis = countdownSeconds * 1000;
//
//        redis.expire("OrderInitResponseDto:" + orderToken, 1260, TimeUnit.SECONDS);
//        OrderInitResponseDto responseDto = redisCache.get(orderToken, OrderInitResponseDto.class);
//
//        while (!found && (System.currentTimeMillis() - startTime) < countdownMillis) {
//            CourierService.FindCourierResult findNearestCourierFurther = courierService.findNearestCourierFurther(responseDto);
//
//            found = findNearestCourierFurther.found();
//            try {
//                Thread.sleep(100); // avoid busy waiting
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                return ResponseEntity.status(500).body("Courier searching interrupted");
//            }
//        }
//
//        if (found) {
//            return ResponseEntity.ok( OrderInitResponseDto());
//        } else {
//            return ResponseEntity.status(408).body("0 couriers found");
//        }
//    }, workerExecutor);
//
//    return future;
//}