import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILivreurs } from '../livreurs.model';

@Component({
  selector: 'jhi-livreurs-detail',
  templateUrl: './livreurs-detail.component.html',
})
export class LivreursDetailComponent implements OnInit {
  livreurs: ILivreurs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livreurs }) => {
      this.livreurs = livreurs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
