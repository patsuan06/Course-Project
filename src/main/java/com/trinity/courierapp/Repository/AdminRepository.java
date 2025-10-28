package com.trinity.courierapp.Repository;

import com.trinity.courierapp.Entity.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
}
