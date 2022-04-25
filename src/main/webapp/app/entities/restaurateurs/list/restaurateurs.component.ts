import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurateurs } from '../restaurateurs.model';
import { RestaurateursService } from '../service/restaurateurs.service';
import { RestaurateursDeleteDialogComponent } from '../delete/restaurateurs-delete-dialog.component';

@Component({
  selector: 'jhi-restaurateurs',
  templateUrl: './restaurateurs.component.html',
})
export class RestaurateursComponent implements OnInit {
  restaurateurs?: IRestaurateurs[];
  isLoading = false;

  constructor(protected restaurateursService: RestaurateursService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.restaurateursService.query().subscribe({
      next: (res: HttpResponse<IRestaurateurs[]>) => {
        this.isLoading = false;
        this.restaurateurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRestaurateurs): number {
    return item.id!;
  }

  delete(restaurateurs: IRestaurateurs): void {
    const modalRef = this.modalService.open(RestaurateursDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.restaurateurs = restaurateurs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
