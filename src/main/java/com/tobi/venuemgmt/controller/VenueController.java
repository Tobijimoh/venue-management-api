package com.tobi.venuemgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.model.VenueStatus;
import com.tobi.venuemgmt.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // GET all venues
    @GetMapping
    public List<Venue> getAllVenues() {
        return venueService.findAllVenues();
    }

    // GET a single venue by ID
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return venueService.findVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET venues by type
    @GetMapping("/search")
    public List<Venue> getVenueByType(@RequestParam String type) {
        return venueService.findVenuesByType(type);
    }

    // POST a new venue
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venue createVenue(@RequestBody Venue venue) {
        return venueService.saveVenue(venue);
    }

    // PUT to update a venue's status
    @PutMapping("/{id}/status")
    public ResponseEntity<Venue> updateVenueStatus(@PathVariable Long id, @RequestBody VenueStatus status) {
        return venueService.updateVenueStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a venue by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
