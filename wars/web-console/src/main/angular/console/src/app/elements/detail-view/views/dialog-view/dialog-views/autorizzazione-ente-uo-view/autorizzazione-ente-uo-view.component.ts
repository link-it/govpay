import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Voce } from '../../../../../../services/voce.service';
import { UtilService } from '../../../../../../services/util.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { MatSelectChange } from '@angular/material';
import { IModalDialog } from '../../../../../../classes/interfaces/IModalDialog';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../../../classes/modal-behavior';
import { Parameters } from '../../../../../../classes/parameters';

@Component({
  selector: 'link-autorizzazione-ente-uo-view',
  templateUrl: './autorizzazione-ente-uo-view.component.html',
  styleUrls: ['./autorizzazione-ente-uo-view.component.scss']
})
export class AutorizzazioneEnteUoViewComponent implements IModalDialog, IFormComponent, OnInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  protected _option: any = { hasOption: false, hasNoOption: false, hasAllOption: false };
  protected _nothing: any = { ragioneSociale: UtilService.NESSUNA_UNITA_OPERATIVA.label, idUnita: UtilService.NESSUNA_UNITA_OPERATIVA.value };
  protected _all: any = { ragioneSociale: UtilService.TUTTE_UNITA_OPERATIVE.label, idUnita: UtilService.TUTTE_UNITA_OPERATIVE.value };

  protected domini = [];
  protected unitaOperativeDominio = [];

  protected _voce = Voce;

  constructor(protected gps: GovpayService, protected us: UtilService) { }

  ngOnInit() {
    this.fGroup.addControl('dominio_ctrl', new FormControl(''));
    this.fGroup.addControl('unitaOperative_ctrl', new FormControl(''));
    this._loadDomini();
  }

  protected _loadDomini() {
    const _service = UtilService.URL_DOMINI;
    this.gps.getDataService(_service, 'associati=true').subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.domini = this.filterByList((response && response.body)?response.body['risultati']:[], this.parent.domini, 'idDominio');
        this.domini.unshift({ ragioneSociale: UtilService.TUTTI_DOMINI.label, idDominio: UtilService.TUTTI_DOMINI.value });
        if(this.domini.length == 1) {
          const _fdom = this.domini[0];
          this.fGroup.controls['dominio_ctrl'].setValue(_fdom);
          this._dominiChangeSelection({ value: _fdom });
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _unitaOperativaChangeSelection(event: MatSelectChange) {
    this._option.hasAllOption = false;
    this._option.hasNoOption = false;
    this._option.hasOption = false;
    this._updateBools(event.value);
  }

  protected _updateBools(bools: any[]) {
    bools.forEach(sel => {
      if(sel.idUnita === '*') {
        this._option.hasAllOption = true;
        this._option.hasNoOption = false;
        this._option.hasOption = false;
      } else if(sel.idUnita === null) {
        this._option.hasAllOption = false;
        this._option.hasNoOption = true;
        this._option.hasOption = false;
      } else {
        this._option.hasAllOption = false;
        this._option.hasNoOption = false;
        this._option.hasOption = true;
      }
    });
  }

  protected _dominiChangeSelection(event: any) {
    this.unitaOperativeDominio = [];
    this.fGroup.controls['unitaOperative_ctrl'].reset();
    this.fGroup.controls['unitaOperative_ctrl'].disable();
    if (event.value.idDominio !== '*') {
      this._loadUnitaOperative(event.value.unitaOperative);
    }
  }

  protected _loadUnitaOperative(_uoRef: string) {
    this.gps.getDataService(_uoRef, 'associati=true').subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.fGroup.controls['unitaOperative_ctrl'].enable();
        const _uoList: any[] = (response && response.body)?response.body['risultati']:[];
        this.unitaOperativeDominio = _uoList.sort((item1, item2) => {
          return (item1.ragioneSociale>item2.ragioneSociale)?1:(item1.ragioneSociale<item2.ragioneSociale)?-1:0;
        });
        if(this.unitaOperativeDominio.length == 1) {
          const _uo = this.unitaOperativeDominio[0];
          this.fGroup.controls['unitaOperative_ctrl'].setValue([_uo]);
          this._updateBools([_uo]);
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
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
        if(el.jsonP[key] == item[key]) {
          _keep = false;
        }
      });
      return _keep;
    });
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json: any = {};

    _json.dominio = this.fGroup.controls['dominio_ctrl'].value;
    _json.unitaOperative = this.fGroup.controls['unitaOperative_ctrl'].value;

    return _json;
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

}
