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
import { IViolation } from 'app/shared/model/violation.model';
import { ViolationType } from 'app/shared/model/enumerations/violation-type.model';
import { getEntity, updateEntity, createEntity, reset } from './violation.reducer';

export const ViolationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const drivers = useAppSelector(state => state.driver.entities);
  const violationEntity = useAppSelector(state => state.violation.entity);
  const loading = useAppSelector(state => state.violation.loading);
  const updating = useAppSelector(state => state.violation.updating);
  const updateSuccess = useAppSelector(state => state.violation.updateSuccess);
  const violationTypeValues = Object.keys(ViolationType);

  const handleClose = () => {
    navigate('/violation' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDrivers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...violationEntity,
      ...values,
      driver: drivers.find(it => it.id.toString() === values.driver.toString()),
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
          timestamp: displayDefaultDateTime(),
        }
      : {
          type: 'ALCOHOL',
          ...violationEntity,
          timestamp: convertDateTimeFromServer(violationEntity.timestamp),
          driver: violationEntity?.driver?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="backendHeThongNhungApp.violation.home.createOrEditLabel" data-cy="ViolationCreateUpdateHeading">
            <Translate contentKey="backendHeThongNhungApp.violation.home.createOrEditLabel">Create or edit a Violation</Translate>
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
                  id="violation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label="Value" id="violation-value" name="value" data-cy="value" type="text" />
              <ValidatedField
                label="Timestamp"
                id="violation-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Type" id="violation-type" name="type" data-cy="type" type="select">
                {violationTypeValues.map(violationType => (
                  <option value={violationType} key={violationType}>
                    {violationType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Is Delete" id="violation-isDelete" name="isDelete" data-cy="isDelete" check type="checkbox" />
              <ValidatedField id="violation-driver" name="driver" data-cy="driver" label="Driver" type="select">
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/violation" replace color="info">
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

export default ViolationUpdate;
