import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'link-unita-operativa-view',
  templateUrl: './unita-operativa-view.component.html',
  styleUrls: ['./unita-operativa-view.component.scss']
})
export class UnitaOperativaViewComponent implements  IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('idUnita_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('ragioneSociale_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
    this.fGroup.addControl('indirizzo_ctrl', new FormControl(''));
    this.fGroup.addControl('civico_ctrl', new FormControl(''));
    this.fGroup.addControl('cap_ctrl', new FormControl(''));
    this.fGroup.addControl('localita_ctrl', new FormControl(''));
    this.fGroup.addControl('provincia_ctrl', new FormControl(''));
    this.fGroup.addControl('nazione_ctrl', new FormControl(''));
    this.fGroup.addControl('email_ctrl', new FormControl(''));
    this.fGroup.addControl('pec_ctrl', new FormControl(''));
    this.fGroup.addControl('tel_ctrl', new FormControl(''));
    this.fGroup.addControl('fax_ctrl', new FormControl(''));
    this.fGroup.addControl('web_ctrl', new FormControl(''));
    this.fGroup.addControl('area_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idUnita_ctrl'].disable();
        this.fGroup.controls['idUnita_ctrl'].setValue((this.json.idUnita)?this.json.idUnita:'');
        this.fGroup.controls['ragioneSociale_ctrl'].setValue((this.json.ragioneSociale)?this.json.ragioneSociale:'');
        this.fGroup.controls['indirizzo_ctrl'].setValue((this.json.indirizzo)?this.json.indirizzo:'');
        this.fGroup.controls['civico_ctrl'].setValue((this.json.civico)?this.json.civico:'');
        this.fGroup.controls['cap_ctrl'].setValue((this.json.cap)?this.json.cap:'');
        this.fGroup.controls['localita_ctrl'].setValue((this.json.localita)?this.json.localita:'');
        this.fGroup.controls['provincia_ctrl'].setValue((this.json.provincia)?this.json.provincia:'');
        this.fGroup.controls['nazione_ctrl'].setValue((this.json.nazione)?this.json.nazione:'');
        this.fGroup.controls['email_ctrl'].setValue((this.json.email)?this.json.email:'');
        this.fGroup.controls['pec_ctrl'].setValue((this.json.pec)?this.json.pec:'');
        this.fGroup.controls['tel_ctrl'].setValue((this.json.tel)?this.json.tel:'');
        this.fGroup.controls['fax_ctrl'].setValue((this.json.fax)?this.json.fax:'');
        this.fGroup.controls['web_ctrl'].setValue((this.json.web)?this.json.web:'');
        this.fGroup.controls['area_ctrl'].setValue((this.json.area)?this.json.area:'');
        this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
      }
    });
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.idUnita = (!this.fGroup.controls['idUnita_ctrl'].disabled)?_info['idUnita_ctrl']:this.json.idUnita;
    _json.ragioneSociale = (_info['ragioneSociale_ctrl'])?_info['ragioneSociale_ctrl']:null;
    _json.abilitato = _info['abilita_ctrl'];
    _json.indirizzo = (_info['indirizzo_ctrl'])?_info['indirizzo_ctrl']:null;
    _json.civico = (_info['civico_ctrl'])?_info['civico_ctrl']:null;
    _json.cap = (_info['cap_ctrl'])?_info['cap_ctrl']:null;
    _json.localita = (_info['localita_ctrl'])?_info['localita_ctrl']:null;
    _json.provincia = (_info['provincia_ctrl'])?_info['provincia_ctrl']:null;
    _json.nazione = (_info['nazione_ctrl'])?_info['nazione_ctrl']:null;
    _json.email = (_info['email_ctrl'])?_info['email_ctrl']:null;
    _json.pec = (_info['pec_ctrl'])?_info['pec_ctrl']:null;
    _json.tel = (_info['tel_ctrl'])?_info['tel_ctrl']:null;
    _json.fax = (_info['fax_ctrl'])?_info['fax_ctrl']:null;
    _json.web = (_info['web_ctrl'])?_info['web_ctrl']:null;
    _json.area = (_info['area_ctrl'])?_info['area_ctrl']:null;

    return _json;
  }
}
