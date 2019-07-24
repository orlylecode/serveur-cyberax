/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { JoueurDeleteDialogComponent } from 'app/entities/joueur/joueur-delete-dialog.component';
import { JoueurService } from 'app/entities/joueur/joueur.service';

describe('Component Tests', () => {
  describe('Joueur Management Delete Component', () => {
    let comp: JoueurDeleteDialogComponent;
    let fixture: ComponentFixture<JoueurDeleteDialogComponent>;
    let service: JoueurService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [JoueurDeleteDialogComponent]
      })
        .overrideTemplate(JoueurDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JoueurDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JoueurService);
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
