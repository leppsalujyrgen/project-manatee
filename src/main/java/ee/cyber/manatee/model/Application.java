package ee.cyber.manatee.model;


import java.time.OffsetDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ee.cyber.manatee.statemachine.ApplicationState;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationState applicationState;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Candidate candidate;

    @NotNull
    private OffsetDateTime updatedOn;
}
