import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClients } from '../clients.model';

@Component({
  selector: 'jhi-clients-detail',
  templateUrl: './clients-detail.component.html',
})
export class ClientsDetailComponent implements OnInit {
  clients: IClients | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clients }) => {
      this.clients = clients;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
