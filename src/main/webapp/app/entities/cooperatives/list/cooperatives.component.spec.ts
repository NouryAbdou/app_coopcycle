import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CooperativesService } from '../service/cooperatives.service';

import { CooperativesComponent } from './cooperatives.component';

describe('Cooperatives Management Component', () => {
  let comp: CooperativesComponent;
  let fixture: ComponentFixture<CooperativesComponent>;
  let service: CooperativesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CooperativesComponent],
    })
      .overrideTemplate(CooperativesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CooperativesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CooperativesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.cooperatives?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
