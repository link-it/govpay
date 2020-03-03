import { Component, ContentChild, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatAutocomplete, MatAutocompleteTrigger } from '@angular/material';

@Component({
  selector: 'link-async-filterable-select',
  templateUrl: './async-filterable-select.component.html',
  styleUrls: ['./async-filterable-select.component.scss']
})
export class AsyncFilterableSelectComponent implements OnInit {
  @ContentChild(TemplateRef) templateRef: TemplateRef<any>;
  @ViewChild('afSelect', { read: MatAutocompleteTrigger }) _trigger: MatAutocompleteTrigger;
  @ViewChild('afsPanel') _panel: MatAutocomplete;

  @Input() asyncOptions = {
    timeouts : [],
    setTimeout: function(method, delay) {
      this.timeouts.push(setTimeout(method, delay));
      // console.log('New timeout');
    },
    clearAllTimeout: function(){
      for (let i = 0; i < this.timeouts.length; i++) {
        window.clearTimeout(this.timeouts[i]);
        // console.log('Timeout removed');
      }
      this.timeouts = [];
    }
  };
  @Input('options') _options: any[] = [];
  @Input('placeholder') _placeholder: string = '';
  @Input('input-hint') _inputHint = () => { return '' };
  @Input('input-display') _inputDisplay = (value: any) => { return '' };
  @Input('form-group') _formGroup: FormGroup;
  @Input('form-ctrl-name') _formCtrlName: string = '';
  @Input('searching') _searching: boolean = false;
  @Input('required') _required: boolean = false;

  @Output('option-click') _onOptionClick: EventEmitter<any> = new EventEmitter(null);
  @Output('async-keyup') _onAsyncKeyUp: EventEmitter<any> = new EventEmitter(null);

  constructor() { }

  ngOnInit() { }

  protected _keyUp(target: any) {
    this._onAsyncKeyUp.emit({ target: target, trigger: this._trigger, panel: this._panel });
  }

  protected _codeSelection(event: any, target: any) {
    this._onOptionClick.emit({ original: event, target: target, trigger: this._trigger, panel: this._panel });
  }

  isOpen(): boolean {
    if (this._panel) {
      return this._panel.isOpen;
    }
    return false;
  }

  open() {
    if (this._trigger) {
      this._trigger.openPanel();
    }
  }

  close() {
    if (this._trigger) {
      this._trigger.closePanel();
    }
  }


}
