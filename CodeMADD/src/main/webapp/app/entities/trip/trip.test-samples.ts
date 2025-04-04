import dayjs from 'dayjs/esm';

import { ITrip, NewTrip } from './trip.model';

export const sampleWithRequiredData: ITrip = {
  id: 29647,
};

export const sampleWithPartialData: ITrip = {
  id: 16053,
  endTime: dayjs('2025-04-03T20:39'),
  status: 'ONGOING',
};

export const sampleWithFullData: ITrip = {
  id: 31409,
  startTime: dayjs('2025-04-03T19:23'),
  endTime: dayjs('2025-04-03T22:56'),
  status: 'ONGOING',
};

export const sampleWithNewData: NewTrip = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
