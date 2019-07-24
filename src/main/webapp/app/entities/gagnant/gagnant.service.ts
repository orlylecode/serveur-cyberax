import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGagnant } from 'app/shared/model/gagnant.model';

type EntityResponseType = HttpResponse<IGagnant>;
type EntityArrayResponseType = HttpResponse<IGagnant[]>;

@Injectable({ providedIn: 'root' })
export class GagnantService {
  public resourceUrl = SERVER_API_URL + 'api/gagnants';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/gagnants';

  constructor(protected http: HttpClient) {}

  create(gagnant: IGagnant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gagnant);
    return this.http
      .post<IGagnant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(gagnant: IGagnant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gagnant);
    return this.http
      .put<IGagnant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGagnant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGagnant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGagnant[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(gagnant: IGagnant): IGagnant {
    const copy: IGagnant = Object.assign({}, gagnant, {
      dateGain: gagnant.dateGain != null && gagnant.dateGain.isValid() ? gagnant.dateGain.toJSON() : null,
      datePayment: gagnant.datePayment != null && gagnant.datePayment.isValid() ? gagnant.datePayment.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateGain = res.body.dateGain != null ? moment(res.body.dateGain) : null;
      res.body.datePayment = res.body.datePayment != null ? moment(res.body.datePayment) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((gagnant: IGagnant) => {
        gagnant.dateGain = gagnant.dateGain != null ? moment(gagnant.dateGain) : null;
        gagnant.datePayment = gagnant.datePayment != null ? moment(gagnant.datePayment) : null;
      });
    }
    return res;
  }
}
