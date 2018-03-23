import { Directive, ElementRef, EventEmitter, HostListener, Input, Output, Renderer2 } from '@angular/core';

@Directive({
  selector: '[link-item]'
})
export class ItemDirective {
  @HostListener('mouseenter', [ '$event' ]) onMouseEnter(event) {
    if (this._mouseEvent && this._directiveEnabled) {
      this.over(true);
    }
  }

  @HostListener('mouseleave') onMouseLeave() {
    if (this._mouseEvent && this._directiveEnabled) {
      this.over(false);
    }
    this._mouseEvent = true;
  }

  @HostListener('touchstart', [ '$event' ]) onTs(event) {
    if(this._directiveEnabled) {
      this._mouseEvent = false;
      this.touchstartX = event.changedTouches[0].screenX;
      this.delay.start = Date.now();
    }
  }

  @HostListener('touchend', [ '$event' ]) onTe(event) {
    if(this._directiveEnabled) {
      this.touchendX = event.changedTouches[ 0 ].screenX;
      this.delay.end = Date.now();
      this.delay.diff = Math.abs(this.touchendX - this.touchstartX);

      this.handleSwipe();
    }
  }

  @Input('link-item') _directiveEnabled: boolean = false;

  @Output('link-swipe-right') onSwipeRight: EventEmitter<any> = new EventEmitter();
  @Output('link-swipe-left') onSwipeLeft: EventEmitter<any> = new EventEmitter();
  @Output('link-over') onOver: EventEmitter<any> = new EventEmitter();
  @Output('link-out') onOut: EventEmitter<any> = new EventEmitter();

  protected _mouseEvent: boolean = true;
  protected touchstartX = 0;
  protected touchendX = 0;
  protected delay = { start: 0, end: 0, diff: 0 };

  constructor(private element: ElementRef, private renderer: Renderer2) {
    renderer.addClass(element.nativeElement, 'item-directive');
  }

  protected over(_isOver: boolean) {
    this.renderClass(_isOver);
    (_isOver)?this.onOver.emit():this.onOut.emit();
  }

  protected renderClass(_isOver: boolean) {
    if (_isOver) {
      this.renderer.addClass(this.element.nativeElement, 'item-directive-hover');
    } else {
      this.renderer.removeClass(this.element.nativeElement, 'item-directive-hover');
    }
  }

  protected handleSwipe() {
    this.renderClass(false);
    let touch = (this.touchendX < this.touchstartX)?-1:((this.touchendX > this.touchstartX)?1:0);
    if (touch != 0 && this.delay.diff >= 50 && (this.delay.end - this.delay.start) <= 300) {
      switch(touch) {
        case -1:
          //console.log('Swiped left');
          this.renderClass(true);
          this.onSwipeLeft.emit();
          break;
        case 1:
          //console.log('Swiped right');
          //this.renderClass(true);
          //this.onSwipeRight.emit();
          break;
      }
    }
  }

}
