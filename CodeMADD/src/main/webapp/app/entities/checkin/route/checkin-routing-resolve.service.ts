import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICheckin } from '../checkin.model';
import { CheckinService } from '../service/checkin.service';

export const checkinResolve = (route: ActivatedRouteSnapshot): Observable<null | ICheckin> => {
  const id = route.params['id'];
  if (id) {
    return inject(CheckinService)
      .find(id)
      .pipe(
        mergeMap((checkin: HttpResponse<ICheckin>) => {
          if (checkin.body) {
            return of(checkin.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default checkinResolve;
