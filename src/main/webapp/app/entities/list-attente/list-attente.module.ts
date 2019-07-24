import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  ListAttenteComponent,
  ListAttenteDetailComponent,
  ListAttenteUpdateComponent,
  ListAttenteDeletePopupComponent,
  ListAttenteDeleteDialogComponent,
  listAttenteRoute,
  listAttentePopupRoute
} from './';

const ENTITY_STATES = [...listAttenteRoute, ...listAttentePopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ListAttenteComponent,
    ListAttenteDetailComponent,
    ListAttenteUpdateComponent,
    ListAttenteDeleteDialogComponent,
    ListAttenteDeletePopupComponent
  ],
  entryComponents: [ListAttenteComponent, ListAttenteUpdateComponent, ListAttenteDeleteDialogComponent, ListAttenteDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxListAttenteModule {}
