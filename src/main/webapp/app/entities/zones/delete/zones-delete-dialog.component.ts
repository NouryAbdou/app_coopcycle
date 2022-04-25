import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IZones } from '../zones.model';
import { ZonesService } from '../service/zones.service';

@Component({
  templateUrl: './zones-delete-dialog.component.html',
})
export class ZonesDeleteDialogComponent {
  zones?: IZones;

  constructor(protected zonesService: ZonesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zonesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
