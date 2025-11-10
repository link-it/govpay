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
  @ViewChild('sslConfigRecuperoRT') sslConfigRecuperoRT: SslConfigComponent;
  @ViewChild('sslConfigACA') sslConfigACA: SslConfigComponent;
  @ViewChild('sslConfigGPD') sslConfigGPD: SslConfigComponent;
  @ViewChild('sslConfigFR') sslConfigFR: SslConfigComponent;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  voce = Voce;
  protected _us = UtilService;
  protected _isSubscriptionKeyRecuperoRTRequired: boolean = false;
  protected _isSubscriptionKeyACARequired: boolean = false;
  protected _isSubscriptionKeyGPDRequired: boolean = false;
  protected _isSubscriptionKeyFRRequired: boolean = false;

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
	this.fGroup.addControl('urlACA_ctrl', new FormControl('', []));
    this.fGroup.addControl('subscriptionKeyACA_ctrl', new FormControl('', []));
	this.fGroup.addControl('urlGPD_ctrl', new FormControl('', []));
    this.fGroup.addControl('subscriptionKeyGPD_ctrl', new FormControl('', []));
	this.fGroup.addControl('urlFR_ctrl', new FormControl('', []));
    this.fGroup.addControl('subscriptionKeyFR_ctrl', new FormControl('', []));
	// FormGroup dedicati per i componenti SSL
	this.fGroup.addControl('sslAuthPagoPa', new FormGroup({}));
	this.fGroup.addControl('sslAuthPagoPaRecuperoRT', new FormGroup({}));
	this.fGroup.addControl('sslAuthPagoPaACA', new FormGroup({}));
	this.fGroup.addControl('sslAuthPagoPaGPD', new FormGroup({}));
	this.fGroup.addControl('sslAuthPagoPaFR', new FormGroup({}));
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
		if (this.json.servizioPagoPaACA) {
          const _aca = this.json.servizioPagoPaACA;
          this.fGroup.controls['urlACA_ctrl'].setValue(_aca.url?_aca.url:'');
          this.fGroup.controls['subscriptionKeyACA_ctrl'].setValue(_aca.subscriptionKey);
		  this._isSubscriptionKeyACARequired = _aca.url?true:false;
		  this.fGroup.controls['subscriptionKeyACA_ctrl'].setValidators(_aca.url?[Validators.required]:[]);
        }
		if (this.json.servizioPagoPaGPD) {
          const _gpd = this.json.servizioPagoPaGPD;
          this.fGroup.controls['urlGPD_ctrl'].setValue(_gpd.url?_gpd.url:'');
          this.fGroup.controls['subscriptionKeyGPD_ctrl'].setValue(_gpd.subscriptionKey);
		  this._isSubscriptionKeyGPDRequired = _gpd.url?true:false;
		  this.fGroup.controls['subscriptionKeyGPD_ctrl'].setValidators(_gpd.url?[Validators.required]:[]);
        }
		if (this.json.servizioPagoPaFR) {
          const _fr = this.json.servizioPagoPaFR;
          this.fGroup.controls['urlFR_ctrl'].setValue(_fr.url?_fr.url:'');
          this.fGroup.controls['subscriptionKeyFR_ctrl'].setValue(_fr.subscriptionKey);
		  this._isSubscriptionKeyFRRequired = _fr.url?true:false;
		  this.fGroup.controls['subscriptionKeyFR_ctrl'].setValidators(_fr.url?[Validators.required]:[]);
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
	
	if(_info['urlRecuperoRT_ctrl'] != null && _info['urlRecuperoRT_ctrl'] != ''){
		_json.servizioPagoPaRecuperoRT = {
	      auth: null,
	      url: _info['urlRecuperoRT_ctrl'],
	      subscriptionKey: _info['subscriptionKeyRecuperoRT_ctrl']?_info['subscriptionKeyRecuperoRT_ctrl']:null
	    };
	    _json.servizioPagoPaRecuperoRT['auth'] = this.sslConfigRecuperoRT.mapToJson();
	    if(_json.servizioPagoPaRecuperoRT.auth == null) { delete _json.servizioPagoPaRecuperoRT.auth; }
	}
	if(_info['urlACA_ctrl'] != null && _info['urlACA_ctrl'] != ''){
		_json.servizioPagoPaACA = {
	      auth: null,
	      url: _info['urlACA_ctrl'],
	      subscriptionKey: _info['subscriptionKeyACA_ctrl']?_info['subscriptionKeyACA_ctrl']:null
	    };
	    _json.servizioPagoPaACA['auth'] = this.sslConfigACA.mapToJson();
	    if(_json.servizioPagoPaACA.auth == null) { delete _json.servizioPagoPaACA.auth; }
	}
	if(_info['urlGPD_ctrl'] != null && _info['urlGPD_ctrl'] != ''){
		_json.servizioPagoPaGPD = {
	      auth: null,
	      url: _info['urlGPD_ctrl'],
	      subscriptionKey: _info['subscriptionKeyGPD_ctrl']?_info['subscriptionKeyGPD_ctrl']:null
	    };
	    _json.servizioPagoPaGPD['auth'] = this.sslConfigGPD.mapToJson();
	    if(_json.servizioPagoPaGPD.auth == null) { delete _json.servizioPagoPaGPD.auth; }
	}
	if(_info['urlFR_ctrl'] != null && _info['urlFR_ctrl'] != ''){
		_json.servizioPagoPaFR = {
	      auth: null,
	      url: _info['urlFR_ctrl'],
	      subscriptionKey: _info['subscriptionKeyFR_ctrl']?_info['subscriptionKeyFR_ctrl']:null
	    };
	    _json.servizioPagoPaFR['auth'] = this.sslConfigFR.mapToJson();
	    if(_json.servizioPagoPaFR.auth == null) { delete _json.servizioPagoPaFR.auth; }
	}
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

  protected _onACAUrlChange(trigger: any, targets: string) {
      targets.split('|').forEach((_target, _ti) => {
        const _tc = this.fGroup.controls[_target];
        _tc.clearValidators();
		this._isSubscriptionKeyACARequired = false;
        if(trigger.value.trim() !== '') {
          _tc.setValidators((_ti==0)?Validators.required:null);
		  this._isSubscriptionKeyACARequired = true;
        }
        _tc.updateValueAndValidity();
      });
    }

  protected _onGPDUrlChange(trigger: any, targets: string) {
      targets.split('|').forEach((_target, _ti) => {
        const _tc = this.fGroup.controls[_target];
        _tc.clearValidators();
		this._isSubscriptionKeyGPDRequired = false;
        if(trigger.value.trim() !== '') {
          _tc.setValidators((_ti==0)?Validators.required:null);
		  this._isSubscriptionKeyGPDRequired = true;
        }
        _tc.updateValueAndValidity();
      });
    }

  protected _onFRUrlChange(trigger: any, targets: string) {
      targets.split('|').forEach((_target, _ti) => {
        const _tc = this.fGroup.controls[_target];
        _tc.clearValidators();
		this._isSubscriptionKeyFRRequired = false;
        if(trigger.value.trim() !== '') {
          _tc.setValidators((_ti==0)?Validators.required:null);
		  this._isSubscriptionKeyFRRequired = true;
        }
        _tc.updateValueAndValidity();
      });
    }
}
