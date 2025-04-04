import dayjs from 'dayjs/esm';
import { ITrip } from 'app/entities/trip/trip.model';
import { ViolationType } from 'app/entities/enumerations/violation-type.model';

export interface IViolation {
  id: number;
  type?: keyof typeof ViolationType | null;
  value?: number | null;
  timestamp?: dayjs.Dayjs | null;
  trip?: Pick<ITrip, 'id'> | null;
}

export type NewViolation = Omit<IViolation, 'id'> & { id: null };
