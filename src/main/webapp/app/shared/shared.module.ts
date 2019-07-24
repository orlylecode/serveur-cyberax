import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ServeurCyberaxSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ServeurCyberaxSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ServeurCyberaxSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxSharedModule {
  static forRoot() {
    return {
      ngModule: ServeurCyberaxSharedModule
    };
  }
}
