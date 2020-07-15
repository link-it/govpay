import { FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export interface IFormComponent {

  json: any;
  fGroup: FormGroup;
  notifier?: BehaviorSubject<boolean>;

  verifyDisplay?(): boolean;

  markRequiredFormControl?(value: boolean);

  mapToJson?(): any;

}
