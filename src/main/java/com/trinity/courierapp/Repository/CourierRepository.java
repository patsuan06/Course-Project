package com.trinity.courierapp.Repository;

import com.trinity.courierapp.Entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer> {
    Optional<Courier> findByCourierUserFullName(String fullName);
}
