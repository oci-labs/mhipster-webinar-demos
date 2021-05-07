import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './fish.reducer';
import { IFish } from 'app/shared/model/fish.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFishProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Fish = (props: IFishProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { fishList, match, loading } = props;
  return (
    <div>
      <h2 id="fish-heading">
        <Translate contentKey="helloMhipsterApp.fish.home.title">Fish</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="helloMhipsterApp.fish.home.createLabel">Create new Fish</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {fishList && fishList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="helloMhipsterApp.fish.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="helloMhipsterApp.fish.age">Age</Translate>
                </th>
                <th>
                  <Translate contentKey="helloMhipsterApp.fish.waterType">Water Type</Translate>
                </th>
                <th>
                  <Translate contentKey="helloMhipsterApp.fish.school">School</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fishList.map((fish, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${fish.id}`} color="link" size="sm">
                      {fish.id}
                    </Button>
                  </td>
                  <td>{fish.name}</td>
                  <td>{fish.age}</td>
                  <td>
                    <Translate contentKey={`helloMhipsterApp.WaterType.${fish.waterType}`} />
                  </td>
                  <td>{fish.school ? <Link to={`school/${fish.school.id}`}>{fish.school.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${fish.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${fish.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${fish.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="helloMhipsterApp.fish.home.notFound">No Fish found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ fish }: IRootState) => ({
  fishList: fish.entities,
  loading: fish.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Fish);
