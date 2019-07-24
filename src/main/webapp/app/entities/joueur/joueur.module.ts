import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  JoueurComponent,
  JoueurDetailComponent,
  JoueurUpdateComponent,
  JoueurDeletePopupComponent,
  JoueurDeleteDialogComponent,
  joueurRoute,
  joueurPopupRoute
} from './';

const ENTITY_STATES = [...joueurRoute, ...joueurPopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [JoueurComponent, JoueurDetailComponent, JoueurUpdateComponent, JoueurDeleteDialogComponent, JoueurDeletePopupComponent],
  entryComponents: [JoueurComponent, JoueurUpdateComponent, JoueurDeleteDialogComponent, JoueurDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxJoueurModule {}
