import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CheckinComponent } from './list/checkin.component';
import { CheckinDetailComponent } from './detail/checkin-detail.component';
import { CheckinUpdateComponent } from './update/checkin-update.component';
import CheckinResolve from './route/checkin-routing-resolve.service';

const checkinRoute: Routes = [
  {
    path: '',
    component: CheckinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CheckinDetailComponent,
    resolve: {
      checkin: CheckinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CheckinUpdateComponent,
    resolve: {
      checkin: CheckinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CheckinUpdateComponent,
    resolve: {
      checkin: CheckinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default checkinRoute;
