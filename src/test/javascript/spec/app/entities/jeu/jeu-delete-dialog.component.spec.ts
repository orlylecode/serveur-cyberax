/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { JeuDeleteDialogComponent } from 'app/entities/jeu/jeu-delete-dialog.component';
import { JeuService } from 'app/entities/jeu/jeu.service';

describe('Component Tests', () => {
  describe('Jeu Management Delete Component', () => {
    let comp: JeuDeleteDialogComponent;
    let fixture: ComponentFixture<JeuDeleteDialogComponent>;
    let service: JeuService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [JeuDeleteDialogComponent]
      })
        .overrideTemplate(JeuDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JeuDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JeuService);
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
