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
      extraInfo: [
        { label: Voce.AVVISO+': ', value: _json.numeroAvviso },
        { label: Voce.SCADENZA+': ', value: UtilService.defaultDisplay({ value: moment(_json.dataScadenza).format('DD/MM/YYYY'), text: 'Nessuna' }) },
        { label: Voce.VALIDITA+': ', value: UtilService.defaultDisplay({ value: moment(_json.dataValidita).format('DD/MM/YYYY'), text: 'Nessuna' }) },
        { label: Voce.TASSONOMIA_AVVISO+': ', value: _json.tassonomiaAvviso },
      ]
    });
    (_json.tassonomia)?this.info.extraInfo.push({ label: Voce.TASSONOMIA+': ', value: _json.tassonomia }):null;
    if(UtilService.STATI_PENDENZE[_json.stato] == UtilService.STATI_PENDENZE.ANNULLATA) {
      this.info.dataAnnullamento = UtilService.defaultDisplay({ value: moment(_json.dataAnnullamento).format('DD/MM/YYYY') });
      this.info.causale = _json.causale;
      this.info.cancel = true;
    }
    //Dettaglio importi
    this._paymentsSum = 0;
    this.importi = _json.voci.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ value: item.descrizione });
      _std.sottotitolo = new Dato({ label: Voce.ID_PENDENZA+': ', value: item.idVocePendenza });
      _std.importo = this.us.currencyFormat(item.importo);
      _std.stato = item.stato;
      this._paymentsSum += UtilService.defaultDisplay({ value: item.importo, text: 0 });
      let p = new Parameters();
      p.jsonP = item;
      p.model = _std;
      return p;
    }, this);
    //Note -> 'item.segnalazioni'
    if(_json.segnalazioni) {
      this.informazioni = _json.segnalazioni.map(function(sgn) {
        let _cc = new CronoCode();
        _cc.data = UtilService.defaultDisplay({ value: moment(sgn.data).format('DD/MM/YYYY') });
        _cc.codice = sgn.codice;
        _cc.titolo = new Dato({ value: sgn.descrizione });
        _cc.sottotitolo = new Dato({ value: sgn.dettaglio?sgn.dettaglio:'' });
        let p = new Parameters();
        p.jsonP = sgn;
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
          let _date = UtilService.defaultDisplay({ value: moment(item.rpt.dataOraMessaggioRichiesta).format('DD/MM/YYYY') });
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
          let _dataOraRichiesta = UtilService.defaultDisplay({ value: moment(item.dataOraRichiesta).format('DD/MM/YYYY [ore] HH:mm') });
          let _std = new Standard();
          let _st: Dato = Dato.arraysToDato(
            [ Voce.ID_DOMINIO, Voce.IUV, Voce.CCP, Voce.DATA ],
            [ item.idDominio, item.iuv, item.ccp, _dataOraRichiesta ],
            ', '
          );
          _std.titolo = new Dato({ label: item.tipoEvento });
          _std.sottotitolo = _st;
          _std.stato = item.esito;
          let p = new Parameters();
          p.model = _std;
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

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.causale:null });
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
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
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  exportData() {
    this.gps.updateSpinner(true);
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];
    //TODO: Root Pdf pendenza, servizio non attivo
    // * urls.push(UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza);
    // names.push('Dati_pendenza.pdf');
    // contents.push('application/pdf');
    // types.push('blob'); *
    this.tentativi.forEach((el) => {
      // /rpp/{idDominio}/{iuv}/{ccp}/rpt
      // /rpp/{idDominio}/{iuv}/{ccp}/rt
      let item = el.jsonP;
      let _folder = item.rpt.dominio.identificativoDominio+'_'+item.rpt.datiVersamento.identificativoUnivocoVersamento+'_'+item.rpt.datiVersamento.codiceContestoPagamento;
      folders.push(_folder);
      urls.push('/rpp/'+item.rpt.dominio.identificativoDominio+'/'+item.rpt.datiVersamento.identificativoUnivocoVersamento+'/'+item.rpt.datiVersamento.codiceContestoPagamento+'/rpt');
      names.push('Rpt.xml'+_folder);
      contents.push('application/xml');
      types.push('text');
      if(item.rt) {
        urls.push('/rpp/'+item.rt.dominio.identificativoDominio+'/'+item.rt.datiPagamento.identificativoUnivocoVersamento+'/'+item.rt.datiPagamento.CodiceContestoPagamento+'/rt');
        contents.push('application/xml');
        names.push('Rt.xml'+_folder);
        types.push('text');
        urls.push('/rpp/'+item.rt.dominio.identificativoDominio+'/'+item.rt.datiPagamento.identificativoUnivocoVersamento+'/'+item.rt.datiPagamento.CodiceContestoPagamento+'/rt');
        contents.push('application/pdf');
        names.push('Rt.pdf'+_folder);
        types.push('blob');
      }
      urls.push(UtilService.URL_GIORNALE_EVENTI+'?idA2A='+this.json.idA2A+'&idPendenza='+this.json.idPendenza);
      contents.push('application/json');
      names.push('Eventi.csv'+_folder);
      types.push('json');
    }, this);
    this.gps.multiExportService(urls, contents, types).subscribe(function (_response) {
        this.saveFile(_response, { folders: folders, names: names }, '.zip');
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  saveFile(data: any, structure: any, ext: string) {
    let root = 'Pendenza_' + this.json.idA2A + '_' + this.json.idPendenza;
    let zipname = root + ext;
    let zip = new JSZip();
    let zroot = zip.folder(root);
    //TODO: Abilitare appena il servizio Root pdf pendenza è attivo
    // zroot.file(structure.names[0], data[0].body);
    structure.folders.forEach((folder) => {
      let zfolder = zroot.folder(folder);
      data.forEach((file, ref) => {
        if (structure.names[ref].indexOf(folder) != -1) {
          let _name = structure.names[ref].split(folder)[0];
          let zdata = file.body;
          if(_name.indexOf('csv') != -1) {
            zdata = this.jsonToCsv(_name, file.body);
          }
          zfolder.file(_name, zdata);
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
}
