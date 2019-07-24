import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMise } from 'app/shared/model/mise.model';

type EntityResponseType = HttpResponse<IMise>;
type EntityArrayResponseType = HttpResponse<IMise[]>;

@Injectable({ providedIn: 'root' })
export class MiseService {
  public resourceUrl = SERVER_API_URL + 'api/mises';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/mises';

  constructor(protected http: HttpClient) {}

  create(mise: IMise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mise);
    return this.http
      .post<IMise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mise: IMise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mise);
    return this.http
      .put<IMise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMise[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(mise: IMise): IMise {
    const copy: IMise = Object.assign({}, mise, {
      dateMise: mise.dateMise != null && mise.dateMise.isValid() ? mise.dateMise.toJSON() : null,
      dateValidation: mise.dateValidation != null && mise.dateValidation.isValid() ? mise.dateValidation.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateMise = res.body.dateMise != null ? moment(res.body.dateMise) : null;
      res.body.dateValidation = res.body.dateValidation != null ? moment(res.body.dateValidation) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mise: IMise) => {
        mise.dateMise = mise.dateMise != null ? moment(mise.dateMise) : null;
        mise.dateValidation = mise.dateValidation != null ? moment(mise.dateValidation) : null;
      });
    }
    return res;
  }
}
