import { ComponentRef, Type } from '@angular/core';

import { UtilService } from '../services/util.service';

import { RiepilogoViewComponent } from '../elements/item-view/views/riepilogo-view/riepilogo-view.component';
import { StandardViewComponent } from '../elements/item-view/views/standard-view/standard-view.component';
import { CronoViewComponent } from '../elements/item-view/views/crono-view/crono-view.component';
import { CronoCodeViewComponent } from '../elements/item-view/views/crono-code-view/crono-code-view.component';
import { KeyValueViewComponent } from '../elements/item-view/views/key-value-view/key-value-view.component';
import { TwoColsViewComponent } from '../elements/item-view/views/two-cols-view/two-cols-view.component';
import { TwoColsCollapseViewComponent } from '../elements/item-view/views/two-cols-collapse-view/two-cols-collapse-view.component';

import { InputViewComponent } from '../elements/item-view/views/input-view/input-view.component';
import { FilterableViewComponent } from '../elements/item-view/views/filterable-view/filterable-view.component';
import { SelectViewComponent } from '../elements/item-view/views/select-view/select-view.component';
import { BooleanViewComponent } from '../elements/item-view/views/boolean-view/boolean-view.component';
import { LabelViewComponent } from '../elements/item-view/views/label-view/label-view.component';
import { DatePickerViewComponent, TimePickerDialogComponent } from '../elements/item-view/views/date-picker-view/date-picker-view.component';

