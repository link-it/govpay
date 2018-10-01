import { Component, Input } from '@angular/core';
import { FormInput } from '../../../../classes/view/form-input';
import { FormGroup } from '@angular/forms';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-boolean-view',
  templateUrl: './boolean-view.component.html',
  styleUrls: ['./boolean-view.component.scss']
})
export class BooleanViewComponent implements IFormComponent {

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

}
