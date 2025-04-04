import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IViolation, NewViolation } from '../violation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IViolation for edit and NewViolationFormGroupInput for create.
 */
type ViolationFormGroupInput = IViolation | PartialWithRequiredKeyOf<NewViolation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IViolation | NewViolation> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type ViolationFormRawValue = FormValueOf<IViolation>;

type NewViolationFormRawValue = FormValueOf<NewViolation>;

type ViolationFormDefaults = Pick<NewViolation, 'id' | 'timestamp'>;

type ViolationFormGroupContent = {
  id: FormControl<ViolationFormRawValue['id'] | NewViolation['id']>;
  type: FormControl<ViolationFormRawValue['type']>;
  value: FormControl<ViolationFormRawValue['value']>;
  timestamp: FormControl<ViolationFormRawValue['timestamp']>;
  trip: FormControl<ViolationFormRawValue['trip']>;
};

export type ViolationFormGroup = FormGroup<ViolationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ViolationFormService {
  createViolationFormGroup(violation: ViolationFormGroupInput = { id: null }): ViolationFormGroup {
    const violationRawValue = this.convertViolationToViolationRawValue({
      ...this.getFormDefaults(),
      ...violation,
    });
    return new FormGroup<ViolationFormGroupContent>({
      id: new FormControl(
        { value: violationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(violationRawValue.type),
      value: new FormControl(violationRawValue.value),
      timestamp: new FormControl(violationRawValue.timestamp),
      trip: new FormControl(violationRawValue.trip),
    });
  }

  getViolation(form: ViolationFormGroup): IViolation | NewViolation {
    return this.convertViolationRawValueToViolation(form.getRawValue() as ViolationFormRawValue | NewViolationFormRawValue);
  }

  resetForm(form: ViolationFormGroup, violation: ViolationFormGroupInput): void {
    const violationRawValue = this.convertViolationToViolationRawValue({ ...this.getFormDefaults(), ...violation });
    form.reset(
      {
        ...violationRawValue,
        id: { value: violationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ViolationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertViolationRawValueToViolation(rawViolation: ViolationFormRawValue | NewViolationFormRawValue): IViolation | NewViolation {
    return {
      ...rawViolation,
      timestamp: dayjs(rawViolation.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertViolationToViolationRawValue(
    violation: IViolation | (Partial<NewViolation> & ViolationFormDefaults),
  ): ViolationFormRawValue | PartialWithRequiredKeyOf<NewViolationFormRawValue> {
    return {
      ...violation,
      timestamp: violation.timestamp ? violation.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
