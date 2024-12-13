import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { SslConfigComponent } from '../../../ssl-config/ssl-config.component';

@Component({
  selector: 'link-intermediario-view',
  templateUrl: './intermediario-view.component.html',
  styleUrls: ['./intermediario-view.component.scss']
})
export class IntermediarioViewComponent  implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('sslConfig') sslConfig: SslConfigComponent;
  @ViewChild('sslConfig') sslConfigRecuperoRT: SslConfigComponent;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  voce = Voce;
  protected _us = UtilService;
  protected _isSubscriptionKeyRecuperoRTRequired: boolean = false;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('denominazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('idIntermediario_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl());
    // Connettore SOAP: servizioPagoPa
    this.fGroup.addControl('principalPagoPa_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlRPT_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('subscriptionKey_ctrl', new FormControl('', []));
	this.fGroup.addControl('urlRecuperoRT_ctrl', new FormControl('', []));
    this.fGroup.addControl('subscriptionKeyRecuperoRT_ctrl', new FormControl('', []));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idIntermediario_ctrl'].disable();
        this.fGroup.controls['idIntermediario_ctrl'].setValue(this.json.idIntermediario);
        this.fGroup.controls['denominazione_ctrl'].setValue(this.json.denominazione);
        this.fGroup.controls['principalPagoPa_ctrl'].setValue(this.json.principalPagoPa);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        if (this.json.servizioPagoPa) {
          const _rpt = this.json.servizioPagoPa;
          this.fGroup.controls['urlRPT_ctrl'].setValue(_rpt.urlRPT?_rpt.urlRPT:'');
          this.fGroup.controls['subscriptionKey_ctrl'].setValue(_rpt.subscriptionKey);
        }
		if (this.json.servizioPagoPaRecuperoRT) {
          const _recuperoRT = this.json.servizioPagoPaRecuperoRT;
          this.fGroup.controls['urlRecuperoRT_ctrl'].setValue(_recuperoRT.url?_recuperoRT.url:'');
          this.fGroup.controls['subscriptionKeyRecuperoRT_ctrl'].setValue(_recuperoRT.subscriptionKey);
		  this._isSubscriptionKeyRecuperoRTRequired = _recuperoRT.url?true:false;
		  this.fGroup.controls['subscriptionKeyRecuperoRT_ctrl'].setValidators(_recuperoRT.url?[Validators.required]:[]);
        }
      }
    });
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    _json.idIntermediario = (!this.fGroup.controls['idIntermediario_ctrl'].disabled)?_info['idIntermediario_ctrl']:this.json.idIntermediario;
    _json.abilitato = _info['abilita_ctrl'] || false;
    _json.denominazione = (_info['denominazione_ctrl'])?_info['denominazione_ctrl']:null;
    _json.principalPagoPa = (_info['principalPagoPa_ctrl'])?_info['principalPagoPa_ctrl']:null;
    _json.servizioPagoPa = {
      auth: null,
      urlRPT: _info['urlRPT_ctrl'],
      subscriptionKey: _info['subscriptionKey_ctrl']?_info['subscriptionKey_ctrl']:null
    };
    _json.servizioPagoPa['auth'] = this.sslConfig.mapToJson();
    if(_json.servizioPagoPa.auth == null) { delete _json.servizioPagoPa.auth; }
	
	_json.servizioPagoPaRecuperoRT = {
      auth: null,
      url: _info['urlRecuperoRT_ctrl'],
      subscriptionKey: _info['subscriptionKeyRecuperoRT_ctrl']?_info['subscriptionKeyRecuperoRT_ctrl']:null
    };
    _json.servizioPagoPaRecuperoRT['auth'] = this.sslConfigRecuperoRT.mapToJson();
    if(_json.servizioPagoPaRecuperoRT.auth == null) { delete _json.servizioPagoPaRecuperoRT.auth; }
    return _json;
  }
  
  protected _onRecuperoRTUrlChange(trigger: any, targets: string) {
      targets.split('|').forEach((_target, _ti) => {
        const _tc = this.fGroup.controls[_target];
        _tc.clearValidators();
		this._isSubscriptionKeyRecuperoRTRequired = false;
        if(trigger.value.trim() !== '') {
          _tc.setValidators((_ti==0)?Validators.required:null);
		  this._isSubscriptionKeyRecuperoRTRequired = true;
        } 
        _tc.updateValueAndValidity();
      });
    }
}
