import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServeurCyberaxSharedModule } from 'app/shared';
import {
  TerminalComponent,
  TerminalDetailComponent,
  TerminalUpdateComponent,
  TerminalDeletePopupComponent,
  TerminalDeleteDialogComponent,
  terminalRoute,
  terminalPopupRoute
} from './';

const ENTITY_STATES = [...terminalRoute, ...terminalPopupRoute];

@NgModule({
  imports: [ServeurCyberaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TerminalComponent,
    TerminalDetailComponent,
    TerminalUpdateComponent,
    TerminalDeleteDialogComponent,
    TerminalDeletePopupComponent
  ],
  entryComponents: [TerminalComponent, TerminalUpdateComponent, TerminalDeleteDialogComponent, TerminalDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxTerminalModule {}
