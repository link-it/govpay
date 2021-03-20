import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'link-simple-list-card',
  templateUrl: './simple-list-card.component.html',
  styleUrls: ['./simple-list-card.component.scss']
})
export class SimpleListCardComponent implements OnInit {

  @Input('title') _title: string = '';
  @Input('data-list') _dataList: any[] = [];
  @Input('no-data-label') _noDataLabel: string = 'Nessuna informazione';
  @Input('use-directive') _directiveEnable: boolean = true;
  @Input('show-actions') _showActions: boolean = false;
  @Input('action-list') _iconsList: string[] = ['edit'];

  @Input('trigger-icon') _triggerIcon: string = 'more_vert';
  @Input('show-menu-actions') _showMenuActions: boolean = false;
  @Input('menu-actions') _menuActions: SimpleListItem[] = [];

  @Output('over-icon-click') _overIconClick: EventEmitter<any> = new EventEmitter(null);
  @Output('menu-action-click') _menuActionClick: EventEmitter<any> = new EventEmitter(null);

  constructor() { }

  ngOnInit() {
  }

  protected _iconOverClick(item: any, event: any) {
    this._overIconClick.emit({ item: item, bubbleEvent: event });
  }

  protected _actionTriggerClick(value: any) {
    this._menuActionClick.emit({ target: value });
  }
}

export class SimpleListItem {
  label: string = '';
  value: string = '';
}
