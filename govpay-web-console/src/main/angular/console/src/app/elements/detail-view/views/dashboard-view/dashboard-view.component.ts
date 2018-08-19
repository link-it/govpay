import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UtilService } from '../../../../services/util.service';
import { LinkService } from '../../../../services/link.service';

import * as moment from 'moment';

@Component({
  selector: 'link-dashboard-view',
  templateUrl: './dashboard-view.component.html',
  styleUrls: ['./dashboard-view.component.scss']
})
export class DashboardViewComponent implements OnInit {

  news: any[] = [];
  _isLoading:boolean = false;

  constructor(private sanitizer: DomSanitizer, private ls: LinkService) {}

  ngOnInit() {
    const url = 'https://api.github.com/repos/link-it/GovPay/releases';
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) {
          this._isLoading = false;
          this.news = xhr.response?JSON.parse(xhr.response):[];
          this.news.forEach((_news) => {
            _news.body_html = this._trustHtml(_news.body_html)
          });
        } else {
          this._isLoading = false;
          console.log('News Error: ' + xhr.status);
        }
      }
    }.bind(this);
    this._isLoading = true;
    xhr.open('GET', url);
    xhr.timeout = UtilService.TIMEOUT;
    xhr.setRequestHeader('Accept', 'application/vnd.github.v3.html+json');
    xhr.send();
  }

  protected _trustHtml(_html) {
    return this.sanitizer.bypassSecurityTrustHtml(_html);
  }

  protected _route(value: number) {
    UtilService.DASHBOARD_LINKS_PARAMS = { method: '', params: [] };
    UtilService.DASHBOARD_LINKS_PARAMS.method = UtilService.URL_PAGAMENTI;
    switch(value) {
      case 0:
        UtilService.DASHBOARD_LINKS_PARAMS.params.push({ controller: 'stato', value: 'FALLITO' });
        break;
      case 1:
        let _oneHourAgo: string = moment(new Date()).subtract(1, 'h').format('YYYY-MM-DDTHH:mm');
        UtilService.DASHBOARD_LINKS_PARAMS.params.push({ controller: 'stato', value: 'IN_CORSO' });
        UtilService.DASHBOARD_LINKS_PARAMS.params.push({ controller: 'dataRichiestaPagamentoFine', value: _oneHourAgo });
        break;
    }
    this.ls.resetRouteReuseStrategy();
    this.ls.navigateTo([UtilService.DASHBOARD_LINKS_PARAMS.method]);
  }
}
