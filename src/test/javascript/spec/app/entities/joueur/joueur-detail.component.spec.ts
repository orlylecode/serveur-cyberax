/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { JoueurDetailComponent } from 'app/entities/joueur/joueur-detail.component';
import { Joueur } from 'app/shared/model/joueur.model';

describe('Component Tests', () => {
  describe('Joueur Management Detail Component', () => {
    let comp: JoueurDetailComponent;
    let fixture: ComponentFixture<JoueurDetailComponent>;
    const route = ({ data: of({ joueur: new Joueur(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [JoueurDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JoueurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JoueurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.joueur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
