import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';

import { Riepilogo } from '../../../../classes/view/riepilogo';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { IExport } from '../../../../classes/interfaces/IExport';

import * as moment from 'moment';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { CronoCode } from '../../../../classes/view/crono-code';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';
import { NewStandardCollapse } from '../../../../classes/view/new-standard-collapse';
import { TwoColsCollapse } from '../../../../classes/view/two-cols-collapse';
import { HttpResponse } from '@angular/common/http';
import { forEach } from 'angular2-json-schema-form';

@Component({
  selector: 'link-pendenze',
  templateUrl: './pendenze-view.component.html',
  styleUrls: ['./pendenze-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PendenzeViewComponent implements IModalDialog, IExport, OnInit {

  @Input() tentativi = [];
  @Input() importi = [];
  @Input() informazioni = [];
  @Input() eventi = [];

  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() pendenzaMBT: boolean = false;

  protected _voce = Voce;
  protected NOTA = UtilService.NOTA;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected info: Riepilogo;
  protected allegati = [];
  protected proprietaPendenzaAvviso = [];
  protected proprietaPendenzaRt = [];
  protected datiAllegati: any;
  protected infoVisualizzazione: any = { visible: false, titolo: '', campi: [] };
  protected _paymentsSum: number = 0;
  protected _importiOverIcons: string[] = ['file_download'];
  protected _tentativiOverIcons: string[] = ['file_download'];

  protected _isLoadingMore: boolean = false;
  protected _pageRef: any = { next: null };
  protected _lastEvtResponse: any;
  protected _chunks: any[] = [];
  protected _isVisualizzaPersonalizzazioni: boolean = false;

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
    this.dettaglioPendenza();
    this.elencoTentativi();
    this.elencoEventi();
  }

  protected dettaglioPendenza(patch: boolean = false) {
    // /pendenze/idA2A/idPendenza
    let _url = UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        let _json = _response.body;
        this.mapJsonDetail(_json);
        this.gps.updateSpinner(false);
        (patch)?this.us.alert('Aggiornamento stato completato'):null;
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(_json: any) {
    //Riepilogo
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DESCRIZIONE, value: _json.causale }),
      sottotitolo: new Dato({ label: Voce.DEBITORE, value: Dato.concatStrings([_json.soggettoPagatore.anagrafica.toUpperCase(), _json.soggettoPagatore.identificativo.toUpperCase()], ', ') }),
      importo: this.us.currencyFormat(_json.importo),
      stato: UtilService.STATI_PENDENZE[_json.stato],
      extraInfo: []
    });
    if(_json.dominio.ragioneSociale && _json.dominio.idDominio) {
      this.info.extraInfo.push({label: Voce.ENTE_CREDITORE + ': ', value: Dato.concatStrings([_json.dominio.ragioneSociale, _json.dominio.idDominio], ', ')});
    }
    if(_json.unitaOperativa && _json.unitaOperativa.ragioneSociale && _json.unitaOperativa.idUnita) {
      const _uo: string = Dato.concatStrings([_json.unitaOperativa.idUnita, _json.unitaOperativa.ragioneSociale], ' - ');
      this.info.extraInfo.push({label: Voce.UNITA_OPERATIVA + ': ', value: _uo});
    }
    if(_json.direzione) {
      this.info.extraInfo.push({label: Voce.DIREZIONE + ': ', value: _json.direzione});
    }
    if(_json.divisione) {
      this.info.extraInfo.push({label: Voce.DIVISIONE + ': ', value: _json.divisione});
    }
    if(_json.tipoPendenza && _json.tipoPendenza.descrizione) {
      this.info.extraInfo.push({label: Voce.TIPO_PENDENZA + ': ', value: _json.tipoPendenza.descrizione});
    }
    if(_json.tassonomia) {
      this.info.extraInfo.push({ label: Voce.TASSONOMIA_ENTE+': ', value: _json.tassonomia });
    }
    if(_json.annoRiferimento) {
      this.info.extraInfo.push({ label: Voce.ANNO_RIFERIMENTO+': ', value: _json.annoRiferimento });
    }
    if(_json.cartellaPagamento) {
      this.info.extraInfo.push({ label: Voce.CARTELLA_DI_PAGAMENTO+': ', value: _json.cartellaPagamento });
    }
    const _iuv = (_json.iuvAvviso)?_json.iuvAvviso:_json.iuvPagamento;
    if(_iuv) {
      this.info.extraInfo.push({label: Voce.IUV + ': ', value: _iuv});
    }
    if(_json.numeroAvviso) {
      this.info.extraInfo.push({ label: Voce.AVVISO+': ', value: _json.numeroAvviso });
    }
    if(_json.idA2A) {
      this.info.extraInfo.push({ label: Voce.ID_A2A+': ', value: _json.idA2A });
    }
    if(_json.idPendenza) {
      this.info.extraInfo.push({ label: Voce.ID_PENDENZA+': ', value: _json.idPendenza });
    }
    if(_json.dataCaricamento) {
      this.info.extraInfo.push({ label: Voce.DATA_CARICAMENTO+': ', value: moment(_json.dataCaricamento).format('DD/MM/YYYY') });
    }
    if(_json.dataValidita) {
      this.info.extraInfo.push({ label: Voce.VALIDITA+': ', value: moment(_json.dataValidita).format('DD/MM/YYYY') });
    }
    if(_json.dataScadenza) {
      this.info.extraInfo.push({ label: Voce.SCADENZA+': ', value: moment(_json.dataScadenza).format('DD/MM/YYYY') });
    }
    if(_json.dataUltimaModificaAca) {
      this.info.extraInfo.push({ label: Voce.DATA_ULTIMO_AGGIORNAMENTO+': ', value: moment(_json.dataUltimaModificaAca).format('DD/MM/YYYY [ore] HH:mm:ss') });
    }
    if(_json.dataUltimaComunicazioneAca) {
      this.info.extraInfo.push({ label: Voce.DATA_ULTIMA_COMUNICAZIONE_ACA+': ', value: moment(_json.dataUltimaComunicazioneAca).format('DD/MM/YYYY [ore] HH:mm:ss') });
    }
    if(_json.descrizioneStato && 
    	(_json.stato === this.us.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ANOMALA))
    ) {
      this.info.extraInfo.push({ label: Voce.DESCRIZIONE_STATO+': ', value: _json.descrizioneStato });
    }

    //Json Visualizzazione
    if(_json.tipoPendenza && _json.tipoPendenza.visualizzazione) {
      try {
        const _vis = JSON.parse(decodeURIComponent(atob(_json.tipoPendenza.visualizzazione).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join('')));
        this.infoVisualizzazione.visible = !!(_vis.vistaDettaglio && _vis.vistaDettaglio.titolo && _vis.vistaDettaglio.campi.length);
        if (_vis.vistaDettaglio) {
          this.infoVisualizzazione.titolo = _vis.vistaDettaglio.titolo || '';
          this.infoVisualizzazione.campi = _vis.vistaDettaglio.campi.map((field) => {
            return new Dato({label: field.label, value: this.us.searchPropertyByPathString(field.path, _json)});
          });
        }
      } catch (e) {
        console.warn(e);
      }
    }
    //Dettaglio importi
    this._paymentsSum = 0;
    this.importi = _json.voci.map(function(item, index) {
      let _std = new NewStandardCollapse();
	  let _description = item.descrizione;
	  if (!item.descrizione || item.descrizione.trim() === '') {
		if(item.tipoBollo) {
			 _description = Voce.BOLLO;
		  } else {
			_description = Voce.VOCE + ' '+(index +1);		
		  }
	  } else {
		_description = item.descrizione;
	  }
	  
      _std.titolo = new Dato({ value: _description });
      _std.elenco = [];
      const lbls: string[] = [];
      const vals: string[] = [];
      if(item.idVocePendenza) {
        lbls.push(Voce.ID);
        vals.push(item.idVocePendenza);
      }
      if(item.dominio && item.dominio.ragioneSociale) {
        lbls.push(Voce.ENTE_CREDITORE);
        vals.push(item.dominio.ragioneSociale);
      }
      _std.sottotitolo = Dato.arraysToDato(lbls, vals, ', ');
      if(!item.tipoBollo) {
        let tipoContabilitaLabel =  UtilService.TIPI_CONTABILITA_NUMERICHE[item.tipoContabilita];
        _std.elenco.push({ label: Voce.TASSONOMIA, value: Dato.concatStrings([tipoContabilitaLabel, item.codiceContabilita ], '/') });
        _std.elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.ibanAccredito });
        _std.elenco.push({ label: Voce.CONTO_APPOGGIO, value: item.ibanAppoggio });
      } else { // informazioni bollo
		_std.elenco.push({ label: Voce.ID_BOLLO, value: item.tipoBollo });
		_std.elenco.push({ label: Voce.PROVINCIA, value: item.provinciaResidenza });
		_std.elenco.push({ label: Voce.HASH_DOCUMENTO, value: item.hashDocumento });
	  }
      _std.importo = this.us.currencyFormat(item.importo);
      _std.stato = item.stato;
      this._paymentsSum += UtilService.defaultDisplay({ value: item.importo, text: 0 });
      let p = new Parameters();
      _std.item = item;
      if(item.dominio){
        _std.item.dominio = item.dominio;
        _std.item.idDominio = item.dominio.idDominio;
      } else {
        _std.item.dominio = this.json.dominio;
        _std.item.idDominio = this.json.dominio.idDominio;
      }
      // Metadata
      if (item.metadata && item.metadata.mapEntries) {
        const _mapEntries = item.metadata.mapEntries.map(x => { return { label: x.key, value: x.value } });
        _std.elenco.push({ label: Voce.METADATA, value: _mapEntries, type: 'metadata' });
      }
	  // dati allegati
	  if(item.datiAllegati) {
	  		const _datiAllegati = item.datiAllegati;
			_std.elenco.push({ label: Voce.DATI_CUSTOM, value: _datiAllegati, type: 'allegati' });
	  	}
	  
      p.jsonP = item;
      p.model = _std;
      p.type = UtilService.NEW_STANDARD_COLLAPSE;
      return p;
    }, this);
    //Note
    if(_json.note) {
      this.informazioni = _json.note.map(function(_nota) {
        let _cc = new CronoCode();
        _cc.data = _nota.data?moment(_nota.data).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
        _cc.codice = _nota.autore;
        _cc.titolo = new Dato({ value: _nota.oggetto });
        _cc.sottotitolo = new Dato({ value: _nota.testo });
        let p = new Parameters();
        p.jsonP = _nota;
        p.model = _cc;
        p.type = UtilService.CRONO_CODE;
        return p;
      }, this);
    }
    if(_json.allegati) {
      this.allegati = _json.allegati.map(function(_allegato) {
        const _std = new NewStandardCollapse();
        _std.titolo = new Dato({ value: _allegato.descrizione });
        _std.sottotitolo = new Dato({ value: _allegato.nome });
        _std.item = _allegato;
        const p = new Parameters();
        p.jsonP = _allegato;
        p.model = _std;
        p.type = UtilService.ALLEGATO;
        return p;
      }, this);
    }
	if(_json.proprieta) {
		// Data validita avviso
		if(_json.proprieta.dataScandenzaAvviso){
			this.proprietaPendenzaAvviso.push({label: Voce.DATA_VALIDITA_AVVISO, value: moment(_json.proprieta.dataScandenzaAvviso).format('DD/MM/YYYY') });
		}
		// informativa importo avviso
		if(_json.proprieta.informativaImportoAvviso){
			this.proprietaPendenzaAvviso.push({label: Voce.INFORMATIVA_IMPORTO_AVVISO, value: _json.proprieta.informativaImportoAvviso});
		}
		// lingua secondaria avviso
		if(_json.proprieta.linguaSecondaria){
			this.proprietaPendenzaAvviso.push({label: Voce.LINGUA_SECONDARIA_AVVISO, value: UtilService.LINGUE_SECONDARIE[_json.proprieta.linguaSecondaria]});
		}
		// causale lingua secondaria avviso
		if(_json.proprieta.linguaSecondariaCausale){
			this.proprietaPendenzaAvviso.push({label: Voce.CAUSALE_LINGUA_SECONDARIA_AVVISO, value: _json.proprieta.linguaSecondariaCausale});
		}
		// informativa importo avviso lingua secondaria
		if(_json.proprieta.linguaSecondariaInformativaImportoAvviso){
			this.proprietaPendenzaAvviso.push({label: Voce.INFORMATIVA_IMPORTO_LINGUA_SECONDARIA_AVVISO, value: _json.proprieta.linguaSecondariaInformativaImportoAvviso});
		}
		// riga 1 ricevuta
		if(_json.proprieta.lineaTestoRicevuta1){
			this.proprietaPendenzaRt.push({label: Voce.RT_LINEA_TESTO_RIGA1, value: _json.proprieta.lineaTestoRicevuta1});
		}
		// riga 2 ricevuta
		if(_json.proprieta.lineaTestoRicevuta2){
			this.proprietaPendenzaRt.push({label: Voce.RT_LINEA_TESTO_RIGA2, value: _json.proprieta.lineaTestoRicevuta2});
		}
    }
	if(_json.datiAllegati) {
		this.datiAllegati =  _json.datiAllegati;
	}
	
	this._isVisualizzaPersonalizzazioni = _json.proprieta || _json.datiAllegati;
    
    this.pendenzaMBT = this.us.isPendenzaMBT(_json);
  }

  protected elencoTentativi() {
    this.gps.getDataService(this.json.rpp).subscribe(function (_response) {
        let _body = _response.body;
        this.tentativi = _body['risultati'].map(function(item) {
          let _istituto = Voce.NO_PSP;
          const stStrings: string[] = [];
          const versione620: boolean = !!(item.rpt && item.rpt.versioneOggetto && item.rpt.versioneOggetto === '6.2.0');
          if (versione620) {
            _istituto = (item.rt && item.rt.istitutoAttestante)?item.rt.istitutoAttestante.denominazioneAttestante:'';
            const _date = item.rpt.dataOraMessaggioRichiesta?moment(item.rpt.dataOraMessaggioRichiesta).format('DD/MM/YYYY [ore] HH:mm:ss'):Voce.NON_PRESENTE;
            stStrings.push(Voce.DATA+': '+_date);
            const _ccp = (item.rpt.datiVersamento && item.rpt.datiVersamento.codiceContestoPagamento)?item.rpt.datiVersamento.codiceContestoPagamento:Voce.NON_PRESENTE;
            stStrings.push(Voce.CCP+': '+_ccp);
          } else {
            if (item.rpt) {
              if (item.rpt.creditorReferenceId) {
                stStrings.push(Voce.IUV+': '+item.rpt.creditorReferenceId);
              }
            }
            if (item.rt) {
              _istituto = (item.rt.PSPCompanyName || '');
			  const _date = item.rt.paymentDateTime?moment(item.rt.paymentDateTime).format('DD/MM/YYYY [ore] HH:mm:ss'):Voce.NON_PRESENTE;
              stStrings.push(Voce.DATA+': '+_date);
              const _ccp = (item.rt.receiptId)?item.rt.receiptId:Voce.NON_PRESENTE;
              stStrings.push(Voce.ID_RICEVUTA+': '+_ccp);
            } else {
				stStrings.push(Voce.DATA+': '+Voce.NON_PRESENTE);	
			}
            
          }
          let _subtitle = Dato.concatStrings(stStrings, ', ');
          let _std = new StandardCollapse();
          let _map = UtilService.MapStato(item, versione620);
          _std.titolo = new Dato({ label: '', value: _istituto });
          _std.sottotitolo = new Dato({ label: '', value: _subtitle });
          _std.stato = _map.stato;
          _std.motivo = _map.motivo;
          let p = new Parameters();
          p.model = _std;
          p.jsonP = item;
          p.type = UtilService.STANDARD_COLLAPSE;
          return p;
        }, this);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected elencoEventi() {
    let _url = UtilService.URL_GIORNALE_EVENTI;
    let _query = 'idA2A='+this.json.idA2A+'&idPendenza='+this.json.idPendenza;
    this.__getEventi(_url, _query);
  }

  protected __getEventi(_url, _query, _pages = false) {
    if(!this._isLoadingMore) {
      this._isLoadingMore = true;
      this.gps.getDataService(_url, _query).subscribe(function (_response) {
        this._lastEvtResponse = _response.body;
        const _evts = this._lastEvtResponse['risultati'].map(function(item) {
          const _stdTCC: TwoColsCollapse = new TwoColsCollapse();
          const _dataOraEventi = item.dataEvento?moment(item.dataEvento).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
          const _riferimento = this.us.mapRiferimentoGiornale(item);
          _stdTCC.titolo = new Dato({ label: this.us.mappaturaTipoEvento(item.componente, item.tipoEvento) });
          _stdTCC.sottotitolo = new Dato({ label: _riferimento });
          _stdTCC.stato = item.esito;
          _stdTCC.data = _dataOraEventi;
          if(item.dettaglioEsito) {
            _stdTCC.motivo = item.dettaglioEsito;
          }
          const _api = _url.split('?');
          _api[0] += '/' + item.id;
          _stdTCC.url = UtilService.RootByTOA() + _api.join('?');
          _stdTCC.elenco = [];
          if(item.durataEvento) {
            _stdTCC.elenco.push({ label: Voce.DURATA, value: this.us.formatMs(item.durataEvento) });
          }
          if(item.datiPagoPA) {
            if(item.datiPagoPA.idPsp) {
              _stdTCC.elenco.push({ label: Voce.ID_PSP, value: item.datiPagoPA.idPsp });
            }
            if(item.datiPagoPA.idCanale) {
              _stdTCC.elenco.push({ label: Voce.ID_CANALE, value: item.datiPagoPA.idCanale });
            }
            if(item.datiPagoPA.idIntermediarioPsp) {
              _stdTCC.elenco.push({ label: Voce.ID_INTERMEDIARIO_PSP, value: item.datiPagoPA.idIntermediarioPsp });
            }
            if(item.datiPagoPA.tipoVersamento) {
			let _tipoVersamento = UtilService.TIPI_VERSAMENTO[item.datiPagoPA.tipoVersamento] ? UtilService.TIPI_VERSAMENTO[item.datiPagoPA.tipoVersamento] : item.datiPagoPA.tipoVersamento;
              _stdTCC.elenco.push({ label: Voce.TIPO_VERSAMENTO, value: _tipoVersamento });
            }
            if(item.datiPagoPA.modelloPagamento) {
              _stdTCC.elenco.push({ label: Voce.MODELLO_PAGAMENTO, value: UtilService.MODELLI_PAGAMENTO[item.datiPagoPA.modelloPagamento] });
            }
            if(item.datiPagoPA.idDominio) {
              _stdTCC.elenco.push({ label: Voce.ID_DOMINIO, value: item.datiPagoPA.idDominio });
            }
            if(item.datiPagoPA.idIntermediario) {
              _stdTCC.elenco.push({ label: Voce.ID_INTERMEDIARIO, value: item.datiPagoPA.idIntermediario });
            }
            if(item.datiPagoPA.idStazione) {
              _stdTCC.elenco.push({ label: Voce.ID_STAZIONE, value: item.datiPagoPA.idStazione });
            }
          }
          let p = new Parameters();
          p.model = _stdTCC;
          p.type = UtilService.TWO_COLS_COLLAPSE;
          return p;
        }, this);
        this._pageRef = { next: (this._lastEvtResponse['prossimiRisultati'] || null) };
        this.eventi = _pages?this.eventi.concat(_evts):_evts;
        this._isLoadingMore = false;
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this._isLoadingMore = false;
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
    }
  }

  protected _addEdit(type: string, patchOperation: string, mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case UtilService.NOTA:
        _mb.async_callback = this.save.bind(this);
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuova nota';
        _mb.info.viewModel = this.json;
        _mb.info.templateName = UtilService.NOTA;
        break;
    }
    UtilService.dialogBehavior.next(_mb);
  }

  protected _actionMenuRules(): boolean {
    return (this.tentativi.filter((item : any) => {
      // Filtro per pagamento non eseguito
      const versione620: boolean = !!(item.jsonP.rpt && item.jsonP.rpt.versioneOggetto && item.jsonP.rpt.versioneOggetto === '6.2.0');
      return (UtilService.MapStato(item.jsonP, versione620).codiceEsito === 1 && item.jsonP.stato === 'RT_ACCETTATA_PA');
    }).length !== 0);
  }

  protected _actionMenuOp(operation: string) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = true;
    _mb.closure = this.refresh.bind(this);
    if (operation === 'sostituisci-rt') {
      _mb.async_callback = this.save.bind(this);
      _mb.operation = UtilService.PATCH_METHODS.REPLACE;
      _mb.info.dialogTitle = 'Sostituzione RT';
      _mb.info.parent = this.tentativi.map((el: any) => {
        return (el.jsonP.rt && el.jsonP.rt.datiPagamento && el.jsonP.rt.datiPagamento.identificativoUnivocoVersamento)?el.jsonP.rt.datiPagamento.identificativoUnivocoVersamento:'';
      }).filter(s => s !== '');
      _mb.info.templateName = UtilService.TENTATIVO_RT;

      UtilService.dialogBehavior.next(_mb);
    }
  }

  protected refreshAfterPatch() {
    this.dettaglioPendenza(true);
    this.elencoTentativi();
    this.elencoEventi();
  }

  protected _loadMoreEventi() {
    if (this._pageRef.next) {
      this.__getEventi(this._pageRef.next, '', true);
    }
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.causale:null });
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      switch(mb.info.templateName) {
        case UtilService.TENTATIVO_RT:
          this.refreshAfterPatch();
          break;
        case UtilService.NOTA:
          this.json = mb.info.viewModel;
          this.mapJsonDetail(this.json);
          break;
        default:
          let _service = UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza;
          let _json = [
            { op: mb.operation, path: '/stato', value: mb.info.viewModel.stato },
            { op: mb.operation, path: '/descrizioneStato', value: mb.info.viewModel.descrizioneStato }
          ];
          this.gps.saveData(_service, _json, null, UtilService.METHODS.PATCH).subscribe(
            (response) => {
              if(mb.editMode && mb.info.templateName == UtilService.PENDENZA) {
                this.json = response.body;
                this.modified = true;
                this.mapJsonDetail(response.body);
              }
              this.gps.updateSpinner(false);
            },
            (error) => {
              this.gps.updateSpinner(false);
              this.us.onError(error);
            });
          break;
      }
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _ref = null;
      let _service = null;
      let _method = null;
      switch(mb.info.templateName) {
        case UtilService.NOTA:
          _ref = UtilService.EncodeURIComponent(this.json.idA2A)+'/'+UtilService.EncodeURIComponent(this.json.idPendenza);
          _service = UtilService.URL_PENDENZE+'/'+_ref;
          _method = UtilService.METHODS.PATCH;
          _json = [{ op: mb.operation, path: '/'+UtilService.NOTA, value: mb.info.viewModel }];
          break;
        case UtilService.TENTATIVO_RT:
          _ref = '/'+UtilService.EncodeURIComponent(mb.info.viewModel.idDominio)+'/'+UtilService.EncodeURIComponent(mb.info.viewModel.codiceIUV)+'/'+UtilService.EncodeURIComponent(mb.info.viewModel.ccp);
          _service = UtilService.URL_RPPS+_ref;
          _method = UtilService.METHODS.PATCH;
          _json = [{ op: mb.operation, path: '/rt', value: mb.info.viewModel.b64 }];
          break;
      }
      if (_service) {
        this.gps.saveData(_service, _json, _query, _method).subscribe(
          (response) => {
            if(mb.editMode) {
              switch (mb.info.templateName) {
                case UtilService.NOTA:
                  mb.info.viewModel = response.body;
                  break;
              }
            }
            this.gps.updateSpinner(false);
            responseService.next(true);
          },
          (error) => {
            this.gps.updateSpinner(false);
            this.us.onError(error);
          });
      }
    }
  }

  esclusioneNotifiche() { }

  exportData() {
    this.gps.updateSpinner(true);
    let folders: string[] = [];
    const chunk: any[] = [];

    try {
      //Pdf Avviso di pagamento solo se non c'e una marca da bollo'
      if(this.json.numeroAvviso && !this.pendenzaMBT) {
        if (folders.indexOf(UtilService.ROOT_ZIP_FOLDER) == -1) {
          folders.push(UtilService.ROOT_ZIP_FOLDER);
        }
        chunk.push({
          url: UtilService.URL_AVVISI+'/'+UtilService.EncodeURIComponent(this.json.dominio.idDominio)+'/'+UtilService.EncodeURIComponent(this.json.numeroAvviso),
          content: 'application/pdf',
          name: this.json.dominio.idDominio + '_' + this.json.numeroAvviso + '.pdf' + UtilService.ROOT_ZIP_FOLDER,
          type: 'blob'
        });
      }
      this.tentativi.forEach((el) => {
        // /rpp/{idDominio}/{iuv}/{ccp}/rpt
        // /rpp/{idDominio}/{iuv}/{ccp}/rt
        // /eventi/?{idDominio}&{iuv}&{ccp}
        const item = el.jsonP;
        const ref: any = UtilService.ExportMapLoopCfg(item);
        if (ref.idd && ref.iuv && ref.ccp) {
          const _folder = (UtilService.ExportMapChunkLoopCfg('folder', ref).url || '');
          if (folders.indexOf(_folder) == -1) {
            folders.push(_folder);
          }
          chunk.push(UtilService.ExportMapChunkLoopCfg('Rpt.xml', ref, _folder));
          chunk.push(UtilService.ExportMapChunkLoopCfg('Eventi.csv', ref, _folder));
          if(ref.iddRT && ref.iuvRT && ref.ccpRT && _folder) {
            chunk.push(UtilService.ExportMapChunkLoopCfg('Rt.xml', ref, _folder));
            chunk.push(UtilService.ExportMapChunkLoopCfg('Rt.pdf', ref, _folder));
          }
        }
      }, this);
    } catch (error) {
      this.gps.updateSpinner(false);
      this.us.alert('Si è verificato un errore non previsto durante il recupero delle informazioni.', true);
      return;
    }
    if (folders.indexOf(UtilService.ROOT_ZIP_FOLDER) == -1) {
      folders.push(UtilService.ROOT_ZIP_FOLDER);
    }
    const _evtName: string = 'Eventi.csv' + UtilService.ROOT_ZIP_FOLDER;
    const structure: any = { folders: folders, names: [] };
    const rppChunks = chunk.reduce((acc: any, el: any, idx: number) => {
      const i = Math.trunc(idx / UtilService.PREFERENCES['MAX_THREAD_EXPORT_LIMIT']);
      (acc[i])?acc[i].push(el):acc[i] = [ el ];
      return acc;
    }, []);
    const evtChunks = this.us.chunkedData(this._lastEvtResponse, _evtName);
    this._chunks = rppChunks.concat(evtChunks);
    this._chunks.forEach((chunk: any[]) => {
      structure.names = structure.names.concat(chunk.map(_chunk => {
        return _chunk.name;
      }));
    });
    const calls: number = this._chunks.length;
    if(this._lastEvtResponse['pagina'] == this._lastEvtResponse['numPagine']) {
      structure.names.push(_evtName);
    }
    this.gps.updateSpinner(false);
    this.us.updateProgress(true, 'Elaborazione in corso...', 'indeterminate', 0);
    this.threadCall([], calls, structure);
  }

  threadCall(dataCalls: any[], calls: number, structure: any) {
    if (this._chunks.length !== 0) {
      const chunk: any[] = this._chunks.shift();
      const urls = chunk.map(chk => chk.url);
      const contents = chunk.map(chk => chk.content);
      const types = chunk.map(chk => chk.type);
      this.gps.multiExportService(urls, contents, types).subscribe(function (_responses) {
          _responses.forEach((response) => {
            dataCalls = dataCalls.concat(response);
          });
          if (this._chunks.length !== 0) {
            this.us.updateProgress(true, 'Download in corso...', 'determinate', Math.trunc(100 * (1 - (this._chunks.length/calls))));
            this.threadCall(dataCalls, calls, structure);
          } else {
            this._setDefaultData(dataCalls, structure);
          }
        }.bind(this),
        (error) => {
          this.us.updateProgress(false);
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    } else {
      this._setDefaultData(dataCalls, structure);
    }
  }

  protected _setDefaultData(dataCalls: any, structure: any) {
    if(this._lastEvtResponse['pagina'] == this._lastEvtResponse['numPagine']) {
      const _hr = {
        body: this._lastEvtResponse
      };
      const hr: HttpResponse<any> = new HttpResponse(_hr);
      dataCalls.push(hr);
    }
    this.us.updateProgress(true, 'Download in corso...', 'determinate', 100);
    setTimeout(() => {
      this.us.generateStructuredZip(dataCalls, structure, 'Pendenza_' + this.json.idA2A + '_' + this.json.idPendenza);
    }, 500);
  }

  scaricaAvviso() {
    const url = UtilService.URL_AVVISI + '/' + UtilService.EncodeURIComponent(this.json.dominio.idDominio) + '/' + UtilService.EncodeURIComponent(this.json.numeroAvviso);
    const name = this.json.dominio.idDominio + '_' + this.json.numeroAvviso;

    this.gps.pdf(url).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.us.savePdf(response.body, name);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      }
    );
  }

  stampaRicevuta() {
    const item = this.tentativi.find((el) => {
      return el.jsonP.stato === 'RT_ACCETTATA_PA';
    });
    if (item) {
      const ref: any = UtilService.ExportMapLoopCfg(item.jsonP);
      const url = '/rpp/' + UtilService.EncodeURIComponent(ref.iddRT) + '/' + UtilService.EncodeURIComponent(ref.iuvRT) + '/' + UtilService.EncodeURIComponent(ref.ccpRT) + '/rt';
      const name = `Rt_${ref.iuvRT}_${ref.ccpRT}`;

      this.gps.pdf(url).subscribe(
        (response) => {
          this.gps.updateSpinner(false);
          this.us.savePdf(response.body, name);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        }
      );
    } else {
      this.us.alert('Nessuan ricevuta trovata');
    }
  }
}
