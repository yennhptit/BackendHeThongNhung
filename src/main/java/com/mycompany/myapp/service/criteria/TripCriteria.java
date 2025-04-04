package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.TripStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Trip} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TripResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TripStatus
     */
    public static class TripStatusFilter extends Filter<TripStatus> {

        public TripStatusFilter() {}

        public TripStatusFilter(TripStatusFilter filter) {
            super(filter);
        }

        @Override
        public TripStatusFilter copy() {
            return new TripStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private TripStatusFilter status;

    private LongFilter driverId;

    private LongFilter vehicleId;

    private LongFilter checkinId;

    private Boolean distinct;

    public TripCriteria() {}

    public TripCriteria(TripCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.driverId = other.driverId == null ? null : other.driverId.copy();
        this.vehicleId = other.vehicleId == null ? null : other.vehicleId.copy();
        this.checkinId = other.checkinId == null ? null : other.checkinId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TripCriteria copy() {
        return new TripCriteria(this);
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

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public TripStatusFilter getStatus() {
        return status;
    }

    public TripStatusFilter status() {
        if (status == null) {
            status = new TripStatusFilter();
        }
        return status;
    }

    public void setStatus(TripStatusFilter status) {
        this.status = status;
    }

    public LongFilter getDriverId() {
        return driverId;
    }

    public LongFilter driverId() {
        if (driverId == null) {
            driverId = new LongFilter();
        }
        return driverId;
    }

    public void setDriverId(LongFilter driverId) {
        this.driverId = driverId;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            vehicleId = new LongFilter();
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LongFilter getCheckinId() {
        return checkinId;
    }

    public LongFilter checkinId() {
        if (checkinId == null) {
            checkinId = new LongFilter();
        }
        return checkinId;
    }

    public void setCheckinId(LongFilter checkinId) {
        this.checkinId = checkinId;
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
        final TripCriteria that = (TripCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(driverId, that.driverId) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(checkinId, that.checkinId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, status, driverId, vehicleId, checkinId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (driverId != null ? "driverId=" + driverId + ", " : "") +
            (vehicleId != null ? "vehicleId=" + vehicleId + ", " : "") +
            (checkinId != null ? "checkinId=" + checkinId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
