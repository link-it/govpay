import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { IModalDialog } from '../../../../../../classes/interfaces/IModalDialog';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { GovpayService } from '../../../../../../services/govpay.service';
import { Parameters } from '../../../../../../classes/parameters';
import { Standard } from '../../../../../../classes/view/standard';
import { Dato } from '../../../../../../classes/view/dato';

@Component({
  selector: 'link-entrata-dominio-view',
  templateUrl: './entrata-dominio-view.component.html',
  styleUrls: ['./entrata-dominio-view.component.scss']
})
export class EntrataDominioViewComponent implements IModalDialog, IFormComponent,  OnInit, AfterViewInit {
  @ViewChild('tc') _tipoContabilita: ElementRef;
  @ViewChild('cc') _codiceContabilita: ElementRef;
  @ViewChild('ciuv') _codificaIUV: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected contabilita_items: any[];
  protected tipiEntrata_items: any[];
  protected ibanAccredito_items: any[];
  protected ibanAppoggio_items: any[];

  protected _phTc: string = 'Tipo contabilità';
  protected _phCc: string = 'Codice contabilità';
  protected _phCiuv: string = 'Codifica IUV';

  constructor(public gps: GovpayService, public us:UtilService) {
    this.contabilita_items = us.tipiContabilita();
    this._getEntrate();
  }