import { PendenzeViewComponent } from '../elements/detail-view/views/pendenze-view/pendenze-view.component';
import { PagamentiViewComponent } from '../elements/detail-view/views/pagamenti-view/pagamenti-view.component';
import { RegistroIntermediariViewComponent } from '../elements/detail-view/views/registro-intermediari-view/registro-intermediari-view.component';
import { RuoliViewComponent } from '../elements/detail-view/views/ruoli-view/ruoli-view.component';
import { ApplicazioniViewComponent } from '../elements/detail-view/views/applicazioni-view/applicazioni-view.component';
import { DominiViewComponent } from '../elements/detail-view/views/domini-view/domini-view.component';
import { OperatoriViewComponent } from '../elements/detail-view/views/operatori-view/operatori-view.component';
import { StazioneViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/stazione-view/stazione-view.component';
import { IntermediarioViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/intermediario-view/intermediario-view.component';
import { ApplicazioneViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/applicazione-view/applicazione-view.component';
import { EntrataViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/entrata-view/entrata-view.component';
import { TipiPendenzaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/tipi-pendenza-view/tipi-pendenza-view.component';
import { DominioViewComponent, AlertDialog } from '../elements/detail-view/views/dialog-view/dialog-views/dominio-view/dominio-view.component';
import { PendenzaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/pendenza-view/pendenza-view.component';
import { SchedaPendenzaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/scheda-pendenza-view/scheda-pendenza-view.component';
import { EntrataDominioViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/entrata-dominio-view/entrata-dominio-view.component';
import { TipiPendenzaDominioViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/tipi-pendenza-dominio-view/tipi-pendenza-dominio-view.component';
import { UnitaOperativaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/unita-operativa-view/unita-operativa-view.component';
import { AclViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/acl-view/acl-view.component';
import { IbanViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/iban-view/iban-view.component';
import { EntrateViewComponent } from '../elements/detail-view/views/entrate-view/entrate-view.component';
import { OperatoreViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/operatore-view/operatore-view.component';
import { GiornaleEventiViewComponent } from '../elements/detail-view/views/giornale-eventi-view/giornale-eventi-view.component';
import { RiscossioniViewComponent } from '../elements/detail-view/views/riscossioni-view/riscossioni-view.component';
import { RendicontazioniViewComponent } from '../elements/detail-view/views/rendicontazioni-view/rendicontazioni-view.component';
import { IncassiViewComponent } from '../elements/detail-view/views/incassi-view/incassi-view.component';
import { IncassoViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/incasso-view/incasso-view.component';
import { RppsViewComponent } from '../elements/detail-view/views/rpps-view/rpps-view.component';
import { StandardCollapseViewComponent } from '../elements/item-view/views/standard-collapse-view/standard-collapse-view.component';
import { RuoloViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/ruolo-view/ruolo-view.component';
import { TracciatiViewComponent } from '../elements/detail-view/views/tracciati-view/tracciati-view.component';
import { TracciatoViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/tracciato-view/tracciato-view.component';
import { NotaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/nota-view/nota-view.component';
import { TipiPendenzeViewComponent } from '../elements/detail-view/views/tipi-pendenze-view/tipi-pendenze-view.component';
import { TipoPendenzaViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/tipo-pendenza-view/tipo-pendenza-view.component';
import { ReportProspettoRiscossioniViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/report-prospetto-riscossioni-view/report-prospetto-riscossioni-view.component';
import { AutorizzazioneEnteUoViewComponent } from '../elements/detail-view/views/dialog-view/dialog-views/autorizzazione-ente-uo-view/autorizzazione-ente-uo-view.component';

export class EntryPointList {

  public static entryList: Array<any> = [
    PendenzeViewComponent,
    PagamentiViewComponent,
    RegistroIntermediariViewComponent,
    TipiPendenzeViewComponent,
    ApplicazioniViewComponent,
    RppsViewComponent,
    GiornaleEventiViewComponent,
    RiscossioniViewComponent,
    RendicontazioniViewComponent,
    RuoliViewComponent,
    IncassiViewComponent,
    DominiViewComponent,
    EntrateViewComponent,
    TracciatiViewComponent,
    TracciatoViewComponent,
    NotaViewComponent,
    ReportProspettoRiscossioniViewComponent,
    OperatoriViewComponent,
    StandardViewComponent,
    StandardCollapseViewComponent,
    RiepilogoViewComponent,
    StazioneViewComponent,
    IncassoViewComponent,
    EntrataViewComponent,
    TipiPendenzaViewComponent,
    TipoPendenzaViewComponent,
    DominioViewComponent, AlertDialog,
    EntrataDominioViewComponent,
    TipiPendenzaDominioViewComponent,
    UnitaOperativaViewComponent,
    IbanViewComponent,
    AutorizzazioneEnteUoViewComponent,
    PendenzaViewComponent,
    SchedaPendenzaViewComponent,
    IntermediarioViewComponent,
    ApplicazioneViewComponent,
    OperatoreViewComponent,
    AclViewComponent,
    RuoloViewComponent,
    CronoViewComponent,
    CronoCodeViewComponent,
    KeyValueViewComponent,
    TwoColsViewComponent,
    TwoColsCollapseViewComponent,
    InputViewComponent,
    FilterableViewComponent,
    DatePickerViewComponent, TimePickerDialogComponent,
    SelectViewComponent,
    BooleanViewComponent,
    LabelViewComponent
  ];

  public static getComponentByName(name: string):Type<any> {
    let _type = null;

    switch (name) {
      //Component view ref
      case UtilService.PENDENZE:
        _type = PendenzeViewComponent;
        break;
      case UtilService.PAGAMENTI:
        _type = PagamentiViewComponent;
        break;
      case UtilService.REGISTRO_INTERMEDIARI:
        _type = RegistroIntermediariViewComponent;
        break;
      case UtilService.TIPI_PENDENZE:
        _type = TipiPendenzeViewComponent;
        break;
      case UtilService.APPLICAZIONI:
        _type = ApplicazioniViewComponent;
        break;
      case UtilService.GIORNALE_EVENTI:
        _type = GiornaleEventiViewComponent;
        break;
      case UtilService.RISCOSSIONI:
        _type = RiscossioniViewComponent;
        break;
      case UtilService.RENDICONTAZIONI:
        _type = RendicontazioniViewComponent;
        break;
      case UtilService.INCASSI:
        _type = IncassiViewComponent;
        break;
      case UtilService.DOMINI:
        _type = DominiViewComponent;
        break;
      case UtilService.RUOLI:
        _type = RuoliViewComponent;
        break;
      case UtilService.ENTRATE:
        _type = EntrateViewComponent;
        break;
      case UtilService.OPERATORI:
        _type = OperatoriViewComponent;
        break;
      case UtilService.TRACCIATI:
        _type = TracciatiViewComponent;
        break;
      //Item view ref
      case UtilService.RIEPILOGO:
        _type = RiepilogoViewComponent;
        break;
      case UtilService.CRONO:
        _type = CronoViewComponent;
        break;
      case UtilService.CRONO_CODE:
        _type = CronoCodeViewComponent;
        break;
      case UtilService.STANDARD_COLLAPSE:
        _type = StandardCollapseViewComponent;
        break;
      case UtilService.TWO_COLS:
        _type = TwoColsViewComponent;
        break;
      case UtilService.TWO_COLS_COLLAPSE:
        _type = TwoColsCollapseViewComponent;
        break;
      case UtilService.KEY_VALUE:
        _type = KeyValueViewComponent;
        break;
      //Dialog views
      case UtilService.SCHEDA_PENDENZA:
        _type = SchedaPendenzaViewComponent;
        break;
      case UtilService.STAZIONE:
        _type = StazioneViewComponent;
        break;
      case UtilService.AUTORIZZAZIONE_ENTE_UO:
        _type = AutorizzazioneEnteUoViewComponent;
        break;
      case UtilService.INTERMEDIARIO:
        _type = IntermediarioViewComponent;
        break;
      case UtilService.APPLICAZIONE:
        _type = ApplicazioneViewComponent;
        break;
      case UtilService.RPPS:
        _type = RppsViewComponent;
        break;
      case UtilService.OPERATORE:
        _type = OperatoreViewComponent;
        break;
      case UtilService.ACL:
        _type = AclViewComponent;
        break;
      case UtilService.RUOLO:
        _type = RuoloViewComponent;
        break;
      case UtilService.ENTRATA:
        _type = EntrataViewComponent;
        break;
      case UtilService.TIPI_PENDENZA:
        _type = TipiPendenzaViewComponent;
        break;
      case UtilService.TIPO_PENDENZA:
        _type = TipoPendenzaViewComponent;
        break;
      case UtilService.INCASSO:
        _type = IncassoViewComponent;
        break;
      case UtilService.DOMINIO:
        _type = DominioViewComponent;
        break;
      case UtilService.ENTRATA_DOMINIO:
        _type = EntrataDominioViewComponent;
        break;
      case UtilService.TIPI_PENDENZA_DOMINIO:
        _type = TipiPendenzaDominioViewComponent;
        break;
      case UtilService.UNITA_OPERATIVA:
        _type = UnitaOperativaViewComponent;
        break;
      case UtilService.IBAN_ACCREDITO:
        _type = IbanViewComponent;
        break;
      case UtilService.PENDENZA:
        _type = PendenzaViewComponent;
        break;
      case UtilService.TRACCIATO:
        _type = TracciatoViewComponent;
        break;
      case UtilService.NOTA:
        _type = NotaViewComponent;
        break;
      case UtilService.REPORT_PROSPETTO_RISCOSSIONI:
        _type = ReportProspettoRiscossioniViewComponent;
        break;
      //Material Lib
      case UtilService.INPUT:
        _type = InputViewComponent;
        break;
      case UtilService.FILTERABLE:
        _type = FilterableViewComponent;
        break;
      case UtilService.DATE_PICKER:
        _type = DatePickerViewComponent;
        break;
      case UtilService.SELECT:
        _type = SelectViewComponent;
        break;
      case UtilService.SLIDE_TOGGLE:
        _type = BooleanViewComponent;
        break;
      case UtilService.LABEL:
        _type = LabelViewComponent;
        break;
      //Default Item view ref
      default:
        _type = StandardViewComponent;
    }

    return _type;
  }

  private static getComponentName(componentRef: ComponentRef<any>): string {
    let _name: string = null;
    switch (componentRef.componentType.name) {
      //Component view ref
      case 'PendenzeViewComponent':
        _name = UtilService.PENDENZE;
        break;
      case 'PagamentiViewComponent':
        _name = UtilService.PAGAMENTI;
        break;
      case 'RegistroIntermediariViewComponent':
        _name = UtilService.REGISTRO_INTERMEDIARI;
        break;
      case 'TipiPendenzeViewComponent':
        _name = UtilService.TIPI_PENDENZE;
        break;
      case 'ApplicazioniViewComponent':
        _name = UtilService.APPLICAZIONI;
        break;
      case 'RppsViewComponent':
        _name = UtilService.RPPS;
        break;
      case 'GiornaleEventiViewComponent':
        _name = UtilService.GIORNALE_EVENTI;
        break;
      case 'RiscossioniViewComponent':
        _name = UtilService.RISCOSSIONI;
        break;
      case 'RendicontazioniViewComponent':
        _name = UtilService.RENDICONTAZIONI;
        break;
      case 'IncassiViewComponent':
        _name = UtilService.INCASSI;
        break;
      case 'DominiViewComponent':
        _name = UtilService.DOMINI;
        break;
      case 'RuoliViewComponent':
        _name = UtilService.RUOLI;
        break;
      case 'EntrateViewComponent':
        _name = UtilService.ENTRATE;
        break;
      case 'OperatoriViewComponent':
        _name = UtilService.OPERATORI;
        break;
      case 'TracciatiViewComponent':
        _name = UtilService.TRACCIATI;
        break;
      //Item view ref
      case 'RiepilogoViewComponent':
        _name = UtilService.RIEPILOGO;
        break;
      case 'CronoViewComponent':
        _name = UtilService.CRONO;
        break;
      case 'CronoCodeViewComponent':
        _name = UtilService.CRONO_CODE;
        break;
      case 'StandardCollapseViewComponent':
        _name = UtilService.STANDARD_COLLAPSE;
        break;
      case 'TwoColsViewComponent':
        _name = UtilService.TWO_COLS;
        break;
      case 'TwoColsCollapseViewComponent':
        _name = UtilService.TWO_COLS_COLLAPSE;
        break;
      case 'KeyValueViewComponent':
        _name = UtilService.KEY_VALUE;
        break;
      //Dialog views
      case 'StazioneViewComponent':
        _name = UtilService.STAZIONE;
        break;
      case 'AutorizzazioneEnteUoViewComponent':
        _name = UtilService.AUTORIZZAZIONE_ENTE_UO;
        break;
      case 'IntermediarioViewComponent':
        _name = UtilService.INTERMEDIARIO;
        break;
      case 'ApplicazioneViewComponent':
        _name = UtilService.APPLICAZIONE;
        break;
      case 'OperatoreViewComponent':
        _name = UtilService.OPERATORE;
        break;
      case 'AclViewComponent':
        _name = UtilService.ACL;
        break;
      case 'RuoloViewComponent':
        _name = UtilService.RUOLO;
        break;
      case 'EntrataViewComponent':
        _name = UtilService.ENTRATA;
        break;
      case 'TipiPendenzaViewComponent':
        _name = UtilService.TIPI_PENDENZA;
        break;
      case 'TipoPendenzaViewComponent':
        _name = UtilService.TIPO_PENDENZA;
        break;
      case 'IncassoViewComponent':
        _name = UtilService.INCASSO;
        break;
      case 'DominioViewComponent':
        _name = UtilService.DOMINIO;
        break;
      case 'EntrataDominioViewComponent':
        _name = UtilService.ENTRATA_DOMINIO;
        break;
      case 'TipiPendenzaDominioViewComponent':
        _name = UtilService.TIPI_PENDENZA_DOMINIO;
        break;
      case 'UnitaOperativaViewComponent':
        _name = UtilService.UNITA_OPERATIVA;
        break;
      case 'IbanViewComponent':
        _name = UtilService.IBAN_ACCREDITO;
        break;
      case 'SchedaPendenzaViewComponent':
        _name = UtilService.SCHEDA_PENDENZA;
        break;
      case 'PendenzaViewComponent':
        _name = UtilService.PENDENZA;
        break;
      case 'TracciatoViewComponent':
        _name = UtilService.TRACCIATO;
        break;
      case 'NotaViewComponent':
        _name = UtilService.NOTA;
        break;
      case 'ReportProspettoRiscossioniViewComponent':
        _name = UtilService.REPORT_PROSPETTO_RISCOSSIONI;
        break;
      //Material Lib
      case 'InputViewComponent':
        _name = UtilService.INPUT;
        break;
      case 'FilterableViewComponent':
        _name = UtilService.FILTERABLE;
        break;
      case 'DatePickerViewComponent':
        _name = UtilService.DATE_PICKER;
        break;
      case 'SelectViewComponent':
        _name = UtilService.SELECT;
        break;
      case 'LabelViewComponent':
        _name = UtilService.LABEL;
        break;
      case 'BooleanViewComponent':
        _name = UtilService.SLIDE_TOGGLE;
        break;
      //Default Item view ref
      default:
        _name = '';
    }

    return _name;
  }

}

export const EntryListComponents = EntryPointList.entryList;
