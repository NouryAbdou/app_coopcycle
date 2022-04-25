import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { ICommandes } from 'app/entities/commandes/commandes.model';

export interface IRestaurants {
  id?: number;
  nom?: string;
  carte?: string;
  menu?: string | null;
  restaurateur?: IRestaurateurs | null;
  commandes?: ICommandes[] | null;
}

export class Restaurants implements IRestaurants {
  constructor(
    public id?: number,
    public nom?: string,
    public carte?: string,
    public menu?: string | null,
    public restaurateur?: IRestaurateurs | null,
    public commandes?: ICommandes[] | null
  ) {}
}

export function getRestaurantsIdentifier(restaurants: IRestaurants): number | undefined {
  return restaurants.id;
}
