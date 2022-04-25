import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICooperatives, Cooperatives } from '../cooperatives.model';

import { CooperativesService } from './cooperatives.service';

describe('Cooperatives Service', () => {
  let service: CooperativesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICooperatives;
  let expectedResult: ICooperatives | ICooperatives[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CooperativesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
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

    it('should create a Cooperatives', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cooperatives()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cooperatives', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cooperatives', () => {
      const patchObject = Object.assign({}, new Cooperatives());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cooperatives', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
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

    it('should delete a Cooperatives', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCooperativesToCollectionIfMissing', () => {
      it('should add a Cooperatives to an empty array', () => {
        const cooperatives: ICooperatives = { id: 123 };
        expectedResult = service.addCooperativesToCollectionIfMissing([], cooperatives);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cooperatives);
      });

      it('should not add a Cooperatives to an array that contains it', () => {
        const cooperatives: ICooperatives = { id: 123 };
        const cooperativesCollection: ICooperatives[] = [
          {
            ...cooperatives,
          },
          { id: 456 },
        ];
        expectedResult = service.addCooperativesToCollectionIfMissing(cooperativesCollection, cooperatives);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cooperatives to an array that doesn't contain it", () => {
        const cooperatives: ICooperatives = { id: 123 };
        const cooperativesCollection: ICooperatives[] = [{ id: 456 }];
        expectedResult = service.addCooperativesToCollectionIfMissing(cooperativesCollection, cooperatives);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cooperatives);
      });

      it('should add only unique Cooperatives to an array', () => {
        const cooperativesArray: ICooperatives[] = [{ id: 123 }, { id: 456 }, { id: 6371 }];
        const cooperativesCollection: ICooperatives[] = [{ id: 123 }];
        expectedResult = service.addCooperativesToCollectionIfMissing(cooperativesCollection, ...cooperativesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cooperatives: ICooperatives = { id: 123 };
        const cooperatives2: ICooperatives = { id: 456 };
        expectedResult = service.addCooperativesToCollectionIfMissing([], cooperatives, cooperatives2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cooperatives);
        expect(expectedResult).toContain(cooperatives2);
      });

      it('should accept null and undefined values', () => {
        const cooperatives: ICooperatives = { id: 123 };
        expectedResult = service.addCooperativesToCollectionIfMissing([], null, cooperatives, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cooperatives);
      });

      it('should return initial array if no Cooperatives is added', () => {
        const cooperativesCollection: ICooperatives[] = [{ id: 123 }];
        expectedResult = service.addCooperativesToCollectionIfMissing(cooperativesCollection, undefined, null);
        expect(expectedResult).toEqual(cooperativesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
