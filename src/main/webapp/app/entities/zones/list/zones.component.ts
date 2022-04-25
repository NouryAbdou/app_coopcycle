import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IZones } from '../zones.model';
import { ZonesService } from '../service/zones.service';
import { ZonesDeleteDialogComponent } from '../delete/zones-delete-dialog.component';

@Component({
  selector: 'jhi-zones',
  templateUrl: './zones.component.html',
})
export class ZonesComponent implements OnInit {
  zones?: IZones[];
  isLoading = false;

  constructor(protected zonesService: ZonesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.zonesService.query().subscribe({
      next: (res: HttpResponse<IZones[]>) => {
        this.isLoading = false;
        this.zones = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IZones): number {
    return item.id!;
  }

  delete(zones: IZones): void {
    const modalRef = this.modalService.open(ZonesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.zones = zones;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
