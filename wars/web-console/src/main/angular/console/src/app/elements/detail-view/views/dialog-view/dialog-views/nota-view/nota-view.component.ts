import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';

@Component({
  selector: 'link-nota-view',
  templateUrl: './nota-view.component.html',
  styleUrls: ['./nota-view.component.scss']
})
export class NotaViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor(public us:UtilService) { }

  ngOnInit() {
    this.fGroup.addControl('titoloNota_ctrl', new FormControl({ value: 'Notifica Esaminata', disabled: true }));
    this.fGroup.addControl('descrizioneNota_ctrl', new FormControl('', Validators.required));
  }

  ngAfterViewInit() {
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.oggetto = this.fGroup.controls['titoloNota_ctrl'].value;
    _json.testo = _info['descrizioneNota_ctrl'];
    _json.tipo = 'NOTA_UTENTE';

    return _json;
  }
}
