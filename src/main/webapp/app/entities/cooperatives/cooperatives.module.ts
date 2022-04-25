import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CooperativesComponent } from './list/cooperatives.component';
import { CooperativesDetailComponent } from './detail/cooperatives-detail.component';
import { CooperativesUpdateComponent } from './update/cooperatives-update.component';
import { CooperativesDeleteDialogComponent } from './delete/cooperatives-delete-dialog.component';
import { CooperativesRoutingModule } from './route/cooperatives-routing.module';

@NgModule({
  imports: [SharedModule, CooperativesRoutingModule],
  declarations: [CooperativesComponent, CooperativesDetailComponent, CooperativesUpdateComponent, CooperativesDeleteDialogComponent],
  entryComponents: [CooperativesDeleteDialogComponent],
})
export class CooperativesModule {}
