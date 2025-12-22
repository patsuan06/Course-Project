package com.trinity.courierapp.Repository;

import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findAllByUser(User user);
}
