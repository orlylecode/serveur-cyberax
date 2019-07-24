/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { TerminalUpdateComponent } from 'app/entities/terminal/terminal-update.component';
import { TerminalService } from 'app/entities/terminal/terminal.service';
import { Terminal } from 'app/shared/model/terminal.model';

describe('Component Tests', () => {
  describe('Terminal Management Update Component', () => {
    let comp: TerminalUpdateComponent;
    let fixture: ComponentFixture<TerminalUpdateComponent>;
    let service: TerminalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [TerminalUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TerminalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TerminalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TerminalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Terminal(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Terminal();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
