import { AfterContentChecked, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material';

import { LinkService } from './services/link.service';
import { UtilService } from './services/util.service';
import { GovpayService } from './services/govpay.service';

import { NavigationEnd, Router } from '@angular/router';

import * as moment from 'moment';
import 'rxjs/add/operator/filter';
import { DialogViewComponent } from './elements/detail-view/views/dialog-view/dialog-view.component';

import { IModalDialog } from './classes/interfaces/IModalDialog';
import { ModalBehavior } from './classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpHeaders } from '@angular/common/http';
import { Voce } from './services/voce.service';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterContentChecked, IModalDialog {
  @HostListener('window:resize') onResize() {
    let sub = this.ls.getRouterStateConfig();
    this._headerMenuIcon = UtilService.PROFILO_UTENTE && (!this.ls.checkLargeMediaMatch().matches && !this._headerBackIcon);
    //this._headerMenuTitle = !(this.ls.checkLargeMediaMatch().matches);
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && sub.data.search);
    this._contentMarginTop = this._marginTop();
    if(this._applicationVersion) {
      this._once = false;
      this._facSimile();
    }
  }
  @ViewChild('matS') matS: MatSidenav;
  @ViewChild('lhm', { read: ElementRef }) linkHead: ElementRef;
  @ViewChild('facsimile', { read: ElementRef }) facsimile: ElementRef;

  _contentMarginTop: number = 0;
  _extraSideNavQueryMatches: MediaQueryList;
  _keepOnEsc: boolean = false;
  _open: boolean = false;
  _headerMenuIcon: boolean = false;
  _headerMenuTitle: boolean = false;
  _headerBackIcon: boolean = false;
  _headerSearchIcon: boolean = false;
  _headerActionsMenu: boolean = false;
  _spinner: boolean = false;
  _progress: boolean = false;
  _progressValue: number = 0;
  _headerSubTitle: string = '';
  _notificationTitle: string = 'GovPay sta acquisendo le rendicontazioni';
  _actions: any[] = [];

  _showBlueDialog: boolean = false;
  _blueDialogData: ModalBehavior;

  protected _sideNavSetup: any = { menu: [], secMenu: [], terMenu: [], quaMenu: [], pentaMenu: [], esaMenu: [], utenteConnesso: '' };
  protected _preventSideNav: boolean = true;
  protected _appName: string = UtilService.INFORMATION.APP_NAME;
  protected _applicationVersion: string;
  protected _once: boolean = false;

  //IModalDialog implementation
  json: any;

  constructor(public router: Router, public ls: LinkService, public gps: GovpayService, private us: UtilService) {
    this._extraSideNavQueryMatches = this.ls.checkLargeMediaMatch();
    this.router.events.filter(event => event instanceof NavigationEnd).subscribe((ne: NavigationEnd) => {
      let sub = this.ls.getRouterStateConfig(ne.urlAfterRedirects.split('?')[0]);
      if(sub) {
        this.updateHead(sub);
      }
    });
  }

  ngOnInit() {
    this.gps.getDataService(UtilService.URL_INFO).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        UtilService.APPLICATION_VERSION = response.body;
        if(UtilService.APPLICATION_VERSION && UtilService.APPLICATION_VERSION.ambiente) {
          this._applicationVersion = UtilService.APPLICATION_VERSION.ambiente;
        }
        if(UtilService.APPLICATION_VERSION && UtilService.APPLICATION_VERSION.appName) {
          this._appName = UtilService.APPLICATION_VERSION.appName;
          document.title = UtilService.APPLICATION_VERSION.appName;
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
    UtilService.profiloUtenteBehavior.subscribe((_profilo: any) => {
      if(_profilo) {
        UtilService.ID_TIPI_PENDENZA = _profilo.tipiPendenza || [];
        this.gps.multiGetService([
            UtilService.URL_SERVIZIACL,
            UtilService.URL_TIPI_VERSIONE_API,
            UtilService.URL_LABEL_TIPI_EVENTO,
            UtilService.URL_COMPONENTI_EVENTO
          ],
          [ 'SERVIZI',
            'TIPI_VERSIONE_API',
            'MAP_TIPI_EVENTO',
            'COMPONENTI_EVENTO',
          ], UtilService);
        this.setupSideNavigator();
      } else {
        this.ls.routeToLoginForm(UtilService.URL_DASHBOARD);
      }
      UtilService.headBehavior.next(this.ls.getRouterStateConfig());
    });
    this._actions = UtilService.HEADER_ACTIONS;
    UtilService.dialogBehavior.subscribe((_mb: ModalBehavior) => {
      if(_mb) {
        this.us.openDialog(DialogViewComponent, _mb);
      }
    });
    UtilService.blueDialogBehavior.subscribe((_mb: ModalBehavior) => {
      if(_mb) {
        this._blueDialogData = _mb;
        this._showBlueDialog = true;
      }
    });
    UtilService.headBehavior.subscribe((rscData: any) => {
      if(rscData) {
        this.updateHead(rscData);
      }
    });
  }

  ngAfterContentChecked() {
    this._preventSideNav = !UtilService.PROFILO_UTENTE;
    this._spinner = this.gps.spinner;
    this._progress = this.gps.progress;
    this._progressValue = this.gps.progressValue;
    this._contentMarginTop = this._marginTop();
    if(this._applicationVersion && !this._once) {
      this._facSimile();
    }
  }

  protected updateHead(rscData: any) {
    this._headerBackIcon = rscData.data.back;
    this._headerMenuIcon = UtilService.PROFILO_UTENTE && (!this.ls.checkLargeMediaMatch().matches && !rscData.data.back);
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && rscData.data.search);
    //this._headerMenuTitle = !(this.ls.checkLargeMediaMatch().matches);
    this._headerActionsMenu = rscData.data.actions;
    this._headerSubTitle = UtilService.PROFILO_UTENTE?rscData.data.title:this._appName;
    this._actions = this._getHeaderActions(rscData);
  }

  /**
   * Internal facsimile text rotation
   */
  protected _facSimile() {
    if(this.facsimile) {
      this._once = true;
      const facsimile = this.facsimile.nativeElement;
      const span = facsimile.querySelector('span');
      span.style.transform = 'rotate(-' + Math.atan((facsimile.clientHeight/facsimile.clientWidth))*180/Math.PI + 'deg)';
    }
  }

  /**
   * Internal side nav mode controller
   */
  protected _sideNavMode() {
    this._extraSideNavQueryMatches = this.ls.checkLargeMediaMatch();
    this._open = this._extraSideNavQueryMatches.matches;
    this._keepOnEsc = this._extraSideNavQueryMatches.matches;
    return this._extraSideNavQueryMatches.matches?'side':'over';
  }

  /**
   * Internal margin menu event
   */
  protected _marginTop() {
    return (this.linkHead)?this.linkHead.nativeElement.getBoundingClientRect().height:0;
  }

  /**
   * Internal actions list
   * @param rsc: Router state config data
   * @returns {any[]}
   * @private
   */
  protected _getHeaderActions(rsc: any): any[] {
    let a = UtilService.HEADER_ACTIONS.slice(0);
    switch(rsc.fullPath) {
      case UtilService.URL_PENDENZE:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_PENDENZE });
        break;
      case UtilService.URL_PAGAMENTI:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_PAGAMENTI });
        break;
      case UtilService.URL_GIORNALE_EVENTI:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_GIORNALE_EVENTI });
        break;
      case UtilService.URL_GIORNALE_EVENTI+UtilService.URL_DETTAGLIO:
        a.push({ label: Voce.VISTA_COMPLETA, type: UtilService.VISTA_COMPLETA_EVENTO_JSON });
        break;
      case UtilService.URL_RISCOSSIONI:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_RISCOSSIONI });
        break;
      case UtilService.URL_INCASSI:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_INCASSI });
        break;
      case UtilService.URL_RENDICONTAZIONI:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_RENDICONTAZIONI });
        break;
      case UtilService.URL_PENDENZE+UtilService.URL_DETTAGLIO:
        if(rsc.data.info) {
          if(rsc.data.info['stato'] == this.us.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.NON_ESEGUITA)) {
            a.push({ label: 'Annulla pendenza', type: UtilService.PENDENZA });
          }
          if(rsc.data.info['stato'] == this.us.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ANNULLATA)) {
            a.push({ label: 'Ripristina pendenza', type: UtilService.PENDENZA });
          }
        }
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_PENDENZA });
        break;
      case UtilService.URL_PAGAMENTI+UtilService.URL_DETTAGLIO:
        a.push({ label: 'Scarica resoconto', type: UtilService.EXPORT_PAGAMENTO });
        let _showExclude = false;
        if(rsc.data.info) {
          if(rsc.data.info['stato'] == this.us.getKeyByValue(UtilService.STATI_PAGAMENTO, UtilService.STATI_PAGAMENTO.IN_CORSO)) {
            UtilService.BACK_IN_TIME_DATE = moment().subtract(UtilService.BADGE.HOUR, 'h').format('YYYY-MM-DDTHH:mm');
            let dFine = moment(rsc.data.info['dataRichiestaPagamento']);
            let dLimit = moment(UtilService.BACK_IN_TIME_DATE);
            if(dFine < dLimit && !rsc.data.info['verificato']) {
              _showExclude = true;
            }
          }
          if(rsc.data.info['stato'] == this.us.getKeyByValue(UtilService.STATI_PAGAMENTO, UtilService.STATI_PAGAMENTO.FALLITO)) {
            if(!rsc.data.info['verificato']) {
              _showExclude = true;
            }
          }
          if(_showExclude) {
            a.push({ label: 'Segna come esaminato', type: UtilService.ESCLUDI_NOTIFICA });
          }
        }
        break;
      case UtilService.URL_TRACCIATI+UtilService.URL_DETTAGLIO:
        if(rsc.data.info) {
          let _sia = this.us.getKeyByValue(UtilService.STATI_TRACCIATO, UtilService.STATI_TRACCIATO.IN_ATTESA);
          let _sie = this.us.getKeyByValue(UtilService.STATI_TRACCIATO, UtilService.STATI_TRACCIATO.IN_ELABORAZIONE);
          if(rsc.data.info['stato'] != _sia && rsc.data.info['stato'] != _sie) {
            a.push({label: 'Scarica tracciato', type: UtilService.EXPORT_TRACCIATO});
          }
        }
        break;
    }
    return a;
  }

  protected setupSideNavigator() {
    this._sideNavSetup = {
      _utenteConnesso: '',
      menu: [
        { link: UtilService.URL_DASHBOARD, name: UtilService.TXT_DASHBOARD, xhttp: false, icon: false, sort: 0 }
      ],
      secMenu: [],
      terMenu: [],
      quaMenu: [],
      pentaMenu: [],
      esaMenu: []
    };
    this._sideNavSetup.utenteConnesso = UtilService.PROFILO_UTENTE.nome;
    Object.keys(UtilService.USER_ACL).forEach((key) => { UtilService.USER_ACL[key] = false; });
    UtilService.USER_ACL.hasPagamentiePendenze = false;
    UtilService.PROFILO_UTENTE.acl.forEach((acl) => {
      switch(acl.servizio) {
        case 'Anagrafica Applicazioni':
          UtilService.USER_ACL.hasApplicazioni = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_APPLICAZIONI, name: UtilService.TXT_APPLICAZIONI, xhttp: false, icon: false, sort: 3 });
          break;
        case 'Anagrafica Creditore':
          UtilService.USER_ACL.hasCreditore = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_DOMINI, name: UtilService.TXT_DOMINI, xhttp: false, icon: false, sort: 1 });
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_TIPI_PENDENZA, name: UtilService.TXT_TIPI_PENDENZA, xhttp: false, icon: false, sort: 2 });
          // this._sideNavSetup.secMenu.push({ link: UtilService.URL_ENTRATE, name: UtilService.TXT_ENTRATE, xhttp: false, icon: false, sort: -1 });
          break;
        case 'Rendicontazioni e Incassi':
          UtilService.USER_ACL.hasRendiIncassi = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.terMenu.push({ link: UtilService.URL_RENDICONTAZIONI, name: UtilService.TXT_RENDICONTAZIONI, xhttp: false, icon: false, sort: 0 });
