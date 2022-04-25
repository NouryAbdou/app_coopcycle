import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommandes } from '../commandes.model';
import { CommandesService } from '../service/commandes.service';
import { CommandesDeleteDialogComponent } from '../delete/commandes-delete-dialog.component';

@Component({
  selector: 'jhi-commandes',
  templateUrl: './commandes.component.html',
})
export class CommandesComponent implements OnInit {
  commandes?: ICommandes[];
  isLoading = false;

  constructor(protected commandesService: CommandesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.commandesService.query().subscribe({
      next: (res: HttpResponse<ICommandes[]>) => {
        this.isLoading = false;
        this.commandes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICommandes): string {
    return item.id!;
  }

  delete(commandes: ICommandes): void {
    const modalRef = this.modalService.open(CommandesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commandes = commandes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
