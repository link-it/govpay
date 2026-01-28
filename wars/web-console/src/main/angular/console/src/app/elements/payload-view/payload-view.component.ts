import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

enum MimeEnum {
  JSON = 'application/json',
  XML = 'text/xml',
}

@Component({
  selector: 'link-payload-view',
  templateUrl: './payload-view.component.html',
  styleUrls: ['./payload-view.component.scss']
})
export class PayloadViewComponent implements OnInit {

  @Input() data: any = null;
  @Input() attribute: string = '';
  @Input() showCopy: boolean = true;

  @Output() copied: EventEmitter<boolean> = new EventEmitter();

  payload = null;
  payloadString = null;
  type= null;
  header = null;

  _arrXML = ['API_PAGOPA', 'API_MAGGIOLI'];

  readonly MimeEnum = MimeEnum;

  constructor() { }

  ngOnInit() {
    if (this.data[this.attribute]) {
      this.header = this.data[this.attribute].headers;
      const _contentTypeRichiesta = this.header.find((item: any) => (item.nome.toLowerCase() === 'content-type'));
      if (_contentTypeRichiesta) {
        this.type = _contentTypeRichiesta.valore.split(';')[0] || null;
      }
      if (!this.type) {
        this.type = this._arrXML.includes(this.data.componente) ? MimeEnum.XML : MimeEnum.JSON;
      }
      this.payload = this._decodeB64ToJson(this.data[this.attribute].payload);
    }
  }

  protected _decodeB64ToJson(_base: string): any {
    const _jsonDecode = decodeURIComponent(atob(_base).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    this.payloadString = _jsonDecode;
    if (this.type === MimeEnum.JSON) {
      return JSON.parse(_jsonDecode);
    } else {
      return _jsonDecode;
    }
  }

  copyPayload() {
    window.navigator['clipboard'].writeText(this.payloadString)
      .then( r => this.copied.emit(true) )
      .catch( e => { this.copied.emit(false); console.log(e) });
  }
}
