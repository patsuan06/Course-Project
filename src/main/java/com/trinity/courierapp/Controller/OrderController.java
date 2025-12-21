package com.trinity.courierapp.Controller;

import com.trinity.courierapp.DTO.OrderInitRequestDto;
import com.trinity.courierapp.DTO.OrderInitResponseDto;
import com.trinity.courierapp.DTO.OrderTokenDto;
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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
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
    public ResponseEntity<OrderInitResponseDto> initOrder(@Valid @RequestBody OrderInitRequestDto orderRequest) {
        OrderInitResponseDto orderResponse = new OrderInitResponseDto();
        OrderService.CalcResult calcResult = orderService.calculatePrice(orderRequest.getSrcPlaceId(),orderRequest.getDestPlaceId(),orderRequest.getSrcLat(),orderRequest.getSrcLng(),orderRequest.getDestLat(),orderRequest.getDestLng());
        orderResponse.setPrice(calcResult.price());
        orderResponse.setOrderType(calcResult.orderType());
        orderResponse.setPriceKmRate(calcResult.priceRate());
        orderResponse.setDistanceMeters(commonUtils.getDistanceAtoBMeters(orderRequest.getSrcLat(),orderRequest.getSrcLng(),orderRequest.getDestLat(),orderRequest.getDestLng()));
        orderResponse.setDurationMinutes(commonUtils.getDurationAtoBMinutes(orderRequest.getSrcLat(),orderRequest.getSrcLng(),orderRequest.getDestLat(),orderRequest.getDestLng()));
        orderResponse.setRoute(commonUtils.getRouteAtoB(orderRequest.getSrcLat(),orderRequest.getSrcLng(),orderRequest.getDestLat(),orderRequest.getDestLng()));

        //existing info
        orderResponse.setRecipientFullName(orderRequest.getRecipientFullName());
        orderResponse.setRecipientPhoneNumber(orderRequest.getRecipientPhoneNumber());
        orderResponse.setVehicleType(orderRequest.getVehicleType());
        orderResponse.setSrcAddress(orderRequest.getSrcAddress());
        orderResponse.setSrcLat(orderRequest.getSrcLat());
        orderResponse.setSrcLng(orderRequest.getSrcLng());
        orderResponse.setSrcPlaceId(orderRequest.getSrcPlaceId());
        orderResponse.setDestAddress(orderRequest.getDestAddress());
        orderResponse.setDestLat(orderRequest.getDestLat());
        orderResponse.setDestLng(orderRequest.getDestLng());
        orderResponse.setDestPlaceId(orderRequest.getDestPlaceId());

        String orderToken = UUID.randomUUID().toString();
        orderResponse.setOrderToken("OrderInitResponseDto:" + orderToken);
        // store in cache
        redisCache.save("OrderInitResponseDto:" + orderToken, orderResponse, 660);

        //so we wait 10 minutes, then user opts to continue with the order, we wait 10 minutes, if it counts down cancel order
        //after he opts tho, we reset the timer
        return ResponseEntity.ok(orderResponse);
    }


    @PostMapping("/find_courier")
    public CompletableFuture<ResponseEntity<?>> findCourier(@Valid @RequestBody OrderTokenDto orderTokenDto) {
        String orderToken = orderTokenDto.getOrderToken();

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            long countdownMillis = 600 * 1000; // 10 minutes

            OrderInitResponseDto responseDto = redisCache.get(orderToken, OrderInitResponseDto.class);
            if (responseDto == null) return ResponseEntity.status(404).body("Order not found");

            while ((System.currentTimeMillis() - startTime) < countdownMillis) {
                try {
                    // Perform the search
                    CourierService.FindCourierResult findCourierResult = courierService.findNearestCourier(responseDto);

                    if (findCourierResult.found()) {
                        responseDto.setFinalDurationMins(findCourierResult.newDuration());
                        responseDto.setPrice(findCourierResult.newPrice());
                        responseDto.setCourierToARoute(findCourierResult.courierToARoute());
                        responseDto.setCourierToMins(findCourierResult.courierToAMinutes());
                        responseDto.setCourierToARouteMeter(findCourierResult.routeCourierToADist());
                        responseDto.setCourierId(findCourierResult.courierId());
                        redisCache.save( orderToken, responseDto, 660);
                        // Update your DTO with result data here...
                        try {
                            return ResponseEntity.ok(responseDto);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                    // If not found, wait 2 seconds before checking again
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(500).body("Search interrupted");
                } catch (Exception e) {
                    // Log the actual error so you can see why the DB/Logic is failing
                    log.error("e: ", e);
                }
            }

            // NO workerExecutor.shutdownNow() here!
            return ResponseEntity.status(408).body("Courier not found in time");
        });
    }
//    @PostMapping("/find_courier")
//public CompletableFuture< ResponseEntity<?> > findCourier(@Valid @RequestBody OrderTokenDto orderTokenDto) {
//
//
//        String orderToken = orderTokenDto.getOrderToken();
//
//        return CompletableFuture.supplyAsync(() -> {
//            boolean found = false;
//            long startTime = System.currentTimeMillis();
//            int countdownSeconds = 600;
//            long countdownMillis = countdownSeconds * 1000;
//
//            redis.expire("OrderInitResponseDto:" + orderToken, 1260, TimeUnit.SECONDS);
//            OrderInitResponseDto responseDto = redisCache.get(orderToken, OrderInitResponseDto.class);
//
//            while (!found && (System.currentTimeMillis() - startTime) < countdownMillis) {
//                try {
//                    Future<CourierService.FindCourierResult> futureResult = workerExecutor.submit(() -> courierService.findNearestCourier(responseDto));
//                    //The search starts here
//                    CourierService.FindCourierResult findCourierResult = futureResult.get(5000, TimeUnit.MILLISECONDS);
//                    if (findCourierResult.found()) {
//                        responseDto.setFinalDurationMins(findCourierResult.newDuration());
//                        responseDto.setPrice(findCourierResult.newPrice());
//                        responseDto.setCourierToARoute(findCourierResult.courierToARoute());
//                        responseDto.setCourierToMins(findCourierResult.courierToAMinutes());
//                        responseDto.setCourierToARouteMeter(findCourierResult.routeCourierToADist());
//                        responseDto.setCourierId(findCourierResult.courierId());
//                        found = true;
//                        break;
//
//                    }
//                    //will it actually keep searching after there is no one there?
//                } catch (TimeoutException e) {
//                    continue;
//                } catch (InterruptedException|ExecutionException e) {
//                    Thread.currentThread().interrupt();
//                    return ResponseEntity.status(500).body("Courier searching interrupted");
//                } catch (Exception e) {
//                    return ResponseEntity.ok(e.getMessage());
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            //gotta think about how this works, does stop right away when it finds someone
//            workerExecutor.shutdownNow();
//            if (found) {
//                return ResponseEntity.ok(responseDto);
//            } else {
//                return ResponseEntity.status(408).body("Courier not found in time");
//            }
//        });
//    }



    // if the user opts to pay extra
    @PostMapping("/find_courier_far")
    public CompletableFuture< ResponseEntity<?> > findCourierFar(@RequestBody OrderTokenDto orderTokenDto, @AuthenticationPrincipal UserDetails userDetails) {
        String orderToken = orderTokenDto.getOrderToken();
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
    public ResponseEntity<?> createOrder(@RequestBody OrderTokenDto orderTokenDto, @AuthenticationPrincipal UserDetails userDetails){

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        String orderToken = orderTokenDto.getOrderToken();
        OrderInitResponseDto dto = redisCache.get(orderToken, OrderInitResponseDto.class);
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