import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';

@Component({
  selector: 'link-operatore-view',
  templateUrl: './operatore-view.component.html',
  styleUrls: ['./operatore-view.component.scss']
})
export class OperatoreViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected acl = [];
  protected domini = [];
  protected tipiPendenza = [];

  protected voce = Voce;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.elencoDominiPendenze();

    this.fGroup.addControl('principal_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(true));
    this.fGroup.addControl('ragioneSociale_ctrl', new FormControl(''));
    this.fGroup.addControl('dominio_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl(''));

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
        this.fGroup.controls['principal_ctrl'].disable();
        this.fGroup.controls['principal_ctrl'].setValue(this.json.principal);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        this.fGroup.controls['ragioneSociale_ctrl'].setValue(this.json.ragioneSociale);
        if(this.json.domini) {
          this.fGroup.controls['dominio_ctrl'].setValue(this.json.domini);
        }
        if(this.json.tipiPendenza) {
          this.fGroup.controls[ 'tipoPendenza_ctrl' ].setValue(this.json.tipiPendenza);
        }
        if(this.acl.length != 0) {
          this.acl.forEach((item, index) => {
            this.fGroup.controls['autorizzazioni_ctrl_' + index].setValue(item.autorizzazioni.sort().toString());
          });
        }
      }
    });
  }

  protected elencoDominiPendenze() {
    let _services: string[] = [];
    _services.push(UtilService.URL_DOMINI);
    _services.push(UtilService.URL_TIPI_PENDENZA);
    this.gps.updateSpinner(true);
    this.gps.forkService(_services).subscribe(function (_response) {
        if(_response) {
          this.domini = _response[0].body.risultati;
          this.domini.unshift({ ragioneSociale: UtilService.TUTTI_DOMINI.label, idDominio: UtilService.TUTTI_DOMINI.value });
          this.tipiPendenza = _response[1].body.risultati;
          this.tipiPendenza.unshift({ descrizione: UtilService.TUTTI_TIPI_PENDENZA.label, idTipoPendenza: UtilService.TUTTE_ENTRATE.value });
          this.gps.updateSpinner(false);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected dominioCmpFn(d1: any, d2: any): boolean {
    return (d1 && d2)?(d1.idDominio === d2.idDominio):(d1 === d2);
  }

  protected pendenzaCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    _json.principal = (!this.fGroup.controls['principal_ctrl'].disabled)?_info['principal_ctrl']: this.json.principal;
    _json.abilitato = _info['abilita_ctrl'];
    _json.ragioneSociale = (_info['ragioneSociale_ctrl'])?_info['ragioneSociale_ctrl']:null;

    _json.domini = (_info['dominio_ctrl'])?_info['dominio_ctrl']:[];
    _json.tipiPendenza = (_info['tipoPendenza_ctrl'])?_info['tipoPendenza_ctrl']:[];
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
