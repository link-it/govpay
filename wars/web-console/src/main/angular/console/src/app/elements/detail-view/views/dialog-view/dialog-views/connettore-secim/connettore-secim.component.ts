import { AfterContentChecked, AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { MatSelectChange } from '@angular/material';

const SEPARATORE: string = ', ';

@Component({
  selector: 'link-connettore-secim',
  templateUrl: './connettore-secim.component.html',
  styleUrls: ['./connettore-secim.component.scss']
})
export class ConnettoreSecimComponent implements IFormComponent, OnInit, AfterViewInit, AfterContentChecked {

  _Voce = Voce;
  Util = UtilService;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  pattern: string = '^(|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+((,\\s)(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$';
  secimAbilitato: FormControl = new FormControl(false, { updateOn: 'change', validators: Validators.required });
  tipoConnettore: FormControl = new FormControl('');
  secimModalita: string = '';
  _option: any = { hasOption: false, hasAllOption: false };
  _all: any = { descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTI_TIPI_PENDENZA.value };
  _tipiPendenza: any[] = (UtilService.PROFILO_UTENTE.tipiPendenza || []);

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('secimAbilitato_ctrl', this.secimAbilitato);
    this.fGroup.addControl('codiceCliente_ctrl', new FormControl(''));
    this.fGroup.addControl('codiceIstituto_ctrl', new FormControl(''));
    this.fGroup.addControl('versioneCsv_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoConnettore_ctrl', this.tipoConnettore);
    this.fGroup.addControl('tipiPendenza_ctrl', new FormControl(''));
    this.fGroup.addControl('emailIndirizzi_ctrl', new FormControl(''));
    this.fGroup.addControl('emailSubject_ctrl', new FormControl(''));
    this.fGroup.addControl('fileSystemPath_ctrl', new FormControl(''));
  }

  ngAfterContentChecked() {
    this.secimModalita = (this.fGroup && this.fGroup.controls)?this.fGroup.controls['tipoConnettore_ctrl'].value:'';
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.fGroup.controls['secimAbilitato_ctrl'].setValue(false);
      if(this.json) {
        this.fGroup.controls['secimAbilitato_ctrl'].setValue(this.json.abilitato || false);
        this.fGroup.controls['codiceCliente_ctrl'].setValue(this.json.codiceCliente || '');
        this.fGroup.controls['codiceIstituto_ctrl'].setValue(this.json.codiceIstituto || '');
        this.fGroup.controls['versioneCsv_ctrl'].setValue(this.json.versioneCsv || '');
        this.fGroup.controls['tipoConnettore_ctrl'].setValue(this.json.tipoConnettore || '');
        this.fGroup.controls['tipiPendenza_ctrl'].setValue(this.json.tipiPendenza || '');
        if (this.json.emailIndirizzi) {
          this.fGroup.controls['emailIndirizzi_ctrl'].setValue(this.json.emailIndirizzi.join(SEPARATORE) || '');
        }
        this.fGroup.controls['emailSubject_ctrl'].setValue(this.json.emailSubject || '');
        this.fGroup.controls['fileSystemPath_ctrl'].setValue(this.json.fileSystemPath || '');
        this.__bools(this.json.tipiPendenza);
      }
    });
  }

  protected _tipoChange(event: MatSelectChange) {
    this._option.hasAllOption = false;
    this._option.hasOption = false;
    this.__bools(event.value);
  }

  protected __bools(values: any[]) {
    (values || []).forEach(value => {
      if (value.idTipoPendenza === '*') {
        this._option.hasAllOption = true;
        this._option.hasOption = false;
      } else {
        this._option.hasAllOption = false;
        this._option.hasOption = true;
      }
    });
  }

  _onChangeSecim(event: any, type: string) {
    if (type === 'secimAbilitato_ctrl') {
      (event.checked)?this.fGroup.controls['codiceCliente_ctrl'].setValidators(Validators.required):this.fGroup.controls['codiceCliente_ctrl'].clearValidators();
      (event.checked)?this.fGroup.controls['versioneCsv_ctrl'].setValidators(Validators.required):this.fGroup.controls['versioneCsv_ctrl'].clearValidators();
      (event.checked)?this.fGroup.controls['tipoConnettore_ctrl'].setValidators(Validators.required):this.fGroup.controls['tipoConnettore_ctrl'].clearValidators();
      (event.checked)?this.fGroup.controls['tipiPendenza_ctrl'].setValidators(Validators.required):this.fGroup.controls['tipiPendenza_ctrl'].clearValidators();
      this.fGroup.controls['codiceCliente_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      this.fGroup.controls['versioneCsv_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      this.fGroup.controls['tipoConnettore_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      this.fGroup.controls['tipiPendenza_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
      if (!event.checked) {
        this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
        this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
      }
    }
    if (type === 'tipoConnettore_ctrl') {
      (this.secimAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_EMAIL)?this.fGroup.controls['emailIndirizzi_ctrl'].setValidators([Validators.required, Validators.pattern(this.pattern)]):this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
      (this.secimAbilitato.value && event.value === UtilService.CONNETTORE_MODALITA_FILESYSTEM)?this.fGroup.controls['fileSystemPath_ctrl'].setValidators(Validators.required):this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
    }
    this.fGroup.controls['emailIndirizzi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['fileSystemPath_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
  }

  _pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.abilitato = (_info['secimAbilitato_ctrl'] || false);
    if (_json.abilitato) {
      _json.codiceCliente = _info['codiceCliente_ctrl'] || null;
      _json.codiceIstituto = _info['codiceIstituto_ctrl'] || null;
      _json.tipiPendenza = _info['tipiPendenza_ctrl']?_info['tipiPendenza_ctrl'].map((p: any) => {
        return {
          idTipoPendenza: p.idTipoPendenza,
          descrizione: p.descrizione,
        };
      }):null;
      _json.versioneCsv = _info['versioneCsv_ctrl'] || null;
      _json.tipoConnettore = _info['tipoConnettore_ctrl'] || null;
      if (_json.tipoConnettore === UtilService.CONNETTORE_MODALITA_EMAIL) {
        _json.emailIndirizzi = _info['emailIndirizzi_ctrl']?_info['emailIndirizzi_ctrl'].split(SEPARATORE):null;
        _json.emailSubject = _info['emailSubject_ctrl'] || null;
      } else {
        _json.fileSystemPath = _info['fileSystemPath_ctrl'] || null;
      }
    }

    Object.keys(_json).forEach(key => {
      if (_json[key] === null) {
        delete _json[key];
      }
    });

    return _json;
  }

}
