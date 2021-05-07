import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fish from './fish';
import FishDetail from './fish-detail';
import FishUpdate from './fish-update';
import FishDeleteDialog from './fish-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FishUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FishUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FishDetail} />
      <ErrorBoundaryRoute path={match.url} component={Fish} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FishDeleteDialog} />
  </>
);

export default Routes;
