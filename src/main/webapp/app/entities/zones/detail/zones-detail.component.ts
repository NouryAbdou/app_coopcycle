import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZones } from '../zones.model';

@Component({
  selector: 'jhi-zones-detail',
  templateUrl: './zones-detail.component.html',
})
export class ZonesDetailComponent implements OnInit {
  zones: IZones | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zones }) => {
      this.zones = zones;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
