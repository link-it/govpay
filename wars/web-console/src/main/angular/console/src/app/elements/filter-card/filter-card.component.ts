import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';

declare let jQuery: any;

@Component({
  selector: 'link-filter-card',
  templateUrl: './filter-card.component.html',
  styleUrls: ['./filter-card.component.scss']
})
export class FilterCardComponent implements OnInit, OnDestroy {
  @Input('refresh') set refresh(value: boolean) {
    if (!this._refresh && value) {
      this._validateSearchFilter();
    }
    this._refresh = value;
  }
  @Input('search') _text: string = '';
  @Input('target') _target: string = 'searchTarget';
  @Input('title') _title: string = '';
  @Input('data-list') _dataList: any[] = [];
  @Input('action-list') _iconsList: string[] = ['edit'];
  @Input('no-data-label') _noDataLabel: string = 'Nessuna informazione';
  @Input('placeholder') _placeholder: string = 'Cerca...';
  @Input('use-directive') _directiveEnable: boolean = true;
  @Input('show-actions') _showActions: boolean = false;

  @Input('next-page') _next: string = '';
  @Input('previous-page') _previous: string = '';
  @Input('page') _page: number = 1;
  @Input('pages') _pages: number = 1;
  @Input('total-results') _results: number = 1;
  @Input('page-size') _pageSize: number = 5;

  @Output('over-icon-click') _overIconClick: EventEmitter<any> = new EventEmitter(null);
  @Output('add-icon-click') _addIconClick: EventEmitter<any> = new EventEmitter(null);
  @Output('page-event') _pageEvent: EventEmitter<any> = new EventEmitter(null);

  _refresh: boolean = false;
  _timer: any;

  constructor() { }

  ngOnInit() {
  }

  ngOnDestroy() {
    clearTimeout(this._timer);
  }

  protected _iconOverClick(item: any, event: any) {
    this._overIconClick.emit({ item: item, bubbleEvent: event });
  }

  protected _addClick() {
    this._addIconClick.emit();
  }

  protected _reset() {
    this._text = '';
    this._inputChange();
  }

  protected _inputChange() {
    clearTimeout(this._timer);
    this._timer = setTimeout(() => {
      this._pageNavEvent('first');
    }, 800);
  }

  protected _pageNavEvent(value: string) {
    if (value === 'first') {
      this._pageEvent.emit({ search: this._text, pagina: 1 });
    } else {
      this._pageEvent.emit({ search: this._text, pagina: (value === 'next')?this._page + 1:this._page - 1 });
    }
  }

  protected _validateSearchFilter() {
    setTimeout(() => {
      jQuery('#'+this._target).collapse('show');
      this._pageNavEvent('first');
    });
  }

}
