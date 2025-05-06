import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle.reducer';

export const VehicleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDetailsHeading">
          <Translate contentKey="backendHeThongNhungApp.vehicle.detail.title">Vehicle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.id}</dd>
          <dt>
            <span id="licensePlate">
              <Translate contentKey="backendHeThongNhungApp.vehicle.licensePlate">License Plate</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.licensePlate}</dd>
          <dt>
            <span id="model">
              <Translate contentKey="backendHeThongNhungApp.vehicle.model">Model</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.model}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="backendHeThongNhungApp.vehicle.status">Status</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.status}</dd>
          <dt>
            <span id="isDelete">
              <Translate contentKey="backendHeThongNhungApp.vehicle.isDelete">Is Delete</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.isDelete ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/vehicle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle/${vehicleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDetail;
