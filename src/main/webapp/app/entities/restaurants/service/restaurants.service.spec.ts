import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRestaurants, Restaurants } from '../restaurants.model';

import { RestaurantsService } from './restaurants.service';

describe('Restaurants Service', () => {
  let service: RestaurantsService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurants;
  let expectedResult: IRestaurants | IRestaurants[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurantsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      carte: 'AAAAAAA',
      menu: 'AAAAAAA',
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

    it('should create a Restaurants', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Restaurants()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restaurants', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          carte: 'BBBBBB',
          menu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Restaurants', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          carte: 'BBBBBB',
        },
        new Restaurants()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restaurants', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          carte: 'BBBBBB',
          menu: 'BBBBBB',
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

    it('should delete a Restaurants', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurantsToCollectionIfMissing', () => {
      it('should add a Restaurants to an empty array', () => {
        const restaurants: IRestaurants = { id: 123 };
        expectedResult = service.addRestaurantsToCollectionIfMissing([], restaurants);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurants);
      });

      it('should not add a Restaurants to an array that contains it', () => {
        const restaurants: IRestaurants = { id: 123 };
        const restaurantsCollection: IRestaurants[] = [
          {
            ...restaurants,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurantsToCollectionIfMissing(restaurantsCollection, restaurants);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restaurants to an array that doesn't contain it", () => {
        const restaurants: IRestaurants = { id: 123 };
        const restaurantsCollection: IRestaurants[] = [{ id: 456 }];
        expectedResult = service.addRestaurantsToCollectionIfMissing(restaurantsCollection, restaurants);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurants);
      });

      it('should add only unique Restaurants to an array', () => {
        const restaurantsArray: IRestaurants[] = [{ id: 123 }, { id: 456 }, { id: 13674 }];
        const restaurantsCollection: IRestaurants[] = [{ id: 123 }];
        expectedResult = service.addRestaurantsToCollectionIfMissing(restaurantsCollection, ...restaurantsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurants: IRestaurants = { id: 123 };
        const restaurants2: IRestaurants = { id: 456 };
        expectedResult = service.addRestaurantsToCollectionIfMissing([], restaurants, restaurants2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurants);
        expect(expectedResult).toContain(restaurants2);
      });

      it('should accept null and undefined values', () => {
        const restaurants: IRestaurants = { id: 123 };
        expectedResult = service.addRestaurantsToCollectionIfMissing([], null, restaurants, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurants);
      });

      it('should return initial array if no Restaurants is added', () => {
        const restaurantsCollection: IRestaurants[] = [{ id: 123 }];
        expectedResult = service.addRestaurantsToCollectionIfMissing(restaurantsCollection, undefined, null);
        expect(expectedResult).toEqual(restaurantsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
