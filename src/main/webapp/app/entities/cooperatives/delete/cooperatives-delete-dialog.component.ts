import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICooperatives } from '../cooperatives.model';
import { CooperativesService } from '../service/cooperatives.service';

@Component({
  templateUrl: './cooperatives-delete-dialog.component.html',
})
export class CooperativesDeleteDialogComponent {
  cooperatives?: ICooperatives;

  constructor(protected cooperativesService: CooperativesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cooperativesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
