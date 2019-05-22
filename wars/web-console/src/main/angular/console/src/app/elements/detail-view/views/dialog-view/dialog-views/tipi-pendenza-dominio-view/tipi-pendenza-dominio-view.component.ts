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
  selector: 'link-tipi-pendenza-dominio-view',
  templateUrl: './tipi-pendenza-dominio-view.component.html',
  styleUrls: ['./tipi-pendenza-dominio-view.component.scss']
})
export class TipiPendenzaDominioViewComponent implements IModalDialog, IFormComponent,  OnInit, AfterViewInit {
  @ViewChild('iSchemaBrowse') _iSchemaBrowse: ElementRef;
  @ViewChild('iLayoutBrowse') _iLayoutBrowse: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected tipiPendenza_items: any[];

  protected voce = Voce;
  protected _phCiuv: string = 'Codifica IUV';

  // protected _generatori: any[] = [];
  protected _schemaSelected: boolean = false;
  protected _trashSchema: boolean = false;
  protected _jsonSchemaSelected: any;
  protected _layoutSelected: boolean = false;
  protected _trashlayout: boolean = false;
  protected _jsonLayoutSelected: any;
  protected _schema: string = 'Form schema';
  protected _datiAllegati: string = 'Form layout';


  constructor(public gps: GovpayService, public us: UtilService) {
    this._getTipiPendenza();
  }

