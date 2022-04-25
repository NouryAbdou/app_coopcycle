import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestaurantsDetailComponent } from './restaurants-detail.component';

describe('Restaurants Management Detail Component', () => {
  let comp: RestaurantsDetailComponent;
  let fixture: ComponentFixture<RestaurantsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestaurantsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restaurants: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestaurantsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestaurantsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restaurants on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restaurants).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
