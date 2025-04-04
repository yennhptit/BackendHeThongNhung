package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Checkin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckinDTO implements Serializable {

    private Long id;

    private Instant checkinTime;

    private Instant checkoutTime;

    private Boolean faceVerified;

    private TripDTO trip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Instant checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Instant getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Instant checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Boolean getFaceVerified() {
        return faceVerified;
    }

    public void setFaceVerified(Boolean faceVerified) {
        this.faceVerified = faceVerified;
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
        if (!(o instanceof CheckinDTO)) {
            return false;
        }

        CheckinDTO checkinDTO = (CheckinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, checkinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckinDTO{" +
            "id=" + getId() +
            ", checkinTime='" + getCheckinTime() + "'" +
            ", checkoutTime='" + getCheckoutTime() + "'" +
            ", faceVerified='" + getFaceVerified() + "'" +
            ", trip=" + getTrip() +
            "}";
    }
}
