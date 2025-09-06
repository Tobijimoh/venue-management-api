package com.tobi.venuemgmt.repository;

import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.model.VenueType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findByTypeIgnoreCase(VenueType type);
    
    List<Venue> findByStatus(String status);

    List<Venue> findByNameContainingIgnoreCase(String name);

    List<Venue> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT v FROM Venue v WHERE v.name LIKE %:name%")
    List<Venue> findByNamePartialMatch(@Param("name") String name);

    @Query(value = "SELECT location, COUNT(*) FROM venue GROUP BY location", nativeQuery = true)
    List<Object[]> countVenuesByLocation();
}
