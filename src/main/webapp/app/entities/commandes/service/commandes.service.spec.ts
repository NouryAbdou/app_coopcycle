import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommandes, Commandes } from '../commandes.model';

import { CommandesService } from './commandes.service';

describe('Commandes Service', () => {
  let service: CommandesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommandes;
  let expectedResult: ICommandes | ICommandes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommandesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      estPret: false,
      estPaye: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Commandes', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Commandes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commandes', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          estPret: true,
          estPaye: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commandes', () => {
      const patchObject = Object.assign({}, new Commandes());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commandes', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          estPret: true,
          estPaye: true,
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

    it('should delete a Commandes', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommandesToCollectionIfMissing', () => {
      it('should add a Commandes to an empty array', () => {
        const commandes: ICommandes = { id: 'ABC' };
        expectedResult = service.addCommandesToCollectionIfMissing([], commandes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandes);
      });

      it('should not add a Commandes to an array that contains it', () => {
        const commandes: ICommandes = { id: 'ABC' };
        const commandesCollection: ICommandes[] = [
          {
            ...commandes,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addCommandesToCollectionIfMissing(commandesCollection, commandes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commandes to an array that doesn't contain it", () => {
        const commandes: ICommandes = { id: 'ABC' };
        const commandesCollection: ICommandes[] = [{ id: 'CBA' }];
        expectedResult = service.addCommandesToCollectionIfMissing(commandesCollection, commandes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandes);
      });

      it('should add only unique Commandes to an array', () => {
        const commandesArray: ICommandes[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '28c22108-9774-485e-b831-e28ad5b9b505' }];
        const commandesCollection: ICommandes[] = [{ id: 'ABC' }];
        expectedResult = service.addCommandesToCollectionIfMissing(commandesCollection, ...commandesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commandes: ICommandes = { id: 'ABC' };
        const commandes2: ICommandes = { id: 'CBA' };
        expectedResult = service.addCommandesToCollectionIfMissing([], commandes, commandes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandes);
        expect(expectedResult).toContain(commandes2);
      });

      it('should accept null and undefined values', () => {
        const commandes: ICommandes = { id: 'ABC' };
        expectedResult = service.addCommandesToCollectionIfMissing([], null, commandes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandes);
      });

      it('should return initial array if no Commandes is added', () => {
        const commandesCollection: ICommandes[] = [{ id: 'ABC' }];
        expectedResult = service.addCommandesToCollectionIfMissing(commandesCollection, undefined, null);
        expect(expectedResult).toEqual(commandesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
