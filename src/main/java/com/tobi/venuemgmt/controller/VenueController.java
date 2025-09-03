package com.tobi.venuemgmt.controller;

import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.model.VenueStatus;
import com.tobi.venuemgmt.service.VenueService;
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
@RequestMapping("/api/venues")
@Tag(name = "Venue Management", description = "APIs for creating, retrieving, and managing trading venues.")
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    /**
     * Retrieves all venues. Can be filtered by type or name using request parameters.
     * If no parameters are provided, it returns all venues.
     */
    @GetMapping
    @Operation(summary = "Get all venues or filter by properties", description = "Returns a list of all venues. Optionally filters by 'type' or 'name'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<Venue>> getAllVenues(
            @Parameter(description = "Filter venues by type (e.g., EQUITY, FX)") @RequestParam(required = false) String type,
            @Parameter(description = "Filter venues by a case-insensitive partial name match") @RequestParam(required = false) String name) {
        
        if (type != null) {
            return ResponseEntity.ok(venueService.findVenuesByType(type));
        }
        if (name != null) {
            return ResponseEntity.ok(venueService.findVenuesByName(name));
        }
        return ResponseEntity.ok(venueService.findAllVenues());
    }

    /**
     * Finds a single venue by its unique ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a single venue by ID", description = "Returns a single venue, if found.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved venue"),
        @ApiResponse(responseCode = "404", description = "Venue not found with the given ID")
    })
    public ResponseEntity<Venue> getVenueById(@Parameter(description = "ID of the venue to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(venueService.findVenueById(id));
    }

    /**
     * Creates a new venue. The request body should contain the venue details.
     */
    @PostMapping
    @Operation(summary = "Create a new venue", description = "Creates a new venue and returns the created entity with its new ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venue created successfully")
    })
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        Venue savedVenue = venueService.saveVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVenue);
    }

    /**
     * Updates an existing venue's details.
     * The entire venue object is updated with the new data.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing venue", description = "Updates all details for an existing venue identified by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue updated successfully"),
        @ApiResponse(responseCode = "404", description = "Venue not found with the given ID")
    })
    public ResponseEntity<Venue> updateVenue(
            @Parameter(description = "ID of the venue to update") @PathVariable Long id, 
            @RequestBody Venue updatedVenue) {
        Venue venue = venueService.updateVenue(id, updatedVenue);
        return ResponseEntity.ok(venue);
    }

    /**
     * Partially updates a venue's status. This is more efficient than a full PUT
     * for just changing the operational status.
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update a venue's status", description = "Partially updates only the status of a specific venue.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Venue not found with the given ID")
    })
    public ResponseEntity<Venue> updateVenueStatus(
            @Parameter(description = "ID of the venue to update") @PathVariable Long id, 
            @Parameter(description = "The new status for the venue") @RequestParam VenueStatus status) {
        Venue updatedVenue = venueService.updateVenueStatus(id, status);
        return ResponseEntity.ok(updatedVenue);
    }

    /**
     * Deletes a venue by its ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a venue", description = "Deletes a venue from the database by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Venue deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Venue not found with the given ID")
    })
    public ResponseEntity<Void> deleteVenue(@Parameter(description = "ID of the venue to delete") @PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