//          this._sideNavSetup.terMenu.push({ link: UtilService.URL_INCASSI, name: UtilService.TXT_INCASSI, xhttp: false, icon: false, sort: 1 });
          this._sideNavSetup.menu.push({ link: UtilService.URL_INCASSI, name: UtilService.TXT_INCASSI, xhttp: false, icon: false, sort: 3 });
          this._sideNavSetup.terMenu.push({ link: UtilService.URL_RISCOSSIONI, name: UtilService.TXT_RISCOSSIONI, xhttp: false, icon: false, sort: 1 });
          if (acl.autorizzazioni.indexOf(UtilService._CODE.LETTURA) !== -1) {
            this._sideNavSetup.quaMenu.push({ link: UtilService.URL_PROSPETTO_RISCOSSIONI, name: UtilService.TXT_MAN_PROSPETTO_RISCOSSIONI, xhttp: true, icon: false, sort: 0 });
          }
          break;
        case 'Pagamenti':
          UtilService.USER_ACL.hasPagamenti = true;
          this._sideNavSetup.menu.push({ link: UtilService.URL_PAGAMENTI, name: UtilService.TXT_PAGAMENTI, xhttp: false, icon: false, sort: 1 });
          if(!UtilService.USER_ACL.hasPagamentiePendenze && acl.autorizzazioni.indexOf(UtilService._CODE.LETTURA) != -1 && acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) != -1) {
            UtilService.USER_ACL.hasPagamentiePendenze = true;
            this._sideNavSetup.terMenu.push({ link: UtilService.URL_TRACCIATI, name: UtilService.TXT_TRACCIATI, xhttp: false, icon: false, sort: 2 });
          }
          break;
        case 'Pendenze':
          UtilService.USER_ACL.hasPendenze = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.menu.push({ link: UtilService.URL_PENDENZE, name: UtilService.TXT_PENDENZE, xhttp: false, icon: false, sort: 2 });
          if(!UtilService.USER_ACL.hasPagamentiePendenze && acl.autorizzazioni.indexOf(UtilService._CODE.LETTURA) != -1 && acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) != -1) {
            UtilService.USER_ACL.hasPagamentiePendenze = true;
            this._sideNavSetup.terMenu.push({ link: UtilService.URL_TRACCIATI, name: UtilService.TXT_TRACCIATI, xhttp: false, icon: false, sort: 3 });
          }
          break;
        case 'Giornale degli Eventi':
          UtilService.USER_ACL.hasGdE = true;
          this._sideNavSetup.menu.push({ link: UtilService.URL_GIORNALE_EVENTI, name: UtilService.TXT_GIORNALE_EVENTI, xhttp: false, icon: false, sort: 4 });
          break;
        case 'Configurazione e manutenzione':
          UtilService.USER_ACL.hasConfig = true;
          // this._sideNavSetup.pentaMenu.push({ link: '#', name: UtilService.TXT_MAN_NOTIFICHE, xhttp: true, icon: false, sort: # });
          this._sideNavSetup.pentaMenu.push({ link: UtilService.URL_ACQUISIZIONE_RENDICONTAZIONI, name: UtilService.TXT_MAN_RENDICONTAZIONI, xhttp: true, icon: false, sort: 0 });
          this._sideNavSetup.pentaMenu.push({ link: UtilService.URL_RECUPERO_RPT_PENDENTI, name: UtilService.TXT_MAN_PAGAMENTI, xhttp: true, icon: false, sort: 1 });
          // this._sideNavSetup.pentaMenu.push({ link: '#', name: UtilService.TXT_MAN_CACHE, xhttp: true, icon: false, sort: # });
          UtilService.USER_ACL.hasSetting = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          if (UtilService.USER_ACL.hasSetting) {
            this._sideNavSetup.esaMenu.push({ link: UtilService.URL_IMPOSTAZIONI, name: UtilService.TXT_IMPOSTAZIONI, xhttp: false, icon: false, sort: 0 });
          }
          break;
        case 'Anagrafica PagoPA':
          UtilService.USER_ACL.hasPagoPA = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_REGISTRO_INTERMEDIARI, name: UtilService.TXT_REGISTRO_INTERMEDIARI, xhttp: false, icon: false, sort: 0 });
          //this._sideNavSetup.secMenu.push({ link: UtilService.URL_RPPS, name: UtilService.TXT_RPPS, xhttp: false, icon: false, sort: # });
          break;
        case 'Anagrafica Ruoli':
          UtilService.USER_ACL.hasRuoli = (acl.autorizzazioni.indexOf(UtilService._CODE.SCRITTURA) !== -1);
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_OPERATORI, name: UtilService.TXT_OPERATORI, xhttp: false, icon: false, sort: 4 });
          this._sideNavSetup.secMenu.push({ link: UtilService.URL_RUOLI, name: UtilService.TXT_RUOLI, xhttp: false, icon: false, sort: 5 });
          break;
      }
    });
    this._sideNavSetup.menu.sort(UtilService.SortMenu);
    this._sideNavSetup.secMenu.sort(UtilService.SortMenu);
    this._sideNavSetup.terMenu.sort(UtilService.SortMenu);
    this._sideNavSetup.quaMenu.sort(UtilService.SortMenu);
    this._sideNavSetup.pentaMenu.sort(UtilService.SortMenu);
  }

  /**
   * Internal side nav controller
   */
  protected toggleMenu() {
    if(this.matS) {
      (!this.matS.opened)?this.matS.open():this.matS.close();
    }
  }

  /**
   * Internal header menu icon event
   */
  protected _menuIcon() {
    this.toggleMenu();
  }

  /**
   * Internal header back icon event
   * @param event
   * @private
   */
  protected _backIcon() {
    this.ls.navigateBack();
  }

  /**
   * Internal header search icon event
   * @param event
   * @private
   */
  protected _toggleSearch(event) {
    if(event) {
      let sub = this.ls.getRouterStateConfig();
      this._headerSearchIcon = (!event.inputSearch && this.ls.checkSmallMediaMatch().matches && sub.data.search);
    }
  }

  /**
   * Internal header search event
   * @param event
   * @private
   */
  protected _querySearch(event) {
  }

  /**
   * Internal sidenav menu navigation
   * @param event
   * @private
   */
  protected _onMenuNavigation(event) {
    let _keepOpen = this.ls.checkLargeMediaMatch().matches;
    if(event.target.xhttp) {
      switch(event.target.link) {
        case UtilService.URL_RECUPERO_RPT_PENDENTI:
        case UtilService.URL_ACQUISIZIONE_RENDICONTAZIONI:
          this._instantService(event.target.link);
          break;
        case UtilService.URL_PROSPETTO_RISCOSSIONI:
          (this.matS && !_keepOpen)?this.matS.close():null;
          this._openReportConfig(UtilService.REPORT_PROSPETTO_RISCOSSIONI);
          break;
      }
    } else {
      (this.matS && !_keepOpen)?this.matS.close():null;
      this.ls.resetRouteReuseStrategy();
      this.ls.resetRouteHistory();
      this.us.resetDashboardLinksParams();
      if(event.target.icon) {
        this.ls.navigateTo([event.target.link]);
      }
    }
  }

  /**
   * Internal header action menu trigger
   * @param event
   * @private
   */
  protected _doAction(event: any) {
    if(event) {
      let _rsc = this.ls.getRouterStateConfig();
      //console.log(event, _rsc);
      let _componentRef = UtilService.ActiveDetailState;

      let _mb: ModalBehavior = new ModalBehavior();
      switch(event.target.type) {
        //Detail
        case UtilService.EXPORT_PENDENZA:
        case UtilService.EXPORT_PAGAMENTO:
        case UtilService.EXPORT_TRACCIATO:
          (_componentRef)?_componentRef.instance.exportData():null;
          break;
        case UtilService.ESCLUDI_NOTIFICA:
          (_componentRef)?_componentRef.instance.esclusioneNotifiche():null;
          break;
        case UtilService.VISTA_COMPLETA_EVENTO_JSON:
          (_componentRef)?_componentRef.instance.vistaCompletaEvento():null;
          break;
        case UtilService.PENDENZA:
          if(_componentRef) {
            _mb.closure = _componentRef.instance.refresh.bind(_componentRef.instance);
            _mb.editMode = true;
            _mb.info = {
              parent: _rsc.data.info,
              dialogTitle: 'Modifica stato pendenza',
              templateName: UtilService.PENDENZA
            };
            _mb.operation = UtilService.PATCH_METHODS.REPLACE;
            UtilService.dialogBehavior.next(_mb);
          }
          break;
        //Sidelist
        case UtilService.EXPORT_PENDENZE:
        case UtilService.EXPORT_PAGAMENTI:
        case UtilService.EXPORT_GIORNALE_EVENTI:
        case UtilService.EXPORT_RISCOSSIONI:
        case UtilService.EXPORT_INCASSI:
        case UtilService.EXPORT_RENDICONTAZIONI:
          UtilService.exportBehavior.next(event.target.type);
          break;
      }
    }
  }

  /**
   * Reset Blue Dialog
   * @private
   */
  protected reset() {
    this._blueDialogData = new ModalBehavior();
    this._showBlueDialog = false;
  }

  /**
   * Close Blue Dialog
   * @param {ModalBehavior} event
   * @private
   */
  protected _onCloseBlueDialog(event: ModalBehavior) {
    this.reset();
    if(event.closure) {
      event.closure(event);
    }
  }

  /**
   * Instant service from menu
   * @param {string} link
   * @private
   */
  protected _instantService(link: string) {
    let _service = UtilService.URL_OPERAZIONI+link;
    this.gps.getDataService(_service).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        if(response && response.status) {
          let _msg = 'Processo di acquisizione fallito';
          switch(_service) {
            case UtilService.URL_OPERAZIONI+UtilService.URL_ACQUISIZIONE_RENDICONTAZIONI:
              if(response.status == 200) {
                _msg = 'Processo di acquisizione rendicontazioni completato.';
              }
              break;
            case UtilService.URL_OPERAZIONI+UtilService.URL_RECUPERO_RPT_PENDENTI:
              if(response.status == 200) {
                _msg = 'Processo di acquisizione pagamenti completato.';
              }
              break;
          }
          this.us.alert(_msg);
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  /**
   * Modal dialog implementation
   */
  protected _openReportConfig(type: string) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: null,
      dialogTitle: 'Parametri Report',
      templateName: type
    };
    _mb.async_callback = this.save.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {}

  /**
   * Save report
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = UtilService.URL_REPORTISTICHE;
    let headers;
    switch(mb.info.templateName) {
      case UtilService.REPORT_PROSPETTO_RISCOSSIONI:
        _service += UtilService.URL_PROSPETTO_RISCOSSIONI;
        headers = new HttpHeaders();
        headers = headers.set('Content-Type', 'application/pdf');
        headers = headers.set('Accept', 'application/pdf');
        break;
    }
    const json = mb.info.viewModel;
    let query = [];
    if(json.idDominio) {
      query.push('idDominio='+json.idDominio);
    }
    if(json.dataDa) {
      query.push('dataDa='+json.dataDa);
    }
    if(json.dataA) {
      query.push('dataA='+json.dataA);
    }
    this.gps.saveData(_service, null, query.join('&'), UtilService.METHODS.GET, true, headers, 'blob').subscribe(
      (response) => {
          let name = 'Report_' + moment().format('YYYY-MM-DDTHH:mm:ss').toString() + '.pdf';
          let _cd = response.headers.get("content-disposition");
          let _re = /(?:filename=['"](.*\.pdf)['"])/gm;
          let _results = _re.exec(_cd);
          if(_results && _results.length == 2) {
            name = _results[1];
          }
          let zip = new JSZip();
          zip.file(name, response.body);
          zip.generateAsync({type: 'blob'}).then(function (zipData) {
            FileSaver(zipData, name + '.zip');
            this.gps.updateSpinner(false);
          }.bind(this));
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

}
