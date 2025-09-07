package com.tobi.venuemgmt.instrument;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tobi.venuemgmt.common.BaseEntity;
import com.tobi.venuemgmt.venue.Venue;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Instrument extends BaseEntity {

    private String symbol;
    private String name;

    @Enumerated(EnumType.STRING)
    private InstrumentType type;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
