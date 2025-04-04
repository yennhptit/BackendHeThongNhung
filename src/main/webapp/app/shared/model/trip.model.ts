import { IDriver } from 'app/shared/model/driver.model';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { ICheckin } from 'app/shared/model/checkin.model';
import { TripStatus } from 'app/shared/model/enumerations/trip-status.model';

export interface ITrip {
  id?: number;
  startTime?: string | null;
  endTime?: string | null;
  status?: keyof typeof TripStatus | null;
  driver?: IDriver | null;
  vehicle?: IVehicle | null;
  checkin?: ICheckin | null;
}

export const defaultValue: Readonly<ITrip> = {};
