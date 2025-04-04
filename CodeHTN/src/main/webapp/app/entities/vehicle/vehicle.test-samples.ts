import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 19748,
  licensePlate: 'meh the interje',
};

export const sampleWithPartialData: IVehicle = {
  id: 24117,
  licensePlate: 'uncork moderate',
  model: 'eclipse radicalise',
};

export const sampleWithFullData: IVehicle = {
  id: 17147,
  licensePlate: 'tax with woodch',
  model: 'hallucinate sharply',
  status: 'AVAILABLE',
};

export const sampleWithNewData: NewVehicle = {
  licensePlate: 'passionate apud',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
