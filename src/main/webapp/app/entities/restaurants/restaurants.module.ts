import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestaurantsComponent } from './list/restaurants.component';
import { RestaurantsDetailComponent } from './detail/restaurants-detail.component';
import { RestaurantsUpdateComponent } from './update/restaurants-update.component';
import { RestaurantsDeleteDialogComponent } from './delete/restaurants-delete-dialog.component';
import { RestaurantsRoutingModule } from './route/restaurants-routing.module';

@NgModule({
  imports: [SharedModule, RestaurantsRoutingModule],
  declarations: [RestaurantsComponent, RestaurantsDetailComponent, RestaurantsUpdateComponent, RestaurantsDeleteDialogComponent],
  entryComponents: [RestaurantsDeleteDialogComponent],
})
export class RestaurantsModule {}
