package com.trinity.courierapp.Util;

import com.trinity.courierapp.DTO.CoordinateRecord;
import com.trinity.courierapp.DTO.GeocodingResult;
import com.trinity.courierapp.Service.GoogleMapsService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class CommonUtils {

    @Autowired
    private GoogleMapsService googleMapsService;

    private static final double MIN_LATITUDE = 42.75;
    private static final double MAX_LATITUDE = 42.95;
    private static final double MIN_LONGITUDE = 74.45;
    private static final double MAX_LONGITUDE = 74.75;

    public double findDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000; // meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // meters
    }

    public boolean pointInsideCity(CoordinateRecord cityGps, CoordinateRecord gpsB, int radiusMeters) {
        double dist = haversine(cityGps.lat(), cityGps.lng(), gpsB.lat(), gpsB.lng());
        return dist <= radiusMeters;
    }

    public boolean twoPointsInsideCity(CoordinateRecord cityGps, CoordinateRecord gpsA, CoordinateRecord gpsB, int radiusMeters) {
        double distA = haversine(cityGps.lat(), cityGps.lng(), gpsA.lat(), gpsA.lng());
        double distB = haversine(cityGps.lat(), cityGps.lng(), gpsB.lat(), gpsB.lng());
        return distA <= radiusMeters && distB <= radiusMeters;
    }

    public double getDistanceAtoBMeters(String pointA, String pointB) {
        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(pointA);
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcGeocode.lat(), srcGeocode.lng(), destGeocode.lat(), destGeocode.lng());
        return googleMapsService.extractDistance(directions);
    }

    public double getDistanceAtoBMeters(Point srcCoordinates, String pointB) {
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcCoordinates.getY(), srcCoordinates.getX(), destGeocode.lat(), destGeocode.lng());
        return googleMapsService.extractDistance(directions);
    }

    public double getDurationAtoBMinutes(String pointA, String pointB) {
        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(pointA);
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcGeocode.lat(), srcGeocode.lng(), destGeocode.lat(), destGeocode.lng());
        double durationInSec = googleMapsService.extractDuration(directions);
        double durationInMinutes = Math.round(durationInSec / 60);
        return durationInMinutes;
    }

    public double getDurationAtoBMinutes(Point srcCoordinates, String pointB) {
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcCoordinates.getY(), srcCoordinates.getX(), destGeocode.lat(), destGeocode.lng());
        double durationInSec = googleMapsService.extractDuration(directions);
        double durationInMinutes = Math.round(durationInSec / 60);
        return durationInMinutes;
    }

    public String getRouteAtoB(String pointA, String pointB) {
        GeocodingResult srcGeocode = googleMapsService.geocodeAddress(pointA);
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcGeocode.lat(), srcGeocode.lng(), destGeocode.lat(), destGeocode.lng());
        return googleMapsService.extractPolyline(directions);
    }

    public String getRouteAtoB(Point srcCoordinates, String pointB) {
        GeocodingResult destGeocode = googleMapsService.geocodeAddress(pointB);
        Map<String, Object> directions = googleMapsService.
                doGetDirections(srcCoordinates.getY(), srcCoordinates.getX(), destGeocode.lat(), destGeocode.lng());
        return googleMapsService.extractPolyline(directions);
    }

    public static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), 4326);
    public static Point toPoint(double lat, double lng) {
        return GEOMETRY_FACTORY.createPoint(new Coordinate(lng, lat));
    }



    private static final Random random = new Random();

    public double genRandLat() {
        double latitude = MIN_LATITUDE + (MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble();
        double scale = Math.pow(10, 6);
        double rounded = Math.round(latitude * scale) / scale;
        return rounded;
    }

    public double genRandLng() {
        double longitude = MIN_LONGITUDE + (MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble();
        double scale = Math.pow(10, 6);
        double rounded = Math.round(longitude * scale) / scale;
        return rounded;
    }

    public String genRandCoord() {
        double latitude = MIN_LATITUDE + (MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble();
        double longitude = MIN_LONGITUDE + (MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble();
        return String.format("%.6f, %.6f", latitude, longitude);
    }



}
