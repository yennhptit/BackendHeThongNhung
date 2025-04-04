import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Violation from './violation';
import ViolationDetail from './violation-detail';
import ViolationUpdate from './violation-update';
import ViolationDeleteDialog from './violation-delete-dialog';

const ViolationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Violation />} />
    <Route path="new" element={<ViolationUpdate />} />
    <Route path=":id">
      <Route index element={<ViolationDetail />} />
      <Route path="edit" element={<ViolationUpdate />} />
      <Route path="delete" element={<ViolationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ViolationRoutes;
