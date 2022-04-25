import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IZones, Zones } from '../zones.model';

import { ZonesService } from './zones.service';

describe('Zones Service', () => {
  let service: ZonesService;
  let httpMock: HttpTestingController;
  let elemDefault: IZones;
  let expectedResult: IZones | IZones[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ZonesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ville: 'AAAAAAA',
      metropole: 'AAAAAAA',
      communaute: 'AAAAAAA',
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

    it('should create a Zones', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Zones()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Zones', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ville: 'BBBBBB',
          metropole: 'BBBBBB',
          communaute: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Zones', () => {
      const patchObject = Object.assign(
        {
          ville: 'BBBBBB',
        },
        new Zones()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Zones', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ville: 'BBBBBB',
          metropole: 'BBBBBB',
          communaute: 'BBBBBB',
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

    it('should delete a Zones', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addZonesToCollectionIfMissing', () => {
      it('should add a Zones to an empty array', () => {
        const zones: IZones = { id: 123 };
        expectedResult = service.addZonesToCollectionIfMissing([], zones);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zones);
      });

      it('should not add a Zones to an array that contains it', () => {
        const zones: IZones = { id: 123 };
        const zonesCollection: IZones[] = [
          {
            ...zones,
          },
          { id: 456 },
        ];
        expectedResult = service.addZonesToCollectionIfMissing(zonesCollection, zones);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Zones to an array that doesn't contain it", () => {
        const zones: IZones = { id: 123 };
        const zonesCollection: IZones[] = [{ id: 456 }];
        expectedResult = service.addZonesToCollectionIfMissing(zonesCollection, zones);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zones);
      });

      it('should add only unique Zones to an array', () => {
        const zonesArray: IZones[] = [{ id: 123 }, { id: 456 }, { id: 34013 }];
        const zonesCollection: IZones[] = [{ id: 123 }];
        expectedResult = service.addZonesToCollectionIfMissing(zonesCollection, ...zonesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const zones: IZones = { id: 123 };
        const zones2: IZones = { id: 456 };
        expectedResult = service.addZonesToCollectionIfMissing([], zones, zones2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zones);
        expect(expectedResult).toContain(zones2);
      });

      it('should accept null and undefined values', () => {
        const zones: IZones = { id: 123 };
        expectedResult = service.addZonesToCollectionIfMissing([], null, zones, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zones);
      });

      it('should return initial array if no Zones is added', () => {
        const zonesCollection: IZones[] = [{ id: 123 }];
        expectedResult = service.addZonesToCollectionIfMissing(zonesCollection, undefined, null);
        expect(expectedResult).toEqual(zonesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
