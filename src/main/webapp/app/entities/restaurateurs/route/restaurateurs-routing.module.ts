import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestaurateursComponent } from '../list/restaurateurs.component';
import { RestaurateursDetailComponent } from '../detail/restaurateurs-detail.component';
import { RestaurateursUpdateComponent } from '../update/restaurateurs-update.component';
import { RestaurateursRoutingResolveService } from './restaurateurs-routing-resolve.service';

const restaurateursRoute: Routes = [
  {
    path: '',
    component: RestaurateursComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestaurateursDetailComponent,
    resolve: {
      restaurateurs: RestaurateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestaurateursUpdateComponent,
    resolve: {
      restaurateurs: RestaurateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestaurateursUpdateComponent,
    resolve: {
      restaurateurs: RestaurateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restaurateursRoute)],
  exports: [RouterModule],
})
export class RestaurateursRoutingModule {}
