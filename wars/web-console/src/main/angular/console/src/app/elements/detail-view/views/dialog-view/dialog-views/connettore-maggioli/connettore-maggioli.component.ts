import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { TimePickerDialogComponent } from '../../../../../item-view/views/date-picker-view/date-picker-view.component';
import * as moment from 'moment';

const SEPARATORE: string = ', ';

@Component({
  selector: 'link-connettore-maggioli',
  templateUrl: './connettore-maggioli.component.html',
  styleUrls: ['./connettore-maggioli.component.scss']
})
export class ConnettoreMaggioliComponent implements IFormComponent, OnInit, AfterViewInit {

  _Voce = Voce;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  pattern: string = '^(|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+((,\\s)(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$';
  maggioliAbilitato: boolean = false;
  _inviaTracciatoEsito: boolean = false;
  _isAllegatoEmail: boolean = true;
  _dataUltimaRT: Date = null;

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
    this.fGroup.addControl('maggioliAbilitato_ctrl', new FormControl(false));
    this.fGroup.addControl('url_ctrl', new FormControl(''));
    this.fGroup.addControl('username_ctrl', new FormControl(''));
    this.fGroup.addControl('password_ctrl', new FormControl(''));
    this.fGroup.addControl('inviaTracciatoEsito_ctrl', new FormControl(false));
    this.fGroup.addControl('fileSystemPath_ctrl', new FormControl(''));
    this.fGroup.addControl('emailIndirizzi_ctrl', new FormControl(''));
    this.fGroup.addControl('emailSubject_ctrl', new FormControl(''));
    this.fGroup.addControl('emailAllegato_ctrl', new FormControl(true));
    this.fGroup.addControl('downloadBaseUrl_ctrl', new FormControl(''));
    this.fGroup.addControl('dataUltimaRT_ctrl', new FormControl(null));
  }

  ngAfterViewInit() {
    if (this.json) {
      this.fGroup.controls['maggioliAbilitato_ctrl'].setValue(this.json.abilitato || false);
      if (this.json.url) {
        this.fGroup.controls['url_ctrl'].setValue(this.json.url);
      }
      if (this.json.auth) {
        this.fGroup.controls['username_ctrl'].setValue(this.json.auth.username || '');
        this.fGroup.controls['password_ctrl'].setValue(this.json.auth.password || '');
      }
      this.fGroup.controls['inviaTracciatoEsito_ctrl'].setValue(this.json.inviaTracciatoEsito || false);
      if (this.json.fileSystemPath) {
        this.fGroup.controls['fileSystemPath_ctrl'].setValue(this.json.fileSystemPath);
      }
      if (this.json.emailIndirizzi) {
        this.fGroup.controls['emailIndirizzi_ctrl'].setValue(this.json.emailIndirizzi.join(SEPARATORE) || '');
      }
      this.fGroup.controls['emailSubject_ctrl'].setValue(this.json.emailSubject || '');
      this.fGroup.controls['emailAllegato_ctrl'].setValue(true);
      if (this.json.downloadBaseUrl) {
        this.fGroup.controls['downloadBaseUrl_ctrl'].setValue(this.json.downloadBaseUrl);
      }

      this._inviaTracciatoEsito = this.json.inviaTracciatoEsito || false;
      this._isAllegatoEmail = true;
      if (this.json.dataUltimaRT) {
        this._dataUltimaRT = new Date(this.json.dataUltimaRT);
        this.fGroup.controls['dataUltimaRT_ctrl'].setValue(this._dataUltimaRT);
      }

      setTimeout(() => {
        this._onChangeMaggioli({ checked: this.json.abilitato }, 'maggioliAbilitato_ctrl');
      });
    }
  }

  _onChangeMaggioli(event: any, type: string) {
    if (type === 'maggioliAbilitato_ctrl') {
      this.maggioliAbilitato = event.checked;
    }

    // Clear all validators
    this.fGroup.controls['url_ctrl'].clearValidators();
    this.fGroup.controls['username_ctrl'].clearValidators();
    this.fGroup.controls['password_ctrl'].clearValidators();
    this.fGroup.controls['fileSystemPath_ctrl'].clearValidators();
    this.fGroup.controls['emailIndirizzi_ctrl'].clearValidators();
    this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators();

    if (this.maggioliAbilitato) {
      // URL e auth sono sempre obbligatori quando abilitato
      this.fGroup.controls['url_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['username_ctrl'].setValidators(Validators.required);
      this.fGroup.controls['password_ctrl'].setValidators(Validators.required);

      if (this._inviaTracciatoEsito) {
        this.fGroup.controls['fileSystemPath_ctrl'].setValidators(Validators.required);
        this.fGroup.controls['emailIndirizzi_ctrl'].setValidators([Validators.required, Validators.pattern(this.pattern)]);
      }
    }

    // Update validity
    this.fGroup.controls['url_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['username_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['password_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['fileSystemPath_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['emailIndirizzi_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
  }

  _onChangeInviaTracciatoEsito(event: any) {
    this._inviaTracciatoEsito = event.checked || false;
    this._onChangeMaggioli({ checked: this.maggioliAbilitato }, 'maggioliAbilitato_ctrl');
  }

  protected _allegatoChange(event: any) {
    this._isAllegatoEmail = event.checked || false;
    if (event.checked) {
      this.fGroup.controls['downloadBaseUrl_ctrl'].clearValidators();
      this.fGroup.controls['downloadBaseUrl_ctrl'].disable();
    } else {
      if (this._inviaTracciatoEsito) {
        this.fGroup.controls['downloadBaseUrl_ctrl'].setValidators(Validators.required);
      }
      this.fGroup.controls['downloadBaseUrl_ctrl'].enable();
    }
    this.fGroup.controls['downloadBaseUrl_ctrl'].updateValueAndValidity({ onlySelf: false, emitEvent: true });
  }

  protected _overlayTimepicker(event: any) {
    let _dp = this._dataUltimaRT || new Date();
    event.preventDefault();
    event.stopPropagation();
    let timeDialog = this.dialog.open(TimePickerDialogComponent, {
      data: _dp
    } as MatDialogConfig);

    timeDialog.afterClosed().subscribe(orario => {
      if (orario) {
        let _date = this.fGroup.controls['dataUltimaRT_ctrl'].value;
        if (!_date) {
          _date = new Date();
        }
        let _md = moment(_date).set('hour', orario.hh).set('minute', orario.mm);
        _date = new Date(_md.year(), _md.month(), _md.date(), _md.hour(), _md.minute());
        this.fGroup.controls['dataUltimaRT_ctrl'].setValue(_date);
        this._dataUltimaRT = _date;
      }
    });
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json: any = {};

    _json.abilitato = (_info['maggioliAbilitato_ctrl'] || false);

    // URL e credenziali vengono sempre salvate
    _json.url = _info['url_ctrl'] || null;
    _json.auth = {
      username: _info['username_ctrl'] || null,
      password: _info['password_ctrl'] || null
    };

    // dataUltimaRT viene sempre salvata se presente
    if (_info['dataUltimaRT_ctrl']) {
      _json.dataUltimaRT = _info['dataUltimaRT_ctrl'];
    }

    if (_json.abilitato) {
      _json.inviaTracciatoEsito = _info['inviaTracciatoEsito_ctrl'] || false;

      if (_json.inviaTracciatoEsito) {
        _json.fileSystemPath = _info['fileSystemPath_ctrl'] || null;
        _json.emailIndirizzi = _info['emailIndirizzi_ctrl'] ? _info['emailIndirizzi_ctrl'].split(SEPARATORE) : null;
        _json.emailSubject = _info['emailSubject_ctrl'] || null;
        _json.emailAllegato = true;
      }
    }

    // Rimuovi campi null (ma non dataUltimaRT)
    Object.keys(_json).forEach(key => {
      if (_json[key] === null && key !== 'dataUltimaRT') {
        delete _json[key];
      }
    });

    return _json;
  }

}
