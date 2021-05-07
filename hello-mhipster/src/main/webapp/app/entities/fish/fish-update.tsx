import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISchool } from 'app/shared/model/school.model';
import { getEntities as getSchools } from 'app/entities/school/school.reducer';
import { getEntity, updateEntity, createEntity, reset } from './fish.reducer';
import { IFish } from 'app/shared/model/fish.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFishUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FishUpdate = (props: IFishUpdateProps) => {
  const [schoolId, setSchoolId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { fishEntity, schools, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/fish');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSchools();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...fishEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="helloMhipsterApp.fish.home.createOrEditLabel">
            <Translate contentKey="helloMhipsterApp.fish.home.createOrEditLabel">Create or edit a Fish</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : fishEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="fish-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="fish-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="fish-name">
                  <Translate contentKey="helloMhipsterApp.fish.name">Name</Translate>
                </Label>
                <AvField
                  id="fish-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    minLength: { value: 3, errorMessage: translate('entity.validation.minlength', { min: 3 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="ageLabel" for="fish-age">
                  <Translate contentKey="helloMhipsterApp.fish.age">Age</Translate>
                </Label>
                <AvField
                  id="fish-age"
                  type="string"
                  className="form-control"
                  name="age"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="waterTypeLabel" for="fish-waterType">
                  <Translate contentKey="helloMhipsterApp.fish.waterType">Water Type</Translate>
                </Label>
                <AvInput
                  id="fish-waterType"
                  type="select"
                  className="form-control"
                  name="waterType"
                  value={(!isNew && fishEntity.waterType) || 'FRESH'}
                >
                  <option value="FRESH">{translate('helloMhipsterApp.WaterType.FRESH')}</option>
                  <option value="SALT">{translate('helloMhipsterApp.WaterType.SALT')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="fish-school">
                  <Translate contentKey="helloMhipsterApp.fish.school">School</Translate>
                </Label>
                <AvInput id="fish-school" type="select" className="form-control" name="school.id">
                  <option value="" key="0" />
                  {schools
                    ? schools.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/fish" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  schools: storeState.school.entities,
  fishEntity: storeState.fish.entity,
  loading: storeState.fish.loading,
  updating: storeState.fish.updating,
  updateSuccess: storeState.fish.updateSuccess,
});

const mapDispatchToProps = {
  getSchools,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FishUpdate);
