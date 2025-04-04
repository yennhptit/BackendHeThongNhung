import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IViolation, NewViolation } from '../violation.model';

export type PartialUpdateViolation = Partial<IViolation> & Pick<IViolation, 'id'>;

type RestOf<T extends IViolation | NewViolation> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestViolation = RestOf<IViolation>;

export type NewRestViolation = RestOf<NewViolation>;

export type PartialUpdateRestViolation = RestOf<PartialUpdateViolation>;

export type EntityResponseType = HttpResponse<IViolation>;
export type EntityArrayResponseType = HttpResponse<IViolation[]>;

@Injectable({ providedIn: 'root' })
export class ViolationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/violations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(violation: NewViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(violation);
    return this.http
      .post<RestViolation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(violation: IViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(violation);
    return this.http
      .put<RestViolation>(`${this.resourceUrl}/${this.getViolationIdentifier(violation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(violation: PartialUpdateViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(violation);
    return this.http
      .patch<RestViolation>(`${this.resourceUrl}/${this.getViolationIdentifier(violation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestViolation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestViolation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getViolationIdentifier(violation: Pick<IViolation, 'id'>): number {
    return violation.id;
  }

  compareViolation(o1: Pick<IViolation, 'id'> | null, o2: Pick<IViolation, 'id'> | null): boolean {
    return o1 && o2 ? this.getViolationIdentifier(o1) === this.getViolationIdentifier(o2) : o1 === o2;
  }

  addViolationToCollectionIfMissing<Type extends Pick<IViolation, 'id'>>(
    violationCollection: Type[],
    ...violationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const violations: Type[] = violationsToCheck.filter(isPresent);
    if (violations.length > 0) {
      const violationCollectionIdentifiers = violationCollection.map(violationItem => this.getViolationIdentifier(violationItem)!);
      const violationsToAdd = violations.filter(violationItem => {
        const violationIdentifier = this.getViolationIdentifier(violationItem);
        if (violationCollectionIdentifiers.includes(violationIdentifier)) {
          return false;
        }
        violationCollectionIdentifiers.push(violationIdentifier);
        return true;
      });
      return [...violationsToAdd, ...violationCollection];
    }
    return violationCollection;
  }

  protected convertDateFromClient<T extends IViolation | NewViolation | PartialUpdateViolation>(violation: T): RestOf<T> {
    return {
      ...violation,
      timestamp: violation.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restViolation: RestViolation): IViolation {
    return {
      ...restViolation,
      timestamp: restViolation.timestamp ? dayjs(restViolation.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestViolation>): HttpResponse<IViolation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestViolation[]>): HttpResponse<IViolation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
