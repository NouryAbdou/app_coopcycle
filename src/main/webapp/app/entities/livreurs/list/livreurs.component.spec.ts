import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LivreursService } from '../service/livreurs.service';

import { LivreursComponent } from './livreurs.component';

describe('Livreurs Management Component', () => {
  let comp: LivreursComponent;
  let fixture: ComponentFixture<LivreursComponent>;
  let service: LivreursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LivreursComponent],
    })
      .overrideTemplate(LivreursComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivreursComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LivreursService);

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
    expect(comp.livreurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
