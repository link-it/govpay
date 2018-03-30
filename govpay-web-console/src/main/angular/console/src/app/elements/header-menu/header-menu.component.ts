import { Component, EventEmitter, HostListener, Input } from '@angular/core';
import { OnInit, Output, ViewEncapsulation } from '@angular/core';

const MOBILE_SEARCH_SIZE: number = 56;

@Component({
  selector: 'link-header-menu',
  templateUrl: './header-menu.component.html',
  styleUrls: ['./header-menu.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HeaderMenuComponent implements OnInit {
  @HostListener('window:resize') onResize() {
    this._toggleInputSearch(false);
  }

  @Input('head-title') _title: string = 'Header menu title';
  @Input('actions') _actions: any[] = [];
  @Input('actions-label') _actionLabel = 'label';
  @Input('action-menu') _showActionMenu: boolean = false;
  @Input('back-icon') _showBackIcon: boolean = false;
  @Input('menu-icon') _showMenuIcon: boolean = false;
  @Input('search-icon') _showSearchIcon: boolean = false;

  @Output('icon-menu') onIconMenu: EventEmitter<any> = new EventEmitter();
  @Output('icon-back') onIconBack: EventEmitter<any> = new EventEmitter();
  @Output('toggle-search') onToggleSearch: EventEmitter<any> = new EventEmitter();
  @Output('search') onSearch: EventEmitter<any> = new EventEmitter();
  @Output('on-action') onAction: EventEmitter<any> = new EventEmitter();

  _showInputSearch: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  /**
   * Internal menu controller
   * @private
   */
  protected _dispatchSideMenu(isIconBack: boolean = false) {
    (isIconBack)?this.onIconBack.emit():this.onIconMenu.emit();
  }

  /**
   * Internal action menu icon controller
   * @private
   */
  protected _showActionMenuIcon() {
    return this._showActionMenu;
  }

  /**
   * Internal mobile search controller
   * @param _value
   * @private
   */
  protected _toggleSearch(_value: boolean) {
    this._showSearchIcon = !_value;
    this._toggleInputSearch(_value);
  }

  protected _toggleInputSearch(_value: boolean) {
    this._showInputSearch = _value;
    this.onToggleSearch.emit({ inputSearch: _value, iconSearch: this._showSearchIcon, size: _value?MOBILE_SEARCH_SIZE:0 });
  }

  /**
   * Internal mobile search keyboard controller
   * @param event
   * @private
   */
  protected _onEnter() {
    this._toggleSearch(false);
    this.onSearch.emit({ text: '' });
  }

  /**
   * Internal mobile search out controller
   * @param event
   * @private
   */
  protected _onOut() {
    this._toggleSearch(false);
  }

  /**
   * Internal action menu trigger
   * @param value
   * @private
   */
  protected _actionTrigger(value) {
    this.onAction.emit({ target: value });
  }

  closeSearch() {
    this._toggleSearch(false);
  }

}
