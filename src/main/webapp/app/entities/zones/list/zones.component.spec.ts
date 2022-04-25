import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ZonesService } from '../service/zones.service';

import { ZonesComponent } from './zones.component';

describe('Zones Management Component', () => {
  let comp: ZonesComponent;
  let fixture: ComponentFixture<ZonesComponent>;
  let service: ZonesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ZonesComponent],
    })
      .overrideTemplate(ZonesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ZonesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ZonesService);

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
    expect(comp.zones?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
