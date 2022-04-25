import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommandesComponent } from './list/commandes.component';
import { CommandesDetailComponent } from './detail/commandes-detail.component';
import { CommandesUpdateComponent } from './update/commandes-update.component';
import { CommandesDeleteDialogComponent } from './delete/commandes-delete-dialog.component';
import { CommandesRoutingModule } from './route/commandes-routing.module';

@NgModule({
  imports: [SharedModule, CommandesRoutingModule],
  declarations: [CommandesComponent, CommandesDetailComponent, CommandesUpdateComponent, CommandesDeleteDialogComponent],
  entryComponents: [CommandesDeleteDialogComponent],
})
export class CommandesModule {}
