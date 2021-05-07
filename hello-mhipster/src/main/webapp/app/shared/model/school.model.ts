import { IFish } from 'app/shared/model/fish.model';

export interface ISchool {
  id?: number;
  name?: string;
  fish?: IFish[];
}

export const defaultValue: Readonly<ISchool> = {};
