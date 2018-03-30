import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../modal-behavior';

export interface IModalDialog {

  json: any;

  parent?: any;
  modified?: boolean;

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior);
  refresh(mb: ModalBehavior);

  infoDetail?(): any;
  title?(): string;

}
