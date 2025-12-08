package com.trinity.courierapp.Service;

import com.trinity.courierapp.DTO.GeocodingResult;
import com.trinity.courierapp.DTO.OrderInitResponseDto;
import com.trinity.courierapp.Entity.OrderTypeEnum;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Repository.OrderRepository;
import com.trinity.courierapp.Util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private GoogleMapsService googleMapsService;

    @Autowired
    private CommonUtils commonUtils;

//    public Order createOrder(OrderInitRequestDto orderInitRequestDto) {}

    public CalcResult calculatePrice(String srcAddress, String destAddress) {

        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(srcAddress);
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(destAddress);
        OrderTypeEnum currentOrderType = OrderTypeEnum.INTER_REGION;
        double price = 0;
        double boundary = 18;
        double km = commonUtils.findDistanceKm(srcGeocode.lat(), srcGeocode.lng(), destGeocode.lat(), destGeocode.lng());

        if (km < boundary) {
            currentOrderType = OrderTypeEnum.LOCAL;
        }


        return new CalcResult(price, currentOrderType);
    }

    public record CalcResult(double price, OrderTypeEnum orderType) {}



}
