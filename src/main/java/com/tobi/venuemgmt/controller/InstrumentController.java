package com.tobi.venuemgmt.controller;

import com.tobi.venuemgmt.model.Instrument;
import com.tobi.venuemgmt.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    // GET all instruments
    @GetMapping
    public List<Instrument> getAllInstruments() {
        return instrumentService.findAllInstruments();
    }

    // GET a single instrument by ID
    @GetMapping("/{id}")
    public ResponseEntity<Instrument> getInstrumentById(@PathVariable Long id) {
        Instrument instrument = instrumentService.findInstrumentById(id);
        return ResponseEntity.ok(instrument);  
    }

    // GET a single instrument by venue ID
    @GetMapping("/venue/{venueId}")
    public List<Instrument> getInstrumentsByVenueId(@PathVariable Long venueId) {
        return instrumentService.findInstrumentsByVenueId(venueId);
    }

    // POST a new instrument
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instrument createInstrument(@RequestBody Instrument instrument) {
        return instrumentService.saveInstrument(instrument);
    }

    // PUT to update an instrument
    @PutMapping("/{id}")
    public ResponseEntity<Instrument> updateInstrument(@PathVariable Long id, @RequestBody Instrument instrumentDetails) {
        Instrument instrument = instrumentService.findInstrumentById(id);
        instrument.setName(instrumentDetails.getName());
        instrument.setType(instrumentDetails.getType());
        instrument.setVenue(instrumentDetails.getVenue());
        Instrument updatedInstrument = instrumentService.saveInstrument(instrument);
        return ResponseEntity.ok(updatedInstrument);
    }

    // DELETE an instrument by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.noContent().build();
    }
}
