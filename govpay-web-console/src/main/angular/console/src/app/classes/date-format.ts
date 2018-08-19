import { NativeDateAdapter } from '@angular/material';
const SUPPORTS_INTL_API = typeof Intl !== 'undefined';

export class DateFormat extends NativeDateAdapter {
  useUtcForDisplay = true;
  parse(value: any): Date | null {
    if ((typeof value === 'string') && (value.indexOf('/') > -1)) {
      const str = value.split('/');
      const year = Number(str[2]);
      const month = Number(str[1]) - 1;
      const date = Number(str[0]);
      return new Date(year, month, date);
    }
    if ((typeof value === 'string') && (value.indexOf('T') != -1)) {
      const str = value.split('T')[0].split('-');
      const year = Number(str[0]);
      const month = Number(str[1]) - 1;
      const date = Number(str[2]);
      return new Date(year, month, date);
    }
    const timestamp = typeof value === 'number' ? value : Date.parse(value);
    return isNaN(timestamp) ? null : new Date(timestamp);
  }
}
