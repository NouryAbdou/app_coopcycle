import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClientsComponent } from '../list/clients.component';
import { ClientsDetailComponent } from '../detail/clients-detail.component';
import { ClientsUpdateComponent } from '../update/clients-update.component';
import { ClientsRoutingResolveService } from './clients-routing-resolve.service';

const clientsRoute: Routes = [
  {
    path: '',
    component: ClientsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClientsDetailComponent,
    resolve: {
      clients: ClientsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClientsUpdateComponent,
    resolve: {
      clients: ClientsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClientsUpdateComponent,
    resolve: {
      clients: ClientsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(clientsRoute)],
  exports: [RouterModule],
})
export class ClientsRoutingModule {}
