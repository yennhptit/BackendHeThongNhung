package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DriverStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rfid_uid")
    private String rfidUid;

    @Column(name = "license_number")
    private String licenseNumber;

    @Lob
    @Column(name = "face_data")
    private String faceData;

    @Column(name = "created_at")
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DriverStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    @JsonIgnoreProperties(value = { "driver", "vehicle", "checkin" }, allowSetters = true)
    private Set<Trip> trips = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @JsonIgnoreProperties(value = { "trip" }, allowSetters = true)
    private Set<Violation> violations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Driver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Driver name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfidUid() {
        return this.rfidUid;
    }

    public Driver rfidUid(String rfidUid) {
        this.setRfidUid(rfidUid);
        return this;
    }

    public void setRfidUid(String rfidUid) {
        this.rfidUid = rfidUid;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public Driver licenseNumber(String licenseNumber) {
        this.setLicenseNumber(licenseNumber);
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getFaceData() {
        return this.faceData;
    }

    public Driver faceData(String faceData) {
        this.setFaceData(faceData);
        return this;
    }

    public void setFaceData(String faceData) {
        this.faceData = faceData;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Driver createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public DriverStatus getStatus() {
        return this.status;
    }

    public Driver status(DriverStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Set<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(Set<Trip> trips) {
        if (this.trips != null) {
            this.trips.forEach(i -> i.setDriver(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setDriver(this));
        }
        this.trips = trips;
    }

    public Driver trips(Set<Trip> trips) {
        this.setTrips(trips);
        return this;
    }

    public Driver addTrip(Trip trip) {
        this.trips.add(trip);
        trip.setDriver(this);
        return this;
    }

    public Driver removeTrip(Trip trip) {
        this.trips.remove(trip);
        trip.setDriver(null);
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

    public Driver violations(Set<Violation> violations) {
        this.setViolations(violations);
        return this;
    }

    public Driver addViolation(Violation violation) {
        this.violations.add(violation);
        violation.setTrip(this);
        return this;
    }

    public Driver removeViolation(Violation violation) {
        this.violations.remove(violation);
        violation.setTrip(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return getId() != null && getId().equals(((Driver) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rfidUid='" + getRfidUid() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", faceData='" + getFaceData() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
