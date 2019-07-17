import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'link-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.scss']
})
export class LoginCardComponent implements OnInit {
  @ViewChild('formSpid') _formSpid: ElementRef;

  @Input('notify') _notify: boolean = false;

  @Input('SAMLDS') _SAMLDS: number = 1;
  @Input('response-target') _target: string = '';

  @Input('action') _action: string = '';
  @Input('method') _method: string = 'get';

  @Input('aruba-url') _arubaURL: string = 'https://sp.agenziaentrate.gov.it/rp/aruba/s3';
  @Input('infocert-url') _infocertURL: string = 'https://sp.agenziaentrate.gov.it/rp/infocert/s3';
  @Input('intesa-url') _intesaURL: string = 'https://sp.agenziaentrate.gov.it/rp/intesa/s3';
  @Input('lepida-url') _lepidaURL: string = 'https://sp.agenziaentrate.gov.it/rp/lepida/s3';
  @Input('namirial-url') _namirialURL: string = 'https://sp.agenziaentrate.gov.it/rp/namirial/s3';
  @Input('poste-url') _posteURL: string = 'https://sp.agenziaentrate.gov.it/rp/poste/s3';
  @Input('sielte-url') _sielteURL: string = 'https://sp.agenziaentrate.gov.it/rp/sielte/s3';
  @Input('register-url') _registerURL: string = 'https://sp.agenziaentrate.gov.it/rp/register/s3';
  @Input('tim-url') _timURL: string = 'https://sp.agenziaentrate.gov.it/rp/titt/s3';
  @Input('spid-test-url') _spidTestURL: string = '';

  @Output('on-submit') _submit: EventEmitter<any> = new EventEmitter();

  _entityID: string = '';
  _fg: FormGroup = new FormGroup({
    samlds: new FormControl(),
    target: new FormControl(),
    entityID: new FormControl()
  });

  constructor(protected http: HttpClient) { }

  ngOnInit() {
  }

  _onSubmit(id: string, url: string) {
    if (url) {
      this._fg.controls['samlds'].setValue(this._SAMLDS);
      this._fg.controls['target'].setValue(this._target);
      this._fg.controls['entityID'].setValue(url);
      if (this._notify) {
        this._submit.emit({ spid: id, target: this._target, form: this._fg.getRawValue() });
      }
      if (this._formSpid && this._target) {
        this._formSpid.nativeElement.submit();
      }
    }
  }

}
