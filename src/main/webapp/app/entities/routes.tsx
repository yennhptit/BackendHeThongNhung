import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Driver from './driver';
import Vehicle from './vehicle';
import Trip from './trip';
import Violation from './violation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="driver/*" element={<Driver />} />
        <Route path="vehicle/*" element={<Vehicle />} />
        <Route path="trip/*" element={<Trip />} />
        <Route path="violation/*" element={<Violation />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
