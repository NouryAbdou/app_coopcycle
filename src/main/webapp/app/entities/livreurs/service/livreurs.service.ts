import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivreurs, getLivreursIdentifier } from '../livreurs.model';

export type EntityResponseType = HttpResponse<ILivreurs>;
export type EntityArrayResponseType = HttpResponse<ILivreurs[]>;

@Injectable({ providedIn: 'root' })
export class LivreursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livreurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livreurs: ILivreurs): Observable<EntityResponseType> {
    return this.http.post<ILivreurs>(this.resourceUrl, livreurs, { observe: 'response' });
  }

  update(livreurs: ILivreurs): Observable<EntityResponseType> {
    return this.http.put<ILivreurs>(`${this.resourceUrl}/${getLivreursIdentifier(livreurs) as number}`, livreurs, { observe: 'response' });
  }

  partialUpdate(livreurs: ILivreurs): Observable<EntityResponseType> {
    return this.http.patch<ILivreurs>(`${this.resourceUrl}/${getLivreursIdentifier(livreurs) as number}`, livreurs, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILivreurs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILivreurs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLivreursToCollectionIfMissing(livreursCollection: ILivreurs[], ...livreursToCheck: (ILivreurs | null | undefined)[]): ILivreurs[] {
    const livreurs: ILivreurs[] = livreursToCheck.filter(isPresent);
    if (livreurs.length > 0) {
      const livreursCollectionIdentifiers = livreursCollection.map(livreursItem => getLivreursIdentifier(livreursItem)!);
      const livreursToAdd = livreurs.filter(livreursItem => {
        const livreursIdentifier = getLivreursIdentifier(livreursItem);
        if (livreursIdentifier == null || livreursCollectionIdentifiers.includes(livreursIdentifier)) {
          return false;
        }
        livreursCollectionIdentifiers.push(livreursIdentifier);
        return true;
      });
      return [...livreursToAdd, ...livreursCollection];
    }
    return livreursCollection;
  }
}
