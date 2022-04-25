import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurantsService } from '../service/restaurants.service';
import { IRestaurants, Restaurants } from '../restaurants.model';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';

import { RestaurantsUpdateComponent } from './restaurants-update.component';

describe('Restaurants Management Update Component', () => {
  let comp: RestaurantsUpdateComponent;
  let fixture: ComponentFixture<RestaurantsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurantsService: RestaurantsService;
  let restaurateursService: RestaurateursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurantsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RestaurantsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurantsService = TestBed.inject(RestaurantsService);
    restaurateursService = TestBed.inject(RestaurateursService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Restaurateurs query and add missing value', () => {
      const restaurants: IRestaurants = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 51055 };
      restaurants.restaurateur = restaurateur;

      const restaurateursCollection: IRestaurateurs[] = [{ id: 51179 }];
      jest.spyOn(restaurateursService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurateursCollection })));
      const additionalRestaurateurs = [restaurateur];
      const expectedCollection: IRestaurateurs[] = [...additionalRestaurateurs, ...restaurateursCollection];
      jest.spyOn(restaurateursService, 'addRestaurateursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurants });
      comp.ngOnInit();

      expect(restaurateursService.query).toHaveBeenCalled();
      expect(restaurateursService.addRestaurateursToCollectionIfMissing).toHaveBeenCalledWith(
        restaurateursCollection,
        ...additionalRestaurateurs
      );
      expect(comp.restaurateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurants: IRestaurants = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 88079 };
      restaurants.restaurateur = restaurateur;

      activatedRoute.data = of({ restaurants });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurants));
      expect(comp.restaurateursSharedCollection).toContain(restaurateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurants>>();
      const restaurants = { id: 123 };
      jest.spyOn(restaurantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurants }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurantsService.update).toHaveBeenCalledWith(restaurants);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurants>>();
      const restaurants = new Restaurants();
      jest.spyOn(restaurantsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurants }));
      saveSubject.complete();

      // THEN
      expect(restaurantsService.create).toHaveBeenCalledWith(restaurants);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurants>>();
      const restaurants = { id: 123 };
      jest.spyOn(restaurantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurantsService.update).toHaveBeenCalledWith(restaurants);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRestaurateursById', () => {
      it('Should return tracked Restaurateurs primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurateursById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
