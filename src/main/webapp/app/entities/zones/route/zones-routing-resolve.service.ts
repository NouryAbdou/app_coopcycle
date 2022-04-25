import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IZones, Zones } from '../zones.model';
import { ZonesService } from '../service/zones.service';

@Injectable({ providedIn: 'root' })
export class ZonesRoutingResolveService implements Resolve<IZones> {
  constructor(protected service: ZonesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZones> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((zones: HttpResponse<Zones>) => {
          if (zones.body) {
            return of(zones.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Zones());
  }
}
