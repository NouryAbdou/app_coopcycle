import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRestaurateurs, Restaurateurs } from '../restaurateurs.model';

import { RestaurateursService } from './restaurateurs.service';

describe('Restaurateurs Service', () => {
  let service: RestaurateursService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurateurs;
  let expectedResult: IRestaurateurs | IRestaurateurs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurateursService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      city: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Restaurateurs', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Restaurateurs()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restaurateurs', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          city: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Restaurateurs', () => {
      const patchObject = Object.assign(
        {
          prenom: 'BBBBBB',
        },
        new Restaurateurs()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restaurateurs', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          city: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Restaurateurs', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurateursToCollectionIfMissing', () => {
      it('should add a Restaurateurs to an empty array', () => {
        const restaurateurs: IRestaurateurs = { id: 123 };
        expectedResult = service.addRestaurateursToCollectionIfMissing([], restaurateurs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurateurs);
      });

      it('should not add a Restaurateurs to an array that contains it', () => {
        const restaurateurs: IRestaurateurs = { id: 123 };
        const restaurateursCollection: IRestaurateurs[] = [
          {
            ...restaurateurs,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurateursToCollectionIfMissing(restaurateursCollection, restaurateurs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restaurateurs to an array that doesn't contain it", () => {
        const restaurateurs: IRestaurateurs = { id: 123 };
        const restaurateursCollection: IRestaurateurs[] = [{ id: 456 }];
        expectedResult = service.addRestaurateursToCollectionIfMissing(restaurateursCollection, restaurateurs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurateurs);
      });

      it('should add only unique Restaurateurs to an array', () => {
        const restaurateursArray: IRestaurateurs[] = [{ id: 123 }, { id: 456 }, { id: 45207 }];
        const restaurateursCollection: IRestaurateurs[] = [{ id: 123 }];
        expectedResult = service.addRestaurateursToCollectionIfMissing(restaurateursCollection, ...restaurateursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurateurs: IRestaurateurs = { id: 123 };
        const restaurateurs2: IRestaurateurs = { id: 456 };
        expectedResult = service.addRestaurateursToCollectionIfMissing([], restaurateurs, restaurateurs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurateurs);
        expect(expectedResult).toContain(restaurateurs2);
      });

      it('should accept null and undefined values', () => {
        const restaurateurs: IRestaurateurs = { id: 123 };
        expectedResult = service.addRestaurateursToCollectionIfMissing([], null, restaurateurs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurateurs);
      });

      it('should return initial array if no Restaurateurs is added', () => {
        const restaurateursCollection: IRestaurateurs[] = [{ id: 123 }];
        expectedResult = service.addRestaurateursToCollectionIfMissing(restaurateursCollection, undefined, null);
        expect(expectedResult).toEqual(restaurateursCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
