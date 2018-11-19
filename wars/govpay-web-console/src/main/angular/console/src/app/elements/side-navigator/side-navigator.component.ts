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

  @Input('menu-principale') menu: Array<any> = [
    { link: UtilService.URL_DASHBOARD, name: UtilService.TXT_DASHBOARD, xhttp: false, icon: false }
  ];
  @Input('menu-configurazioni') secMenu: Array<any> = [];
  @Input('menu-avanzato') terMenu: Array<any> = [];
  @Input('menu-manutenzione') quaMenu: Array<any> = [];

  @Input('side-nav-title') _title: string = '';
  @Input('side-nav-inhibitor') _disableSideNavigator: boolean = false;

  @Output('menu-navigation') onNavigationMenu: EventEmitter<any> = new EventEmitter();

  protected _topRef: number = 0;
  protected _profilo_router_link: any = { link: UtilService.URL_PROFILO, name: UtilService.TXT_PROFILO, xhttp: false, icon: true };
  protected _utenteConnesso: string = '';

  constructor(public ls: LinkService, public gps: GovpayService, public us: UtilService) {}

  ngOnInit() {
    this._getProfilo();
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

  protected _logout() {
    this.gps.logoutService();
  }

  protected _getProfilo() {
    this.gps.getDataService(UtilService.URL_PROFILO).subscribe(
      (_response) => {
        UtilService.PROFILO_UTENTE = JSON.parse(JSON.stringify(_response.body));
        this._utenteConnesso = UtilService.PROFILO_UTENTE.nome;
        Object.keys(UtilService.USER_ACL).forEach((key) => { UtilService.USER_ACL[key] = false; });
        UtilService.PROFILO_UTENTE.acl.forEach((acl) => {
          switch(acl.servizio) {
            case 'Anagrafica Applicazioni':
              UtilService.USER_ACL.hasApplicazioni = true;
              this.secMenu.push({ link: UtilService.URL_APPLICAZIONI, name: UtilService.TXT_APPLICAZIONI, xhttp: false, icon: false });
            break;
            case 'Anagrafica Creditore':
              UtilService.USER_ACL.hasCreditore = true;
              // this.secMenu.push({ link: UtilService.URL_ENTRATE, name: UtilService.TXT_ENTRATE, xhttp: false, icon: false });
            break;
            case 'Rendicontazioni e Incassi':
              UtilService.USER_ACL.hasRendiIncassi = true;
              this.terMenu.push({ link: UtilService.URL_RENDICONTAZIONI, name: UtilService.TXT_RENDICONTAZIONI, xhttp: false, icon: false });
              this.terMenu.push({ link: UtilService.URL_INCASSI, name: UtilService.TXT_INCASSI, xhttp: false, icon: false });
              this.terMenu.push({ link: UtilService.URL_RISCOSSIONI, name: UtilService.TXT_RISCOSSIONI, xhttp: false, icon: false });
            break;
            case 'Pagamenti e Pendenze':
              UtilService.USER_ACL.hasPagamentiePendenze = true;
              this.menu.push({ link: UtilService.URL_PENDENZE, name: UtilService.TXT_PENDENZE, xhttp: false, icon: false });
              this.menu.push({ link: UtilService.URL_PAGAMENTI, name: UtilService.TXT_PAGAMENTI, xhttp: false, icon: false });
              if(acl.autorizzazioni.indexOf(UtilService._CODE.LETTURA) != -1 && acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) != -1) {
                this.terMenu.push({ link: UtilService.URL_TRACCIATI, name: UtilService.TXT_TRACCIATI, xhttp: false, icon: false });
              }
            break;
            case 'Giornale degli Eventi':
              UtilService.USER_ACL.hasGdE = true;
              this.menu.push({ link: UtilService.URL_GIORNALE_EVENTI, name: UtilService.TXT_GIORNALE_EVENTI, xhttp: false, icon: false });
            break;
            case 'Configurazione e manutenzione':
              UtilService.USER_ACL.hasConfig = true;
              this.secMenu.push({ link: UtilService.URL_OPERATORI, name: UtilService.TXT_OPERATORI, xhttp: false, icon: false });
              // this.quaMenu.push({ link: '#', name: UtilService.TXT_MAN_NOTIFICHE, xhttp: true, icon: false });
              this.quaMenu.push({ link: UtilService.URL_ACQUISIZIONE_RENDICONTAZIONI, name: UtilService.TXT_MAN_RENDICONTAZIONI, xhttp: true, icon: false });
              this.quaMenu.push({ link: UtilService.URL_RECUPERO_RPT_PENDENTI, name: UtilService.TXT_MAN_PAGAMENTI, xhttp: true, icon: false });
              // this.quaMenu.push({ link: '#', name: UtilService.TXT_MAN_CACHE, xhttp: true, icon: false });
            break;
            case 'Anagrafica PagoPA':
              UtilService.USER_ACL.hasPagoPA = true;
              this.secMenu.push({ link: UtilService.URL_DOMINI, name: UtilService.TXT_DOMINI, xhttp: false, icon: false });
              this.secMenu.push({ link: UtilService.URL_REGISTRO_INTERMEDIARI, name: UtilService.TXT_REGISTRO_INTERMEDIARI, xhttp: false, icon: false });
              //this.secMenu.push({ link: UtilService.URL_RPPS, name: UtilService.TXT_RPPS, xhttp: false, icon: false });
            break;
            case 'Anagrafica Ruoli':
              UtilService.USER_ACL.hasRuoli = true;
              this.secMenu.push({ link: UtilService.URL_RUOLI, name: UtilService.TXT_RUOLI, xhttp: false, icon: false });
            break;
          }
        });
        this.gps.updateSpinner(false);
      },
      (error) => {
        //console.log(error);
        this.menu = [];
        this.secMenu = [];
        this.terMenu = [];
        this.quaMenu = [];
        this._utenteConnesso = '';
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

}
