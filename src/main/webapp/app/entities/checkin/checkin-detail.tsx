import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './checkin.reducer';

export const CheckinDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const checkinEntity = useAppSelector(state => state.checkin.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="checkinDetailsHeading">
          <Translate contentKey="backendHeThongNhungApp.checkin.detail.title">Checkin</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{checkinEntity.id}</dd>
          <dt>
            <span id="checkinTime">
              <Translate contentKey="backendHeThongNhungApp.checkin.checkinTime">Checkin Time</Translate>
            </span>
          </dt>
          <dd>
            {checkinEntity.checkinTime ? <TextFormat value={checkinEntity.checkinTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="checkoutTime">
              <Translate contentKey="backendHeThongNhungApp.checkin.checkoutTime">Checkout Time</Translate>
            </span>
          </dt>
          <dd>
            {checkinEntity.checkoutTime ? <TextFormat value={checkinEntity.checkoutTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="faceVerified">
              <Translate contentKey="backendHeThongNhungApp.checkin.faceVerified">Face Verified</Translate>
            </span>
          </dt>
          <dd>{checkinEntity.faceVerified ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="backendHeThongNhungApp.checkin.trip">Trip</Translate>
          </dt>
          <dd>{checkinEntity.trip ? checkinEntity.trip.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/checkin" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/checkin/${checkinEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CheckinDetail;
