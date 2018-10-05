import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';

@Component({
  selector: 'link-tracciato-view',
  templateUrl: './tracciato-view.component.html',
  styleUrls: ['./tracciato-view.component.scss']
})
export class TracciatoViewComponent implements OnInit, IFormComponent {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() json: any;
  @Input() fGroup: FormGroup;

  protected doc: any = null;

  constructor(private us: UtilService) { }

  ngOnInit() {
    this.fGroup.addControl('tracciato_ctrl', new FormControl('', Validators.required));
  }

  protected _select() {
    if(this.iBrowse) {
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      if(_file.type.indexOf('application/json') != -1) {
        this.doc = { file: _file, filename: _file.name };
      } else {
        this.us.alert('Documento non corretto, selezionare un formato Json.');
      }
      this.iBrowse.nativeElement.value = '';
    }
  }

  protected _resetSelection() {
    if(this.iBrowse) {
      this.doc = null;
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls.tracciato_ctrl.setValue('');
    }
    event.stopImmediatePropagation();
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json:any = {};

    _json.file = this.doc.file;
    _json.nome = this.doc.filename;

    return _json;
  }

}
