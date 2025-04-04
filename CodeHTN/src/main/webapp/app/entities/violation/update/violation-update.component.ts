import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { ViolationType } from 'app/entities/enumerations/violation-type.model';
import { ViolationService } from '../service/violation.service';
import { IViolation } from '../violation.model';
import { ViolationFormService, ViolationFormGroup } from './violation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-violation-update',
  templateUrl: './violation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ViolationUpdateComponent implements OnInit {
  isSaving = false;
  violation: IViolation | null = null;
  violationTypeValues = Object.keys(ViolationType);

  tripsSharedCollection: ITrip[] = [];

  editForm: ViolationFormGroup = this.violationFormService.createViolationFormGroup();

  constructor(
    protected violationService: ViolationService,
    protected violationFormService: ViolationFormService,
    protected tripService: TripService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTrip = (o1: ITrip | null, o2: ITrip | null): boolean => this.tripService.compareTrip(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ violation }) => {
      this.violation = violation;
      if (violation) {
        this.updateForm(violation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const violation = this.violationFormService.getViolation(this.editForm);
    if (violation.id !== null) {
      this.subscribeToSaveResponse(this.violationService.update(violation));
    } else {
      this.subscribeToSaveResponse(this.violationService.create(violation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IViolation>>): void {
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

  protected updateForm(violation: IViolation): void {
    this.violation = violation;
    this.violationFormService.resetForm(this.editForm, violation);

    this.tripsSharedCollection = this.tripService.addTripToCollectionIfMissing<ITrip>(this.tripsSharedCollection, violation.trip);
  }

  protected loadRelationshipsOptions(): void {
    this.tripService
      .query()
      .pipe(map((res: HttpResponse<ITrip[]>) => res.body ?? []))
      .pipe(map((trips: ITrip[]) => this.tripService.addTripToCollectionIfMissing<ITrip>(trips, this.violation?.trip)))
      .subscribe((trips: ITrip[]) => (this.tripsSharedCollection = trips));
  }
}
