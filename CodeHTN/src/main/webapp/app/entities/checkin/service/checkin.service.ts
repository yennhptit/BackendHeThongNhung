import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICheckin, NewCheckin } from '../checkin.model';

export type PartialUpdateCheckin = Partial<ICheckin> & Pick<ICheckin, 'id'>;

type RestOf<T extends ICheckin | NewCheckin> = Omit<T, 'checkinTime' | 'checkoutTime'> & {
  checkinTime?: string | null;
  checkoutTime?: string | null;
};

export type RestCheckin = RestOf<ICheckin>;

export type NewRestCheckin = RestOf<NewCheckin>;

export type PartialUpdateRestCheckin = RestOf<PartialUpdateCheckin>;

export type EntityResponseType = HttpResponse<ICheckin>;
export type EntityArrayResponseType = HttpResponse<ICheckin[]>;

@Injectable({ providedIn: 'root' })
export class CheckinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/checkins');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(checkin: NewCheckin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(checkin);
    return this.http
      .post<RestCheckin>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(checkin: ICheckin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(checkin);
    return this.http
      .put<RestCheckin>(`${this.resourceUrl}/${this.getCheckinIdentifier(checkin)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(checkin: PartialUpdateCheckin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(checkin);
    return this.http
      .patch<RestCheckin>(`${this.resourceUrl}/${this.getCheckinIdentifier(checkin)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCheckin>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCheckin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCheckinIdentifier(checkin: Pick<ICheckin, 'id'>): number {
    return checkin.id;
  }

  compareCheckin(o1: Pick<ICheckin, 'id'> | null, o2: Pick<ICheckin, 'id'> | null): boolean {
    return o1 && o2 ? this.getCheckinIdentifier(o1) === this.getCheckinIdentifier(o2) : o1 === o2;
  }

  addCheckinToCollectionIfMissing<Type extends Pick<ICheckin, 'id'>>(
    checkinCollection: Type[],
    ...checkinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const checkins: Type[] = checkinsToCheck.filter(isPresent);
    if (checkins.length > 0) {
      const checkinCollectionIdentifiers = checkinCollection.map(checkinItem => this.getCheckinIdentifier(checkinItem)!);
      const checkinsToAdd = checkins.filter(checkinItem => {
        const checkinIdentifier = this.getCheckinIdentifier(checkinItem);
        if (checkinCollectionIdentifiers.includes(checkinIdentifier)) {
          return false;
        }
        checkinCollectionIdentifiers.push(checkinIdentifier);
        return true;
      });
      return [...checkinsToAdd, ...checkinCollection];
    }
    return checkinCollection;
  }

  protected convertDateFromClient<T extends ICheckin | NewCheckin | PartialUpdateCheckin>(checkin: T): RestOf<T> {
    return {
      ...checkin,
      checkinTime: checkin.checkinTime?.toJSON() ?? null,
      checkoutTime: checkin.checkoutTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCheckin: RestCheckin): ICheckin {
    return {
      ...restCheckin,
      checkinTime: restCheckin.checkinTime ? dayjs(restCheckin.checkinTime) : undefined,
      checkoutTime: restCheckin.checkoutTime ? dayjs(restCheckin.checkoutTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCheckin>): HttpResponse<ICheckin> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCheckin[]>): HttpResponse<ICheckin[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
