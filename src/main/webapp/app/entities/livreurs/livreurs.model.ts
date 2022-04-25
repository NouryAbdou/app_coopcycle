import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { ICooperatives } from 'app/entities/cooperatives/cooperatives.model';

export interface ILivreurs {
  id?: number;
  nom?: string;
  prenom?: string;
  city?: string;
  restaurateur?: IRestaurateurs | null;
  cooperative?: ICooperatives | null;
}

export class Livreurs implements ILivreurs {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public city?: string,
    public restaurateur?: IRestaurateurs | null,
    public cooperative?: ICooperatives | null
  ) {}
}

export function getLivreursIdentifier(livreurs: ILivreurs): number | undefined {
  return livreurs.id;
}
