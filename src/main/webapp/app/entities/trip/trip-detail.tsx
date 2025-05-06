import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip.reducer';

export const TripDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripEntity = useAppSelector(state => state.trip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripDetailsHeading">
          <Translate contentKey="backendHeThongNhungApp.trip.detail.title">Trip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tripEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="backendHeThongNhungApp.trip.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{tripEntity.startTime ? <TextFormat value={tripEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="backendHeThongNhungApp.trip.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{tripEntity.endTime ? <TextFormat value={tripEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="backendHeThongNhungApp.trip.status">Status</Translate>
            </span>
          </dt>
          <dd>{tripEntity.status}</dd>
          <dt>
            <span id="isDelete">
              <Translate contentKey="backendHeThongNhungApp.trip.isDelete">Is Delete</Translate>
            </span>
          </dt>
          <dd>{tripEntity.isDelete ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="backendHeThongNhungApp.trip.driver">Driver</Translate>
          </dt>
          <dd>{tripEntity.driver ? tripEntity.driver.id : ''}</dd>
          <dt>
            <Translate contentKey="backendHeThongNhungApp.trip.vehicle">Vehicle</Translate>
          </dt>
          <dd>{tripEntity.vehicle ? tripEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip/${tripEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripDetail;
