import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DriverComponent } from './list/driver.component';
import { DriverDetailComponent } from './detail/driver-detail.component';
import { DriverUpdateComponent } from './update/driver-update.component';
import DriverResolve from './route/driver-routing-resolve.service';

const driverRoute: Routes = [
  {
    path: '',
    component: DriverComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DriverDetailComponent,
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DriverUpdateComponent,
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DriverUpdateComponent,
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default driverRoute;
