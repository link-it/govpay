import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';

@Component({
  selector: 'link-tentativo-rt-view',
  templateUrl: './tentativo-rt-view.component.html',
  styleUrls: ['./tentativo-rt-view.component.scss']
})
export class TentativoRTViewComponent implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  protected doc: any = { file: null, filename: '', b64: '', dominio: '', iuv: '', ccp: '', errore: false };

  constructor(public us: UtilService) { }

  ngOnInit() {
    this.fGroup.addControl('tentativoRT_ctrl', new FormControl('', Validators.required));
  }

  ngAfterViewInit() {
  }

  protected _select() {
    if(this.iBrowse && !this.doc.file) {
      this.iBrowse.nativeElement.value = '';
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      this.doc.file = _file;
      this.doc.filename = _file.name;
      this.doc.b64 = '';
      this.doc.dominio = '';
      this.doc.iuv = '';
      this.doc.ccp = '';
      this.checkDocument();
    }
  }

  protected _resetSelection(event?: any) {
    if(this.iBrowse) {
      this.doc.file = null;
      this.doc.filename = null;
      this.doc.b64 = '';
      this.doc.dominio = '';
      this.doc.iuv = '';
      this.doc.ccp = '';
      this.doc.errore = false;
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls['tentativoRT_ctrl'].setValue('');
    }
    if(event) {
      event.stopImmediatePropagation();
    }
  }

  protected checkDocument() {
    this.doc.errore = false;
    const fr = new FileReader();
    fr.onerror = function() {
      this.fGroup.controls['tentativoRT_ctrl'].setValue('');
      this.doc.errore = true;
    }.bind(this);
    fr.onload = function() {
      const xml: Document = UtilService.parseXMLString(fr.result);
      if (xml) {
        const iuv = xml.querySelector('identificativoUnivocoVersamento');
        const ccp = xml.querySelector('CodiceContestoPagamento');
        const dominio = xml.querySelector('identificativoDominio');
        this.doc.iuv = iuv?iuv.textContent:'';
        this.doc.ccp = ccp?ccp.textContent:'';
        this.doc.dominio = dominio?dominio.textContent:'';
        if (this.doc.iuv && this.parent.indexOf(this.doc.iuv) !== -1) {
          this.doc.b64 = btoa(fr.result);
          this.fGroup.controls['tentativoRT_ctrl'].setValue(this.doc.filename);
        } else {
          this.doc.errore = true;
        }
      } else {
        this.doc.errore = true;
      }
    }.bind(this);
    fr.readAsBinaryString(this.doc.file);
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json:any = {};

    _json.idDominio = this.doc.dominio;
    _json.codiceIUV = this.doc.iuv;
    _json.ccp = this.doc.ccp;
    _json.b64 = this.doc.b64;

    return _json;
  }
}
