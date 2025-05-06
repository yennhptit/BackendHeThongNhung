package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.DriverStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Driver} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DriverResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /drivers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DriverCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DriverStatus
     */
    public static class DriverStatusFilter extends Filter<DriverStatus> {

        public DriverStatusFilter() {}

        public DriverStatusFilter(DriverStatusFilter filter) {
            super(filter);
        }

        @Override
        public DriverStatusFilter copy() {
            return new DriverStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter driverId;

    private StringFilter licenseNumber;

    private InstantFilter createdAt;

    private DriverStatusFilter status;

    private LongFilter tripId;

    private LongFilter violationId;

    private Boolean distinct;

    public DriverCriteria() {}

    public DriverCriteria(DriverCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.driverId = other.driverId == null ? null : other.driverId.copy();
        this.licenseNumber = other.licenseNumber == null ? null : other.licenseNumber.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.violationId = other.violationId == null ? null : other.violationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DriverCriteria copy() {
        return new DriverCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDriverId() {
        return driverId;
    }

    public StringFilter rfidUid() {
        if (driverId == null) {
            driverId = new StringFilter();
        }
        return driverId;
    }

    public void setDriverId(StringFilter driverId) {
        this.driverId = driverId;
    }

    public StringFilter getLicenseNumber() {
        return licenseNumber;
    }

    public StringFilter licenseNumber() {
        if (licenseNumber == null) {
            licenseNumber = new StringFilter();
        }
        return licenseNumber;
    }

    public void setLicenseNumber(StringFilter licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public DriverStatusFilter getStatus() {
        return status;
    }

    public DriverStatusFilter status() {
        if (status == null) {
            status = new DriverStatusFilter();
        }
        return status;
    }

    public void setStatus(DriverStatusFilter status) {
        this.status = status;
    }

    public LongFilter getTripId() {
        return tripId;
    }

    public LongFilter tripId() {
        if (tripId == null) {
            tripId = new LongFilter();
        }
        return tripId;
    }

    public void setTripId(LongFilter tripId) {
        this.tripId = tripId;
    }

    public LongFilter getViolationId() {
        return violationId;
    }

    public LongFilter violationId() {
        if (violationId == null) {
            violationId = new LongFilter();
        }
        return violationId;
    }

    public void setViolationId(LongFilter violationId) {
        this.violationId = violationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DriverCriteria that = (DriverCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(driverId, that.driverId) &&
            Objects.equals(licenseNumber, that.licenseNumber) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(violationId, that.violationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, driverId, licenseNumber, createdAt, status, tripId, violationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (driverId != null ? "rfidUid=" + driverId + ", " : "") +
            (licenseNumber != null ? "licenseNumber=" + licenseNumber + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (violationId != null ? "violationId=" + violationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
