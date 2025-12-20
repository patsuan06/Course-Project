package com.trinity.courierapp;

import com.trinity.courierapp.Util.CommonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CourierAppApplication  {
    public static void main(String[] args) {
        SpringApplication.run(CourierAppApplication.class, args);
    }
}

//public class CourierAppApplication  {
//    public static void main(String[] args) {
//        CommonUtils commonUtils = new CommonUtils();
//        double lat = commonUtils.genRandLat();
//        double lng = commonUtils.genRandLng();
//        System.out.printf("%.6f,%.6f", lat, lng);
//
//    }
//}