import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZones, getZonesIdentifier } from '../zones.model';

export type EntityResponseType = HttpResponse<IZones>;
export type EntityArrayResponseType = HttpResponse<IZones[]>;

@Injectable({ providedIn: 'root' })
export class ZonesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/zones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(zones: IZones): Observable<EntityResponseType> {
    return this.http.post<IZones>(this.resourceUrl, zones, { observe: 'response' });
  }

  update(zones: IZones): Observable<EntityResponseType> {
    return this.http.put<IZones>(`${this.resourceUrl}/${getZonesIdentifier(zones) as number}`, zones, { observe: 'response' });
  }

  partialUpdate(zones: IZones): Observable<EntityResponseType> {
    return this.http.patch<IZones>(`${this.resourceUrl}/${getZonesIdentifier(zones) as number}`, zones, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IZones>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IZones[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addZonesToCollectionIfMissing(zonesCollection: IZones[], ...zonesToCheck: (IZones | null | undefined)[]): IZones[] {
    const zones: IZones[] = zonesToCheck.filter(isPresent);
    if (zones.length > 0) {
      const zonesCollectionIdentifiers = zonesCollection.map(zonesItem => getZonesIdentifier(zonesItem)!);
      const zonesToAdd = zones.filter(zonesItem => {
        const zonesIdentifier = getZonesIdentifier(zonesItem);
        if (zonesIdentifier == null || zonesCollectionIdentifiers.includes(zonesIdentifier)) {
          return false;
        }
        zonesCollectionIdentifiers.push(zonesIdentifier);
        return true;
      });
      return [...zonesToAdd, ...zonesCollection];
    }
    return zonesCollection;
  }
}
