import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDriver } from 'app/shared/model/driver.model';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { getEntities as getVehicles } from 'app/entities/vehicle/vehicle.reducer';
import { ICheckin } from 'app/shared/model/checkin.model';
import { getEntities as getCheckins } from 'app/entities/checkin/checkin.reducer';
import { ITrip } from 'app/shared/model/trip.model';
import { TripStatus } from 'app/shared/model/enumerations/trip-status.model';
import { getEntity, updateEntity, createEntity, reset } from './trip.reducer';

export const TripUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const drivers = useAppSelector(state => state.driver.entities);
  const vehicles = useAppSelector(state => state.vehicle.entities);
  const checkins = useAppSelector(state => state.checkin.entities);
  const tripEntity = useAppSelector(state => state.trip.entity);
  const loading = useAppSelector(state => state.trip.loading);
  const updating = useAppSelector(state => state.trip.updating);
  const updateSuccess = useAppSelector(state => state.trip.updateSuccess);
  const tripStatusValues = Object.keys(TripStatus);

  const handleClose = () => {
    navigate('/trip' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDrivers({}));
    dispatch(getVehicles({}));
    dispatch(getCheckins({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...tripEntity,
      ...values,
      driver: drivers.find(it => it.id.toString() === values.driver.toString()),
      vehicle: vehicles.find(it => it.id.toString() === values.vehicle.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          status: 'ONGOING',
          ...tripEntity,
          startTime: convertDateTimeFromServer(tripEntity.startTime),
          endTime: convertDateTimeFromServer(tripEntity.endTime),
          driver: tripEntity?.driver?.id,
          vehicle: tripEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="backendHeThongNhungApp.trip.home.createOrEditLabel" data-cy="TripCreateUpdateHeading">
            <Translate contentKey="backendHeThongNhungApp.trip.home.createOrEditLabel">Create or edit a Trip</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="trip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label="Start Time"
                id="trip-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="End Time"
                id="trip-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Status" id="trip-status" name="status" data-cy="status" type="select">
                {tripStatusValues.map(tripStatus => (
                  <option value={tripStatus} key={tripStatus}>
                    {tripStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="trip-driver" name="driver" data-cy="driver" label="Driver" type="select">
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="trip-vehicle" name="vehicle" data-cy="vehicle" label="Vehicle" type="select">
                <option value="" key="0" />
                {vehicles
                  ? vehicles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TripUpdate;
