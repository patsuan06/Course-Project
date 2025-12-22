package com.trinity.courierapp.Repository;


import com.trinity.courierapp.Entity.Courier;
import com.trinity.courierapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByFullName(String fullName);

    String fullName(String fullName);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByCourier(Courier courier);
}
