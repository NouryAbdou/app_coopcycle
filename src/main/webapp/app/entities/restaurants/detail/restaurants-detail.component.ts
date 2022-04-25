import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaurants } from '../restaurants.model';

@Component({
  selector: 'jhi-restaurants-detail',
  templateUrl: './restaurants-detail.component.html',
})
export class RestaurantsDetailComponent implements OnInit {
  restaurants: IRestaurants | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurants }) => {
      this.restaurants = restaurants;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
