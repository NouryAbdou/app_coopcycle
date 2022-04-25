import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommandesComponent } from '../list/commandes.component';
import { CommandesDetailComponent } from '../detail/commandes-detail.component';
import { CommandesUpdateComponent } from '../update/commandes-update.component';
import { CommandesRoutingResolveService } from './commandes-routing-resolve.service';

const commandesRoute: Routes = [
  {
    path: '',
    component: CommandesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommandesDetailComponent,
    resolve: {
      commandes: CommandesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommandesUpdateComponent,
    resolve: {
      commandes: CommandesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommandesUpdateComponent,
    resolve: {
      commandes: CommandesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commandesRoute)],
  exports: [RouterModule],
})
export class CommandesRoutingModule {}
