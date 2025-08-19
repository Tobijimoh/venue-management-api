package com.tobi.venuemgmt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Instrument extends BaseEntity {

    private String symbol;
    private String name;
    private String type;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
