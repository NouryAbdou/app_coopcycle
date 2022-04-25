import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurateursService } from '../service/restaurateurs.service';
import { IRestaurateurs, Restaurateurs } from '../restaurateurs.model';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';
import { CooperativesService } from 'app/entities/cooperatives/service/cooperatives.service';

import { RestaurateursUpdateComponent } from './restaurateurs-update.component';

describe('Restaurateurs Management Update Component', () => {
  let comp: RestaurateursUpdateComponent;
  let fixture: ComponentFixture<RestaurateursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurateursService: RestaurateursService;
  let cooperativesService: CooperativesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurateursUpdateComponent],
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
      .overrideTemplate(RestaurateursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurateursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurateursService = TestBed.inject(RestaurateursService);
    cooperativesService = TestBed.inject(CooperativesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cooperatives query and add missing value', () => {
      const restaurateurs: IRestaurateurs = { id: 456 };
      const cooperative: ICooperatives = { id: 85832 };
      restaurateurs.cooperative = cooperative;

      const cooperativesCollection: ICooperatives[] = [{ id: 25028 }];
      jest.spyOn(cooperativesService, 'query').mockReturnValue(of(new HttpResponse({ body: cooperativesCollection })));
      const additionalCooperatives = [cooperative];
      const expectedCollection: ICooperatives[] = [...additionalCooperatives, ...cooperativesCollection];
      jest.spyOn(cooperativesService, 'addCooperativesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurateurs });
      comp.ngOnInit();

      expect(cooperativesService.query).toHaveBeenCalled();
      expect(cooperativesService.addCooperativesToCollectionIfMissing).toHaveBeenCalledWith(
        cooperativesCollection,
        ...additionalCooperatives
      );
      expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurateurs: IRestaurateurs = { id: 456 };
      const cooperative: ICooperatives = { id: 11373 };
      restaurateurs.cooperative = cooperative;

      activatedRoute.data = of({ restaurateurs });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurateurs));
      expect(comp.cooperativesSharedCollection).toContain(cooperative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateurs>>();
      const restaurateurs = { id: 123 };
      jest.spyOn(restaurateursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurateurs }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurateursService.update).toHaveBeenCalledWith(restaurateurs);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateurs>>();
      const restaurateurs = new Restaurateurs();
      jest.spyOn(restaurateursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurateurs }));
      saveSubject.complete();

      // THEN
      expect(restaurateursService.create).toHaveBeenCalledWith(restaurateurs);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Restaurateurs>>();
      const restaurateurs = { id: 123 };
      jest.spyOn(restaurateursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurateurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurateursService.update).toHaveBeenCalledWith(restaurateurs);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCooperativesById', () => {
      it('Should return tracked Cooperatives primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCooperativesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
