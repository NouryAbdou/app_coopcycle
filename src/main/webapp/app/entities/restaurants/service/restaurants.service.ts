import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurants, getRestaurantsIdentifier } from '../restaurants.model';

export type EntityResponseType = HttpResponse<IRestaurants>;
export type EntityArrayResponseType = HttpResponse<IRestaurants[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurants: IRestaurants): Observable<EntityResponseType> {
    return this.http.post<IRestaurants>(this.resourceUrl, restaurants, { observe: 'response' });
  }

  update(restaurants: IRestaurants): Observable<EntityResponseType> {
    return this.http.put<IRestaurants>(`${this.resourceUrl}/${getRestaurantsIdentifier(restaurants) as number}`, restaurants, {
      observe: 'response',
    });
  }

  partialUpdate(restaurants: IRestaurants): Observable<EntityResponseType> {
    return this.http.patch<IRestaurants>(`${this.resourceUrl}/${getRestaurantsIdentifier(restaurants) as number}`, restaurants, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurants>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurants[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaurantsToCollectionIfMissing(
    restaurantsCollection: IRestaurants[],
    ...restaurantsToCheck: (IRestaurants | null | undefined)[]
  ): IRestaurants[] {
    const restaurants: IRestaurants[] = restaurantsToCheck.filter(isPresent);
    if (restaurants.length > 0) {
      const restaurantsCollectionIdentifiers = restaurantsCollection.map(restaurantsItem => getRestaurantsIdentifier(restaurantsItem)!);
      const restaurantsToAdd = restaurants.filter(restaurantsItem => {
        const restaurantsIdentifier = getRestaurantsIdentifier(restaurantsItem);
        if (restaurantsIdentifier == null || restaurantsCollectionIdentifiers.includes(restaurantsIdentifier)) {
          return false;
        }
        restaurantsCollectionIdentifiers.push(restaurantsIdentifier);
        return true;
      });
      return [...restaurantsToAdd, ...restaurantsCollection];
    }
    return restaurantsCollection;
  }
}
