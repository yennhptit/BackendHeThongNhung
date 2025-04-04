import { IDriver } from 'app/shared/model/driver.model';
import { ViolationType } from 'app/shared/model/enumerations/violation-type.model';

export interface IViolation {
  id?: number;
  value?: number | null;
  timestamp?: string | null;
  type?: keyof typeof ViolationType | null;
  trip?: IDriver | null;
}

export const defaultValue: Readonly<IViolation> = {};
