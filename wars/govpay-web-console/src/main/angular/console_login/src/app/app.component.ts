import { AfterViewInit, Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {

  news: any[] = [];
  _isLoading: boolean = false;
  logoutCode: string = '';
  _dashboardConfig: any;

  constructor(private message: MatSnackBar, private sanitizer: DomSanitizer) {}

  ngOnInit() {
    this.logoutCode = this.checkCode();
    this._dashboardConfig = this.cardConfig();
    if(this._dashboardConfig.news) {
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
            console.log('Error: ' + xhr.status);
          }
        }
      }.bind(this);
      this._isLoading = true;
      xhr.open('GET', url);
      xhr.setRequestHeader('Accept', 'application/vnd.github.v3.html+json');
      xhr.timeout = 20000;
      xhr.send();
    }
  }

  ngAfterViewInit() {
    this.alert();
  }

  protected _trustHtml(_html) {
    return this.sanitizer.bypassSecurityTrustHtml(_html);
  }

  protected checkCode(): string {
    return window['logoutCode']();
  }

  protected basePath(): string {
    return window['httpBasePath']();
  }

  protected cardConfig(): any {
    return window['cardConfig']();
  }

  /**
   *
   * Alert messages
   * @param {string} _message
   * @param {boolean} _keep
   */
  protected alert(_keep: boolean = false) {
    let _config = { duration: 10000, panelClass: 'overflow-hidden' };
    let _action = null;
    if (_keep){
      _config = null;
      _action = 'Chiudi';
    }
    let _message = '';
    switch(this.logoutCode) {
      case 'SESSION_EXPIRED_CODE':
        _message = 'Sessione scaduta.';
        break;
      case 'ACCESS_ERROR_CODE':
        _message = 'Credenziali errate.';
        break;
      case 'PROFILO_ERROR_CODE':
        _message = 'Utente non abilitato.';
        break;
    }
    if(this.logoutCode && _message != '') {
      setTimeout(() => {
        this.message.open(_message, _action, _config);
      });
    }
  }

}
