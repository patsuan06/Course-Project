package com.trinity.courierapp.Repository;

import com.trinity.courierapp.Entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer> {
    Optional<Courier> findByCourierUserFullName(String fullName);
    //for future need to check if there are any at all.
//    List<Courier> findByAvailableTrue(); error some
    @Query(
            value = "SELECT * FROM courier_profiles c " +
                    "WHERE ST_DWithin(c.courier_gps, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :radius)",
            nativeQuery = true
    )
    List<Courier> findCouriersWithinRadius(@Param("lat") double lat,
                                           @Param("lng") double lng,
                                           @Param("radius") double radiusInMeters);





//    public interface CourierRepository extends JpaRepository<Courier, Long> {
//
//        @Query(
//                value = """
//            SELECT *
//            FROM courier c
//            WHERE c.status = 'AVAILABLE'
//              AND ST_DWithin(
//                    c.location,
//                    ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
//                    :radius
//              )
//        """,
//                nativeQuery = true
//        )
//        List<Courier> findCouriersWithinRadius(
//                @Param("lat") double lat,
//                @Param("lng") double lng,
//                @Param("radius") double radiusMeters
//        );
//    }

}

