import { IClients } from 'app/entities/clients/clients.model';
import { IRestaurants } from 'app/entities/restaurants/restaurants.model';

export interface ICommandes {
  id?: string;
  estPret?: boolean | null;
  estPaye?: boolean | null;
  client?: IClients | null;
  restaurants?: IRestaurants[] | null;
}

export class Commandes implements ICommandes {
  constructor(
    public id?: string,
    public estPret?: boolean | null,
    public estPaye?: boolean | null,
    public client?: IClients | null,
    public restaurants?: IRestaurants[] | null
  ) {
    this.estPret = this.estPret ?? false;
    this.estPaye = this.estPaye ?? false;
  }
}

export function getCommandesIdentifier(commandes: ICommandes): string | undefined {
  return commandes.id;
}
