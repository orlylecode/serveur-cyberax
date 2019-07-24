/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServeurCyberaxTestModule } from '../../../test.module';
import { TerminalDetailComponent } from 'app/entities/terminal/terminal-detail.component';
import { Terminal } from 'app/shared/model/terminal.model';

describe('Component Tests', () => {
  describe('Terminal Management Detail Component', () => {
    let comp: TerminalDetailComponent;
    let fixture: ComponentFixture<TerminalDetailComponent>;
    const route = ({ data: of({ terminal: new Terminal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServeurCyberaxTestModule],
        declarations: [TerminalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TerminalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TerminalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.terminal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
