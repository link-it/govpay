import { FormGroup } from '@angular/forms';

export interface IFormComponent {

  json: any;
  fGroup: FormGroup;

  verifyDisplay?(): boolean;

  markRequiredFormControl?(value: boolean);

  mapToJson?(): any;

}
