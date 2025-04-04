import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { ICheckin } from '../checkin.model';
import { CheckinService } from '../service/checkin.service';
import { CheckinFormService, CheckinFormGroup } from './checkin-form.service';

@Component({
  standalone: true,
  selector: 'jhi-checkin-update',
  templateUrl: './checkin-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CheckinUpdateComponent implements OnInit {
  isSaving = false;
  checkin: ICheckin | null = null;

  tripsSharedCollection: ITrip[] = [];

  editForm: CheckinFormGroup = this.checkinFormService.createCheckinFormGroup();

  constructor(
    protected checkinService: CheckinService,
    protected checkinFormService: CheckinFormService,
    protected tripService: TripService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTrip = (o1: ITrip | null, o2: ITrip | null): boolean => this.tripService.compareTrip(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ checkin }) => {
      this.checkin = checkin;
      if (checkin) {
        this.updateForm(checkin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const checkin = this.checkinFormService.getCheckin(this.editForm);
    if (checkin.id !== null) {
      this.subscribeToSaveResponse(this.checkinService.update(checkin));
    } else {
      this.subscribeToSaveResponse(this.checkinService.create(checkin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICheckin>>): void {
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

  protected updateForm(checkin: ICheckin): void {
    this.checkin = checkin;
    this.checkinFormService.resetForm(this.editForm, checkin);

    this.tripsSharedCollection = this.tripService.addTripToCollectionIfMissing<ITrip>(this.tripsSharedCollection, checkin.trip);
  }

  protected loadRelationshipsOptions(): void {
    this.tripService
      .query()
      .pipe(map((res: HttpResponse<ITrip[]>) => res.body ?? []))
      .pipe(map((trips: ITrip[]) => this.tripService.addTripToCollectionIfMissing<ITrip>(trips, this.checkin?.trip)))
      .subscribe((trips: ITrip[]) => (this.tripsSharedCollection = trips));
  }
}
