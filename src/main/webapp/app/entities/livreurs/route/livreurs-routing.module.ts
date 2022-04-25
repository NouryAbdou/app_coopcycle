import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LivreursComponent } from '../list/livreurs.component';
import { LivreursDetailComponent } from '../detail/livreurs-detail.component';
import { LivreursUpdateComponent } from '../update/livreurs-update.component';
import { LivreursRoutingResolveService } from './livreurs-routing-resolve.service';

const livreursRoute: Routes = [
  {
    path: '',
    component: LivreursComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LivreursDetailComponent,
    resolve: {
      livreurs: LivreursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LivreursUpdateComponent,
    resolve: {
      livreurs: LivreursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LivreursUpdateComponent,
    resolve: {
      livreurs: LivreursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(livreursRoute)],
  exports: [RouterModule],
})
export class LivreursRoutingModule {}
