import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILivreurs, Livreurs } from '../livreurs.model';
import { LivreursService } from '../service/livreurs.service';

@Injectable({ providedIn: 'root' })
export class LivreursRoutingResolveService implements Resolve<ILivreurs> {
  constructor(protected service: LivreursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILivreurs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((livreurs: HttpResponse<Livreurs>) => {
          if (livreurs.body) {
            return of(livreurs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Livreurs());
  }
}
