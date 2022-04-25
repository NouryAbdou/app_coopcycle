import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';

export interface IZones {
  id?: number;
  ville?: string;
  metropole?: string | null;
  communaute?: string | null;
  cooperatives?: ICooperatives[] | null;
}

export class Zones implements IZones {
  constructor(
    public id?: number,
    public ville?: string,
    public metropole?: string | null,
    public communaute?: string | null,
    public cooperatives?: ICooperatives[] | null
  ) {}
}

export function getZonesIdentifier(zones: IZones): number | undefined {
  return zones.id;
}
