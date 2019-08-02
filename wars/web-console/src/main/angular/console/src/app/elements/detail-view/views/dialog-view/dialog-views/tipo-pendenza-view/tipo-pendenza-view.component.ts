import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';

@Component({
  selector: 'link-tipo-pendenza-view',
  templateUrl: './tipo-pendenza-view.component.html',
  styleUrls: ['./tipo-pendenza-view.component.scss']
})
export class TipoPendenzaViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _voce = Voce;

  protected _tipologie: any[] = UtilService.TIPOLOGIA_PENDENZA;
  protected _generatori: any[] = UtilService.GENERATORI;
  protected _applicazioni: any[] = [];

  constructor(protected us: UtilService, protected gps: GovpayService) { }

  ngOnInit() {
    this._elencoApplicazioni();
    this.fGroup.addControl('idTipoPendenza_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('descrizione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('tipo_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilitato_ctrl', new FormControl(false));
    this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(false));

    this.fGroup.addControl('visualizzazione_ctrl', new FormControl(''));
    this.fGroup.addControl('generatore_ctrl', new FormControl(''));
    this.fGroup.addControl('schema_ctrl', new FormControl(''));

    this.fGroup.addControl('validazione_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazione_ctrl', new FormControl(''));
    this.fGroup.addControl('definizione_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltro_ctrl', new FormControl(''));

    this.fGroup.addControl('tipoTemplateAP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggetto_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggio_ctrl', new FormControl(''));
    this.fGroup.addControl('allegaPdf_ctrl', new FormControl({ value: false, disabled: true }));

    this.fGroup.addControl('tipoTemplateAR_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoRicevuta_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioRicevuta_ctrl', new FormControl(''));
    this.fGroup.addControl('allegaPdfRicevuta_ctrl', new FormControl({ value: false, disabled: true }));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idTipoPendenza_ctrl'].disable();
        this.fGroup.controls['idTipoPendenza_ctrl'].setValue((this.json.idTipoPendenza)?this.json.idTipoPendenza:'');
        this.fGroup.controls['descrizione_ctrl'].setValue((this.json.descrizione)?this.json.descrizione:'');
        this.fGroup.controls['tipo_ctrl'].setValue((this.json.tipo)?this.json.tipo:'');
        this.fGroup.controls['codificaIUV_ctrl'].setValue((this.json.codificaIUV)?this.json.codificaIUV:'');
        this.fGroup.controls['pagaTerzi_ctrl'].setValue(this.json.pagaTerzi);
        this.fGroup.controls['abilitato_ctrl'].setValue(this.json.abilitato);

        this.fGroup.controls['visualizzazione_ctrl'].setValue((this.json.visualizzazione)?(this.json.visualizzazione || ''):'');
        this.fGroup.controls['generatore_ctrl'].setValue((this.json.form)?(this.json.form.tipo || ''):'');
        this.fGroup.controls['schema_ctrl'].setValue((this.json.form)?(this.json.form.definizione || ''):'');

        this.fGroup.controls['validazione_ctrl'].setValue(this.json.validazione || '');
        this.fGroup.controls['tipoTrasformazione_ctrl'].setValue((this.json.trasformazione && this.json.trasformazione.tipo) || '');
        this.fGroup.controls['definizione_ctrl'].setValue((this.json.trasformazione && this.json.trasformazione.definizione) || '');
        this.fGroup.controls['inoltro_ctrl'].setValue(this.json.inoltro || '');

        if(this.json.promemoriaAvviso && this.json.promemoriaAvviso.tipo && this.json.promemoriaAvviso.oggetto && this.json.promemoriaAvviso.messaggio) {
          this.fGroup.controls['tipoTemplateAP_ctrl'].setValue((this.json.promemoriaAvviso && this.json.promemoriaAvviso.tipo) || '');
          this.fGroup.controls['oggetto_ctrl'].setValue((this.json.promemoriaAvviso && this.json.promemoriaAvviso.oggetto) || '');
          this.fGroup.controls['messaggio_ctrl'].setValue((this.json.promemoriaAvviso && this.json.promemoriaAvviso.messaggio) || '');
          this.fGroup.controls['allegaPdf_ctrl'].enable();
          this.fGroup.controls['allegaPdf_ctrl'].setValue((this.json.promemoriaAvviso && this.json.promemoriaAvviso.allegaPdf) || false);
        }

        if(this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.tipo && this.json.promemoriaRicevuta.oggetto && this.json.promemoriaRicevuta.messaggio) {
          this.fGroup.controls['tipoTemplateAR_ctrl'].setValue((this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.tipo) || '');
          this.fGroup.controls['oggettoRicevuta_ctrl'].setValue((this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.oggetto) || '');
          this.fGroup.controls['messaggioRicevuta_ctrl'].setValue((this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.messaggio) || '');
          this.fGroup.controls['allegaPdfRicevuta_ctrl'].enable();
          this.fGroup.controls['allegaPdfRicevuta_ctrl'].setValue((this.json.promemoriaRicevuta && this.json.promemoriaRicevuta.allegaPdf) || false);
        }
      }
    });
  }

  _elencoApplicazioni() {
    this.gps.getDataService(UtilService.URL_APPLICAZIONI).subscribe(
      (response) => {
        this._applicazioni = response.body?(response.body.risultati || []):[];
        this.gps.updateSpinner(false);
      },
      (error) => {
        this._applicazioni = [];
        this.gps.updateSpinner(false);
        this.us.onError(error);
      }
    );
  }

  // protected _compoaringFct(item: any, selection: any): boolean {
  //   return (item && selection && item.principal === selection.principal);
  // }

  protected _selectChange(event: any, controller: string, controller2?: string) {
    if(!event.value) {
      this.fGroup.controls[controller].setValue('');
      this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
      if(controller2) {
        this.fGroup.controls[controller2].disable();
      }
    } else {
      if(controller2) {
        this.fGroup.controls[controller2].enable();
      }
    }
    if(controller2) {
      this.fGroup.controls[controller2].updateValueAndValidity({ onlySelf: true });
    }
  }

  protected _lfsChange(event: any, controller: string) {
    if(event.type == 'file-selector-change') {
      if(event.value) {
        this.fGroup.controls[controller].setValidators([ Validators.required ]);
      } else {
        this.fGroup.controls[controller].clearValidators();
        this.fGroup.controls[controller].setValue('');
      }
      this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.idTipoPendenza = (!this.fGroup.controls['idTipoPendenza_ctrl'].disabled)?_info['idTipoPendenza_ctrl']:this.json.idTipoPendenza;
    _json.descrizione = (_info['descrizione_ctrl'])?_info['descrizione_ctrl']:null;
    _json.tipo = _info['tipo_ctrl'];
    _json.codificaIUV = (_info['codificaIUV_ctrl'])?_info['codificaIUV_ctrl']:null;
    _json.pagaTerzi = _info['pagaTerzi_ctrl'];
    _json.abilitato = _info['abilitato_ctrl'];

    _json.visualizzazione = _info['visualizzazione_ctrl'] || null;
    _json.form = {
      tipo: _info['generatore_ctrl'] || null,
      definizione: _info['schema_ctrl'] || null
    };
    _json.validazione = _info['validazione_ctrl'] || null;
    _json.trasformazione = {
      tipo: _info['tipoTrasformazione_ctrl'] || null,
      definizione: _info['definizione_ctrl'] || null
    };
    _json.inoltro = _info['inoltro_ctrl'] || null;
    _json.promemoriaAvviso = null;
    if(_info['tipoTemplateAP_ctrl']) {
      _json.promemoriaAvviso = {};
      _json.promemoriaAvviso.tipo = _info['tipoTemplateAP_ctrl'];
      _json.promemoriaAvviso.oggetto = _info['oggetto_ctrl'] || null;
      _json.promemoriaAvviso.messaggio = _info['messaggio_ctrl'] || null;
      _json.promemoriaAvviso.allegaPdf = _info['allegaPdf_ctrl'];
    }
    _json.promemoriaRicevuta = null;
    if(_info['tipoTemplateAR_ctrl']) {
      _json.promemoriaRicevuta = {};
      _json.promemoriaRicevuta.tipo = _info[ 'tipoTemplateAR_ctrl' ];
      _json.promemoriaRicevuta.oggetto = _info['oggettoRicevuta_ctrl'] || null;
      _json.promemoriaRicevuta.messaggio = _info['messaggioRicevuta_ctrl'] || null;
      _json.promemoriaAvviso.allegaPdf = _info['allegaPdfRicevuta_ctrl'];
    }

    return _json;
  }

}
