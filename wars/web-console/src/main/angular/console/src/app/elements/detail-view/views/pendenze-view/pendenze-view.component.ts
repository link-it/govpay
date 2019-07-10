import { AfterViewInit, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';

import { Riepilogo } from '../../../../classes/view/riepilogo';
import { Dato } from '../../../../classes/view/dato';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { IExport } from '../../../../classes/interfaces/IExport';

import * as moment from 'moment';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { CronoCode } from '../../../../classes/view/crono-code';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';
import { TwoCols } from '../../../../classes/view/two-cols';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-pendenze',
  templateUrl: './pendenze-view.component.html',
  styleUrls: ['./pendenze-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PendenzeViewComponent implements IModalDialog, IExport, OnInit, AfterViewInit {

  @Input() tentativi = [];
  @Input() importi = [];
  @Input() informazioni = [];
  @Input() eventi = [];

  @Input() json: any;
  @Input() modified: boolean = false;


  protected NOTA = UtilService.NOTA;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected info: Riepilogo;
  protected _paymentsSum: number = 0;
  protected _importiOverIcons: string[] = ['file_download'];
  protected _tentativiOverIcons: string[] = ['file_download'];

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
    this.dettaglioPendenza();
    this.elencoTentativi();
    this.elencoEventi();
  }

  ngAfterViewInit() {
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
      titolo: new Dato({ label: Voce.ENTE_CREDITORE, value: Dato.concatStrings([_json.dominio.ragioneSociale, _json.dominio.idDominio], ', ') }),
      sottotitolo: new Dato({ label: Voce.DEBITORE, value: Dato.concatStrings([_json.soggettoPagatore.anagrafica, _json.soggettoPagatore.identificativo], ', ') }),
      importo: this.us.currencyFormat(_json.importo),
      stato: UtilService.STATI_PENDENZE[_json.stato],
      extraInfo: []
    });
    const _iuv = (_json.iuvAvviso)?_json.iuvAvviso:_json.iuvPagamento;
    this.info.extraInfo.push({ label: Voce.IUV+': ', value: _iuv });
    if(_json.numeroAvviso) {
      this.info.extraInfo.push({ label: Voce.AVVISO+': ', value: _json.numeroAvviso });
    }
    if(_json.dataScadenza) {
      this.info.extraInfo.push({ label: Voce.SCADENZA+': ', value: moment(_json.dataScadenza).format('DD/MM/YYYY') });
    }
    if(_json.dataValidita) {
      this.info.extraInfo.push({ label: Voce.VALIDITA+': ', value: moment(_json.dataValidita).format('DD/MM/YYYY') });
    }
    if(_json.idPendenza) {
      this.info.extraInfo.push({ label: Voce.ID_PENDENZA+': ', value: _json.idPendenza });
    }
    if(_json.idA2A) {
      this.info.extraInfo.push({ label: Voce.ID_A2A+': ', value: _json.idA2A });
    }
    if(_json.tassonomiaAvviso) {
      this.info.extraInfo.push({ label: Voce.TASSONOMIA_AVVISO+': ', value: _json.tassonomiaAvviso });
    }
    if(_json.tassonomia) {
      this.info.extraInfo.push({ label: Voce.TASSONOMIA+': ', value: _json.tassonomia });
    }
    if(_json.dataCaricamento) {
      this.info.extraInfo.push({ label: Voce.DATA_CARICAMENTO+': ', value: moment(_json.dataCaricamento).format('DD/MM/YYYY') });
    }
    if(_json.dataUltimoAggiornamento) {
      this.info.extraInfo.push({ label: Voce.DATA_ULTIMO_AGGIORNAMENTO+': ', value: moment(_json.dataUltimoAggiornamento).format('DD/MM/YYYY') });
    }
    //Dettaglio importi
    this._paymentsSum = 0;
    this.importi = _json.voci.map(function(item) {
      let _std = new StandardCollapse();
      _std.titolo = new Dato({ value: item.descrizione });
      _std.elenco = [];
      if(item.tipoBollo) {
        _std.sottotitolo = Dato.arraysToDato([Voce.ID_PENDENZA, Voce.ID_BOLLO], [item.idVocePendenza, item.tipoBollo], ', ');
      } else {
        _std.sottotitolo = new Dato({ label: Voce.ID_PENDENZA+': ', value: item.idVocePendenza });
        _std.elenco.push({ label: Voce.CONTABILITA, value: Dato.concatStrings([ item.tipoContabilita, item.codiceContabilita ], ', ') });
        _std.elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.ibanAccredito });
      }
      _std.importo = this.us.currencyFormat(item.importo);
      _std.stato = item.stato;
      this._paymentsSum += UtilService.defaultDisplay({ value: item.importo, text: 0 });
      let p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      p.type = UtilService.STANDARD_COLLAPSE;
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
  }

  protected elencoTentativi() {
    this.gps.getDataService(this.json.rpp).subscribe(function (_response) {
        let _body = _response.body;
        this.tentativi = _body['risultati'].map(function(item) {
          let _date = item.rpt.dataOraMessaggioRichiesta?moment(item.rpt.dataOraMessaggioRichiesta).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
          let _subtitle = Dato.concatStrings([ Voce.DATA+': '+_date, Voce.CCP+': '+item.rpt.datiVersamento.codiceContestoPagamento ], ', ');
          let _std = new StandardCollapse();
          let _map = this._mapStato(item);
          _std.titolo = new Dato({ label: '', value: (item.rt && item.rt.istitutoAttestante)?item.rt.istitutoAttestante.denominazioneAttestante:Voce.NO_PSP });
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
    this.gps.getDataService(_url, _query).subscribe(function (_response) {
        let _body = _response.body;
        this.eventi = _body['risultati'].map(function(item) {
          const _stdTC: TwoCols = new TwoCols();
          const _dataOraEventi = item.dataEvento?moment(item.dataEvento).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
          const _riferimento = this.us.mapRiferimentoGiornale(item);
          _stdTC.titolo = new Dato({ label: this.us.mappaturaTipoEvento(item.tipoEvento) });
          _stdTC.sottotitolo = new Dato({ label: _riferimento });
          _stdTC.stato = item.esito;
          _stdTC.data = _dataOraEventi;
          let p = new Parameters();
          p.model = _stdTC;
          p.type = UtilService.TWO_COLS;
          return p;
        }, this);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _mapStato(item: any): any {
    let _map: any = { stato: '', motivo: '' };
    switch (item.stato) {
      case 'RT_ACCETTATA_PA':
        _map.stato = (item.rt)?UtilService.STATI_ESITO_PAGAMENTO[item.rt.datiPagamento.codiceEsitoPagamento]:'n/a';
        break;
      case 'RPT_RIFIUTATA_NODO':
      case 'RPT_RIFIUTATA_PSP':
      case 'RPT_ERRORE_INVIO_A_PSP':
        _map.stato = UtilService.STATI_RPP.FALLITO;
        _map.motivo = item.dettaglioStato+' - stato: '+item.stato;
        break;
      case 'RT_RIFIUTATA_PA':
      case 'RT_ESITO_SCONOSCIUTO_PA':
        _map.stato = UtilService.STATI_RPP.ANOMALO;
        _map.motivo = item.dettaglioStato+' - stato: '+item.stato;
        break;
      default:
        _map.stato = UtilService.STATI_RPP.IN_CORSO;
    }
    return _map;
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
        case this.NOTA:
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
      let _method = null;
      let _ref = encodeURIComponent(this.json.idA2A)+'/'+encodeURIComponent(this.json.idPendenza);
      let _service = UtilService.URL_PENDENZE+'/'+_ref;
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

  esclusioneNotifiche() { }

  exportData() {
    this.gps.updateSpinner(true);
    let _folder = '';
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];
    try {
      //TODO: Root Pdf pendenza, servizio non attivo
      // * urls.push(UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza);
      // names.push('Dati_pendenza.pdf');
      // contents.push('application/pdf');
      // types.push('blob'); *

      //Pdf Avviso di pagamento
      if(this.json.numeroAvviso) {
        if (folders.indexOf(UtilService.ROOT_ZIP_FOLDER) == -1) {
          folders.push(UtilService.ROOT_ZIP_FOLDER);
        }
        urls.push(UtilService.URL_AVVISI+'/'+encodeURIComponent(this.json.dominio.idDominio)+'/'+encodeURIComponent(this.json.numeroAvviso));
        contents.push('application/pdf');
        names.push(this.json.dominio.idDominio + '_' + this.json.numeroAvviso + '.pdf' + UtilService.ROOT_ZIP_FOLDER);
        types.push('blob');
      }
      this.tentativi.forEach((el) => {
        // /rpp/{idDominio}/{iuv}/{ccp}/rpt
        // /rpp/{idDominio}/{iuv}/{ccp}/rt
        let item = el.jsonP;
        _folder = encodeURIComponent(item.rpt.dominio.identificativoDominio)+'_'+encodeURIComponent(item.rpt.datiVersamento.identificativoUnivocoVersamento)+'_'+encodeURIComponent(item.rpt.datiVersamento.codiceContestoPagamento);
        if (folders.indexOf(_folder) == -1) {
          folders.push(_folder);
        }
        urls.push('/rpp/'+encodeURIComponent(item.rpt.dominio.identificativoDominio)+'/'+encodeURIComponent(item.rpt.datiVersamento.identificativoUnivocoVersamento)+'/'+encodeURIComponent(item.rpt.datiVersamento.codiceContestoPagamento)+'/rpt');
        names.push('Rpt.xml'+_folder);
        contents.push('application/xml');
        types.push('text');
        if(item.rt) {
          urls.push('/rpp/'+encodeURIComponent(item.rt.dominio.identificativoDominio)+'/'+encodeURIComponent(item.rt.datiPagamento.identificativoUnivocoVersamento)+'/'+encodeURIComponent(item.rt.datiPagamento.CodiceContestoPagamento)+'/rt');
          contents.push('application/xml');
          names.push('Rt.xml'+_folder);
          types.push('text');
          urls.push('/rpp/'+encodeURIComponent(item.rt.dominio.identificativoDominio)+'/'+encodeURIComponent(item.rt.datiPagamento.identificativoUnivocoVersamento)+'/'+encodeURIComponent(item.rt.datiPagamento.CodiceContestoPagamento)+'/rt');
          contents.push('application/pdf');
          names.push('Rt.pdf'+_folder);
          types.push('blob');
          urls.push(UtilService.URL_GIORNALE_EVENTI+'?idA2A='+encodeURIComponent(this.json.idA2A)+'&idPendenza='+encodeURIComponent(this.json.idPendenza));
          contents.push('application/json');
          names.push('Eventi.csv'+_folder);
          types.push('json');
        }
      }, this);
      if (this.tentativi.length == 0 && this.eventi.length != 0) {
        if (folders.indexOf(UtilService.ROOT_ZIP_FOLDER) == -1) {
          folders.push(UtilService.ROOT_ZIP_FOLDER);
        }
        urls.push(UtilService.URL_GIORNALE_EVENTI+'?idA2A='+encodeURIComponent(this.json.idA2A)+'&idPendenza='+encodeURIComponent(this.json.idPendenza));
        contents.push('application/json');
        names.push('Eventi.csv' + UtilService.ROOT_ZIP_FOLDER);
        types.push('json');
      }
    } catch (error) {
      this.gps.updateSpinner(false);
      this.us.alert('Si è verificato un errore non previsto durante il recupero delle informazioni.', true);
    }
    if(urls.length != 0) {
      this.gps.multiExportService(urls, contents, types).subscribe(function (_response) {
          this.saveFile(_response, { folders: folders, names: names }, '.zip');
        }.bind(this),
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    } else {
      this.gps.updateSpinner(false);
      this.us.alert('Nessuna informazione disponibile per eseguire lo scaricamento del resoconto.', true);
    }
  }

  saveFile(data: any, structure: any, ext: string) {
    let root = 'Pendenza_' + this.json.idA2A + '_' + this.json.idPendenza;
    let zipname = root + ext;
    let zip = new JSZip();
    let zroot = zip.folder(root);
    //TODO: Abilitare appena il servizio Root pdf pendenza è attivo
    // zroot.file(structure.names[0], data[0].body);
    structure.folders.forEach((folder) => {
      let zfolder;
      if(folder !== UtilService.ROOT_ZIP_FOLDER) {
        zfolder = zroot.folder(folder);
      }
      data.forEach((file, ref) => {
        let o;
        if (folder != UtilService.ROOT_ZIP_FOLDER) {
          if (structure.names[ref].indexOf(folder) != -1) {
            //folder
            o = this._elaborate(structure.names[ref].split(folder)[0], file);
            zfolder.file(o['name'], o['zdata']);
          }
        } else {
          if(structure.names[ref].indexOf(UtilService.ROOT_ZIP_FOLDER) != -1) {
            //root
            o = this._elaborate(structure.names[ref].split(folder)[0], file);
            zroot.file(o['name'], o['zdata']);
          }
        }
      });
    });
    zip.generateAsync({type: 'blob'}).then(function (zipData) {
      FileSaver(zipData, zipname);
      this.gps.updateSpinner(false);
    }.bind(this));
  }

  jsonToCsv(name: string, jsonData: any): string {
    let _csv: string = '';
    switch(name) {
      case 'Eventi.csv':
        let _jsonArray: any[] = jsonData.risultati;
        let _keys = [];
        _jsonArray.forEach((_json, index) => {
          if(index == 0) {
            _keys = Object.keys(_json);
            let _mappedKeys = _keys.map((key) => {
              return '"'+key+'"';
            });
            _csv = _mappedKeys.join(', ')+'\r\n';
          }
          let row: string[] = [];
          _keys.forEach((_key) => {
             row.push('"'+(_json[_key] || 'n/a')+'"');
          });
          _csv += row.join(', ')+'\r\n';
        });
        break;
    }

    return _csv;
  }

  /**
   * Elaborate structure
   * @param {string} name
   * @param {any} file
   * @returns {any}
   * @private
   */
  protected _elaborate(name: string, file: any): any {
    let zdata = file.body;
    if(name.indexOf('csv') != -1) {
      zdata = this.jsonToCsv(name, file.body);
    }
    return { zdata: zdata, name: name };

  }
}
