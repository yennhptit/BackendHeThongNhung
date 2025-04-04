package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ViolationType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Violation} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ViolationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /violations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViolationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ViolationType
     */
    public static class ViolationTypeFilter extends Filter<ViolationType> {

        public ViolationTypeFilter() {}

        public ViolationTypeFilter(ViolationTypeFilter filter) {
            super(filter);
        }

        @Override
        public ViolationTypeFilter copy() {
            return new ViolationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter value;

    private InstantFilter timestamp;

    private ViolationTypeFilter type;

    private LongFilter tripId;

    private Boolean distinct;

    public ViolationCriteria() {}

    public ViolationCriteria(ViolationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ViolationCriteria copy() {
        return new ViolationCriteria(this);
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

    public FloatFilter getValue() {
        return value;
    }

    public FloatFilter value() {
        if (value == null) {
            value = new FloatFilter();
        }
        return value;
    }

    public void setValue(FloatFilter value) {
        this.value = value;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            timestamp = new InstantFilter();
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public ViolationTypeFilter getType() {
        return type;
    }

    public ViolationTypeFilter type() {
        if (type == null) {
            type = new ViolationTypeFilter();
        }
        return type;
    }

    public void setType(ViolationTypeFilter type) {
        this.type = type;
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
        final ViolationCriteria that = (ViolationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(type, that.type) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, timestamp, type, tripId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViolationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
