/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { MiseUpdateComponent } from 'app/entities/mise/mise-update.component';
import { MiseService } from 'app/entities/mise/mise.service';
import { Mise } from 'app/shared/model/mise.model';

describe('Component Tests', () => {
  describe('Mise Management Update Component', () => {
    let comp: MiseUpdateComponent;
    let fixture: ComponentFixture<MiseUpdateComponent>;
    let service: MiseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [MiseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MiseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MiseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Mise(123);
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
        const entity = new Mise();
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
