import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  MiseComponent,
  MiseDetailComponent,
  MiseUpdateComponent,
  MiseDeletePopupComponent,
  MiseDeleteDialogComponent,
  miseRoute,
  misePopupRoute
} from './';

const ENTITY_STATES = [...miseRoute, ...misePopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [MiseComponent, MiseDetailComponent, MiseUpdateComponent, MiseDeleteDialogComponent, MiseDeletePopupComponent],
  entryComponents: [MiseComponent, MiseUpdateComponent, MiseDeleteDialogComponent, MiseDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxMiseModule {}
