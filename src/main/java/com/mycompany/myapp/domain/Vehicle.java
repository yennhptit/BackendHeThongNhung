package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.VehicleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "model")
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @JsonIgnoreProperties(value = { "driver", "vehicle", "checkin" }, allowSetters = true)
    private Set<Trip> trips = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public Vehicle licensePlate(String licensePlate) {
        this.setLicensePlate(licensePlate);
        return this;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return this.model;
    }

    public Vehicle model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleStatus getStatus() {
        return this.status;
    }

    public Vehicle status(VehicleStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Set<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(Set<Trip> trips) {
        if (this.trips != null) {
            this.trips.forEach(i -> i.setVehicle(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setVehicle(this));
        }
        this.trips = trips;
    }

    public Vehicle trips(Set<Trip> trips) {
        this.setTrips(trips);
        return this;
    }

    public Vehicle addTrip(Trip trip) {
        this.trips.add(trip);
        trip.setVehicle(this);
        return this;
    }

    public Vehicle removeTrip(Trip trip) {
        this.trips.remove(trip);
        trip.setVehicle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", licensePlate='" + getLicensePlate() + "'" +
            ", model='" + getModel() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
