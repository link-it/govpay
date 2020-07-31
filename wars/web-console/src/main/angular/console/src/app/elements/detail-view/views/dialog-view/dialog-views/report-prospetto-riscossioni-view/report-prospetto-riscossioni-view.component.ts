import { Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import * as moment from 'moment';

@Component({
  selector: 'link-report-prospetto-riscossioni-view',
  templateUrl: './report-prospetto-riscossioni-view.component.html',
  styleUrls: ['./report-prospetto-riscossioni-view.component.scss']
})
export class ReportProspettoRiscossioniViewComponent implements OnInit, IFormComponent {

  @Input() fGroup: FormGroup;
  @Input() json: any;


  protected _us = UtilService;
  protected _voce = Voce;
  protected _domini = [];
  protected invalidRange: any = { start: false, end: false };
  protected invalidDateRangeError: any = { start: '', end: '' };

  constructor() { }

  ngOnInit() {
    this._domini = UtilService.PROFILO_UTENTE?UtilService.PROFILO_UTENTE.domini:[];
    this.fGroup.addControl('formato_ctrl', new FormControl(UtilService.PDF, [Validators.required]));
    this.fGroup.addControl('dominio_ctrl', new FormControl(''));
    this.fGroup.addControl('dataDa_ctrl', new FormControl(''));
    this.fGroup.addControl('dataA_ctrl', new FormControl(''));
  }

  _compareDatesStart(event) {
    this.invalidRange.start = false;
    const _value = this.fGroup.controls['dataA_ctrl'].value;
    if(_value && new Date(event.value) > new Date(_value)) {
      this.fGroup.controls['dataDa_ctrl'].setValue(null);
      this.invalidDateRangeError.start = `Data inizio ${moment(event.value).format('DD/MM/YYYY')} non consentita.`;
      this.invalidRange.start = true;
    }
  }

  _compareDatesEnd(event) {
    this.invalidRange.end = false;
    const _value = this.fGroup.controls['dataDa_ctrl'].value;
    if(_value && new Date(event.value) < new Date(_value)) {
      this.fGroup.controls['dataA_ctrl'].setValue(null);
      this.invalidDateRangeError.end = `Data fine ${moment(event.value).format('DD/MM/YYYY')} non consentita.`;
      this.invalidRange.end = true;
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.formato = _info['formato_ctrl']?_info['formato_ctrl']:UtilService.PDF;
    _json.idDominio = _info['dominio_ctrl']?_info['dominio_ctrl'].idDominio:null;
    _json.dataDa = _info['dataDa_ctrl']?moment(_info['dataDa_ctrl']).format('YYYY-MM-DD'):null;
    _json.dataA = _info['dataA_ctrl']?moment(_info['dataA_ctrl']).format('YYYY-MM-DD'):null;

    return _json;
  }
}
