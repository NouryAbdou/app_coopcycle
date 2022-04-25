import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClientsService } from '../service/clients.service';
import { IClients, Clients } from '../clients.model';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';

import { ClientsUpdateComponent } from './clients-update.component';

describe('Clients Management Update Component', () => {
  let comp: ClientsUpdateComponent;
  let fixture: ComponentFixture<ClientsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clientsService: ClientsService;
  let restaurateursService: RestaurateursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClientsUpdateComponent],
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
      .overrideTemplate(ClientsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClientsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clientsService = TestBed.inject(ClientsService);
    restaurateursService = TestBed.inject(RestaurateursService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call restaurateur query and add missing value', () => {
      const clients: IClients = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 50015 };
      clients.restaurateur = restaurateur;

      const restaurateurCollection: IRestaurateurs[] = [{ id: 70020 }];
      jest.spyOn(restaurateursService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurateurCollection })));
      const expectedCollection: IRestaurateurs[] = [restaurateur, ...restaurateurCollection];
      jest.spyOn(restaurateursService, 'addRestaurateursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      expect(restaurateursService.query).toHaveBeenCalled();
      expect(restaurateursService.addRestaurateursToCollectionIfMissing).toHaveBeenCalledWith(restaurateurCollection, restaurateur);
      expect(comp.restaurateursCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const clients: IClients = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 34851 };
      clients.restaurateur = restaurateur;

      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(clients));
      expect(comp.restaurateursCollection).toContain(restaurateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clients>>();
      const clients = { id: 123 };
      jest.spyOn(clientsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clients }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(clientsService.update).toHaveBeenCalledWith(clients);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clients>>();
      const clients = new Clients();
      jest.spyOn(clientsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clients }));
      saveSubject.complete();

      // THEN
      expect(clientsService.create).toHaveBeenCalledWith(clients);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clients>>();
      const clients = { id: 123 };
      jest.spyOn(clientsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clientsService.update).toHaveBeenCalledWith(clients);
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
