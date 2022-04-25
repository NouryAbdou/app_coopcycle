import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivreurs } from '../livreurs.model';
import { LivreursService } from '../service/livreurs.service';

@Component({
  templateUrl: './livreurs-delete-dialog.component.html',
})
export class LivreursDeleteDialogComponent {
  livreurs?: ILivreurs;

  constructor(protected livreursService: LivreursService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.livreursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
