package com.tobi.venuemgmt.venue;

import com.tobi.venuemgmt.model.BaseEntity;
import com.tobi.venuemgmt.model.Instrument;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Venue extends BaseEntity {
    private String name;
    private String location;

    @Enumerated(EnumType.STRING)
    private VenueType type;

    @Enumerated(EnumType.STRING)
    private VenueStatus status;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instrument> instruments;

}
