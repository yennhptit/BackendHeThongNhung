import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDriver, NewDriver } from '../driver.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDriver for edit and NewDriverFormGroupInput for create.
 */
type DriverFormGroupInput = IDriver | PartialWithRequiredKeyOf<NewDriver>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDriver | NewDriver> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type DriverFormRawValue = FormValueOf<IDriver>;

type NewDriverFormRawValue = FormValueOf<NewDriver>;

type DriverFormDefaults = Pick<NewDriver, 'id' | 'createdAt'>;

type DriverFormGroupContent = {
  id: FormControl<DriverFormRawValue['id'] | NewDriver['id']>;
  name: FormControl<DriverFormRawValue['name']>;
  rfidUid: FormControl<DriverFormRawValue['rfidUid']>;
  licenseNumber: FormControl<DriverFormRawValue['licenseNumber']>;
  faceData: FormControl<DriverFormRawValue['faceData']>;
  createdAt: FormControl<DriverFormRawValue['createdAt']>;
  status: FormControl<DriverFormRawValue['status']>;
};

export type DriverFormGroup = FormGroup<DriverFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DriverFormService {
  createDriverFormGroup(driver: DriverFormGroupInput = { id: null }): DriverFormGroup {
    const driverRawValue = this.convertDriverToDriverRawValue({
      ...this.getFormDefaults(),
      ...driver,
    });
    return new FormGroup<DriverFormGroupContent>({
      id: new FormControl(
        { value: driverRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(driverRawValue.name, {
        validators: [Validators.required],
      }),
      rfidUid: new FormControl(driverRawValue.rfidUid, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      licenseNumber: new FormControl(driverRawValue.licenseNumber, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      faceData: new FormControl(driverRawValue.faceData),
      createdAt: new FormControl(driverRawValue.createdAt),
      status: new FormControl(driverRawValue.status),
    });
  }

  getDriver(form: DriverFormGroup): IDriver | NewDriver {
    return this.convertDriverRawValueToDriver(form.getRawValue() as DriverFormRawValue | NewDriverFormRawValue);
  }

  resetForm(form: DriverFormGroup, driver: DriverFormGroupInput): void {
    const driverRawValue = this.convertDriverToDriverRawValue({ ...this.getFormDefaults(), ...driver });
    form.reset(
      {
        ...driverRawValue,
        id: { value: driverRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DriverFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
    };
  }

  private convertDriverRawValueToDriver(rawDriver: DriverFormRawValue | NewDriverFormRawValue): IDriver | NewDriver {
    return {
      ...rawDriver,
      createdAt: dayjs(rawDriver.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertDriverToDriverRawValue(
    driver: IDriver | (Partial<NewDriver> & DriverFormDefaults),
  ): DriverFormRawValue | PartialWithRequiredKeyOf<NewDriverFormRawValue> {
    return {
      ...driver,
      createdAt: driver.createdAt ? driver.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
