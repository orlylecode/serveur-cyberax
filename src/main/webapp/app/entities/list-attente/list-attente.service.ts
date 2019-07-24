import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IListAttente } from 'app/shared/model/list-attente.model';

type EntityResponseType = HttpResponse<IListAttente>;
type EntityArrayResponseType = HttpResponse<IListAttente[]>;

@Injectable({ providedIn: 'root' })
export class ListAttenteService {
  public resourceUrl = SERVER_API_URL + 'api/list-attentes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/list-attentes';

  constructor(protected http: HttpClient) {}

  create(listAttente: IListAttente): Observable<EntityResponseType> {
    return this.http.post<IListAttente>(this.resourceUrl, listAttente, { observe: 'response' });
  }

  update(listAttente: IListAttente): Observable<EntityResponseType> {
    return this.http.put<IListAttente>(this.resourceUrl, listAttente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IListAttente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IListAttente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IListAttente[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
