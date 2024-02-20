import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-stazione-view',
  templateUrl: './stazione-view.component.html',
  styleUrls: ['./stazione-view.component.scss']
})
export class StazioneViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  protected versioni: any[];
  protected _isPasswordRequired: boolean = true;

  constructor(public us: UtilService) {}

  ngOnInit() {
    this.versioni = this.us.versioniStazione();

    this.fGroup.addControl('idStazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('modelloUnico_ctrl', new FormControl(false));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idStazione_ctrl'].disable();
        const unds = this.json.idStazione.indexOf('_');
        let idStazione;
        if(unds !== -1) {
          idStazione = this.json.idStazione.substring(unds + 1);
        }
        this.fGroup.controls['idStazione_ctrl'].setValue((idStazione)?idStazione:'');
        this.fGroup.controls['password_ctrl'].setValue((this.json.password)?this.json.password:'');
        let _modelloUnicoValue = this.json.versione ? UtilService.MODELLO_UNICO_BOOLEAN_DA_VERSIONE[this.json.versione] : false;
        this.fGroup.controls['modelloUnico_ctrl'].setValue(_modelloUnicoValue);
        this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
        
        // controllo required campo password
        this._isPasswordRequired = !_modelloUnicoValue;
        this.setPasswordRequired(this._isPasswordRequired);
      }
    });
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    if(!this.fGroup.controls['idStazione_ctrl'].disabled){
      _json.idStazione = this.parent.json.idIntermediario + '_' + _info['idStazione_ctrl'];
    } else {
      _json.idStazione = this.json.idStazione;
    }
    _json.abilitato = _info['abilita_ctrl'];
    _json.password = (_info['password_ctrl'])?_info['password_ctrl']:null;
    let _versioneFromBoolean = UtilService.VERSIONE_DA_MODELLO_UNICO_BOOLEAN[_info['modelloUnico_ctrl']];
    _json.versione = _versioneFromBoolean;
    //_json.versione = (_info['versione_ctrl'])?_info['versione_ctrl']:null;

    return _json;
  }

  protected _modelloUnicoChange(event: any) {
	this._isPasswordRequired = !event.checked;
	this.setPasswordRequired(this._isPasswordRequired);
  }
  
   protected setPasswordRequired(pwdRequired: boolean) {
    const controls: any = this.fGroup.controls;
    if (pwdRequired) {
	  controls.password_ctrl.setValidators(Validators.required);
    } else {
	  controls.password_ctrl.clearValidators();
    }
  }
}
