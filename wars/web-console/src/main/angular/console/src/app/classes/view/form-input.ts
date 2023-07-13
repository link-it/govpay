import { HttpClient } from '@angular/common/http';

export class FormInput {

  protected static MAX_INPUT = 524288;

  id: string = '';
  label: string = '';
  noOptionLabel: string = 'Nessuno'; //Default no select option label
  placeholder: string = '';
  maxlength: number = FormInput.MAX_INPUT;
  type: string = '';
  values: any[] = [];
  value: any = '';
  showTime: boolean = true;
  showTooltip: boolean = true; // Tooltip filterable/select
  optionControlValue: boolean = false; // Default filerable display input value
  defaultTime: string = null; //HH:MM
  pattern: RegExp = null;
  required: boolean = false;
  promise: any = {
    async: false,
    url: '',
    mapFct: Function,
    loaded: true,
    eventType: null
  };

  dependency: string = null;
  source: any = null;
  target: any = null;


  constructor (_data?: any, private http?: HttpClient) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          this[key] = _data[key];
        }
      }
    }
  }

  MaxInput(): number {
    return FormInput.MAX_INPUT;
  }

  asyncValues() {
    if(this.promise && this.promise.async && this.promise.url) {
      this.promise.loaded = false;
      document.dispatchEvent(new CustomEvent(this.promise.eventType, { detail: { asyncLoaded: this.promise.loaded }, bubbles: true }));
      this.http.get(this.promise.url).subscribe(
        (response) => {
          this.values = this.promise.mapFct?this.promise.mapFct(response):[];
          this.promise.loaded = true;
          document.dispatchEvent(new CustomEvent(this.promise.eventType, { detail: { asyncLoaded: this.promise.loaded }, bubbles: true }));
        },
        (error) => {
          console.log('Caricamento asincrono non completato.', error);
        }
      );
    }
  }
}
