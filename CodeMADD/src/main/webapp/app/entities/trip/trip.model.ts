import dayjs from 'dayjs/esm';
import { IDriver } from 'app/entities/driver/driver.model';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { TripStatus } from 'app/entities/enumerations/trip-status.model';

export interface ITrip {
  id: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  status?: keyof typeof TripStatus | null;
  driver?: Pick<IDriver, 'id'> | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
}

export type NewTrip = Omit<ITrip, 'id'> & { id: null };
