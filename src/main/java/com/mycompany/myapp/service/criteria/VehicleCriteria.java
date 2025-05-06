package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.VehicleStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Vehicle} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering VehicleStatus
     */
    public static class VehicleStatusFilter extends Filter<VehicleStatus> {

        public VehicleStatusFilter() {}

        public VehicleStatusFilter(VehicleStatusFilter filter) {
            super(filter);
        }

        @Override
        public VehicleStatusFilter copy() {
            return new VehicleStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter licensePlate;

    private StringFilter model;

    private VehicleStatusFilter status;

    private BooleanFilter isDelete;

    private LongFilter tripId;

    private Boolean distinct;

    public VehicleCriteria() {}

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.licensePlate = other.licensePlate == null ? null : other.licensePlate.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.isDelete = other.isDelete == null ? null : other.isDelete.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
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

    public StringFilter getLicensePlate() {
        return licensePlate;
    }

    public StringFilter licensePlate() {
        if (licensePlate == null) {
            licensePlate = new StringFilter();
        }
        return licensePlate;
    }

    public void setLicensePlate(StringFilter licensePlate) {
        this.licensePlate = licensePlate;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public VehicleStatusFilter getStatus() {
        return status;
    }

    public VehicleStatusFilter status() {
        if (status == null) {
            status = new VehicleStatusFilter();
        }
        return status;
    }

    public void setStatus(VehicleStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getIsDelete() {
        return isDelete;
    }

    public BooleanFilter isDelete() {
        if (isDelete == null) {
            isDelete = new BooleanFilter();
        }
        return isDelete;
    }

    public void setIsDelete(BooleanFilter isDelete) {
        this.isDelete = isDelete;
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
        final VehicleCriteria that = (VehicleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(licensePlate, that.licensePlate) &&
            Objects.equals(model, that.model) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isDelete, that.isDelete) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licensePlate, model, status, isDelete, tripId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (licensePlate != null ? "licensePlate=" + licensePlate + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (isDelete != null ? "isDelete=" + isDelete + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
