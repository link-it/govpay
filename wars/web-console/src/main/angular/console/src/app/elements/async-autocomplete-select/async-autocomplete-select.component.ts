import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { map, startWith } from 'rxjs/operators';
import { MatAutocomplete, MatAutocompleteTrigger } from '@angular/material';

@Component({
  selector: 'link-async-autocomplete-select',
  templateUrl: './async-autocomplete-select.component.html',
  styleUrls: ['./async-autocomplete-select.component.scss']
})
export class AsyncAutocompleteSelectComponent implements OnInit {
  @ViewChild('element', { read: MatAutocompleteTrigger }) _trigger: MatAutocompleteTrigger;
  private _dataProvider: any[] = [];
  set dataProvider(value: any[]) {
    this._dataProvider = value;
    if (this.control) {
      this.control.updateValueAndValidity({ onlySelf: true });
    }
  }

  @Input() label: string = '';
  @Input() optionLabel: string = 'label';
  @Input() optionValue: string = 'value';
  @Input() optionFilter: string = 'label';
  @Input() labelMore: string = 'Carica altri...';
  @Input() get dataProvider(): any[] {
    return this._dataProvider;
  }
  @Input() control: FormControl;
  @Input() fieldRequired: boolean = false;
  @Input() scrollLimit: boolean = false;

  @Output() scrollOver: EventEmitter<any> = new EventEmitter(null);
  @Output() change: EventEmitter<any> = new EventEmitter(null);

  _SCROLL: number = 0;
  _hasSelection: boolean = false;
  _filteredOptions: Observable<any[]>;

  _displayFn = function (item?: any): string | undefined {
    let _option;
    if(item && item[this.optionValue].trim() !== '' || (typeof item === 'string' && item.trim() !== '')) {
      const _value: string = (typeof item === 'string')?item:item[this.optionValue];
      _option = (this._dataProvider || []).filter((option) => {
        if (typeof option === 'string') {
          return option.indexOf(_value) !== -1;
        } else {
          return (option[this.optionLabel].toLowerCase().indexOf(_value.toLowerCase()) !== -1 ||
                  option[this.optionValue].toLowerCase().indexOf(_value.toLowerCase()) !== -1);
        }
      });
    }
    return (_option && _option[0][this.optionLabel]) || undefined;
  }.bind(this);

  constructor() { }

  ngOnInit() {
    if (this.control) {
    this._filteredOptions = this.control.valueChanges
      .pipe(
        startWith(''),
        map((value) => this._filter(value))
      );
    }
  }

  protected _onSelection(event: any) {
    this._hasSelection = (event.option);
    this.change.emit({ value: event.option.value, source: event.option });
  }

  protected _onAutoClick(event: any, autoComplete: MatAutocomplete) {
    if (this._trigger && !this._trigger.panelOpen) {
      this._trigger._handleInput(event);
      if (this.scrollLimit) {
        setTimeout(() => {
          this._registerForScroll(autoComplete);
        }, 500);
      }
    }
  }

  protected _registerForScroll(element: MatAutocomplete) {
    if (element) {
      if (element.isOpen) {
        this._SCROLL = 0;
        const _panel: any = element.panel.nativeElement;
        _panel.removeEventListener('scroll', this._scrollingFct.bind(this));
        _panel.removeEventListener('wheel', this._scrollingFct.bind(this));
        _panel.addEventListener('scroll', this._scrollingFct.bind(this));
        _panel.addEventListener('wheel', this._scrollingFct.bind(this));
      }
    }
  }

  protected _scrollingFct(event: any) {
    const OFFSET: number = 100;
    const SIZE: number = event.currentTarget.scrollHeight - event.currentTarget.clientHeight;
    if (event.currentTarget.scrollTop > (SIZE - OFFSET) && (event.type === 'wheel' || event.currentTarget.scrollTop > this._SCROLL)) {
      this.scrollOver.emit();
    }
    this._SCROLL = event.target.scrollTop;
  }

  protected _reset(target: any) {
    this._hasSelection = false;
    target.value = '';
    if (this.control) {
      this.control.reset();
      this.change.emit({ value: null, source: null });
    }
  }

  protected _filter(value: any): any[] {
    let filterValue: string = '';
    if (typeof value === 'string') {
      filterValue = value?value.toLowerCase():'';
    } else {
      filterValue = value?(value[this.optionLabel] || ''):'';
    }
    return (this._dataProvider || []).filter((option) => {
      if (typeof option === 'string') {
        return (option.toLowerCase().indexOf(filterValue) !== -1);
      } else {
        if (this.optionLabel && this.optionValue) {
          return (option[this.optionLabel].toLowerCase().indexOf(filterValue) !== -1 || option[this.optionValue].toLowerCase().indexOf(filterValue) !== -1);
        }
        return false;
      }
    });
  }

}
