package com.tobi.venuemgmt.controller;

import com.tobi.venuemgmt.model.Instrument;
import com.tobi.venuemgmt.service.InstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
@Tag(name = "Instrument Management", description = "APIs for creating, retrieving, and managing financial instruments.")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    /**
     * Retrieves all instruments available across all venues.
     */
    @GetMapping
    @Operation(summary = "Get all instruments", description = "Returns a list of all financial instruments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<Instrument>> getAllInstruments() {
        return ResponseEntity.ok(instrumentService.findAllInstruments());
    }

    /**
     * Finds a single instrument by its unique ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a single instrument by ID", description = "Returns a single instrument, if found.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instrument"),
        @ApiResponse(responseCode = "404", description = "Instrument not found with the given ID")
    })
    public ResponseEntity<Instrument> getInstrumentById(@Parameter(description = "ID of the instrument to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(instrumentService.findInstrumentById(id));
    }

    /**
     * Finds all instruments that belong to a specific venue.
     */
    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get instruments by Venue ID", description = "Returns a list of all instruments associated with a specific venue.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "404", description = "Venue not found with the given ID, so no instruments could be retrieved")
    })
    public ResponseEntity<List<Instrument>> getInstrumentsByVenueId(@Parameter(description = "ID of the venue to filter instruments by") @PathVariable Long venueId) {
        return ResponseEntity.ok(instrumentService.findInstrumentsByVenueId(venueId));
    }


    /**
     * Creates a new instrument. The request body must contain the instrument details,
     * including a valid venueId to associate it with.
     */
    @PostMapping
    @Operation(summary = "Create a new instrument", description = "Creates a new financial instrument and associates it with an existing venue.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Instrument created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input, e.g., the specified venueId does not exist")
    })
    public ResponseEntity<Instrument> createInstrument(@RequestBody Instrument instrument) {
        Instrument savedInstrument = instrumentService.saveInstrument(instrument);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInstrument);
    }

    /**
     * Updates an existing instrument's details.
     * The service layer handles the logic of finding and updating the entity.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing instrument", description = "Updates the details for an existing instrument identified by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instrument updated successfully"),
        @ApiResponse(responseCode = "404", description = "Instrument not found with the given ID")
    })
    public ResponseEntity<Instrument> updateInstrument(
            @Parameter(description = "ID of the instrument to update") @PathVariable Long id, 
            @RequestBody Instrument instrumentDetails) {
        // Best practice: The service handles the fetch-and-update logic.
        Instrument updatedInstrument = instrumentService.updateInstrument(id, instrumentDetails);
        return ResponseEntity.ok(updatedInstrument);
    }

    /**
     * Deletes an instrument by its ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an instrument", description = "Deletes an instrument from the database by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Instrument deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Instrument not found with the given ID")
    })
    public ResponseEntity<Void> deleteInstrument(@Parameter(description = "ID of the instrument to delete") @PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.noContent().build();
    }
}

