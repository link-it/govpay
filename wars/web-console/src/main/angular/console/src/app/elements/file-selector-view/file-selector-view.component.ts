import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-file-selector-view',
  templateUrl: './file-selector-view.component.html',
  styleUrls: ['./file-selector-view.component.scss']
})
export class FileSelectorViewComponent implements OnInit, OnDestroy {
  @ViewChild('iBrowse', {read: ElementRef}) _iBrowse: ElementRef;
  @ViewChild('btn', {read: ElementRef}) _btn: ElementRef;

  @Input('placeholder') _placeholder: string = 'Documento';
  @Input('fControlName') _fControlName: string = '';
  @Input('fGroup') _formGroup: FormGroup;
  @Input('filename') _externalName: string = '';
  @Input('file-text-only') _onlyText: boolean = true;
  @Input('selected') _selected: boolean = false;
  @Input('preset-value') _presetValue: any;
  @Input('disabled') _disabled: boolean = false;

  @Output('fileSelectorChange') _change: EventEmitter<any> = new EventEmitter(null);
  @Output('fileSelectorReset') _reset: EventEmitter<any> = new EventEmitter(null);

  protected _fcs: Subscription;
  protected _name: string = '';
  protected _userSelected: boolean = false;

  protected b64toBlob = (b64Data: any, contentType: string = '', sliceSize: number = 512) => {
    const byteCharacters = atob(b64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);

      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    const blob = new Blob(byteArrays, {type: contentType});
    return blob;
  };

  constructor() { }

  ngOnInit() {
    this._fcs = this._formGroup.controls[this._fControlName].valueChanges.subscribe((value) => {
      this.validateFileSelector();
    });
  }

  ngOnDestroy() {
    this._fcs.unsubscribe();
  }

  protected validateFileSelector() {
    if(this._formGroup && this._fControlName) {
      this._selected = !!this._formGroup.controls[this._fControlName].value;
      this._change.emit({ type: 'file-selector-change', value: this._selected, controller: this._formGroup.controls[this._fControlName] });
      if (!this._selected && !this._presetValue) {
        this._btn.nativeElement.classList.add('has-no-settings');
        this._btn.nativeElement.querySelector('mat-icon').classList.add('has-no-settings');
        this._iBrowse.nativeElement.value = '';
        this._name = '';
      } else {
        if(this._selected || !this._presetValue || (this._presetValue && this._userSelected)) {
          this._btn.nativeElement.classList.remove('has-no-settings');
          this._btn.nativeElement.querySelector('mat-icon').classList.remove('has-no-settings');
          this._btn.nativeElement.classList.remove('double-set');
          this._btn.nativeElement.querySelector('mat-icon').classList.remove('double-set');
        } else if(!this._userSelected) {
          this._btn.nativeElement.classList.add('double-set');
          this._btn.nativeElement.querySelector('mat-icon').classList.add('double-set');
        }
      }
    }
  }

  protected readFile(event)
  {
    if(event.currentTarget.files && event.currentTarget.files.length != 0) {
      const reader = new FileReader();
      const ct = event.currentTarget;
      const _name = ct.files[0].name;
      reader.onerror = this._error.bind(this);
      reader.onload = function() {
        try {
          if(typeof reader.result === 'string') {
            const encoded = btoa(reader.result.toString());
            this._name = _name;
            this._selected = true;
            this._userSelected = true;
            this._formGroup.controls[this._fControlName].setValue(encoded);
          } else {
            ct.value = '';
            this._selected = false;
            this._userSelected = false;
            this._name = 'Il contenuto del documento non è compatibile.';
          }
        } catch (e) {
          ct.value = '';
          this._selected = false;
          this._userSelected = false;
        }
      }.bind(this);
      reader.readAsBinaryString(event.currentTarget.files[0]);
    }
  }

  protected _clear(el: any) {
    el.value = '';
    this._name = '';
    this._userSelected = false;
    this._formGroup.controls[this._fControlName].setValue('');
    this._reset.emit({ type: 'file-selector-reset', value: '', controller: this._formGroup.controls[this._fControlName] });
  };

  private _error(event) {
    console.log(event);
    this._name = 'Impossibile leggere il file.';
  };

  protected _doClick(title: string, name: string) {
    if (this._onlyText) {
      this._showTextContent(title);
    } else {
      this._saveFile(name || this._externalName || (title + '.replaceWithExt'));
    }
  }

  protected _saveFile(title: string) {
    try {
      let blob: Blob = this.b64toBlob(this._formGroup.controls[this._fControlName].value);
      let zip = new JSZip();
      zip.file(title, blob);
      zip.generateAsync({type: 'blob'}).then(function (zipData) {
        FileSaver(zipData, 'Archivio.zip');
      });
    } catch (e) {
      console.log('Si è verificato un errore non previsto durante la creazione del file.');
    }
  }

  protected _showTextContent(title: string) {
    let _decoded = '';
    try{
      const _value = this._formGroup.controls[this._fControlName].value || this._presetValue;
      _decoded = decodeURIComponent(atob(_value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join('')).toString().trim().replace(/</g,'&lt;');
      const _page = `
        <html>
          <title>${title || 'File'}</title>
          <head><style>html, body { padding: 0; margin: 0; background-color: #404040; color: #fff; } iframe { width: 100%; height: 100%; border: 0;} </style></head>
          <body>
            <pre>
              <code>
                ${_decoded}
              </code>
            </pre>
          </body>
        </html>`;
      const _tmpW = window.open();
      _tmpW.document.open();
      _tmpW.document.write(_page);
      _tmpW.document.close();
    } catch(e) {
      console.log(e);
    }
  }

}
