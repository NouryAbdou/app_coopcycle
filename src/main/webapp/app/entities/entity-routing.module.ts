import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'zones',
        data: { pageTitle: 'appCoopcycleApp.zones.home.title' },
        loadChildren: () => import('./zones/zones.module').then(m => m.ZonesModule),
      },
      {
        path: 'cooperatives',
        data: { pageTitle: 'appCoopcycleApp.cooperatives.home.title' },
        loadChildren: () => import('./cooperatives/cooperatives.module').then(m => m.CooperativesModule),
      },
      {
        path: 'livreurs',
        data: { pageTitle: 'appCoopcycleApp.livreurs.home.title' },
        loadChildren: () => import('./livreurs/livreurs.module').then(m => m.LivreursModule),
      },
      {
        path: 'restaurateurs',
        data: { pageTitle: 'appCoopcycleApp.restaurateurs.home.title' },
        loadChildren: () => import('./restaurateurs/restaurateurs.module').then(m => m.RestaurateursModule),
      },
      {
        path: 'restaurants',
        data: { pageTitle: 'appCoopcycleApp.restaurants.home.title' },
        loadChildren: () => import('./restaurants/restaurants.module').then(m => m.RestaurantsModule),
      },
      {
        path: 'clients',
        data: { pageTitle: 'appCoopcycleApp.clients.home.title' },
        loadChildren: () => import('./clients/clients.module').then(m => m.ClientsModule),
      },
      {
        path: 'commandes',
        data: { pageTitle: 'appCoopcycleApp.commandes.home.title' },
        loadChildren: () => import('./commandes/commandes.module').then(m => m.CommandesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
