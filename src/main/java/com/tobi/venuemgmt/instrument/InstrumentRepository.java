package com.tobi.venuemgmt.instrument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    List<Instrument> findByVenueId(Long venueId);

    List<Instrument> findBySymbolContainingIgnoreCase(String symbol);

    List<Instrument> findByType(InstrumentType type);
}
