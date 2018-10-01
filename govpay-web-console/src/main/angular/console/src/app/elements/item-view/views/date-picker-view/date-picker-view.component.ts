import { AfterViewInit, Component, Directive, ElementRef, EventEmitter, HostListener, Inject, Input, Output } from '@angular/core';
import { FormInput } from '../../../../classes/view/form-input';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';

import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material';
import * as moment from 'moment';

@Component({
  selector: 'link-date-picker-view',
  templateUrl: './date-picker-view.component.html',
  styleUrls: ['./date-picker-view.component.scss']
})
export class DatePickerViewComponent implements IFormComponent, AfterViewInit {

  @Input() json: FormInput = new FormInput();
  @Input() fGroup: FormGroup;

  _pickerDate: any;

  constructor(private dialog: MatDialog) { }

  ngAfterViewInit() {
    if(this.json && this.json.value) {
      //Default selection
      this.fGroup.controls[this.json.id+'_ctrl'].setValue(new Date(this.json.value));
    }
  }

  verifyDisplay(): boolean {
    let _showMe: boolean = !this.json.dependency;
    if(this.json && this.json.source) {
      _showMe = (this.json.source.value == this.json.target);
    }
    this.markRequiredFormControl(_showMe);

    return _showMe;
  }

  markRequiredFormControl(value: boolean) {
    if(!value) {
      if(this.json.required && this.json.dependency) {
        if(this.fGroup.controls.hasOwnProperty(this.json.id+'_ctrl')) {
          this.fGroup.removeControl(this.json.id+'_ctrl');
        }
      }
    } else {
      if(this.json.required && this.json.dependency) {
        if(!this.fGroup.controls.hasOwnProperty(this.json.id+'_ctrl')) {
          this.fGroup.addControl(this.json.id+'_ctrl', new FormControl('', Validators.required));
        }
      }
    }
  }

  //TIME_PICKER
  protected _overlayTimepicker(_picker, event) {
    let _dp = !_picker?new Date():_picker;
    event.preventDefault();
    event.stopPropagation();
    let timeDialog = this.dialog.open(TimePickerDialogComponent, {
      data: _dp
    });

    timeDialog.afterClosed().subscribe(orario => {
      if(orario) {
        let _date = this.fGroup.controls[this.json.id+'_ctrl'].value;
        let _md = moment(_date).set('hour', orario.hh).set('minute', orario.mm);
        _date = new Date(_md.year(), _md.month(), _md.date(), _md.hour(), _md.minute());
        this.fGroup.controls[this.json.id+'_ctrl'].setValue(_date);
      }
    });

  }

}

@Directive({
  selector: '[time-scroll]'
})
export class TimepickerScrollDirective {

  @Output() onMouseWheel = new EventEmitter();

  @HostListener('mousewheel', ['$event', 'true']) onMouseWheelChrome(event: any) {
    this.mouseWheelFunction(event);
  }

  @HostListener('DOMMouseScroll', ['$event', 'true']) onMouseWheelFirefox(event: any) {
    this.mouseWheelFunction(event);
  }

  @HostListener('onmousewheel', ['$event', 'true']) onMouseWheelIE(event: any) {
    this.mouseWheelFunction(event);
  }

  constructor(private el: ElementRef) {}

  mouseWheelFunction(event: any) {
    let _event = window.event || event; // old IE support
    let _direction = _event.wheelDelta || -1*_event.detail;
    let delta = Math.max(-1, Math.min(1, _direction));
    let target = this.el.nativeElement;
    this.onMouseWheel.emit({ target: target,  delta: -delta });
  }

}

@Component({
  selector: "link-time-picker-view",
  template: `
    <div class="d-flex align-items-center justify-content-between">
     <span>{{_convertText(orario)}}</span>
     <button mat-icon-button (click)="_closeDialog()"><mat-icon>clear</mat-icon></button>
    </div>
    <div class="d-flex flex-row">
      <div class="d-flex flex-column px-3 text-center" time-scroll (onMouseWheel)="_onWheel($event, 'hh')">
        <button mat-icon-button (click)="_hhmm($event, 'hup')" class="my-3">
          <mat-icon>keyboard_arrow_up</mat-icon>
        </button>
        <div>{{convertTime(orario?.hh, 'hh')}}</div>
        <button mat-icon-button (click)="_hhmm($event,'hdown')" class="my-3">
          <mat-icon>keyboard_arrow_down</mat-icon>
        </button>
      </div>
      <span class="align-self-center">:</span>
      <div class="d-flex flex-column px-3 text-center" time-scroll (onMouseWheel)="_onWheel($event, 'mm')">
        <button mat-icon-button (click)="_hhmm($event, 'mup')" class="my-3">
          <mat-icon>keyboard_arrow_up</mat-icon>
        </button>
        <div>{{convertTime(orario?.mm, 'mm')}}</div>
        <button mat-icon-button (click)="_hhmm($event,'mdown')" class="my-3">
          <mat-icon>keyboard_arrow_down</mat-icon>
        </button>
      </div>
    </div>
  `,
  styles: ['']
})
export class TimePickerDialogComponent {

  orario: any = { hh: 0, mm: 0 };

  constructor(public dialogRef: MatDialogRef<TimePickerDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
    let _md = moment(this.data);
    this.orario.hh = _md.hour();
    this.orario.mm = _md.minute();
  }

  _hhmm(event, value) {
    if(event) {
      event.stopPropagation();
      event.preventDefault();
    }
    let _time = this.orario;
    switch(value) {
      case 'hup':
        _time.hh = (_time.hh + 1) % 24;
        break;
      case 'hdown':
        _time.hh = ((_time.hh - 1)<0)?'23':(_time.hh - 1) % 24;
        break;
      case 'mup':
        _time.mm = ((_time.mm) + 1) % 60;
        break;
      case 'mdown':
        _time.mm = ((_time.mm - 1)<0)?'59':(_time.mm - 1) % 60;
        break;
    }
    this.orario = { hh: parseInt(_time.hh), mm: parseInt(_time.mm) };
  }

  _onWheel(event, value) {
    if(value == 'hh') {
      (event.delta>0)?this._hhmm(null, 'hup'):this._hhmm(null, 'hdown');
    }
    if(value == 'mm') {
      (event.delta>0)?this._hhmm(null, 'mup'):this._hhmm(null, 'mdown');
    }
  }

  convertTime(value, type) {
    if(type == 'hh') {
      return ('0'+(parseInt(value) % 24)).substr(-2);
    }
    return ('0'+(parseInt(value) % 60)).substr(-2);
  }

  _convertText(value) {
    return this.convertTime(value.hh, 'hh')+':'+this.convertTime(value.mm, 'mm');
  }

  reset() {
    this.orario = { hh: 0, mm: 0 };
  }

  _closeDialog() {
    this.dialogRef.close(this.orario);
  }
}
