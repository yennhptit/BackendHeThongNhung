import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './violation.reducer';

export const ViolationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const violationEntity = useAppSelector(state => state.violation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="violationDetailsHeading">
          <Translate contentKey="backendHeThongNhungApp.violation.detail.title">Violation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{violationEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="backendHeThongNhungApp.violation.value">Value</Translate>
            </span>
          </dt>
          <dd>{violationEntity.value}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="backendHeThongNhungApp.violation.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {violationEntity.timestamp ? <TextFormat value={violationEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="backendHeThongNhungApp.violation.type">Type</Translate>
            </span>
          </dt>
          <dd>{violationEntity.type}</dd>
          <dt>
            <Translate contentKey="backendHeThongNhungApp.violation.trip">Trip</Translate>
          </dt>
          <dd>{violationEntity.trip ? violationEntity.trip.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/violation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/violation/${violationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ViolationDetail;
