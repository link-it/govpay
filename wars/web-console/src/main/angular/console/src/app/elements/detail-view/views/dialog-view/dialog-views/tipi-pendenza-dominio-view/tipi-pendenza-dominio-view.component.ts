import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AsyncFilterableSelectComponent } from '../../../../../async-filterable-select/async-filterable-select.component';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { GovpayService } from '../../../../../../services/govpay.service';
import { Parameters } from '../../../../../../classes/parameters';
import { Standard } from '../../../../../../classes/view/standard';
import { Dato } from '../../../../../../classes/view/dato';

@Component({
  selector: 'link-tipi-pendenza-dominio-view',
  templateUrl: './tipi-pendenza-dominio-view.component.html',
  styleUrls: ['./tipi-pendenza-dominio-view.component.scss']
})
export class TipiPendenzaDominioViewComponent implements IFormComponent,  OnInit, AfterViewInit {
  @ViewChild('asyncTipiPendenza', { read: AsyncFilterableSelectComponent }) _asyncTipiPendenza: AsyncFilterableSelectComponent;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected tipiPendenza_items: any[] = [];

  protected _voce = Voce;

  protected _apiKeyRequired: boolean = false;
  protected _generatori: any[] = UtilService.GENERATORI;
  protected _applicazioni: any[] = [];
  protected _preset: any = {
      shadow_visualizzazione: null,
      shadow_validazione: null,
      shadow_definizione: null,
      shadow_definizioneIPO: null,
      shadow_definizionePS: null,
      shadow_impaginazione: null,
      shadow_validazionePS: null,
      shadow_definizionePS1: null,
      shadow_oggettoNAP: null,
      shadow_messaggioNAP: null,
      shadow_oggettoPSP: null,
      shadow_messaggioPSP: null,
      shadow_oggettoNRP: null,
      shadow_messaggioNRP: null,
      shadow_oggettoNAP_IO: null,
      shadow_messaggioNAP_IO: null,
      shadow_oggettoPSP_IO: null,
      shadow_messaggioPSP_IO: null,
      shadow_oggettoNRP_IO: null,
      shadow_messaggioNRP_IO: null,
      shadow_richiesta: null,
      shadow_risposta: null
  };

  // Async filterable select
  protected _searching: boolean = false;
  protected _inputDisplay = (value: any) => {
    return value?value.descrizione:'';
  };
  protected _mapRisultati = () => {
    if(this.tipiPendenza_items && this.tipiPendenza_items.length > 1) {
      return this.tipiPendenza_items.length + ' risultati';
    }

    return '';
  };

  constructor(public gps: GovpayService, public us: UtilService) {
    this._elencoApplicazioni();
  }

