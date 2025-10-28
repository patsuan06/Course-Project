package com.trinity.courierapp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @ColumnDefault("nextval('admin_admin_id_seq')")
    @Column(name = "admin_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "password", length = 60)
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}