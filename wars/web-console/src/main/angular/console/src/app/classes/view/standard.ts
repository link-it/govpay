import { Dato } from './dato';

export class Standard {

  titolo: Dato = new Dato({ label: '', value: '' });
  sottotitolo: Dato = new Dato({ label: '', value: '' });
  importo: string = null;
  stato: string = null;

  constructor (_data?: any) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          if(_data[key] !== null && _data[key] !== undefined) {
            this[key] = _data[key].toString();
          } else {
            this[key] = (key != 'importo')?'n/a':-1;
          }
        }
      }
    }
  }

  getStandardTitle(): string{
    return [this.titolo.label, this.titolo.value].join(' ').trim();
  }
}
