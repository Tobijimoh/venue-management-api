package com.tobi.venuemgmt.service;

import com.tobi.venuemgmt.model.Instrument;
import com.tobi.venuemgmt.model.InstrumentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tobi.venuemgmt.repository.InstrumentRepository;
import com.tobi.venuemgmt.exception.ResourceAlreadyExistsException;
import com.tobi.venuemgmt.exception.ResourceNotFoundException;

import java.util.List;

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
        List<Instrument> existing = instrumentRepository.findBySymbolContainingIgnoreCase(instrument.getSymbol());
        if (!existing.isEmpty()) {
            throw new ResourceAlreadyExistsException("Instrument with symbol " + instrument.getSymbol() + " already exists.");
        }
        return instrumentRepository.save(instrument);
    }

    /**
     * Updates an existing instrument's descriptive details.
     * This method intentionally does not allow changing the instrument's symbol or
     * its parent venue,
     * as these are considered immutable properties after creation to maintain data
     * integrity.
     *
     * @param id                The ID of the instrument to update.
     * @param instrumentDetails An Instrument object containing the new details.
     * @return The updated and saved Instrument entity.
     * @throws ResourceNotFoundException if no instrument is found with the given
     *                                   ID.
     */
    public Instrument updateInstrument(Long id, Instrument instrumentDetails) {
        // Find the existing instrument or throw an exception if not found.
        Instrument existingInstrument = findInstrumentById(id);

        // Update only the descriptive, mutable fields.
        existingInstrument.setName(instrumentDetails.getName());
        existingInstrument.setType(instrumentDetails.getType());

        // Note: Symbol and Venue are intentionally not updated here.
        // Changing these would be a more complex operation like a delist/relist.
        return instrumentRepository.save(existingInstrument);
    }

    public void deleteInstrument(Long id) {
        // Check if the instrument exists before trying to delete to provide a clear
        // error.
        if (!instrumentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instrument with ID " + id + " not found, cannot delete.");
        }
        instrumentRepository.deleteById(id);
    }

    public List<Instrument> findInstrumentsByVenueId(Long venueId) {
        return instrumentRepository.findByVenueId(venueId);
    }

    public List<Instrument> findInstrumentsByType(InstrumentType type) {
        return instrumentRepository.findByType(type);
    }

    public List<Instrument> findInstrumentsBySymbol(String symbol) {
        return instrumentRepository.findBySymbolContainingIgnoreCase(symbol);
    }

}
