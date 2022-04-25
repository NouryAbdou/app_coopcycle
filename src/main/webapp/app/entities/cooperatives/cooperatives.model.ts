import { IZones } from 'app/entities/zones/zones.model';

export interface ICooperatives {
  id?: number;
  nom?: string;
  zone?: IZones | null;
}

export class Cooperatives implements ICooperatives {
  constructor(public id?: number, public nom?: string, public zone?: IZones | null) {}
}

export function getCooperativesIdentifier(cooperatives: ICooperatives): number | undefined {
  return cooperatives.id;
}
