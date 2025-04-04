import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrip, NewTrip } from '../trip.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrip for edit and NewTripFormGroupInput for create.
 */
type TripFormGroupInput = ITrip | PartialWithRequiredKeyOf<NewTrip>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrip | NewTrip> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type TripFormRawValue = FormValueOf<ITrip>;

type NewTripFormRawValue = FormValueOf<NewTrip>;

type TripFormDefaults = Pick<NewTrip, 'id' | 'startTime' | 'endTime'>;

type TripFormGroupContent = {
  id: FormControl<TripFormRawValue['id'] | NewTrip['id']>;
  startTime: FormControl<TripFormRawValue['startTime']>;
  endTime: FormControl<TripFormRawValue['endTime']>;
  status: FormControl<TripFormRawValue['status']>;
  driver: FormControl<TripFormRawValue['driver']>;
  vehicle: FormControl<TripFormRawValue['vehicle']>;
};

export type TripFormGroup = FormGroup<TripFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TripFormService {
  createTripFormGroup(trip: TripFormGroupInput = { id: null }): TripFormGroup {
    const tripRawValue = this.convertTripToTripRawValue({
      ...this.getFormDefaults(),
      ...trip,
    });
    return new FormGroup<TripFormGroupContent>({
      id: new FormControl(
        { value: tripRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startTime: new FormControl(tripRawValue.startTime),
      endTime: new FormControl(tripRawValue.endTime),
      status: new FormControl(tripRawValue.status),
      driver: new FormControl(tripRawValue.driver),
      vehicle: new FormControl(tripRawValue.vehicle),
    });
  }

  getTrip(form: TripFormGroup): ITrip | NewTrip {
    return this.convertTripRawValueToTrip(form.getRawValue() as TripFormRawValue | NewTripFormRawValue);
  }

  resetForm(form: TripFormGroup, trip: TripFormGroupInput): void {
    const tripRawValue = this.convertTripToTripRawValue({ ...this.getFormDefaults(), ...trip });
    form.reset(
      {
        ...tripRawValue,
        id: { value: tripRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TripFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertTripRawValueToTrip(rawTrip: TripFormRawValue | NewTripFormRawValue): ITrip | NewTrip {
    return {
      ...rawTrip,
      startTime: dayjs(rawTrip.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawTrip.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertTripToTripRawValue(
    trip: ITrip | (Partial<NewTrip> & TripFormDefaults),
  ): TripFormRawValue | PartialWithRequiredKeyOf<NewTripFormRawValue> {
    return {
      ...trip,
      startTime: trip.startTime ? trip.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: trip.endTime ? trip.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
