import { AfterContentChecked, Component, ComponentFactoryResolver, ComponentRef, Input, OnInit, Type, ViewChild, ViewContainerRef } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { IModalDialog } from "../../../../../../classes/interfaces/IModalDialog";
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { GovpayService } from '../../../../../../services/govpay.service';
import { GeneratorsEntryPointList } from '../../../../../../classes/generators-entry-point-list';

@Component({
  selector: 'link-scheda-pendenza-view',
  templateUrl: './scheda-pendenza-view.component.html',
  styleUrls: ['./scheda-pendenza-view.component.scss']
})
export class SchedaPendenzaViewComponent implements IModalDialog, IFormComponent, OnInit, AfterContentChecked {
  @ViewChild('jsContainer', {read: ViewContainerRef}) _jsContainer: ViewContainerRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _domini: any[];
  protected _tipiPendenzaDominio: any[];

  protected _componentRef: ComponentRef<any>;
  protected _componentRefType: string = UtilService.A2_JSON_SCHEMA_FORM;
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
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].disable();
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

  protected _loadDomini() {
    const _service = UtilService.URL_DOMINI;
    this.gps.getDataService(_service).subscribe(
    (response) => {
      this.gps.updateSpinner(false);
      this._domini = (response && response.body)?response.body['risultati']:[];
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected _dominiChangeSelection(event: any) {
    this._tipiPendenzaDominio = [];
    this._autoFormReset();
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].disable();
    this._loadTipiPendenzaDominio(event.value.tipiPendenza);
  }

  protected _loadTipiPendenzaDominio(_dominioRef: string) {
    this.gps.getDataService(_dominioRef).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.fGroup.controls['tipiPendenzaDominio_ctrl'].enable();
        this._tipiPendenzaDominio = (response && response.body)?response.body['risultati']:[];
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _tipiPendenzaDominioChangeSelection(event: any) {
    // console.log(event.value);
    this._autoFormReset();
    const _data = {
      schema: null,
      layout: null
    };
    if(event.value.valori && event.value.valori.schema) {
     _data.schema = event.value.valori.schema;
    } else {
     _data.schema = event.value.schema?event.value.schema:null;
    }
    if(event.value.valori && event.value.valori.datiAllegati) {
     _data.layout = event.value.valori.datiAllegati.layout?event.value.valori.datiAllegati.layout:null;
    } else {
     _data.layout = event.value.datiAllegati.layout?event.value.datiAllegati.layout:null;
    }
    if(_data.schema) {
      this.jsonSchema = _data.schema;
      if(_data.layout) {
        this.jsonLayout = _data.layout;
      }
      this._createJsForm();
      this._showAutomaticForm = true;
    }
  }

  protected _autoFormReset() {
    this._showAutomaticForm = false;
    if(this._componentRef) {
      this._componentRef.destroy();
    }
  }

  protected _createJsForm(_typeGenerator: string = '') {
    const componentType = GeneratorsEntryPointList.getComponentByName(this._componentRefType);
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
    const body = mb.info.viewModel;
    this.gps.saveData(UtilService.URL_PENDENZE, body, null, UtilService.METHODS.POST).subscribe(
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
    }

    return _json;
  }
}
