import { Standard } from './standard';

export class Crono extends Standard {

  data: string = '00/00/0000 hh:mm';

  constructor (_data?: any) {

    super(_data);

    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          (_data[key] !== null && _data[key] !== undefined)?this[key] = _data[key]:null;
        }
      }
    }
  }
}
