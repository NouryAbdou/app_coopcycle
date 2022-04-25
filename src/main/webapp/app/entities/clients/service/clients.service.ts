import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClients, getClientsIdentifier } from '../clients.model';

export type EntityResponseType = HttpResponse<IClients>;
export type EntityArrayResponseType = HttpResponse<IClients[]>;

@Injectable({ providedIn: 'root' })
export class ClientsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clients: IClients): Observable<EntityResponseType> {
    return this.http.post<IClients>(this.resourceUrl, clients, { observe: 'response' });
  }

  update(clients: IClients): Observable<EntityResponseType> {
    return this.http.put<IClients>(`${this.resourceUrl}/${getClientsIdentifier(clients) as number}`, clients, { observe: 'response' });
  }

  partialUpdate(clients: IClients): Observable<EntityResponseType> {
    return this.http.patch<IClients>(`${this.resourceUrl}/${getClientsIdentifier(clients) as number}`, clients, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClients>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClients[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClientsToCollectionIfMissing(clientsCollection: IClients[], ...clientsToCheck: (IClients | null | undefined)[]): IClients[] {
    const clients: IClients[] = clientsToCheck.filter(isPresent);
    if (clients.length > 0) {
      const clientsCollectionIdentifiers = clientsCollection.map(clientsItem => getClientsIdentifier(clientsItem)!);
      const clientsToAdd = clients.filter(clientsItem => {
        const clientsIdentifier = getClientsIdentifier(clientsItem);
        if (clientsIdentifier == null || clientsCollectionIdentifiers.includes(clientsIdentifier)) {
          return false;
        }
        clientsCollectionIdentifiers.push(clientsIdentifier);
        return true;
      });
      return [...clientsToAdd, ...clientsCollection];
    }
    return clientsCollection;
  }
}
