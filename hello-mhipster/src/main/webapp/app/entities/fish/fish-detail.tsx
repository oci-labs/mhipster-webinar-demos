import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fish.reducer';
import { IFish } from 'app/shared/model/fish.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFishDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FishDetail = (props: IFishDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { fishEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="helloMhipsterApp.fish.detail.title">Fish</Translate> [<b>{fishEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="helloMhipsterApp.fish.name">Name</Translate>
            </span>
          </dt>
          <dd>{fishEntity.name}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="helloMhipsterApp.fish.age">Age</Translate>
            </span>
          </dt>
          <dd>{fishEntity.age}</dd>
          <dt>
            <span id="waterType">
              <Translate contentKey="helloMhipsterApp.fish.waterType">Water Type</Translate>
            </span>
          </dt>
          <dd>{fishEntity.waterType}</dd>
          <dt>
            <Translate contentKey="helloMhipsterApp.fish.school">School</Translate>
          </dt>
          <dd>{fishEntity.school ? fishEntity.school.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/fish" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fish/${fishEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ fish }: IRootState) => ({
  fishEntity: fish.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FishDetail);
