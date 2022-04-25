import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurateurs } from '../restaurateurs.model';
import { RestaurateursService } from '../service/restaurateurs.service';

@Component({
  templateUrl: './restaurateurs-delete-dialog.component.html',
})
export class RestaurateursDeleteDialogComponent {
  restaurateurs?: IRestaurateurs;

  constructor(protected restaurateursService: RestaurateursService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaurateursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
