import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Gagnant } from 'app/shared/model/gagnant.model';
import { GagnantService } from './gagnant.service';
import { GagnantComponent } from './gagnant.component';
import { GagnantDetailComponent } from './gagnant-detail.component';
import { GagnantUpdateComponent } from './gagnant-update.component';
import { GagnantDeletePopupComponent } from './gagnant-delete-dialog.component';
import { IGagnant } from 'app/shared/model/gagnant.model';

@Injectable({ providedIn: 'root' })
export class GagnantResolve implements Resolve<IGagnant> {
  constructor(private service: GagnantService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGagnant> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Gagnant>) => response.ok),
        map((gagnant: HttpResponse<Gagnant>) => gagnant.body)
      );
    }
    return of(new Gagnant());
  }
}

export const gagnantRoute: Routes = [
  {
    path: '',
    component: GagnantComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Gagnants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GagnantDetailComponent,
    resolve: {
      gagnant: GagnantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Gagnants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GagnantUpdateComponent,
    resolve: {
      gagnant: GagnantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Gagnants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GagnantUpdateComponent,
    resolve: {
      gagnant: GagnantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Gagnants'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const gagnantPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GagnantDeletePopupComponent,
    resolve: {
      gagnant: GagnantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Gagnants'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
