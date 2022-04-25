import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommandes } from '../commandes.model';
import { CommandesService } from '../service/commandes.service';

@Component({
  templateUrl: './commandes-delete-dialog.component.html',
})
export class CommandesDeleteDialogComponent {
  commandes?: ICommandes;

  constructor(protected commandesService: CommandesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.commandesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
