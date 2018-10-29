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
    if(this.json && this.json.value !== null && this.json.value !== undefined) {
      //Default selection
      this.fGroup.controls[this.json.id+'_ctrl'].setValue(this.json.value);
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

}
