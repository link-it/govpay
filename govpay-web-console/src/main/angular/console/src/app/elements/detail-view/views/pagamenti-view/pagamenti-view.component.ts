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
  @Input() json: any;

  protected info: Riepilogo;
  protected _paymentsSum: number = 0;
  protected _importiOverIcons: string[] = ['file_download'];

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioPagamento();
  }

  ngAfterViewInit() {
  }

  protected dettaglioPagamento() {
    //console.log('pagamento/detail', this.json);
    let _url = UtilService.URL_PAGAMENTI+'/'+this.json.id;
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
    let _ist, _tv;
    if(this.json.rpp && this.json.rpp.length != 0) {
      let _rpp = this.json.rpp[0];
      _ist = (_rpp.rt)?_rpp.rt.istitutoAttestante.denominazioneAttestante:'';
      _tv = (_rpp.rpt)?_rpp.rpt.datiVersamento.tipoVersamento:'';
    }
    let _debitore = Voce.UNDEFINED;
    (this.json.soggettoVersante)?_debitore = Dato.concatStrings([ this.json.soggettoVersante.anagrafica, this.json.soggettoVersante.identificativo ], ', '):null;
    let _st = new Dato({ label: Voce.DEBITORE, value: _debitore });
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DATA, value: UtilService.defaultDisplay({ value: moment(this.json.dataRichiestaPagamento).format('DD/MM/YYYY') }) }),
      sottotitolo: _st,
      importo: this.us.currencyFormat(this.json.importo),
      stato: UtilService.STATI_PAGAMENTO[this.json.stato],
      extraInfo: []
    });
    this.info.extraInfo.push({ label: Voce.ISTITUTO+': ', value: _ist });
    (_tv)?this.info.extraInfo.push({ label: Voce.TIPO+': ', value: UtilService.TIPI_VERSAMENTO[_tv] }):null;
    this._paymentsSum = 0;
    if(this.json.rpp) {
      this.pagamenti = this.json.rpp.map(function(item) {
        let _stato, _dettaglio;
        switch (item.stato) {
          case 'RT_ACCETTATA_PA':
            _stato = (item.rt)?UtilService.STATI_ESITO_PAGAMENTO[item.rt.datiPagamento.codiceEsitoPagamento]:-1;
            break;
          case 'RPT_RIFIUTATA_NODO':
          case 'RPT_RIFIUTATA_PSP':
          case 'RPT_ERRORE_INVIO_A_PSP':
            _stato = UtilService.STATI_RPP.FALLITO;
            _dettaglio = item.dettaglioStato;
            break;
          case 'RT_RIFIUTATA_PA':
          case 'RT_ESITO_SCONOSCIUTO_PA':
            _stato = UtilService.STATI_RPP.ANOMALO;
            _dettaglio = item.dettaglioStato;
            break;
          default:
            _stato = UtilService.STATI_RPP.IN_CORSO;
        }
        let _st = new Dato({ value: Dato.concatStrings([ Voce.ENTE_CREDITORE+': '+item.pendenza.dominio.ragioneSociale, Voce.AVVISO+': '+item.pendenza.numeroAvviso ], ', ') });
        let _std = new StandardCollapse();
        _std.titolo = new Dato({ value: item.pendenza.causale });
        _std.sottotitolo = _st;
        _std.stato = _stato;
        _std.importo = this.us.currencyFormat(item.rpt.datiVersamento.importoTotaleDaVersare);
        this._paymentsSum += UtilService.defaultDisplay({ value: parseFloat(item.rpt.datiVersamento.importoTotaleDaVersare), text: 0 });
        (_dettaglio)?_std.motivo = _dettaglio:null;
        let p = new Parameters();
        p.model = _std;
        p.type = UtilService.STANDARD_COLLAPSE;
        p.jsonP = item;
        return p;
      }, this);
    }
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.id:null });
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  refresh(mb: ModalBehavior) {}

  exportData() {
    this.gps.updateSpinner(true);
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];
    //TODO: Root Pdf carrello pendenza, servizio non attivo
    // * urls.push(UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza);
    // names.push('Dati_pagamento.pdf');
    // contents.push('application/pdf');
    // types.push('blob'); *
    this.pagamenti.forEach((el) => {
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
    let root = 'Pagamento_' + this.json.id;
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
          zfolder.file(_name, zdata);
        }
      });
    });
    zip.generateAsync({type: 'blob'}).then(function (zipData) {
      FileSaver(zipData, zipname);
      this.gps.updateSpinner(false);
    }.bind(this));
  }

}
