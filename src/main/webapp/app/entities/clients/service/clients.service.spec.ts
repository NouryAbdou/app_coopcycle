import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClients, Clients } from '../clients.model';

import { ClientsService } from './clients.service';

describe('Clients Service', () => {
  let service: ClientsService;
  let httpMock: HttpTestingController;
  let elemDefault: IClients;
  let expectedResult: IClients | IClients[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClientsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      email: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
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

    it('should create a Clients', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Clients()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Clients', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Clients', () => {
      const patchObject = Object.assign(
        {
          prenom: 'BBBBBB',
          email: 'BBBBBB',
        },
        new Clients()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Clients', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
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

    it('should delete a Clients', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClientsToCollectionIfMissing', () => {
      it('should add a Clients to an empty array', () => {
        const clients: IClients = { id: 123 };
        expectedResult = service.addClientsToCollectionIfMissing([], clients);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clients);
      });

      it('should not add a Clients to an array that contains it', () => {
        const clients: IClients = { id: 123 };
        const clientsCollection: IClients[] = [
          {
            ...clients,
          },
          { id: 456 },
        ];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, clients);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Clients to an array that doesn't contain it", () => {
        const clients: IClients = { id: 123 };
        const clientsCollection: IClients[] = [{ id: 456 }];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, clients);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clients);
      });

      it('should add only unique Clients to an array', () => {
        const clientsArray: IClients[] = [{ id: 123 }, { id: 456 }, { id: 92783 }];
        const clientsCollection: IClients[] = [{ id: 123 }];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, ...clientsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const clients: IClients = { id: 123 };
        const clients2: IClients = { id: 456 };
        expectedResult = service.addClientsToCollectionIfMissing([], clients, clients2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clients);
        expect(expectedResult).toContain(clients2);
      });

      it('should accept null and undefined values', () => {
        const clients: IClients = { id: 123 };
        expectedResult = service.addClientsToCollectionIfMissing([], null, clients, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clients);
      });

      it('should return initial array if no Clients is added', () => {
        const clientsCollection: IClients[] = [{ id: 123 }];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, undefined, null);
        expect(expectedResult).toEqual(clientsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
