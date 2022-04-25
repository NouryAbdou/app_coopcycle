import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaurateurs } from '../restaurateurs.model';

@Component({
  selector: 'jhi-restaurateurs-detail',
  templateUrl: './restaurateurs-detail.component.html',
})
export class RestaurateursDetailComponent implements OnInit {
  restaurateurs: IRestaurateurs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurateurs }) => {
      this.restaurateurs = restaurateurs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
