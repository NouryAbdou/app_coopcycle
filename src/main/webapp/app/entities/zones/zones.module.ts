import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ZonesComponent } from './list/zones.component';
import { ZonesDetailComponent } from './detail/zones-detail.component';
import { ZonesUpdateComponent } from './update/zones-update.component';
import { ZonesDeleteDialogComponent } from './delete/zones-delete-dialog.component';
import { ZonesRoutingModule } from './route/zones-routing.module';

@NgModule({
  imports: [SharedModule, ZonesRoutingModule],
  declarations: [ZonesComponent, ZonesDetailComponent, ZonesUpdateComponent, ZonesDeleteDialogComponent],
  entryComponents: [ZonesDeleteDialogComponent],
})
export class ZonesModule {}
