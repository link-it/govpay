import { AfterContentChecked, AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';

import { LinkService } from "../../services/link.service";
import { UtilService } from '../../services/util.service';
import { GovpayService } from '../../services/govpay.service';

@Component({
  selector: 'link-side-navigator',
  templateUrl: './side-navigator.component.html',
  styleUrls: ['./side-navigator.component.scss']
})
export class SideNavigatorComponent implements OnInit, AfterContentChecked, AfterViewInit {
  @ViewChild('head') _head: ElementRef;
  @ViewChild('mat_list_menu') _matListMenu: ElementRef;

  @Input('app-name') appName: string = 'GovPay Enterprise';

  @Input('menu-principale') menu: Array<any> = [];
  @Input('menu-configurazioni') secMenu: Array<any> = [];
  @Input('menu-avanzato') terMenu: Array<any> = [];
  @Input('menu-reportistica') quaMenu: Array<any> = [];
  @Input('menu-manutenzione') pentaMenu: Array<any> = [];
  @Input('menu-impostazioni') esaMenu: Array<any> = [];

  @Input('utente-connesso') _utenteConnesso: string = '';
  @Input('side-nav-title') _title: string = '';
  @Input('side-nav-inhibitor') _disableSideNavigator: boolean = false;

  @Output('menu-navigation') onNavigationMenu: EventEmitter<any> = new EventEmitter();

  protected _topRef: number = 0;
  protected _profilo_router_link: any = { link: UtilService.URL_PROFILO, name: UtilService.TXT_PROFILO, xhttp: false, icon: true };

  constructor(public ls: LinkService, public gps: GovpayService, public us: UtilService) {}

  ngOnInit() {
  }

  /**
   * Bug Angular Material 5.2.x
   * Mat-sidenav scrolls back to top after
   * open if 'mode' is not 'side' because of
   * autofocus on first focusable element.
   */
  ngAfterViewInit() {
    let tis = this._matListMenu.nativeElement.querySelectorAll('[tabindex]');
    [].forEach.call(tis, function(el) {
      if(el.getAttribute('tabindex') != -1) {
        el.setAttribute('tabindex', -1);
      }
    });
  }

  ngAfterContentChecked() {
    setTimeout(() => {
      this.refStyles();
    });
  }

  protected _size() {
    let _top = (this.ls.checkMediaMatch().matches)?184:192;
    return {
      'height': _top+'px'
    }
  }

  protected refStyles() {
    if(this._topRef != this._head.nativeElement.clientHeight) {
      this._topRef = this._head.nativeElement.clientHeight;
    }
  }

  protected _menuRoute(section) {
    this.onNavigationMenu.emit({ target: section });
  }

  protected _routerActiveState(link: string): any {
    const url = this.ls.getRouterUrl();
    if ((link === url) || (link + UtilService.URL_DETTAGLIO === url)) {
      return true
    }
    return false;
  }

  protected _logout() {
    this.gps.logoutService();
  }

}
