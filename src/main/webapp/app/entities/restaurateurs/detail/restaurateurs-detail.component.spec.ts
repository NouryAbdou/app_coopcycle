import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestaurateursDetailComponent } from './restaurateurs-detail.component';

describe('Restaurateurs Management Detail Component', () => {
  let comp: RestaurateursDetailComponent;
  let fixture: ComponentFixture<RestaurateursDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestaurateursDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restaurateurs: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestaurateursDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestaurateursDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restaurateurs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restaurateurs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
