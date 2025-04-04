import dayjs from 'dayjs/esm';

import { ICheckin, NewCheckin } from './checkin.model';

export const sampleWithRequiredData: ICheckin = {
  id: 1826,
};

export const sampleWithPartialData: ICheckin = {
  id: 6988,
  checkinTime: dayjs('2025-04-03T19:45'),
};

export const sampleWithFullData: ICheckin = {
  id: 18706,
  checkinTime: dayjs('2025-04-04T03:46'),
  checkoutTime: dayjs('2025-04-04T05:45'),
  faceVerified: true,
};

export const sampleWithNewData: NewCheckin = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
