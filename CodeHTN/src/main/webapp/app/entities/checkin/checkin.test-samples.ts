import dayjs from 'dayjs/esm';

import { ICheckin, NewCheckin } from './checkin.model';

export const sampleWithRequiredData: ICheckin = {
  id: 23609,
};

export const sampleWithPartialData: ICheckin = {
  id: 28012,
  checkoutTime: dayjs('2025-04-03T18:15'),
  faceVerified: false,
};

export const sampleWithFullData: ICheckin = {
  id: 8290,
  checkinTime: dayjs('2025-04-03T19:49'),
  checkoutTime: dayjs('2025-04-03T09:19'),
  faceVerified: true,
};

export const sampleWithNewData: NewCheckin = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
