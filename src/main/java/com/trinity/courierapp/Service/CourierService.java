package com.trinity.courierapp.Service;

import com.trinity.courierapp.DTO.GeocodingResult;
import com.trinity.courierapp.DTO.OrderInitResponseDto;
import com.trinity.courierapp.Entity.Courier;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Util.CommonUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private GoogleMapsService googleMapsService;

    // searching for courier in 100 km radius
    public FindCourierResult findNearestCourierFurther(OrderInitResponseDto dto) {
        boolean found = false;
        String srcAddress = dto.getSrcAddress();
        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(srcAddress);
        //radius in meters to cut of the search area
        double MAX_DISTANCE= 100_000;

        List<Courier> candidates =
                courierRepository.findEligibleCouriers(
                        srcGeocode.lat(),
                        srcGeocode.lng(),
                        MAX_DISTANCE,
                        dto.getVehicleType()
                );
        Courier nearest = null;
        double minRouteDistance = MAX_DISTANCE;
        double routeDist = 0;

        for (Courier c : candidates) {
            routeDist = commonUtils.getDistanceAtoBMeters(c.getCourierGps(), srcAddress);

            if (routeDist <= MAX_DISTANCE && routeDist < minRouteDistance) {
                minRouteDistance = routeDist;
                nearest = c;
            }
        }

        double finalPrice = dto.getPrice() + routeDist * dto.getPriceKmRate() * 0.5;;
        double durationMins = dto.getDurationMinutes();
        assert nearest != null;
        Point courierCoords = nearest.getCourierGps();
        String courierToARoute = commonUtils.getRouteAtoB(courierCoords,srcAddress);
        double courierToAMins = commonUtils.getDurationAtoBMinutes(courierCoords,srcAddress);
        double finalDuration = courierToAMins + durationMins;


        return new FindCourierResult(nearest, true, finalDuration,finalPrice,courierToARoute,courierToAMins,routeDist);
    }


    //searching for courier in 10 km radius
    public FindCourierResult findNearestCourier(OrderInitResponseDto dto) {
        String srcAddress = dto.getSrcAddress();
        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(srcAddress);
        //radius in meters to cut of the search area
        double MAX_DISTANCE= 10_000;

        List<Courier> candidates =
                courierRepository.findEligibleCouriers(
                        srcGeocode.lat(),
                        srcGeocode.lng(),
                        MAX_DISTANCE,
                        dto.getVehicleType()
                );
        Courier nearest = null;
        double minRouteDistance = MAX_DISTANCE;
        double routeDist = 0;

        for (Courier c : candidates) {
            routeDist = commonUtils.getDistanceAtoBMeters(c.getCourierGps(), srcAddress);

            if (routeDist <= MAX_DISTANCE && routeDist < minRouteDistance) {
                minRouteDistance = routeDist;
                nearest = c;
            }
        }

        double finalPrice = dto.getPrice();
        double durationMins = dto.getDurationMinutes();
        assert nearest != null;
        Point courierCoords = nearest.getCourierGps();
        String courierToARoute = commonUtils.getRouteAtoB(courierCoords,srcAddress);
        double courierToAMins = commonUtils.getDurationAtoBMinutes(courierCoords,srcAddress);
        double finalDuration = courierToAMins + durationMins;

        return new FindCourierResult(nearest,true,finalDuration,finalPrice,courierToARoute,courierToAMins,routeDist);
    }

    public record FindCourierResult(Courier courier, boolean found, double newDuration, double newPrice, String courierToARoute, double courierToAMinutes, double routeCourierToADist) {}

}

//  You could take out the route loop coniditon out and use it as a module ot reduc eboilerplate code
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