package com.tobi.venuemgmt.service;

import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.model.VenueStatus;
import com.tobi.venuemgmt.exception.ResourceAlreadyExistsException;
import com.tobi.venuemgmt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tobi.venuemgmt.repository.VenueRepository;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    public Venue findVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue with ID " + id + " not found."));
    }

    public Venue saveVenue(Venue venue) {
        if (venue.getId() == null) {
            List<Venue> existingVenues = venueRepository.findByNameContainingIgnoreCase(venue.getName());
            if (!existingVenues.isEmpty()) {
                throw new ResourceAlreadyExistsException("A venue with the new '" + venue.getName() + "' already exists.");
            }
            venue.setStatus(VenueStatus.OPEN);
        }
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    public Venue updateVenueStatus(Long id, VenueStatus newStatus) {
        Venue venue = findVenueById(id);
        venue.setStatus(newStatus);
        return venueRepository.save(venue); 
    }

    public List<Venue> findVenuesByType(String type) {
        return venueRepository.findByTypeIgnoreCase(type);
    }

    public List<Venue> findVenuesByName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }
}
