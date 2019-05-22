import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { IExport } from '../../../../classes/interfaces/IExport';
import { Dato } from '../../../../classes/view/dato';
import { Voce } from '../../../../services/voce.service';
import { Parameters } from '../../../../classes/parameters';

import * as moment from 'moment';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-tracciati-view',
  templateUrl: './tracciati-view.component.html',
  styleUrls: ['./tracciati-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TracciatiViewComponent implements IModalDialog, IExport, OnInit {

  @Input() operazioni = [];
  @Input() informazioni = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected _isLoading: boolean = false;
  protected _lastResponse: any;
  protected info: Riepilogo;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioTracciato();
    this.operazioniTracciato();
  }

  protected dettaglioTracciato(patch: boolean = false) {
    let _url = UtilService.URL_TRACCIATI+'/'+this.json.id;
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

  protected operazioniTracciato(service?: string, concat?: boolean) {
    let _url = (service || UtilService.URL_TRACCIATI+'/'+this.json.id+'/'+UtilService.OPERAZIONI_TRACCIATO);
    concat = concat || false;
    this._isLoading = true;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        let _json = _response.body;
        this._lastResponse = JSON.parse(JSON.stringify(_response.body));
        this.mapJsonDetail(_json, true, concat);
        this._isLoading = false;
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this._isLoading = false;
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(_json: any, _operazioni: boolean = false, concat: boolean = false) {
    if(!_operazioni) {
      //Riepilogo
      this.json = _json;
      let _tmpDC = _json.dataOraCaricamento?moment(_json.dataOraCaricamento).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
      this.info = new Riepilogo({
        titolo: new Dato({ label: Voce.NOME,  value: _json.nomeFile }),
        sottotitolo: new Dato({ label: Voce.DATA_CARICAMENTO,  value: _tmpDC }),
        stato: UtilService.STATI_TRACCIATO[_json.stato],
        extraInfo: []
      });
      if(_json.numeroOperazioniEseguite !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_ESEGUITE+': ', value: _json.numeroOperazioniEseguite });
      }
      if(_json.numeroOperazioniFallite !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_FALLITE+': ', value: _json.numeroOperazioniFallite });
      }
      if(_json.numeroOperazioniTotali !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_TOTALI+': ', value: _json.numeroOperazioniTotali });
      }
      if(_json.operatoreMittente) {
        this.info.extraInfo.push({ label: Voce.OPERATORE_MITTENTE+': ', value: _json.operatoreMittente });
      }
      if(_json.dataOraUltimoAggiornamento) {
        this.info.extraInfo.push({ label: Voce.DATA_ULTIMO_AGGIORNAMENTO+': ', value: moment(_json.dataOraUltimoAggiornamento).format('DD/MM/YYYY') });
      }
    } else {
      //Dettaglio operazioni tracciato
      const _temp = _json.risultati.map(function(item) {
        let _std = new StandardCollapse();
        _std.titolo = new Dato({ value: item.identificativoPendenza });
        _std.sottotitolo = new Dato({ value: UtilService.TIPO_OPERAZIONI_TRACCIATO[item.tipoOperazione].LABEL });
        _std.stato = UtilService.STATI_TRACCIATO[item.stato];
        _std.elenco = [];
        if(!(UtilService.STATI_TRACCIATO[item.stato] == UtilService.STATI_TRACCIATO.ESEGUITO && item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.DEL.KEY)) {
          if(item.applicazione) {
            _std.elenco.push({ label: Voce.APPLICAZIONE, value: item.applicazione });
          }
          if(item.identificativoPendenza) {
            _std.elenco.push({ label: Voce.ID_PENDENZA, value: item.identificativoPendenza });
          }
          if(item.enteCreditore && item.enteCreditore.ragioneSociale) {
            _std.elenco.push({ label: Voce.ENTE_CREDITORE, value: item.enteCreditore.ragioneSociale });
          }
          if(item.soggettoPagatore) {
            _std.elenco.push({ label: Voce.DEBITORE, value: Dato.concatStrings([ item.soggettoPagatore.anagrafica, item.soggettoPagatore.identificativo], ', ') });
          }
          //Tipo operazione
          if(item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.DEL.KEY) {
            //DEL
            if(UtilService.STATI_TRACCIATO[item.stato] != UtilService.STATI_TRACCIATO.ESEGUITO) {
              if(item.risposta && item.risposta.descrizioneEsito) {
                _std.elenco.push({ label: Voce.DESCRIZIONE, value: item.risposta.descrizioneEsito });
              }
            }
          } else {
            //!DEL
            if(UtilService.STATI_TRACCIATO[item.stato] != UtilService.STATI_TRACCIATO.ESEGUITO) {
              if(item.descrizioneStato) {
                _std.elenco.push({ label: Voce.DESCRIZIONE, value: item.descrizioneStato });
              }
            }
          }
          //ADD
          if(item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.ADD.KEY && UtilService.STATI_TRACCIATO[item.stato] == UtilService.STATI_TRACCIATO.ESEGUITO) {
            if(item.numeroAvviso) {
              _std.elenco.push({ label: Voce.AVVISO, value: item.numeroAvviso });
            }
          }
        }
        let p = new Parameters();
        p.jsonP = item;
        p.model = _std;
        p.type = UtilService.STANDARD_COLLAPSE;
        return p;
      }, this);
      this.operazioni = concat?this.operazioni.concat(_temp):_temp;
    }
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.causale:null });
  }

  refresh(mb: ModalBehavior) {}

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = UtilService.URL_PENDENZE + '/' + UtilService.TRACCIATI;

    let _data;
    let _autoHeaders = true;
    if (mb.info.viewModel.json) {
      _autoHeaders = false;
      _data = mb.info.viewModel.json;
    } else {
      _data = new FormData();
      _data.append('file', mb.info.viewModel.file);
    }

    this.gps.saveData(_service, _data, null, UtilService.METHODS.POST, _autoHeaders).subscribe(
      () => {
        this.gps.updateSpinner(false);
        responseService.next(true);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  exportData() {
    this.gps.updateSpinner(true);
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];
    urls.push(UtilService.URL_TRACCIATI+'/'+this.json.id+'/stampe');
    names.push(this.json.nomeFile+'.zip');
    contents.push('application/zip');
    types.push('blob');

    urls.push(UtilService.URL_TRACCIATI+'/'+this.json.id);
    names.push(this.json.nomeFile+'.json');
    contents.push('application/json');
    types.push('text');

    urls.push(UtilService.URL_TRACCIATI+'/'+this.json.id+'/esito');
    contents.push('application/json');
    names.push(this.json.nomeFile+'_esito.json');
    types.push('text');

    this.gps.multiExportService(urls, contents, types).subscribe(function (_response) {
        this.saveFile(_response, { folders: folders, names: names }, '.zip');
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  saveFile(data: any, structure: any, ext: string) {
    try {
    let root = this.json.nomeFile+'_'+moment().format('DDMMYYYY_HHmmss');
    let zipname = root + ext;
    let zip = new JSZip();
      data.forEach((file, ref) => {
        let _name = structure.names[ref];
        let zdata = file.body;
        if(_name.indexOf('.json') != -1) {
          let _json = JSON.parse(zdata);
          if(_json.contenuto) {
            zdata = JSON.stringify(_json.contenuto);
          }
          if(_json.esito) {
            zdata = JSON.stringify(_json.esito);
          }
        } else {
          let _cd = file.headers.get("content-disposition");
          let _re = /(?:filename=['"](.*\.zip)['"])/gm;
          let _results = _re.exec(_cd);
          if(_results && _results.length == 2) {
            _name = _results[1];
          }
        }
        zip.file(_name, zdata);
      });
      zip.generateAsync({type: 'blob'}).then(function (zipData) {
        FileSaver(zipData, zipname);
        this.gps.updateSpinner(false);
      }.bind(this));
    } catch (e) {
      this.gps.updateSpinner(false);
      this.us.alert('Si è verificato un errore non previsto durante la creazione del file.');
    }
  }

  /**
   * Get last result data
   * @returns {any}
   */
  getLastResult(): any {
    return this._lastResponse;
  }

  /**
   * _loadMoreData: Infinite scrolling
   * @private
   */
  protected _loadMoreData() {
    let _results = this.getLastResult();
    if(_results && _results['prossimiRisultati']) {
      this.operazioniTracciato(_results['prossimiRisultati'], true);
    }
  }

}
