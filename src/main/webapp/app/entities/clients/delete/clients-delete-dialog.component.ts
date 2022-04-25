import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClients } from '../clients.model';
import { ClientsService } from '../service/clients.service';

@Component({
  templateUrl: './clients-delete-dialog.component.html',
})
export class ClientsDeleteDialogComponent {
  clients?: IClients;

  constructor(protected clientsService: ClientsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clientsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
