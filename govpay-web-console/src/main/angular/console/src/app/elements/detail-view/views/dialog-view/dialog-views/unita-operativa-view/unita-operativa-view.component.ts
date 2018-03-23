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
    this.fGroup.addControl('gln_ctrl', new FormControl(''));
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
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.idUnita = _info['idUnita_ctrl'];
    _json.ragioneSociale = _info['ragioneSociale_ctrl'];
    _json.gln = _info['gln_ctrl'];
    _json.abilita = _info['abilita_ctrl'];
    _json.indirizzo = _info['indirizzo_ctrl'];
    _json.civico = _info['civico_ctrl'];
    _json.cap = _info['cap_ctrl'];
    _json.localita = _info['localita_ctrl'];
    _json.provincia = _info['provincia_ctrl'];
    _json.nazione = _info['nazione_ctrl'];
    _json.email = _info['email_ctrl'];
    _json.pec = _info['pec_ctrl'];
    _json.tel = _info['tel_ctrl'];
    _json.fax = _info['fax_ctrl'];
    _json.web = _info['web_ctrl'];
    _json.area = _info['area_ctrl'];

    return _json;
  }
}
