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
        return instrumentService.findInstrumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());  
    }

    // POST a new instrument
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instrument createInstrument(@RequestBody Instrument instrument) {
        return instrumentService.saveInstrument(instrument);
    }
}
