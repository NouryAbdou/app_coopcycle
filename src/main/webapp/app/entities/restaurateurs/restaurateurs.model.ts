import { IRestaurants } from 'app/entities/restaurants/restaurants.model';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';
import { IClients } from 'app/entities/clients/clients.model';
import { ILivreurs } from 'app/entities/livreurs/livreurs.model';

export interface IRestaurateurs {
  id?: number;
  nom?: string;
  prenom?: string;
  city?: string | null;
  restaurants?: IRestaurants[] | null;
  cooperative?: ICooperatives | null;
  client?: IClients | null;
  livreur?: ILivreurs | null;
}

export class Restaurateurs implements IRestaurateurs {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public city?: string | null,
    public restaurants?: IRestaurants[] | null,
    public cooperative?: ICooperatives | null,
    public client?: IClients | null,
    public livreur?: ILivreurs | null
  ) {}
}

export function getRestaurateursIdentifier(restaurateurs: IRestaurateurs): number | undefined {
  return restaurateurs.id;
}
