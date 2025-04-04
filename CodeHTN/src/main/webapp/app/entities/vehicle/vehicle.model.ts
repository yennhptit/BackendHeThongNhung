import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';

export interface IVehicle {
  id: number;
  licensePlate?: string | null;
  model?: string | null;
  status?: keyof typeof VehicleStatus | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
