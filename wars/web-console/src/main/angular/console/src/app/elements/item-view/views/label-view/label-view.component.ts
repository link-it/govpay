import { Component, Input } from '@angular/core';
import { FormInput } from '../../../../classes/view/form-input';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'link-label-view',
  templateUrl: './label-view.component.html',
  styleUrls: ['./label-view.component.scss']
})
export class LabelViewComponent implements IFormComponent {

  @Input() json: FormInput = new FormInput();
  @Input() fGroup: FormGroup;

  constructor() { }

  verifyDisplay(): boolean {
    let _showMe: boolean = !this.json.dependency;
    if(this.json && this.json.source) {
      _showMe = (this.json.source.value == this.json.target);
    }
    return _showMe;
  }

  markRequiredFormControl(value: boolean) {
    return true;
  }

}
