import { AfterContentChecked, Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material';

import { LinkService } from './services/link.service';
import { UtilService } from './services/util.service';
import { GovpayService } from './services/govpay.service';

import { NavigationEnd, Router } from '@angular/router';

import 'rxjs/add/operator/filter';
import { DialogViewComponent } from './elements/detail-view/views/dialog-view/dialog-view.component';
import { ModalBehavior } from './classes/modal-behavior';

@Component({
  selector: 'link-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterContentChecked {
  @HostListener('window:resize') onResize() {
    let sub = this.ls.getRouterStateConfig();
    this._headerMenuIcon = (!this.ls.checkLargeMediaMatch().matches && !this._headerBackIcon);
    //this._headerMenuTitle = !(this.ls.checkLargeMediaMatch().matches);
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && sub.data.search);
    this._contentMarginTop = this._marginTop();
  }
  @ViewChild('matS') matS: MatSidenav;
  @ViewChild('lhm', { read: ElementRef }) linkHead: ElementRef;

  _contentMarginTop: number = 0;
  _extraSideNavQueryMatches: MediaQueryList;
  _open: boolean = false;
  _headerMenuIcon: boolean = false;
  _headerMenuTitle: boolean = false;
  _headerBackIcon: boolean = false;
  _headerSearchIcon: boolean = false;
  _headerActionsMenu: boolean = false;
  _spinner: boolean = false;
  _progress: boolean = false;
  _progressValue: number = 0;
  _appName: string = 'GovPay';
  _headerSubTitle: string = '';
  _notificationTitle: string = 'Govpay sta acquisendo le rendicontazioni';
  _actions: any[] = [];

  _showBlueDialog: boolean = false;
  _blueDialogData: ModalBehavior;

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
    this.gps.multiGetService([ UtilService.URL_SERVIZIACL, UtilService.URL_TIPI_VERSIONE_API ], [ 'SERVIZI', 'TIPI_VERSIONE_API' ], UtilService);
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
    this._spinner = this.gps.spinner;
    this._progress = this.gps.progress;
    this._progressValue = this.gps.progressValue;
    this._contentMarginTop = this._marginTop();
  }

  protected updateHead(rscData: any) {
    this._headerBackIcon = rscData.data.back;
    this._headerMenuIcon = (!this.ls.checkLargeMediaMatch().matches && !rscData.data.back);
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && rscData.data.search);
    //this._headerMenuTitle = !(this.ls.checkLargeMediaMatch().matches);
    this._headerActionsMenu = rscData.data.actions;
    this._headerSubTitle = rscData.data.title;
    this._actions = this._getHeaderActions(rscData);
  }

  /**
   * Internal side nav mode controller
   */
  protected _sideNavMode() {
    this._extraSideNavQueryMatches = this.ls.checkLargeMediaMatch();
    this._open = this._extraSideNavQueryMatches.matches;
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
        break;
    }
    return a;
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
    if(event.target.xhttp) {
      let _service = UtilService.URL_OPERAZIONI+event.target.link;
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
    } else {
      let _keepOpen = this.ls.checkLargeMediaMatch().matches;
      (this.matS && !_keepOpen)?this.matS.close():null;
      this.ls.resetRouteReuseStrategy();
      this.ls.resetRouteHistory();
      this.us.resetDashboardLinksParams();
      //this.ls.navigateTo([event.target.link]);
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
          (_componentRef)?_componentRef.instance.exportData():null;
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

}
