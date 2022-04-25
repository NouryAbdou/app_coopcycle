import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivreursService } from '../service/livreurs.service';
import { ILivreurs, Livreurs } from '../livreurs.model';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';
import { CooperativesService } from 'app/entities/cooperatives/service/cooperatives.service';

import { LivreursUpdateComponent } from './livreurs-update.component';

describe('Livreurs Management Update Component', () => {
  let comp: LivreursUpdateComponent;
  let fixture: ComponentFixture<LivreursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livreursService: LivreursService;
  let restaurateursService: RestaurateursService;
  let cooperativesService: CooperativesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivreursUpdateComponent],
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
      .overrideTemplate(LivreursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivreursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livreursService = TestBed.inject(LivreursService);
    restaurateursService = TestBed.inject(RestaurateursService);
    cooperativesService = TestBed.inject(CooperativesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call restaurateur query and add missing value', () => {
      const livreurs: ILivreurs = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 92203 };
      livreurs.restaurateur = restaurateur;

      const restaurateurCollection: IRestaurateurs[] = [{ id: 95848 }];
      jest.spyOn(restaurateursService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurateurCollection })));
      const expectedCollection: IRestaurateurs[] = [restaurateur, ...restaurateurCollection];
      jest.spyOn(restaurateursService, 'addRestaurateursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      expect(restaurateursService.query).toHaveBeenCalled();
      expect(restaurateursService.addRestaurateursToCollectionIfMissing).toHaveBeenCalledWith(restaurateurCollection, restaurateur);
      expect(comp.restaurateursCollection).toEqual(expectedCollection);
    });

    it('Should call Cooperatives query and add missing value', () => {
      const livreurs: ILivreurs = { id: 456 };
      const cooperative: ICooperatives = { id: 36019 };
      livreurs.cooperative = cooperative;

      const cooperativesCollection: ICooperatives[] = [{ id: 33456 }];
      jest.spyOn(cooperativesService, 'query').mockReturnValue(of(new HttpResponse({ body: cooperativesCollection })));
      const additionalCooperatives = [cooperative];
      const expectedCollection: ICooperatives[] = [...additionalCooperatives, ...cooperativesCollection];
      jest.spyOn(cooperativesService, 'addCooperativesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      expect(cooperativesService.query).toHaveBeenCalled();
      expect(cooperativesService.addCooperativesToCollectionIfMissing).toHaveBeenCalledWith(
        cooperativesCollection,
        ...additionalCooperatives
      );
      expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const livreurs: ILivreurs = { id: 456 };
      const restaurateur: IRestaurateurs = { id: 23547 };
      livreurs.restaurateur = restaurateur;
      const cooperative: ICooperatives = { id: 51068 };
      livreurs.cooperative = cooperative;

      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(livreurs));
      expect(comp.restaurateursCollection).toContain(restaurateur);
      expect(comp.cooperativesSharedCollection).toContain(cooperative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreurs>>();
      const livreurs = { id: 123 };
      jest.spyOn(livreursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreurs }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(livreursService.update).toHaveBeenCalledWith(livreurs);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreurs>>();
      const livreurs = new Livreurs();
      jest.spyOn(livreursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreurs }));
      saveSubject.complete();

      // THEN
      expect(livreursService.create).toHaveBeenCalledWith(livreurs);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreurs>>();
      const livreurs = { id: 123 };
      jest.spyOn(livreursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreurs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livreursService.update).toHaveBeenCalledWith(livreurs);
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

    describe('trackCooperativesById', () => {
      it('Should return tracked Cooperatives primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCooperativesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
