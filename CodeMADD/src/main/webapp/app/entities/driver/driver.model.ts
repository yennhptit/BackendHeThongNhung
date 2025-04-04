import dayjs from 'dayjs/esm';
import { DriverStatus } from 'app/entities/enumerations/driver-status.model';

export interface IDriver {
  id: number;
  name?: string | null;
  rfidUid?: string | null;
  licenseNumber?: string | null;
  faceData?: string | null;
  createdAt?: dayjs.Dayjs | null;
  status?: keyof typeof DriverStatus | null;
}

export type NewDriver = Omit<IDriver, 'id'> & { id: null };
