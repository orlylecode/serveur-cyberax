import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Terminal } from 'app/shared/model/terminal.model';
import { TerminalService } from './terminal.service';
import { TerminalComponent } from './terminal.component';
import { TerminalDetailComponent } from './terminal-detail.component';
import { TerminalUpdateComponent } from './terminal-update.component';
import { TerminalDeletePopupComponent } from './terminal-delete-dialog.component';
import { ITerminal } from 'app/shared/model/terminal.model';

@Injectable({ providedIn: 'root' })
export class TerminalResolve implements Resolve<ITerminal> {
  constructor(private service: TerminalService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITerminal> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Terminal>) => response.ok),
        map((terminal: HttpResponse<Terminal>) => terminal.body)
      );
    }
    return of(new Terminal());
  }
}

export const terminalRoute: Routes = [
  {
    path: '',
    component: TerminalComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Terminals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TerminalDetailComponent,
    resolve: {
      terminal: TerminalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Terminals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TerminalUpdateComponent,
    resolve: {
      terminal: TerminalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Terminals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TerminalUpdateComponent,
    resolve: {
      terminal: TerminalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Terminals'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const terminalPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TerminalDeletePopupComponent,
    resolve: {
      terminal: TerminalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Terminals'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
