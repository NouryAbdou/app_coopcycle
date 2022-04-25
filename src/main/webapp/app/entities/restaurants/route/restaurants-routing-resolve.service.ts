import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestaurants, Restaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';

@Injectable({ providedIn: 'root' })
export class RestaurantsRoutingResolveService implements Resolve<IRestaurants> {
  constructor(protected service: RestaurantsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaurants> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restaurants: HttpResponse<Restaurants>) => {
          if (restaurants.body) {
            return of(restaurants.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Restaurants());
  }
}
