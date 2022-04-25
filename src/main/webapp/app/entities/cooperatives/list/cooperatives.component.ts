import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICooperatives } from '../cooperatives.model';
import { CooperativesService } from '../service/cooperatives.service';
import { CooperativesDeleteDialogComponent } from '../delete/cooperatives-delete-dialog.component';

@Component({
  selector: 'jhi-cooperatives',
  templateUrl: './cooperatives.component.html',
})
export class CooperativesComponent implements OnInit {
  cooperatives?: ICooperatives[];
  isLoading = false;

  constructor(protected cooperativesService: CooperativesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cooperativesService.query().subscribe({
      next: (res: HttpResponse<ICooperatives[]>) => {
        this.isLoading = false;
        this.cooperatives = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICooperatives): number {
    return item.id!;
  }

  delete(cooperatives: ICooperatives): void {
    const modalRef = this.modalService.open(CooperativesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cooperatives = cooperatives;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
