import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { TripStatus } from 'app/entities/enumerations/trip-status.model';
import { TripService } from '../service/trip.service';
import { ITrip } from '../trip.model';
import { TripFormService, TripFormGroup } from './trip-form.service';

@Component({
  standalone: true,
  selector: 'jhi-trip-update',
  templateUrl: './trip-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TripUpdateComponent implements OnInit {
  isSaving = false;
  trip: ITrip | null = null;
  tripStatusValues = Object.keys(TripStatus);

  driversSharedCollection: IDriver[] = [];
  vehiclesSharedCollection: IVehicle[] = [];

  editForm: TripFormGroup = this.tripFormService.createTripFormGroup();

  constructor(
    protected tripService: TripService,
    protected tripFormService: TripFormService,
    protected driverService: DriverService,
    protected vehicleService: VehicleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDriver = (o1: IDriver | null, o2: IDriver | null): boolean => this.driverService.compareDriver(o1, o2);

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trip }) => {
      this.trip = trip;
      if (trip) {
        this.updateForm(trip);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trip = this.tripFormService.getTrip(this.editForm);
    if (trip.id !== null) {
      this.subscribeToSaveResponse(this.tripService.update(trip));
    } else {
      this.subscribeToSaveResponse(this.tripService.create(trip));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrip>>): void {
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

  protected updateForm(trip: ITrip): void {
    this.trip = trip;
    this.tripFormService.resetForm(this.editForm, trip);

    this.driversSharedCollection = this.driverService.addDriverToCollectionIfMissing<IDriver>(this.driversSharedCollection, trip.driver);
    this.vehiclesSharedCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(
      this.vehiclesSharedCollection,
      trip.vehicle,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.driverService
      .query()
      .pipe(map((res: HttpResponse<IDriver[]>) => res.body ?? []))
      .pipe(map((drivers: IDriver[]) => this.driverService.addDriverToCollectionIfMissing<IDriver>(drivers, this.trip?.driver)))
      .subscribe((drivers: IDriver[]) => (this.driversSharedCollection = drivers));

    this.vehicleService
      .query()
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.trip?.vehicle)))
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesSharedCollection = vehicles));
  }
}
