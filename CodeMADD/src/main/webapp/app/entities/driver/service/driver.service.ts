import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDriver, NewDriver } from '../driver.model';

export type PartialUpdateDriver = Partial<IDriver> & Pick<IDriver, 'id'>;

type RestOf<T extends IDriver | NewDriver> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestDriver = RestOf<IDriver>;

export type NewRestDriver = RestOf<NewDriver>;

export type PartialUpdateRestDriver = RestOf<PartialUpdateDriver>;

export type EntityResponseType = HttpResponse<IDriver>;
export type EntityArrayResponseType = HttpResponse<IDriver[]>;

@Injectable({ providedIn: 'root' })
export class DriverService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/drivers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(driver: NewDriver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(driver);
    return this.http
      .post<RestDriver>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(driver: IDriver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(driver);
    return this.http
      .put<RestDriver>(`${this.resourceUrl}/${this.getDriverIdentifier(driver)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(driver: PartialUpdateDriver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(driver);
    return this.http
      .patch<RestDriver>(`${this.resourceUrl}/${this.getDriverIdentifier(driver)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDriver>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDriver[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDriverIdentifier(driver: Pick<IDriver, 'id'>): number {
    return driver.id;
  }

  compareDriver(o1: Pick<IDriver, 'id'> | null, o2: Pick<IDriver, 'id'> | null): boolean {
    return o1 && o2 ? this.getDriverIdentifier(o1) === this.getDriverIdentifier(o2) : o1 === o2;
  }

  addDriverToCollectionIfMissing<Type extends Pick<IDriver, 'id'>>(
    driverCollection: Type[],
    ...driversToCheck: (Type | null | undefined)[]
  ): Type[] {
    const drivers: Type[] = driversToCheck.filter(isPresent);
    if (drivers.length > 0) {
      const driverCollectionIdentifiers = driverCollection.map(driverItem => this.getDriverIdentifier(driverItem)!);
      const driversToAdd = drivers.filter(driverItem => {
        const driverIdentifier = this.getDriverIdentifier(driverItem);
        if (driverCollectionIdentifiers.includes(driverIdentifier)) {
          return false;
        }
        driverCollectionIdentifiers.push(driverIdentifier);
        return true;
      });
      return [...driversToAdd, ...driverCollection];
    }
    return driverCollection;
  }

  protected convertDateFromClient<T extends IDriver | NewDriver | PartialUpdateDriver>(driver: T): RestOf<T> {
    return {
      ...driver,
      createdAt: driver.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDriver: RestDriver): IDriver {
    return {
      ...restDriver,
      createdAt: restDriver.createdAt ? dayjs(restDriver.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDriver>): HttpResponse<IDriver> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDriver[]>): HttpResponse<IDriver[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
