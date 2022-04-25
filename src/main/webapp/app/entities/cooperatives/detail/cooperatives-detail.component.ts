import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICooperatives } from '../cooperatives.model';

@Component({
  selector: 'jhi-cooperatives-detail',
  templateUrl: './cooperatives-detail.component.html',
})
export class CooperativesDetailComponent implements OnInit {
  cooperatives: ICooperatives | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cooperatives }) => {
      this.cooperatives = cooperatives;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
