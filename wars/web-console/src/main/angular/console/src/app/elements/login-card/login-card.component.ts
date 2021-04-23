import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'link-login-card',
  templateUrl: './login-card.component.html',
  styleUrls: ['./login-card.component.scss']
})
export class LoginCardComponent implements OnInit, AfterViewInit {
  @ViewChild('formSpid') _formSpid: ElementRef;

  @Input('notify') _notify: boolean = false;
  @Input('label') _label: string = 'Accedi';

  @Input('SAMLDS') _SAMLDS: number = 1;
  @Input('response-target') _target: string = '';

  @Input('action') _action: string = '';
  @Input('method') _method: string = 'get';

  @Input('providers') _providers: Providers;

  @Output('on-submit') _submit: EventEmitter<any> = new EventEmitter();

  _arubaURL: string = '';
  _infocertURL: string = '';
  _intesaURL: string = '';
  _lepidaURL: string = '';
  _namirialURL: string = '';
  _posteURL: string = '';
  _registerURL: string = '';
  _sielteURL: string = '';
  _timURL: string = '';
  _spidTestURL: string = '';

  _entityID: string = '';
  _fg: FormGroup = new FormGroup({
    samlds: new FormControl(),
    target: new FormControl(),
    entityID: new FormControl()
  });

  constructor(protected http: HttpClient) { }

  ngOnInit() {
    if (this._providers) {
      if (this._providers.ARUBA) {
        this._arubaURL = this._providers.ARUBA;
      }
      if (this._providers.INFOCERT) {
        this._infocertURL = this._providers.INFOCERT;
      }
      if (this._providers.INTESA) {
        this._intesaURL = this._providers.INTESA;
      }
      if (this._providers.LEPIDA) {
        this._lepidaURL = this._providers.LEPIDA;
      }
      if (this._providers.NAMIRIAL) {
        this._namirialURL = this._providers.NAMIRIAL;
      }
      if (this._providers.POSTE) {
        this._posteURL = this._providers.POSTE;
      }
      if (this._providers.REGISTER) {
        this._registerURL = this._providers.REGISTER;
      }
      if (this._providers.SIELTE) {
        this._sielteURL = this._providers.SIELTE;
      }
      if (this._providers.TIM) {
        this._timURL = this._providers.TIM;
      }
      if (this._providers.SPID_TEST) {
        this._spidTestURL = this._providers.SPID_TEST;
      }
    }
  }

  ngAfterViewInit() {
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

export class Providers {
  SPID_TEST: string = '';
  ARUBA: string = '';
  INFOCERT: string = '';
  INTESA: string = '';
  LEPIDA: string = '';
  NAMIRIAL: string = '';
  POSTE: string = '';
  REGISTER: string = '';
  SIELTE: string = '';
  TIM: string = '';
}
