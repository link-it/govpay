import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { HttpClient } from '@angular/common/http';
import { AsyncFilterableSelectComponent } from '../../../../../async-filterable-select/async-filterable-select.component';

import * as moment from 'moment';

@Component({
  selector: 'link-incasso-view',
  templateUrl: './incasso-view.component.html',
  styleUrls: ['./incasso-view.component.scss']
})
export class IncassoViewComponent implements IFormComponent, OnDestroy,  OnInit, AfterViewInit {
  @ViewChild('asyncIdfIuv', { read: AsyncFilterableSelectComponent }) _asyncIdfIuv: AsyncFilterableSelectComponent;
  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _voce = Voce;
  protected _domini: any[] = [];
  protected _idfIuv: any[] = [];
  protected _searching: boolean = false;
  protected _mapRisultati = () => {
    if(this._idfIuv && this._idfIuv.length > 1) {
      return this._idfIuv.length + ' risultati';
    }

    return '';
  };

  constructor(protected us: UtilService, protected gps: GovpayService, protected http: HttpClient) {
    this._loadDomini();
  }

  ngOnInit() {
    this.fGroup.addControl('dominio_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('idfIuv_ctrl', new FormControl({ value: '', disabled: true }, Validators.required));
    this.fGroup.addControl('importo_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('sct_ctrl', new FormControl('', Validators.required));
    // this.fGroup.addControl('causale_ctrl', new FormControl('', Validators.required));
    // this.fGroup.addControl('importo_ctrl', new FormControl('', Validators.required));
    // this.fGroup.addControl('dataValuta_ctrl', new FormControl(''));
    // this.fGroup.addControl('dataContabile_ctrl', new FormControl(''));

    /*this._idfIuvFilter = this.fGroup.controls['idfIuv_ctrl'].valueChanges
      .pipe(map((value) => {
          return this.filterIdfIuv(value);
        })
      );*/
  }

  ngOnDestroy() {
    this._asyncIdfIuv.asyncOptions.clearAllTimeout();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        // this.fGroup.controls['causale_ctrl'].setValue((this.json.causale)?this.json.causale:'');
        // this.fGroup.controls['importo_ctrl'].setValue((this.json.importo)?this.json.importo:'');
        // this.fGroup.controls['dataValuta_ctrl'].setValue((this.json.dataValuta)?this.json.dataValuta:'');
        // this.fGroup.controls['dataContabile_ctrl'].setValue((this.json.dataContabile)?this.json.dataContabile:'');
      }
    });
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

  protected _onChangeDominio(event: any) {
    this._idfIuv = [];
    this._reset();
    if(event.value) {
      this.fGroup.controls['idfIuv_ctrl'].enable();
      this.elencoIdf('');
    }
  }

  protected _codeSelection(event: any) {
    if(event.original.option && event.original.option.value) {
      const fluxIUV: any = event.original.option.value;
      if(event.target) {
        event.target.value = fluxIUV.label;
        event.target.blur();
      }
      this.fGroup.controls['sct_ctrl'].setValue('');
      if (fluxIUV.flusso && fluxIUV.trn) {
        this.fGroup.controls['sct_ctrl'].setValue(fluxIUV.trn);
      }
      this.fGroup.controls['importo_ctrl'].setValue(event.original.option.value.importo);
    } else {
      this._reset();
    }
  }

  protected _reset() {
    this.fGroup.controls['idfIuv_ctrl'].setValue('');
    this.fGroup.controls['sct_ctrl'].setValue('');
    this.fGroup.controls['importo_ctrl'].setValue('');
  }

  protected _keyUp(event: any) {
    this._asyncIdfIuv.asyncOptions.clearAllTimeout();
    if(event.target.value) {
      const _delayFct = function () {
        // console.log('End timeout');
        this._asyncIdfIuv.asyncOptions.clearAllTimeout();
        if(this._asyncIdfIuv.isOpen()) {
          this._asyncIdfIuv.close();
        }
        this.elencoIdf(event.target.value);
      }.bind(this);
      this._asyncIdfIuv.asyncOptions.setTimeout(_delayFct, 800);
    } else {
      this._reset();
      this._asyncIdfIuv.close();
      this._idfIuv = [];
    }
  }

