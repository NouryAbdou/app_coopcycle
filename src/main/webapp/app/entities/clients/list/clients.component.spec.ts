import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClientsService } from '../service/clients.service';

import { ClientsComponent } from './clients.component';

describe('Clients Management Component', () => {
  let comp: ClientsComponent;
  let fixture: ComponentFixture<ClientsComponent>;
  let service: ClientsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClientsComponent],
    })
      .overrideTemplate(ClientsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClientsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClientsService);

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
    expect(comp.clients?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
