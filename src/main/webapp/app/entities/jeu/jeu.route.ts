import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Jeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';
import { JeuComponent } from './jeu.component';
import { JeuDetailComponent } from './jeu-detail.component';
import { JeuUpdateComponent } from './jeu-update.component';
import { JeuDeletePopupComponent } from './jeu-delete-dialog.component';
import { IJeu } from 'app/shared/model/jeu.model';

@Injectable({ providedIn: 'root' })
export class JeuResolve implements Resolve<IJeu> {
  constructor(private service: JeuService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IJeu> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Jeu>) => response.ok),
        map((jeu: HttpResponse<Jeu>) => jeu.body)
      );
    }
    return of(new Jeu());
  }
}

export const jeuRoute: Routes = [
  {
    path: '',
    component: JeuComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Jeus'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JeuDetailComponent,
    resolve: {
      jeu: JeuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jeus'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JeuUpdateComponent,
    resolve: {
      jeu: JeuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jeus'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JeuUpdateComponent,
    resolve: {
      jeu: JeuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jeus'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const jeuPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: JeuDeletePopupComponent,
    resolve: {
      jeu: JeuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jeus'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
