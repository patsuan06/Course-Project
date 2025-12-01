package com.trinity.courierapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "full_name", nullable = false, length = 60)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = Integer.MAX_VALUE)
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToOne(mappedBy = "courierUser")
    private Courier courier;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", columnDefinition = "user_type_enum",nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserTypeEnum userType;

    public User() {}

    public User(String fullName, String phoneNumber, String password, String email, UserTypeEnum userType) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.userType = userType;

    }

}