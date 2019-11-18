import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../services/util.service';
import { GovpayService } from '../../services/govpay.service';
import { MatTabGroup } from '@angular/material';

@Component({
  selector: 'link-impostazioni-view',
  templateUrl: './impostazioni-view.component.html',
  styleUrls: ['./impostazioni-view.component.scss']
})
export class ImpostazioniViewComponent implements OnInit, AfterViewInit {
  @ViewChild('tabs') tabGroup: MatTabGroup;

  protected json: any;

  protected gdeForm: FormGroup;
  protected serverForm: FormGroup;
  protected promemoriaForm: FormGroup;
  protected ricevutaForm: FormGroup;
  protected parserForm: FormGroup;
  protected protezioneForm: FormGroup;

  protected _serverAbilitato: FormControl = new FormControl(false);
  protected _protezioneAbilitato: FormControl = new FormControl(false);

  constructor(protected us: UtilService, protected gps: GovpayService) {
    this.gdeForm = new FormGroup({
      pagoPARL_ctrl: new FormControl('mai'),
      payRL_ctrl: new FormControl('mai'),
      pendenzaRL_ctrl: new FormControl('mai'),
      ragioneriaRL_ctrl: new FormControl('mai'),
      backofficeRL_ctrl: new FormControl('mai'),
      enteRL_ctrl: new FormControl('mai'),

      pagoPARS_ctrl: new FormControl('mai'),
      payRS_ctrl: new FormControl('mai'),
      pendenzaRS_ctrl: new FormControl('mai'),
      ragioneriaRS_ctrl: new FormControl('mai'),
      backofficeRS_ctrl: new FormControl('mai'),
      enteRS_ctrl: new FormControl('mai'),

      pagoPADL_ctrl: new FormControl('mai'),
      payDL_ctrl: new FormControl('mai'),
      pendenzaDL_ctrl: new FormControl('mai'),
      ragioneriaDL_ctrl: new FormControl('mai'),
      backofficeDL_ctrl: new FormControl('mai'),
      enteDL_ctrl: new FormControl('mai'),

      pagoPADS_ctrl: new FormControl('mai'),
      payDS_ctrl: new FormControl('mai'),
      pendenzaDS_ctrl: new FormControl('mai'),
      ragioneriaDS_ctrl: new FormControl('mai'),
      backofficeDS_ctrl: new FormControl('mai'),
      enteDS_ctrl: new FormControl('mai')
    });
    this.serverForm = new FormGroup({
      serverAbilitato_ctrl: this._serverAbilitato,
      serverHost_ctrl: new FormControl(''),
      serverPorta_ctrl: new FormControl('25'),
      serverUsername_ctrl: new FormControl(''),
      serverPassword_ctrl: new FormControl(''),
      serverMittente_ctrl: new FormControl(''),
      serverCTimeout_ctrl: new FormControl('10000'),
      serverRTimeout_ctrl: new FormControl('120000')
    });
    this.promemoriaForm = new FormGroup({
      promemoriaTipo_ctrl: new FormControl('', Validators.required),
      promemoriaOggetto_ctrl: new FormControl('', Validators.required),
      promemoriaMessaggio_ctrl: new FormControl('', Validators.required),
      promemoriaAvviso_ctrl: new FormControl(false)
    });
    this.ricevutaForm = new FormGroup({
      ricevutaTipo_ctrl: new FormControl('', Validators.required),
      ricevutaOggetto_ctrl: new FormControl('', Validators.required),
      ricevutaMessaggio_ctrl: new FormControl('', Validators.required),
      ricevutaStampa_ctrl: new FormControl(false)
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
    setTimeout(() => {
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
    }

    if (this.json.mailBatch) {
      this.serverForm.controls['serverAbilitato_ctrl'].setValue(this.json.mailBatch.abilitato);
      if (this.json.mailBatch.mailserver) {
        this.serverForm.controls['serverHost_ctrl'].setValue(this.json.mailBatch.mailserver.host || '');
        this.serverForm.controls['serverPorta_ctrl'].setValue(this.json.mailBatch.mailserver.port || '');
        this.serverForm.controls['serverUsername_ctrl'].setValue(this.json.mailBatch.mailserver.username || '');
        this.serverForm.controls['serverPassword_ctrl'].setValue(this.json.mailBatch.mailserver.password || '');
        this.serverForm.controls['serverMittente_ctrl'].setValue(this.json.mailBatch.mailserver.from || '');
        this.serverForm.controls['serverCTimeout_ctrl'].setValue(this.json.mailBatch.mailserver.readTimeout || '');
        this.serverForm.controls['serverRTimeout_ctrl'].setValue(this.json.mailBatch.mailserver.connectionTimeout || '');
      }
    }

    if (this.json.mailPromemoria) {
      this.promemoriaForm.controls['promemoriaTipo_ctrl'].setValue(this.json.mailPromemoria.tipo || '');
      this.promemoriaForm.controls['promemoriaOggetto_ctrl'].setValue(this.json.mailPromemoria.oggetto || '');
      this.promemoriaForm.controls['promemoriaMessaggio_ctrl'].setValue(this.json.mailPromemoria.messaggio || '');
      this.promemoriaForm.controls['promemoriaAvviso_ctrl'].setValue(this.json.mailPromemoria.allegaPdf || false);
    }

    if (this.json.mailRicevuta) {
      this.ricevutaForm.controls['ricevutaTipo_ctrl'].setValue(this.json.mailRicevuta.tipo || '');
      this.ricevutaForm.controls['ricevutaOggetto_ctrl'].setValue(this.json.mailRicevuta.oggetto || '');
      this.ricevutaForm.controls['ricevutaMessaggio_ctrl'].setValue(this.json.mailRicevuta.messaggio || '');
      this.ricevutaForm.controls['ricevutaStampa_ctrl'].setValue(this.json.mailRicevuta.allegaPdf || false);
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

  onSubmit(form: string, values: any) {
    let _bodyPatch: any = {};
    switch(form) {
      case 'serverForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/mailBatch",
          value: {
            abilitato: values.serverAbilitato_ctrl,
            mailserver:  {
              host: values.serverHost_ctrl,
              port: values.serverPorta_ctrl,
              username: values.serverUsername_ctrl,
              password: values.serverPassword_ctrl,
              from: values.serverMittente_ctrl,
              readTimeout: values.serverCTimeout_ctrl,
              connectionTimeout: values.serverRTimeout_ctrl
            }
          }
        }];
        break;
      case 'promemoriaForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/mailPromemoria",
          value: {
            tipo: values.promemoriaTipo_ctrl,
            oggetto: values.promemoriaOggetto_ctrl,
            messaggio: values.promemoriaMessaggio_ctrl,
            allegaPdf: values.promemoriaAvviso_ctrl
          }
        }];
        break;
      case 'ricevutaForm':
        _bodyPatch = [{
          op: UtilService.PATCH_METHODS.REPLACE,
          path: "/mailRicevuta",
          value: {
            tipo: values.ricevutaTipo_ctrl,
            oggetto: values.ricevutaOggetto_ctrl,
            messaggio: values.ricevutaMessaggio_ctrl,
            allegaPdf: values.ricevutaStampa_ctrl
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
