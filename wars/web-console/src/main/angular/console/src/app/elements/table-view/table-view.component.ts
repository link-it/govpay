import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'link-table-view',
  templateUrl: './table-view.component.html',
  styleUrls: ['./table-view.component.scss']
})
export class TableViewComponent implements OnInit {

  @Input() data: any[] = null;
  @Input() attributes: any[] = [];

  constructor() { }

  ngOnInit() {}
}
