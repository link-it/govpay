import { UtilService } from '../services/util.service';

export class Parameters {
  id: string = '';
  type: string = UtilService.STANDARD;
  model: any = null;
  jsonP: any = null;

  constructor (_data?: any) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          this[key] = _data[key];
        }
      }
    }
  }
}
