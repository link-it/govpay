import { Injectable, OnDestroy } from '@angular/core';

//Restricted Routing
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { UtilService } from './util.service';
import { LinkService } from './link.service';
import { GovpayService } from './govpay.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthGuardService implements OnDestroy {

  constructor(private ls: LinkService, private gps: GovpayService) { }

  ngOnDestroy(){
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean|Observable<boolean> {

    if (UtilService.PROFILO_UTENTE) {
      if(state.url.indexOf(UtilService.URL_TRACCIATI) != -1) {
        return this.checkAuthorities(state);
      }
      return true;
    }
    return this.gps.isAuthenticated(UtilService.URL_PROFILO).map(
      (result) => {
        this.gps.updateSpinner(false);
        UtilService.cacheUser(result.body);
        if(state.url.indexOf(UtilService.URL_TRACCIATI) != -1) {
          return this.checkAuthorities(state);
        }
        return true;
      }).catch(error => {
      this.gps.updateSpinner(false);
      UtilService.cleanUser();
      this.ls.routeToLoginForm(UtilService.URL_DASHBOARD);
      return Observable.of(false);
    });
  }

  /**
   * Controllo autorizzazioni
   *
   * @param state: RouterStateSnapshot
   * @returns {any}
   */
  private checkAuthorities(state: RouterStateSnapshot): boolean {
    let _tracciatiAccess = UtilService.PROFILO_UTENTE.acl.filter((acl) => {
      if(acl.servizio == 'Pagamenti e Pendenze') {
        return (acl.autorizzazioni.indexOf(UtilService._CODE.LETTURA) != -1 && acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) != -1);
      }
      return false;
    });

    return (_tracciatiAccess && _tracciatiAccess.length != 0 && state.url.indexOf(UtilService.URL_TRACCIATI) != -1);
  }

}
