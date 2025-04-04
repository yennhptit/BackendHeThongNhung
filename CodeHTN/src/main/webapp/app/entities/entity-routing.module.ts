import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'driver',
        data: { pageTitle: 'Drivers' },
        loadChildren: () => import('./driver/driver.routes'),
      },
      {
        path: 'vehicle',
        data: { pageTitle: 'Vehicles' },
        loadChildren: () => import('./vehicle/vehicle.routes'),
      },
      {
        path: 'trip',
        data: { pageTitle: 'Trips' },
        loadChildren: () => import('./trip/trip.routes'),
      },
      {
        path: 'checkin',
        data: { pageTitle: 'Checkins' },
        loadChildren: () => import('./checkin/checkin.routes'),
      },
      {
        path: 'violation',
        data: { pageTitle: 'Violations' },
        loadChildren: () => import('./violation/violation.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
