import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrip, NewTrip } from '../trip.model';

export type PartialUpdateTrip = Partial<ITrip> & Pick<ITrip, 'id'>;

type RestOf<T extends ITrip | NewTrip> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestTrip = RestOf<ITrip>;

export type NewRestTrip = RestOf<NewTrip>;

export type PartialUpdateRestTrip = RestOf<PartialUpdateTrip>;

export type EntityResponseType = HttpResponse<ITrip>;
export type EntityArrayResponseType = HttpResponse<ITrip[]>;

@Injectable({ providedIn: 'root' })
export class TripService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trips');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(trip: NewTrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http.post<RestTrip>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .put<RestTrip>(`${this.resourceUrl}/${this.getTripIdentifier(trip)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trip: PartialUpdateTrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .patch<RestTrip>(`${this.resourceUrl}/${this.getTripIdentifier(trip)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrip>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrip[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTripIdentifier(trip: Pick<ITrip, 'id'>): number {
    return trip.id;
  }

  compareTrip(o1: Pick<ITrip, 'id'> | null, o2: Pick<ITrip, 'id'> | null): boolean {
    return o1 && o2 ? this.getTripIdentifier(o1) === this.getTripIdentifier(o2) : o1 === o2;
  }

  addTripToCollectionIfMissing<Type extends Pick<ITrip, 'id'>>(
    tripCollection: Type[],
    ...tripsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trips: Type[] = tripsToCheck.filter(isPresent);
    if (trips.length > 0) {
      const tripCollectionIdentifiers = tripCollection.map(tripItem => this.getTripIdentifier(tripItem)!);
      const tripsToAdd = trips.filter(tripItem => {
        const tripIdentifier = this.getTripIdentifier(tripItem);
        if (tripCollectionIdentifiers.includes(tripIdentifier)) {
          return false;
        }
        tripCollectionIdentifiers.push(tripIdentifier);
        return true;
      });
      return [...tripsToAdd, ...tripCollection];
    }
    return tripCollection;
  }

  protected convertDateFromClient<T extends ITrip | NewTrip | PartialUpdateTrip>(trip: T): RestOf<T> {
    return {
      ...trip,
      startTime: trip.startTime?.toJSON() ?? null,
      endTime: trip.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrip: RestTrip): ITrip {
    return {
      ...restTrip,
      startTime: restTrip.startTime ? dayjs(restTrip.startTime) : undefined,
      endTime: restTrip.endTime ? dayjs(restTrip.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrip>): HttpResponse<ITrip> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrip[]>): HttpResponse<ITrip[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
