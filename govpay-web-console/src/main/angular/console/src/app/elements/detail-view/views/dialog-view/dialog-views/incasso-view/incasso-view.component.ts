import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

import * as moment from 'moment';

@Component({
  selector: 'link-incasso-view',
  templateUrl: './incasso-view.component.html',
  styleUrls: ['./incasso-view.component.scss']
})
export class IncassoViewComponent implements IFormComponent,  OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;


  constructor(public us:UtilService) {
  }

  ngOnInit() {
    //this.fGroup.addControl('id_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('causale_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('importo_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('dataValuta_ctrl', new FormControl(''));
    this.fGroup.addControl('dataContabile_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    // setTimeout(() => {
    //   if(this.json) {
    //     this.fGroup.controls['id_ctrl'].disable();
    //     this.fGroup.controls['id_ctrl'].setValue((this.json.id)?this.json.id:'');
    //     this.fGroup.controls['causale_ctrl'].setValue((this.json.causale)?this.json.causale:'');
    //     this.fGroup.controls['importo_ctrl'].setValue((this.json.importo)?this.json.importo:'');
    //     this.fGroup.controls['dataValuta_ctrl'].setValue((this.json.dataValuta)?this.json.dataValuta:'');
    //     this.fGroup.controls['dataContabile_ctrl'].setValue((this.json.dataContabile)?this.json.dataContabile:'');
    //   }
    // });
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    //_json.id = (!this.fGroup.controls['id_ctrl'].disabled)?_info['id_ctrl']:this.json.id;
    _json.causale = (_info['causale_ctrl'])?_info['causale_ctrl']:null;
    _json.importo = (_info['importo_ctrl'])?_info['importo_ctrl']:null;
    _json.dataValuta = (_info['dataValuta_ctrl'])?moment(_info['dataValuta_ctrl']).format('YYYY-MM-DD'):null;
    _json.dataContabile = (_info['dataContabile_ctrl'])?moment(_info['dataContabile_ctrl']).format('YYYY-MM-DD'):null;

    return _json;
  }

}
