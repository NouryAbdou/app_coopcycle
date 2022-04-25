import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';

@Component({
  templateUrl: './restaurants-delete-dialog.component.html',
})
export class RestaurantsDeleteDialogComponent {
  restaurants?: IRestaurants;

  constructor(protected restaurantsService: RestaurantsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaurantsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
