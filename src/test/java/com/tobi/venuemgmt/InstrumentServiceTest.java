package com.tobi.venuemgmt;

import com.tobi.venuemgmt.exception.ResourceAlreadyExistsException;
import com.tobi.venuemgmt.exception.ResourceNotFoundException;
import com.tobi.venuemgmt.model.Instrument;
import com.tobi.venuemgmt.model.InstrumentType;
import com.tobi.venuemgmt.model.Venue;
import com.tobi.venuemgmt.repository.InstrumentRepository;
import com.tobi.venuemgmt.service.InstrumentService;

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
public class InstrumentServiceTest {

    @Mock
    private InstrumentRepository instrumentRepository;

    @InjectMocks
    private InstrumentService instrumentService;

    // Helper method to create a sample instrument
    private Instrument createSampleInstrument() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("NYSE");

        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setSymbol("AAPL");
        instrument.setName("Apple Inc.");
        instrument.setType(InstrumentType.STOCK);
        instrument.setVenue(venue);
        return instrument;
    }

    @Test
    public void whenFindAll_thenReturnAllInstruments() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findAll()).thenReturn(List.of(instrument));

        List<Instrument> result = instrumentService.findAllInstruments();

        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        verify(instrumentRepository, times(1)).findAll();
    }

    @Test
    public void whenFindById_thenInstrumentIsReturned() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));

        Instrument result = instrumentService.findInstrumentById(1L);

        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
    }

    @Test
    public void whenFindByIdNotFound_thenThrowException() {
        when(instrumentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> instrumentService.findInstrumentById(1L));
    }

    @Test
    void whenSaveNewInstrument_thenInstrumentIsSaved() {
        Instrument newInstrument = new Instrument();
        newInstrument.setSymbol("TSLA");
        newInstrument.setName("Tesla Inc.");
        newInstrument.setType(InstrumentType.STOCK);

        when(instrumentRepository.findBySymbolContainingIgnoreCase("TSLA")).thenReturn(Collections.emptyList());
        when(instrumentRepository.save(any(Instrument.class))).thenAnswer(invocation -> {
            Instrument i = invocation.getArgument(0);
            i.setId(2L);
            return i;
        });

        Instrument savedInstrument = instrumentService.saveInstrument(newInstrument);

        assertNotNull(savedInstrument.getId());
        assertEquals("TSLA", savedInstrument.getSymbol());
        verify(instrumentRepository, times(1)).save(newInstrument);
    }

    @Test
    void whenSaveDuplicateInstrument_thenThrowException() {
        Instrument existingInstrument = createSampleInstrument();
        Instrument duplicateInstrument = new Instrument();
        duplicateInstrument.setSymbol("AAPL");
        duplicateInstrument.setName("Apple Inc.");

        when(instrumentRepository.findBySymbolContainingIgnoreCase("AAPL"))
                .thenReturn(List.of(existingInstrument));

        assertThrows(ResourceAlreadyExistsException.class, () -> instrumentService.saveInstrument(duplicateInstrument));
    }

    @Test
    void whenUpdateInstrument_thenFieldsAreChanged() {
        Instrument existingInstrument = createSampleInstrument();
        Instrument updatedInstrument = new Instrument();
        updatedInstrument.setSymbol("AAPL");
        updatedInstrument.setName("Apple Updated Inc.");
        updatedInstrument.setType(InstrumentType.BOND);

        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(existingInstrument));
        when(instrumentRepository.save(existingInstrument)).thenReturn(existingInstrument);

        Instrument result = instrumentService.updateInstrument(1L, updatedInstrument);

        assertEquals("Apple Updated Inc.", result.getName());
        assertEquals(InstrumentType.BOND, result.getType());
    }

    @Test
    void whenDeleteInstrument_thenRepositoryDeleteCalled() {
        when(instrumentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(instrumentRepository).deleteById(1L);

        instrumentService.deleteInstrument(1L);

        verify(instrumentRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenFindByType_thenReturnFilteredInstruments() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findByType(InstrumentType.STOCK)).thenReturn(List.of(instrument));

        List<Instrument> result = instrumentService.findInstrumentsByType(InstrumentType.STOCK);

        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
    }

    @Test
    void whenFindBySymbol_thenReturnMatchingInstruments() {
        Instrument instrument = createSampleInstrument();
        when(instrumentRepository.findBySymbolContainingIgnoreCase("AAPL")).thenReturn(List.of(instrument));

        List<Instrument> result = instrumentService.findInstrumentsBySymbol("AAPL");

        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
    }
}
