import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICheckin, NewCheckin } from '../checkin.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICheckin for edit and NewCheckinFormGroupInput for create.
 */
type CheckinFormGroupInput = ICheckin | PartialWithRequiredKeyOf<NewCheckin>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICheckin | NewCheckin> = Omit<T, 'checkinTime' | 'checkoutTime'> & {
  checkinTime?: string | null;
  checkoutTime?: string | null;
};

type CheckinFormRawValue = FormValueOf<ICheckin>;

type NewCheckinFormRawValue = FormValueOf<NewCheckin>;

type CheckinFormDefaults = Pick<NewCheckin, 'id' | 'checkinTime' | 'checkoutTime' | 'faceVerified'>;

type CheckinFormGroupContent = {
  id: FormControl<CheckinFormRawValue['id'] | NewCheckin['id']>;
  checkinTime: FormControl<CheckinFormRawValue['checkinTime']>;
  checkoutTime: FormControl<CheckinFormRawValue['checkoutTime']>;
  faceVerified: FormControl<CheckinFormRawValue['faceVerified']>;
  trip: FormControl<CheckinFormRawValue['trip']>;
};

export type CheckinFormGroup = FormGroup<CheckinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CheckinFormService {
  createCheckinFormGroup(checkin: CheckinFormGroupInput = { id: null }): CheckinFormGroup {
    const checkinRawValue = this.convertCheckinToCheckinRawValue({
      ...this.getFormDefaults(),
      ...checkin,
    });
    return new FormGroup<CheckinFormGroupContent>({
      id: new FormControl(
        { value: checkinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      checkinTime: new FormControl(checkinRawValue.checkinTime),
      checkoutTime: new FormControl(checkinRawValue.checkoutTime),
      faceVerified: new FormControl(checkinRawValue.faceVerified),
      trip: new FormControl(checkinRawValue.trip),
    });
  }

  getCheckin(form: CheckinFormGroup): ICheckin | NewCheckin {
    return this.convertCheckinRawValueToCheckin(form.getRawValue() as CheckinFormRawValue | NewCheckinFormRawValue);
  }

  resetForm(form: CheckinFormGroup, checkin: CheckinFormGroupInput): void {
    const checkinRawValue = this.convertCheckinToCheckinRawValue({ ...this.getFormDefaults(), ...checkin });
    form.reset(
      {
        ...checkinRawValue,
        id: { value: checkinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CheckinFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      checkinTime: currentTime,
      checkoutTime: currentTime,
      faceVerified: false,
    };
  }

  private convertCheckinRawValueToCheckin(rawCheckin: CheckinFormRawValue | NewCheckinFormRawValue): ICheckin | NewCheckin {
    return {
      ...rawCheckin,
      checkinTime: dayjs(rawCheckin.checkinTime, DATE_TIME_FORMAT),
      checkoutTime: dayjs(rawCheckin.checkoutTime, DATE_TIME_FORMAT),
    };
  }

  private convertCheckinToCheckinRawValue(
    checkin: ICheckin | (Partial<NewCheckin> & CheckinFormDefaults),
  ): CheckinFormRawValue | PartialWithRequiredKeyOf<NewCheckinFormRawValue> {
    return {
      ...checkin,
      checkinTime: checkin.checkinTime ? checkin.checkinTime.format(DATE_TIME_FORMAT) : undefined,
      checkoutTime: checkin.checkoutTime ? checkin.checkoutTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
