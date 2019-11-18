import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { MediaMatcher } from "@angular/cdk/layout";
import { HttpClientModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';

import { AppComponent } from './app.component';

import { LinkService } from './services/link.service';
import { GovpayService } from './services/govpay.service';
import { UtilService } from './services/util.service';
import { AuthGuardService } from './services/auth-guard.service';

import { RoutingClass } from './app.router';
import { RouteReuseStrategy } from '@angular/router';
import { ListReuseStrategy } from './list-reuse-strategy';
import { EntryListComponents } from './classes/entry-point-list';

import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDatepickerModule, MatNativeDateModule, DateAdapter, MatRadioModule } from '@angular/material';
import { MatButtonModule, MatIconModule, MatMenuModule, MatListModule, MatTabsModule, MatSlideToggleModule, MatCheckboxModule } from '@angular/material';
import { MatDialogModule, MatSidenavModule, MatToolbarModule, MatTooltipModule, MatCardModule } from '@angular/material';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule, MatInputModule, MatSelectModule } from '@angular/material';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { DateFormat } from './classes/date-format';

import { HeaderMenuComponent } from './elements/header-menu/header-menu.component';
import { MobileSearchComponent } from './elements/header-menu/mobile-search.component';
import { ProfiloComponent } from './elements/profilo/profilo.component';
import { SideNavigatorComponent } from './elements/side-navigator/side-navigator.component';
import { ContentViewComponent } from './elements/content-view/content-view.component';
import { ListViewComponent } from './elements/list-view/list-view.component';
import { FormViewComponent } from './elements/list-view/form-view.component';
import { SideListComponent } from './elements/list-view/side-list.component';
import { ItemViewComponent } from './elements/item-view/item-view.component';
import { ItemDirective } from './elements/item-view/item.directive';
import { TimepickerScrollDirective } from './elements/item-view/views/date-picker-view/date-picker-view.component';
import { DetailViewComponent } from './elements/detail-view/detail-view.component';
import { SpinnerComponent } from './elements/spinner/spinner.component';
import { ProgressComponent } from './elements/progress/progress.component';
import { LinkScrollDirective } from './link-scroll.directive';
import { DialogViewComponent } from './elements/detail-view/views/dialog-view/dialog-view.component';
import { DialogBlueViewComponent } from './elements/detail-view/views/dialog-view/dialog-blue-view.component';
import { LogoComponent } from './elements/logo/logo.component';
import { DashboardViewComponent } from './elements/detail-view/views/dashboard-view/dashboard-view.component';
import { BadgeCardComponent } from './elements/badge-card/badge-card.component';
import { LoginCardComponent } from './elements/login-card/login-card.component';
import { NotaViewComponent } from './elements/detail-view/views/dialog-view/dialog-views/nota-view/nota-view.component';
import { TwoColsViewComponent } from './elements/item-view/views/two-cols-view/two-cols-view.component';

import { JsonSchemaFormModule, MaterialDesignFrameworkModule } from 'angular2-json-schema-form';
import { GeneratorsEntryListComponents } from "./classes/generators-entry-point-list";
import { TipoPendenzaViewComponent } from './elements/detail-view/views/dialog-view/dialog-views/tipo-pendenza-view/tipo-pendenza-view.component';
import { FileSelectorViewComponent } from './elements/file-selector-view/file-selector-view.component';
import { FabGroupComponent } from './elements/fab-group/fab-group.component';
import { ReportProspettoRiscossioniViewComponent } from './elements/detail-view/views/dialog-view/dialog-views/report-prospetto-riscossioni-view/report-prospetto-riscossioni-view.component';
import { ImpostazioniViewComponent } from './elements/impostazioni-view/impostazioni-view.component';
import { AutorizzazioneEnteUoViewComponent } from './elements/detail-view/views/dialog-view/dialog-views/autorizzazione-ente-uo-view/autorizzazione-ente-uo-view.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderMenuComponent,
    MobileSearchComponent,
    ProfiloComponent,
    DashboardViewComponent,
    SideNavigatorComponent,
    ContentViewComponent,
    ListViewComponent,
    FormViewComponent,
    SideListComponent,
    ItemViewComponent,
    ItemDirective,
    DialogViewComponent,
    DialogBlueViewComponent,
    DetailViewComponent,
    EntryListComponents,
    SpinnerComponent,
    ProgressComponent,
    LinkScrollDirective,
    LogoComponent,
    TimepickerScrollDirective,
    BadgeCardComponent,
    LoginCardComponent,
    NotaViewComponent,
    TwoColsViewComponent,
    TipoPendenzaViewComponent,
    FileSelectorViewComponent,
    FabGroupComponent,
    ReportProspettoRiscossioniViewComponent,
    ImpostazioniViewComponent,
    AutorizzazioneEnteUoViewComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RoutingClass,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatListModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatExpansionModule,
    MatCardModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatSnackBarModule,
    MatDialogModule,
    MatSlideToggleModule,
    MatDatepickerModule, MatNativeDateModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    FlexLayoutModule,
    MaterialDesignFrameworkModule,
    JsonSchemaFormModule.forRoot(MaterialDesignFrameworkModule)
  ],
  entryComponents: [ EntryListComponents, GeneratorsEntryListComponents, DialogViewComponent, ItemViewComponent ],
  providers: [ { provide: RouteReuseStrategy, useClass: ListReuseStrategy },
    MediaMatcher, LinkService, GovpayService, UtilService,
    { provide: DateAdapter, useClass: DateFormat }, AuthGuardService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
  constructor(private dateAdapter: DateAdapter<Date>) {
    dateAdapter.setLocale('it-IT'); // DD/MM/YYYY
  }
}
