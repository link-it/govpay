import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'link-ricevuta-view',
  templateUrl: './ricevuta-view.component.html',
  styleUrls: ['./ricevuta-view.component.scss']
})
export class RicevutaViewComponent implements OnInit, OnDestroy, IFormComponent {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() json: any;
  @Input() fGroup: FormGroup;
  @Input() notifier: BehaviorSubject<boolean>;

  _subscription: Subscription;
  _advancedSettings: boolean = false;
  _autoIndex: number = 1;

  protected _voce = Voce;

  protected _methodSelected: any;

  constructor(public gps: GovpayService, private us: UtilService) {
  }

  ngOnInit() {
    this._resetMethod();
    this.fGroup.addControl('ricevuta_ctrl', new FormControl('', Validators.required));
  }

  ngOnDestroy() {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
  }

  protected _select() {
    if(this.iBrowse && !this._methodSelected.file) {
      this.iBrowse.nativeElement.value = '';
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      this._methodSelected.file = _file;
      this._methodSelected.filename = _file.name;
      this._methodSelected.json = '';
      this.fGroup.controls['ricevuta_ctrl'].setValue(_file.name);
    }
  }

  protected _resetSelection(event?: any) {
    if(this.iBrowse) {
      this._methodSelected.file = null;
      this._methodSelected.filename = null;
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls['ricevuta_ctrl'].setValue('');
    }
    if(event) {
      event.stopImmediatePropagation();
    }
  }

  protected _resetMethod() {
    this._methodSelected = {
      id: -1,
      auto: false,
      file: null,
      filename: '',
      name: '',
      method: null,
      json: '',
      mimeType: ''
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json: any;

    if (this._methodSelected) {
      _json = {};
      _json.file = this._methodSelected.file;
      _json.nome = this._methodSelected.filename;
      if (this._methodSelected.json) {
        _json.json = this._methodSelected.json;
      }
      _json.mimeType = this._methodSelected.mimeType;
    }
    return _json;
  }

}
