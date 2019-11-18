import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";

import { ListViewComponent } from './elements/list-view/list-view.component';
import { DetailViewComponent } from './elements/detail-view/detail-view.component';
import { UtilService } from './services/util.service';
import { ProfiloComponent } from './elements/profilo/profilo.component';
import { AuthGuardService } from './services/auth-guard.service';
import { DashboardViewComponent } from './elements/detail-view/views/dashboard-view/dashboard-view.component';
import { ImpostazioniViewComponent } from './elements/impostazioni-view/impostazioni-view.component';

const _routes: Routes = [
  { path: '', redirectTo: UtilService.ROUTE(UtilService.URL_DASHBOARD), pathMatch: 'full' },
  { path: UtilService.ROUTE(UtilService.URL_DASHBOARD), component: DashboardViewComponent, data: { type: null, title: UtilService.TXT_DASHBOARD, search: false, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PENDENZE), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.PENDENZE, title: UtilService.TXT_PENDENZE, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PENDENZE)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.PENDENZE, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PAGAMENTI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.PAGAMENTI, title: UtilService.TXT_PAGAMENTI, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_PAGAMENTI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.PAGAMENTI, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_INTERMEDIARI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.REGISTRO_INTERMEDIARI, title: UtilService.TXT_REGISTRO_INTERMEDIARI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_REGISTRO_INTERMEDIARI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.REGISTRO_INTERMEDIARI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_APPLICAZIONI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.APPLICAZIONI, title: UtilService.TXT_APPLICAZIONI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_APPLICAZIONI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.APPLICAZIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_DOMINI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.DOMINI, title: UtilService.TXT_DOMINI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_DOMINI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.DOMINI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_TIPI_PENDENZA), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.TIPI_PENDENZE, title: UtilService.TXT_TIPI_PENDENZA, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_TIPI_PENDENZA)+UtilService.URL_DETTAGLIO, canActivate: [ AuthGuardService ], component: DetailViewComponent, data: { type: UtilService.TIPI_PENDENZE, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  // { path: UtilService.ROUTE(UtilService.URL_ENTRATE), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.ENTRATE, title: UtilService.TXT_ENTRATE, search: true, back: false, actions: false, info: null, reuse: false } },
  // { path: UtilService.ROUTE(UtilService.URL_ENTRATE)+UtilService.URL_DETTAGLIO, canActivate: [ AuthGuardService ], component: DetailViewComponent, data: { type: UtilService.ENTRATE, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_OPERATORI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.OPERATORI, title: UtilService.TXT_OPERATORI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_OPERATORI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.OPERATORI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_GIORNALE_EVENTI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.GIORNALE_EVENTI, title: UtilService.TXT_GIORNALE_EVENTI, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_GIORNALE_EVENTI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.GIORNALE_EVENTI, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RISCOSSIONI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RISCOSSIONI, title: UtilService.TXT_RISCOSSIONI, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RISCOSSIONI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RISCOSSIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RENDICONTAZIONI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RENDICONTAZIONI, title: UtilService.TXT_RENDICONTAZIONI, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RENDICONTAZIONI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RENDICONTAZIONI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_INCASSI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.INCASSI, title: UtilService.TXT_INCASSI, search: true, back: false, actions: true, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_INCASSI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.INCASSI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RPPS), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RPPS, title: UtilService.TXT_RPPS, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RPPS)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RPPS, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RUOLI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RUOLI, title: UtilService.TXT_RUOLI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_RUOLI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.RUOLI, title: '', search: false, back: true, actions: false, info: null, reuse: false } },
  //Restricted
  { path: UtilService.ROUTE(UtilService.URL_TRACCIATI), component: ListViewComponent, canActivate: [ AuthGuardService ], data: { type: UtilService.TRACCIATI, title: UtilService.TXT_TRACCIATI, search: true, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_TRACCIATI)+UtilService.URL_DETTAGLIO, component: DetailViewComponent, data: { type: UtilService.TRACCIATI, title: '', search: false, back: true, actions: true, info: null, reuse: false } },
  //Profile
  { path: UtilService.ROUTE(UtilService.URL_PROFILO), component: ProfiloComponent, canActivate: [ AuthGuardService ], data: { type: null, title: UtilService.TXT_PROFILO, search: false, back: false, actions: false, info: null, reuse: false } },
  { path: UtilService.ROUTE(UtilService.URL_IMPOSTAZIONI), component: ImpostazioniViewComponent, canActivate: [ AuthGuardService ], data: { type: null, title: UtilService.TXT_IMPOSTAZIONI, search: false, back: false, actions: false, info: null, reuse: false } },
  { path: '**', redirectTo: UtilService.ROUTE(UtilService.URL_DASHBOARD) }
];

export const RoutingClass: ModuleWithProviders = RouterModule.forRoot(_routes,{ enableTracing: false });
