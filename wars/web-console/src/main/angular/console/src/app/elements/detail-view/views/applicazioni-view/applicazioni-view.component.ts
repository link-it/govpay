import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from "../../../../services/voce.service";
import { Dato } from '../../../../classes/view/dato';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';

@Component({
  selector: 'link-applicazioni-view',
  templateUrl: './applicazioni-view.component.html',
  styleUrls: ['./applicazioni-view.component.scss']
})
export class ApplicazioniViewComponent implements IModalDialog, OnInit, AfterViewInit{

  @Input() domini = [];
  @Input() tipiPendenza = [];
  @Input() informazioni = [];
  @Input() aapi = [];
  @Input() avvisi = [];
  @Input() serviziApi = [];
  @Input() ruoli = [];
  // @Input() notifiche = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected voce = Voce;
  protected DOMINIO = UtilService.DOMINIO;
  protected ENTRATA = UtilService.ENTRATA;
  protected ACL = UtilService.ACL;
  protected ADD = UtilService.PATCH_METHODS.ADD;
  protected _OPERAZIONI = Voce.OPERAZIONI;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioApplicazione();
  }

  ngAfterViewInit() {
  }

  protected dettaglioApplicazione() {
    let _url = UtilService.URL_APPLICAZIONI+'/'+encodeURIComponent(this.json.idA2A);
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
    let _dettaglio = { info: [], api: [], avviso: [], serviziApi: [], domini: [], tipiPendenza: [], ruoli: [] };
    _dettaglio.info.push(new Dato({ label: Voce.PRINCIPAL, value: this.json.principal }));
    _dettaglio.info.push(new Dato({ label: Voce.ID_A2A, value: this.json.idA2A }));
    _dettaglio.info.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    _dettaglio.api.push(new Dato({ label: Voce.API_PAGAMENTI, value: UtilService.ABILITA[this.json.apiPagamenti.toString()] }));
    _dettaglio.api.push(new Dato({ label: Voce.API_PENDENZE, value: UtilService.ABILITA[this.json.apiPendenze.toString()] }));
    _dettaglio.api.push(new Dato({ label: Voce.API_RAGIONERIA, value: UtilService.ABILITA[this.json.apiRagioneria.toString()] }));
    if(this.json.codificaAvvisi) {
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_CODEC, value: this.json.codificaAvvisi.codificaIuv }));
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_REGEX, value: this.json.codificaAvvisi.regExpIuv }));
      _dettaglio.avviso.push(new Dato({ label: Voce.IUV_GENERATION, value: UtilService.ABILITA[this.json.codificaAvvisi.generazioneIuvInterna.toString()] }));
    }
    if(this.json.servizioIntegrazione) {
      _dettaglio.serviziApi.push(new Dato({ label: Voce.URL, value: this.json.servizioIntegrazione.url }));
      _dettaglio.serviziApi.push(new Dato({ label: Voce.VERSIONE_API, value: this.json.servizioIntegrazione.versioneApi }));
      if(this.json.servizioIntegrazione.auth) {
        _dettaglio.serviziApi.push(new Dato({ label: Voce.TIPO_AUTH, value: this.json.servizioIntegrazione.auth.hasOwnProperty('username')?'HTTP Basic':'SSL' }));
        if(this.json.servizioIntegrazione.auth.username) {
          _dettaglio.serviziApi.push(new Dato({label: Voce.USERNAME, value: this.json.servizioIntegrazione.auth.username }));
          _dettaglio.serviziApi.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioIntegrazione.auth.password }));
        }
        if(this.json.servizioIntegrazione.auth.tipo) {
          _dettaglio.serviziApi.push(new Dato({label: Voce.TIPO, value: this.json.servizioIntegrazione.auth.tipo }));
          _dettaglio.serviziApi.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioIntegrazione.auth.ksLocation }));
          _dettaglio.serviziApi.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioIntegrazione.auth.ksPassword }));
          if(this.json.servizioIntegrazione.auth.tsLocation) {
            _dettaglio.serviziApi.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioIntegrazione.auth.tsLocation }));
          }
          if(this.json.servizioIntegrazione.auth.tsPassword) {
            _dettaglio.serviziApi.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioIntegrazione.auth.tsPassword }));
          }
        }
      }
    }

