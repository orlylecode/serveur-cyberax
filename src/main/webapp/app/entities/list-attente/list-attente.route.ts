import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ListAttente } from 'app/shared/model/list-attente.model';
import { ListAttenteService } from './list-attente.service';
import { ListAttenteComponent } from './list-attente.component';
import { ListAttenteDetailComponent } from './list-attente-detail.component';
import { ListAttenteUpdateComponent } from './list-attente-update.component';
import { ListAttenteDeletePopupComponent } from './list-attente-delete-dialog.component';
import { IListAttente } from 'app/shared/model/list-attente.model';

@Injectable({ providedIn: 'root' })
export class ListAttenteResolve implements Resolve<IListAttente> {
  constructor(private service: ListAttenteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IListAttente> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ListAttente>) => response.ok),
        map((listAttente: HttpResponse<ListAttente>) => listAttente.body)
      );
    }
    return of(new ListAttente());
  }
}

export const listAttenteRoute: Routes = [
  {
    path: '',
    component: ListAttenteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ListAttentes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ListAttenteDetailComponent,
    resolve: {
      listAttente: ListAttenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ListAttentes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ListAttenteUpdateComponent,
    resolve: {
      listAttente: ListAttenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ListAttentes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ListAttenteUpdateComponent,
    resolve: {
      listAttente: ListAttenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ListAttentes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const listAttentePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ListAttenteDeletePopupComponent,
    resolve: {
      listAttente: ListAttenteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ListAttentes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
