import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ZonesService } from '../service/zones.service';
import { IZones, Zones } from '../zones.model';

import { ZonesUpdateComponent } from './zones-update.component';

describe('Zones Management Update Component', () => {
  let comp: ZonesUpdateComponent;
  let fixture: ComponentFixture<ZonesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let zonesService: ZonesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ZonesUpdateComponent],
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
      .overrideTemplate(ZonesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ZonesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    zonesService = TestBed.inject(ZonesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const zones: IZones = { id: 456 };

      activatedRoute.data = of({ zones });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(zones));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Zones>>();
      const zones = { id: 123 };
      jest.spyOn(zonesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zones }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(zonesService.update).toHaveBeenCalledWith(zones);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Zones>>();
      const zones = new Zones();
      jest.spyOn(zonesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zones }));
      saveSubject.complete();

      // THEN
      expect(zonesService.create).toHaveBeenCalledWith(zones);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Zones>>();
      const zones = { id: 123 };
      jest.spyOn(zonesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(zonesService.update).toHaveBeenCalledWith(zones);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
