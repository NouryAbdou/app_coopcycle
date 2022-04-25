import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommandesDetailComponent } from './commandes-detail.component';

describe('Commandes Management Detail Component', () => {
  let comp: CommandesDetailComponent;
  let fixture: ComponentFixture<CommandesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommandesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commandes: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CommandesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommandesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commandes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commandes).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
