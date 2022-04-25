import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LivreursComponent } from './list/livreurs.component';
import { LivreursDetailComponent } from './detail/livreurs-detail.component';
import { LivreursUpdateComponent } from './update/livreurs-update.component';
import { LivreursDeleteDialogComponent } from './delete/livreurs-delete-dialog.component';
import { LivreursRoutingModule } from './route/livreurs-routing.module';

@NgModule({
  imports: [SharedModule, LivreursRoutingModule],
  declarations: [LivreursComponent, LivreursDetailComponent, LivreursUpdateComponent, LivreursDeleteDialogComponent],
  entryComponents: [LivreursDeleteDialogComponent],
})
export class LivreursModule {}
