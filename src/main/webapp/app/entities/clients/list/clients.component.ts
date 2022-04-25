import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClients } from '../clients.model';
import { ClientsService } from '../service/clients.service';
import { ClientsDeleteDialogComponent } from '../delete/clients-delete-dialog.component';

@Component({
  selector: 'jhi-clients',
  templateUrl: './clients.component.html',
})
export class ClientsComponent implements OnInit {
  clients?: IClients[];
  isLoading = false;

  constructor(protected clientsService: ClientsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.clientsService.query().subscribe({
      next: (res: HttpResponse<IClients[]>) => {
        this.isLoading = false;
        this.clients = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IClients): number {
    return item.id!;
  }

  delete(clients: IClients): void {
    const modalRef = this.modalService.open(ClientsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clients = clients;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
