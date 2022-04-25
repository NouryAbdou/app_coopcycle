import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RestaurateursService } from '../service/restaurateurs.service';

import { RestaurateursComponent } from './restaurateurs.component';

describe('Restaurateurs Management Component', () => {
  let comp: RestaurateursComponent;
  let fixture: ComponentFixture<RestaurateursComponent>;
  let service: RestaurateursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurateursComponent],
    })
      .overrideTemplate(RestaurateursComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurateursComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurateursService);

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
    expect(comp.restaurateurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
