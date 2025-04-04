import dayjs from 'dayjs/esm';

import { IDriver, NewDriver } from './driver.model';

export const sampleWithRequiredData: IDriver = {
  id: 15992,
  name: 'without',
  rfidUid: 'even tightly',
  licenseNumber: 'grouse stealthily',
};

export const sampleWithPartialData: IDriver = {
  id: 22674,
  name: 'aw',
  rfidUid: 'caterpillar barium',
  licenseNumber: 'whup go palatable',
  createdAt: dayjs('2025-04-03T11:34'),
};

export const sampleWithFullData: IDriver = {
  id: 25157,
  name: 'ack herb shimmy',
  rfidUid: 'fox overdevelop',
  licenseNumber: 'mmm for',
  faceData: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2025-04-04T08:40'),
  status: 'INACTIVE',
};

export const sampleWithNewData: NewDriver = {
  name: 'steep devil',
  rfidUid: 'quizzical lope so',
  licenseNumber: 'yahoo brr',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
