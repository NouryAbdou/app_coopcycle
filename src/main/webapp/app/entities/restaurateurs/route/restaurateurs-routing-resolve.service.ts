import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestaurateurs, Restaurateurs } from '../restaurateurs.model';
import { RestaurateursService } from '../service/restaurateurs.service';

@Injectable({ providedIn: 'root' })
export class RestaurateursRoutingResolveService implements Resolve<IRestaurateurs> {
  constructor(protected service: RestaurateursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaurateurs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restaurateurs: HttpResponse<Restaurateurs>) => {
          if (restaurateurs.body) {
            return of(restaurateurs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Restaurateurs());
  }
}
