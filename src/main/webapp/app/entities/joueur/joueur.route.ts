import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Joueur } from 'app/shared/model/joueur.model';
import { JoueurService } from './joueur.service';
import { JoueurComponent } from './joueur.component';
import { JoueurDetailComponent } from './joueur-detail.component';
import { JoueurUpdateComponent } from './joueur-update.component';
import { JoueurDeletePopupComponent } from './joueur-delete-dialog.component';
import { IJoueur } from 'app/shared/model/joueur.model';

@Injectable({ providedIn: 'root' })
export class JoueurResolve implements Resolve<IJoueur> {
  constructor(private service: JoueurService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IJoueur> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Joueur>) => response.ok),
        map((joueur: HttpResponse<Joueur>) => joueur.body)
      );
    }
    return of(new Joueur());
  }
}

export const joueurRoute: Routes = [
  {
    path: '',
    component: JoueurComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Joueurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JoueurDetailComponent,
    resolve: {
      joueur: JoueurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Joueurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JoueurUpdateComponent,
    resolve: {
      joueur: JoueurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Joueurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JoueurUpdateComponent,
    resolve: {
      joueur: JoueurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Joueurs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const joueurPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: JoueurDeletePopupComponent,
    resolve: {
      joueur: JoueurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Joueurs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
