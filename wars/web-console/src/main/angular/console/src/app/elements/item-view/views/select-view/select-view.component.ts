import { AfterViewInit, Component, Input } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FormInput } from '../../../../classes/view/form-input';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-select-view',
  templateUrl: './select-view.component.html',
  styleUrls: ['./select-view.component.scss']
})
export class SelectViewComponent implements IFormComponent, AfterViewInit {

  @Input() json: FormInput = new FormInput();
  @Input() fGroup: FormGroup;

  constructor() { }

  ngAfterViewInit() {
    if(this.json) {
      if (this.json.promise && this.json.promise.async) {
        document.removeEventListener(this.json.promise.eventType, this._asyncLoaded.bind(this));
        document.addEventListener(this.json.promise.eventType, this._asyncLoaded.bind(this));
        this.json.asyncValues();
      }
      if (this.json.value !== null && this.json.value !== undefined) {
        //Default selection
        this.fGroup.controls[this.json.id+'_ctrl'].setValue(this.json.value);
      }
    }
  }

  protected _asyncLoaded() {
    if(this.json && this.json.promise && this.json.promise.async) {
      if(this.json.promise.loaded) {
        this.fGroup.controls[this.json.id+'_ctrl'].enable();
        if(this.json.promise.preventSelection) {
          if(this.json.values.length === 1) {
            this.fGroup.controls[this.json.id+'_ctrl'].setValue(this.json.values[0].value);
          }
          if(this.json.values.length === 0) {
            this.fGroup.controls[this.json.id+'_ctrl'].disable();
          }
        }
      } else {
        this.fGroup.controls[this.json.id+'_ctrl'].disable();
      }
    }
  }

  protected _onChangeSelection() {
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
      if(this.json.dependency) {
        this.fGroup.controls[this.json.id+'_ctrl'].reset();
        if(this.json.required) {
          if(this.fGroup.controls.hasOwnProperty(this.json.id+'_ctrl')) {
            this.fGroup.removeControl(this.json.id+'_ctrl');
          }
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

}
