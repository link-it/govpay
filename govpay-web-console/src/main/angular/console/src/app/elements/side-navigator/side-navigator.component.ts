import { AfterContentChecked, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';

import { LinkService } from "../../services/link.service";
import { Profilo } from '../../classes/profilo';
import { UtilService } from '../../services/util.service';

@Component({
  selector: 'link-side-navigator',
  templateUrl: './side-navigator.component.html',
  styleUrls: ['./side-navigator.component.scss']
})
export class SideNavigatorComponent implements OnInit, AfterContentChecked {
  @ViewChild('head') _head: ElementRef;

  @Input('app-name') appName: string = 'GovPay Enterprise';
  @Input('menu-principale') menu: Array<any> = [
    { link: UtilService.URL_PAGAMENTI, name: UtilService.TXT_PAGAMENTI },
    { link: UtilService.URL_PENDENZE, name: UtilService.TXT_PENDENZE}
  ];
  @Input('menu-secondario') secMenu: Array<any> = [
    { link: UtilService.URL_ACL, name: UtilService.TXT_ACL},
    { link: UtilService.URL_APPLICAZIONI, name: UtilService.TXT_APPLICAZIONI},
    { link: UtilService.URL_DOMINI, name: UtilService.TXT_DOMINI},
    { link: UtilService.URL_ENTRATE, name: UtilService.TXT_ENTRATE},
    { link: UtilService.URL_RENDICONTAZIONI, name: UtilService.TXT_RENDICONTAZIONI},
    { link: UtilService.URL_GIORNALE_EVENTI, name: UtilService.TXT_GIORNALE_EVENTI},
    { link: UtilService.URL_INCASSI, name: UtilService.TXT_INCASSI},
    { link: UtilService.URL_OPERATORI, name: UtilService.TXT_OPERATORI},
    { link: UtilService.URL_REGISTRO_INTERMEDIARI, name: UtilService.TXT_REGISTRO_INTERMEDIARI},
    { link: UtilService.URL_REGISTRO_PSP, name: UtilService.TXT_REGISTRO_PSP},
    { link: UtilService.URL_RPPS, name: UtilService.TXT_RPPS},
    { link: UtilService.URL_RISCOSSIONI, name: UtilService.TXT_RISCOSSIONI}
  ];
  @Input('profilo-utente') profilo: Profilo = new Profilo();

  @Output('menu-navigation') onNavigationMenu: EventEmitter<any> = new EventEmitter();

  protected up: boolean = false;
  protected _topRef: number = 0;
  protected _profilo_router_link: string = UtilService.URL_PROFILO;

  constructor(public ls: LinkService) {}

  ngOnInit() {
  }

  ngAfterContentChecked() {
    setTimeout(() => {
      this.refStyles();
    });
  }

  protected refStyles() {
    if(this._topRef != this._head.nativeElement.clientHeight) {
      this._topRef = this._head.nativeElement.clientHeight;
    }
  }

  protected _userInfo() {
    this.up = !this.up;
  }

  protected _menuRoute(link) {
    this.onNavigationMenu.emit({ target: link });
  }

}
