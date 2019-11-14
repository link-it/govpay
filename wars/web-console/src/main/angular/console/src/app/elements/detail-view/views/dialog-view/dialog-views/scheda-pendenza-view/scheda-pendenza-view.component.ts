import { AfterContentChecked, Component, ComponentFactoryResolver, ComponentRef, Input, OnInit, Type, ViewChild, ViewContainerRef } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { IModalDialog } from "../../../../../../classes/interfaces/IModalDialog";
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { GovpayService } from '../../../../../../services/govpay.service';
import { GeneratorsEntryPointList } from '../../../../../../classes/generators-entry-point-list';
import { Voce } from '../../../../../../services/voce.service';

@Component({
  selector: 'link-scheda-pendenza-view',
  templateUrl: './scheda-pendenza-view.component.html',
  styleUrls: ['./scheda-pendenza-view.component.scss']
})
export class SchedaPendenzaViewComponent implements IModalDialog, IFormComponent, OnInit, AfterContentChecked {
  @ViewChild('jsContainer', {read: ViewContainerRef}) _jsContainer: ViewContainerRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _voce = Voce;

  protected _domini: any[] = [];
  protected _tipiPendenzaDominio: any[] = [];
  protected _unitaOperativeDominio: any[] = [];

  protected _componentRef: ComponentRef<any>;
  protected _componentRefType: string = '';
  protected _autoSchemaForm: boolean = false;
  protected _showAutomaticForm: boolean = false;
  protected _formOptions: any = {
    addSubmit: false
  };

  protected jsonSchema;
  protected jsonLayout;
  protected jsonData;

  constructor(public gps: GovpayService, public us:UtilService, private cFR: ComponentFactoryResolver) {
  }

  ngOnInit() {
    this.fGroup.addControl('_autoSchemaForm_ctrl', new FormControl(this._autoSchemaForm, Validators.required));
    this.fGroup.addControl('domini_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('tipiPendenzaDominio_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('unitaOperative_ctrl', new FormControl('', Validators.required));
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].disable();
    this.fGroup.controls['unitaOperative_ctrl'].disable();
    this._loadDomini();
  }

  ngAfterContentChecked() {
  }

  protected _checkChange(event) {
    event.source.checked = this._autoSchemaForm;
  }

  protected validFn(event: any) {
    // console.log(_componentType);
    if(this._componentRefType === UtilService.A2_JSON_SCHEMA_FORM) {
      this._autoSchemaForm = event;
      this.fGroup.controls['_autoSchemaForm_ctrl'].setValue(event);
      // this.fGroup.controls['_autoSchemaForm_ctrl'].updateValueAndValidity({ onlySelf: true });
    }
  }

  protected dominioCmpFn(d1: any, d2: any): boolean {
    return (d1 && d2)?(d1.idDominio === d2.idDominio):(d1 === d2);
  }

