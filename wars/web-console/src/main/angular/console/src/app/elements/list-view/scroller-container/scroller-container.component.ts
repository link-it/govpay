import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';

@Component({
  selector: 'link-scroller-container',
  templateUrl: './scroller-container.component.html',
  styleUrls: ['./scroller-container.component.scss']
})
export class ScrollerContainerComponent implements OnInit {

  @Input('is-loading') _preventMultiCall: boolean = false;
  @Output('load-more') _loadMore: EventEmitter<any> = new EventEmitter(null);

  constructor() { }

  ngOnInit() {
  }

  /**
   * Infinite scrolling
   * @private
   */
  protected _loadMoreData() {
    this._loadMore.emit({ event: 'load-more' });
  }

}
