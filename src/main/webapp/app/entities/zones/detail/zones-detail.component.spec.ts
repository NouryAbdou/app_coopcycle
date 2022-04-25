import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ZonesDetailComponent } from './zones-detail.component';

describe('Zones Management Detail Component', () => {
  let comp: ZonesDetailComponent;
  let fixture: ComponentFixture<ZonesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ZonesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ zones: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ZonesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ZonesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load zones on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.zones).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
