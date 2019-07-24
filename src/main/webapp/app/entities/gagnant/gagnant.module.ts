import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  GagnantComponent,
  GagnantDetailComponent,
  GagnantUpdateComponent,
  GagnantDeletePopupComponent,
  GagnantDeleteDialogComponent,
  gagnantRoute,
  gagnantPopupRoute
} from './';

const ENTITY_STATES = [...gagnantRoute, ...gagnantPopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GagnantComponent,
    GagnantDetailComponent,
    GagnantUpdateComponent,
    GagnantDeleteDialogComponent,
    GagnantDeletePopupComponent
  ],
  entryComponents: [GagnantComponent, GagnantUpdateComponent, GagnantDeleteDialogComponent, GagnantDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxGagnantModule {}
