import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './driver.reducer';

export const DriverDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const driverEntity = useAppSelector(state => state.driver.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="driverDetailsHeading">
          <Translate contentKey="backendHeThongNhungApp.driver.detail.title">Driver</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{driverEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="backendHeThongNhungApp.driver.name">Name</Translate>
            </span>
          </dt>
          <dd>{driverEntity.name}</dd>
          <dt>
            <span id="rfidUid">
              <Translate contentKey="backendHeThongNhungApp.driver.rfidUid">Rfid Uid</Translate>
            </span>
          </dt>
          <dd>{driverEntity.rfidUid}</dd>
          <dt>
            <span id="licenseNumber">
              <Translate contentKey="backendHeThongNhungApp.driver.licenseNumber">License Number</Translate>
            </span>
          </dt>
          <dd>{driverEntity.licenseNumber}</dd>
          <dt>
            <span id="faceData">
              <Translate contentKey="backendHeThongNhungApp.driver.faceData">Face Data</Translate>
            </span>
          </dt>
          <dd>{driverEntity.faceData}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="backendHeThongNhungApp.driver.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{driverEntity.createdAt ? <TextFormat value={driverEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="backendHeThongNhungApp.driver.status">Status</Translate>
            </span>
          </dt>
          <dd>{driverEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/driver" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/driver/${driverEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DriverDetail;
