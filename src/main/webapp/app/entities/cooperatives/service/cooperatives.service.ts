import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICooperatives, getCooperativesIdentifier } from '../cooperatives.model';

export type EntityResponseType = HttpResponse<ICooperatives>;
export type EntityArrayResponseType = HttpResponse<ICooperatives[]>;

@Injectable({ providedIn: 'root' })
export class CooperativesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cooperatives');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cooperatives: ICooperatives): Observable<EntityResponseType> {
    return this.http.post<ICooperatives>(this.resourceUrl, cooperatives, { observe: 'response' });
  }

  update(cooperatives: ICooperatives): Observable<EntityResponseType> {
    return this.http.put<ICooperatives>(`${this.resourceUrl}/${getCooperativesIdentifier(cooperatives) as number}`, cooperatives, {
      observe: 'response',
    });
  }

  partialUpdate(cooperatives: ICooperatives): Observable<EntityResponseType> {
    return this.http.patch<ICooperatives>(`${this.resourceUrl}/${getCooperativesIdentifier(cooperatives) as number}`, cooperatives, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICooperatives>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICooperatives[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCooperativesToCollectionIfMissing(
    cooperativesCollection: ICooperatives[],
    ...cooperativesToCheck: (ICooperatives | null | undefined)[]
  ): ICooperatives[] {
    const cooperatives: ICooperatives[] = cooperativesToCheck.filter(isPresent);
    if (cooperatives.length > 0) {
      const cooperativesCollectionIdentifiers = cooperativesCollection.map(
        cooperativesItem => getCooperativesIdentifier(cooperativesItem)!
      );
      const cooperativesToAdd = cooperatives.filter(cooperativesItem => {
        const cooperativesIdentifier = getCooperativesIdentifier(cooperativesItem);
        if (cooperativesIdentifier == null || cooperativesCollectionIdentifiers.includes(cooperativesIdentifier)) {
          return false;
        }
        cooperativesCollectionIdentifiers.push(cooperativesIdentifier);
        return true;
      });
      return [...cooperativesToAdd, ...cooperativesCollection];
    }
    return cooperativesCollection;
  }
}
