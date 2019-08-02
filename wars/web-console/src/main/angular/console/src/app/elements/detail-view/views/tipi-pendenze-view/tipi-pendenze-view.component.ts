import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { Dato } from '../../../../classes/view/dato';
import { Voce } from '../../../../services/voce.service';

@Component({
  selector: 'link-tipi-pendenze-view',
  templateUrl: './tipi-pendenze-view.component.html',
  styleUrls: ['./tipi-pendenze-view.component.scss']
})
export class TipiPendenzeViewComponent implements IModalDialog, OnInit, AfterViewInit {
  @ViewChild('iSchemaBrowse') _iSchemaBrowse: ElementRef;

  @Input() informazioni = [];
  @Input() runtime: any = { tipo: null, definizione: null, validazione: null, inoltro: null, trasformazione: null };
  @Input() promemoriaAvviso: any = { tipo: null, allegaPdf: null, oggetto: null, messaggio: null, noconfig: false };
  @Input() promemoriaRicevuta: any = { tipo: null, allegaPdf: null, oggetto: null, messaggio: null, noconfig: false };

  @Input() json: any;
  @Input() modified: boolean = false;

  protected _voce = Voce;
  protected _jsonSchemaSelected: any;
  protected _jsonVisualizzazione: any;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this._getDettaglioTipiPendenza();
  }

  ngAfterViewInit() {
  }

  _getDettaglioTipiPendenza() {
    let _url = UtilService.URL_TIPI_PENDENZA+'/'+encodeURIComponent(this.json.idTipoPendenza);
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
    // Riepilogo
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: Voce.DESCRIZIONE, value: this.json.descrizione }));
    _dettaglio.push(new Dato({ label: Voce.ID_TIPO_PENDENZA, value: this.json.idTipoPendenza }));
    _dettaglio.push(new Dato({ label: Voce.TIPO, value: this.us.getLabelByValue(UtilService.TIPOLOGIA_PENDENZA, this.json.tipo) }));
    _dettaglio.push(new Dato({ label: Voce.IUV_CODEC, value: this.json.codificaIUV }));
    _dettaglio.push(new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(this.json.abilitato)?UtilService.ABILITA[this.json.abilitato]:Voce.NON_PRESENTE }));
    _dettaglio.push(new Dato({ label: Voce.TERZI, value: this.us.hasValue(this.json.pagaTerzi)?UtilService.ABILITA[this.json.pagaTerzi]:Voce.NON_PRESENTE }));
    this.informazioni = _dettaglio.slice(0);
    if (this.json.form && this.json.form.definizione && this.json.form.tipo) {
      this._jsonSchemaSelected = {};
      this._jsonSchemaSelected['schema'] = this.json.form.definizione;
      this._jsonSchemaSelected['generatore'] = this.us.getLabelByValue(UtilService.GENERATORI, this.json.form.tipo);
    }
    this.runtime = {
      trasformazione: null,
      tipo: null,
      definizione: null,
      validazione: this.json.validazione || null,
      inoltro: new Dato({ label: Voce.APPLICAZIONE, value: this.json.inoltro || Voce.NON_PRESENTE })
    };
    if(this.json.trasformazione && this.json.trasformazione.tipo && this.json.trasformazione.definizione) {
      this.runtime.tipo = new Dato({ label: Voce.TIPO, value: (this.us.sentenceCapitalize(this.json.trasformazione.tipo) || Voce.NON_PRESENTE) });
      this.runtime.definizione = this.json.trasformazione.definizione || null;
    } else {
      this.runtime.trasformazione = new Dato({ label: Voce.TRASFORMAZIONE, value: Voce.NON_CONFIGURATO });
    }

    if(this.json.promemoriaAvviso && this.json.promemoriaAvviso.tipo && this.json.promemoriaAvviso.oggetto && this.json.promemoriaAvviso.messaggio) {
      this.promemoriaAvviso = {
        tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: this.us.sentenceCapitalize(this.json.promemoriaAvviso.tipo) }),
        allegaPdf: new Dato({label: Voce.ALLEGA_PDF_AVVISO, value: UtilService.ABILITA[this.json.promemoriaAvviso.allegaPdf || 'false']}),
        oggetto: this.json.promemoriaAvviso.oggetto || null,
        messaggio: this.json.promemoriaAvviso.messaggio || null,
        noconfig: false
      };
    } else {
      this.promemoriaAvviso.noconfig = true;
    }

    if(this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.tipo && this.json.promemoriaRicevuta.oggetto && this.json.promemoriaRicevuta.messaggio) {
      this.promemoriaRicevuta = {
        tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: this.us.sentenceCapitalize(this.json.promemoriaRicevuta.tipo) }),
        allegaPdf: new Dato({label: Voce.ALLEGA_PDF_RICEVUTA, value: UtilService.ABILITA[this.json.promemoriaRicevuta.allegaPdf || 'false']}),
        oggetto: this.json.promemoriaRicevuta.oggetto || null,
        messaggio: this.json.promemoriaRicevuta.messaggio || null,
        noconfig: false
      };
    } else {
      this.promemoriaRicevuta.noconfig = true;
    }

    if(this.json.visualizzazione) {
      this._jsonVisualizzazione = this.json.visualizzazione;
    }
  }

  protected _showTextContent(value: string, title: string) {
    let _decoded = '';
    try{
      // _decoded = decodeURI(atob(value));
      _decoded = decodeURIComponent(atob(value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      const _page = `
        <html>
        <title>${title || 'File'}</title>
        <style>html, body { padding: 0; margin: 0; background-color: #404040; color: #fff; } iframe { width: 100%; height: 100%; border: 0;} </style>
          <body>
            <pre>
            <code>
              ${_decoded}
            </code>
            </pre>
          </body>
        </html>`;
      const _tmpW = window.open();
      _tmpW.document.open();
      _tmpW.document.write(_page);
      _tmpW.document.close();
    } catch(e) {
      console.log(e);
    }
  }

  protected _editTipoPendenza() {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica tipo pendenza',
      templateName: UtilService.TIPO_PENDENZA
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      Object.assign(this.json, mb.info.viewModel);
      this.mapJsonDetail();
    }
  }

  /**
   * Save Tipo pendenza (Put to: /tipiPendenza/{idTipoPendenza}
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = UtilService.URL_TIPI_PENDENZA;
    let json = JSON.parse(JSON.stringify(mb.info.viewModel));

    delete json['idTipoPendenza'];
    _service += '/'+encodeURIComponent(mb.info.viewModel['idTipoPendenza']);

    this.gps.saveData(_service, json).subscribe(
      () => {
        this.gps.updateSpinner(false);
        responseService.next(true);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.descrizione:null });
  }

  infoDetail(): any {
    return {};
  }

}
