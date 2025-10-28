package com.trinity.courierapp.Entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_id_gen")
    @SequenceGenerator(name = "client_id_gen", sequenceName = "client_client_id_seq", allocationSize = 1)
    @Column(name = "client_id", nullable = false)
    private Integer id;

    @Column(name = "full_name", nullable = false, length = 60)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = Integer.MAX_VALUE)
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    private Set<Order> orders = new LinkedHashSet<>();

    @Column(name = "password", nullable = false, length = 60)
    private String password;

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