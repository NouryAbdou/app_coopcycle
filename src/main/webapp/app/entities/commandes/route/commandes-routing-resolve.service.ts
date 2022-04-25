import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommandes, Commandes } from '../commandes.model';
import { CommandesService } from '../service/commandes.service';

@Injectable({ providedIn: 'root' })
export class CommandesRoutingResolveService implements Resolve<ICommandes> {
  constructor(protected service: CommandesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommandes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commandes: HttpResponse<Commandes>) => {
          if (commandes.body) {
            return of(commandes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Commandes());
  }
}
