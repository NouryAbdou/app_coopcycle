import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommandes, Commandes } from '../commandes.model';
import { CommandesService } from '../service/commandes.service';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';
import { IRestaurants } from 'app/entities/restaurants/restaurants.model';
import { RestaurantsService } from 'app/entities/restaurants/service/restaurants.service';

@Component({
  selector: 'jhi-commandes-update',
  templateUrl: './commandes-update.component.html',
})
export class CommandesUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClients[] = [];
  restaurantsSharedCollection: IRestaurants[] = [];

  editForm = this.fb.group({
    id: [],
    estPret: [],
    estPaye: [],
    client: [],
    restaurants: [],
  });

  constructor(
    protected commandesService: CommandesService,
    protected clientsService: ClientsService,
    protected restaurantsService: RestaurantsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandes }) => {
      this.updateForm(commandes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandes = this.createFromForm();
    if (commandes.id !== undefined) {
      this.subscribeToSaveResponse(this.commandesService.update(commandes));
    } else {
      this.subscribeToSaveResponse(this.commandesService.create(commandes));
    }
  }

  trackClientsById(_index: number, item: IClients): number {
    return item.id!;
  }

  trackRestaurantsById(_index: number, item: IRestaurants): number {
    return item.id!;
  }

  getSelectedRestaurants(option: IRestaurants, selectedVals?: IRestaurants[]): IRestaurants {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandes>>): void {
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

  protected updateForm(commandes: ICommandes): void {
    this.editForm.patchValue({
      id: commandes.id,
      estPret: commandes.estPret,
      estPaye: commandes.estPaye,
      client: commandes.client,
      restaurants: commandes.restaurants,
    });

    this.clientsSharedCollection = this.clientsService.addClientsToCollectionIfMissing(this.clientsSharedCollection, commandes.client);
    this.restaurantsSharedCollection = this.restaurantsService.addRestaurantsToCollectionIfMissing(
      this.restaurantsSharedCollection,
      ...(commandes.restaurants ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientsService
      .query()
      .pipe(map((res: HttpResponse<IClients[]>) => res.body ?? []))
      .pipe(map((clients: IClients[]) => this.clientsService.addClientsToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClients[]) => (this.clientsSharedCollection = clients));

    this.restaurantsService
      .query()
      .pipe(map((res: HttpResponse<IRestaurants[]>) => res.body ?? []))
      .pipe(
        map((restaurants: IRestaurants[]) =>
          this.restaurantsService.addRestaurantsToCollectionIfMissing(restaurants, ...(this.editForm.get('restaurants')!.value ?? []))
        )
      )
      .subscribe((restaurants: IRestaurants[]) => (this.restaurantsSharedCollection = restaurants));
  }

  protected createFromForm(): ICommandes {
    return {
      ...new Commandes(),
      id: this.editForm.get(['id'])!.value,
      estPret: this.editForm.get(['estPret'])!.value,
      estPaye: this.editForm.get(['estPaye'])!.value,
      client: this.editForm.get(['client'])!.value,
      restaurants: this.editForm.get(['restaurants'])!.value,
    };
  }
}
