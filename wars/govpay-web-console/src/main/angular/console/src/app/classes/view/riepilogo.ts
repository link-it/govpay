import { Standard } from './standard';

export class Riepilogo extends Standard {

  extraInfo: Array<any> = []; //[ { label: 'Label', value: 'Value' } ];

  cancel: boolean;
  dataAnnullamento: string = '00/00/0000';
  causale: string = '';

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
