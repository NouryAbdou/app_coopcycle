import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICooperatives, Cooperatives } from '../cooperatives.model';
import { CooperativesService } from '../service/cooperatives.service';
import { IZones } from 'app/entities/zones/zones.model';
import { ZonesService } from 'app/entities/zones/service/zones.service';

@Component({
  selector: 'jhi-cooperatives-update',
  templateUrl: './cooperatives-update.component.html',
})
export class CooperativesUpdateComponent implements OnInit {
  isSaving = false;

  zonesSharedCollection: IZones[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required, Validators.minLength(5)]],
    zone: [],
  });

  constructor(
    protected cooperativesService: CooperativesService,
    protected zonesService: ZonesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cooperatives }) => {
      this.updateForm(cooperatives);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cooperatives = this.createFromForm();
    if (cooperatives.id !== undefined) {
      this.subscribeToSaveResponse(this.cooperativesService.update(cooperatives));
    } else {
      this.subscribeToSaveResponse(this.cooperativesService.create(cooperatives));
    }
  }

  trackZonesById(_index: number, item: IZones): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICooperatives>>): void {
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

  protected updateForm(cooperatives: ICooperatives): void {
    this.editForm.patchValue({
      id: cooperatives.id,
      nom: cooperatives.nom,
      zone: cooperatives.zone,
    });

    this.zonesSharedCollection = this.zonesService.addZonesToCollectionIfMissing(this.zonesSharedCollection, cooperatives.zone);
  }

  protected loadRelationshipsOptions(): void {
    this.zonesService
      .query()
      .pipe(map((res: HttpResponse<IZones[]>) => res.body ?? []))
      .pipe(map((zones: IZones[]) => this.zonesService.addZonesToCollectionIfMissing(zones, this.editForm.get('zone')!.value)))
      .subscribe((zones: IZones[]) => (this.zonesSharedCollection = zones));
  }

  protected createFromForm(): ICooperatives {
    return {
      ...new Cooperatives(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      zone: this.editForm.get(['zone'])!.value,
    };
  }
}
