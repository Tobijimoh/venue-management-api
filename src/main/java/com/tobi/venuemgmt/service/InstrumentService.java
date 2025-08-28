package com.tobi.venuemgmt.service;

import com.tobi.venuemgmt.model.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tobi.venuemgmt.repository.InstrumentRepository;
import com.tobi.venuemgmt.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    @Autowired
    public InstrumentService(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    public List<Instrument> findAllInstruments() {
        return instrumentRepository.findAll();
    }

    public Instrument findInstrumentById(Long id) {
        return instrumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument with ID " + id + " not found."));
    }

    public Instrument saveInstrument(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    public void deleteInstrument(Long id) {
        instrumentRepository.deleteById(id);
    }

    public List<Instrument> findInstrumentsByVenueId(Long venueId) {
        return instrumentRepository.findByVenueId(venueId);
    }
}