  protected tipoPendenzaDominioCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  protected unitaOperativaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idUnita === p2.idUnita):(p1 === p2);
  }

  protected _loadDomini() {
    const _service = UtilService.URL_DOMINI;
    this.gps.getDataService(_service, 'associati=true').subscribe(
    (response) => {
      this.gps.updateSpinner(false);
      this._domini = (response && response.body)?response.body['risultati']:[];
      if(this._domini.length == 1) {
        const _fdom = this._domini[0];
        this.fGroup.controls['domini_ctrl'].setValue(_fdom);
        this._dominiChangeSelection({ value: _fdom });
      }
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected _dominiChangeSelection(event: any) {
    this._tipiPendenzaDominio = [];
    this._unitaOperativeDominio = [];
    this._autoFormReset();
    this.fGroup.controls['unitaOperative_ctrl'].reset();
    this.fGroup.controls['unitaOperative_ctrl'].disable();
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].disable();
    const _url_tipiPendenza = event.value.tipiPendenza + '?form=true&abilitato=true&tipo=dovuto';
    const _url_unitaOperative = event.value.unitaOperative;
    this._loadTipiPendenzaDominio(_url_tipiPendenza);
    this._loadUnitaOperative(_url_unitaOperative);
  }

  protected _loadTipiPendenzaDominio(_dominioRef: string) {
    this.gps.getDataService(_dominioRef).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.fGroup.controls['tipiPendenzaDominio_ctrl'].enable();
        this._tipiPendenzaDominio = (response && response.body)?response.body['risultati']:[];
        if(this._tipiPendenzaDominio.length == 1) {
          const _ftpdom = this._tipiPendenzaDominio[0];
          this.fGroup.controls['tipiPendenzaDominio_ctrl'].setValue(_ftpdom);
          this._tipiPendenzaDominioChangeSelection({ value: _ftpdom });
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _tipiPendenzaDominioChangeSelection(event: any) {
    // console.log(event.value);
    this._autoFormReset();
    let _jsonDecoded = {
      schema: null,
      layout: null
    };
    if(event.value.valori && event.value.valori.form) {
      _jsonDecoded = this._decodeB64ToJson(event.value.valori.form.definizione);
      this._componentRefType = event.value.valori.form.tipo || '';
    } else {
      if(event.value && event.value.form) {
        _jsonDecoded = this._decodeB64ToJson(event.value.form.definizione);
        this._componentRefType = event.value.form.tipo || '';
      }
    }
    if(_jsonDecoded.schema) {
      this.jsonSchema = _jsonDecoded.schema;
      if(_jsonDecoded.layout) {
        this.jsonLayout = _jsonDecoded.layout;
      }
      this._createJsForm();
      this._showAutomaticForm = true;
    }
  }

  protected _loadUnitaOperative(_uoRef: string) {
    this.gps.getDataService(_uoRef, 'associati=true').subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.fGroup.controls['unitaOperative_ctrl'].enable();
        const _uoList: any[] = (response && response.body)?response.body['risultati']:[];
        const _hasEC = (_uoList.filter(uo => uo.idUnita.toLowerCase() === 'ec').length !== 0);
        this._unitaOperativeDominio = _uoList.sort((item1, item2) => {
          return (item1.ragioneSociale>item2.ragioneSociale)?1:(item1.ragioneSociale<item2.ragioneSociale)?-1:0;
        });
        if (_hasEC) {
          _uoList.unshift({ idUnita: null, ragioneSociale: Voce.NESSUNA });
        }
        if(this._unitaOperativeDominio.length == 1) {
          const _uo = this._unitaOperativeDominio[0];
          this.fGroup.controls['unitaOperative_ctrl'].setValue(_uo);
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _unitaOperativaChangeSelection(event: any) {
  }

  protected _decodeB64ToJson(_base: string): any {
    try {
      const _jsonDecode = decodeURIComponent(atob(_base).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(_jsonDecode);
    } catch (e) {
      this.us.alert('Formato json non corretto.');
      return null;
    }
  }

  protected _autoFormReset() {
    this._showAutomaticForm = false;
    this.fGroup.controls['_autoSchemaForm_ctrl'].setValue(true);
    if(this._componentRef) {
      this._componentRef.destroy();
    }
  }

  protected _createJsForm() {
    const componentType = GeneratorsEntryPointList.getComponentByName(this._componentRefType);
    this._formOptions['defautWidgetOptions'] = {
      validationMessages: Voce.VALIDATION_IT_MESSAGES
    };
    this._mapByComponentType(componentType);
  }

  protected _mapByComponentType(cType: Type<any>) {
    const componentFact = this.cFR.resolveComponentFactory(cType);
    this._componentRef = this._jsContainer.createComponent(componentFact);
    if(this._componentRefType === UtilService.A2_JSON_SCHEMA_FORM) {
      this._componentRef.instance.schema = this.jsonSchema;
      this._componentRef.instance.layout = this.jsonLayout;
      // this._componentRef.instance.data = this.jsonData; // DEBUG ONLY
      this._componentRef.instance.options = this._formOptions;
      this._componentRef.instance.framework = 'material-design';
      this._componentRef.instance.isValid.subscribe((data: any) => {
        // console.log(data);
        this.validFn(data);
      });
    } else {
      this.us.alert('Auto generatore non definito.');
    }
  }

  /**
   * Save nuova pendenza (Post to: /pendenze )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    const body = JSON.parse(JSON.stringify(mb.info.viewModel));
    const _url = UtilService.URL_PENDENZE + '/' + encodeURIComponent(body.idDominio) + '/' + encodeURIComponent(body.idTipoPendenza);
    const _query = mb.info.idUnitaOperativa?'idUnitaOperativa=' + mb.info.idUnitaOperativa:null;
    delete body.idDominio;
    delete body.idTipoPendenza;
    delete body.idUnitaOperativa;
    this.gps.saveData(_url, body, _query, UtilService.METHODS.POST).subscribe(
    () => {
      this.gps.updateSpinner(false);
      responseService.next(true);
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  refresh(mb: ModalBehavior) {}

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json: any = {};

    if(this._componentRefType === UtilService.A2_JSON_SCHEMA_FORM) {
      _json = this._componentRef.instance.jsf.data;
      _json.idDominio = this.fGroup.controls['domini_ctrl'].value.idDominio;
      _json.idTipoPendenza = this.fGroup.controls['tipiPendenzaDominio_ctrl'].value.idTipoPendenza;
      _json.idUnitaOperativa = this.fGroup.controls['unitaOperative_ctrl'].value.idUnita;
    }

    return _json;
  }
}
