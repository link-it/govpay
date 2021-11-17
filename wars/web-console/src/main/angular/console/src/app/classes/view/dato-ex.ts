export class DatoEx {

  label: string = '';
  value: any = null;
  type: string = '';

  constructor (_data?: any) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          this[key] = _data[key];
        }
      }
    }
  }

  /**
   * datoToString
   * @param {string} separator: default ': '
   * @returns {string}
   */
  datoToString(separator: string = ': '): string {
    return this.label + separator + this.value;
  }

  /**
   * arraysToString
   * @param {string[]} labels
   * @param {string[]} values
   * @param {string} separator: default ' '
   * @returns {string}
   */
  public static arraysToString(labels: string[], values: string[], separator: string = ' '): string {
    let sst = [];
    labels.forEach((s, i) => {
      sst.push(s + ': ' + values[i]);
    });

    return sst.join(separator);
  }

  /**
   * concatStrings
   * @param {string[]} labels
   * @param {string} separator: default ' '
   * @returns {string}
   */
  public static concatStrings(labels: string[], separator: string = ' '): string {
    let sst = [];
    labels.forEach((s) => {
      if(s) {
        sst.push(s);
      }
    });

    return sst.join(separator);
  }

  /**
   * Array of strings to Dato object (label only)
   * @param {string[]} labels
   * @param {string[]} values
   * @param {string} separator
   * @returns {Dato}
   * @private
   */
  public static arraysToDato(labels: string[], values: string[], separator: string = ' '): DatoEx {
    let sst = [];
    labels.forEach((s, i) => {
      sst.push(s+': '+ values[i]);
    });

    return new DatoEx({ label: sst.join(separator), value: '' });
  }
}
