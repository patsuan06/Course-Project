package com.trinity.courierapp;

import com.trinity.courierapp.Util.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrourierAppApplicationTests {

    @Autowired
    private CommonUtils commonUtils;


    @Test
    void genPoint(){
        double lat = commonUtils.genRandLat();
        double lng = commonUtils.genRandLng();
        System.out.printf("%.6f,%.6f", lat, lng);;
    }

}
