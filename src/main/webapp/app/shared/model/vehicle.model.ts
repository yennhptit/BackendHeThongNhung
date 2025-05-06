import { ITrip } from 'app/shared/model/trip.model';
import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';

export interface IVehicle {
  id?: number;
  licensePlate?: string;
  model?: string | null;
  status?: keyof typeof VehicleStatus | null;
  isDelete?: boolean | null;
  trips?: ITrip[] | null;
}

export const defaultValue: Readonly<IVehicle> = {
  isDelete: false,
};