  ngOnInit() {
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl('', [ Validators.required, this.requireMatch.bind(this) ]));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(null));
    this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(null));
    // PERSONALIZZAZIONE DETTAGLIO PENDENZA
    this.fGroup.addControl('visualizzazione_ctrl', new FormControl(''));
    // INSERIMENTO PENDENZE DA OPERATORE
    this.fGroup.addControl('generatore_ctrl', new FormControl(''));
    this.fGroup.addControl('definizione_ctrl', new FormControl(''));
    this.fGroup.addControl('validazione_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazioneIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('definizioneIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltroIPO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoIPO_ctrl', new FormControl(null));
    // PAGAMENTO SPONTANEO
    this.fGroup.addControl('generatorePS_ctrl', new FormControl(''));
    this.fGroup.addControl('definizionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('impaginazione_ctrl', new FormControl(''));
    this.fGroup.addControl('validazionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazionePS_ctrl', new FormControl(''));
    this.fGroup.addControl('definizionePS1_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltroPS_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPS_ctrl', new FormControl(null));
    // COMUNICAZINI VIA EMAIL
    this.fGroup.addControl('tipoTemplateNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNAP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNAP_ctrl', new FormControl(null));
    this.fGroup.addControl('allegaPdfNAP_ctrl', new FormControl(null));

    this.fGroup.addControl('giorniPreavvisoPSP_ctrl', new FormControl('10'));
    this.fGroup.addControl('tipoTemplatePSP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoPSP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioPSP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPSP_ctrl', new FormControl(null));

    this.fGroup.addControl('tipoTemplateNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNRP_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNRP_ctrl', new FormControl(null));
    this.fGroup.addControl('allegaPdfNRP_ctrl', new FormControl(null));
    this.fGroup.addControl('soloPagamentiNRP_ctrl', new FormControl(null));
    //APP IO
    this.fGroup.addControl('apiKeyNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTemplateNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNAP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNAP_IO_ctrl', new FormControl(null));

    this.fGroup.addControl('giorniPreavvisoPSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTemplatePSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoPSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioPSP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoPSP_IO_ctrl', new FormControl(null));

    this.fGroup.addControl('tipoTemplateNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioNRP_IO_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitatoNRP_IO_ctrl', new FormControl(null));
    this.fGroup.addControl('soloPagamentiNRP_IO_ctrl', new FormControl(null));
    // ALTRE FUNZIONI
    this.fGroup.addControl('tipoTemplateAF_ctrl', new FormControl(''));
    this.fGroup.addControl('richiesta_ctrl', new FormControl(''));
    this.fGroup.addControl('risposta_ctrl', new FormControl(''));
    this.fGroup.addControl('lineaEsito_ctrl', new FormControl(''));

    this.__apiRequired();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        const _json = JSON.parse(JSON.stringify(this.json));
        this.fGroup.controls['tipoPendenza_ctrl'].disable();
        this.fGroup.controls['tipoPendenza_ctrl'].setValidators([ Validators.required ]);
        this.fGroup.controls['tipoPendenza_ctrl'].setValue(_json);
        this.fGroup.controls['codificaIUV_ctrl'].setValue(_json.codificaIUV || '');
        this.fGroup.controls['abilita_ctrl'].setValue(_json.abilitato);
        this.fGroup.controls['pagaTerzi_ctrl'].setValue(_json.pagaTerzi);
        this._updateValues(_json);
      } else {
        this._getTipiPendenza('');
      }
    });
  }

  protected __apiRequired() {
    this.fGroup.valueChanges.subscribe(
      (values: any) => {
        this._apiKeyRequired = !!(values.abilitatoNAP_IO_ctrl || values.abilitatoPSP_IO_ctrl || values.abilitatoNRP_IO_ctrl);
        !this._apiKeyRequired?this.fGroup.controls['apiKeyNAP_IO_ctrl'].clearValidators():this.fGroup.controls['apiKeyNAP_IO_ctrl'].setValidators([Validators.required]);
      });
  }

  protected requireMatch(control: FormControl): ValidationErrors | null {
    const selection: any = control.value;
    const _filtered = this.tipiPendenza_items.filter((item: any) => {
      return selection && (
        (selection.jsonP && selection.jsonP.descrizione === item.jsonP.descrizione) ||
        (selection.descrizione && selection.descrizione === item.jsonP.descrizione) ||
        (selection === item.jsonP.descrizione)
      );
    });
    if (_filtered.length === 0) {
      return { requireMatch: true };
    }

    return null;
  }

  protected _onOptionSelection(event: any) {
    if(event.original.option && event.original.option.value) {
      if(event.target) {
        this.fGroup.reset();
        this.fGroup.controls['tipoPendenza_ctrl'].setValue(event.original.option.value.jsonP);
        event.target.blur();
        this._updateValues(event.original.option.value.jsonP);
      }
    }
  }

  protected _asyncKeyUp(event: any) {
    this._asyncTipiPendenza.asyncOptions.clearAllTimeout();
    if(event.target.value) {
      const _delayFct = function () {
        this._asyncTipiPendenza.asyncOptions.clearAllTimeout();
        if(this._asyncTipiPendenza.isOpen()) {
          this._asyncTipiPendenza.close();
        }
        this._getTipiPendenza(event.target.value);
      }.bind(this);
      this._asyncTipiPendenza.asyncOptions.setTimeout(_delayFct, 800);
    } else {
      this._asyncTipiPendenza.close();
      this.tipiPendenza_items = [];
    }
  }

  /*
   * Check item index list by key
   * @param item: json item
   * @param {Parameters[]} checkList
   * @param {string} key
   * @returns {boolean}
   */
  // protected checkItemIndex(item: any, checkList: Parameters[], key: string): boolean {
  //   let _hasEntry: boolean = false;
  //   checkList.forEach((el) => {
  //     if(el.jsonP[key] == item[key]) {
  //       _hasEntry = true;
  //     }
  //   });
  //   return _hasEntry;
  // }

  protected _elencoApplicazioni() {
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

  protected _lfsChange(event: any, controller: string) {
    if(event.type == 'file-selector-change') {
      if(event.value) {
        this.fGroup.controls[controller].setValidators([ Validators.required ]);
      } else {
        this.fGroup.controls[controller].clearValidators();
      }
      this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
    }
  }

  protected _getTipiPendenza(value: string) {
    const _service = UtilService.URL_TIPI_PENDENZA + '?descrizione=' + value + '&nonAssociati=' + this.parent.json.idDominio + '&' + UtilService.QUERY_ESCLUDI_METADATI_PAGINAZIONE;
    this._searching = true;
    this.fGroup.controls['tipoPendenza_ctrl'].disable();
    this.gps.forkService([_service]).subscribe(
    (_responses) => {
      this._asyncTipiPendenza.asyncOptions.clearAllTimeout();
      this._searching = false;
      this.fGroup.controls['tipoPendenza_ctrl'].enable();
      let _body = _responses[0]['body'];
      let p: Parameters;
      this.tipiPendenza_items = _body['risultati'].map(function(item) {
        p = new Parameters();
        p.jsonP = item;
        p.model = this.mapNewItem(item);
        return p;
      }, this);
      if(!this._asyncTipiPendenza.isOpen()) {
        this._asyncTipiPendenza.open();
      }
      this._asyncTipiPendenza.focusInput();
      this.gps.updateSpinner(false);
    },
    (error) => {
      this._asyncTipiPendenza.asyncOptions.clearAllTimeout();
      this._searching = false;
      this.tipiPendenza_items = [];
      this.fGroup.controls['tipoPendenza_ctrl'].enable();
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    _std.titolo = new Dato({ label: item.descrizione, value: '' });
    _std.sottotitolo = new Dato({ label: Voce.ID_TIPO_PENDENZA+': ', value: item.idTipoPendenza });
    return _std;
  }

  protected _updateValues(json: any) {
    if(!this.fGroup.controls['tipoPendenza_ctrl'].disabled) {
      return;
    }

    this._preset.shadow_visualizzazione = (json.visualizzazione?json.visualizzazione:'');
    this._preset.shadow_validazione = (json.portaleBackoffice?json.portaleBackoffice.validazione:'');
    this._preset.shadow_definizione = ((json.portaleBackoffice && json.portaleBackoffice.form)?(json.portaleBackoffice.form.definizione ||''):'');
    this._preset.shadow_definizioneIPO = ((json.portaleBackoffice && json.portaleBackoffice.trasformazione)?(json.portaleBackoffice.trasformazione.definizione ||''):'');
    this._preset.shadow_definizionePS = ((json.portalePagamento && json.portalePagamento.form)?(json.portalePagamento.form.definizione ||''):'');
    this._preset.shadow_impaginazione = ((json.portalePagamento && json.portalePagamento.form)?(json.portalePagamento.form.impaginazione ||''):'');
    this._preset.shadow_validazionePS = (json.portalePagamento?(json.portalePagamento.validazione ||''):'');
    this._preset.shadow_definizionePS1 = ((json.portalePagamento && json.portalePagamento.trasformazione)?(json.portalePagamento.trasformazione.definizione ||''):'');
    this._preset.shadow_oggettoNAP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaAvviso)?(json.avvisaturaMail.promemoriaAvviso.oggetto ||''):'');
    this._preset.shadow_messaggioNAP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaAvviso)?(json.avvisaturaMail.promemoriaAvviso.messaggio ||''):'');
    this._preset.shadow_oggettoPSP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaScadenza)?(json.avvisaturaMail.promemoriaScadenza.oggetto ||''):'');
    this._preset.shadow_messaggioPSP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaScadenza)?(json.avvisaturaMail.promemoriaScadenza.messaggio ||''):'');
    this._preset.shadow_oggettoNRP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaRicevuta)?(json.avvisaturaMail.promemoriaRicevuta.oggetto ||''):'');
    this._preset.shadow_messaggioNRP = ((json.avvisaturaMail && json.avvisaturaMail.promemoriaRicevuta)?(json.avvisaturaMail.promemoriaRicevuta.messaggio ||''):'');
    this._preset.shadow_oggettoNAP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaAvviso)?(json.avvisaturaAppIO.promemoriaAvviso.oggetto ||''):'');
    this._preset.shadow_messaggioNAP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaAvviso)?(json.avvisaturaAppIO.promemoriaAvviso.messaggio ||''):'');
    this._preset.shadow_oggettoPSP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaScadenza)?(json.avvisaturaAppIO.promemoriaScadenza.oggetto ||''):'');
    this._preset.shadow_messaggioPSP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaScadenza)?(json.avvisaturaAppIO.promemoriaScadenza.messaggio ||''):'');
    this._preset.shadow_oggettoNRP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaRicevuta)?(json.avvisaturaAppIO.promemoriaRicevuta.oggetto ||''):'');
    this._preset.shadow_messaggioNRP_IO = ((json.avvisaturaAppIO && json.avvisaturaAppIO.promemoriaRicevuta)?(json.avvisaturaAppIO.promemoriaRicevuta.messaggio ||''):'');
    this._preset.shadow_richiesta = (json.tracciatoCsv?(json.tracciatoCsv.richiesta ||''):'');
    this._preset.shadow_risposta = (json.tracciatoCsv?(json.tracciatoCsv.risposta ||''):'');

    setTimeout(() => {
      if (json.valori && json.valori.codificaIUV !== undefined) {
        this.fGroup.controls['codificaIUV_ctrl'].setValue(json.valori.codificaIUV);
      }
      if (json.valori && json.valori.abilitato !== undefined) {
        this.fGroup.controls['abilita_ctrl'].setValue(json.valori.abilitato);
      }
      if (json.valori && json.valori.pagaTerzi !== undefined) {
        this.fGroup.controls['pagaTerzi_ctrl'].setValue(json.valori.pagaTerzi);
      }
      // PERSONALIZZAZIONE DETTAGLIO PENDENZA
      if (json.valori && json.valori.visualizzazione !== undefined) {
      this.fGroup.controls['visualizzazione_ctrl'].setValue((json.valori.visualizzazione)?(json.valori.visualizzazione || ''):'');
      }
      // INSERIMENTO PENDENZE DA OPERATORE
      if (json.valori && json.valori.portaleBackoffice) {
        const _pb: any = json.valori.portaleBackoffice;
        this.fGroup.controls['generatore_ctrl'].setValue((_pb.form)?(_pb.form.tipo || ''):'');
        this.fGroup.controls['definizione_ctrl'].setValue((_pb.form)?(_pb.form.definizione || ''):'');
        this.fGroup.controls['validazione_ctrl'].setValue(_pb.validazione || '');
        this.fGroup.controls['tipoTrasformazioneIPO_ctrl'].setValue((_pb.trasformazione)?(_pb.trasformazione.tipo || ''):'');
        this.fGroup.controls['definizioneIPO_ctrl'].setValue((_pb.trasformazione)?(_pb.trasformazione.definizione || ''):'');
        this.fGroup.controls['inoltroIPO_ctrl'].setValue(_pb.inoltro || '');
        if (_pb.abilitato !== undefined) {
          this.fGroup.controls['abilitatoIPO_ctrl'].setValue(_pb.abilitato);
        }
      }
      // PAGAMENTO SPONTANEO
      if (json.valori && json.valori.portalePagamento) {
        const _pp: any = json.valori.portalePagamento;
        this.fGroup.controls['generatorePS_ctrl'].setValue((_pp.form)?(_pp.form.tipo || ''):'');
        this.fGroup.controls['definizionePS_ctrl'].setValue((_pp.form)?(_pp.form.definizione || ''):'');
        this.fGroup.controls['impaginazione_ctrl'].setValue((_pp.form)?(_pp.form.impaginazione || ''):'');
        this.fGroup.controls['validazionePS_ctrl'].setValue(_pp.validazione || '');
        this.fGroup.controls['tipoTrasformazionePS_ctrl'].setValue((_pp.trasformazione)?(_pp.trasformazione.tipo || ''):'');
        this.fGroup.controls['definizionePS1_ctrl'].setValue((_pp.trasformazione)?(_pp.trasformazione.definizione || ''):'');
        this.fGroup.controls['inoltroPS_ctrl'].setValue(_pp.inoltro || '');
        if (_pp.abilitato !== undefined) {
          this.fGroup.controls['abilitatoPS_ctrl'].setValue(_pp.abilitato);
        }
      }
      // COMUNICAZINI VIA EMAIL
      if (json.valori && json.valori.avvisaturaMail) {
        if (json.valori.avvisaturaMail.promemoriaAvviso) {
          const _pa: any = json.valori.avvisaturaMail.promemoriaAvviso;
          if (_pa.abilitato !== undefined) {
            this.fGroup.controls['abilitatoNAP_ctrl'].setValue(_pa.abilitato);
          }
          if (_pa.allegaPdf !== undefined) {
            this.fGroup.controls['allegaPdfNAP_ctrl'].setValue(_pa.allegaPdf);
          }
          this.fGroup.controls['tipoTemplateNAP_ctrl'].setValue(_pa.tipo || '');
          if (_pa.oggetto) {
            this.fGroup.controls['oggettoNAP_ctrl'].setValue(_pa.oggetto || '');
            this.fGroup.controls['messaggioNAP_ctrl'].setValue(_pa.messaggio || '');
          }
        }
        if (json.valori && json.valori.avvisaturaMail.promemoriaScadenza) {
          const _ps: any = json.valori.avvisaturaMail.promemoriaScadenza;
          if (_ps.abilitato !== undefined) {
            this.fGroup.controls['abilitatoPSP_ctrl'].setValue(_ps.abilitato);
          }
          this.fGroup.controls['giorniPreavvisoPSP_ctrl'].setValue(_ps.preavviso || '');
          this.fGroup.controls['tipoTemplatePSP_ctrl'].setValue(_ps.tipo || '');
          if (_ps.oggetto) {
            this.fGroup.controls['oggettoPSP_ctrl'].setValue(_ps.oggetto || '');
            this.fGroup.controls['messaggioPSP_ctrl'].setValue(_ps.messaggio || '');
          }
        }
        if (json.valori && json.valori.avvisaturaMail.promemoriaRicevuta) {
          const _pr: any = json.valori.avvisaturaMail.promemoriaRicevuta;
          if (_pr.abilitato !== undefined) {
            this.fGroup.controls['abilitatoNRP_ctrl'].setValue(_pr.abilitato);
          }
          if (_pr.soloEseguiti !== undefined) {
            this.fGroup.controls['soloPagamentiNRP_ctrl'].setValue(_pr.soloEseguiti);
          }
          if (_pr.allegaPdf !== undefined) {
            this.fGroup.controls['allegaPdfNRP_ctrl'].setValue(_pr.allegaPdf);
          }
          this.fGroup.controls['tipoTemplateNRP_ctrl'].setValue(_pr.tipo || '');
          if (_pr.oggetto) {
            this.fGroup.controls['oggettoNRP_ctrl'].setValue(_pr.oggetto || '');
            this.fGroup.controls['messaggioNRP_ctrl'].setValue(_pr.messaggio || '');
          }
        }
      }
      // APP IO
      if (json.valori && json.valori.avvisaturaAppIO) {
        this.fGroup.controls['apiKeyNAP_IO_ctrl'].setValue(json.valori.avvisaturaAppIO.apiKey || '');
        if (json.valori.avvisaturaAppIO.promemoriaAvviso) {
          const _pa: any = json.valori.avvisaturaAppIO.promemoriaAvviso;
          if (_pa.abilitato !== undefined) {
            this.fGroup.controls['abilitatoNAP_IO_ctrl'].setValue(_pa.abilitato);
          }
          this.fGroup.controls['tipoTemplateNAP_IO_ctrl'].setValue(_pa.tipo || '');
          if (_pa.oggetto) {
            this.fGroup.controls['oggettoNAP_IO_ctrl'].setValue(_pa.oggetto || '');
            this.fGroup.controls['messaggioNAP_IO_ctrl'].setValue(_pa.messaggio || '');
          }
        }
        if (json.valori && json.valori.avvisaturaAppIO.promemoriaScadenza) {
          const _ps: any = json.valori.avvisaturaAppIO.promemoriaScadenza;
          if (_ps.abilitato !== undefined) {
            this.fGroup.controls['abilitatoPSP_IO_ctrl'].setValue(_ps.abilitato);
          }
          this.fGroup.controls['giorniPreavvisoPSP_IO_ctrl'].setValue(_ps.preavviso || '');
          this.fGroup.controls['tipoTemplatePSP_IO_ctrl'].setValue(_ps.tipo || '');
          if (_ps.oggetto) {
            this.fGroup.controls['oggettoPSP_IO_ctrl'].setValue(_ps.oggetto || '');
            this.fGroup.controls['messaggioPSP_IO_ctrl'].setValue(_ps.messaggio || '');
          }
        }
        if (json.valori && json.valori.avvisaturaAppIO.promemoriaRicevuta) {
          const _pr: any = json.valori.avvisaturaAppIO.promemoriaRicevuta;
          if (_pr.abilitato !== undefined) {
            this.fGroup.controls['abilitatoNRP_IO_ctrl'].setValue(_pr.abilitato);
          }
          if (_pr.soloEseguiti !== undefined) {
            this.fGroup.controls['soloPagamentiNRP_IO_ctrl'].setValue(_pr.soloEseguiti);
          }
          this.fGroup.controls['tipoTemplateNRP_IO_ctrl'].setValue(_pr.tipo || '');
          if (_pr.oggetto) {
            this.fGroup.controls['oggettoNRP_IO_ctrl'].setValue(_pr.oggetto || '');
            this.fGroup.controls['messaggioNRP_IO_ctrl'].setValue(_pr.messaggio || '');
          }
        }
      }
      // ALTRE FUNZIONI
      if (json.valori && json.valori.tracciatoCsv) {
        this.fGroup.controls['tipoTemplateAF_ctrl'].setValue(json.valori.tracciatoCsv.tipo || '');
        this.fGroup.controls['richiesta_ctrl'].setValue(json.valori.tracciatoCsv.richiesta || '');
        this.fGroup.controls['risposta_ctrl'].setValue(json.valori.tracciatoCsv.risposta || '');
        this.fGroup.controls['lineaEsito_ctrl'].setValue(json.valori.tracciatoCsv.intestazione || '');
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

    if(!this.fGroup.controls['tipoPendenza_ctrl'].disabled) {
      _json = _info['tipoPendenza_ctrl'];
    } else {
      _json = JSON.parse(JSON.stringify(this.json));
    }

    const _promemoriaAvviso = {};
    _promemoriaAvviso['tipo'] = _info['tipoTemplateNAP_ctrl'] || null;
    _promemoriaAvviso['oggetto'] = _info['oggettoNAP_ctrl'] || null;
    _promemoriaAvviso['messaggio'] = _info['messaggioNAP_ctrl'] || null;
    _promemoriaAvviso['abilitato'] = (_info['abilitatoNAP_ctrl'] != undefined)?_info['abilitatoNAP_ctrl']:null;
    _promemoriaAvviso['allegaPdf'] = (_info['allegaPdfNAP_ctrl'] != undefined)?_info['allegaPdfNAP_ctrl']:null;

    const _promemoriaScadenza = {};
    _promemoriaScadenza['tipo'] = _info['tipoTemplatePSP_ctrl'] || null;
    _promemoriaScadenza['oggetto'] = _info['oggettoPSP_ctrl'] || null;
    _promemoriaScadenza['messaggio'] = _info['messaggioPSP_ctrl'] || null;
    _promemoriaScadenza['preavviso'] = _info['giorniPreavvisoPSP_ctrl'] || null;
    _promemoriaScadenza['abilitato'] = (_info['abilitatoPSP_ctrl'] != undefined)?_info['abilitatoPSP_ctrl']:null;

    const _promemoriaRicevuta = {};
    _promemoriaRicevuta['tipo'] = _info['tipoTemplateNRP_ctrl'] || null;
    _promemoriaRicevuta['oggetto'] = _info['oggettoNRP_ctrl'] || null;
    _promemoriaRicevuta['messaggio'] = _info['messaggioNRP_ctrl'] || null;
    _promemoriaRicevuta['abilitato'] = (_info['abilitatoNRP_ctrl'] != undefined)?_info['abilitatoNRP_ctrl']:null;
    _promemoriaRicevuta['allegaPdf'] = (_info['allegaPdfNRP_ctrl'] != undefined)?_info['allegaPdfNRP_ctrl']:null;
    _promemoriaRicevuta['soloEseguiti'] = (_info['soloPagamentiNRP_ctrl'] != undefined)?_info['soloPagamentiNRP_ctrl']:null;

    const _promemoriaAvvisoIO = {};
    _promemoriaAvvisoIO['tipo'] = _info['tipoTemplateNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['oggetto'] = _info['oggettoNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['messaggio'] = _info['messaggioNAP_IO_ctrl'] || null;
    _promemoriaAvvisoIO['abilitato'] = (_info['abilitatoNAP_IO_ctrl'] != undefined)?_info['abilitatoNAP_IO_ctrl']:null;

    const _promemoriaScadenzaIO = {};
    _promemoriaScadenzaIO['tipo'] = _info['tipoTemplatePSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['oggetto'] = _info['oggettoPSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['messaggio'] = _info['messaggioPSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['preavviso'] = _info['giorniPreavvisoPSP_IO_ctrl'] || null;
    _promemoriaScadenzaIO['abilitato'] = (_info['abilitatoPSP_IO_ctrl'] != undefined)?_info['abilitatoPSP_IO_ctrl']:null;

    const _promemoriaRicevutaIO = {};
    _promemoriaRicevutaIO['tipo'] = _info['tipoTemplateNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['oggetto'] = _info['oggettoNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['messaggio'] = _info['messaggioNRP_IO_ctrl'] || null;
    _promemoriaRicevutaIO['abilitato'] = (_info['abilitatoNRP_IO_ctrl'] != undefined)?_info['abilitatoNRP_IO_ctrl']:null;
    _promemoriaRicevutaIO['soloEseguiti'] = (_info['soloPagamentiNRP_IO_ctrl'] != undefined)?_info['soloPagamentiNRP_IO_ctrl']:null;

    _json.valori = {
      pagaTerzi: (_info['pagaTerzi_ctrl'] !== undefined)?_info['pagaTerzi_ctrl']:null,
      abilitato: (_info['abilita_ctrl'] !== undefined)?_info['abilita_ctrl']:null,
      codificaIUV: (_info['codificaIUV_ctrl'])?_info['codificaIUV_ctrl']:null,
      // PERSONALIZZAZIONE DETTAGLIO PENDENZA
      visualizzazione: _info['visualizzazione_ctrl'] || null,
      // INSERIMENTO PENDENZE DA OPERATORE
      portaleBackoffice: {
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
        abilitato: (_info['abilitatoIPO_ctrl'] != undefined)?_info['abilitatoIPO_ctrl']:null
      },
      // PAGAMENTO SPONTANEO
      portalePagamento: {
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
        abilitato: (_info['abilitatoPS_ctrl'] != undefined)?_info['abilitatoPS_ctrl']:null
      },
      // COMUNICAZINI VIA EMAIL
      avvisaturaMail: {
        promemoriaAvviso: _promemoriaAvviso || null,
        promemoriaScadenza: _promemoriaScadenza || null,
        promemoriaRicevuta: _promemoriaRicevuta || null
      },
      // APP IO
      avvisaturaAppIO: {
        apiKey: _info['apiKeyNAP_IO_ctrl'] || null,
        promemoriaAvviso: _promemoriaAvvisoIO || null,
        promemoriaScadenza: _promemoriaScadenzaIO || null,
        promemoriaRicevuta: _promemoriaRicevutaIO || null
      },
      // ALTRE FUNZIONI
      tracciatoCsv: {
        tipo: _info['tipoTemplateAF_ctrl'] || null,
        intestazione: _info['lineaEsito_ctrl'] || null,
        richiesta: _info['richiesta_ctrl'] || null,
        risposta: _info['risposta_ctrl'] || null
      }
    };

    return _json;
  }

}