  ngOnInit() {
    this._trashSchema = false;
    this._trashlayout = false;
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(''));
    this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(null));
    // this.fGroup.addControl('generatore_ctrl', new FormControl(''));
    this.fGroup.addControl('schema_ctrl', new FormControl(''));
    this.fGroup.addControl('datiAllegati_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['tipoPendenza_ctrl'].disable();
        this.fGroup.controls['tipoPendenza_ctrl'].setValue((this.json)?this.json:'');
        if(this.json.valori) {
          this.fGroup.controls['codificaIUV_ctrl'].setValue((this.json.valori.codificaIUV)?this.json.valori.codificaIUV:'');
          this.fGroup.controls['abilita_ctrl'].setValue(this.json.valori.abilitato);
          this.fGroup.controls['pagaTerzi_ctrl'].setValue(this.json.valori.pagaTerzi);
          this._updateJson(this.json);
        }
      }
    });
  }

  protected _onChangeSelection(target) {
    if(!target.value) {
      let _mb: ModalBehavior = new ModalBehavior();
      _mb.info = {
        dialogTitle: 'Nuovo tipo pendenza',
        templateName: UtilService.TIPI_PENDENZA
      };
      _mb.async_callback = this.save.bind(this);
      _mb.closure = this.refresh.bind(this);
      UtilService.dialogBehavior.next(_mb);
    } else {
      this._resetDefault();
      this._updateValues(target.value);
    }
  }

  protected _tipoPendenzaComparingFct(option: any, selection: any): boolean {
    return (selection && option.idTipoPendenza == selection.idTipoPendenza);
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

  protected _getTipiPendenza() {
    let _service = UtilService.URL_TIPI_PENDENZA;
    this.gps.getDataService(_service).subscribe(
    (_response) => {
      let _body = _response.body;
      let p: Parameters;
      let _dtp = _body['risultati'].map(function(item) {
        p = new Parameters();
        p.jsonP = item;
        p.model = this.mapNewItem(item);
        return p;
      }, this);
      if(!this.json) {
        //Insert mode
        this.tipiPendenza_items = this.filterByList(_dtp.slice(0), this.parent.tipiPendenza, 'idTipoPendenza');
      } else {
        //Edit mode
        this.tipiPendenza_items = _dtp;
      }
      this.gps.updateSpinner(false);
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    _std.titolo = new Dato({ label: item.descrizione, value: '' });
    _std.sottotitolo = new Dato({ label: Voce.ID_TIPO_PENDENZA+': ', value: item.idTipoPendenza });
    return _std;
  }

  protected _updateValues(json: any) {
    this.fGroup.controls['abilita_ctrl'].setValue(json.abilitato);
    this.fGroup.controls['pagaTerzi_ctrl'].setValue(json.pagaTerzi);
    if(json.codificaIUV) {
      this.fGroup.controls['codificaIUV_ctrl'].setValue(json.codificaIUV);
    }
    this._updateJson(json);
  }

  protected _updateJson(json: any) {
    this._jsonSchemaSelected = (json.schema)?json.schema:null;
    this._jsonLayoutSelected = (json.datiAllegati && json.datiAllegati.layout)?json.datiAllegati.layout:null;
    // this.fGroup.controls['generatore_ctrl'].setValue((json.datiAllegati.generatore)?json.datiAllegati.generatore:'');
    if(this.fGroup.controls['tipoPendenza_ctrl'].disabled) {
      this._trashSchema = !!(json.valori && json.valori.schema);
      this._trashlayout = !!(json.valori && json.valori.datiAllegati && json.valori.datiAllegati.layout);
      this.fGroup.controls['schema_ctrl'].setValue(this._trashSchema?json.valori.schema:'');
      this.fGroup.controls['datiAllegati_ctrl'].setValue(this._trashlayout?json.valori.datiAllegati.layout:'');
    }
  }
  protected _resetDefault() {
    this._schemaSelected = false;
    this._layoutSelected = false;
    this.fGroup.controls['abilita_ctrl'].setValue('');
    this.fGroup.controls['pagaTerzi_ctrl'].setValue('');
    this.fGroup.controls['codificaIUV_ctrl'].setValue('');
    // this.fGroup.controls['generatore_ctrl'].setValue('');
    this._trashSchema = false;
    this._trashlayout = false;
    this._jsonSchemaSelected = null;
    this._jsonLayoutSelected = null;
    this._clear('schema', true);
    this._clear('datiAllegati');
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

  protected _clear(ctrl: string, schema: boolean = false) {
    this['_'+ctrl] = schema?'Form schema':'Form layout';
    if(schema) {
      this._iSchemaBrowse.nativeElement.value = '';
      this._schemaSelected = false;
    } else {
      this._iLayoutBrowse.nativeElement.value = '';
      this._layoutSelected = false;
    }
    this.fGroup.controls[ctrl+'_ctrl'].setValue('');
  };

  private _error(event) {
    console.log(event);
    this.us.alert('Impossibile leggere il file.');
  };

  protected _trashSchemaSelection(schema: boolean = false) {
    if(schema) {
      this._trashSchema = false;
      this.fGroup.controls['schema_ctrl'].setValue('');
    } else {
      this._trashlayout = false;
      this.fGroup.controls['datiAllegati_ctrl'].setValue('');
    }
  }

  protected _showSchemaContent(schema: boolean = false) {
    let _json = schema?this._jsonSchemaSelected:this._jsonLayoutSelected;
    if(schema && this._trashSchema) {
      _json = this.fGroup.controls['schema_ctrl'].value;
    }
    if(!schema && this._trashlayout) {
      _json = this.fGroup.controls['datiAllegati_ctrl'].value;
    }
    const _page = `
      <html>
      <title>JSON</title>
      <style>html, body { padding: 0; margin: 0; background-color: #404040; color: #fff; } iframe { width: 100%; height: 100%; border: 0;} </style>
        <body>
          <pre>
          <code>
            ${JSON.stringify(_json, null, '\t')}
          </code>
          </pre>
        </body>
      </html>`;
    const _tmpW = window.open();
    _tmpW.document.open();
    _tmpW.document.write(_page);
    _tmpW.document.close();
  }

  /**
   * Save tipo pendenza (Put to: /tipiPendenza/{idTipoPendenza} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _hasEntry = this.checkItemIndex(mb.info.viewModel, this.parent.tipiPendenza, 'idTipoPendenza');
    let _hasLocalEntry = this.checkItemIndex(mb.info.viewModel, this.tipiPendenza_items, 'idTipoPendenza');
    if(!_hasEntry && !_hasLocalEntry) {
      let _service = UtilService.URL_TIPI_PENDENZA+'/'+encodeURIComponent(mb.info.viewModel.idTipoPendenza);
      let _json = JSON.parse(JSON.stringify(mb.info.viewModel));
      delete _json.idTipoPendenza;
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
      this.us.alert('Tipo informazione "'+mb.info.viewModel.idTipoPendenza+'" già associata.');
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
      this.tipiPendenza_items.push(p);

      this.fGroup.controls['tipoPendenza_ctrl'].setValue(p.jsonP);
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

    if(!this.fGroup.controls['tipoPendenza_ctrl'].disabled) {
      _json = _info['tipoPendenza_ctrl'];
    } else {
      _json = this.json;
    }
    _json.valori = {
      pagaTerzi: (_info['pagaTerzi_ctrl'] !== undefined)?_info['pagaTerzi_ctrl']:null,
      abilitato: (_info['abilita_ctrl'] !== undefined)?_info['abilita_ctrl']:null,
      codificaIUV: _info['codificaIUV_ctrl']
    };
    _json.valori.schema = _info['schema_ctrl']?_info['schema_ctrl']:null;
    _json.valori.datiAllegati = null;
    if(_info['datiAllegati_ctrl']) {
      _json.valori.datiAllegati = {
        // generatore: _info[generatore_ctrl']',
        layout: _info['datiAllegati_ctrl']
      };
    }

    return _json;
  }

}
