package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Checkin.
 */
@Entity
@Table(name = "checkin")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Checkin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "checkin_time")
    private Instant checkinTime;

    @Column(name = "checkout_time")
    private Instant checkoutTime;

    @Column(name = "face_verified")
    private Boolean faceVerified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "checkins", "violations", "driver", "vehicle" }, allowSetters = true)
    private Trip trip;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Checkin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCheckinTime() {
        return this.checkinTime;
    }

    public Checkin checkinTime(Instant checkinTime) {
        this.setCheckinTime(checkinTime);
        return this;
    }

    public void setCheckinTime(Instant checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Instant getCheckoutTime() {
        return this.checkoutTime;
    }

    public Checkin checkoutTime(Instant checkoutTime) {
        this.setCheckoutTime(checkoutTime);
        return this;
    }

    public void setCheckoutTime(Instant checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Boolean getFaceVerified() {
        return this.faceVerified;
    }

    public Checkin faceVerified(Boolean faceVerified) {
        this.setFaceVerified(faceVerified);
        return this;
    }

    public void setFaceVerified(Boolean faceVerified) {
        this.faceVerified = faceVerified;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Checkin trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkin)) {
            return false;
        }
        return getId() != null && getId().equals(((Checkin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Checkin{" +
            "id=" + getId() +
            ", checkinTime='" + getCheckinTime() + "'" +
            ", checkoutTime='" + getCheckoutTime() + "'" +
            ", faceVerified='" + getFaceVerified() + "'" +
            "}";
    }
}
