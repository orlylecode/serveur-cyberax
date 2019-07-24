import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  JeuComponent,
  JeuDetailComponent,
  JeuUpdateComponent,
  JeuDeletePopupComponent,
  JeuDeleteDialogComponent,
  jeuRoute,
  jeuPopupRoute
} from './';

const ENTITY_STATES = [...jeuRoute, ...jeuPopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [JeuComponent, JeuDetailComponent, JeuUpdateComponent, JeuDeleteDialogComponent, JeuDeletePopupComponent],
  entryComponents: [JeuComponent, JeuUpdateComponent, JeuDeleteDialogComponent, JeuDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxJeuModule {}
