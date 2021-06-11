import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { SslConfigComponent } from '../../../ssl-config/ssl-config.component';

@Component({
  selector: 'link-intermediario-view',
  templateUrl: './intermediario-view.component.html',
  styleUrls: ['./intermediario-view.component.scss']
})
export class IntermediarioViewComponent  implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('sslConfig') sslConfig: SslConfigComponent;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  voce = Voce;
  protected _us = UtilService;
  // protected versioni: any[] = UtilService.TIPI_VERSIONE_API;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('denominazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('idIntermediario_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl());
    // Connettore SOAP: servizioPagoPa
    this.fGroup.addControl('principalPagoPa_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlRPT_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('urlAvvisatura_ctrl', new FormControl(''));
    // this.fGroup.addControl('versioneApi_ctrl', new FormControl('', Validators.required));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idIntermediario_ctrl'].disable();
        this.fGroup.controls['idIntermediario_ctrl'].setValue(this.json.idIntermediario);
        this.fGroup.controls['denominazione_ctrl'].setValue(this.json.denominazione);
        this.fGroup.controls['principalPagoPa_ctrl'].setValue(this.json.principalPagoPa);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        if (this.json.servizioPagoPa) {
          const _rpt = this.json.servizioPagoPa;
          this.fGroup.controls['urlRPT_ctrl'].setValue(_rpt.urlRPT?_rpt.urlRPT:'');
          this.fGroup.controls['urlAvvisatura_ctrl'].setValue(_rpt.urlAvvisatura && UtilService.TEMPORARY_DEPRECATED_CODE?_rpt.urlAvvisatura:'');
          // this.fGroup.controls['versioneApi_ctrl'].setValue(this.json.servizioPagoPa.versioneApi);
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
    _json.idIntermediario = (!this.fGroup.controls['idIntermediario_ctrl'].disabled)?_info['idIntermediario_ctrl']:this.json.idIntermediario;
    _json.abilitato = _info['abilita_ctrl'] || false;
    _json.denominazione = (_info['denominazione_ctrl'])?_info['denominazione_ctrl']:null;
    _json.principalPagoPa = (_info['principalPagoPa_ctrl'])?_info['principalPagoPa_ctrl']:null;
    _json.servizioPagoPa = {
      auth: null,
      urlRPT: _info['urlRPT_ctrl'],
      urlAvvisatura: _info['urlAvvisatura_ctrl']?_info['urlAvvisatura_ctrl']:null,
      // versioneApi: _info['versioneApi_ctrl']
    };
    _json.servizioPagoPa['auth'] = this.sslConfig.mapToJson();
    if(_json.servizioPagoPa.auth == null) { delete _json.servizioPagoPa.auth; }
    if(_json.servizioPagoPa.urlAvvisatura == null) { delete _json.servizioPagoPa.urlAvvisatura; }

    return _json;
  }
}
