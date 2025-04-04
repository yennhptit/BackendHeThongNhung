import dayjs from 'dayjs/esm';

import { IViolation, NewViolation } from './violation.model';

export const sampleWithRequiredData: IViolation = {
  id: 18251,
};

export const sampleWithPartialData: IViolation = {
  id: 30918,
  timestamp: dayjs('2025-04-03T18:57'),
};

export const sampleWithFullData: IViolation = {
  id: 25895,
  type: 'ALCOHOL',
  value: 16197.58,
  timestamp: dayjs('2025-04-03T23:19'),
};

export const sampleWithNewData: NewViolation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
