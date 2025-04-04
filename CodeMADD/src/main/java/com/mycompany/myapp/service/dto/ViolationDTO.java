package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ViolationType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Violation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViolationDTO implements Serializable {

    private Long id;

    private ViolationType type;

    private Float value;

    private Instant timestamp;

    private TripDTO trip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViolationType getType() {
        return type;
    }

    public void setType(ViolationType type) {
        this.type = type;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViolationDTO)) {
            return false;
        }

        ViolationDTO violationDTO = (ViolationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, violationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViolationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            ", timestamp='" + getTimestamp() + "'" +
            ", trip=" + getTrip() +
            "}";
    }
}
