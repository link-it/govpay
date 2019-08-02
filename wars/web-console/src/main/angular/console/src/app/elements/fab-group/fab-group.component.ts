import { Component, ElementRef, EventEmitter, HostListener, OnInit, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'link-fab-group',
  templateUrl: './fab-group.component.html',
  styleUrls: ['./fab-group.component.scss']
})
export class FabGroupComponent implements OnInit {
  @ViewChild('mfg') _mfg: ElementRef;

  @HostListener('document:click', ['$event.target']) onClick(targetElement) {
    const clickedInside = this._elementRef.nativeElement.contains(targetElement);
    if (!clickedInside) {
      this._close();
    }
  }
  @Output('mini-fab-click') _miniFabClick: EventEmitter<any> = new EventEmitter(null);

  constructor(private _elementRef : ElementRef) { }

  ngOnInit() {
  }

  _toggleExpand() {
    if (this._mfg.nativeElement.classList.contains('open')) {
      this._close();
    } else {
      this._mfg.nativeElement.classList.add('open');
    }
  }

  _close() {
    if (this._mfg && this._mfg.nativeElement) {
      this._mfg.nativeElement.classList.remove('open');
    }
  }

  _miniFabTrigger(action: string) {
    this._close();
    this._miniFabClick.emit({ value: action });
  }
}
