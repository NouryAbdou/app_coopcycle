import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILivreurs, Livreurs } from '../livreurs.model';

import { LivreursService } from './livreurs.service';

describe('Livreurs Service', () => {
  let service: LivreursService;
  let httpMock: HttpTestingController;
  let elemDefault: ILivreurs;
  let expectedResult: ILivreurs | ILivreurs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LivreursService);
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

    it('should create a Livreurs', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Livreurs()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Livreurs', () => {
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

    it('should partial update a Livreurs', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
        },
        new Livreurs()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Livreurs', () => {
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

    it('should delete a Livreurs', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLivreursToCollectionIfMissing', () => {
      it('should add a Livreurs to an empty array', () => {
        const livreurs: ILivreurs = { id: 123 };
        expectedResult = service.addLivreursToCollectionIfMissing([], livreurs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livreurs);
      });

      it('should not add a Livreurs to an array that contains it', () => {
        const livreurs: ILivreurs = { id: 123 };
        const livreursCollection: ILivreurs[] = [
          {
            ...livreurs,
          },
          { id: 456 },
        ];
        expectedResult = service.addLivreursToCollectionIfMissing(livreursCollection, livreurs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Livreurs to an array that doesn't contain it", () => {
        const livreurs: ILivreurs = { id: 123 };
        const livreursCollection: ILivreurs[] = [{ id: 456 }];
        expectedResult = service.addLivreursToCollectionIfMissing(livreursCollection, livreurs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livreurs);
      });

      it('should add only unique Livreurs to an array', () => {
        const livreursArray: ILivreurs[] = [{ id: 123 }, { id: 456 }, { id: 97013 }];
        const livreursCollection: ILivreurs[] = [{ id: 123 }];
        expectedResult = service.addLivreursToCollectionIfMissing(livreursCollection, ...livreursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const livreurs: ILivreurs = { id: 123 };
        const livreurs2: ILivreurs = { id: 456 };
        expectedResult = service.addLivreursToCollectionIfMissing([], livreurs, livreurs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livreurs);
        expect(expectedResult).toContain(livreurs2);
      });

      it('should accept null and undefined values', () => {
        const livreurs: ILivreurs = { id: 123 };
        expectedResult = service.addLivreursToCollectionIfMissing([], null, livreurs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livreurs);
      });

      it('should return initial array if no Livreurs is added', () => {
        const livreursCollection: ILivreurs[] = [{ id: 123 }];
        expectedResult = service.addLivreursToCollectionIfMissing(livreursCollection, undefined, null);
        expect(expectedResult).toEqual(livreursCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
