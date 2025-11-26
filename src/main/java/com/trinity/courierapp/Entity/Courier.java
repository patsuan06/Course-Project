package com.trinity.courierapp.Entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Setter
@Getter
@Entity
@Table(name = "courier_profiles")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_id_gen")
    @SequenceGenerator(name = "courier_id_gen", sequenceName = "courier_courier_id_seq", allocationSize = 1)
    @Column(name = "courier_id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "courier")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private User courierUser;

    @Column(name = "courier_gps", columnDefinition = "geography(Point,4326)")
    private Point courierGps;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleTypeEnum vehicleType;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("FREE")
    @Column(name = "courier_status",nullable = false)
    private CourierStatusEnum courierStatus;

    public Courier() {}

    public Courier(Point courierGps, VehicleTypeEnum vehicleType, CourierStatusEnum courierStatus) {
        this.courierGps = courierGps;
        this.vehicleType = vehicleType;
        this.courierStatus = courierStatus;
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

}