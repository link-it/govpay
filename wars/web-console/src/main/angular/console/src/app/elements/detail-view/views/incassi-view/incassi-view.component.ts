import { AfterViewInit, Component, Input } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';
import { NewStandardCollapse } from '../../../../classes/view/new-standard-collapse';
import { IExport } from '../../../../classes/interfaces/IExport';
import { isNullOrUndefined } from 'util';

import * as moment from 'moment';

declare let GovRiconciliazioniConfig: any;

@Component({
  selector: 'link-incassi-view',
  templateUrl: './incassi-view.component.html',
  styleUrls: ['./incassi-view.component.scss']
})
export class IncassiViewComponent implements IModalDialog, IExport, AfterViewInit {

  _voce = Voce;

  @Input() informazioni = [];
  @Input() riscossioni = [];

  @Input() json: any;

  protected info: Riepilogo;

  _quoteExport = ['titolo', 'tipologia', 'categoria', 'capitolo', 'articolo', 'accertamento', 'annoEsercizio', 'importo'];
  _quoteLabel = {
    capitolo: 'Capitolo',
    annoEsercizio: 'Anno esercizio',
    importo: 'Importo',
    titolo: 'Titolo',
    accertamento: 'Accertamento',
    tipologia: 'Tipologia',
    categoria: 'Categoria',
    articolo: 'Articolo',
    proprietaCustom: 'Proprieta custom'
  };
  _exportLabel = {
    idDominio: 'Dominio',
    idFlusso: 'Id Flusso',
    iuv: 'IUV',
    importo: 'Importo',
    data: 'Data',
    idPendenza: 'Id Pendenze',
    tipoPendenza: 'Tipo pendenza',
    idVocePendenza: 'Id voce pendenza',
    datiAllegatiPendenza: 'Dati allegati pendenza',
    datiAllegatiVocePendenza: 'Dati allegati voce pendenza'
  };

  _quoteCount = 10;

