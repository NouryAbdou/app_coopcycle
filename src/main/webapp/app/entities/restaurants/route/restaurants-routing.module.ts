import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestaurantsComponent } from '../list/restaurants.component';
import { RestaurantsDetailComponent } from '../detail/restaurants-detail.component';
import { RestaurantsUpdateComponent } from '../update/restaurants-update.component';
import { RestaurantsRoutingResolveService } from './restaurants-routing-resolve.service';

const restaurantsRoute: Routes = [
  {
    path: '',
    component: RestaurantsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestaurantsDetailComponent,
    resolve: {
      restaurants: RestaurantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestaurantsUpdateComponent,
    resolve: {
      restaurants: RestaurantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestaurantsUpdateComponent,
    resolve: {
      restaurants: RestaurantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restaurantsRoute)],
  exports: [RouterModule],
})
export class RestaurantsRoutingModule {}
