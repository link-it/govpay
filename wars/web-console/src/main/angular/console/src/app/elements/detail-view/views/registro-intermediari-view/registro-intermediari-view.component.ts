import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Component({
  selector: 'link-registro-intermediari-view',
  templateUrl: './registro-intermediari-view.component.html',
  styleUrls: ['./registro-intermediari-view.component.scss']
})
export class RegistroIntermediariViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() connettoriSoap = [];
  @Input() connettoriSFtp = { lettura: [], scrittura: [] };
  @Input() connettoreRT = [];
  @Input() connettoreACA = [];
  @Input() connettoreGPD = [];
  @Input() connettoreFR = [];
  @Input() stazioni = [];

  @Input() json: any;
  @Input() modified: boolean = false;


  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRegistroIntermediari();
    //this.elencoStazioni();
  }

  ngAfterViewInit() {
  }

  protected dettaglioRegistroIntermediari() {
    let _url = UtilService.URL_REGISTRO_INTERMEDIARI+'/'+UtilService.EncodeURIComponent(this.json.idIntermediario);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });

  }

  protected mapJsonDetail() {
    //Riepilogo
    let _dettaglio = { informazioni: [], connettoriSoap: [] , connettoreRT: [], connettoreACA: [], connettoreGPD: [], connettoreFR: [] };
    _dettaglio.informazioni.push(new Dato({ label: Voce.DENOMINAZIONE, value: this.json.denominazione }));
    _dettaglio.informazioni.push(new Dato({ label: Voce.ID_INTERMEDIARIO, value: this.json.idIntermediario }));
    _dettaglio.informazioni.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    if(this.json.servizioPagoPa) {
      _dettaglio.connettoriSoap.push(new Dato({ label: Voce.PRINCIPAL, value: this.json.principalPagoPa }));
      _dettaglio.connettoriSoap.push(new Dato({ label: Voce.URL, value: this.json.servizioPagoPa.urlRPT }));
      if (this.json.servizioPagoPa.subscriptionKey) {
        _dettaglio.connettoriSoap.push(new Dato({ label: Voce.OCP_APIM_SUBSCRIPTION_KEY, value: this.json.servizioPagoPa.subscriptionKey }));
      }
      if(this.json.servizioPagoPa.auth) {
        if(this.json.servizioPagoPa.auth.clientId) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.OAUTH2_CLIENT_CREDENTIALS }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID, value: this.json.servizioPagoPa.auth.clientId }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET, value: this.json.servizioPagoPa.auth.clientSecret }));
          if(this.json.servizioPagoPa.auth.scope) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_SCOPE, value: this.json.servizioPagoPa.auth.scope }));
          }
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT, value: this.json.servizioPagoPa.auth.urlTokenEndpoint }));
        }
        if(this.json.servizioPagoPa.auth.apiId) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.API_KEY }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.API_ID, value: this.json.servizioPagoPa.auth.apiId }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.API_KEY, value: this.json.servizioPagoPa.auth.apiKey }));
        }
        if(this.json.servizioPagoPa.auth.headerName) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.HTTP_HEADER }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.HEADER_NAME, value: this.json.servizioPagoPa.auth.headerName }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.HEADER_VALUE, value: this.json.servizioPagoPa.auth.headerValue }));
        }
        if(this.json.servizioPagoPa.auth.username) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPa.auth.username }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPa.auth.password }));
        }
        if(this.json.servizioPagoPa.auth.tipo) {
          _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.SSL }));
          _dettaglio.connettoriSoap.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPa.auth.tipo }));
          if(this.json.servizioPagoPa.auth.sslType) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.SSL_CFG_TYPE, value: this.json.servizioPagoPa.auth.sslType }));
          }
          if(this.json.servizioPagoPa.auth.tsType) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.TRUST_STORE_TYPE, value: this.json.servizioPagoPa.auth.tsType }));
          }
          if(this.json.servizioPagoPa.auth.tsLocation) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPa.auth.tsLocation }));
          }
          if(this.json.servizioPagoPa.auth.tsPassword) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPa.auth.tsPassword }));
          }
          if(this.json.servizioPagoPa.auth.ksType) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_TYPE, value: this.json.servizioPagoPa.auth.ksType }));
          }
          if(this.json.servizioPagoPa.auth.ksLocation) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPa.auth.ksLocation }));
          }
          if(this.json.servizioPagoPa.auth.ksPassword) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPa.auth.ksPassword }));
          }
          if(this.json.servizioPagoPa.auth.ksPKeyPasswd) {
            _dettaglio.connettoriSoap.push(new Dato({label: Voce.KEY_STORE_PWD_PRIVATE_KEY, value: this.json.servizioPagoPa.auth.ksPKeyPasswd }));
          }
        }
      } else {
        _dettaglio.connettoriSoap.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
      }
    }
	
	if(this.json.servizioPagoPaRecuperoRT) {
	      _dettaglio.connettoreRT.push(new Dato({ label: Voce.URL, value: this.json.servizioPagoPaRecuperoRT.url }));
	      if (this.json.servizioPagoPaRecuperoRT.subscriptionKey) {
	        _dettaglio.connettoreRT.push(new Dato({ label: Voce.OCP_APIM_SUBSCRIPTION_KEY, value: this.json.servizioPagoPaRecuperoRT.subscriptionKey }));
	      }
	      if(this.json.servizioPagoPaRecuperoRT.auth) {
	        if(this.json.servizioPagoPaRecuperoRT.auth.clientId) {
	          _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.OAUTH2_CLIENT_CREDENTIALS }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID, value: this.json.servizioPagoPaRecuperoRT.auth.clientId }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET, value: this.json.servizioPagoPaRecuperoRT.auth.clientSecret }));
	          if(this.json.servizioPagoPaRecuperoRT.auth.scope) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_SCOPE, value: this.json.servizioPagoPaRecuperoRT.auth.scope }));
	          }
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT, value: this.json.servizioPagoPaRecuperoRT.auth.urlTokenEndpoint }));
	        }
	        if(this.json.servizioPagoPaRecuperoRT.auth.apiId) {
	          _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.API_KEY }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.API_ID, value: this.json.servizioPagoPaRecuperoRT.auth.apiId }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.API_KEY, value: this.json.servizioPagoPaRecuperoRT.auth.apiKey }));
	        }
	        if(this.json.servizioPagoPaRecuperoRT.auth.headerName) {
	          _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.HTTP_HEADER }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.HEADER_NAME, value: this.json.servizioPagoPaRecuperoRT.auth.headerName }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.HEADER_VALUE, value: this.json.servizioPagoPaRecuperoRT.auth.headerValue }));
	        }
	        if(this.json.servizioPagoPaRecuperoRT.auth.username) {
	          _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPaRecuperoRT.auth.username }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPaRecuperoRT.auth.password }));
	        }
	        if(this.json.servizioPagoPaRecuperoRT.auth.tipo) {
	          _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.SSL }));
	          _dettaglio.connettoreRT.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPaRecuperoRT.auth.tipo }));
	          if(this.json.servizioPagoPaRecuperoRT.auth.sslType) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.SSL_CFG_TYPE, value: this.json.servizioPagoPaRecuperoRT.auth.sslType }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.tsType) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.TRUST_STORE_TYPE, value: this.json.servizioPagoPaRecuperoRT.auth.tsType }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.tsLocation) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPaRecuperoRT.auth.tsLocation }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.tsPassword) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPaRecuperoRT.auth.tsPassword }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.ksType) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.KEY_STORE_TYPE, value: this.json.servizioPagoPaRecuperoRT.auth.ksType }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.ksLocation) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPaRecuperoRT.auth.ksLocation }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.ksPassword) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPaRecuperoRT.auth.ksPassword }));
	          }
	          if(this.json.servizioPagoPaRecuperoRT.auth.ksPKeyPasswd) {
	            _dettaglio.connettoreRT.push(new Dato({label: Voce.KEY_STORE_PWD_PRIVATE_KEY, value: this.json.servizioPagoPaRecuperoRT.auth.ksPKeyPasswd }));
	          }
	        }
	      } else {
	        _dettaglio.connettoreRT.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
	      }
	    }

	if(this.json.servizioPagoPaACA) {
	      _dettaglio.connettoreACA.push(new Dato({ label: Voce.URL, value: this.json.servizioPagoPaACA.url }));
	      if (this.json.servizioPagoPaACA.subscriptionKey) {
	        _dettaglio.connettoreACA.push(new Dato({ label: Voce.OCP_APIM_SUBSCRIPTION_KEY, value: this.json.servizioPagoPaACA.subscriptionKey }));
	      }
	      if(this.json.servizioPagoPaACA.auth) {
	        if(this.json.servizioPagoPaACA.auth.clientId) {
	          _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.OAUTH2_CLIENT_CREDENTIALS }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID, value: this.json.servizioPagoPaACA.auth.clientId }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET, value: this.json.servizioPagoPaACA.auth.clientSecret }));
	          if(this.json.servizioPagoPaACA.auth.scope) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_SCOPE, value: this.json.servizioPagoPaACA.auth.scope }));
	          }
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT, value: this.json.servizioPagoPaACA.auth.urlTokenEndpoint }));
	        }
	        if(this.json.servizioPagoPaACA.auth.apiId) {
	          _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.API_KEY }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.API_ID, value: this.json.servizioPagoPaACA.auth.apiId }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.API_KEY, value: this.json.servizioPagoPaACA.auth.apiKey }));
	        }
	        if(this.json.servizioPagoPaACA.auth.headerName) {
	          _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.HTTP_HEADER }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.HEADER_NAME, value: this.json.servizioPagoPaACA.auth.headerName }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.HEADER_VALUE, value: this.json.servizioPagoPaACA.auth.headerValue }));
	        }
	        if(this.json.servizioPagoPaACA.auth.username) {
	          _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPaACA.auth.username }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPaACA.auth.password }));
	        }
	        if(this.json.servizioPagoPaACA.auth.tipo) {
	          _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.SSL }));
	          _dettaglio.connettoreACA.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPaACA.auth.tipo }));
	          if(this.json.servizioPagoPaACA.auth.sslType) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.SSL_CFG_TYPE, value: this.json.servizioPagoPaACA.auth.sslType }));
	          }
	          if(this.json.servizioPagoPaACA.auth.tsType) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.TRUST_STORE_TYPE, value: this.json.servizioPagoPaACA.auth.tsType }));
	          }
	          if(this.json.servizioPagoPaACA.auth.tsLocation) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPaACA.auth.tsLocation }));
	          }
	          if(this.json.servizioPagoPaACA.auth.tsPassword) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPaACA.auth.tsPassword }));
	          }
	          if(this.json.servizioPagoPaACA.auth.ksType) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.KEY_STORE_TYPE, value: this.json.servizioPagoPaACA.auth.ksType }));
	          }
	          if(this.json.servizioPagoPaACA.auth.ksLocation) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPaACA.auth.ksLocation }));
	          }
	          if(this.json.servizioPagoPaACA.auth.ksPassword) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPaACA.auth.ksPassword }));
	          }
	          if(this.json.servizioPagoPaACA.auth.ksPKeyPasswd) {
	            _dettaglio.connettoreACA.push(new Dato({label: Voce.KEY_STORE_PWD_PRIVATE_KEY, value: this.json.servizioPagoPaACA.auth.ksPKeyPasswd }));
	          }
	        }
	      } else {
	        _dettaglio.connettoreACA.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
	      }
	    }

	if(this.json.servizioPagoPaGPD) {
	      _dettaglio.connettoreGPD.push(new Dato({ label: Voce.URL, value: this.json.servizioPagoPaGPD.url }));
	      if (this.json.servizioPagoPaGPD.subscriptionKey) {
	        _dettaglio.connettoreGPD.push(new Dato({ label: Voce.OCP_APIM_SUBSCRIPTION_KEY, value: this.json.servizioPagoPaGPD.subscriptionKey }));
	      }
	      if(this.json.servizioPagoPaGPD.auth) {
	        if(this.json.servizioPagoPaGPD.auth.clientId) {
	          _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.OAUTH2_CLIENT_CREDENTIALS }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID, value: this.json.servizioPagoPaGPD.auth.clientId }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET, value: this.json.servizioPagoPaGPD.auth.clientSecret }));
	          if(this.json.servizioPagoPaGPD.auth.scope) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_SCOPE, value: this.json.servizioPagoPaGPD.auth.scope }));
	          }
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT, value: this.json.servizioPagoPaGPD.auth.urlTokenEndpoint }));
	        }
	        if(this.json.servizioPagoPaGPD.auth.apiId) {
	          _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.API_KEY }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.API_ID, value: this.json.servizioPagoPaGPD.auth.apiId }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.API_KEY, value: this.json.servizioPagoPaGPD.auth.apiKey }));
	        }
	        if(this.json.servizioPagoPaGPD.auth.headerName) {
	          _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.HTTP_HEADER }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.HEADER_NAME, value: this.json.servizioPagoPaGPD.auth.headerName }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.HEADER_VALUE, value: this.json.servizioPagoPaGPD.auth.headerValue }));
	        }
	        if(this.json.servizioPagoPaGPD.auth.username) {
	          _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPaGPD.auth.username }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPaGPD.auth.password }));
	        }
	        if(this.json.servizioPagoPaGPD.auth.tipo) {
	          _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.SSL }));
	          _dettaglio.connettoreGPD.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPaGPD.auth.tipo }));
	          if(this.json.servizioPagoPaGPD.auth.sslType) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.SSL_CFG_TYPE, value: this.json.servizioPagoPaGPD.auth.sslType }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.tsType) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.TRUST_STORE_TYPE, value: this.json.servizioPagoPaGPD.auth.tsType }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.tsLocation) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPaGPD.auth.tsLocation }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.tsPassword) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPaGPD.auth.tsPassword }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.ksType) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.KEY_STORE_TYPE, value: this.json.servizioPagoPaGPD.auth.ksType }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.ksLocation) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPaGPD.auth.ksLocation }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.ksPassword) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPaGPD.auth.ksPassword }));
	          }
	          if(this.json.servizioPagoPaGPD.auth.ksPKeyPasswd) {
	            _dettaglio.connettoreGPD.push(new Dato({label: Voce.KEY_STORE_PWD_PRIVATE_KEY, value: this.json.servizioPagoPaGPD.auth.ksPKeyPasswd }));
	          }
	        }
	      } else {
	        _dettaglio.connettoreGPD.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
	      }
	    }

	if(this.json.servizioPagoPaFR) {
	      _dettaglio.connettoreFR.push(new Dato({ label: Voce.URL, value: this.json.servizioPagoPaFR.url }));
	      if (this.json.servizioPagoPaFR.subscriptionKey) {
	        _dettaglio.connettoreFR.push(new Dato({ label: Voce.OCP_APIM_SUBSCRIPTION_KEY, value: this.json.servizioPagoPaFR.subscriptionKey }));
	      }
	      if(this.json.servizioPagoPaFR.auth) {
	        if(this.json.servizioPagoPaFR.auth.clientId) {
	          _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.OAUTH2_CLIENT_CREDENTIALS }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID, value: this.json.servizioPagoPaFR.auth.clientId }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET, value: this.json.servizioPagoPaFR.auth.clientSecret }));
	          if(this.json.servizioPagoPaFR.auth.scope) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_SCOPE, value: this.json.servizioPagoPaFR.auth.scope }));
	          }
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT, value: this.json.servizioPagoPaFR.auth.urlTokenEndpoint }));
	        }
	        if(this.json.servizioPagoPaFR.auth.apiId) {
	          _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.API_KEY }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.API_ID, value: this.json.servizioPagoPaFR.auth.apiId }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.API_KEY, value: this.json.servizioPagoPaFR.auth.apiKey }));
	        }
	        if(this.json.servizioPagoPaFR.auth.headerName) {
	          _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.HTTP_HEADER }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.HEADER_NAME, value: this.json.servizioPagoPaFR.auth.headerName }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.HEADER_VALUE, value: this.json.servizioPagoPaFR.auth.headerValue }));
	        }
	        if(this.json.servizioPagoPaFR.auth.username) {
	          _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.BASIC }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.USERNAME, value: this.json.servizioPagoPaFR.auth.username }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.PASSWORD, value: this.json.servizioPagoPaFR.auth.password }));
	        }
	        if(this.json.servizioPagoPaFR.auth.tipo) {
	          _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.SSL }));
	          _dettaglio.connettoreFR.push(new Dato({label: Voce.TIPO, value: this.json.servizioPagoPaFR.auth.tipo }));
	          if(this.json.servizioPagoPaFR.auth.sslType) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.SSL_CFG_TYPE, value: this.json.servizioPagoPaFR.auth.sslType }));
	          }
	          if(this.json.servizioPagoPaFR.auth.tsType) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.TRUST_STORE_TYPE, value: this.json.servizioPagoPaFR.auth.tsType }));
	          }
	          if(this.json.servizioPagoPaFR.auth.tsLocation) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.TRUST_STORE_LOC, value: this.json.servizioPagoPaFR.auth.tsLocation }));
	          }
	          if(this.json.servizioPagoPaFR.auth.tsPassword) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.TRUST_STORE_PWD, value: this.json.servizioPagoPaFR.auth.tsPassword }));
	          }
	          if(this.json.servizioPagoPaFR.auth.ksType) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.KEY_STORE_TYPE, value: this.json.servizioPagoPaFR.auth.ksType }));
	          }
	          if(this.json.servizioPagoPaFR.auth.ksLocation) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.KEY_STORE_LOC, value: this.json.servizioPagoPaFR.auth.ksLocation }));
	          }
	          if(this.json.servizioPagoPaFR.auth.ksPassword) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.KEY_STORE_PWD, value: this.json.servizioPagoPaFR.auth.ksPassword }));
	          }
	          if(this.json.servizioPagoPaFR.auth.ksPKeyPasswd) {
	            _dettaglio.connettoreFR.push(new Dato({label: Voce.KEY_STORE_PWD_PRIVATE_KEY, value: this.json.servizioPagoPaFR.auth.ksPKeyPasswd }));
	          }
	        }
	      } else {
	        _dettaglio.connettoreFR.push(new Dato({ label: Voce.TIPO_AUTH, value: Voce.NESSUNA }));
	      }
	    }

    this.elencoStazioni();
    this.informazioni = _dettaglio.informazioni.slice(0);
    this.connettoriSoap = _dettaglio.connettoriSoap.slice(0);
	this.connettoreRT = _dettaglio.connettoreRT.slice(0);
	this.connettoreACA = _dettaglio.connettoreACA.slice(0);
	this.connettoreGPD = _dettaglio.connettoreGPD.slice(0);
	this.connettoreFR = _dettaglio.connettoreFR.slice(0);
  }

  protected elencoStazioni() {
    this.stazioni = this.json.stazioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this.mapNewItem(item);
      return p;
    }, this);
  }

  /**
   * Map item Stazione
   * @param item
   * @returns {Standard}
   */
  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    let _st = Dato.arraysToDato(
      [ Voce.MODELLO_UNICO, Voce.ABILITATO ],
      [
        UtilService.defaultDisplay({ value: UtilService.MODELLO_UNICO_DA_VERSIONE[item.versione] }),
        UtilService.defaultDisplay({ value: UtilService.ABILITA[(item.abilitato).toString()] }) ],
      ', '
    );
    _std.titolo = new Dato({ label: Voce.ID_STAZIONE+': ', value: item.idStazione });
    _std.sottotitolo = _st;

    return  _std;
  }

  protected _iconClick(ref: any, event: any) {
    let _ivm = ref.getItemViewModel();
    switch(event.type) {
      case 'edit':
        this._addEditStazione(true, _ivm.jsonP);
      break;
      case 'delete':
        console.log('delete');
      break;
    }
  }

  protected _editRegistro(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica intermediario',
      templateName: UtilService.INTERMEDIARIO
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  protected _addEditStazione(mode: boolean = false, _viewModel?: any) {
    let _mb: ModalBehavior = new ModalBehavior();
    _mb.editMode = mode;
    _mb.info = {
      viewModel: _viewModel,
      parent: this,
      dialogTitle: (!mode)?'Nuova stazione':'Modifica stazione',
      templateName: UtilService.STAZIONE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let p = new Parameters();
      let json = mb.info.viewModel;
      switch(mb.info.templateName) {
        case UtilService.STAZIONE:
          p.jsonP = json;
          p.model = this.mapNewItem(json);
          if(!mb.editMode) {
            this.stazioni.push(p);
          } else {
            this.stazioni.map((item) => {
              if (item.jsonP.idStazione == json.idStazione) {
                Object.keys(p.jsonP).forEach((key) => {
                  item.jsonP[key]= p.jsonP[key];
                });
                item.model = p.model;
              }
              return item;
            });
          }
        break;
        case UtilService.INTERMEDIARIO:
          this.dettaglioRegistroIntermediari();
        break;
      }
    }
  }

  /**
   * Save Registro intermediari|Stazioni (Put to: /intermediari/{idIntermediario} or
   * /intermediari/{idIntermediario}/stazioni/{idStazione} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = UtilService.URL_REGISTRO_INTERMEDIARI;
    let json = JSON.parse(JSON.stringify(mb.info.viewModel));
    let templateName = mb.info.templateName;
    if(templateName == UtilService.INTERMEDIARIO) {
      //(mb.editMode)?json.stazioni = this.json.stazioni:null;
      delete json['idIntermediario'];
      _service += '/'+UtilService.EncodeURIComponent(mb.info.viewModel['idIntermediario']);
    }
    if(templateName == UtilService.STAZIONE) {
      _service += '/'+UtilService.EncodeURIComponent(this.json.idIntermediario);
      _service += UtilService.URL_STAZIONI+'/'+UtilService.EncodeURIComponent(mb.info.viewModel['idStazione']);
      delete json['idStazione'];
    }
    this.gps.saveData(_service, json).subscribe(
      () => {
        this.gps.updateSpinner(false);
        responseService.next(true);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.denominazione:null });
  }

  infoDetail(): any {
    return {};
  }
}
