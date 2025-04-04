import { ITrip } from 'app/shared/model/trip.model';

export interface ICheckin {
  id?: number;
  checkinTime?: string | null;
  checkoutTime?: string | null;
  faceVerified?: boolean | null;
  trip?: ITrip | null;
}

export const defaultValue: Readonly<ICheckin> = {
  faceVerified: false,
};
