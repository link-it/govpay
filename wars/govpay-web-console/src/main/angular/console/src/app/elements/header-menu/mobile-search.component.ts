import { Component, ElementRef, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'link-mobile-search',
  templateUrl: './mobile-search.component.html',
  styleUrls: ['./mobile-search.component.scss']
})
export class MobileSearchComponent implements OnInit {
  @ViewChild('search') _mobileSearchInput: ElementRef;

  @Output() enter: EventEmitter<any> = new EventEmitter(null);
  @Output() out: EventEmitter<any> = new EventEmitter(null);

  protected keyEvent: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this._mobileSearchInput?this._mobileSearchInput.nativeElement.focus():null;
    });
  }

  /**
   * Internal mobile search keyboard controller
   * @param event
   * @private
   */
  private _onEnter(event) {
    this.keyEvent = false;
    if(event.keyCode == 13 || event.code == 'Enter') {
      this.keyEvent = true;
      this.enter.emit();
    }
  }

  /**
   * Internal mobile search blur controller
   * @param event
   * @private
   */
  private _onBlur(event) {
    if(!this.keyEvent) {
      this.out.emit();
    }
  }

}
