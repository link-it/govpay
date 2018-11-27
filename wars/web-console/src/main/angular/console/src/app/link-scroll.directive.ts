import { Directive } from '@angular/core';
import { Input, Output, ElementRef } from '@angular/core';
import { HostListener, EventEmitter } from '@angular/core';

@Directive({
  selector: '[link-scroll]'
})
export class LinkScrollDirective {

  private _bottomDistance: number = 0;
  private _topDistance: number = 0;
  private _limit: boolean = false;

  private _startPointY: number = 0;

  @Input() set top(distance: number) {
    this._topDistance = distance;
  }
  @Input() set bottom(distance: number) {
    this._bottomDistance = distance;
  }
  @Input() bottomLimit: boolean = false;
  @Input() topLimit: boolean = false;

  @Output() onScrollBottom = new EventEmitter();
  @Output() onScrollTop = new EventEmitter();
  @Output() onMouseWheel = new EventEmitter();

  @HostListener('scroll', ['$event', 'true']) onMouseScroll(event: any) {
    this.mouseScrollFunction(event);
  }

  @HostListener('mousewheel', ['$event', 'true']) onMouseWheelChrome(event: any) {
    this.mouseWheelFunction(event);
  }

  @HostListener('DOMMouseScroll', ['$event', 'true']) onMouseWheelFirefox(event: any) {
    this.mouseWheelFunction(event);
  }

  @HostListener('onmousewheel', ['$event', 'true']) onMouseWheelIE(event: any) {
    this.mouseWheelFunction(event);
  }

  @HostListener('touchstart', ['$event']) onTs(event) {
    let _localEvent = event.originalEvent || event;
    this._startPointY = _localEvent.touches[0].clientY;
  }

  @HostListener('touchmove', ['$event']) onTm(event) {
    let direction: number = 0;
    let _localEvent = event.originalEvent || event;
    let _currentY = _localEvent.touches[0].clientY;
    if(_currentY > this._startPointY) {
      direction = -1;
    } else if(_currentY < this._startPointY){
      direction = 1;
    }
    let _event = { direction: direction, deltaY: direction * Math.abs(_currentY - this._startPointY) };
    this.touchFunction(_event);

    this._startPointY = _currentY;
  }

  constructor(private el: ElementRef) {}

  touchFunction(event: any) {
    let target = this.el.nativeElement;
    this._limit = false;
    let _margin = (target.scrollHeight - target.clientHeight - target.scrollTop);
    if(this.bottomLimit && _margin <= this._bottomDistance && event.direction > 0) {
      this._limit = true;
      this.onScrollBottom.emit({ target: target });
    }
    if(this.topLimit && target.scrollTop <= this._topDistance && event.direction < 0) {
      this._limit = true;
      this.onScrollTop.emit({ target: target });
    }
  }

  mouseWheelFunction(event: any) {
    let _event = window.event || event; // old IE support
    let _direction = _event.wheelDelta || -10*_event.detail;
    let target = this.el.nativeElement;
    this._limit = false;
    let _margin = (target.scrollHeight - target.clientHeight - target.scrollTop);
    if(this.bottomLimit && _margin <= this._bottomDistance && _direction < 0) {
      this._limit = true;
      this.onScrollBottom.emit({ target: target });
    }
    if(this.topLimit && target.scrollTop <= this._topDistance && _direction > 0) {
      this._limit = true;
      this.onScrollTop.emit({ target: target });
    }
  }

  mouseScrollFunction(event: any) {
    let _direction = (window.scrollY > this._startPointY)?1:-1;
    this._startPointY = window.scrollY;
    let target = this.el.nativeElement;
    this._limit = false;
    let _margin = (target.scrollHeight - target.clientHeight - target.scrollTop);
    if(this.bottomLimit && _margin <= this._bottomDistance && _direction < 0) {
      this._limit = true;
      this.onScrollBottom.emit({ target: target });
    }
    if(this.topLimit && target.scrollTop <= this._topDistance && _direction > 0) {
      this._limit = true;
      this.onScrollTop.emit({ target: target });
    }
  }

}




