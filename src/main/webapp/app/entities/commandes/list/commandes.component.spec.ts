import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CommandesService } from '../service/commandes.service';

import { CommandesComponent } from './commandes.component';

describe('Commandes Management Component', () => {
  let comp: CommandesComponent;
  let fixture: ComponentFixture<CommandesComponent>;
  let service: CommandesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CommandesComponent],
    })
      .overrideTemplate(CommandesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CommandesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
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
    expect(comp.commandes?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
