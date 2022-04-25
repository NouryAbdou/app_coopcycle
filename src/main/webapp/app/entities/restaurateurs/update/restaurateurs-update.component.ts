import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurateurs, Restaurateurs } from '../restaurateurs.model';
import { RestaurateursService } from '../service/restaurateurs.service';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';
import { CooperativesService } from 'app/entities/cooperatives/service/cooperatives.service';

@Component({
  selector: 'jhi-restaurateurs-update',
  templateUrl: './restaurateurs-update.component.html',
})
export class RestaurateursUpdateComponent implements OnInit {
  isSaving = false;

  cooperativesSharedCollection: ICooperatives[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required, Validators.minLength(5)]],
    prenom: [null, [Validators.required, Validators.minLength(5)]],
    city: [],
    cooperative: [],
  });

  constructor(
    protected restaurateursService: RestaurateursService,
    protected cooperativesService: CooperativesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurateurs }) => {
      this.updateForm(restaurateurs);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurateurs = this.createFromForm();
    if (restaurateurs.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurateursService.update(restaurateurs));
    } else {
      this.subscribeToSaveResponse(this.restaurateursService.create(restaurateurs));
    }
  }

  trackCooperativesById(_index: number, item: ICooperatives): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurateurs>>): void {
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

  protected updateForm(restaurateurs: IRestaurateurs): void {
    this.editForm.patchValue({
      id: restaurateurs.id,
      nom: restaurateurs.nom,
      prenom: restaurateurs.prenom,
      city: restaurateurs.city,
      cooperative: restaurateurs.cooperative,
    });

    this.cooperativesSharedCollection = this.cooperativesService.addCooperativesToCollectionIfMissing(
      this.cooperativesSharedCollection,
      restaurateurs.cooperative
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IRestaurateurs {
    return {
      ...new Restaurateurs(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      city: this.editForm.get(['city'])!.value,
      cooperative: this.editForm.get(['cooperative'])!.value,
    };
  }
}
