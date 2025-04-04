import dayjs from 'dayjs/esm';

import { IViolation, NewViolation } from './violation.model';

export const sampleWithRequiredData: IViolation = {
  id: 12080,
};

export const sampleWithPartialData: IViolation = {
  id: 26388,
  type: 'DROWSINESS',
  value: 28783.21,
};

export const sampleWithFullData: IViolation = {
  id: 14698,
  type: 'ALCOHOL',
  value: 30767.75,
  timestamp: dayjs('2025-04-03T17:29'),
};

export const sampleWithNewData: NewViolation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
