import { AfterViewInit, Component, Input } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { GovpayService } from '../../../../services/govpay.service';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { IExport } from '../../../../classes/interfaces/IExport';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-rendicontazioni-view',
  templateUrl: './rendicontazioni-view.component.html',
  styleUrls: [ './rendicontazioni-view.component.scss' ]
})
export class RendicontazioniViewComponent implements IModalDialog, IExport, AfterViewInit {

  @Input() segnalazioni = [];
  @Input() rendicontazioni = [];

  @Input() json: any;

  protected info: Riepilogo;

  constructor(private gps: GovpayService, private us: UtilService) {
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.dettaglioEvento();
    });
  }

  protected dettaglioEvento() {
    let _url = UtilService.URL_RENDICONTAZIONI+'/'+UtilService.EncodeURIComponent(this.json.idDominio)+'/'+UtilService.EncodeURIComponent(this.json.idFlusso)+'/'+this.json.dataFlusso;
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

  protected mapJsonDetail() {
    //Riepilogo
    let _dr = this.json.dataRegolamento?moment(this.json.dataRegolamento).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
    let _df = this.json.dataFlusso?moment(this.json.dataFlusso).format('DD/MM/YYYY [ore] HH:mm:ss'):Voce.NON_PRESENTE;
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.ISTITUTO, value: this.json.ragioneSocialePsp + ' (' + this.json.idPsp + ')' }),
      sottotitolo: new Dato({ label: Voce.ENTE_CREDITORE, value: this.json.ragioneSocialeDominio?(this.json.ragioneSocialeDominio + ' (' + this.json.idDominio + ')'):this.json.idDominio }),
      importo: this.us.currencyFormat(this.json.importoTotale),
      extraInfo: [
        { label: Voce.ID_CONTABILE+': ', value: this.json.trn },
        { label: Voce.DATA_FLUSSO+': ', value: _df },
        { label: Voce.DATA_REGOLAMENTO+': ', value: _dr },
        { label: Voce.NUMERO_PAY+': ', value: this.json.numeroPagamenti }
      ]
    });
    let _warn = [];
    if(this.json.segnalazioni) {
      this.json.segnalazioni.forEach((s) => {
        let _mappedElement = new Parameters();
        let _std: Standard = new Standard();
        _std.titolo = new Dato({ label: Voce.CODICE+': ', value: s.codice });
        _std.sottotitolo = new Dato({ label: Voce.DESCRIZIONE+': ', value: s.descrizione });
        _mappedElement.model = _std;
        _mappedElement.jsonP = s;
        _warn.push(_mappedElement);
      });
    }
    this.segnalazioni = _warn.slice(0);

    //Rendicontazioni
    this.rendicontazioni = this.json.rendicontazioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, UtilService.URL_RENDICONTAZIONI);
      return p;
    }, this);
  }

  protected _mapNewItemByType(item: any, type: string): Standard {
    let _std = new Standard();
    switch(type) {
      case UtilService.URL_RENDICONTAZIONI:
        let _st = Dato.arraysToDato(
          [ Voce.IUR_SIGLA, Voce.INDICE ],
          [ item.iur, item.indice ],
          ', '
        );
        _std.titolo = new Dato({ label: Voce.IUV,  value: item.iuv });
        _std.sottotitolo = _st;
        _std.importo = this.us.currencyFormat(item.importo);
        _std.stato = UtilService.STATI_ESITO_RENDICONTAZIONI[item.esito];
        break;
    }
    return _std;
  }

  exportData(type: string = 'xml') {
    switch (type) {
      case 'xml':
        this._exportDataXML();
        break;
        case 'csv':
        this._exportDataCSV();
        break;
    }
  }

  _exportDataXML() {
    this.gps.updateSpinner(true);
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];

    urls.push(UtilService.URL_RENDICONTAZIONI+'/'+this.json.idDominio+'/'+this.json.idFlusso+'/'+this.json.dataFlusso);
    names.push('Flusso_' + this.json.idFlusso.toString() + '.xml');
    contents.push('application/xml');
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
      let root = 'Flusso_' + this.json.idFlusso.toString() + '_'+moment().format('DDMMYYYY_HHmmss');
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
          let _re = /(?:filename=['"](.*(\.zip|\.csv|\.json|\.xml))['"])/gm;
          let _results = _re.exec(_cd);
          if(_results && _results.length == 3) {
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

  _exportDataCSV() {
    let _exportLabel = {
      idFlusso: 'Id Flusso',
      idPsp: 'Id Psp',
      idDominio: 'Dominio',
      iuv: 'IUV',
      iur: 'IUR',
      importo: 'Importo',
      data: 'Data',
      esito: 'Esito',
      idPendenza: 'Id pendenza',
      idTipoPendenza: 'Id tipo pendenza',
      idVocePendenza: 'Id voce pendenza',
      causale: 'Causale',
      idSoggettoPagatore: 'Id debitore',
      soggettoPagatore: 'Debitore anagrafica'
    };

    const _json: any = JSON.parse(JSON.stringify(this.json));
    const _rendicontazioni: any[] = [];
    this.json.rendicontazioni.forEach(rend => {
      const rendicontazione: any = {};
      rendicontazione[_exportLabel['idFlusso']] = _json.idFlusso ? _json.idFlusso : '';
      rendicontazione[_exportLabel['idPsp']] = _json.idPsp ? _json.idPsp : '';
      rendicontazione[_exportLabel['idDominio']] = _json.idDominio ? _json.idDominio : '';
      rendicontazione[_exportLabel['iuv']] = rend.iuv || '';
      rendicontazione[_exportLabel['iur']] = rend.iur || '';
      rendicontazione[_exportLabel['importo']] = rend.importo || 0;
      rendicontazione[_exportLabel['data']] = rend.data || '';
      rendicontazione[_exportLabel['idPendenza']] = '';
      rendicontazione[_exportLabel['idTipoPendenza']] = '';
      rendicontazione[_exportLabel['idVocePendenza']] = '';
      rendicontazione[_exportLabel['causale']] = '';
      rendicontazione[_exportLabel['idSoggettoPagatore']] = '';
      rendicontazione[_exportLabel['soggettoPagatore']] = '';
      if (rend.riscossione && rend.riscossione.vocePendenza && rend.riscossione.vocePendenza.pendenza) {
        rendicontazione[_exportLabel['idPendenza']] = rend.riscossione.vocePendenza.pendenza.idPendenza || '';
        if (rend.riscossione.vocePendenza.pendenza.tipoPendenza) {
          rendicontazione[_exportLabel['idTipoPendenza']] = rend.riscossione.vocePendenza.pendenza.tipoPendenza.idTipoPendenza || '';
        }
        rendicontazione[_exportLabel['idVocePendenza']] = rend.riscossione.vocePendenza.pendenza.idVocePendenza || '';
        rendicontazione[_exportLabel['causale']] = rend.riscossione.vocePendenza.pendenza.causale || '';
        if (rend.riscossione.vocePendenza.pendenza.soggettoPagatore) {
          rendicontazione[_exportLabel['idSoggettoPagatore']] = rend.riscossione.vocePendenza.pendenza.soggettoPagatore.identificativo || '';
          rendicontazione[_exportLabel['soggettoPagatore']] = rend.riscossione.vocePendenza.pendenza.soggettoPagatore.anagrafica || '';
        }
      }
      _rendicontazioni.push(rendicontazione);
    });

    this.gps.updateSpinner(true);
    const fileName = 'Flusso_' + _json.idFlusso.toString() + '_'+moment().format('DDMMYYYY_HHmmss') + '.csv';
    const csvData: string = this.us.jsonToCsv('Rendicontazioni.csv', _rendicontazioni);
    try {
      const blob = new Blob([csvData], {type: "text/plain;charset=utf-8"});
      FileSaver(blob, fileName);
    } catch (e) {
      this.us.alert('Si è verificato un errore non previsto durante la creazione del file.');
    }
    this.gps.updateSpinner(false);
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.idFlusso:null });
  }

}
