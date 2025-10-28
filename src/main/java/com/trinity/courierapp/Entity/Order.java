package com.trinity.courierapp.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.math.BigDecimal;


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
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "src_gps", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point srcGps;

    @Column(name = "dest_gps", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point destGps;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleTypeEnum vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderTypeEnum orderType;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("SEARCHING_FOR_COURIERS")
    @Column(name = "order_status", nullable = false)
    private OrderStatusEnum orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodEnum paymentMethod;

    public VehicleTypeEnum getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeEnum vehicleType) {
        this.vehicleType = vehicleType;
    }


    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Point getDestGps() {
        return destGps;
    }

    public void setDestGps(Point destGps) {
        this.destGps = destGps;
    }

    public Point getSrcGps() {
        return srcGps;
    }

    public void setSrcGps(Point srcGps) {
        this.srcGps = srcGps;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRecipientFullName() {
        return recipientFullName;
    }

    public void setRecipientFullName(String recipientFullName) {
        this.recipientFullName = recipientFullName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}