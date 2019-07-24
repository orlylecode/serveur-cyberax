/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { GagnantDeleteDialogComponent } from 'app/entities/gagnant/gagnant-delete-dialog.component';
import { GagnantService } from 'app/entities/gagnant/gagnant.service';

describe('Component Tests', () => {
  describe('Gagnant Management Delete Component', () => {
    let comp: GagnantDeleteDialogComponent;
    let fixture: ComponentFixture<GagnantDeleteDialogComponent>;
    let service: GagnantService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [GagnantDeleteDialogComponent]
      })
        .overrideTemplate(GagnantDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GagnantDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GagnantService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
