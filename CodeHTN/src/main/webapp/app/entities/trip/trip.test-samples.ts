import dayjs from 'dayjs/esm';

import { ITrip, NewTrip } from './trip.model';

export const sampleWithRequiredData: ITrip = {
  id: 18432,
};

export const sampleWithPartialData: ITrip = {
  id: 26613,
};

export const sampleWithFullData: ITrip = {
  id: 11960,
  startTime: dayjs('2025-04-04T00:20'),
  endTime: dayjs('2025-04-03T17:53'),
  status: 'COMPLETED',
};

export const sampleWithNewData: NewTrip = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
