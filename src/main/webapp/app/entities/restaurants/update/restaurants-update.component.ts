import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurants, Restaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';

@Component({
  selector: 'jhi-restaurants-update',
  templateUrl: './restaurants-update.component.html',
})
export class RestaurantsUpdateComponent implements OnInit {
  isSaving = false;

  restaurateursSharedCollection: IRestaurateurs[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required, Validators.minLength(5)]],
    carte: [null, [Validators.required]],
    menu: [],
    restaurateur: [],
  });

  constructor(
    protected restaurantsService: RestaurantsService,
    protected restaurateursService: RestaurateursService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurants }) => {
      this.updateForm(restaurants);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurants = this.createFromForm();
    if (restaurants.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantsService.update(restaurants));
    } else {
      this.subscribeToSaveResponse(this.restaurantsService.create(restaurants));
    }
  }

  trackRestaurateursById(_index: number, item: IRestaurateurs): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurants>>): void {
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

  protected updateForm(restaurants: IRestaurants): void {
    this.editForm.patchValue({
      id: restaurants.id,
      nom: restaurants.nom,
      carte: restaurants.carte,
      menu: restaurants.menu,
      restaurateur: restaurants.restaurateur,
    });

    this.restaurateursSharedCollection = this.restaurateursService.addRestaurateursToCollectionIfMissing(
      this.restaurateursSharedCollection,
      restaurants.restaurateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurateursService
      .query()
      .pipe(map((res: HttpResponse<IRestaurateurs[]>) => res.body ?? []))
      .pipe(
        map((restaurateurs: IRestaurateurs[]) =>
          this.restaurateursService.addRestaurateursToCollectionIfMissing(restaurateurs, this.editForm.get('restaurateur')!.value)
        )
      )
      .subscribe((restaurateurs: IRestaurateurs[]) => (this.restaurateursSharedCollection = restaurateurs));
  }

  protected createFromForm(): IRestaurants {
    return {
      ...new Restaurants(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      carte: this.editForm.get(['carte'])!.value,
      menu: this.editForm.get(['menu'])!.value,
      restaurateur: this.editForm.get(['restaurateur'])!.value,
    };
  }
}
