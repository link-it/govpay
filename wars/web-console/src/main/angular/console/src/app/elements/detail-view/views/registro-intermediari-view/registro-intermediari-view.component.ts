import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Component({
  selector: 'link-registro-intermediari-view',
  templateUrl: './registro-intermediari-view.component.html',
  styleUrls: ['./registro-intermediari-view.component.scss']
})
export class RegistroIntermediariViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() connettoriSoap = [];
  @Input() connettoriSFtp = { lettura: [], scrittura: [] };
  @Input() stazioni = [];

  @Input() json: any;
  @Input() modified: boolean = false;


  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRegistroIntermediari();
    //this.elencoStazioni();
  }

  ngAfterViewInit() {
  }

  protected dettaglioRegistroIntermediari() {
    let _url = UtilService.URL_REGISTRO_INTERMEDIARI+'/'+encodeURIComponent(this.json.idIntermediario);
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
    let _dettaglio = { informazioni: [], connettoriSoap: [], connettoriSFtp: { lettura: [], scrittura: [] } };
    _dettaglio.informazioni.push(new Dato({ label: Voce.DENOMINAZIONE, value: this.json.denominazione }));
    _dettaglio.informazioni.push(new Dato({ label: Voce.ID_INTERMEDIARIO, value: this.json.idIntermediario }));
    _dettaglio.informazioni.push(new Dato({ label: Voce.PAGO_PA, value: this.json.principalPagoPa }));
    _dettaglio.informazioni.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    if(this.json.servizioPagoPa) {
      _dettaglio.connettoriSoap.push(new Dato({ label: Voce.SERVIZIO_RPT, value: this.json.servizioPagoPa.urlRPT }));
      if(this.json.servizioPagoPa.urlAvvisatura) {
        _dettaglio.connettoriSoap.push(new Dato({label: Voce.SERVIZIO_AVVISATURA, value: this.json.servizioPagoPa.urlAvvisatura}));
      }
      if(this.json.servizioPagoPa.auth) {
        if(this.json.servizioPagoPa.auth.username) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPa.auth.username }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPa.auth.password }));
        }
        if(this.json.servizioPagoPa.auth.tipo) {
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPa.auth.tipo }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPa.auth.ksLocation }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPa.auth.ksPassword }));
          if(this.json.servizioPagoPa.auth.tsLocation) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPa.auth.tsLocation }));
          }
          if(this.json.servizioPagoPa.auth.tsPassword) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPa.auth.tsPassword }));
          }
        }
      } else {
        _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
      }
    }
    if(this.json.servizioFtp) {
      if(this.json.servizioFtp.ftp_lettura) {
        if(this.json.servizioFtp.ftp_lettura.host) {
          _dettaglio.connettoriSFtp.lettura.push(new Dato({label: Voce.HOSTNAME, value: this.json.servizioFtp.ftp_lettura.host }));
        }
        if(this.json.servizioFtp.ftp_lettura.porta) {
          _dettaglio.connettoriSFtp.lettura.push(new Dato({label: Voce.PORTA, value: this.json.servizioFtp.ftp_lettura.porta }));
        }
        if(this.json.servizioFtp.ftp_lettura.username) {
          _dettaglio.connettoriSFtp.lettura.push(new Dato({label: Voce.USERNAME, value: this.json.servizioFtp.ftp_lettura.username }));
        }
        if(this.json.servizioFtp.ftp_lettura.password) {
          _dettaglio.connettoriSFtp.lettura.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioFtp.ftp_lettura.password }));
        }
      }
      if(this.json.servizioFtp.ftp_scrittura) {
        if(this.json.servizioFtp.ftp_scrittura.host) {
          _dettaglio.connettoriSFtp.scrittura.push(new Dato({label: Voce.HOSTNAME, value: this.json.servizioFtp.ftp_scrittura.host }));
        }
        if(this.json.servizioFtp.ftp_scrittura.porta) {
          _dettaglio.connettoriSFtp.scrittura.push(new Dato({label: Voce.PORTA, value: this.json.servizioFtp.ftp_scrittura.porta }));
        }
        if(this.json.servizioFtp.ftp_scrittura.username) {
          _dettaglio.connettoriSFtp.scrittura.push(new Dato({label: Voce.USERNAME, value: this.json.servizioFtp.ftp_scrittura.username }));
        }
        if(this.json.servizioFtp.ftp_scrittura.password) {
          _dettaglio.connettoriSFtp.scrittura.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioFtp.ftp_scrittura.password }));
        }
      }
    }
    this.elencoStazioni();
    this.informazioni = _dettaglio.informazioni.slice(0);
    this.connettoriSoap = _dettaglio.connettoriSoap.slice(0);
    this.connettoriSFtp.lettura = _dettaglio.connettoriSFtp.lettura.slice(0);
    this.connettoriSFtp.scrittura = _dettaglio.connettoriSFtp.scrittura.slice(0);
  }

  protected elencoStazioni() {
    this.stazioni = this.json.stazioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this.mapNewItem(item);
      return p;
    }, this);
  }

  /**
   * Map item Stazione
   * @param item
   * @returns {Standard}
   */
  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    let _st = Dato.arraysToDato(
      [ Voce.PASSWORD, Voce.ABILITATO ],
      [ UtilService.defaultDisplay({ value: item.password }), UtilService.defaultDisplay({ value: UtilService.ABILITA[(item.abilitato).toString()] }) ],
      ', '
    );
    _std.titolo = new Dato({ label: Voce.ID_STAZIONE+': ', value: item.idStazione });
    _std.sottotitolo = _st;

    return  _std;
  }

  protected _iconClick(ref: any, event: any) {
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        this._addEditStazione(true, _ivm.jsonP);
      break;
      case 'delete':
        console.log('delete');
      break;
    }
  }

  protected _editRegistro(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica intermediario',
      templateName: UtilService.INTERMEDIARIO
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  protected _addEditStazione(mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.info = {
      viewModel: _viewModel,
      dialogTitle: (!mode)?'Nuova stazione':'Modifica stazione',
      templateName: UtilService.STAZIONE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p = new Parameters();
      let json = mb.info.viewModel;
      switch(mb.info.templateName) {
        case UtilService.STAZIONE:
          p.jsonP = json;
          p.model = this.mapNewItem(json);
          if(!mb.editMode) {
            this.stazioni.push(p);
          } else {
            this.stazioni.map((item) => {
              if (item.jsonP.idStazione == json.idStazione) {
                Object.keys(p.jsonP).forEach((key) => {
                  item.jsonP[key]= p.jsonP[key];
                });
                item.model = p.model;
              }
              return item;
            });
          }
        break;
        case UtilService.INTERMEDIARIO:
          this.dettaglioRegistroIntermediari();
        break;
      }
    }
  }

  /**
   * Save Registro intermediari|Stazioni (Put to: /intermediari/{idIntermediario} or
   * /intermediari/{idIntermediario}/stazioni/{idStazione} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = UtilService.URL_REGISTRO_INTERMEDIARI;
    let json = JSON.parse(JSON.stringify(mb.info.viewModel));
    let templateName = mb.info.templateName;
    if(templateName == UtilService.INTERMEDIARIO) {
      //(mb.editMode)?json.stazioni = this.json.stazioni:null;
      delete json['idIntermediario'];
      _service += '/'+encodeURIComponent(mb.info.viewModel['idIntermediario']);
    }
    if(templateName == UtilService.STAZIONE) {
      _service += '/'+encodeURIComponent(this.json.idIntermediario);
      _service += UtilService.URL_STAZIONI+'/'+encodeURIComponent(mb.info.viewModel['idStazione']);
      delete json['idStazione'];
    }
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
    return UtilService.defaultDisplay({ value: this.json?this.json.denominazione:null });
  }

  infoDetail(): any {
    return {};
  }
}
