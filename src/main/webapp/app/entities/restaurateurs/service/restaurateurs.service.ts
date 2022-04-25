import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurateurs, getRestaurateursIdentifier } from '../restaurateurs.model';

export type EntityResponseType = HttpResponse<IRestaurateurs>;
export type EntityArrayResponseType = HttpResponse<IRestaurateurs[]>;

@Injectable({ providedIn: 'root' })
export class RestaurateursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurateurs: IRestaurateurs): Observable<EntityResponseType> {
    return this.http.post<IRestaurateurs>(this.resourceUrl, restaurateurs, { observe: 'response' });
  }

  update(restaurateurs: IRestaurateurs): Observable<EntityResponseType> {
    return this.http.put<IRestaurateurs>(`${this.resourceUrl}/${getRestaurateursIdentifier(restaurateurs) as number}`, restaurateurs, {
      observe: 'response',
    });
  }

  partialUpdate(restaurateurs: IRestaurateurs): Observable<EntityResponseType> {
    return this.http.patch<IRestaurateurs>(`${this.resourceUrl}/${getRestaurateursIdentifier(restaurateurs) as number}`, restaurateurs, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurateurs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurateurs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaurateursToCollectionIfMissing(
    restaurateursCollection: IRestaurateurs[],
    ...restaurateursToCheck: (IRestaurateurs | null | undefined)[]
  ): IRestaurateurs[] {
    const restaurateurs: IRestaurateurs[] = restaurateursToCheck.filter(isPresent);
    if (restaurateurs.length > 0) {
      const restaurateursCollectionIdentifiers = restaurateursCollection.map(
        restaurateursItem => getRestaurateursIdentifier(restaurateursItem)!
      );
      const restaurateursToAdd = restaurateurs.filter(restaurateursItem => {
        const restaurateursIdentifier = getRestaurateursIdentifier(restaurateursItem);
        if (restaurateursIdentifier == null || restaurateursCollectionIdentifiers.includes(restaurateursIdentifier)) {
          return false;
        }
        restaurateursCollectionIdentifiers.push(restaurateursIdentifier);
        return true;
      });
      return [...restaurateursToAdd, ...restaurateursCollection];
    }
    return restaurateursCollection;
  }
}
