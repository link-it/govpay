import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-pendenza-view',
  templateUrl: './pendenza-view.component.html',
  styleUrls: ['./pendenza-view.component.scss']
})
export class PendenzaViewComponent implements IFormComponent,  OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  protected stati: any[];

  constructor(public us:UtilService) {
  }

  ngOnInit() {
    this.stati = this.us.statiPendenza().filter((stato) => {
      return (this.parent.stato != stato.value);
    });
    this.fGroup.addControl('stato_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('descrizioneStato_ctrl', new FormControl(''));
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

    _json.stato = _info['stato_ctrl'];
    _json.descrizioneStato = _info['descrizioneStato_ctrl'];

    return _json;
  }
}
