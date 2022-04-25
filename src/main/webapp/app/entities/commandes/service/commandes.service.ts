import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommandes, getCommandesIdentifier } from '../commandes.model';

export type EntityResponseType = HttpResponse<ICommandes>;
export type EntityArrayResponseType = HttpResponse<ICommandes[]>;

@Injectable({ providedIn: 'root' })
export class CommandesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commandes: ICommandes): Observable<EntityResponseType> {
    return this.http.post<ICommandes>(this.resourceUrl, commandes, { observe: 'response' });
  }

  update(commandes: ICommandes): Observable<EntityResponseType> {
    return this.http.put<ICommandes>(`${this.resourceUrl}/${getCommandesIdentifier(commandes) as string}`, commandes, {
      observe: 'response',
    });
  }

  partialUpdate(commandes: ICommandes): Observable<EntityResponseType> {
    return this.http.patch<ICommandes>(`${this.resourceUrl}/${getCommandesIdentifier(commandes) as string}`, commandes, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICommandes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommandes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommandesToCollectionIfMissing(
    commandesCollection: ICommandes[],
    ...commandesToCheck: (ICommandes | null | undefined)[]
  ): ICommandes[] {
    const commandes: ICommandes[] = commandesToCheck.filter(isPresent);
    if (commandes.length > 0) {
      const commandesCollectionIdentifiers = commandesCollection.map(commandesItem => getCommandesIdentifier(commandesItem)!);
      const commandesToAdd = commandes.filter(commandesItem => {
        const commandesIdentifier = getCommandesIdentifier(commandesItem);
        if (commandesIdentifier == null || commandesCollectionIdentifiers.includes(commandesIdentifier)) {
          return false;
        }
        commandesCollectionIdentifiers.push(commandesIdentifier);
        return true;
      });
      return [...commandesToAdd, ...commandesCollection];
    }
    return commandesCollection;
  }
}
