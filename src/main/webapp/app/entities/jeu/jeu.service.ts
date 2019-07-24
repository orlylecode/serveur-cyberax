import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IJeu } from 'app/shared/model/jeu.model';

type EntityResponseType = HttpResponse<IJeu>;
type EntityArrayResponseType = HttpResponse<IJeu[]>;

@Injectable({ providedIn: 'root' })
export class JeuService {
  public resourceUrl = SERVER_API_URL + 'api/jeus';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/jeus';

  constructor(protected http: HttpClient) {}

  create(jeu: IJeu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jeu);
    return this.http
      .post<IJeu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(jeu: IJeu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jeu);
    return this.http
      .put<IJeu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJeu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJeu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJeu[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(jeu: IJeu): IJeu {
    const copy: IJeu = Object.assign({}, jeu, {
      dateCreation: jeu.dateCreation != null && jeu.dateCreation.isValid() ? jeu.dateCreation.toJSON() : null,
      dateLancement: jeu.dateLancement != null && jeu.dateLancement.isValid() ? jeu.dateLancement.toJSON() : null,
      dateCloture: jeu.dateCloture != null && jeu.dateCloture.isValid() ? jeu.dateCloture.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation != null ? moment(res.body.dateCreation) : null;
      res.body.dateLancement = res.body.dateLancement != null ? moment(res.body.dateLancement) : null;
      res.body.dateCloture = res.body.dateCloture != null ? moment(res.body.dateCloture) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((jeu: IJeu) => {
        jeu.dateCreation = jeu.dateCreation != null ? moment(jeu.dateCreation) : null;
        jeu.dateLancement = jeu.dateLancement != null ? moment(jeu.dateLancement) : null;
        jeu.dateCloture = jeu.dateCloture != null ? moment(jeu.dateCloture) : null;
      });
    }
    return res;
  }
}
