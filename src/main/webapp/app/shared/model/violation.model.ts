import { IDriver } from 'app/shared/model/driver.model';
import { ViolationType } from 'app/shared/model/enumerations/violation-type.model';

export interface IViolation {
  id?: number;
  value?: number | null;
  timestamp?: string | null;
  type?: keyof typeof ViolationType | null;
  isDelete?: boolean | null;
  driver?: IDriver | null;
}

export const defaultValue: Readonly<IViolation> = {
  isDelete: false,
};
