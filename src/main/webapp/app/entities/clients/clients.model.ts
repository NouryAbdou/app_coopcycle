import { IRestaurateurs } from 'app/entities/restaurateurs/restaurateurs.model';
import { ICommandes } from 'app/entities/commandes/commandes.model';

export interface IClients {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string | null;
  phoneNumber?: string;
  restaurateur?: IRestaurateurs | null;
  commandes?: ICommandes[] | null;
}

export class Clients implements IClients {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public email?: string | null,
    public phoneNumber?: string,
    public restaurateur?: IRestaurateurs | null,
    public commandes?: ICommandes[] | null
  ) {}
}

export function getClientsIdentifier(clients: IClients): number | undefined {
  return clients.id;
}
