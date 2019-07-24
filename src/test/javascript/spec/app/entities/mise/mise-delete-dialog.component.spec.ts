/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { MiseDeleteDialogComponent } from 'app/entities/mise/mise-delete-dialog.component';
import { MiseService } from 'app/entities/mise/mise.service';

describe('Component Tests', () => {
  describe('Mise Management Delete Component', () => {
    let comp: MiseDeleteDialogComponent;
    let fixture: ComponentFixture<MiseDeleteDialogComponent>;
    let service: MiseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [MiseDeleteDialogComponent]
      })
        .overrideTemplate(MiseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MiseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiseService);
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
