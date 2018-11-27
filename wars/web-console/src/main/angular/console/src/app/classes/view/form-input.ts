export class FormInput {

  protected static MAX_INPUT = 524288;

  id: string = '';
  label: string = '';
  placeholder: string = '';
  maxlength: number = FormInput.MAX_INPUT;
  type: string = '';
  values: any[] = [];
  value: any = '';
  showTime: boolean = true;
  defaultTime: string = null; //HH:MM
  pattern: RegExp = null;
  required: boolean = false;

  dependency: string = null;
  source: any = null;
  target: any = null;


  constructor (_data?: any) {
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
}
