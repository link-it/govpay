import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SslConfigComponent } from '../../../ssl-config/ssl-config.component';

@Component({
  selector: 'link-connettore-send',
  templateUrl: './connettore-send.component.html',
  styleUrls: ['./connettore-send.component.scss']
})
export class ConnettoreSendComponent implements IFormComponent, OnInit, AfterViewInit {

  @ViewChild('sslConfigSend') sslConfigSend: SslConfigComponent;

  _Voce = Voce;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('url_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('subscriptionKey_ctrl', new FormControl('', []));
    this.fGroup.addControl('abilitaGDE_ctrl', new FormControl(false));
    // FormGroup dedicato al componente di autenticazione
    this.fGroup.addControl('sslAuthSend', new FormGroup({}));
  }

  ngAfterViewInit() {
    if (this.json) {
      this.fGroup.controls['url_ctrl'].setValue(this.json.url || '');
      this.fGroup.controls['subscriptionKey_ctrl'].setValue(this.json.subscriptionKey || '');
      this.fGroup.controls['abilitaGDE_ctrl'].setValue(this.json.abilitaGDE || false);
    }
  }

  mapToJson(): any {
    const _info = this.fGroup.value;
    const _json: any = {
      auth: null,
      url: _info['url_ctrl'],
      subscriptionKey: _info['subscriptionKey_ctrl'] ? _info['subscriptionKey_ctrl'] : null,
      abilitaGDE: (_info['abilitaGDE_ctrl'] || false)
    };

    _json['auth'] = this.sslConfigSend.mapToJson();
    if (_json.auth == null) { delete _json.auth; }
    if (_json.subscriptionKey == null) { delete _json.subscriptionKey; }

    return _json;
  }

}
