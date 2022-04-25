import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CooperativesComponent } from '../list/cooperatives.component';
import { CooperativesDetailComponent } from '../detail/cooperatives-detail.component';
import { CooperativesUpdateComponent } from '../update/cooperatives-update.component';
import { CooperativesRoutingResolveService } from './cooperatives-routing-resolve.service';

const cooperativesRoute: Routes = [
  {
    path: '',
    component: CooperativesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CooperativesDetailComponent,
    resolve: {
      cooperatives: CooperativesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CooperativesUpdateComponent,
    resolve: {
      cooperatives: CooperativesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CooperativesUpdateComponent,
    resolve: {
      cooperatives: CooperativesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cooperativesRoute)],
  exports: [RouterModule],
})
export class CooperativesRoutingModule {}