  ngOnInit() {
    this.ibanAccredito_items = this.parent.iban_cc.slice(0);
    this.ibanAppoggio_items = this.parent.iban_cc.slice(0);
    this.fGroup.addControl('tipoEntrata_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ibanAccredito_ctrl', new FormControl(''));
    this.fGroup.addControl('ibanAppoggio_ctrl', new FormControl({ value: '', disabled: true }));
    this.fGroup.addControl('tipoContabilita_ctrl', new FormControl(''));
    this.fGroup.addControl('codiceContabilita_ctrl', new FormControl(''));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['tipoEntrata_ctrl'].disable();
        this.fGroup.controls['tipoEntrata_ctrl'].setValue((this.json.tipoEntrata)?this.json.tipoEntrata:'');
        this.fGroup.controls['ibanAccredito_ctrl'].setValue((this.json.ibanAccredito)?this.json.ibanAccredito:'');
        this.fGroup.controls['ibanAppoggio_ctrl'].setValue((this.json.ibanAppoggio)?this.json.ibanAppoggio:'');
        this.fGroup.controls['tipoContabilita_ctrl'].setValue((this.json.tipoContabilita)?this.json.tipoContabilita:'');
        this.fGroup.controls['codiceContabilita_ctrl'].setValue((this.json.codiceContabilita)?this.json.codiceContabilita:'');
        this.fGroup.controls['codificaIUV_ctrl'].setValue((this.json.codificaIUV)?this.json.codificaIUV:'');
        this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
        (this.json.ibanAccredito)?this.fGroup.controls['ibanAppoggio_ctrl'].enable():this.fGroup.controls['ibanAppoggio_ctrl'].disable();
      }
    });
  }

  protected _onChangeSelection(target) {
    if(!target.value) {
      let _mb: ModalBehavior = new ModalBehavior();
      _mb.info = {
        dialogTitle: 'Nuova entrata',
        templateName: UtilService.ENTRATA
      };
      _mb.async_callback = this.save.bind(this);
      _mb.closure = this.refresh.bind(this);
      UtilService.dialogBehavior.next(_mb);
    } else {
      this._resetDefault();
      this._updateValues(target.value);
    }
  }

  protected _tipoEntrataComparingFct(option: any, selection: any): boolean {
    return (selection && option.idEntrata == selection.idEntrata);
  }


  protected _onIbanChangeSelection(_accredito: any, _appoggio: any, isAccredito: boolean) {
    if(isAccredito && _accredito.value) {
      this.fGroup.controls['ibanAppoggio_ctrl'].setValue('');
      this.ibanAppoggio_items = this.parent.iban_cc.filter((iban) => {
        return (iban.jsonP.ibanAccredito != _accredito.value);
      });
    }
    (_accredito.value)?this.fGroup.controls['ibanAppoggio_ctrl'].enable():this.fGroup.controls['ibanAppoggio_ctrl'].disable();

  }

  /**
   * Filter by list and key to {label,value} object mapped list
   * @param {any[]} fullList
   * @param {Parameters[]} checkList
   * @param {string} key
   * @returns { any[] }
   */
  protected filterByList(fullList: any[], checkList: Parameters[], key: string): any[] {
    return fullList.filter((item) => {
      let _keep: boolean = true;
      checkList.forEach((el) => {
        if(el.jsonP[key] == item.jsonP[key]) {
          _keep = false;
        }
      });
      return _keep;
    });
  }

  /**
   * Check item index list by key
   * @param item: json item
   * @param {Parameters[]} checkList
   * @param {string} key
   * @returns {boolean}
   */
  protected checkItemIndex(item: any, checkList: Parameters[], key: string): boolean {
    let _hasEntry: boolean = false;
    checkList.forEach((el) => {
      if(el.jsonP[key] == item[key]) {
        _hasEntry = true;
      }
    });
    return _hasEntry;
  }

  protected _getEntrate() {
    let _service = UtilService.URL_ENTRATE;
    this.gps.getDataService(_service).subscribe(
    (_response) => {
      this.gps.updateSpinner(false);
      let _body = _response.body;
      let p: Parameters;
      let _de = _body['risultati'].map(function(item) {
        p = new Parameters();
        p.jsonP = item;
        p.model = this.mapNewItem(item);
        return p;
      }, this);
      if(!this.json) {
        //Insert mode
        this.tipiEntrata_items = this.filterByList(_de.slice(0), this.parent.entrate, 'idEntrata');
      } else {
        //Edit mode
        this.tipiEntrata_items = _de;
      }
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    _std.titolo = new Dato({ label: item.descrizione, value: '' });
    _std.sottotitolo = new Dato({ label: Voce.ID_ENTRATA+': ', value: item.idEntrata });
    return _std;
  }

  protected _onChange(value: string) {
    let i = value.indexOf(' (');
    (i != -1)?value = value.substr(0, i):null;
  }

  // protected _updatePlaceholders(json: any) {
  //   (json.tipoContabilita)?this._phTc = Voce.TIPO_CONTABILITA+' ('+Voce.VALORE_PREDEFINITO+': ' + UtilService.TIPI_CONTABILITA[json.tipoContabilita] + ')':null;
  //   (json.codiceContabilita)?this._phCc = Voce.CODICE_CONTABILITA+' ('+Voce.VALORE_PREDEFINITO+': ' + json.codiceContabilita + ')':null;
  //   (json.codificaIUV)?this._phCiuv = Voce.IUV_CODEC+' ('+Voce.VALORE_PREDEFINITO+': ' + json.codificaIUV + ')':null;
  // }
  protected _updateValues(json: any) {
    (json.tipoContabilita)?this.fGroup.controls['tipoContabilita_ctrl'].setValue(json.tipoContabilita):null;
    (json.codiceContabilita)?this.fGroup.controls['codiceContabilita_ctrl'].setValue(json.codiceContabilita):null;
    (json.codificaIUV)?this.fGroup.controls['codificaIUV_ctrl'].setValue(json.codificaIUV):null;
  }

  protected _resetDefault() {
    this.fGroup.controls['tipoContabilita_ctrl'].setValue('');
    this.fGroup.controls['codiceContabilita_ctrl'].setValue('');
    this.fGroup.controls['codificaIUV_ctrl'].setValue('');
  }

  /**
   * Save tipo entrata (Put to: /entrate/{idEntrata} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _hasEntry = this.checkItemIndex(mb.info.viewModel, this.parent.entrate, 'idEntrata');
    let _hasLocalEntry = this.checkItemIndex(mb.info.viewModel, this.tipiEntrata_items, 'idEntrata');
    if(!_hasEntry && !_hasLocalEntry) {
      let _service = UtilService.URL_ENTRATE+'/'+encodeURIComponent(mb.info.viewModel.idEntrata);
      let _json = JSON.parse(JSON.stringify(mb.info.viewModel));
      delete _json.idEntrata;
      this.gps.saveData(_service, _json).subscribe(
        () => {
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    } else {
      this.us.alert('Tipo informazione "'+mb.info.viewModel.idEntrata+'" già associata.');
    }
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    this._resetDefault();
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p: Parameters = new Parameters();
      p.jsonP = mb.info.viewModel;
      p.model = this.mapNewItem(mb.info.viewModel);
      this.tipiEntrata_items.push(p);

      this.fGroup.controls['tipoEntrata_ctrl'].setValue(p.jsonP);
      this._updateValues(p.jsonP);
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    if(!this.fGroup.controls['tipoEntrata_ctrl'].disabled) {
      _json.tipoEntrata = _info['tipoEntrata_ctrl'];
      _json.idEntrata = _info['tipoEntrata_ctrl'].idEntrata;
    } else {
      _json.tipoEntrata = this.json.tipoEntrata;
      _json.idEntrata = this.json.idEntrata;
    }
    _json.ibanAccredito = (_info['ibanAccredito_ctrl'])?_info['ibanAccredito_ctrl']:null;
    _json.ibanAppoggio = (_info['ibanAppoggio_ctrl'])?_info['ibanAppoggio_ctrl']:null;
    _json.tipoContabilita = (_info['tipoContabilita_ctrl'])?_info['tipoContabilita_ctrl']:null;
    _json.codiceContabilita = (_info['codiceContabilita_ctrl'])?_info['codiceContabilita_ctrl']:null;
    _json.codificaIUV = (_info['codificaIUV_ctrl'])?_info['codificaIUV_ctrl']:null;
    _json.abilitato = _info['abilita_ctrl'];

    return _json;
  }

}
