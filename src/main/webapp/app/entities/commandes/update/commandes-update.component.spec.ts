import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandesService } from '../service/commandes.service';
import { ICommandes, Commandes } from '../commandes.model';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';
import { IRestaurants } from 'app/entities/restaurants/restaurants.model';
import { RestaurantsService } from 'app/entities/restaurants/service/restaurants.service';

import { CommandesUpdateComponent } from './commandes-update.component';

describe('Commandes Management Update Component', () => {
  let comp: CommandesUpdateComponent;
  let fixture: ComponentFixture<CommandesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandesService: CommandesService;
  let clientsService: ClientsService;
  let restaurantsService: RestaurantsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommandesUpdateComponent],
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
      .overrideTemplate(CommandesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandesService = TestBed.inject(CommandesService);
    clientsService = TestBed.inject(ClientsService);
    restaurantsService = TestBed.inject(RestaurantsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Clients query and add missing value', () => {
      const commandes: ICommandes = { id: 'CBA' };
      const client: IClients = { id: 3725 };
      commandes.client = client;

      const clientsCollection: IClients[] = [{ id: 17425 }];
      jest.spyOn(clientsService, 'query').mockReturnValue(of(new HttpResponse({ body: clientsCollection })));
      const additionalClients = [client];
      const expectedCollection: IClients[] = [...additionalClients, ...clientsCollection];
      jest.spyOn(clientsService, 'addClientsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      expect(clientsService.query).toHaveBeenCalled();
      expect(clientsService.addClientsToCollectionIfMissing).toHaveBeenCalledWith(clientsCollection, ...additionalClients);
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Restaurants query and add missing value', () => {
      const commandes: ICommandes = { id: 'CBA' };
      const restaurants: IRestaurants[] = [{ id: 49731 }];
      commandes.restaurants = restaurants;

      const restaurantsCollection: IRestaurants[] = [{ id: 55087 }];
      jest.spyOn(restaurantsService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantsCollection })));
      const additionalRestaurants = [...restaurants];
      const expectedCollection: IRestaurants[] = [...additionalRestaurants, ...restaurantsCollection];
      jest.spyOn(restaurantsService, 'addRestaurantsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      expect(restaurantsService.query).toHaveBeenCalled();
      expect(restaurantsService.addRestaurantsToCollectionIfMissing).toHaveBeenCalledWith(restaurantsCollection, ...additionalRestaurants);
      expect(comp.restaurantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commandes: ICommandes = { id: 'CBA' };
      const client: IClients = { id: 86997 };
      commandes.client = client;
      const restaurants: IRestaurants = { id: 28205 };
      commandes.restaurants = [restaurants];

      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commandes));
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.restaurantsSharedCollection).toContain(restaurants);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commandes>>();
      const commandes = { id: 'ABC' };
      jest.spyOn(commandesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandesService.update).toHaveBeenCalledWith(commandes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commandes>>();
      const commandes = new Commandes();
      jest.spyOn(commandesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandes }));
      saveSubject.complete();

      // THEN
      expect(commandesService.create).toHaveBeenCalledWith(commandes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commandes>>();
      const commandes = { id: 'ABC' };
      jest.spyOn(commandesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandesService.update).toHaveBeenCalledWith(commandes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClientsById', () => {
      it('Should return tracked Clients primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClientsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRestaurantsById', () => {
      it('Should return tracked Restaurants primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurantsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedRestaurants', () => {
      it('Should return option if no Restaurants is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedRestaurants(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Restaurants for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedRestaurants(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Restaurants is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedRestaurants(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
