package com.tobi.venuemgmt.service;

import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.model.VenueStatus;
import com.tobi.venuemgmt.model.VenueType;
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

        checkDuplicateVenue(venue);

        if (venue.getId() == null) {
            venue.setStatus(VenueStatus.OPEN);
        }
        return venueRepository.save(venue);
    }

     public Venue updateVenue(Long id, Venue updatedVenue) {
        Venue venue = findVenueById(id); // Throws an exception if not found

        checkDuplicateVenue(updatedVenue);

        venue.setName(updatedVenue.getName());
        venue.setLocation(updatedVenue.getLocation());
        venue.setType(updatedVenue.getType());
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

    public List<Venue> findVenuesByType(VenueType type) {
        return venueRepository.findByTypeIgnoreCase(type);
    }

    public List<Venue> findVenuesByName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }

    //Helper method to check for duplicate venue names
    private void checkDuplicateVenue(Venue venue) {
        List<Venue> existingVenues = venueRepository.findByNameContainingIgnoreCase(venue.getName());
        if (!existingVenues.isEmpty()) {
            // Check if the found venue is the same as the one being updated
            if (venue.getId() == null || (existingVenues.size() > 1 || existingVenues.get(0).getId() != venue.getId())) {
                throw new ResourceAlreadyExistsException("A venue with the name '" + venue.getName() + "' already exists.");
            }
        }
    }
}
