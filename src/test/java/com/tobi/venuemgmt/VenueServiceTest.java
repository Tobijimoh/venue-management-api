package com.tobi.venuemgmt;

import com.tobi.venuemgmt.exception.ResourceAlreadyExistsException;
import com.tobi.venuemgmt.exception.ResourceNotFoundException;
import com.tobi.venuemgmt.venue.Venue;
import com.tobi.venuemgmt.venue.VenueRepository;
import com.tobi.venuemgmt.venue.VenueService;
import com.tobi.venuemgmt.venue.VenueStatus;
import com.tobi.venuemgmt.venue.VenueType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    // Helper method to create a sample venue
    private Venue createSampleVenue() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("NYSE");
        venue.setLocation("New York");
        venue.setType(VenueType.RM);
        venue.setStatus(VenueStatus.OPEN);
        return venue;
    }

    @Test
    public void whenFindAll_thenReturnAllVenues() {
        Venue venue = createSampleVenue();
        when(venueRepository.findAll()).thenReturn(List.of(venue));

        List<Venue> result = venueService.findAllVenues();

        assertEquals(1, result.size());
        assertEquals("NYSE", result.get(0).getName());
        verify(venueRepository, times(1)).findAll();
    }

    @Test
    public void whenFindById_thenVenueIsReturned() {
        Venue venue = createSampleVenue();
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        Venue result = venueService.findVenueById(1L);

        assertNotNull(result);
        assertEquals("NYSE", result.getName());
    }

    @Test
    public void whenFindByIdNotFound_thenThrowException() {
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> venueService.findVenueById(1L));
    }

    @Test
    void whenSaveNewVenue_thenStatusIsSetAndSaved() {
        Venue newVenue = new Venue();
        newVenue.setName("LSE");
        newVenue.setLocation("London");
        newVenue.setType(VenueType.RM);

        when(venueRepository.findByNameContainingIgnoreCase("LSE")).thenReturn(Collections.emptyList());
        when(venueRepository.save(any(Venue.class))).thenAnswer(invocation -> {
            Venue v = invocation.getArgument(0);
            v.setId(2L);
            return v;
        });

        Venue savedVenue = venueService.saveVenue(newVenue);

        assertNotNull(savedVenue.getId());
        assertEquals(VenueStatus.OPEN, savedVenue.getStatus());
        verify(venueRepository, times(1)).save(newVenue);
    }

    @Test
    void whenSaveDuplicateVenue_thenThrowException() {
        Venue existingVenue = createSampleVenue();
        Venue newVenueWitchDuplicateName = new Venue();
        newVenueWitchDuplicateName.setName("NYSE");
        newVenueWitchDuplicateName.setLocation("New York");
        when(venueRepository.findByNameContainingIgnoreCase("NYSE")).thenReturn(List.of(existingVenue));

        assertThrows(ResourceAlreadyExistsException.class, () -> venueService.saveVenue(newVenueWitchDuplicateName));
    }

    @Test
    void whenUpdateVenue_thenFieldsAreChanged() {
        Venue existingVenue = createSampleVenue();
        Venue updatedVenue = new Venue();
        updatedVenue.setName("Updated NYSE");
        updatedVenue.setLocation("Updated Location");
        updatedVenue.setType(VenueType.MTF);

        when(venueRepository.findById(1L)).thenReturn(Optional.of(existingVenue));
        when(venueRepository.save(existingVenue)).thenReturn(existingVenue);

        Venue result = venueService.updateVenue(1L, updatedVenue);

        assertEquals("Updated NYSE", result.getName());
        assertEquals("Updated Location", result.getLocation());
        assertEquals(VenueType.MTF, result.getType());
    }

    @Test
    void whenUpdateVenueStatus_thenStatusIsUpdated() {
        Venue venue = createSampleVenue();
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(venueRepository.save(venue)).thenReturn(venue);

        Venue result = venueService.updateVenueStatus(1L, VenueStatus.CLOSED);

        assertEquals(VenueStatus.CLOSED, result.getStatus());
    }

    @Test
    void whenDeleteVenue_thenRepositoryDeleteCalled() {
        doNothing().when(venueRepository).deleteById(1L);

        venueService.deleteVenue(1L);

        verify(venueRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenFindByType_thenReturnFilteredVenues() {
        Venue venue = createSampleVenue();
        when(venueRepository.findByType(VenueType.RM)).thenReturn(List.of(venue));

        List<Venue> result = venueService.findVenuesByType(VenueType.RM);

        assertEquals(1, result.size());
        assertEquals("NYSE", result.get(0).getName());
    }

    @Test
    void whenFindByName_thenReturnMatchingVenues() {
        Venue venue = createSampleVenue();
        when(venueRepository.findByNameContainingIgnoreCase("NYSE")).thenReturn(List.of(venue));

        List<Venue> result = venueService.findVenuesByName("NYSE");

        assertEquals(1, result.size());
        assertEquals("NYSE", result.get(0).getName());
    }
}