  constructor(public gps: GovpayService, public us: UtilService) {
    if (GovRiconciliazioniConfig && GovRiconciliazioniConfig.quoteExport && GovRiconciliazioniConfig.quoteLabel && GovRiconciliazioniConfig.exportLabel) {
      this._quoteExport = GovRiconciliazioniConfig.quoteExport;
      this._quoteLabel = GovRiconciliazioniConfig.quoteLabel;
      this._exportLabel = GovRiconciliazioniConfig.exportLabel;
      this._exportLabel = GovRiconciliazioniConfig.exportLabel;
      this._quoteCount = GovRiconciliazioniConfig.quoteCount || 10;
    }
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.dettaglioIncasso();
    });
  }

  protected dettaglioIncasso() {
    let _url = UtilService.URL_INCASSI+'/'+this.json.dominio.idDominio+'/'+this.json.idIncasso;
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
    let _st: Dato;
    if (this.json.dominio && this.json.dominio.ragioneSociale !== null) {
      _st = new Dato({ label: Voce.ENTE_CREDITORE, value: this.json.dominio.ragioneSociale });
    }
    this.info = new Riepilogo({
      importo: this.us.currencyFormat(this.json.importo),
      extraInfo: [
        { label: Voce.IDENTIFICATIVO_PSP_RIVERSAMENTO+': ', value: this.json.sct }
      ]
    });
    this.info.titolo = null;
    this.info.sottotitolo = _st || null;
    const _data: string = this.json.data?moment(this.json.data).format('DD/MM/YYYY'):null;
    if (_data) {
      this.info.extraInfo.push({ label: Voce.DATA_REGISTRAZIONE + ': ', value: _data });
    }
    // if (this.json.idIncasso) {
    //   this.info.extraInfo.push({ label: Voce.RICONCILIAZIONE+': ', value: this.json.idIncasso });
    // }
    if (this.json.idFlusso) {
      this.info.extraInfo.push({ label: Voce.ID_FLUSSO + ': ', value: this.json.idFlusso });
    }
    if (this.json.iuv) {
      this.info.extraInfo.push({ label: Voce.IUV + ': ', value: this.json.iuv });
    }

    //Riscossioni
    this.riscossioni = this.json.riscossioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, UtilService.URL_RISCOSSIONI);
      p.type = UtilService.NEW_STANDARD_COLLAPSE;
      return p;
    }, this);
  }

  protected _mapNewItemByType(item: any, type: string): Standard {
    let _stdC = new NewStandardCollapse();
    switch(type) {
      case UtilService.URL_RISCOSSIONI:
        _stdC.titolo = new Dato({ value: item.vocePendenza.pendenza.causale });
        _stdC.sottotitolo = new Dato({ label: Voce.IUV+': ', value: item.iuv });
        _stdC.importo = this.us.currencyFormat(item.importo);
        _stdC.item = item;
        _stdC.elenco = [];
        if (item.vocePendenza && item.vocePendenza.idVocePendenza) {
          _stdC.elenco.push({ label: Voce.ID_VOCE_PENDENZA, value: item.vocePendenza.idVocePendenza });
        }
        if (item.indice !== null && item.indice !== undefined) {
          _stdC.elenco.push({ label: Voce.INDICE_VOCE, value: item.indice });
        }
        if (item.vocePendenza && item.vocePendenza.descrizione) {
          _stdC.elenco.push({ label: Voce.DESCRIZIONE, value: item.vocePendenza.descrizione });
        }
        if (item.vocePendenza && item.vocePendenza.descrizioneCausaleRPT) {
          _stdC.elenco.push({ label: Voce.CAUSALE, value: item.vocePendenza.descrizioneCausaleRPT });
        }
        if (item.iur) {
          _stdC.elenco.push({ label: Voce.ID_RISCOSSIONE, value: item.iur });
        }
        if (item.vocePendenza && item.vocePendenza.pendenza && item.vocePendenza.pendenza.idA2A) {
          _stdC.elenco.push({ label: Voce.ID_A2A, value: item.vocePendenza.pendenza.idA2A });
        }
        if (item.vocePendenza && item.vocePendenza.pendenza && item.vocePendenza.pendenza.idPendenza) {
          _stdC.elenco.push({ label: Voce.ID_PENDENZA, value: item.vocePendenza.pendenza.idPendenza });
        }
        break;
    }
    return _stdC;
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = null;
    let _json = null;
    let _method = null;
    switch(mb.info.templateName) {
      case UtilService.INCASSO:
        _service = UtilService.URL_INCASSI + '/' + UtilService.EncodeURIComponent(mb.info.viewModel['idDominio']);
        _json = mb.info.viewModel;
        delete _json.idDominio;
        _method = UtilService.METHODS.POST;
        break;
    }
    if(_json && _service) {
      this.gps.saveData(_service, _json, null, _method).subscribe(
        (response) => {
          this.gps.updateSpinner(false);
          mb.info.viewModel = response.body;
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  exportData(data?: any) {
    const _json: any = JSON.parse(JSON.stringify(this.json));
    const _riscossioni: any[] = [];
    this.json.riscossioni.forEach(risc => {
      const quote = (risc.vocePendenza && risc.vocePendenza.contabilita) ? risc.vocePendenza.contabilita.quote : [];
      const riscossione: any = {};
      riscossione[this._exportLabel['idDominio']] = _json.dominio.idDominio;
      riscossione[this._exportLabel['idFlusso']] = _json.idFlusso ? _json.idFlusso : '';
      riscossione[this._exportLabel['iuv']] = risc.iuv || '';
      riscossione[this._exportLabel['importo']] = risc.importo || 0;
      riscossione[this._exportLabel['data']] = risc.data || '';
      riscossione[this._exportLabel['idPendenza']] = risc.vocePendenza.pendenza.idPendenza || '';
      riscossione[this._exportLabel['tipoPendenza']] = risc.vocePendenza.pendenza.idTipoPendenza || '';
      riscossione[this._exportLabel['idVocePendenza']] = risc.vocePendenza.idVocePendenza || '';
      riscossione[this._exportLabel['datiAllegatiPendenza']] = risc.vocePendenza.pendenza.datiAllegati || '';
      riscossione[this._exportLabel['datiAllegatiVocePendenza']] = risc.vocePendenza.datiAllegati || '';

      for (let i = 0; i < this._quoteCount; i++) {
        this._quoteExport.forEach(key => {
          const label = `${this._quoteLabel[key]} ${i + 1}`;
          riscossione[label] = (this.us.hasValue(quote[i])) ? quote[i][key] : '';
        });
      }

      _riscossioni.push(riscossione);
    });

    this.us.updateProgress(true, 'Export in corso...', 'determinate', 0);
    const fileName = _json.idFlusso ? _json.idFlusso : _json.iuv;
    const zip: any = this.us.initZip();
    const csvData: string = this.us.jsonToCsv('PagamentiRiconciliati.csv', _riscossioni);
    this.us.addDataToZip(csvData, fileName + '.csv', zip);
    this.us.updateProgress(true, 'Export in corso...', 'determinate', 100);
    this.us.updateProgress(false, '', 'indeterminate', 0, 0);
    this.us.saveZip(zip, fileName);
  }

  exportData_(data?: any) {
    const _json: any = JSON.parse(JSON.stringify(this.json));
    delete _json['riscossioni'];
    const structure: any = {
      names: [ 'Riconciliazione.csv', 'PagamentiRiconciliati.csv' ],
      json: [ _json, this.json.riscossioni ]
    };
    const zip: any = this.us.initZip();
    this.us.updateProgress(true, 'Export in corso...', 'determinate', 0);
    this.__loopElaborate(zip, structure);
  }

  __loopElaborate(zip: any, structure: any) {
    setTimeout(() => {
      if (structure.names.length !== 0) {
        const data: any = this.us.jsonToCsv(structure.names[0], structure.json[0]);
        this.us.addDataToZip(data, structure.names[0], zip);
        this.us.updateProgress(true, 'Export in corso...', 'determinate', Math.trunc( 100/structure.names.length));
        structure.names.shift();
        structure.json.shift();
        this.__loopElaborate(zip, structure);
      } else {
        this.us.updateProgress(true, 'Export in corso...', 'determinate',100);
        this.us.saveZip(zip, 'Incasso');
      }
    }, 200);
  }

  refresh(mb: ModalBehavior) {}

  title(): string {
    return this.json?this.json.sct:'Dettaglio incasso';
  }
}
