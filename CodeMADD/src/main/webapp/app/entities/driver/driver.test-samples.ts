import dayjs from 'dayjs/esm';

import { IDriver, NewDriver } from './driver.model';

export const sampleWithRequiredData: IDriver = {
  id: 9068,
  name: 'bootleg mid proposal',
  rfidUid: 'sinful courteous',
  licenseNumber: 'completion modest li',
};

export const sampleWithPartialData: IDriver = {
  id: 6743,
  name: 'ah pair dead',
  rfidUid: 'oh geez',
  licenseNumber: 'extremely except meh',
  faceData: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2025-04-04T01:45'),
  status: 'ACTIVE',
};

export const sampleWithFullData: IDriver = {
  id: 24434,
  name: 'whoever',
  rfidUid: 'now after',
  licenseNumber: 'lard unless redecora',
  faceData: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2025-04-03T22:06'),
  status: 'ACTIVE',
};

export const sampleWithNewData: NewDriver = {
  name: 'pish scratch',
  rfidUid: 'yahoo carefully hen',
  licenseNumber: 'on',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
