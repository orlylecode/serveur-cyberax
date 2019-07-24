import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Mise } from 'app/shared/model/mise.model';
import { MiseService } from './mise.service';
import { MiseComponent } from './mise.component';
import { MiseDetailComponent } from './mise-detail.component';
import { MiseUpdateComponent } from './mise-update.component';
import { MiseDeletePopupComponent } from './mise-delete-dialog.component';
import { IMise } from 'app/shared/model/mise.model';

@Injectable({ providedIn: 'root' })
export class MiseResolve implements Resolve<IMise> {
  constructor(private service: MiseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMise> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Mise>) => response.ok),
        map((mise: HttpResponse<Mise>) => mise.body)
      );
    }
    return of(new Mise());
  }
}

export const miseRoute: Routes = [
  {
    path: '',
    component: MiseComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Mises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MiseDetailComponent,
    resolve: {
      mise: MiseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Mises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MiseUpdateComponent,
    resolve: {
      mise: MiseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Mises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MiseUpdateComponent,
    resolve: {
      mise: MiseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Mises'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const misePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MiseDeletePopupComponent,
    resolve: {
      mise: MiseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Mises'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
