import { ITrip } from 'app/shared/model/trip.model';
import { IViolation } from 'app/shared/model/violation.model';
import { DriverStatus } from 'app/shared/model/enumerations/driver-status.model';

export interface IDriver {
  id?: number;
  name?: string;
  driverId?: string | null;
  licenseNumber?: string | null;
  faceData?: string | null;
  createdAt?: string | null;
  status?: keyof typeof DriverStatus | null;
  isDelete?: boolean | null;
  trips?: ITrip[] | null;
  violations?: IViolation[] | null;
}

export const defaultValue: Readonly<IDriver> = {
  isDelete: false,
};
