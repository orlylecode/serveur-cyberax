/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { ListAttenteDetailComponent } from 'app/entities/list-attente/list-attente-detail.component';
import { ListAttente } from 'app/shared/model/list-attente.model';

describe('Component Tests', () => {
  describe('ListAttente Management Detail Component', () => {
    let comp: ListAttenteDetailComponent;
    let fixture: ComponentFixture<ListAttenteDetailComponent>;
    const route = ({ data: of({ listAttente: new ListAttente(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [ListAttenteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ListAttenteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ListAttenteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.listAttente).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
