import { ISchool } from 'app/shared/model/school.model';
import { WaterType } from 'app/shared/model/enumerations/water-type.model';

export interface IFish {
  id?: number;
  name?: string;
  age?: number;
  waterType?: WaterType;
  school?: ISchool;
}

export const defaultValue: Readonly<IFish> = {};
