import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDriver } from '../driver.model';
import { DriverService } from '../service/driver.service';

export const driverResolve = (route: ActivatedRouteSnapshot): Observable<null | IDriver> => {
  const id = route.params['id'];
  if (id) {
    return inject(DriverService)
      .find(id)
      .pipe(
        mergeMap((driver: HttpResponse<IDriver>) => {
          if (driver.body) {
            return of(driver.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default driverResolve;
