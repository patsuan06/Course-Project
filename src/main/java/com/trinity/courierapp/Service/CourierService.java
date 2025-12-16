//package com.trinity.courierapp.Service;
//
//import com.trinity.courierapp.DTO.CoordinateRecord;
//import com.trinity.courierapp.DTO.GeocodingResult;
//import com.trinity.courierapp.DTO.OrderInitResponseDto;
//import com.trinity.courierapp.Entity.Courier;
//import com.trinity.courierapp.Repository.CourierRepository;
//import com.trinity.courierapp.Util.CommonUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CourierService {
//
//    @Autowired
//    private CourierRepository courierRepository;
//
//    @Autowired
//    private CommonUtils commonUtils;
//
//    @Autowired
//    private GoogleMapsService googleMapsService;
//
//    //radius in meters to cut of the search area
//    private static final double MAX_DISTANCE= 10_000;
//
//    // searching for courier in 100 km radius
//    public FindCourierResult findNearestCourierFurther(OrderInitResponseDto dto) {
//        String destAddress = dto.getDestAddress();
//        String srcAddress = dto.getSrcAddress();
//        double durationMinutes = dto.getDurationMinutes();
//
////        String route = orderService.getRouteAtoB();
//
//        return new FindCourierResult();
//    }
//
//    //searching for courier in 10 km radius
//    public FindCourierResult findNearestCourier(OrderInitResponseDto dto) {
//        String destAddress = dto.getDestAddress();
//        String srcAddress = dto.getSrcAddress();
//
//        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(srcAddress);
//        double durationMinutes = dto.getDurationMinutes();
//        double price = dto.getPrice();
//        double String courierToARoute = dto.get
//
//        List<Courier> candidates =
//                courierRepository.findCouriersWithinRadius(
//                        srcGeocode.lat(),
//                        srcGeocode.lng(),
//                        MAX_DISTANCE
//                );
//        Courier nearest = null;
//        double minRouteDistance = MAX_DISTANCE;
//
//        for (Courier c : candidates) {
//            double routeDist = commonUtils.getDistanceAtoBMeters(c.getCourierGps(), srcAddress);
//
//            if (routeDist <= MAX_DISTANCE && routeDist < minRouteDistance) {
//                minRouteDistance = routeDist;
//                nearest = c;
//            }
//        }
//        return findCourierResult();
//
//
//
//
//
//
//        CoordinateRecord courierCoords = ;
//        String route = commonUtils.getRouteAtoB(CoordinateRecord courierCoords, srcAddress);
//
//
//
//
//
//
//
//        return new FindCourierResult();
//    }
//
//    public record FindCourierResult(){
//
//    };
//
//
//
//    public Courier findNearestCourierByRoute(CoordinateRecord target, List<Courier> couriers) {
//        Courier nearest = null;
//        double minDistance = Double.MAX_VALUE;
//
//        for (Courier c : couriers) {
//            // get route distance from courier to target in meters
//            double dist = getRouteDistance(c.getLat(), c.getLng(), target.lat(), target.lng());
//            if (dist < minDistance) {
//                minDistance = dist;
//                nearest = c;
//            }
//        }
//
//        return nearest;
//    }
//
//
//
//
//}
