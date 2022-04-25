import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICooperatives, Cooperatives } from '../cooperatives.model';
import { CooperativesService } from '../service/cooperatives.service';

@Injectable({ providedIn: 'root' })
export class CooperativesRoutingResolveService implements Resolve<ICooperatives> {
  constructor(protected service: CooperativesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICooperatives> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cooperatives: HttpResponse<Cooperatives>) => {
          if (cooperatives.body) {
            return of(cooperatives.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cooperatives());
  }
}
