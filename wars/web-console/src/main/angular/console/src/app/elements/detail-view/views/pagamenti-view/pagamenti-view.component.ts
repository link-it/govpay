import { AfterViewInit, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { GovpayService } from '../../../../services/govpay.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';

import * as moment from 'moment';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';
import { IExport } from '../../../../classes/interfaces/IExport';
import { CronoCode } from '../../../../classes/view/crono-code';
import { TwoColsCollapse } from '../../../../classes/view/two-cols-collapse';
import { HttpResponse } from '@angular/common/http';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-pagamenti-view',
  templateUrl: './pagamenti-view.component.html',
  styleUrls: ['./pagamenti-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PagamentiViewComponent implements IModalDialog, IExport, OnInit, AfterViewInit {

  @Input() pagamenti = [];
  @Input() informazioni = [];
  @Input() eventi = [];

  @Input() json: any;
  @Input() modified: boolean = false;


  protected NOTA = UtilService.NOTA;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected info: Riepilogo;
  protected _paymentsSum: number = 0;
  protected _importiOverIcons: string[] = ['file_download'];

  protected _isLoadingMore: boolean = false;
  protected _pageRef: any = { next: null };
  protected _lastEvtResponse: any;
  protected _chunks: any[] = [];

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioPagamento();
    this.elencoEventi();
  }

  ngAfterViewInit() {
  }

  protected dettaglioPagamento() {
    //console.log('pagamento/detail', this.json);
    let _url = UtilService.URL_PAGAMENTI+'/'+this.json.id;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail(this.json);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected elencoEventi() {
    let _url = UtilService.URL_GIORNALE_EVENTI;
    let _query = 'idPagamento='+this.json.id;
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
          _stdTCC.titolo = new Dato({ label: this.us.mappaturaTipoEvento(item.tipoEvento) });
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

  protected mapJsonDetail(_json: any) {
    //Riepilogo
    let _istituto;
    let _soggettoVersante: string = Voce.UNDEFINED;
    if(_json.rpp && _json.rpp.length !== 0) {
      const _rpp0: any = _json.rpp[0];
      const versione620: boolean = !!(_rpp0.rpt && _rpp0.rpt.versioneOggetto && _rpp0.rpt.versioneOggetto === '6.2.0');
      if (versione620) {
        _istituto = (_rpp0.rt && _rpp0.rt.istitutoAttestante)?_rpp0.rt.istitutoAttestante.denominazioneAttestante:'';
        if(_json.soggettoVersante){
          _soggettoVersante = Dato.concatStrings([ _json.soggettoVersante.anagrafica, _json.soggettoVersante.identificativo ], ', ');
        }
      } else {
        const soggetto: string[] = [];
        if (_rpp0.rt && _rpp0.rt.receipt) {
          _istituto = (_rpp0.rt.receipt.PSPCompanyName || '');
          if (_rpp0.rt && _rpp0.rt.receipt && _rpp0.rt.receipt.payer) {
            if (_rpp0.rt.receipt.payer.fullName) {
              soggetto.push(_rpp0.rt.receipt.payer.fullName);
            }
            if (_rpp0.rt.receipt.payer.uniqueIdentifier && _rpp0.rt.receipt.payer.uniqueIdentifier.entityUniqueIdentifierValue) {
              soggetto.push(_rpp0.rt.receipt.payer.uniqueIdentifier.entityUniqueIdentifierValue);
            }
          }
        }
        if (soggetto.length !== 0) {
          _soggettoVersante = Dato.concatStrings(soggetto, ', ');
        }
      }
    }
    const _st = new Dato({ label: Voce.SOGGETTO_VERSANTE, value: _soggettoVersante });
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DATA, value: _json.dataRichiestaPagamento?moment(_json.dataRichiestaPagamento).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE }),
      sottotitolo: _st,
      importo: this.us.currencyFormat(_json.importo),
      stato: UtilService.STATI_PAGAMENTO[_json.stato],
      extraInfo: []
    });
    if(_istituto) {
      this.info.extraInfo.push({ label: Voce.ISTITUTO+': ', value: _istituto })
    }
    if(_json.modello) {
      this.info.extraInfo.push({ label: Voce.TIPO+': ', value: _json.modello })
    }
    if(_json.id) {
      this.info.extraInfo.push({ label: Voce.ID_SESSIONE+': ', value: _json.id });
    }
    this._paymentsSum = 0;
    if(_json.rpp) {
      this.pagamenti = _json.rpp.map(function(item) {
        const versione620: boolean = !!(item.rpt && item.rpt.versioneOggetto && item.rpt.versioneOggetto === '6.2.0');
        const ms: any = UtilService.MapStato(item, versione620);
        const stStrings: string[] = [Voce.ENTE_CREDITORE+': '+item.pendenza.dominio.ragioneSociale];
        let importoRpp: string = '0';
        if (versione620) {
          if (item.rpt.datiVersamento) {
            if (item.rpt.datiVersamento.identificativoUnivocoVersamento) {
              stStrings.push(Voce.IUV+': '+item.rpt.datiVersamento.identificativoUnivocoVersamento);
            }
            if (item.rpt.datiVersamento.identificativoUnivocoVersamento) {
              importoRpp = item.rpt.datiVersamento.importoTotaleDaVersare;
            }
          }
        } else {
          if (item.rpt && item.rpt.data) {
            if (item.rpt.data.creditorReferenceId) {
              stStrings.push(Voce.IUV+': '+item.rpt.data.creditorReferenceId);
            }
          }
          if (item.rt && item.rt.receipt) {
            importoRpp = item.rt.receipt.paymentAmount;
          }
        }
        let _st = new Dato({ value: Dato.concatStrings(stStrings, ', ') });
        let _std = new StandardCollapse();
        _std.titolo = new Dato({ value: item.pendenza.causale });
        _std.sottotitolo = _st;
        _std.stato = ms.stato;
        _std.importo = this.us.currencyFormat(parseFloat(importoRpp));
        this._paymentsSum += UtilService.defaultDisplay({ value: parseFloat(importoRpp), text: 0 });
        // Disattivato collapse (_std.motivo)
        // if (ms.dettaglio) {
        //   _std.motivo = ms.dettaglio;
        // }
        let p = new Parameters();
        p.model = _std;
        p.type = UtilService.STANDARD_COLLAPSE;
        p.jsonP = item;
        return p;
      }, this);
    }
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
  }

  protected _addEdit(type: string, patchOperation: string, mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.closure = this.refresh.bind(this);
    switch(type) {
      case this.NOTA:
        _mb.async_callback = this.save.bind(this);
        _mb.operation = patchOperation;
        _mb.info.dialogTitle = 'Nuova nota';
        _mb.info.viewModel = this.json;
        _mb.info.templateName = this.NOTA;
        break;
    }
    UtilService.dialogBehavior.next(_mb);
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
    return UtilService.defaultDisplay({ value: this.json?this.json.nome:null });
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      switch(mb.info.templateName) {
        case this.NOTA:
          this.json = mb.info.viewModel;
          this.mapJsonDetail(this.json);
          break;
      }
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _method = null;
      let _ref = UtilService.EncodeURIComponent(this.json.id);
      let _service = UtilService.URL_PAGAMENTI+'/'+_ref;
      switch(mb.info.templateName) {
        case UtilService.NOTA:
          _method = UtilService.METHODS.PATCH;
          _json = [{ op: mb.operation, path: '/'+UtilService.NOTA, value: mb.info.viewModel }];
          break;
      }
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

  esclusioneNotifiche() {
    let _query = null;
    let _ref = UtilService.EncodeURIComponent(this.json.id);
    let _service = UtilService.URL_PAGAMENTI+'/'+_ref;
    let _method = UtilService.METHODS.PATCH;
    let _json = [{ op: UtilService.PATCH_METHODS.REPLACE, path: '/'+UtilService.VERIFICATO, value: true }];
    this.modified = false;
    this.gps.saveData(_service, _json, _query, _method).subscribe(
    (response) => {
      this.json = response.body;
      this.modified = true;
      this.mapJsonDetail(this.json);
      this.gps.updateSpinner(false);
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  exportData() {
    this.gps.updateSpinner(true);
    const folders: string[] = [];
    const chunk: any[] = [];
    try {
      this.pagamenti.forEach((el) => {
        // /rpp/{idDominio}/{iuv}/{ccp}/rpt
        // /rpp/{idDominio}/{iuv}/{ccp}/rt
        const item = el.jsonP;
        const ref: any = UtilService.ExportMapLoopCfg(item);
        if (ref.idd && ref.iuv) {
          const _folder = (UtilService.ExportMapChunkLoopCfg('folder', ref).url || '');
          if (folders.indexOf(_folder) == -1) {
            folders.push(_folder);
          }
          if (ref.ccp && _folder) {
            chunk.push(UtilService.ExportMapChunkLoopCfg('Rpt.xml', ref, _folder));
            chunk.push(UtilService.ExportMapChunkLoopCfg('Eventi.csv', ref, _folder));
          }
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
      this.us.generateStructuredZip(dataCalls, structure, 'Pagamento_' + this.json.id);
    }, 1000);
  }
}
