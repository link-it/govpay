import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { Dato } from '../../../../classes/view/dato';
import { Voce } from '../../../../services/voce.service';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-tipi-pendenze-view',
  templateUrl: './tipi-pendenze-view.component.html',
  styleUrls: ['./tipi-pendenze-view.component.scss']
})
export class TipiPendenzeViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() portaleBackoffice: any = { tipo: null, definizione: null, validazione: null, trasformazione: null, definizioneIPO: null, inoltro: null, abilitato: null };
  @Input() portalePagamento: any = { tipo: null, definizione: null, impaginazione: null, validazione: null, trasformazione: null, definizionePS1: null, inoltro: null, abilitato: null };
  @Input() promemoraAvvisoNAP: any = { tipo: null, allegaPdf: null, abilitato: null, oggetto: null, messaggio: null };
  @Input() promemoraAvvisoNAP_IO: any = { tipo: null, abilitato: null, oggetto: null, messaggio: null };
  @Input() promemoraAvvisoPSP: any = { tipo: null, preavviso: null, abilitato: null, oggetto: null, messaggio: null };
  @Input() promemoraAvvisoPSP_IO: any = { tipo: null, preavviso: null, abilitato: null, oggetto: null, messaggio: null };
  @Input() promemoraAvvisoNRP: any = { tipo: null, allegaPdf: null, abilitato: null, soloEseguiti: null, oggetto: null, messaggio: null };
  @Input() promemoraAvvisoNRP_IO: any = { tipo: null, abilitato: null, soloEseguiti: null, oggetto: null, messaggio: null };
  @Input() tracciato: any = { tipo: null, richiesta: null, risposta: null, intestazione: null, visualizzazione: null };

  @Input() json: any;
  @Input() modified: boolean = false;

  protected _voce = Voce;
  protected _jsonVisualizzazione: any;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this._getDettaglioTipiPendenza();
  }

  ngAfterViewInit() {
  }

  _getDettaglioTipiPendenza() {
    let _url = UtilService.URL_TIPI_PENDENZA+'/'+UtilService.EncodeURIComponent(this.json.idTipoPendenza);
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
    _dettaglio.push(new Dato({ label: Voce.IUV_CODEC, value: this.json.codificaIUV }));
    _dettaglio.push(new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(this.json.abilitato)?UtilService.ABILITA[this.json.abilitato]:Voce.NON_PRESENTE }));
    _dettaglio.push(new Dato({ label: Voce.TERZI, value: this.us.hasValue(this.json.pagaTerzi)?UtilService.ABILITA[this.json.pagaTerzi]:Voce.NON_PRESENTE }));
    this.informazioni = _dettaglio.slice(0);

    if(this.json.visualizzazione) {
      this._jsonVisualizzazione = this.json.visualizzazione;
    }
    if (this.json.portaleBackoffice) {
      const _pb: any = this.json.portaleBackoffice;
      this.portaleBackoffice = {
        tipo: new Dato({ label: Voce.TIPO_LAYOUT, value: (_pb.form)?(_pb.form.tipo || Voce.NON_PRESENTE):Voce.NON_PRESENTE }),
        definizione: (_pb.form && _pb.form.definizione) || null,
        validazione: (_pb.validazione || null),
        trasformazione: new Dato({ label: Voce.TRASFORMAZIONE_DATI, value: (_pb.trasformazione)?(_pb.trasformazione.tipo || Voce.NON_PRESENTE):Voce.NON_PRESENTE }),
        definizioneIPO: (_pb.trasformazione && _pb.trasformazione.definizione) || null,
        inoltro: new Dato({ label: Voce.INOLTRO, value: (_pb.inoltro || Voce.NON_PRESENTE) }),
        abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pb.abilitato)?UtilService.ABILITA[_pb.abilitato]:Voce.NON_PRESENTE })
      };
    }
    if (this.json.portalePagamento) {
      const _pp: any = this.json.portalePagamento;
      this.portalePagamento = {
        tipo: new Dato({ label: Voce.TIPO_LAYOUT, value: (_pp.form)?(_pp.form.tipo || Voce.NON_PRESENTE):Voce.NON_PRESENTE }),
        definizione: (_pp.form && _pp.form.definizione) || null,
        impaginazione: (_pp.form && _pp.form.impaginazione) || null,
        validazione: (_pp.validazione || null),
        trasformazione: new Dato({ label: Voce.TRASFORMAZIONE_DATI, value: (_pp.trasformazione)?(_pp.trasformazione.tipo || Voce.NON_PRESENTE):Voce.NON_PRESENTE }),
        definizionePS1: (_pp.trasformazione && _pp.trasformazione.definizione) || null,
        inoltro: new Dato({ label: Voce.INOLTRO, value: (_pp.inoltro || Voce.NON_PRESENTE) }),
        abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pp.abilitato)?UtilService.ABILITA[_pp.abilitato]:Voce.NON_PRESENTE })
      };
    }
    if (this.json.avvisaturaMail) {
      if (this.json.avvisaturaMail.promemoriaAvviso) {
        const _pa: any = this.json.avvisaturaMail.promemoriaAvviso;
        this.promemoraAvvisoNAP = {
          tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_pa.tipo || Voce.NON_PRESENTE) }),
          allegaPdf: new Dato({ label: Voce.ALLEGA_PDF_AVVISO, value: this.us.hasValue(_pa.allegaPdf)?UtilService.ABILITA[_pa.allegaPdf]:Voce.NON_PRESENTE }),
          abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pa.abilitato)?UtilService.ABILITA[_pa.abilitato]:Voce.NON_PRESENTE }),
          oggetto: _pa.oggetto || '',
          messaggio: _pa.messaggio || ''
        };
      }
      if (this.json.avvisaturaMail.promemoriaScadenza) {
        const _ps: any = this.json.avvisaturaMail.promemoriaScadenza;
        this.promemoraAvvisoPSP = {
          tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_ps.tipo || Voce.NON_PRESENTE) }),
          preavviso: new Dato({ label: Voce.GIORNI_PREAVVISO, value: (_ps.preavviso || 10) }),
          abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_ps.abilitato)?UtilService.ABILITA[_ps.abilitato]:Voce.NON_PRESENTE }),
          oggetto: _ps.oggetto || '',
          messaggio: _ps.messaggio || ''
        };
      }
      if (this.json.avvisaturaMail.promemoriaRicevuta) {
        const _pr: any = this.json.avvisaturaMail.promemoriaRicevuta;
        this.promemoraAvvisoNRP = {
          tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_pr.tipo || Voce.NON_PRESENTE) }),
          soloEseguiti: new Dato({ label: Voce.SOLO_PAGAMENTI, value: this.us.hasValue(_pr.soloEseguiti)?UtilService.ABILITA[_pr.soloEseguiti]:Voce.NON_PRESENTE }),
          abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pr.abilitato)?UtilService.ABILITA[_pr.abilitato]:Voce.NON_PRESENTE }),
          allegaPdf: new Dato({ label: Voce.ALLEGA_PDF_AVVISO, value: this.us.hasValue(_pr.allegaPdf)?UtilService.ABILITA[_pr.allegaPdf]:Voce.NON_PRESENTE }),
          oggetto: _pr.oggetto || '',
          messaggio: _pr.messaggio || ''
        };
      }
      if (this.json.avvisaturaAppIO) {
        if (this.json.avvisaturaAppIO.promemoriaAvviso) {
          const _pa: any = this.json.avvisaturaAppIO.promemoriaAvviso;
          this.promemoraAvvisoNAP_IO = {
            tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_pa.tipo || Voce.NON_PRESENTE) }),
            abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pa.abilitato)?UtilService.ABILITA[_pa.abilitato]:Voce.NON_PRESENTE }),
            oggetto: _pa.oggetto || '',
            messaggio: _pa.messaggio || ''
          };
        }
        if (this.json.avvisaturaAppIO.promemoriaScadenza) {
          const _ps: any = this.json.avvisaturaAppIO.promemoriaScadenza;
          this.promemoraAvvisoPSP_IO = {
            tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_ps.tipo || Voce.NON_PRESENTE) }),
            preavviso: new Dato({ label: Voce.GIORNI_PREAVVISO, value: (_ps.preavviso || 10) }),
            abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_ps.abilitato)?UtilService.ABILITA[_ps.abilitato]:Voce.NON_PRESENTE }),
            oggetto: _ps.oggetto || '',
            messaggio: _ps.messaggio || ''
          };
        }
        if (this.json.avvisaturaAppIO.promemoriaRicevuta) {
          const _pr: any = this.json.avvisaturaAppIO.promemoriaRicevuta;
          this.promemoraAvvisoNRP_IO = {
            tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (_pr.tipo || Voce.NON_PRESENTE) }),
            soloEseguiti: new Dato({ label: Voce.SOLO_PAGAMENTI, value: this.us.hasValue(_pr.soloEseguiti)?UtilService.ABILITA[_pr.soloEseguiti]:Voce.NON_PRESENTE }),
            abilitato: new Dato({ label: Voce.ABILITATO, value: this.us.hasValue(_pr.abilitato)?UtilService.ABILITA[_pr.abilitato]:Voce.NON_PRESENTE }),
            oggetto: _pr.oggetto || '',
            messaggio: _pr.messaggio || ''
          };
        }
        if (this.json.tracciatoCsv) {
          this.tracciato = {
            tipo: new Dato({ label: Voce.TIPO_TEMPLATE, value: (this.json.tracciatoCsv.tipo || Voce.NON_PRESENTE) }),
            richiesta: this.json.tracciatoCsv.richiesta || '',
            risposta: this.json.tracciatoCsv.risposta || '',
            intestazione: new Dato({ label: Voce.INTESTAZIONE_ESITO, value: (this.json.tracciatoCsv.intestazione || Voce.NON_PRESENTE) })
          };
        }
      }
    }
  }

  protected _doClick(textOnly: boolean, data: string, title: string) {
    if (textOnly) {
      this._showTextContent(data, title);
    } else {
      this._saveFile(data, (title + '.replaceWithExt'));
    }
  }

  protected _saveFile(data: string, title: string) {
    try {
      let blob: Blob = UtilService.b64toBlob(data);
      let zip = new JSZip();
      zip.file(title, blob);
      zip.generateAsync({type: 'blob'}).then(function (zipData) {
        FileSaver(zipData, 'Archivio.zip');
      });
    } catch (e) {
      console.log('Si è verificato un errore non previsto durante la creazione del file.');
    }
  }

  protected _showTextContent(value: string, title: string) {
    let _decoded = '';
    try{
      // _decoded = decodeURI(atob(value));
      _decoded = decodeURIComponent(atob(value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join('')).toString().trim().replace(/</g,'&lt;');
      const _page = `
        <html>
          <title>${title || 'File'}</title>
          <head><style>html, body { padding: 0; margin: 0; background-color: #404040; color: #fff; } iframe { width: 100%; height: 100%; border: 0;} </style></head>
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
    _service += '/'+UtilService.EncodeURIComponent(mb.info.viewModel['idTipoPendenza']);

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
