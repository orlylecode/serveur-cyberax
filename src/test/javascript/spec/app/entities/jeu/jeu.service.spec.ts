/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JeuService } from 'app/entities/jeu/jeu.service';
import { IJeu, Jeu } from 'app/shared/model/jeu.model';

describe('Service Tests', () => {
  describe('Jeu Service', () => {
    let injector: TestBed;
    let service: JeuService;
    let httpMock: HttpTestingController;
    let elemDefault: IJeu;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(JeuService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Jeu(0, currentDate, currentDate, currentDate, false, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            dateCloture: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Jeu', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            dateCloture: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateLancement: currentDate,
            dateCloture: currentDate
          },
          returnedFromService
        );
        service
          .create(new Jeu(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Jeu', async () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            dateCloture: currentDate.format(DATE_TIME_FORMAT),
            encour: true,
            pourcentageMise: 1,
            pourcentageRebourt: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateLancement: currentDate,
            dateCloture: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Jeu', async () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            dateCloture: currentDate.format(DATE_TIME_FORMAT),
            encour: true,
            pourcentageMise: 1,
            pourcentageRebourt: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateLancement: currentDate,
            dateCloture: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Jeu', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
