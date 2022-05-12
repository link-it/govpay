import { AfterViewInit, AfterContentChecked, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../services/util.service';
import { GovpayService } from '../../services/govpay.service';
import { Voce } from '../../services/voce.service';
import { MatTabGroup } from '@angular/material';

declare let jQuery: any;

@Component({
  selector: 'link-impostazioni-view',
  templateUrl: './impostazioni-view.component.html',
  styleUrls: ['./impostazioni-view.component.scss']
})
export class ImpostazioniViewComponent implements OnInit, AfterViewInit, AfterContentChecked {
@ViewChild('tg', { read: MatTabGroup }) tg: MatTabGroup;
@ViewChild('tgIO', { read: MatTabGroup }) tgIO: MatTabGroup;

  protected Voce = Voce;
  protected Util = UtilService;
  protected json: any;

  protected _tgFormIsValid: boolean = false;
  protected _tgIOFormIsValid: boolean = false;
  protected hasTsValue: boolean = false;
  protected hasKsValue: boolean = false;
  protected _tsControllers: string[] = ['tsType_ctrl', 'truststoreLocation_ctrl', 'truststorePassword_ctrl', 'truststoreAlgorithm_ctrl'];
  protected _ksControllers: string[] = ['ksType_ctrl', 'keystoreLocation_ctrl', 'keystorePassword_ctrl', 'keystoreAlgorithm_ctrl'];
  protected _sslControllers: string[] = ['cryptoType_ctrl'];

  protected gdeForm: FormGroup;
  protected serverForm: FormGroup;
  protected avvisaturaAppIOForm: FormGroup;
  protected appIOBatchForm: FormGroup;
  protected avvisaturaMailForm: FormGroup;
  protected parserForm: FormGroup;
  protected protezioneForm: FormGroup;

  protected _serverAbilitato: FormControl = new FormControl(false);
  protected _sslConfigAbilitato: FormControl = new FormControl(false);
  protected _truststoreLocation: FormControl = new FormControl();
  protected _keystoreLocation: FormControl = new FormControl();
  protected _appIOBatchAbilitato: FormControl = new FormControl(false);
  protected _protezioneAbilitato: FormControl = new FormControl(false);

  constructor(protected us: UtilService, protected gps: GovpayService) {
    this.gdeForm = new FormGroup({
      pagoPARL_ctrl: new FormControl('mai'),
      payRL_ctrl: new FormControl('mai'),
      pendenzaRL_ctrl: new FormControl('mai'),
      ragioneriaRL_ctrl: new FormControl('mai'),
      backofficeRL_ctrl: new FormControl('mai'),
      enteRL_ctrl: new FormControl('mai'),
      apiBEIORL_ctrl: new FormControl('mai'),
      apiMJPPARL_ctrl: new FormControl('mai'),

      pagoPARS_ctrl: new FormControl('mai'),
      payRS_ctrl: new FormControl('mai'),
      pendenzaRS_ctrl: new FormControl('mai'),
      ragioneriaRS_ctrl: new FormControl('mai'),
      backofficeRS_ctrl: new FormControl('mai'),
      enteRS_ctrl: new FormControl('mai'),
      apiBEIORS_ctrl: new FormControl('mai'),
      apiMJPPARS_ctrl: new FormControl('mai'),

      pagoPADL_ctrl: new FormControl('mai'),
      payDL_ctrl: new FormControl('mai'),
      pendenzaDL_ctrl: new FormControl('mai'),
      ragioneriaDL_ctrl: new FormControl('mai'),
      backofficeDL_ctrl: new FormControl('mai'),
      enteDL_ctrl: new FormControl('mai'),
      apiBEIODL_ctrl: new FormControl('mai'),
      apiMJPPADL_ctrl: new FormControl('mai'),

      pagoPADS_ctrl: new FormControl('mai'),
      payDS_ctrl: new FormControl('mai'),
      pendenzaDS_ctrl: new FormControl('mai'),
      ragioneriaDS_ctrl: new FormControl('mai'),
      backofficeDS_ctrl: new FormControl('mai'),
      enteDS_ctrl: new FormControl('mai'),
      apiBEIODS_ctrl: new FormControl('mai'),
      apiMJPPADS_ctrl: new FormControl('mai')
    });
    this.serverForm = new FormGroup({
      serverAbilitato_ctrl: this._serverAbilitato,
      serverHost_ctrl: new FormControl(''),
      serverPorta_ctrl: new FormControl('25'),
      serverUsername_ctrl: new FormControl(''),
      serverPassword_ctrl: new FormControl(''),
      serverMittente_ctrl: new FormControl(''),
      serverCTimeout_ctrl: new FormControl('10000'),
      serverRTimeout_ctrl: new FormControl('120000'),
      sslConfig_ctrl: this._sslConfigAbilitato,
      startTls_ctrl: new FormControl(false),
      cryptoType_ctrl: new FormControl(''),
      hostnameVerifier_ctrl: new FormControl(false),
      tsType_ctrl: new FormControl(''),
      truststoreLocation_ctrl: this._truststoreLocation,
      truststorePassword_ctrl: new FormControl(''),
      truststoreAlgorithm_ctrl: new FormControl(''),
      ksType_ctrl: new FormControl(''),
      keystoreLocation_ctrl: this._keystoreLocation,
      keystorePassword_ctrl: new FormControl(''),
      keystoreAlgorithm_ctrl: new FormControl('')
    });
    this.avvisaturaAppIOForm = new FormGroup({
      promemoriaAvvisoIOTipo_ctrl: new FormControl('', Validators.required),
      promemoriaAvvisoIOOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaAvvisoIOMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaIOTipo_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaIOOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaIOMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaIOEseguiti_ctrl: new FormControl(false),
      promemoriaScadenzaIOTipo_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaIOOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaIOMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaIOPreavviso_ctrl: new FormControl('', Validators.required)
    });
    this.appIOBatchForm = new FormGroup({
      appIOBatchAbilitato_ctrl: this._appIOBatchAbilitato,
      appIOBatchUrl_ctrl: new FormControl(''),
      appIOBatchTTL_ctrl: new FormControl('')
    });
    this.avvisaturaMailForm = new FormGroup({
      promemoriaAvvisoMailTipo_ctrl: new FormControl('', Validators.required),
      promemoriaAvvisoMailOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaAvvisoMailMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaAvvisoMailStampa_ctrl: new FormControl(false),
      promemoriaRicevutaMailTipo_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaMailOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaMailMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaRicevutaMailEseguiti_ctrl: new FormControl(false),
      promemoriaRicevutaMailStampa_ctrl: new FormControl(false),
      promemoriaScadenzaMailTipo_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaMailOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaMailMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaScadenzaMailPreavviso_ctrl: new FormControl('', Validators.required)
    });
    this.parserForm = new FormGroup({
      parserTipo_ctrl: new FormControl('', Validators.required),
      parserCaricamento_ctrl: new FormControl('', Validators.required),
      parserEsito_ctrl: new FormControl('', Validators.required),
      parserLineaEsito_ctrl: new FormControl('')
    });
    this.protezioneForm = new FormGroup({
      protezioneAbilitato_ctrl: this._protezioneAbilitato,
      protezioneUrl_ctrl: new FormControl(''),
      protezioneKey_ctrl: new FormControl(''),
      protezioneSecret_ctrl: new FormControl(''),
      protezioneSoglia_ctrl: new FormControl(''),
      protezionePropResponse_ctrl: new FormControl('g-recaptcha-response'),
      protezioneRTimeout_ctrl: new FormControl('120000'),
      protezioneCTimeout_ctrl: new FormControl('10000'),
      protezioneVerifica_ctrl: new FormControl(false)
    });
  }

  ngOnInit() {
    this.getConfigurazioni();
  }

  ngAfterViewInit() {
    this._setConfigCollapsible();
  }

  ngAfterContentChecked() {
    if (this.tg) {
      this._tgFormIsValid = (this.tg.selectedIndex===0)?this.serverForm.valid:this.avvisaturaMailForm.valid;
    }
    if (this.tgIO) {
      this._tgIOFormIsValid = (this.tgIO.selectedIndex===0)?this.appIOBatchForm.valid:this.avvisaturaAppIOForm.valid;
    }
  }

  /**
   * Bug Angular mat-tab-group + Bootstrap Collapse
   * @private
   */
  _setConfigCollapsible() {
    const ids: string[] = [ '#gde', '#avvisaturaAppIO', '#avvisaturaMail' ];
    ids.forEach((id: string) => {
      const _jc = jQuery(id);
      _jc.one('shown.bs.collapse', function () {
        _jc.find('mat-ink-bar').css({ width: _jc.find('.mat-tab-label.mat-tab-label-active').outerWidth() });
      }.bind(this));
    });
  }

  getConfigurazioni() {
    let _url = UtilService.URL_IMPOSTAZIONI;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  mapJsonDetail() {
    if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce) {
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiPagoPA) {
        this.gdeForm.controls[ 'pagoPARL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagoPA.letture.log);
        this.gdeForm.controls[ 'pagoPADL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagoPA.letture.dump);
        this.gdeForm.controls[ 'pagoPARS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagoPA.scritture.log);
        this.gdeForm.controls[ 'pagoPADS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagoPA.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiPagamento) {
        this.gdeForm.controls[ 'payRL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagamento.letture.log);
        this.gdeForm.controls[ 'payDL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagamento.letture.dump);
        this.gdeForm.controls[ 'payRS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagamento.scritture.log);
        this.gdeForm.controls[ 'payDS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPagamento.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiBackoffice) {
        this.gdeForm.controls[ 'backofficeRL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackoffice.letture.log);
        this.gdeForm.controls[ 'backofficeDL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackoffice.letture.dump);
        this.gdeForm.controls[ 'backofficeRS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackoffice.scritture.log);
        this.gdeForm.controls[ 'backofficeDS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackoffice.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiEnte) {
        this.gdeForm.controls[ 'enteRL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiEnte.letture.log);
        this.gdeForm.controls[ 'enteDL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiEnte.letture.dump);
        this.gdeForm.controls[ 'enteRS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiEnte.scritture.log);
        this.gdeForm.controls[ 'enteDS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiEnte.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiPendenze) {
        this.gdeForm.controls[ 'pendenzaRL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPendenze.letture.log);
        this.gdeForm.controls[ 'pendenzaDL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPendenze.letture.dump);
        this.gdeForm.controls[ 'pendenzaRS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPendenze.scritture.log);
        this.gdeForm.controls[ 'pendenzaDS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiPendenze.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiRagioneria) {
        this.gdeForm.controls[ 'ragioneriaRL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiRagioneria.letture.log);
        this.gdeForm.controls[ 'ragioneriaDL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiRagioneria.letture.dump);
        this.gdeForm.controls[ 'ragioneriaRS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiRagioneria.scritture.log);
        this.gdeForm.controls[ 'ragioneriaDS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiRagioneria.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiBackendIO) {
        this.gdeForm.controls[ 'apiBEIORL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackendIO.letture.log);
        this.gdeForm.controls[ 'apiBEIODL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackendIO.letture.dump);
        this.gdeForm.controls[ 'apiBEIORS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackendIO.scritture.log);
        this.gdeForm.controls[ 'apiBEIODS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiBackendIO.scritture.dump);
      }
      if (this.json.giornaleEventi && this.json.giornaleEventi.interfacce.apiMaggioliJPPA) {
        this.gdeForm.controls[ 'apiMJPPARL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiMaggioliJPPA.letture.log);
        this.gdeForm.controls[ 'apiMJPPADL_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiMaggioliJPPA.letture.dump);
        this.gdeForm.controls[ 'apiMJPPARS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiMaggioliJPPA.scritture.log);
        this.gdeForm.controls[ 'apiMJPPADS_ctrl' ].setValue(this.json.giornaleEventi.interfacce.apiMaggioliJPPA.scritture.dump);
      }
    }

    if (this.json.mailBatch) {
      this.serverForm.controls['serverAbilitato_ctrl'].setValue(this.json.mailBatch.abilitato || false);
      if (this.json.mailBatch.mailserver) {
        this.serverForm.controls['serverHost_ctrl'].setValue(this.json.mailBatch.mailserver.host || '');
        this.serverForm.controls['serverPorta_ctrl'].setValue(this.json.mailBatch.mailserver.port || '');
        this.serverForm.controls['serverUsername_ctrl'].setValue(this.json.mailBatch.mailserver.username || '');
        this.serverForm.controls['serverPassword_ctrl'].setValue(this.json.mailBatch.mailserver.password || '');
        this.serverForm.controls['serverMittente_ctrl'].setValue(this.json.mailBatch.mailserver.from || '');
        this.serverForm.controls['serverCTimeout_ctrl'].setValue(this.json.mailBatch.mailserver.readTimeout || '');
        this.serverForm.controls['serverRTimeout_ctrl'].setValue(this.json.mailBatch.mailserver.connectionTimeout || '');
        this.serverForm.controls['startTls_ctrl'].setValue(this.json.mailBatch.mailserver.startTls || false);
        if (this.json.mailBatch.mailserver.sslConfig) {
          const _sslConfig: any = this.json.mailBatch.mailserver.sslConfig;
          this.serverForm.controls['sslConfig_ctrl'].setValue(_sslConfig.abilitato || false);
          this.serverForm.controls['cryptoType_ctrl'].setValue(_sslConfig.type || '');
          this.serverForm.controls['hostnameVerifier_ctrl'].setValue(_sslConfig.hostnameVerifier || false);
          if (_sslConfig.truststore) {
            this.serverForm.controls['tsType_ctrl'].setValue(_sslConfig.truststore.type || '');
            this.serverForm.controls['truststoreLocation_ctrl'].setValue(_sslConfig.truststore.location || '');
            this.serverForm.controls['truststorePassword_ctrl'].setValue(_sslConfig.truststore.password || '');
            this.serverForm.controls['truststoreAlgorithm_ctrl'].setValue(_sslConfig.truststore.managementAlgorithm || '');
          }
          if (_sslConfig.keystore) {
            this.serverForm.controls['ksType_ctrl'].setValue(_sslConfig.keystore.type || '');
            this.serverForm.controls['keystoreLocation_ctrl'].setValue(_sslConfig.keystore.location || '');
            this.serverForm.controls['keystorePassword_ctrl'].setValue(_sslConfig.keystore.password || '');
            this.serverForm.controls['keystoreAlgorithm_ctrl'].setValue(_sslConfig.keystore.managementAlgorithm || '');
          }
          this._ksTsValidators('hasTsValue', this._tsControllers);
          this._ksTsValidators('hasKsValue', this._ksControllers);
        }
      }
    }

    if (this.json.avvisaturaAppIO) {
      if (this.json.avvisaturaAppIO.promemoriaAvviso) {
        this.avvisaturaAppIOForm.controls['promemoriaAvvisoIOTipo_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaAvviso.tipo || '');
        this.avvisaturaAppIOForm.controls['promemoriaAvvisoIOOggetto_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaAvviso.oggetto || '');
        this.avvisaturaAppIOForm.controls['promemoriaAvvisoIOMessaggio_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaAvviso.messaggio || '');
      }
      if (this.json.avvisaturaAppIO.promemoriaRicevuta) {
        this.avvisaturaAppIOForm.controls['promemoriaRicevutaIOTipo_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaRicevuta.tipo || '');
        this.avvisaturaAppIOForm.controls['promemoriaRicevutaIOOggetto_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaRicevuta.oggetto || '');
        this.avvisaturaAppIOForm.controls['promemoriaRicevutaIOMessaggio_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaRicevuta.messaggio || '');
        this.avvisaturaAppIOForm.controls['promemoriaRicevutaIOEseguiti_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaRicevuta.soloEseguiti || false);
      }
      if (this.json.avvisaturaAppIO.promemoriaScadenza) {
        this.avvisaturaAppIOForm.controls['promemoriaScadenzaIOTipo_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaScadenza.tipo || '');
        this.avvisaturaAppIOForm.controls['promemoriaScadenzaIOOggetto_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaScadenza.oggetto || '');
        this.avvisaturaAppIOForm.controls['promemoriaScadenzaIOMessaggio_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaScadenza.messaggio || '');
        this.avvisaturaAppIOForm.controls['promemoriaScadenzaIOPreavviso_ctrl'].setValue(this.json.avvisaturaAppIO.promemoriaScadenza.preavviso || '');
      }
    }

    if (this.json.appIOBatch) {
      if (this.json.appIOBatch) {
        this.appIOBatchForm.controls['appIOBatchUrl_ctrl'].setValue(this.json.appIOBatch.url || '');
        this.appIOBatchForm.controls['appIOBatchTTL_ctrl'].setValue(this.json.appIOBatch.timeToLive || '');
        this.appIOBatchForm.controls['appIOBatchAbilitato_ctrl'].setValue(this.json.appIOBatch.abilitato || false);
      }
    }

    if (this.json.avvisaturaMail) {
      if (this.json.avvisaturaMail.promemoriaAvviso) {
        this.avvisaturaMailForm.controls['promemoriaAvvisoMailTipo_ctrl'].setValue(this.json.avvisaturaMail.promemoriaAvviso.tipo || '');
        this.avvisaturaMailForm.controls['promemoriaAvvisoMailOggetto_ctrl'].setValue(this.json.avvisaturaMail.promemoriaAvviso.oggetto || '');
        this.avvisaturaMailForm.controls['promemoriaAvvisoMailMessaggio_ctrl'].setValue(this.json.avvisaturaMail.promemoriaAvviso.messaggio || '');
        this.avvisaturaMailForm.controls['promemoriaAvvisoMailStampa_ctrl'].setValue(this.json.avvisaturaMail.promemoriaAvviso.allegaPdf || false);
      }
      if (this.json.avvisaturaMail.promemoriaRicevuta) {
        this.avvisaturaMailForm.controls['promemoriaRicevutaMailTipo_ctrl'].setValue(this.json.avvisaturaMail.promemoriaRicevuta.tipo || '');
        this.avvisaturaMailForm.controls['promemoriaRicevutaMailOggetto_ctrl'].setValue(this.json.avvisaturaMail.promemoriaRicevuta.oggetto || '');
        this.avvisaturaMailForm.controls['promemoriaRicevutaMailMessaggio_ctrl'].setValue(this.json.avvisaturaMail.promemoriaRicevuta.messaggio || '');
        this.avvisaturaMailForm.controls['promemoriaRicevutaMailEseguiti_ctrl'].setValue(this.json.avvisaturaMail.promemoriaRicevuta.soloEseguiti || false);
        this.avvisaturaMailForm.controls['promemoriaRicevutaMailStampa_ctrl'].setValue(this.json.avvisaturaMail.promemoriaRicevuta.allegaPdf || false);
      }
      if (this.json.avvisaturaMail.promemoriaScadenza) {
        this.avvisaturaMailForm.controls['promemoriaScadenzaMailTipo_ctrl'].setValue(this.json.avvisaturaMail.promemoriaScadenza.tipo || '');
        this.avvisaturaMailForm.controls['promemoriaScadenzaMailOggetto_ctrl'].setValue(this.json.avvisaturaMail.promemoriaScadenza.oggetto || '');
        this.avvisaturaMailForm.controls['promemoriaScadenzaMailMessaggio_ctrl'].setValue(this.json.avvisaturaMail.promemoriaScadenza.messaggio || '');
        this.avvisaturaMailForm.controls['promemoriaScadenzaMailPreavviso_ctrl'].setValue(this.json.avvisaturaMail.promemoriaScadenza.preavviso || '');
      }
    }

    if (this.json.tracciatoCsv) {
      this.parserForm.controls['parserTipo_ctrl'].setValue(this.json.tracciatoCsv.tipo || '');
      this.parserForm.controls['parserCaricamento_ctrl'].setValue(this.json.tracciatoCsv.richiesta || '');
      this.parserForm.controls['parserEsito_ctrl'].setValue(this.json.tracciatoCsv.risposta || '');
      this.parserForm.controls['parserLineaEsito_ctrl'].setValue(this.json.tracciatoCsv.intestazione || '');
    }

    if (this.json.hardening) {
      this.protezioneForm.controls['protezioneAbilitato_ctrl'].setValue(this.json.hardening.abilitato);
      if (this.json.hardening.captcha) {
        this.protezioneForm.controls['protezioneUrl_ctrl'].setValue(this.json.hardening.captcha.serverURL || '');
        this.protezioneForm.controls['protezioneKey_ctrl'].setValue(this.json.hardening.captcha.siteKey || '');
        this.protezioneForm.controls['protezioneSecret_ctrl'].setValue(this.json.hardening.captcha.secretKey || '');
        this.protezioneForm.controls['protezioneSoglia_ctrl'].setValue(this.json.hardening.captcha.soglia || '');
        this.protezioneForm.controls['protezionePropResponse_ctrl'].setValue(this.json.hardening.captcha.parametro || '');
        this.protezioneForm.controls['protezioneRTimeout_ctrl'].setValue(this.json.hardening.captcha.readTimeout || '');
        this.protezioneForm.controls['protezioneCTimeout_ctrl'].setValue(this.json.hardening.captcha.connectionTimeout || '');
        this.protezioneForm.controls['protezioneVerifica_ctrl'].setValue(this.json.hardening.captcha.denyOnFail || false);
      }
    }
  }

  protected _cryptoTypeChangeSelection(event: any) {

  }

  /**
   *
   * @param {any} event
   * @param {string} fgName
   * @param options
   * @private
   */
  protected _toggleValidators(event: any, fgName: string, options: any = { all: false, ctrls: [], disable: false }) {
    if (options.all) {
      options.ctrls = Object.keys(this[fgName].controls);
    }
    (options.ctrls || []).forEach((ctrl: string) => {
      const c: FormControl = this[fgName].controls[ctrl];
      c.setErrors(null);
      (event.checked)?c.setValidators(Validators.required):c.clearValidators();
      (!event.checked && options.disable)?c.disable():c.enable();
      c.updateValueAndValidity({ onlySelf: false, emitEvent: true });
    });
    if (fgName === 'serverForm') {
      this._ksTsValidators('hasTsValue', this._tsControllers);
      this._ksTsValidators('hasKsValue', this._ksControllers);
    }
  }

  protected _ksTsValidators(target: string, ctrls: string[] = []) {
    this[target] = false;
    const controls: any = this['serverForm'].controls;
    ctrls.some((ctrl: string) => {
      const c: FormControl = controls[ctrl];
      if ((c.value) && this._sslConfigAbilitato.value) {
        this[target] = true;
        return this[target];
      }
    });
    ctrls.forEach((ctrl: string) => {
      const c: FormControl = controls[ctrl];
      c.setErrors(null);
      c.clearValidators();
      if (this[target]) {
        c.setValidators(Validators.required)
      }
      c.updateValueAndValidity({ onlySelf: false, emitEvent: true });
    });
  }

  onSubmit(form: string, values: any) {
    let _bodyPatch: any = {};
    switch(form) {
      case 'serverForm':
        _bodyPatch = [];
        const _obj: any = {
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/mailBatch",
          value: {
            abilitato: values.serverAbilitato_ctrl,
            mailserver:  {
              host: values.serverHost_ctrl || '',
              port: values.serverPorta_ctrl || '',
              username: values.serverUsername_ctrl || '',
              password: values.serverPassword_ctrl || '',
              from: values.serverMittente_ctrl || '',
              readTimeout: values.serverCTimeout_ctrl || '',
              connectionTimeout: values.serverRTimeout_ctrl || '',
              startTls: values.startTls_ctrl
            }
          }
        };
        _obj.value.mailserver.sslConfig = {};
        _obj.value.mailserver.sslConfig.abilitato = values.sslConfig_ctrl;
        _obj.value.mailserver.sslConfig.type = values.cryptoType_ctrl || '';
        _obj.value.mailserver.sslConfig.hostnameVerifier = values.hostnameVerifier_ctrl;
        _obj.value.mailserver.sslConfig.truststore = {};
        _obj.value.mailserver.sslConfig.truststore.type = values.tsType_ctrl || '';
        _obj.value.mailserver.sslConfig.truststore.location = values.truststoreLocation_ctrl || '';
        _obj.value.mailserver.sslConfig.truststore.password = values.truststorePassword_ctrl || '';
        _obj.value.mailserver.sslConfig.truststore.managementAlgorithm = values.truststoreAlgorithm_ctrl || '';
        if (this.hasKsValue) {
          _obj.value.mailserver.sslConfig.keystore = {};
          _obj.value.mailserver.sslConfig.keystore.type = values.ksType_ctrl || '';
          _obj.value.mailserver.sslConfig.keystore.location = values.keystoreLocation_ctrl || '';
          _obj.value.mailserver.sslConfig.keystore.password = values.keystorePassword_ctrl || '';
          _obj.value.mailserver.sslConfig.keystore.managementAlgorithm = values.keystoreAlgorithm_ctrl || '';
        }
        _bodyPatch = [_obj];
        break;
      case 'avvisaturaAppIOForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/avvisaturaAppIO",
          value: {
            promemoriaAvviso: {
              tipo: values.promemoriaAvvisoIOTipo_ctrl,
              oggetto: values.promemoriaAvvisoIOOggetto_ctrl,
              messaggio: values.promemoriaAvvisoIOMessaggio_ctrl
            },
            promemoriaRicevuta: {
              tipo: values.promemoriaRicevutaIOTipo_ctrl,
              oggetto: values.promemoriaRicevutaIOOggetto_ctrl,
              messaggio: values.promemoriaRicevutaIOMessaggio_ctrl,
              soloEseguiti: values.promemoriaRicevutaIOEseguiti_ctrl
            },
            promemoriaScadenza: {
              tipo: values.promemoriaScadenzaIOTipo_ctrl,
              oggetto: values.promemoriaScadenzaIOOggetto_ctrl,
              messaggio: values.promemoriaScadenzaIOMessaggio_ctrl,
              preavviso: values.promemoriaScadenzaIOPreavviso_ctrl
            }
          }
        }];
        break;
      case 'appIOBatchForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/appIOBatch",
          value: {
            url: values.appIOBatchUrl_ctrl,
            timeToLive: values.appIOBatchTTL_ctrl,
            abilitato: values.appIOBatchAbilitato_ctrl
          }
        }];
        break;
      case 'avvisaturaMailForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/avvisaturaMail",
          value: {
            promemoriaAvviso: {
              tipo: values.promemoriaAvvisoMailTipo_ctrl,
              oggetto: values.promemoriaAvvisoMailOggetto_ctrl,
              messaggio: values.promemoriaAvvisoMailMessaggio_ctrl,
              allegaPdf: values.promemoriaAvvisoMailStampa_ctrl
            },
            promemoriaRicevuta: {
              tipo: values.promemoriaRicevutaMailTipo_ctrl,
              oggetto: values.promemoriaRicevutaMailOggetto_ctrl,
              messaggio: values.promemoriaRicevutaMailMessaggio_ctrl,
              soloEseguiti: values.promemoriaRicevutaMailEseguiti_ctrl,
              allegaPdf: values.promemoriaRicevutaMailStampa_ctrl
            },
            promemoriaScadenza: {
              tipo: values.promemoriaScadenzaMailTipo_ctrl,
              oggetto: values.promemoriaScadenzaMailOggetto_ctrl,
              messaggio: values.promemoriaScadenzaMailMessaggio_ctrl,
              preavviso: values.promemoriaScadenzaMailPreavviso_ctrl
            }
          }
        }];
        break;
      case 'parserForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/tracciatoCsv",
          value: {
            tipo: values.parserTipo_ctrl,
            intestazione: values.parserLineaEsito_ctrl,
            richiesta: values.parserCaricamento_ctrl,
            risposta: values.parserEsito_ctrl
          }
        }];
        break;
      case 'protezioneForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/hardening",
          value: {
            abilitato: values.protezioneAbilitato_ctrl,
            captcha: {
              serverURL: values.protezioneUrl_ctrl,
              siteKey: values.protezioneKey_ctrl,
              secretKey: values.protezioneSecret_ctrl,
              soglia: values.protezioneSoglia_ctrl,
              parametro: values.protezionePropResponse_ctrl,
              denyOnFail: values.protezioneVerifica_ctrl,
              connectionTimeout: values.protezioneCTimeout_ctrl,
              readTimeout: values.protezioneRTimeout_ctrl
            }
          }
        }];
        break;
      default:
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/giornaleEventi",
          value: {
            interfacce: {
              apiEnte: {
                letture: {
                  log: values.enteRL_ctrl,
                  dump: values.enteDL_ctrl
                },
                scritture: {
                  log: values.enteRS_ctrl,
                  dump: values.enteDS_ctrl
                }
              },
              apiPagamento: {
                letture: {
                  log: values.payRL_ctrl,
                  dump: values.payDL_ctrl
                },
                scritture: {
                  log: values.payRS_ctrl,
                  dump: values.payDS_ctrl
                }
              },
              apiRagioneria: {
                letture: {
                  log: values.ragioneriaRL_ctrl,
                  dump: values.ragioneriaDL_ctrl
                },
                scritture: {
                  log: values.ragioneriaRS_ctrl,
                  dump: values.ragioneriaDS_ctrl
                }
              },
              apiBackoffice: {
                letture: {
                  log: values.backofficeRL_ctrl,
                  dump: values.backofficeDL_ctrl
                },
                scritture: {
                  log: values.backofficeRS_ctrl,
                  dump: values.backofficeDS_ctrl
                }
              },
              apiPagoPA: {
                letture: {
                  log: values.pagoPARL_ctrl,
                  dump: values.pagoPADL_ctrl
                },
                scritture: {
                  log: values.pagoPARS_ctrl,
                  dump: values.pagoPADS_ctrl
                }
              },
              apiPendenze: {
                letture: {
                  log: values.pendenzaRL_ctrl,
                  dump: values.pendenzaDL_ctrl
                },
                scritture: {
                  log: values.pendenzaRS_ctrl,
                  dump: values.pendenzaDS_ctrl
                }
              },
              apiBackendIO: {
                letture: {
                  log: values.apiBEIORL_ctrl,
                  dump: values.apiBEIODL_ctrl
                },
                scritture: {
                  log: values.apiBEIORS_ctrl,
                  dump: values.apiBEIODS_ctrl
                }
              },
              apiMaggioliJPPA: {
                letture: {
                  log: values.apiMJPPARL_ctrl,
                  dump: values.apiMJPPADL_ctrl
                },
                scritture: {
                  log: values.apiMJPPARS_ctrl,
                  dump: values.apiMJPPADS_ctrl
                }
              }
            }
          }
        }];
    }
    this._patchConfig(_bodyPatch);
  }

  protected _patchConfig(body: any) {
    const _service = UtilService.URL_IMPOSTAZIONI;
    this.gps.saveData(_service, body, null, UtilService.METHODS.PATCH).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.us.alert('Operazione completata.');
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

}
