import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VehicleComponent } from './list/vehicle.component';
import { VehicleDetailComponent } from './detail/vehicle-detail.component';
import { VehicleUpdateComponent } from './update/vehicle-update.component';
import VehicleResolve from './route/vehicle-routing-resolve.service';

const vehicleRoute: Routes = [
  {
    path: '',
    component: VehicleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehicleDetailComponent,
    resolve: {
      vehicle: VehicleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehicleUpdateComponent,
    resolve: {
      vehicle: VehicleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehicleUpdateComponent,
    resolve: {
      vehicle: VehicleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleRoute;
