import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TripComponent } from './list/trip.component';
import { TripDetailComponent } from './detail/trip-detail.component';
import { TripUpdateComponent } from './update/trip-update.component';
import TripResolve from './route/trip-routing-resolve.service';

const tripRoute: Routes = [
  {
    path: '',
    component: TripComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripDetailComponent,
    resolve: {
      trip: TripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripUpdateComponent,
    resolve: {
      trip: TripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripUpdateComponent,
    resolve: {
      trip: TripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tripRoute;
