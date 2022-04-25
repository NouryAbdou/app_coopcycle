import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClients, Clients } from '../clients.model';
import { ClientsService } from '../service/clients.service';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';

@Component({
  selector: 'jhi-clients-update',
  templateUrl: './clients-update.component.html',
})
export class ClientsUpdateComponent implements OnInit {
  isSaving = false;

  restaurateursCollection: IRestaurateurs[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    email: [],
    phoneNumber: [null, [Validators.required]],
    restaurateur: [],
  });

  constructor(
    protected clientsService: ClientsService,
    protected restaurateursService: RestaurateursService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clients }) => {
      this.updateForm(clients);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clients = this.createFromForm();
    if (clients.id !== undefined) {
      this.subscribeToSaveResponse(this.clientsService.update(clients));
    } else {
      this.subscribeToSaveResponse(this.clientsService.create(clients));
    }
  }

  trackRestaurateursById(_index: number, item: IRestaurateurs): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClients>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clients: IClients): void {
    this.editForm.patchValue({
      id: clients.id,
      nom: clients.nom,
      prenom: clients.prenom,
      email: clients.email,
      phoneNumber: clients.phoneNumber,
      restaurateur: clients.restaurateur,
    });

    this.restaurateursCollection = this.restaurateursService.addRestaurateursToCollectionIfMissing(
      this.restaurateursCollection,
      clients.restaurateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurateursService
      .query({ filter: 'client-is-null' })
      .pipe(map((res: HttpResponse<IRestaurateurs[]>) => res.body ?? []))
      .pipe(
        map((restaurateurs: IRestaurateurs[]) =>
          this.restaurateursService.addRestaurateursToCollectionIfMissing(restaurateurs, this.editForm.get('restaurateur')!.value)
        )
      )
      .subscribe((restaurateurs: IRestaurateurs[]) => (this.restaurateursCollection = restaurateurs));
  }

  protected createFromForm(): IClients {
    return {
      ...new Clients(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      restaurateur: this.editForm.get(['restaurateur'])!.value,
    };
  }
}
