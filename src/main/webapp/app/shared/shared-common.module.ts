import { NgModule } from '@angular/core';

import { ServeurCyberaxSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [ServeurCyberaxSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [ServeurCyberaxSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ServeurCyberaxSharedCommonModule {}
