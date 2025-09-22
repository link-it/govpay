import { Component, Input } from '@angular/core';
import { FormInput } from '../../../../classes/view/form-input';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Voce } from '../../../../services/voce.service';

@Component({
  selector: 'link-input-view',
  templateUrl: './input-view.component.html',
  styleUrls: ['./input-view.component.scss']
})
export class InputViewComponent implements IFormComponent {

  @Input() json: FormInput = new FormInput();
  @Input() fGroup: FormGroup;

  constructor() { }

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

  isWarning(value: string): boolean {
    if (value && this.json.warning && this.json.pattern) {
      var pattern = new RegExp(this.json.pattern);
      return !pattern.test(value);
    }
    return false;
  }

  _checkFloating(placeholder: string) {
    if(placeholder && placeholder !== undefined && placeholder !== '') {
      return 'always';
    }
    return 'auto';
  }

}
