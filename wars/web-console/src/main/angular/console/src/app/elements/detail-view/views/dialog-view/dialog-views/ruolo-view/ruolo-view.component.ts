
import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Voce } from '../../../../../../services/voce.service';
import { UtilService } from '../../../../../../services/util.service';

@Component({
  selector: 'link-ruolo-view',
  templateUrl: './ruolo-view.component.html',
  styleUrls: ['./ruolo-view.component.scss']
})
export class RuoloViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected voce = Voce;
  protected acl = [];

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('id_ctrl', new FormControl('', Validators.required));

    if(this.json) {
      this.acl = this.json.acl.slice(0);
    }
    // Mappatura autorizzazioni
    const _sauth = this.acl.map((item) => {
      return item.servizio;
    });
    UtilService.SERVIZI.forEach((_servizio, index) => {
      if(_sauth.indexOf(_servizio) == -1) {
        this.acl.push({ servizio: _servizio, autorizzazioni: [] });
      }
    });
    // Map original ACL labels
    this.acl = this.acl.map((item) => {
      item.mapACL = UtilService.MAP_ACL(item.servizio);
      return item;
    });
    // Sort original ACL
    this.acl.sort((item1, item2) => {
      return (item1.mapACL>item2.mapACL)?1:(item1.mapACL<item2.mapACL)?-1:0;
    });

    this.acl.forEach((item, index) => {
      this.fGroup.addControl('autorizzazioni_ctrl_' + index, new FormControl(''));
    });
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['id_ctrl'].disable();
        this.fGroup.controls['id_ctrl'].setValue(this.json.id);
        if(this.acl.length != 0) {
          this.acl.forEach((item, index) => {
            this.fGroup.controls['autorizzazioni_ctrl_' + index].setValue(item.autorizzazioni.sort().toString());
          });
        }
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

    _json.id = ((!this.fGroup.controls['id_ctrl'].disabled))?_info['id_ctrl']:this.json.id;
    _json.acl = this.acl.map((item, index) => {
      item.autorizzazioni = (_info['autorizzazioni_ctrl_' + index])?_info['autorizzazioni_ctrl_' + index].split(','):[];
      delete item.mapACL;
      return item;
    }).filter(item => {
      return item.autorizzazioni.length != 0;
    });
    return _json;
  }

}
