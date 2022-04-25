import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CooperativesDetailComponent } from './cooperatives-detail.component';

describe('Cooperatives Management Detail Component', () => {
  let comp: CooperativesDetailComponent;
  let fixture: ComponentFixture<CooperativesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CooperativesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cooperatives: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CooperativesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CooperativesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cooperatives on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cooperatives).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
