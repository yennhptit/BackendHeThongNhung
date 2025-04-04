import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrip } from '../trip.model';
import { TripService } from '../service/trip.service';

export const tripResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrip> => {
  const id = route.params['id'];
  if (id) {
    return inject(TripService)
      .find(id)
      .pipe(
        mergeMap((trip: HttpResponse<ITrip>) => {
          if (trip.body) {
            return of(trip.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tripResolve;
