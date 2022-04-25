import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CooperativesService } from '../service/cooperatives.service';
import { ICooperatives, Cooperatives } from '../cooperatives.model';
import { IZones } from 'app/entities/zones/zones.model';
import { ZonesService } from 'app/entities/zones/service/zones.service';

import { CooperativesUpdateComponent } from './cooperatives-update.component';

describe('Cooperatives Management Update Component', () => {
  let comp: CooperativesUpdateComponent;
  let fixture: ComponentFixture<CooperativesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cooperativesService: CooperativesService;
  let zonesService: ZonesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CooperativesUpdateComponent],
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
      .overrideTemplate(CooperativesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CooperativesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cooperativesService = TestBed.inject(CooperativesService);
    zonesService = TestBed.inject(ZonesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Zones query and add missing value', () => {
      const cooperatives: ICooperatives = { id: 456 };
      const zone: IZones = { id: 97132 };
      cooperatives.zone = zone;

      const zonesCollection: IZones[] = [{ id: 71819 }];
      jest.spyOn(zonesService, 'query').mockReturnValue(of(new HttpResponse({ body: zonesCollection })));
      const additionalZones = [zone];
      const expectedCollection: IZones[] = [...additionalZones, ...zonesCollection];
      jest.spyOn(zonesService, 'addZonesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cooperatives });
      comp.ngOnInit();

      expect(zonesService.query).toHaveBeenCalled();
      expect(zonesService.addZonesToCollectionIfMissing).toHaveBeenCalledWith(zonesCollection, ...additionalZones);
      expect(comp.zonesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cooperatives: ICooperatives = { id: 456 };
      const zone: IZones = { id: 51421 };
      cooperatives.zone = zone;

      activatedRoute.data = of({ cooperatives });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cooperatives));
      expect(comp.zonesSharedCollection).toContain(zone);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperatives>>();
      const cooperatives = { id: 123 };
      jest.spyOn(cooperativesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperatives });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cooperatives }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cooperativesService.update).toHaveBeenCalledWith(cooperatives);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperatives>>();
      const cooperatives = new Cooperatives();
      jest.spyOn(cooperativesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperatives });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cooperatives }));
      saveSubject.complete();

      // THEN
      expect(cooperativesService.create).toHaveBeenCalledWith(cooperatives);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cooperatives>>();
      const cooperatives = { id: 123 };
      jest.spyOn(cooperativesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cooperatives });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cooperativesService.update).toHaveBeenCalledWith(cooperatives);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackZonesById', () => {
      it('Should return tracked Zones primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackZonesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
