import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ZonesComponent } from '../list/zones.component';
import { ZonesDetailComponent } from '../detail/zones-detail.component';
import { ZonesUpdateComponent } from '../update/zones-update.component';
import { ZonesRoutingResolveService } from './zones-routing-resolve.service';

const zonesRoute: Routes = [
  {
    path: '',
    component: ZonesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZonesDetailComponent,
    resolve: {
      zones: ZonesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZonesUpdateComponent,
    resolve: {
      zones: ZonesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZonesUpdateComponent,
    resolve: {
      zones: ZonesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(zonesRoute)],
  exports: [RouterModule],
})
export class ZonesRoutingModule {}
