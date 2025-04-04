import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 9133,
  licensePlate: 'swarm temp quir',
};

export const sampleWithPartialData: IVehicle = {
  id: 15961,
  licensePlate: 'phew rank drat',
  model: 'given zowie',
  status: 'AVAILABLE',
};

export const sampleWithFullData: IVehicle = {
  id: 15113,
  licensePlate: 'hmph',
  model: 'ouch pond hm',
  status: 'AVAILABLE',
};

export const sampleWithNewData: NewVehicle = {
  licensePlate: 'line phew plus',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
