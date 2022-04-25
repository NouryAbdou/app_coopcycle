import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IZones, Zones } from '../zones.model';
import { ZonesService } from '../service/zones.service';

@Component({
  selector: 'jhi-zones-update',
  templateUrl: './zones-update.component.html',
})
export class ZonesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ville: [null, [Validators.required]],
    metropole: [],
    communaute: [],
  });

  constructor(protected zonesService: ZonesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zones }) => {
      this.updateForm(zones);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const zones = this.createFromForm();
    if (zones.id !== undefined) {
      this.subscribeToSaveResponse(this.zonesService.update(zones));
    } else {
      this.subscribeToSaveResponse(this.zonesService.create(zones));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZones>>): void {
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

  protected updateForm(zones: IZones): void {
    this.editForm.patchValue({
      id: zones.id,
      ville: zones.ville,
      metropole: zones.metropole,
      communaute: zones.communaute,
    });
  }

  protected createFromForm(): IZones {
    return {
      ...new Zones(),
      id: this.editForm.get(['id'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      metropole: this.editForm.get(['metropole'])!.value,
      communaute: this.editForm.get(['communaute'])!.value,
    };
  }
}