/*if(this.json.domini && this.json.domini.length != 0) {
  this.json.domini.forEach((item, index) => {
    _dettaglio.domini.push(new Dato({ label: (index != 0)?'':Voce.ENTI_CREDITORI, value: item.ragioneSociale }));
  });
} else {
  _dettaglio.domini.push(new Dato({ label: Voce.ENTI_CREDITORI, value: Voce.NESSUNO }));
}*/
    _dettaglio.domini = this.elencoDominiMap(this.json.domini || []);

    if(this.json.tipiPendenza && this.json.tipiPendenza.length != 0) {
      this.json.tipiPendenza.forEach((item, index) => {
        _dettaglio.tipiPendenza.push(new Dato({ label: (index != 0)?'':Voce.TIPI_PENDENZA, value: item.descrizione }));
      });
    } else {
      _dettaglio.tipiPendenza.push(new Dato({ label: Voce.TIPI_PENDENZA, value: Voce.NESSUNO }));
    }
    if(this.json.ruoli && this.json.ruoli.length != 0) {
      this.json.ruoli.forEach((item, index) => {
        _dettaglio.ruoli.push(new Dato({ label: (index != 0)?'':Voce.RUOLI, value: item.id }));
      });
    } else {
      _dettaglio.ruoli.push(new Dato({ label: Voce.RUOLI, value: Voce.NESSUNO }));
    }

    this.informazioni = _dettaglio.info.slice(0);
    this.aapi = _dettaglio.api.slice(0);
    this.avvisi = _dettaglio.avviso.slice(0);
    this.serviziApi = _dettaglio.serviziApi.slice(0);
    this.domini = _dettaglio.domini.slice(0);
    this.tipiPendenza = _dettaglio.tipiPendenza.slice(0);
    this.ruoli = _dettaglio.ruoli.slice(0);
  }


  protected elencoDominiMap(data: any[]) {
    return data.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this.mapNewItem(item);
      return p;
    }, this);
  }

  /**
   * Map item Dominio
   * @param item
   * @returns {Standard}
   */
  protected mapNewItem(item: any): Standard {
    let _values = (item.unitaOperative || []).map(uo => {
      return uo.ragioneSociale;
    });
    let _std = new Standard();
    let _st = new Dato({
      label: `${Voce.UNITA_OPERATIVE}: `
    });
    if (_values.length !== 0) {
      _st.value = _values.join(', ');
    } else {
      _st.value = (item.idDominio === '*')?Voce.TUTTE:Voce.NESSUNA
    }
    _std.titolo = new Dato({ label: item.ragioneSociale });
    _std.sottotitolo = _st;

    return  _std;
  }

  protected _editApplicazione(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica applicazione',
      templateName: UtilService.APPLICAZIONE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      if(mb.info.templateName === UtilService.APPLICAZIONE) {
        this.json = mb.info.viewModel;
        this.json.domini = this.json.domini.map(el => el.jsonP);
        this.mapJsonDetail();
      }
    }
  }

  /**
   * Save Applicazione|Dominio|tipiPendenza|Acl (Put to: /applicazioni/{idA2A} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _method = null;
      let _id = (mb.editMode)?this.json['idA2A']:mb.info.viewModel['idA2A'];
      let _service = UtilService.URL_APPLICAZIONI+'/'+encodeURIComponent(_id);
      if(mb.info.templateName === UtilService.APPLICAZIONE) {
        _json = {
          idA2A: mb.info.viewModel.idA2A,
          principal: mb.info.viewModel.principal,
          codificaAvvisi: mb.info.viewModel.codificaAvvisi,
          domini: mb.info.viewModel.domini,
          tipiPendenza: mb.info.viewModel.tipiPendenza,
          apiPagamenti: mb.info.viewModel.apiPagamenti,
          apiPendenze: mb.info.viewModel.apiPendenze,
          apiRagioneria: mb.info.viewModel.apiRagioneria,
          acl: mb.info.viewModel.acl,
          ruoli: mb.info.viewModel.ruoli,
          servizioIntegrazione: mb.info.viewModel.servizioIntegrazione,
          abilitato: mb.info.viewModel.abilitato
        };
        delete _json.idA2A;
        _json.domini = _json.domini.map((d) => {
          const _d = {
            idDominio: d.jsonP.idDominio,
            ragioneSociale: d.jsonP.ragioneSociale,
          };
          if (d.jsonP.unitaOperative && d.jsonP.unitaOperative.length !== 0) {
            _d['unitaOperative'] = d.jsonP.unitaOperative.map(uo => {
              return {
                idUnita: uo.idUnita,
                ragioneSociale: uo.ragioneSociale
              }
            });
          }
          return _d;
        });
        _json.tipiPendenza = _json.tipiPendenza.map((e) => {
          return e.idTipoPendenza;
        });
        _json.ruoli = _json.ruoli.map((e) => {
          return e.id;
        });
      }
      this.gps.saveData(_service, _json, _query, _method).subscribe(
        (response) => {
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.idA2A:null });
  }

  infoDetail(): any {
    return {};
  }

}
