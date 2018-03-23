import { AfterContentChecked, Component, HostListener, ViewChild } from '@angular/core';
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
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && sub.data.search);
  }
  @ViewChild('matS') matS: MatSidenav;

  _extraSideNavQueryMatches: MediaQueryList;
  _open: boolean = false;
  _searchComponentHeight: number = 0;
  _headerMenuIcon: boolean = false;
  _headerBackIcon: boolean = false;
  _headerSearchIcon: boolean = false;
  _headerActionsMenu: boolean = false;
  _spinner: boolean = false;
  _headerTitle: string = '';
  _actions: any[] = [];

  _showBlueDialog: boolean = false;
  _blueDialogData: ModalBehavior;

  constructor(public router: Router, public ls: LinkService, public gps: GovpayService, private us: UtilService) {
    this._extraSideNavQueryMatches = this.ls.checkLargeMediaMatch();
    this.router.events.filter(event => event instanceof NavigationEnd).subscribe((ne: NavigationEnd) => {
      let sub = this.ls.getRouterStateConfig(ne.urlAfterRedirects.split('?')[0]);
      this.updateHead(sub);
    });
  }

  ngOnInit() {
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
  }

  protected updateHead(rscData: any) {
    this._headerBackIcon = rscData.data.back;
    this._headerMenuIcon = (!this.ls.checkLargeMediaMatch().matches && !rscData.data.back);
    this._headerSearchIcon = (this.ls.checkSmallMediaMatch().matches && rscData.data.search);
    this._headerActionsMenu = rscData.data.actions;
    this._headerTitle = rscData.data.title;
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
    let _top = (this.ls.checkMediaMatch().matches)?56:64;
    return _top + this._searchComponentHeight;
  }

  /**
   * Internal actions list
   * @param rsc: Router state config data
   * @returns {any[]}
   * @private
   */
  protected _getHeaderActions(rsc: any): any[] {
    let a = UtilService.HEADER_ACTIONS.slice(0);
    switch(rsc.path) {
      case UtilService.URL_PENDENZE:
        a.push({ label: 'Aggiorna stato', type: UtilService.PENDENZA });
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
      this._searchComponentHeight = event.size;
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
    (this.matS && !_keepOpen)?this.matS.close():null;
    this.ls.resetRouteReuseStrategy();
    this.ls.resetRouteHistory();
    this.ls.navigateTo([event.target]);
  }

  /**
   * Internal header action menu trigger
   * @param event
   * @private
   */
  protected _doAction(event) {
    let _rsc = this.ls.getRouterStateConfig();
    //console.log(event, _rsc);
    let _componentRef = UtilService.ActiveDetailState;
    if(_componentRef) {
      let _mb: ModalBehavior = new ModalBehavior();
      _mb.closure = _componentRef.instance.refresh.bind(_componentRef.instance);
      switch(event.target.type) {
        case UtilService.PENDENZA:
          _mb.info = {
            parent: _rsc.data.info,
            dialogTitle: 'Modifica stato pendenza',
            templateName: UtilService.PENDENZA
          };
          UtilService.dialogBehavior.next(_mb);
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
