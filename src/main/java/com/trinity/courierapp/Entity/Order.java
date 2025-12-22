package com.trinity.courierapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_gen")
    @SequenceGenerator(name = "orders_id_gen", sequenceName = "orders_order_id_seq", allocationSize = 1)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @Column(name = "recipient_full_name", length = 60)
    private String recipientFullName;

    @Column(name = "price", nullable = false, precision = 6)
    private BigDecimal price;

    @Column(name = "recipient_phone_number", nullable = false, length = Integer.MAX_VALUE)
    private String recipientPhoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "src_gps", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point srcGps;

    @Column(name = "dest_gps", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point destGps;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", columnDefinition = "vehicle_type_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private VehicleTypeEnum vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", columnDefinition = "order_type_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderTypeEnum orderType;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("SEARCHING_FOR_COURIERS")
    @Column(name = "order_status", columnDefinition = "order_status_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderStatusEnum orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", columnDefinition = "payment_method_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentMethodEnum paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentDetail paymentDetail;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    public Order() {}

    public Order(Point srcGps, Point destGps, OrderTypeEnum orderType, OrderStatusEnum orderStatus, VehicleTypeEnum vehicleType, Courier courier, User user, PaymentMethodEnum paymentMethod,String recipientFullName, BigDecimal price, String recipientPhoneNumber, LocalDate orderDate, PaymentDetail paymentDetail) {
        this.srcGps = srcGps;
        this.destGps = destGps;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.vehicleType = vehicleType;
        this.courier = courier;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.recipientFullName = recipientFullName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.orderDate = orderDate;
        this.paymentDetail = paymentDetail;
    }


    public double getSrcLatitude() {
        return srcGps != null ? srcGps.getY() : 0.0;
    }

    public double getSrcLongitude() {
        return srcGps != null ? srcGps.getX() : 0.0;
    }

    public void setSrcCoordinates(double latitude, double longitude) {
        this.srcGps = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }

    public double getDestLatitude() {
        return srcGps != null ? srcGps.getY() : 0.0;
    }

    public double getDestLongitude() {
        return srcGps != null ? srcGps.getX() : 0.0;
    }

    public void setDestCoordinates(double latitude, double longitude) {
        this.srcGps = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }

}