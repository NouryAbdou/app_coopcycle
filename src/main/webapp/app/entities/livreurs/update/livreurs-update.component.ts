import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILivreurs, Livreurs } from '../livreurs.model';
import { LivreursService } from '../service/livreurs.service';
import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { RestaurateursService } from 'app/entities/restaurateurs/service/restaurateurs.service';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';
import { CooperativesService } from 'app/entities/cooperatives/service/cooperatives.service';

@Component({
  selector: 'jhi-livreurs-update',
  templateUrl: './livreurs-update.component.html',
})
export class LivreursUpdateComponent implements OnInit {
  isSaving = false;

  restaurateursCollection: IRestaurateurs[] = [];
  cooperativesSharedCollection: ICooperatives[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required, Validators.minLength(5)]],
    prenom: [null, [Validators.required, Validators.minLength(10)]],
    city: [null, [Validators.required, Validators.minLength(3)]],
    restaurateur: [],
    cooperative: [],
  });

  constructor(
    protected livreursService: LivreursService,
    protected restaurateursService: RestaurateursService,
    protected cooperativesService: CooperativesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livreurs }) => {
      this.updateForm(livreurs);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livreurs = this.createFromForm();
    if (livreurs.id !== undefined) {
      this.subscribeToSaveResponse(this.livreursService.update(livreurs));
    } else {
      this.subscribeToSaveResponse(this.livreursService.create(livreurs));
    }
  }

  trackRestaurateursById(_index: number, item: IRestaurateurs): number {
    return item.id!;
  }

  trackCooperativesById(_index: number, item: ICooperatives): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivreurs>>): void {
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

  protected updateForm(livreurs: ILivreurs): void {
    this.editForm.patchValue({
      id: livreurs.id,
      nom: livreurs.nom,
      prenom: livreurs.prenom,
      city: livreurs.city,
      restaurateur: livreurs.restaurateur,
      cooperative: livreurs.cooperative,
    });

    this.restaurateursCollection = this.restaurateursService.addRestaurateursToCollectionIfMissing(
      this.restaurateursCollection,
      livreurs.restaurateur
    );
    this.cooperativesSharedCollection = this.cooperativesService.addCooperativesToCollectionIfMissing(
      this.cooperativesSharedCollection,
      livreurs.cooperative
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurateursService
      .query({ filter: 'livreur-is-null' })
      .pipe(map((res: HttpResponse<IRestaurateurs[]>) => res.body ?? []))
      .pipe(
        map((restaurateurs: IRestaurateurs[]) =>
          this.restaurateursService.addRestaurateursToCollectionIfMissing(restaurateurs, this.editForm.get('restaurateur')!.value)
        )
      )
      .subscribe((restaurateurs: IRestaurateurs[]) => (this.restaurateursCollection = restaurateurs));

    this.cooperativesService
      .query()
      .pipe(map((res: HttpResponse<ICooperatives[]>) => res.body ?? []))
      .pipe(
        map((cooperatives: ICooperatives[]) =>
          this.cooperativesService.addCooperativesToCollectionIfMissing(cooperatives, this.editForm.get('cooperative')!.value)
        )
      )
      .subscribe((cooperatives: ICooperatives[]) => (this.cooperativesSharedCollection = cooperatives));
  }

  protected createFromForm(): ILivreurs {
    return {
      ...new Livreurs(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      city: this.editForm.get(['city'])!.value,
      restaurateur: this.editForm.get(['restaurateur'])!.value,
      cooperative: this.editForm.get(['cooperative'])!.value,
    };
  }
}
