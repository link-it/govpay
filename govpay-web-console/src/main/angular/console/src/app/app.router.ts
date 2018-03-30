import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";

import { ListViewComponent } from './elements/list-view/list-view.component';
import { DetailViewComponent } from './elements/detail-view/detail-view.component';
import { UtilService } from './services/util.service';
import { ProfiloComponent } from './elements/profilo/profilo.component';

const _routes: Routes = [
  { path: '', redirectTo: UtilService.URL_PENDENZE, pathMatch: 'full' },
  { path: UtilService.ROUTE(UtilService.URL_PENDENZE), component: ListViewComponent, data: { type: UtilService.PENDENZE, title: UtilService.TXT_PENDENZE, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PENDENZE)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.PENDENZE, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PAGAMENTI), component: ListViewComponent, data: { type: UtilService.PAGAMENTI, title: UtilService.TXT_PAGAMENTI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PAGAMENTI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.PAGAMENTI, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_PSP), component: ListViewComponent, data: { type: UtilService.REGISTRO_PSP, title: UtilService.TXT_REGISTRO_PSP, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_PSP)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.REGISTRO_PSP, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_INTERMEDIARI), component: ListViewComponent, data: { type: UtilService.REGISTRO_INTERMEDIARI, title: UtilService.TXT_REGISTRO_INTERMEDIARI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_INTERMEDIARI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.REGISTRO_INTERMEDIARI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_APPLICAZIONI), component: ListViewComponent, data: { type: UtilService.APPLICAZIONI, title: UtilService.TXT_APPLICAZIONI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_APPLICAZIONI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.APPLICAZIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_DOMINI), component: ListViewComponent, data: { type: UtilService.DOMINI, title: UtilService.TXT_DOMINI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_DOMINI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.DOMINI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_ENTRATE), component: ListViewComponent, data: { type: UtilService.ENTRATE, title: UtilService.TXT_ENTRATE, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_ENTRATE)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.ENTRATE, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_OPERATORI), component: ListViewComponent, data: { type: UtilService.OPERATORI, title: UtilService.TXT_OPERATORI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_OPERATORI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.OPERATORI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_GIORNALE_EVENTI), component: ListViewComponent, data: { type: UtilService.GIORNALE_EVENTI, title: UtilService.TXT_GIORNALE_EVENTI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_GIORNALE_EVENTI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.GIORNALE_EVENTI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RISCOSSIONI), component: ListViewComponent, data: { type: UtilService.RISCOSSIONI, title: UtilService.TXT_RISCOSSIONI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RISCOSSIONI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.RISCOSSIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RENDICONTAZIONI), component: ListViewComponent, data: { type: UtilService.RENDICONTAZIONI, title: UtilService.TXT_RENDICONTAZIONI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RENDICONTAZIONI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.RENDICONTAZIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_INCASSI), component: ListViewComponent, data: { type: UtilService.INCASSI, title: UtilService.TXT_INCASSI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_INCASSI)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.INCASSI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RPPS), component: ListViewComponent, data: { type: UtilService.RPPS, title: UtilService.TXT_RPPS, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RPPS)+'/dettaglio', component: DetailViewComponent, data: { type: UtilService.RPPS, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_ACL), component: ListViewComponent, data: { type: UtilService.ACLS, title: UtilService.TXT_ACL, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PROFILO), component: ProfiloComponent, data: { type: null, title: UtilService.TXT_PROFILO, search: false, back: false, actions: false, info: null, reuse: false } },
  { path: '**', redirectTo: '' }
];

export const RoutingClass: ModuleWithProviders = RouterModule.forRoot(_routes,{ enableTracing: false });
