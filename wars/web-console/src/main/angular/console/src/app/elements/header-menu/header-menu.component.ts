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
  }

  @Input('notification-title') _notification: string = '';
  @Input('notification-dismiss') _agree: boolean = false;
  @Input('notification-progress') _progress: number = 0;

  @Input('head-title') _title: string = '';
  @Input('head-sub-title') _subtitle: string = '';

  @Input('actions') _actions: any[] = [];
  @Input('actions-label') _actionLabel = 'label';
  @Input('action-menu') _showActionMenu: boolean = false;
  @Input('back-icon') _showBackIcon: boolean = false;
  @Input('menu-icon') _showMenuIcon: boolean = false;
  @Input('search-icon') _showSearchIcon: boolean = false;
  @Input('show-header-title') _showHeaderTitle: boolean = false;

  @Output('icon-menu') onIconMenu: EventEmitter<any> = new EventEmitter();
  @Output('icon-back') onIconBack: EventEmitter<any> = new EventEmitter();
  @Output('toggle-search') onToggleSearch: EventEmitter<any> = new EventEmitter();
  @Output('on-action') onAction: EventEmitter<any> = new EventEmitter();

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
   * Internal header controller
   * @private
   */
  protected _showUpperHeader() {
    return this._showHeaderTitle;
  }

  /**
   * Internal action menu icon controller
   * @private
   */
  protected _showActionMenuIcon() {
    return this._showActionMenu && this._actions.length != 0;
  }

  /**
   * Internal mobile search controller
   * @private
   */
  protected _toggleSearch() {
    this.onToggleSearch.emit({ iconSearch: this._showSearchIcon, size: MOBILE_SEARCH_SIZE });
  }

  /**
   * Internal action menu trigger
   * @param value
   * @private
   */
  protected _actionTrigger(value) {
    this.onAction.emit({ target: value });
  }

}
