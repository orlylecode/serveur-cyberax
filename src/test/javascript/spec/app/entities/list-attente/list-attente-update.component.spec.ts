/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { ListAttenteUpdateComponent } from 'app/entities/list-attente/list-attente-update.component';
import { ListAttenteService } from 'app/entities/list-attente/list-attente.service';
import { ListAttente } from 'app/shared/model/list-attente.model';

describe('Component Tests', () => {
  describe('ListAttente Management Update Component', () => {
    let comp: ListAttenteUpdateComponent;
    let fixture: ComponentFixture<ListAttenteUpdateComponent>;
    let service: ListAttenteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [ListAttenteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ListAttenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ListAttenteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ListAttenteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ListAttente(123);
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
        const entity = new ListAttente();
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
