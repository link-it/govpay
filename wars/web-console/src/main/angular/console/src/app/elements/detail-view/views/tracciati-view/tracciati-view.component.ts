import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { IExport } from '../../../../classes/interfaces/IExport';
import { Dato } from '../../../../classes/view/dato';
import { Voce } from '../../../../services/voce.service';
import { Parameters } from '../../../../classes/parameters';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpHeaders } from '@angular/common/http';
import { TwoColsCollapse } from '../../../../classes/view/two-cols-collapse';
import { SocketNotification } from '../../../../classes/socket-notification';
import * as moment from 'moment';

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
        this.modified = true;
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
      let _st: Dato;
      if(_json.dominio && _json.dominio.ragioneSociale !== null) {
        _st = new Dato({ label: Voce.ENTE_CREDITORE+': ', value: _json.dominio.ragioneSociale });
      }
      const _pda = this.us.pdaTracciato(_json);
      this.info = new Riepilogo({
        titolo: new Dato({ label: Voce.NOME,  value: _json.nomeFile }),
        stato: UtilService.STATI_TRACCIATO[_json.stato],
        socketNotification: (_pda !== undefined)?new SocketNotification({
          notifier: this._mapSocketNotifier.bind(this),
          URI: UtilService.URL_TRACCIATI,
          data: _json,
          timeout: UtilService.PREFERENCES['POLLING_TRACCIATI'] || null
        }):null,
        avanzamento: _pda,
        extraInfo: []
      });
      this.info.sottotitolo = _st || null;
      this._loadExtraInfo(_json);
    } else {
      //Dettaglio operazioni tracciato
      const _temp = _json.risultati.map(function(item) {
        const _titolo: string[] = [];
        if (item.risposta && item.risposta.idA2A) {
          _titolo.push(item.risposta.idA2A);
        }
        if (item.identificativoPendenza) {
          _titolo.push(item.identificativoPendenza);
        }
        let _stdTCC = new TwoColsCollapse();
        _stdTCC.gtTextUL = _titolo.join('/');
        _stdTCC.gtTextBL = UtilService.TIPO_OPERAZIONI_TRACCIATO[item.tipoOperazione].LABEL;
        _stdTCC.gtTextUR = UtilService.STATI_TRACCIATO[item.stato];
        _stdTCC.gtTextBR = item.numero?`#${item.numero}`:'';
        _stdTCC.generalTemplate = true;
        _stdTCC.elenco = [];
        if(!(UtilService.STATI_TRACCIATO[item.stato] == UtilService.STATI_TRACCIATO.ESEGUITO && item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.DEL.KEY)) {
          if(item.applicazione) {
            _stdTCC.elenco.push({ label: Voce.APPLICAZIONE, value: item.applicazione });
          }
          if(item.identificativoPendenza) {
            _stdTCC.elenco.push({ label: Voce.ID_PENDENZA, value: item.identificativoPendenza });
          }
          if(item.enteCreditore && item.enteCreditore.ragioneSociale) {
            _stdTCC.elenco.push({ label: Voce.ENTE_CREDITORE, value: item.enteCreditore.ragioneSociale });
          }
          if(item.soggettoPagatore) {
            _stdTCC.elenco.push({ label: Voce.DEBITORE, value: Dato.concatStrings([ item.soggettoPagatore.anagrafica, item.soggettoPagatore.identificativo], ', ') });
          }
          //Tipo operazione
          if(item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.DEL.KEY) {
            //DEL
            if(UtilService.STATI_TRACCIATO[item.stato] != UtilService.STATI_TRACCIATO.ESEGUITO) {
              if(item.risposta && item.risposta.descrizioneEsito) {
                _stdTCC.elenco.push({ label: Voce.DESCRIZIONE, value: item.risposta.descrizioneEsito });
              }
            }
          } else {
            //!DEL
            if(UtilService.STATI_TRACCIATO[item.stato] != UtilService.STATI_TRACCIATO.ESEGUITO) {
              if(item.descrizioneStato) {
                _stdTCC.elenco.push({ label: Voce.DESCRIZIONE, value: item.descrizioneStato });
              }
            }
          }
          //ADD
          if(item.tipoOperazione == UtilService.TIPO_OPERAZIONI_TRACCIATO.ADD.KEY && UtilService.STATI_TRACCIATO[item.stato] == UtilService.STATI_TRACCIATO.ESEGUITO) {
            if(item.numeroAvviso) {
              _stdTCC.elenco.push({ label: Voce.AVVISO, value: item.numeroAvviso });
            }
          }
        }
        let p = new Parameters();
        p.jsonP = item;
        p.model = _stdTCC;
        p.type = UtilService.TWO_COLS_COLLAPSE;
        return p;
      }, this);
      this.operazioni = concat?this.operazioni.concat(_temp):_temp;
    }
  }

  protected _loadExtraInfo(_json: any) {
    const _tmpDC = _json.dataOraCaricamento?moment(_json.dataOraCaricamento).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    this.info.extraInfo = [];
    this.info.extraInfo.push({ label: Voce.DATA_CARICAMENTO,  value: _tmpDC });
    this.info.extraInfo.push({ label: Voce.RICHIESTA_STAMPA_AVVISI+': ', value: UtilService.ABILITA[(_json.stampaAvvisi || false).toString()] });
    if((UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ELABORAZIONE_STAMPA) ||
      (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.IN_ELABORAZIONE) ||
      (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO) ||
      (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO_CON_ERRORI)) {
      if(_json.numeroOperazioniTotali !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_TOTALI+': ', value: _json.numeroOperazioniTotali });
      }
      if(_json.numeroOperazioniEseguite !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_ESEGUITE+': ', value: _json.numeroOperazioniEseguite });
      }
      if(_json.numeroOperazioniFallite !== null) {
        this.info.extraInfo.push({ label: Voce.OPERAZIONI_FALLITE+': ', value: _json.numeroOperazioniFallite });
      }
    }
    if(_json.stampaAvvisi && ((UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ELABORAZIONE_STAMPA) ||
        (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.IN_ELABORAZIONE) ||
        (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO) ||
        (UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO_CON_ERRORI))) {
      if(_json.numeroOperazioniTotali !== null) {
        this.info.extraInfo.push({ label: Voce.AVVISI_TOTALI+': ', value: _json.numeroAvvisiTotali });
      }
      if(_json.numeroOperazioniEseguite !== null) {
        this.info.extraInfo.push({ label: Voce.AVVISI_STAMPATI+': ', value: _json.numeroAvvisiStampati });
      }
      if(_json.numeroOperazioniFallite !== null) {
        this.info.extraInfo.push({ label: Voce.AVVISI_FALLITI+': ', value: _json.numeroAvvisiFalliti });
      }
    }
    if(_json.operatoreMittente) {
      this.info.extraInfo.push({ label: Voce.OPERATORE_MITTENTE+': ', value: _json.operatoreMittente });
    }
    if((UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO ||
        UtilService.STATI_TRACCIATO[_json.stato.toUpperCase()] === UtilService.STATI_TRACCIATO.ESEGUITO_CON_ERRORI) &&
        _json.dataOraUltimoAggiornamento) {
      this.info.extraInfo.push({ label: Voce.DATA_COMPLETAMENTO+': ', value: moment(_json.dataOraUltimoAggiornamento).format('DD/MM/YYYY [ore] HH:mm') });
    }
  }

  /**
   * Socket notification handler
   * @param {Riepilogo} info
   * @param {Function} updater
   * @private
   */
  protected _mapSocketNotifier(info: Riepilogo, updater: Function) {
    if (info && info.socketNotification) {
      const _url: string = info.socketNotification.URI+'/'+info.socketNotification.data.id;
      if (_url) {
        setTimeout(() => {
          if (info.socketNotification) {
            this.gps.getDataServiceBkg(_url).subscribe(
              function (_response) {
                if (info.socketNotification) {
                  info.socketNotification.data = _response.body;
                  const _pda = this.us.pdaTracciato(info.socketNotification.data);
                  updater({ property: 'stato', value: UtilService.STATI_TRACCIATO[info.socketNotification.data.stato] });
                  updater({ property: 'avanzamento', value: _pda });
                  this._loadExtraInfo(info.socketNotification.data);
                  if (_pda !== undefined) {
                    this._mapSocketNotifier(info, updater);
                  } else {
                    info.resetSocket();
                    this.modified = true;
                  }
                }
              }.bind(this),
              (error) => {
                this.us.onError(error);
              });
          }
        }, info.socketNotification.timeout);
      }
    }
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.nomeFile:'Dettaglio tracciato' });
  }

  refresh(mb: ModalBehavior) {}

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let headers;
    let _service = UtilService.URL_PENDENZE + '/' + UtilService.TRACCIATI;

    let _data;
    try {
      if (mb.info.viewModel) {
        if (mb.info.viewModel.json) {
          _data = mb.info.viewModel.json;
        } else {
          headers = new HttpHeaders();
          headers = headers.set('X-GOVPAY-FILENAME', mb.info.viewModel.nome);
          _data = new FormData();
          _data.append('file', mb.info.viewModel.file);
        }
        if(mb.info.viewModel.mimeType === 'text/csv') {
          _service += '/' + mb.info.viewModel.idDominio;
          if(mb.info.viewModel.idTipoPendenza) {
            _service += '/' +mb.info.viewModel.idTipoPendenza;
          }
        }
        const query: string = `stampaAvvisi=${mb.info.viewModel.stampaAvvisi}`;

        this.gps.saveData(_service, _data, query, UtilService.METHODS.POST, false, headers).subscribe(
          () => {
            this.gps.updateSpinner(false);
            responseService.next(true);
          },
          (error) => {
            this.gps.updateSpinner(false);
            this.us.onError(error);
          });
      } else {
        this.us.alert('Impossibile eseguire l‘operazione richiesta, dati non disponibili.');
      }
    } catch(e) {
      console.warn(e);
    }
  }

  exportData(type: string) {
    let url: string = '';
    switch (type) {
      case UtilService.EXPORT_TRACCIATO_ESITO:
        url = UtilService.URL_TRACCIATI+'/'+this.json.id+'/esito';
        break;
      case UtilService.EXPORT_TRACCIATO_RICHIESTA:
        url = UtilService.URL_TRACCIATI+'/'+this.json.id+'/richiesta';
        break;
      default:
      url = UtilService.URL_TRACCIATI+'/'+this.json.id+'/stampe';
    }
    window.open(UtilService.RootByTOA() + url, '_blank');
  }

  saveFile(data: any, structure: any, ext: string) {}

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
