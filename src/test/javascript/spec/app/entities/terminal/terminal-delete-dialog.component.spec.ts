/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { TerminalDeleteDialogComponent } from 'app/entities/terminal/terminal-delete-dialog.component';
import { TerminalService } from 'app/entities/terminal/terminal.service';

describe('Component Tests', () => {
  describe('Terminal Management Delete Component', () => {
    let comp: TerminalDeleteDialogComponent;
    let fixture: ComponentFixture<TerminalDeleteDialogComponent>;
    let service: TerminalService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [TerminalDeleteDialogComponent]
      })
        .overrideTemplate(TerminalDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TerminalDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TerminalService);
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
