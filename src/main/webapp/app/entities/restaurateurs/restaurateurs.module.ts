import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestaurateursComponent } from './list/restaurateurs.component';
import { RestaurateursDetailComponent } from './detail/restaurateurs-detail.component';
import { RestaurateursUpdateComponent } from './update/restaurateurs-update.component';
import { RestaurateursDeleteDialogComponent } from './delete/restaurateurs-delete-dialog.component';
import { RestaurateursRoutingModule } from './route/restaurateurs-routing.module';

@NgModule({
  imports: [SharedModule, RestaurateursRoutingModule],
  declarations: [RestaurateursComponent, RestaurateursDetailComponent, RestaurateursUpdateComponent, RestaurateursDeleteDialogComponent],
  entryComponents: [RestaurateursDeleteDialogComponent],
})
export class RestaurateursModule {}
