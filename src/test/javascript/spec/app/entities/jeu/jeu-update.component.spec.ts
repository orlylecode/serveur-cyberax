/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { JeuUpdateComponent } from 'app/entities/jeu/jeu-update.component';
import { JeuService } from 'app/entities/jeu/jeu.service';
import { Jeu } from 'app/shared/model/jeu.model';

describe('Component Tests', () => {
  describe('Jeu Management Update Component', () => {
    let comp: JeuUpdateComponent;
    let fixture: ComponentFixture<JeuUpdateComponent>;
    let service: JeuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [JeuUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JeuUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JeuUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JeuService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Jeu(123);
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
        const entity = new Jeu();
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
