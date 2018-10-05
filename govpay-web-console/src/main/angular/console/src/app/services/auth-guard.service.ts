import { Injectable, OnDestroy } from '@angular/core';

//Restricted Routing
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { UtilService } from './util.service';
import { LinkService } from './link.service';

@Injectable()
export class AuthGuardService implements OnDestroy {

  constructor(private ls: LinkService) { }

  ngOnDestroy(){
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    if (UtilService.PROFILO_UTENTE) {
      return this.checkAuthorities(state);
    } else {
      this.ls.navigateToRoot();
    }

    return false;
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
