import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivreurs } from '../livreurs.model';
import { LivreursService } from '../service/livreurs.service';
import { LivreursDeleteDialogComponent } from '../delete/livreurs-delete-dialog.component';

@Component({
  selector: 'jhi-livreurs',
  templateUrl: './livreurs.component.html',
})
export class LivreursComponent implements OnInit {
  livreurs?: ILivreurs[];
  isLoading = false;

  constructor(protected livreursService: LivreursService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.livreursService.query().subscribe({
      next: (res: HttpResponse<ILivreurs[]>) => {
        this.isLoading = false;
        this.livreurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILivreurs): number {
    return item.id!;
  }

  delete(livreurs: ILivreurs): void {
    const modalRef = this.modalService.open(LivreursDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.livreurs = livreurs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
