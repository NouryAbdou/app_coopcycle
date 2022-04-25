import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RestaurantsService } from '../service/restaurants.service';

import { RestaurantsComponent } from './restaurants.component';

describe('Restaurants Management Component', () => {
  let comp: RestaurantsComponent;
  let fixture: ComponentFixture<RestaurantsComponent>;
  let service: RestaurantsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurantsComponent],
    })
      .overrideTemplate(RestaurantsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurantsService);

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
    expect(comp.restaurants?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
