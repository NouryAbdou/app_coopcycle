import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClientsDetailComponent } from './clients-detail.component';

describe('Clients Management Detail Component', () => {
  let comp: ClientsDetailComponent;
  let fixture: ComponentFixture<ClientsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClientsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ clients: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClientsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClientsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load clients on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.clients).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
