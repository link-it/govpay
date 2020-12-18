import { Injectable, OnDestroy } from '@angular/core';

//Restricted Routing
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';

import { UtilService } from './util.service';
import { Observable } from 'rxjs/Observable';
import { LinkService } from './link.service';

@Injectable()
export class AuthGuardService implements OnDestroy, CanActivate {

  constructor(private ls: LinkService) { }

  ngOnDestroy() {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean|Observable<boolean> {

    if (UtilService.PROFILO_UTENTE) {
      if(state.url.indexOf(UtilService.URL_TRACCIATI) !== -1) {
        const hasRule: boolean = UtilService.USER_ACL.hasPagamentiePendenze;
        if (!hasRule) {
          this.ls.navigateTo([UtilService.URL_DASHBOARD]);
        }
        return hasRule;
      }
      return true;
    }

    this.ls.navigateTo([UtilService.URL_DASHBOARD]);

    return false;
  }

}
