import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClientsComponent } from './list/clients.component';
import { ClientsDetailComponent } from './detail/clients-detail.component';
import { ClientsUpdateComponent } from './update/clients-update.component';
import { ClientsDeleteDialogComponent } from './delete/clients-delete-dialog.component';
import { ClientsRoutingModule } from './route/clients-routing.module';

@NgModule({
  imports: [SharedModule, ClientsRoutingModule],
  declarations: [ClientsComponent, ClientsDetailComponent, ClientsUpdateComponent, ClientsDeleteDialogComponent],
  entryComponents: [ClientsDeleteDialogComponent],
})
export class ClientsModule {}
