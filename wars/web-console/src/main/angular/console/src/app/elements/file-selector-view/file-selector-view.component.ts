import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

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
  @Input('selected') _selected: boolean = false;
  @Input('doubleSet') _doubleSet: boolean = false;
  @Input('disabled') _disabled: boolean = false;

  @Output('fileSelectorChange') _change: EventEmitter<any> = new EventEmitter(null);
  @Output('fileSelectorReset') _reset: EventEmitter<any> = new EventEmitter(null);

  protected _fcs: Subscription;
  protected _name: string = '';

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
      this._change.emit({type: 'file-selector-change', value: this._selected});
      if (!this._selected) {
        this._btn.nativeElement.classList.add('has-no-settings');
        this._btn.nativeElement.querySelector('mat-icon').classList.add('has-no-settings');
        this._iBrowse.nativeElement.value = '';
        this._name = '';
      } else {
        if(!this._doubleSet) {
          this._btn.nativeElement.classList.remove('has-no-settings');
          this._btn.nativeElement.querySelector('mat-icon').classList.remove('has-no-settings');
          this._btn.nativeElement.classList.remove('double-set');
          this._btn.nativeElement.querySelector('mat-icon').classList.remove('double-set');
        } else {
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
            this._formGroup.controls[this._fControlName].setValue(encoded);
          } else {
            ct.value = '';
            this._selected = false;
            this._name = 'Il contenuto del documento non è compatibile.';
          }
        } catch (e) {
          ct.value = '';
          this._selected = false;
        }
      }.bind(this);
      reader.readAsBinaryString(event.currentTarget.files[0]);
    }
  }

  protected _clear(el: any) {
    el.value = '';
    this._name = '';
    this._formGroup.controls[this._fControlName].setValue('');
    this._reset.emit({type: 'file-selector-reset', value: ''});
  };

  private _error(event) {
    console.log(event);
    this._name = 'Impossibile leggere il file.';
  };

  protected _showTextContent(title: string) {
    let _decoded = '';
    try{
      // _decoded = (atob(this._formGroup.controls[this._fControlName].value));
      _decoded = decodeURIComponent(atob(this._formGroup.controls[this._fControlName].value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      const _page = `
        <html>
        <title>${title || 'File'}</title>
        <style>html, body { padding: 0; margin: 0; background-color: #404040; color: #fff; } iframe { width: 100%; height: 100%; border: 0;} </style>
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
