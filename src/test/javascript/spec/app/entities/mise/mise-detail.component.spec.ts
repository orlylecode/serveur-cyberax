/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { MiseDetailComponent } from 'app/entities/mise/mise-detail.component';
import { Mise } from 'app/shared/model/mise.model';

describe('Component Tests', () => {
  describe('Mise Management Detail Component', () => {
    let comp: MiseDetailComponent;
    let fixture: ComponentFixture<MiseDetailComponent>;
    const route = ({ data: of({ mise: new Mise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [MiseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MiseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MiseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
