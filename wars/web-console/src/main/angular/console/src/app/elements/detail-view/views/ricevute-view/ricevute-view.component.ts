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

@Component({
  selector: 'link-ricevute',
  templateUrl: './ricevute-view.component.html',
  styleUrls: ['./ricevute-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RicevuteViewComponent implements IModalDialog, IExport, OnInit {

  @Input() tentativi = [];
  @Input() importi = [];
  @Input() informazioni = [];
  @Input() eventi = [];

  @Input() json: any;
  @Input() modified: boolean = false;


  protected NOTA = UtilService.NOTA;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected info: Riepilogo;
  protected datiPsp = [];
  protected infoVisualizzazione: any = { visible: false, titolo: '', campi: [] };
  protected _paymentsSum: number = 0;
  protected _importiOverIcons: string[] = ['file_download'];
  protected _tentativiOverIcons: string[] = ['file_download'];

  protected _isLoadingMore: boolean = false;
  protected _pageRef: any = { next: null };
  protected _lastEvtResponse: any;
  protected _chunks: any[] = [];

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
    this.dettaglioRicevuta();
    this.elencoEventi();
  }

  protected dettaglioRicevuta(patch: boolean = false) {
    // /rrp/{idDominio}/{iuv}/{idRicevuta}
    const versione620: boolean = !!(this.json.rpt && this.json.rpt.versioneOggetto && this.json.rpt.versioneOggetto === '6.2.0');
    let idDominio = '';
    let iuv = '';
    let idRicevuta = '';
    if (versione620) {
      idDominio = this.json.rt.dominio.identificativoDominio;
      iuv = this.json.rt.datiPagamento.identificativoUnivocoVersamento;
      idRicevuta = this.json.rt.datiPagamento.CodiceContestoPagamento;
    } else {
      idDominio = this.json.rt.fiscalCode;
      iuv = this.json.rt.creditorReferenceId;
      idRicevuta = this.json.rt.receiptId;
    }
    let _url = `${UtilService.URL_RICEVUTE}/${idDominio}/${iuv}/${idRicevuta}`;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        let _json = _response.body;
        this.mapJsonDetail(_json);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(_json: any) {
    //Riepilogo
    const versione620: boolean = !!(this.json.rpt && this.json.rpt.versioneOggetto && this.json.rpt.versioneOggetto === '6.2.0');
    const _pendenza = _json.pendenza;
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DESCRIZIONE, value: _pendenza.causale }),
      sottotitolo: new Dato({ label: Voce.DEBITORE, value: Dato.concatStrings([_pendenza.soggettoPagatore.anagrafica.toUpperCase(), _pendenza.soggettoPagatore.identificativo.toUpperCase()], ', ') }),
      importo: this.us.currencyFormat(_pendenza.importo),
      stato: UtilService.STATI_PENDENZE[_pendenza.stato],
      extraInfo: []
    });
    if(_pendenza.dominio.ragioneSociale && _pendenza.dominio.idDominio) {
      this.info.extraInfo.push({label: Voce.ENTE_CREDITORE + ': ', value: Dato.concatStrings([_pendenza.dominio.ragioneSociale, _pendenza.dominio.idDominio], ', ')});
    }
    if(_pendenza.tipoPendenza && _pendenza.tipoPendenza.descrizione) {
      this.info.extraInfo.push({label: Voce.TIPI_PENDENZA + ': ', value: _pendenza.tipoPendenza.descrizione});
    }
    if(_pendenza.tassonomiaAvviso) {
      this.info.extraInfo.push({ label: Voce.TASSONOMIA_ENTE+': ', value: _pendenza.tassonomiaAvviso });
    }
    const _iuv = (_pendenza.iuvAvviso)?_pendenza.iuvAvviso:_pendenza.iuvPagamento;
    if(_iuv) {
      this.info.extraInfo.push({label: Voce.IUV + ': ', value: _iuv});
    }
    if(_pendenza.numeroAvviso) {
      this.info.extraInfo.push({ label: Voce.AVVISO+': ', value: _pendenza.numeroAvviso });
    }
    if(_pendenza.idA2A) {
      this.info.extraInfo.push({ label: Voce.ID_A2A+': ', value: _pendenza.idA2A });
    }
    if(_pendenza.idPendenza) {
      this.info.extraInfo.push({ label: Voce.ID_PENDENZA+': ', value: _pendenza.idPendenza });
    }
    if(_json.rt && _json.rt.receiptId) {
      this.info.extraInfo.push({ label: Voce.ID_RICEVUTA+': ', value: _json.rt.receiptId });
    }
    if(_pendenza.dataPagamento) {
      this.info.extraInfo.push({ label: Voce.DATA_PAGAMENTO+': ', value: moment(_pendenza.dataPagamento).format('DD/MM/YYYY') });
    }
    // if(_json.dataValidita) {
    //   this.info.extraInfo.push({ label: Voce.VALIDITA+': ', value: moment(_json.dataValidita).format('DD/MM/YYYY') });
    // }
    // if(_json.dataScadenza) {
    //   this.info.extraInfo.push({ label: Voce.SCADENZA+': ', value: moment(_json.dataScadenza).format('DD/MM/YYYY') });
    // }

    // Dettaglio importi
    if (versione620) {
      if (_json.rt && _json.rt.datiPagamento && _json.rt.datiPagamento.datiSingoloPagamento) {
        this._paymentsSum = _json.rt.datiPagamento.importoTotalePagato;
        this.importi = _json.rt.datiPagamento.datiSingoloPagamento.map(function(item, index) {
          let _std = new NewStandardCollapse();
          _std.motivo = 'force_elenco';
          _std.titolo = new Dato({ value: item.causaleVersamento });
          _std.sottotitolo = new Dato({ value: index });
          _std.elenco = [];
          const lbls: string[] = [];
          const vals: string[] = [];
          if(item.idTransfer) {
            lbls.push(Voce.ID);
            vals.push(index + '');
          }
          _std.sottotitolo = Dato.arraysToDato(lbls, vals, ', ');
          if(_json.rt.enteBeneficiario) {
			_std.elenco.push({ label: Voce.ENTE_CREDITORE, value: `${_json.rt.enteBeneficiario.denominazioneBeneficiario} (${_json.rt.enteBeneficiario.identificativoUnivocoBeneficiario.codiceIdentificativoUnivoco})`, type: 'string' });
          }
          if(item.IBAN) {
            _std.elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.IBAN, type: 'string' });
          }
          if(item.datiSpecificiRiscossione) {
            _std.elenco.push({ label: Voce.TASSONOMIA, value: item.datiSpecificiRiscossione, type: 'string' });
          }
          _std.importo = this.us.currencyFormat(item.singoloImportoPagato);
          _std.stato = item.esitoSingoloPagamento;
          // this._paymentsSum += UtilService.defaultDisplay({ value: item.transferAmount, text: 0 });
          let p = new Parameters();
          _std.item = item;
          // _std.item.idDominio = _pendenza.dominio.idDominio;
          p.jsonP = item;
          p.model = _std;
          p.type = UtilService.NEW_STANDARD_COLLAPSE;
          return p;
        }, this);
        if (_json.rt) {
          if(_json.rt.istitutoAttestante.identificativoUnivocoAttestante.codiceIdentificativoUnivoco) {
            this.datiPsp.push({label: Voce.ID_PSP, value: _json.rt.istitutoAttestante.identificativoUnivocoAttestante.codiceIdentificativoUnivoco});
          }
          if(_json.rt.istitutoAttestante.denominazioneAttestante) {
            this.datiPsp.push({label: Voce.DENOMINAZIONE, value: _json.rt.istitutoAttestante.denominazioneAttestante});
          }
        }
        }
    } else {
      if (_json.rt && _json.rt.transferList && _json.rt.transferList.transfer) {
        this._paymentsSum = _json.rt.paymentAmount;
        this.importi = _json.rt.transferList.transfer.map(function(item) {
          let _std = new NewStandardCollapse();
          _std.motivo = 'force_elenco';
          _std.titolo = new Dato({ value: item.remittanceInformation });
          _std.sottotitolo = new Dato({ value: item.idTransfer });
          _std.elenco = [];
          const lbls: string[] = [];
          const vals: string[] = [];
          if(item.idTransfer) {
            lbls.push(Voce.ID);
            vals.push(item.idTransfer);
          }
          _std.sottotitolo = Dato.arraysToDato(lbls, vals, ', ');
          if(item.fiscalCodePA) {
            _std.elenco.push({ label: Voce.ENTE_CREDITORE, value: item.fiscalCodePA, type: 'string' });
          } else {
          _std.elenco.push({ label: Voce.ENTE_CREDITORE, value: `${_json.rt.companyName} (${_json.rt.fiscalCode})`, type: 'string' });
          }
          if(item.IBAN) {
            _std.elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.IBAN, type: 'string' });
          }
          if(item.transferCategory) {
            _std.elenco.push({ label: Voce.TASSONOMIA, value: item.transferCategory, type: 'string' });
          }
          // Metadata
          if (item.metadata && item.metadata.mapEntry) {
            const _mapEntries = item.metadata.mapEntry.map(x => { return { label: x.key, value: x.value } });
            _std.elenco.push({ label: Voce.METADATA, value: _mapEntries, type: 'metadata' });
          }
          _std.importo = this.us.currencyFormat(item.transferAmount);
          _std.stato = item.stato;
          // this._paymentsSum += UtilService.defaultDisplay({ value: item.transferAmount, text: 0 });
          let p = new Parameters();
          _std.item = item;
          // _std.item.idDominio = _pendenza.dominio.idDominio;
          p.jsonP = item;
          p.model = _std;
          p.type = UtilService.NEW_STANDARD_COLLAPSE;
          return p;
        }, this);
      }
      if (_json.rt) {
        if(_json.rt.idPSP) {
          this.datiPsp.push({label: Voce.ID_PSP, value: _json.rt.idPSP});
        }
        if(_json.rt.PSPCompanyName) {
          this.datiPsp.push({label: Voce.DENOMINAZIONE, value: _json.rt.PSPCompanyName});
        }
        if(_json.rt.idChannel) {
          this.datiPsp.push({label: Voce.CANALE, value: Dato.concatStrings([_json.rt.channelDescription, _json.rt.idChannel], ', ')});
        }
      }
    }
  }

  protected elencoEventi() {
    let _url = UtilService.URL_GIORNALE_EVENTI;
    // eventi relativi alla transazione con la ricevuta
    const versione620: boolean = !!(this.json.rpt && this.json.rpt.versioneOggetto && this.json.rpt.versioneOggetto === '6.2.0');
    let idDominio = '';
    let iuv = '';
    let idRicevuta = '';
    if (versione620) {
      idDominio = this.json.rt.dominio.identificativoDominio;
      iuv = this.json.rt.datiPagamento.identificativoUnivocoVersamento;
      idRicevuta = this.json.rt.datiPagamento.CodiceContestoPagamento;
    } else {
      idDominio = this.json.rt.fiscalCode;
      iuv = this.json.rt.creditorReferenceId;
      idRicevuta = this.json.rt.receiptId;
    }
    let _query = 'idDominio='+idDominio+'&iuv='+iuv; //+'&ccp='+idRicevuta
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
              _stdTCC.elenco.push({ label: Voce.TIPO_VERSAMENTO, value: item.datiPagoPA.tipoVersamento });
            }
            if(item.datiPagoPA.modelloPagamento) {
              _stdTCC.elenco.push({ label: Voce.MODELLO_PAGAMENTO, value: item.datiPagoPA.modelloPagamento });
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

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  esclusioneNotifiche() { }

  exportData() {}

  stampaRicevuta() {
    const versione620: boolean = !!(this.json.rpt && this.json.rpt.versioneOggetto && this.json.rpt.versioneOggetto === '6.2.0');
    let idDominio = '';
    let iuv = '';
    let idRicevuta = '';
    if (versione620) {
      idDominio = this.json.rt.dominio.identificativoDominio;
      iuv = this.json.rt.datiPagamento.identificativoUnivocoVersamento;
      idRicevuta = this.json.rt.datiPagamento.CodiceContestoPagamento;
    } else {
      idDominio = this.json.rt.fiscalCode;
      iuv = this.json.rt.creditorReferenceId;
      idRicevuta = this.json.rt.receiptId;
    }
    let _url = `${UtilService.URL_RICEVUTE}/${idDominio}/${iuv}/${idRicevuta}/rt`;

    let name = `${idDominio}_${iuv}_${idRicevuta}`;

    this.gps.pdf(_url).subscribe(
      (response) => {
        let _cd = response.headers.get("content-disposition");
        let _re = /(?:filename=['"](.*(\.pdf))['"])/gm;
        let _results = _re.exec(_cd);
        if(_results && _results.length == 3) {
          name = _results[1].split('.')[0];
        }
        // name = _cd.filename || name;
        this.gps.updateSpinner(false);
        this.us.savePdf(response.body, name);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      }
    );
  }
}
