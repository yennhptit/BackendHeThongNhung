package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TripStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TripStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @JsonIgnoreProperties(value = { "trip" }, allowSetters = true)
    private Set<Checkin> checkins = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @JsonIgnoreProperties(value = { "trip" }, allowSetters = true)
    private Set<Violation> violations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trips" }, allowSetters = true)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trips" }, allowSetters = true)
    private Vehicle vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Trip startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Trip endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public TripStatus getStatus() {
        return this.status;
    }

    public Trip status(TripStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public Set<Checkin> getCheckins() {
        return this.checkins;
    }

    public void setCheckins(Set<Checkin> checkins) {
        if (this.checkins != null) {
            this.checkins.forEach(i -> i.setTrip(null));
        }
        if (checkins != null) {
            checkins.forEach(i -> i.setTrip(this));
        }
        this.checkins = checkins;
    }

    public Trip checkins(Set<Checkin> checkins) {
        this.setCheckins(checkins);
        return this;
    }

    public Trip addCheckins(Checkin checkin) {
        this.checkins.add(checkin);
        checkin.setTrip(this);
        return this;
    }

    public Trip removeCheckins(Checkin checkin) {
        this.checkins.remove(checkin);
        checkin.setTrip(null);
        return this;
    }

    public Set<Violation> getViolations() {
        return this.violations;
    }

    public void setViolations(Set<Violation> violations) {
        if (this.violations != null) {
            this.violations.forEach(i -> i.setTrip(null));
        }
        if (violations != null) {
            violations.forEach(i -> i.setTrip(this));
        }
        this.violations = violations;
    }

    public Trip violations(Set<Violation> violations) {
        this.setViolations(violations);
        return this;
    }

    public Trip addViolations(Violation violation) {
        this.violations.add(violation);
        violation.setTrip(this);
        return this;
    }

    public Trip removeViolations(Violation violation) {
        this.violations.remove(violation);
        violation.setTrip(null);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Trip driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Trip vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return getId() != null && getId().equals(((Trip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
