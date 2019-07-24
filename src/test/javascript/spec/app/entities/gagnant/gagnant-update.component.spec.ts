/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { GagnantUpdateComponent } from 'app/entities/gagnant/gagnant-update.component';
import { GagnantService } from 'app/entities/gagnant/gagnant.service';
import { Gagnant } from 'app/shared/model/gagnant.model';

describe('Component Tests', () => {
  describe('Gagnant Management Update Component', () => {
    let comp: GagnantUpdateComponent;
    let fixture: ComponentFixture<GagnantUpdateComponent>;
    let service: GagnantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [GagnantUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GagnantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GagnantUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GagnantService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Gagnant(123);
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
        const entity = new Gagnant();
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
