import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';

@Component({
  selector: 'link-tipo-pendenza-view',
  templateUrl: './tipo-pendenza-view.component.html',
  styleUrls: ['./tipo-pendenza-view.component.scss']
})
export class TipoPendenzaViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _voce = Voce;

  protected _generatori: any[] = UtilService.GENERATORI;
  protected _applicazioni: any[] = [];

  constructor(protected us: UtilService, protected gps: GovpayService) { }

  ngOnInit() {
    this._elencoApplicazioni();
    this.fGroup.addControl('idTipoPendenza_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('descrizione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitato_ctrl', new FormControl(false));
    this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(false));
    // PERSONALIZZAZIONE DETTAGLIO PENDENZA
    this.fGroup.addControl('visualizzazione_ctrl', new FormControl(''));
    // INSERIMENTO PENDENZE DA OPERATORE
    this.fGroup.addControl('generatore_ctrl', new FormControl(''));
    this.fGroup.addControl('definizione_ctrl', new FormControl(''));
    this.fGroup.addControl('impaginazione_ctrl', new FormControl(''));
    this.fGroup.addControl('validazione_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazioneIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('definizioneIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltroIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoIPO_ctrl', new FormControl(false));
    // PAGAMENTO SPONTANEO
    this.fGroup.addControl('generatorePS_ctrl', new FormControl(''));
    this.fGroup.addControl('definizionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('validazionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('definizionePS1_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltroPS_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPS_ctrl', new FormControl(false));
    // COMUNICAZINI VIA EMAIL
    this.fGroup.addControl('tipoTemplateNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNAP_ctrl', new FormControl(true));
    this.fGroup.addControl('allegaPdfNAP_ctrl', new FormControl(false));

    this.fGroup.addControl('giorniPreavvisoPSP_ctrl', new FormControl('10'));
    this.fGroup.addControl('tipoTemplatePSP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoPSP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioPSP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPSP_ctrl', new FormControl(false));

    this.fGroup.addControl('tipoTemplateNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNRP_ctrl', new FormControl(false));
    this.fGroup.addControl('allegaPdfNRP_ctrl', new FormControl(false));
    this.fGroup.addControl('soloPagamentiNRP_ctrl', new FormControl(false));
    //APP IO
    this.fGroup.addControl('tipoTemplateNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNAP_IO_ctrl', new FormControl(false));

    this.fGroup.addControl('giorniPreavvisoPSP_IO_ctrl', new FormControl('10'));
    this.fGroup.addControl('tipoTemplatePSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoPSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioPSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPSP_IO_ctrl', new FormControl(false));

    this.fGroup.addControl('tipoTemplateNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNRP_IO_ctrl', new FormControl(false));
    this.fGroup.addControl('soloPagamentiNRP_IO_ctrl', new FormControl(false));
    // ALTRE FUNZIONI
    this.fGroup.addControl('tipoTemplateAF_ctrl', new FormControl(''));
    this.fGroup.addControl('richiesta_ctrl', new FormControl(''));
    this.fGroup.addControl('risposta_ctrl', new FormControl(''));
    this.fGroup.addControl('lineaEsito_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idTipoPendenza_ctrl'].disable();
        this.fGroup.controls['idTipoPendenza_ctrl'].setValue((this.json.idTipoPendenza)?this.json.idTipoPendenza:'');
        this.fGroup.controls['descrizione_ctrl'].setValue((this.json.descrizione)?this.json.descrizione:'');
        this.fGroup.controls['codificaIUV_ctrl'].setValue((this.json.codificaIUV)?this.json.codificaIUV:'');
        this.fGroup.controls['abilitato_ctrl'].setValue(this.json.abilitato);
        this.fGroup.controls['pagaTerzi_ctrl'].setValue(this.json.pagaTerzi);
        // PERSONALIZZAZIONE DETTAGLIO PENDENZA
        this.fGroup.controls['visualizzazione_ctrl'].setValue((this.json.visualizzazione)?(this.json.visualizzazione || ''):'');
        // INSERIMENTO PENDENZE DA OPERATORE
        if (this.json.portaleBackoffice) {
          const _pb: any = this.json.portaleBackoffice;
          this.fGroup.controls['generatore_ctrl'].setValue((_pb.form)?(_pb.form.tipo || ''):'');
          this.fGroup.controls['definizione_ctrl'].setValue((_pb.form)?(_pb.form.definizione || ''):'');
          this.fGroup.controls['validazione_ctrl'].setValue(_pb.validazione || '');
          this.fGroup.controls['tipoTrasformazioneIPO_ctrl'].setValue((_pb.trasformazione)?(_pb.trasformazione.tipo || ''):'');
          this.fGroup.controls['definizioneIPO_ctrl'].setValue((_pb.trasformazione)?(_pb.trasformazione.definizione || ''):'');
          this.fGroup.controls['inoltroIPO_ctrl'].setValue(_pb.inoltro || '');
          this.fGroup.controls['abilitatoIPO_ctrl'].setValue(_pb.abilitato);
        }
        // PAGAMENTO SPONTANEO
        if (this.json.portalePagamento) {
          const _pp: any = this.json.portalePagamento;
          this.fGroup.controls['generatorePS_ctrl'].setValue((_pp.form)?(_pp.form.tipo || ''):'');
          this.fGroup.controls['definizionePS_ctrl'].setValue((_pp.form)?(_pp.form.definizione || ''):'');
          this.fGroup.controls['impaginazione_ctrl'].setValue((_pp.form)?(_pp.form.impaginazione || ''):'');
          this.fGroup.controls['validazionePS_ctrl'].setValue(_pp.validazione || '');
          this.fGroup.controls['tipoTrasformazionePS_ctrl'].setValue((_pp.trasformazione)?(_pp.trasformazione.tipo || ''):'');
          this.fGroup.controls['definizionePS1_ctrl'].setValue((_pp.trasformazione)?(_pp.trasformazione.definizione || ''):'');
          this.fGroup.controls['inoltroPS_ctrl'].setValue(_pp.inoltro || '');
          this.fGroup.controls['abilitatoPS_ctrl'].setValue(_pp.abilitato);
        }
        // COMUNICAZINI VIA EMAIL
        if (this.json.avvisaturaMail) {
          if (this.json.avvisaturaMail.promemoriaAvviso) {
            const _pa: any = this.json.avvisaturaMail.promemoriaAvviso;
            this.fGroup.controls['abilitatoNAP_ctrl'].setValue(_pa.abilitato);
            this.fGroup.controls['allegaPdfNAP_ctrl'].setValue(_pa.allegaPdf);
            this.fGroup.controls['tipoTemplateNAP_ctrl'].setValue(_pa.tipo || '');
            if (_pa.oggetto) {
              this.fGroup.controls['oggettoNAP_ctrl'].setValue(_pa.oggetto || '');
              this.fGroup.controls['messaggioNAP_ctrl'].setValue(_pa.messaggio || '');
            }
          }
          if (this.json.avvisaturaMail.promemoriaScadenza) {
            const _ps: any = this.json.avvisaturaMail.promemoriaScadenza;
            this.fGroup.controls['abilitatoPSP_ctrl'].setValue(_ps.abilitato);
            this.fGroup.controls['giorniPreavvisoPSP_ctrl'].setValue(_ps.preavviso || '10');
            this.fGroup.controls['tipoTemplatePSP_ctrl'].setValue(_ps.tipo || '');
            if (_ps.oggetto) {
              this.fGroup.controls['oggettoPSP_ctrl'].setValue(_ps.oggetto || '');
              this.fGroup.controls['messaggioPSP_ctrl'].setValue(_ps.messaggio || '');
            }
          }
          if (this.json.avvisaturaMail.promemoriaRicevuta) {
            const _pr: any = this.json.avvisaturaMail.promemoriaRicevuta;
            this.fGroup.controls['abilitatoNRP_ctrl'].setValue(_pr.abilitato);
            this.fGroup.controls['soloPagamentiNRP_ctrl'].setValue(_pr.soloEseguiti);
            this.fGroup.controls['allegaPdfNRP_ctrl'].setValue(_pr.allegaPdf);
            this.fGroup.controls['tipoTemplateNRP_ctrl'].setValue(_pr.tipo || '');
            if (_pr.oggetto) {
              this.fGroup.controls['oggettoNRP_ctrl'].setValue(_pr.oggetto || '');
              this.fGroup.controls['messaggioNRP_ctrl'].setValue(_pr.messaggio || '');
            }
          }
        }
        // APP IO
        if (this.json.avvisaturaAppIO) {
          if (this.json.avvisaturaAppIO.promemoriaAvviso) {
            const _pa: any = this.json.avvisaturaAppIO.promemoriaAvviso;
            this.fGroup.controls['abilitatoNAP_IO_ctrl'].setValue(_pa.abilitato);
            this.fGroup.controls['tipoTemplateNAP_IO_ctrl'].setValue(_pa.tipo || '');
            if (_pa.oggetto) {
              this.fGroup.controls['oggettoNAP_IO_ctrl'].setValue(_pa.oggetto || '');
              this.fGroup.controls['messaggioNAP_IO_ctrl'].setValue(_pa.messaggio || '');
            }
          }
          if (this.json.avvisaturaAppIO.promemoriaScadenza) {
            const _ps: any = this.json.avvisaturaAppIO.promemoriaScadenza;
            this.fGroup.controls['abilitatoPSP_IO_ctrl'].setValue(_ps.abilitato);
            this.fGroup.controls['giorniPreavvisoPSP_IO_ctrl'].setValue(_ps.preavviso || '10');
            this.fGroup.controls['tipoTemplatePSP_IO_ctrl'].setValue(_ps.tipo || '');
            if (_ps.oggetto) {
              this.fGroup.controls['oggettoPSP_IO_ctrl'].setValue(_ps.oggetto || '');
              this.fGroup.controls['messaggioPSP_IO_ctrl'].setValue(_ps.messaggio || '');
            }
          }
          if (this.json.avvisaturaAppIO.promemoriaRicevuta) {
            const _pr: any = this.json.avvisaturaAppIO.promemoriaRicevuta;
            this.fGroup.controls['abilitatoNRP_IO_ctrl'].setValue(_pr.abilitato);
            this.fGroup.controls['soloPagamentiNRP_IO_ctrl'].setValue(_pr.soloEseguiti);
            this.fGroup.controls['tipoTemplateNRP_IO_ctrl'].setValue(_pr.tipo || '');
            if (_pr.oggetto) {
              this.fGroup.controls['oggettoNRP_IO_ctrl'].setValue(_pr.oggetto || '');
              this.fGroup.controls['messaggioNRP_IO_ctrl'].setValue(_pr.messaggio || '');
            }
          }
        }
        // ALTRE FUNZIONI
        if (this.json.tracciatoCsv) {
          this.fGroup.controls['tipoTemplateAF_ctrl'].setValue(this.json.tracciatoCsv.tipo || '');
          this.fGroup.controls['richiesta_ctrl'].setValue(this.json.tracciatoCsv.richiesta || '');
          this.fGroup.controls['risposta_ctrl'].setValue(this.json.tracciatoCsv.risposta || '');
          this.fGroup.controls['lineaEsito_ctrl'].setValue(this.json.tracciatoCsv.intestazione || '');
        }
      }
    });
  }

  _elencoApplicazioni() {
    this.gps.getDataService(UtilService.URL_APPLICAZIONI).subscribe(
      (response) => {
        this._applicazioni = response.body?(response.body.risultati || []):[];
        this.gps.updateSpinner(false);
      },
      (error) => {
        this._applicazioni = [];
        this.gps.updateSpinner(false);
        this.us.onError(error);
      }
    );
  }

  // protected _compoaringFct(item: any, selection: any): boolean {
  //   return (item && selection && item.principal === selection.principal);
  // }

  // protected _selectChange(event: any, controller: string, controller2?: string[]) {
  //   if(!event.value) {
  //     this.fGroup.controls[controller].setValue('');
  //     this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
  //     controller2.forEach((ctrl: string) => {
  //       this.fGroup.controls[ctrl].disable();
  //     });
  //   } else {
  //     controller2.forEach((ctrl: string) => {
  //       this.fGroup.controls[ctrl].enable();
  //     });
  //   }
  //   controller2.forEach((ctrl: string) => {
  //     this.fGroup.controls[ctrl].updateValueAndValidity({ onlySelf: true });
  //   });
  // }

  protected _lfsChange(event: any, controller: string) {
    if(event.type == 'file-selector-change') {
      if(event.value) {
        this.fGroup.controls[controller].setValidators([ Validators.required ]);
      } else {
        if (this.fGroup.controls[controller].value && !event.controller.value) {
          this.fGroup.controls[controller].setValidators([ Validators.required ]);
          event.controller.setValidators([ Validators.required ]);
        } else {
          this.fGroup.controls[controller].clearValidators();
          event.controller.clearValidators();
        }
        event.controller.updateValueAndValidity({ onlySelf: true, emitEvent: false });
      }
      this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.idTipoPendenza = (!this.fGroup.controls['idTipoPendenza_ctrl'].disabled)?_info['idTipoPendenza_ctrl']:this.json.idTipoPendenza;
    _json.descrizione = (_info['descrizione_ctrl'])?_info['descrizione_ctrl']:null;
    _json.codificaIUV = (_info['codificaIUV_ctrl'])?_info['codificaIUV_ctrl']:null;
    _json.abilitato = _info['abilitato_ctrl'] || false;
    _json.pagaTerzi = _info['pagaTerzi_ctrl'] || false;
    // PERSONALIZZAZIONE DETTAGLIO PENDENZA
    _json.visualizzazione = _info['visualizzazione_ctrl'] || null;
    // INSERIMENTO PENDENZE DA OPERATORE
    _json.portaleBackoffice = {
      form: {
        tipo: _info['generatore_ctrl'] || null,
        definizione: _info['definizione_ctrl'] || null
      },
      validazione: _info['validazione_ctrl'] || null,
      trasformazione: {
        tipo: _info['tipoTrasformazioneIPO_ctrl'] || null,
        definizione: _info['definizioneIPO_ctrl'] || null,
      },
      inoltro: _info['inoltroIPO_ctrl'] || null,
      abilitato: _info['abilitatoIPO_ctrl'] || false
    };
    // PAGAMENTO SPONTANEO
    _json.portalePagamento = {
      form: {
        tipo: _info['generatorePS_ctrl'] || null,
        definizione: _info['definizionePS_ctrl'] || null,
        impaginazione: _info['impaginazione_ctrl'] || null
      },
      validazione: _info['validazionePS_ctrl'] || null,
      trasformazione: {
        tipo: _info['tipoTrasformazionePS_ctrl'] || null,
        definizione: _info['definizionePS1_ctrl'] || null,
      },
      inoltro: _info['inoltroPS_ctrl'] || null,
      abilitato: _info['abilitatoPS_ctrl'] || false
    };
    // COMUNICAZINI VIA EMAIL
    _json.avvisaturaMail = {
      promemoriaAvviso: null,
      promemoriaScadenza: null,
      promemoriaRicevuta: null
    };
    const _promemoriaAvviso = {};
    _promemoriaAvviso['tipo'] = _info['tipoTemplateNAP_ctrl'] || null;
    _promemoriaAvviso['oggetto'] = _info['oggettoNAP_ctrl'] || null;
    _promemoriaAvviso['messaggio'] = _info['messaggioNAP_ctrl'] || null;
    _promemoriaAvviso['abilitato'] = _info['abilitatoNAP_ctrl'] || false;
    _promemoriaAvviso['allegaPdf'] = _info['allegaPdfNAP_ctrl'] || false;
    _json.avvisaturaMail.promemoriaAvviso = _promemoriaAvviso;

    const _promemoriaScadenza = {};
    _promemoriaScadenza['tipo'] = _info['tipoTemplatePSP_ctrl'] || null;
    _promemoriaScadenza['oggetto'] = _info['oggettoPSP_ctrl'] || null;
    _promemoriaScadenza['messaggio'] = _info['messaggioPSP_ctrl'] || null;
    _promemoriaScadenza['preavviso'] = _info['giorniPreavvisoPSP_ctrl'] || 10;
    _promemoriaScadenza['abilitato'] = _info['abilitatoPSP_ctrl'] || false;
    _json.avvisaturaMail.promemoriaScadenza = _promemoriaScadenza;

    const _promemoriaRicevuta = {};
    _promemoriaRicevuta['tipo'] = _info['tipoTemplateNRP_ctrl'] || null;
    _promemoriaRicevuta['oggetto'] = _info['oggettoNRP_ctrl'] || null;
    _promemoriaRicevuta['messaggio'] = _info['messaggioNRP_ctrl'] || null;
    _promemoriaRicevuta['abilitato'] = _info['abilitatoNRP_ctrl'] || false;
    _promemoriaRicevuta['allegaPdf'] = _info['allegaPdfNRP_ctrl'] || false;
    _promemoriaRicevuta['soloEseguiti'] = _info['soloPagamentiNRP_ctrl'] || false;
    _json.avvisaturaMail.promemoriaRicevuta = _promemoriaRicevuta;

    // APP IO
    _json.avvisaturaAppIO = {
      promemoriaAvviso: null,
      promemoriaScadenza: null,
      promemoriaRicevuta: null
    };

    const _promemoriaAvvisoIO = {};
    _promemoriaAvvisoIO['tipo'] = _info['tipoTemplateNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['oggetto'] = _info['oggettoNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['messaggio'] = _info['messaggioNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['abilitato'] = _info['abilitatoNAP_IO_ctrl'] || false;
    _json.avvisaturaAppIO.promemoriaAvviso = _promemoriaAvvisoIO;

    const _promemoriaScadenzaIO = {};
    _promemoriaScadenzaIO['tipo'] = _info['tipoTemplatePSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['oggetto'] = _info['oggettoPSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['messaggio'] = _info['messaggioPSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['preavviso'] = _info['giorniPreavvisoPSP_IO_ctrl'] || 10;
    _promemoriaScadenzaIO['abilitato'] = _info['abilitatoPSP_IO_ctrl'] || false;
    _json.avvisaturaAppIO.promemoriaScadenza = _promemoriaScadenzaIO;

    const _promemoriaRicevutaIO = {};
    _promemoriaRicevutaIO['tipo'] = _info['tipoTemplateNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['oggetto'] = _info['oggettoNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['messaggio'] = _info['messaggioNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['abilitato'] = _info['abilitatoNRP_IO_ctrl'] || false;
    _promemoriaRicevutaIO['soloEseguiti'] = _info['soloPagamentiNRP_IO_ctrl'] || false;
    _json.avvisaturaAppIO.promemoriaRicevuta = _promemoriaRicevutaIO;

    // ALTRE FUNZIONI
    _json.tracciatoCsv = {
      tipo: _info['tipoTemplateAF_ctrl'] || null,
      intestazione: _info['lineaEsito_ctrl'] || null,
      richiesta: _info['richiesta_ctrl'] || null,
      risposta: _info['risposta_ctrl'] || null
    };

    return _json;
  }

}
