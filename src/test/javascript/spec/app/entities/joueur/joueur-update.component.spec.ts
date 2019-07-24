/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { JoueurUpdateComponent } from 'app/entities/joueur/joueur-update.component';
import { JoueurService } from 'app/entities/joueur/joueur.service';
import { Joueur } from 'app/shared/model/joueur.model';

describe('Component Tests', () => {
  describe('Joueur Management Update Component', () => {
    let comp: JoueurUpdateComponent;
    let fixture: ComponentFixture<JoueurUpdateComponent>;
    let service: JoueurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [JoueurUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JoueurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JoueurUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JoueurService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Joueur(123);
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
        const entity = new Joueur();
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
