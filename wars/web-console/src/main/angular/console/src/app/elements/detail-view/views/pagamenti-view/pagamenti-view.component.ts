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

  @Input() json: any;
  @Input() modified: boolean = false;


  protected NOTA = UtilService.NOTA;
  protected ADD = UtilService.PATCH_METHODS.ADD;
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
        this.mapJsonDetail(this.json);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail(_json: any) {
    //Riepilogo
    let _ist, _tv;
    if(_json.rpp && _json.rpp.length != 0) {
      let _rpp = _json.rpp[0];
      _ist = (_rpp.rt)?_rpp.rt.istitutoAttestante.denominazioneAttestante:'';
      _tv = (_rpp.rpt)?_rpp.rpt.datiVersamento.tipoVersamento:'';
    }
    let _debitore = Voce.UNDEFINED;
    if(_json.soggettoVersante){
      _debitore = Dato.concatStrings([ _json.soggettoVersante.anagrafica, _json.soggettoVersante.identificativo ], ', ');
    }
    let _st = new Dato({ label: Voce.SOGGETTO_VERSANTE, value: _debitore });
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DATA, value: _json.dataRichiestaPagamento?moment(_json.dataRichiestaPagamento).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE }),
      sottotitolo: _st,
      importo: this.us.currencyFormat(_json.importo),
      stato: UtilService.STATI_PAGAMENTO[_json.stato],
      extraInfo: []
    });
    if(_ist) {
      this.info.extraInfo.push({ label: Voce.ISTITUTO+': ', value: _ist })
    }
    if(_tv) {
      this.info.extraInfo.push({ label: Voce.TIPO+': ', value: UtilService.TIPI_VERSAMENTO[_tv] })
    }
    if(_json.id) {
      this.info.extraInfo.push({ label: Voce.ID_SESSIONE+': ', value: _json.id });
    }
    this._paymentsSum = 0;
    if(_json.rpp) {
      this.pagamenti = _json.rpp.map(function(item) {
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
        let _st = new Dato({ value: Dato.concatStrings([ Voce.ENTE_CREDITORE+': '+item.pendenza.dominio.ragioneSociale, Voce.IUV+': '+item.rpt.datiVersamento.identificativoUnivocoVersamento ], ', ') });
        let _std = new StandardCollapse();
        _std.titolo = new Dato({ value: item.pendenza.causale });
        _std.sottotitolo = _st;
        _std.stato = _stato;
        _std.importo = this.us.currencyFormat(item.rpt.datiVersamento.importoTotaleDaVersare);
        this._paymentsSum += UtilService.defaultDisplay({ value: parseFloat(item.rpt.datiVersamento.importoTotaleDaVersare), text: 0 });
        //TODO: Disattivato collapse (_std.motivo)
        // if (_dettaglio) {
        //   _std.motivo = _dettaglio;
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
      let _ref = encodeURIComponent(this.json.id);
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
    let _ref = encodeURIComponent(this.json.id);
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
    let urls: string[] = [];
    let contents: string[] = [];
    let types: string[] = [];
    let folders: string[] = [];
    let names: string[] = [];
    try {
      //TODO: Root Pdf carrello pendenza, servizio non attivo
      // * urls.push(UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza);
      // names.push('Dati_pagamento.pdf');
      // contents.push('application/pdf');
      // types.push('blob'); *
      this.pagamenti.forEach((el) => {
        // /rpp/{idDominio}/{iuv}/{ccp}/rpt
        // /rpp/{idDominio}/{iuv}/{ccp}/rt
        let item = el.jsonP;
        let _folder = encodeURIComponent(item.rpt.dominio.identificativoDominio)+'_'+encodeURIComponent(item.rpt.datiVersamento.identificativoUnivocoVersamento)+'_'+encodeURIComponent(item.rpt.datiVersamento.codiceContestoPagamento);
        folders.push(_folder);
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
        }
      }, this);
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
