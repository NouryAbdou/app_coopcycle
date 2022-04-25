import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommandes } from '../commandes.model';

@Component({
  selector: 'jhi-commandes-detail',
  templateUrl: './commandes-detail.component.html',
})
export class CommandesDetailComponent implements OnInit {
  commandes: ICommandes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandes }) => {
      this.commandes = commandes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
