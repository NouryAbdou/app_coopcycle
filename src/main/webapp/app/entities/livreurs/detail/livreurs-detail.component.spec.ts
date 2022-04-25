import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LivreursDetailComponent } from './livreurs-detail.component';

describe('Livreurs Management Detail Component', () => {
  let comp: LivreursDetailComponent;
  let fixture: ComponentFixture<LivreursDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LivreursDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ livreurs: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LivreursDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LivreursDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load livreurs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.livreurs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
