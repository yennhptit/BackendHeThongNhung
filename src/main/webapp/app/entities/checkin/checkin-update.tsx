import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrip } from 'app/shared/model/trip.model';
import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { ICheckin } from 'app/shared/model/checkin.model';
import { getEntity, updateEntity, createEntity, reset } from './checkin.reducer';

export const CheckinUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const checkinEntity = useAppSelector(state => state.checkin.entity);
  const loading = useAppSelector(state => state.checkin.loading);
  const updating = useAppSelector(state => state.checkin.updating);
  const updateSuccess = useAppSelector(state => state.checkin.updateSuccess);

  const handleClose = () => {
    navigate('/checkin' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.checkinTime = convertDateTimeToServer(values.checkinTime);
    values.checkoutTime = convertDateTimeToServer(values.checkoutTime);

    const entity = {
      ...checkinEntity,
      ...values,
      trip: trips.find(it => it.id.toString() === values.trip.toString()),
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
          checkinTime: displayDefaultDateTime(),
          checkoutTime: displayDefaultDateTime(),
        }
      : {
          ...checkinEntity,
          checkinTime: convertDateTimeFromServer(checkinEntity.checkinTime),
          checkoutTime: convertDateTimeFromServer(checkinEntity.checkoutTime),
          trip: checkinEntity?.trip?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="backendHeThongNhungApp.checkin.home.createOrEditLabel" data-cy="CheckinCreateUpdateHeading">
            <Translate contentKey="backendHeThongNhungApp.checkin.home.createOrEditLabel">Create or edit a Checkin</Translate>
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
                  id="checkin-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label="Checkin Time"
                id="checkin-checkinTime"
                name="checkinTime"
                data-cy="checkinTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Checkout Time"
                id="checkin-checkoutTime"
                name="checkoutTime"
                data-cy="checkoutTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Face Verified"
                id="checkin-faceVerified"
                name="faceVerified"
                data-cy="faceVerified"
                check
                type="checkbox"
              />
              <ValidatedField id="checkin-trip" name="trip" data-cy="trip" label="Trip" type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/checkin" replace color="info">
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

export default CheckinUpdate;
