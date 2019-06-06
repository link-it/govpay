import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormInput } from '../../../../../../classes/view/form-input';

@Component({
  selector: 'link-tipi-pendenza-view',
  templateUrl: './tipi-pendenza-view.component.html',
  styleUrls: ['./tipi-pendenza-view.component.scss']
})
export class TipiPendenzaViewComponent implements IFormComponent,  OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _jsonFormInput: boolean = false;

  protected _tipologie: any[] = UtilService.TIPOLOGIA_PENDENZA;

  // protected _generatori: any[] = [];
  protected _schemaSelected: boolean = false;
  protected _layoutSelected: boolean = false;
  protected _schema: string = 'Form schema';
  protected _datiAllegati: string = 'Form layout';

  constructor(protected us: UtilService) {}

  ngOnInit() {
    if(this.json instanceof FormInput) {
      this._jsonFormInput = true;
      this.fGroup.addControl('idTipoPendenza_ctrl', new FormControl('', Validators.required));
    } else {
      this.fGroup.addControl('idTipoPendenza_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('descrizione_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('tipo_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
      this.fGroup.addControl('abilitato_ctrl', new FormControl(false));
      this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(false));
      // this.fGroup.addControl('generatore_ctrl', new FormControl(''));
      this.fGroup.addControl('schema_ctrl', new FormControl(''));
      this.fGroup.addControl('datiAllegati_ctrl', new FormControl(''));
    }
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        if(this.json instanceof FormInput && this.json.values.length == 0) {
          this.fGroup.controls[ 'idTipoPendenza_ctrl' ].disable();
        }
        if(!(this.json instanceof FormInput)) {
          this.fGroup.controls['idTipoPendenza_ctrl'].disable();
          this.fGroup.controls['idTipoPendenza_ctrl'].setValue((this.json.idTipoPendenza)?this.json.idTipoPendenza:'');
          this.fGroup.controls['descrizione_ctrl'].setValue((this.json.descrizione)?this.json.descrizione:'');
          this.fGroup.controls['tipo_ctrl'].setValue((this.json.tipo)?this.json.tipo:'');
          this.fGroup.controls['codificaIUV_ctrl'].setValue((this.json.codificaIUV)?this.json.codificaIUV:'');
          this.fGroup.controls['pagaTerzi_ctrl'].setValue(this.json.pagaTerzi);
          this.fGroup.controls['abilitato_ctrl'].setValue(this.json.abilitato);
          // this.fGroup.controls['generatore_ctrl'].setValue((this.json.datiAllegati && this.json.datiAllegati.generatore)?this.json.datiAllegati.generatore:'');
          this.fGroup.controls['schema_ctrl'].setValue((this.json.schema)?this.json.schema:'');
          this.fGroup.controls['datiAllegati_ctrl'].setValue((this.json.datiAllegati && this.json.datiAllegati.layout)?this.json.datiAllegati.layout:'');
        }
      }
    });
  }

  //Schema Json Load
  protected readJsonSchema(event)
  {
    if(event.currentTarget.files && event.currentTarget.files.length != 0) {
      const reader = new FileReader();
      const ct = event.currentTarget;
      const _name = ct.files[0].name;
      reader.onerror = this._error.bind(this);
      reader.onload = function() {
        try {
          const json = JSON.parse(reader.result);
          if(json && !(json instanceof String)) {
            this._schema = _name;
            this._schemaSelected = true;
            this.fGroup.controls['schema_ctrl'].setValue(json);
          } else {
            ct.value = '';
            this._schemaSelected = false;
            this.us.alert('Il contenuto del documento non rispetta il formato json.');
          }
        } catch (e) {
          ct.value = '';
          this._schemaSelected = false;
          this.us.alert(e);
        }
      }.bind(this);
      reader.readAsBinaryString(event.currentTarget.files[0]);
    }
  }

  protected readJsonLayout(event)
  {
    if(event.currentTarget.files && event.currentTarget.files.length != 0) {
      const reader = new FileReader();
      const ct = event.currentTarget;
      const _name = ct.files[0].name;
      reader.onerror = this._error.bind(this);
      reader.onload = function() {
        try {
          const json = JSON.parse(reader.result);
          if(json && !(json instanceof String)) {
            this._datiAllegati = _name;
            this._layoutSelected = true;
            this.fGroup.controls['datiAllegati_ctrl'].setValue(json);
          } else {
            ct.value = '';
            this._layoutSelected = false;
            this.us.alert('Il contenuto del documento non rispetta il formato json.');
          }
        } catch (e) {
          ct.value = '';
          this._layoutSelected = false;
          this.us.alert(e);
        }
      }.bind(this);
      reader.readAsBinaryString(event.currentTarget.files[0]);
    }
  }

  protected _clear(el: any, ctrl: string, schema: boolean = false) {
    el.value = '';
    this['_'+ctrl] = schema?'Form schema':'Form layout';
    if(schema) {
      this._schemaSelected = false;
    } else {
      this._layoutSelected = false;
    }
    this.fGroup.controls[ctrl+'_ctrl'].setValue('');
  };

  private _error(event) {
    console.log(event);
    this.us.alert('Impossibile leggere il file.');
  };

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    if(this.json instanceof FormInput) {
      _json.values = [];
      _info['idTipoPendenza_ctrl'].forEach((el) => {
        _json.values.push({
          idTipoPendenza: el.value,
          descrizione: el.label
        });
      });
    } else {
      _json.idTipoPendenza = (!this.fGroup.controls['idTipoPendenza_ctrl'].disabled)?_info['idTipoPendenza_ctrl']:this.json.idTipoPendenza;
      _json.descrizione = (_info['descrizione_ctrl'])?_info['descrizione_ctrl']:null;
      _json.tipo = _info['tipo_ctrl'];
      _json.codificaIUV = (_info['codificaIUV_ctrl'])?_info['codificaIUV_ctrl']:null;
      _json.pagaTerzi = _info['pagaTerzi_ctrl'];
      _json.abilitato = _info['abilitato_ctrl'];
      _json.schema = _info['schema_ctrl']?_info['schema_ctrl']:null;
      _json.datiAllegati = null;
      if(_info['datiAllegati_ctrl']) {
        _json.datiAllegati = {
          // generatore: _info[generatore_ctrl']',
          layout: _info['datiAllegati_ctrl']
        };
      }
    }
    return _json;
  }
}
