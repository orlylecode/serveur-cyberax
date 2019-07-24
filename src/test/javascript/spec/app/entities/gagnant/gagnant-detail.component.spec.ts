/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { GagnantDetailComponent } from 'app/entities/gagnant/gagnant-detail.component';
import { Gagnant } from 'app/shared/model/gagnant.model';

describe('Component Tests', () => {
  describe('Gagnant Management Detail Component', () => {
    let comp: GagnantDetailComponent;
    let fixture: ComponentFixture<GagnantDetailComponent>;
    const route = ({ data: of({ gagnant: new Gagnant(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [GagnantDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GagnantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GagnantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gagnant).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