  protected elencoIdfIuv(value) {
    let _services: any[] = [];
    const _selection = this.fGroup.controls['dominio_ctrl'].value;
    const _queryRisc = '?tipo=ENTRATA&stato=RISCOSSA&idDominio=' + _selection.idDominio + '&iuv=' + value;
    const _queryRendi = '?stato=Acquisito&incassato=false&idDominio=' + _selection.idDominio + '&idFlusso=' + value;
    const _risc = UtilService.URL_RISCOSSIONI + _queryRisc;
    const _rendi = UtilService.URL_RENDICONTAZIONI + _queryRendi;
    _services.push(_risc);
    _services.push(_rendi);
    this._searching = true;
    this.fGroup.controls['idfIuv_ctrl'].disable();
    this.gps.forkService(_services).subscribe(
      (_responses) => {
        this._asyncIdfIuv.asyncOptions.clearAllTimeout();
        if(_responses) {
          this._searching = false;
          this.fGroup.controls['idfIuv_ctrl'].enable();
          const _riscos = _responses[0]['body'].risultati.map(_ris => {
            return {
              flusso: '',
              trn: '',
              importo: _ris.importo,
              data: moment(_ris.data).format('DD/MM/YYYY'),
              iuv: _ris.iuv,
              label: _ris.iuv
            }
          });
          const _rendis = _responses[1]['body'].risultati.map(_rend => {
            return {
              flusso: _rend.idFlusso,
              trn: _rend.trn,
              importo: _rend.importoTotale,
              data: moment(_rend.dataFlusso).format('DD/MM/YYYY'),
              iuv: '',
              label: _rend.idFlusso
            }
          });
          this._idfIuv = _riscos.concat(_rendis);
          if(!this._asyncIdfIuv.isOpen()) {
            this._asyncIdfIuv.open();
          }
          this._asyncIdfIuv.focusInput();
        }
      }, (error) => {
        this._asyncIdfIuv.asyncOptions.clearAllTimeout();
        this._searching = false;
        this.fGroup.controls['idfIuv_ctrl'].enable();
        this._idfIuv = [];
      });
  }

  protected elencoIdf(value) {
    const _services: any[] = [];
    const _selection = this.fGroup.controls['dominio_ctrl'].value;
    const _queryRendi = '?stato=Acquisito&incassato=false&idDominio=' + _selection.idDominio + '&idFlusso=' + value;
    const _rendi = UtilService.URL_RENDICONTAZIONI + _queryRendi;
    _services.push(_rendi);
    this._searching = true;
    this.fGroup.controls['idfIuv_ctrl'].disable();
    this.gps.forkService(_services).subscribe(
      (_responses) => {
        this._asyncIdfIuv.asyncOptions.clearAllTimeout();
        if (_responses) {
          this._searching = false;
          this.fGroup.controls['idfIuv_ctrl'].enable();
          const _rendis = _responses[0]['body'].risultati.map(_rend => {
            return {
              flusso: _rend.idFlusso,
              trn: _rend.trn,
              importo: _rend.importoTotale,
              data: moment(_rend.dataFlusso).format('DD/MM/YYYY'),
              iuv: '',
              label: _rend.idFlusso
            }
          });
          this._idfIuv = _rendis;
          if (!this._asyncIdfIuv.isOpen()) {
            this._asyncIdfIuv.open();
          }
          this._asyncIdfIuv.focusInput();
        }
      }, (error) => {
        this._asyncIdfIuv.asyncOptions.clearAllTimeout();
        this._searching = false;
        this.fGroup.controls['idfIuv_ctrl'].enable();
        this._idfIuv = [];
      });
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.idDominio = (_info['dominio_ctrl'])?_info['dominio_ctrl'].idDominio:null;
    _json.importo = (_info['importo_ctrl'])?_info['importo_ctrl']:null;
    _json.sct = (_info['sct_ctrl'])?_info['sct_ctrl']:null;
    if(_info['idfIuv_ctrl'] && _info['idfIuv_ctrl'].flusso) {
      _json.idFlusso = _info['idfIuv_ctrl'].flusso;
    }
    if(_info['idfIuv_ctrl'] && _info['idfIuv_ctrl'].iuv) {
      _json.iuv = _info['idfIuv_ctrl'].iuv;
    }
    // _json.causale = (_info['causale_ctrl'])?_info['causale_ctrl']:null;
    // _json.importo = (_info['importo_ctrl'])?_info['importo_ctrl']:null;
    // _json.dataValuta = (_info['dataValuta_ctrl'])?moment(_info['dataValuta_ctrl']).format('YYYY-MM-DD'):null;
    // _json.dataContabile = (_info['dataContabile_ctrl'])?moment(_info['dataContabile_ctrl']).format('YYYY-MM-DD'):null;

    return _json;
  }

}
