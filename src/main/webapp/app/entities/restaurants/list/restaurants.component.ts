import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';
import { RestaurantsDeleteDialogComponent } from '../delete/restaurants-delete-dialog.component';

@Component({
  selector: 'jhi-restaurants',
  templateUrl: './restaurants.component.html',
})
export class RestaurantsComponent implements OnInit {
  restaurants?: IRestaurants[];
  isLoading = false;

  constructor(protected restaurantsService: RestaurantsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.restaurantsService.query().subscribe({
      next: (res: HttpResponse<IRestaurants[]>) => {
        this.isLoading = false;
        this.restaurants = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRestaurants): number {
    return item.id!;
  }

  delete(restaurants: IRestaurants): void {
    const modalRef = this.modalService.open(RestaurantsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.restaurants = restaurants;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
