import dayjs from 'dayjs/esm';
import { ITrip } from 'app/entities/trip/trip.model';

export interface ICheckin {
  id: number;
  checkinTime?: dayjs.Dayjs | null;
  checkoutTime?: dayjs.Dayjs | null;
  faceVerified?: boolean | null;
  trip?: Pick<ITrip, 'id'> | null;
}

export type NewCheckin = Omit<ICheckin, 'id'> & { id: null };
