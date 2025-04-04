package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Checkin} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CheckinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /checkins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckinCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter checkinTime;

    private InstantFilter checkoutTime;

    private BooleanFilter faceVerified;

    private LongFilter tripId;

    private Boolean distinct;

    public CheckinCriteria() {}

    public CheckinCriteria(CheckinCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.checkinTime = other.checkinTime == null ? null : other.checkinTime.copy();
        this.checkoutTime = other.checkoutTime == null ? null : other.checkoutTime.copy();
        this.faceVerified = other.faceVerified == null ? null : other.faceVerified.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CheckinCriteria copy() {
        return new CheckinCriteria(this);
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

    public InstantFilter getCheckinTime() {
        return checkinTime;
    }

    public InstantFilter checkinTime() {
        if (checkinTime == null) {
            checkinTime = new InstantFilter();
        }
        return checkinTime;
    }

    public void setCheckinTime(InstantFilter checkinTime) {
        this.checkinTime = checkinTime;
    }

    public InstantFilter getCheckoutTime() {
        return checkoutTime;
    }

    public InstantFilter checkoutTime() {
        if (checkoutTime == null) {
            checkoutTime = new InstantFilter();
        }
        return checkoutTime;
    }

    public void setCheckoutTime(InstantFilter checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public BooleanFilter getFaceVerified() {
        return faceVerified;
    }

    public BooleanFilter faceVerified() {
        if (faceVerified == null) {
            faceVerified = new BooleanFilter();
        }
        return faceVerified;
    }

    public void setFaceVerified(BooleanFilter faceVerified) {
        this.faceVerified = faceVerified;
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
        final CheckinCriteria that = (CheckinCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(checkinTime, that.checkinTime) &&
            Objects.equals(checkoutTime, that.checkoutTime) &&
            Objects.equals(faceVerified, that.faceVerified) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, checkinTime, checkoutTime, faceVerified, tripId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckinCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (checkinTime != null ? "checkinTime=" + checkinTime + ", " : "") +
            (checkoutTime != null ? "checkoutTime=" + checkoutTime + ", " : "") +
            (faceVerified != null ? "faceVerified=" + faceVerified + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
