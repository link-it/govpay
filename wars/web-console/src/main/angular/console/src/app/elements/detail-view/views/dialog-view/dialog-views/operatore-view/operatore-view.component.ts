import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { Parameters } from '../../../../../../classes/parameters';
import { Standard } from '../../../../../../classes/view/standard';
import { Dato } from '../../../../../../classes/view/dato';
import { IModalDialog } from '../../../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Component({
  selector: 'link-operatore-view',
  templateUrl: './operatore-view.component.html',
  styleUrls: ['./operatore-view.component.scss']
})
export class OperatoreViewComponent implements IModalDialog, IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;

  // protected acl = [];
  protected ruoli = [];
  protected domini = [];
  protected tipiPendenza = [];

  protected voce = Voce;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.elencoDominiPendenzeRuoli();

    this.fGroup.addControl('principal_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(true));
    this.fGroup.addControl('ragioneSociale_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl(''));
    this.fGroup.addControl('ruoli_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['principal_ctrl'].disable();
        this.fGroup.controls['principal_ctrl'].setValue(this.json.principal);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        this.fGroup.controls['ragioneSociale_ctrl'].setValue(this.json.ragioneSociale);
        if(this.json.tipiPendenza) {
          this.fGroup.controls[ 'tipoPendenza_ctrl' ].setValue(this.json.tipiPendenza);
        }
        if(this.json.ruoli) {
          this.fGroup.controls[ 'ruoli_ctrl' ].setValue(this.json.ruoli);
        }
      }
    });
  }

  protected pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  protected ruoliCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.id === p2.id):(p1 === p2);
  }

  protected elencoDominiPendenzeRuoli() {
    let _services: string[] = [];
    _services.push(UtilService.URL_TIPI_PENDENZA);
    _services.push(UtilService.URL_RUOLI);
    this.gps.updateSpinner(true);
    this.gps.forkService(_services).subscribe(function (_response) {
        if(_response) {
          this.domini = (this.json)?this.elencoDominiMap(this.json.domini || []):[];
          this.tipiPendenza = _response[0].body.risultati;
          this.tipiPendenza.unshift({ descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTE_ENTRATE.value });
          this.ruoli = _response[1].body.risultati;
          this.gps.updateSpinner(false);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
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

  /**
   * Add (Enti,Unità operative)
   * @param {boolean} mode
   * @param _viewModel
   * @private
   */
  protected _addEnteUO(mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.info = {
      viewModel: _viewModel,
      parent: this,
      dialogTitle: 'Nuova autorizzazione',
      templateName: UtilService.AUTORIZZAZIONE_ENTE_UO
    };
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  protected _iconClick(ref: any, event: any) {
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'delete':
        this.domini = this.domini.filter(d => {
          return d.jsonP.idDominio !== _ivm.jsonP.idDominio;
        });
        break;
    }
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p = new Parameters();
      let json = mb.info.viewModel;
      const _newItem = {
        idDominio: json.dominio.idDominio,
        ragioneSociale: json.dominio.ragioneSociale,
        unitaOperative: json.unitaOperative || []
      };
      p.jsonP = _newItem;
      p.model = this.mapNewItem(_newItem);
      (_newItem.idDominio === '*')?this.domini = [p]:this.domini.push(p);
      if (this.domini.length > 1) {
        this.domini = this.domini.filter(el => {
          return el.jsonP.idDominio !== '*';
        });
      }
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    _json.principal = (!this.fGroup.controls['principal_ctrl'].disabled)?_info['principal_ctrl']: this.json.principal;
    _json.abilitato = _info['abilita_ctrl'];
    _json.ragioneSociale = (_info['ragioneSociale_ctrl'])?_info['ragioneSociale_ctrl']:null;
    _json.domini = this.domini || [];
    _json.tipiPendenza = (_info['tipoPendenza_ctrl'])?_info['tipoPendenza_ctrl']:[];
    _json.ruoli = (_info['ruoli_ctrl'])?_info['ruoli_ctrl']:[];
    _json.acl =  this.json?(this.json.acl || []):[];

    return _json;
  }

}
