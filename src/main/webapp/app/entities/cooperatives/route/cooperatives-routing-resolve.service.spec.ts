import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICooperatives, Cooperatives } from '../cooperatives.model';
import { CooperativesService } from '../service/cooperatives.service';

import { CooperativesRoutingResolveService } from './cooperatives-routing-resolve.service';

describe('Cooperatives routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CooperativesRoutingResolveService;
  let service: CooperativesService;
  let resultCooperatives: ICooperatives | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CooperativesRoutingResolveService);
    service = TestBed.inject(CooperativesService);
    resultCooperatives = undefined;
  });

  describe('resolve', () => {
    it('should return ICooperatives returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCooperatives = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCooperatives).toEqual({ id: 123 });
    });

    it('should return new ICooperatives if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCooperatives = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCooperatives).toEqual(new Cooperatives());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cooperatives })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCooperatives = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCooperatives).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
