package com.trinity.courierapp.Entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "courier")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_id_gen")
    @SequenceGenerator(name = "courier_id_gen", sequenceName = "courier_courier_id_seq", allocationSize = 1)
    @Column(name = "courier_id", nullable = false)
    private Integer id;

    @Column(name = "full_name", nullable = false, length = 60)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = Integer.MAX_VALUE)
    private String phoneNumber;

    @OneToMany(mappedBy = "courier")
    private Set<Order> orders = new LinkedHashSet<>();

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "courier_gps", columnDefinition = "geography(Point,4326)")
    private Point courierGps;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleTypeEnum vehicleType;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("FREE")
    @Column(name = "courier_status",nullable = false)
    private CourierStatusEnum courierStatus;

    public VehicleTypeEnum getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeEnum vehicleType) {
        this.vehicleType = vehicleType;
    }

    public CourierStatusEnum getCourierStatus() {
        return courierStatus;
    }

    public void setCourierStatus(CourierStatusEnum courierStatus) {
        this.courierStatus = courierStatus;
    }

    public Point getCourierGps() {
        return courierGps;
    }

    public void setCourierGps(Point courierGps) {
        this.courierGps = courierGps;
    }

    public double getLatitude() {
        return courierGps != null ? courierGps.getY() : 0.0;
    }

    public double getLongitude() {
        return courierGps != null ? courierGps.getX() : 0.0;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.courierGps = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}