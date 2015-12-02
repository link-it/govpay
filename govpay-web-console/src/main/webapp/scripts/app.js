/*
Copyright (c) 2015 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/
 //var govpayUrl= 'http://localhost:8080/govpay';
  var govpayUrl= '../govpayConsole';
(function(document) {
  'use strict';

  // Grab a reference to our auto-binding template
  // and give it some initial binding values
  // Learn more about auto-binding templates at http://goo.gl/Dx1u2g
  var app = document.querySelector('#app');
  app.isFirstPage = true;
  app.isEditSection = false;
  app.govpayUrl = govpayUrl;
  app.govpayUrlOperatore = govpayUrl + '/rs/dars/operatori/user';
  app.govpayUrlCheckSession = '';// govpayUrl + '/logout';
  app.govpayUrlLogout = govpayUrl + '/logout?logout=true';
  app.profiloCorrente = null;

    app._visualizzaInformazioniProfilo  = function(event){
        app.profiloCorrente = event.detail.response.response;
        app.isEditSection = false;
    }

    app._visualizzaErroreGetProfilo  = function(event){
        app.isEditSection = false;
        var responseCode = event.detail.request.status;
        var responseStatus = event.detail.request.statusText;

        console.log('Errore get profilo: ' + responseCode);

        // Url servizi Rest errata
        if(responseCode == 404){
            var erroreCorrente = { event : event , messaggio : 'Servizi DARS non raggiungibili.'};
            document.querySelector('#errorPage').setErrore(erroreCorrente);
            app.route = 'error';
        } else if(responseCode == 401){
            var erroreCorrente = { event : event , messaggio : 'Non si dispone dei diritti necessari per visualizzare il contenuto richiesto.'};
            document.querySelector('#nonAutorizzatoPage').setErrore(erroreCorrente);
            app.route = 'nonAutorizzato';
          //  window.location = '/public/nonAutorizzato.html';
        }
    }

    app._esitoCheckSession  = function(event){
       var responseCode = event.detail.status;
       var responseStatus = event.detail.statusText;

       console.log('Check Session: ' + responseCode);
    }

    app._erroreCheckSession  = function(event){
        var responseCode = event.detail.status;
        var responseStatus = event.detail.statusText;

        console.log('Session Scaduta: ' + responseCode);
        // Url servizi Rest errata
        if(responseCode == 404){

        }
    }

  // controlla se sto navigando in una sezione di edit, restituisce true se devo nasconderla

    app._nascondiSezione = function(profiloCorrente,nomeSezione){
      if(nomeSezione == 'operazioni')
        return true;

     if(profiloCorrente){
      if(this._isSezioneAmministratore(nomeSezione)){
            var isAdmin = this._isAdmin(profiloCorrente);
            return !isAdmin;
        } else{
            return false;
            }
      }
      //se non posso decodificare il profilo lo nascondo
        else
            return true;
    };

  app._isSezioneAmministratore = function(posizione){
    if(posizione == 'intermediari' || posizione == 'domini' ||
               posizione == 'enti' || posizione == 'tributi' ||
                  posizione == 'applicazioni'  || posizione == 'operazioni' || posizione == 'anagraficaNdp'
              || posizione == 'rendicontazioni'    || posizione == 'anagraficaCreditore'){
        return true;
    } else {
        return false;
    }
  };

    app._isAdmin = function(profiloCorrente){
          var isAdmin = false;

            if(profiloCorrente.profilo == 'ADMIN')
          //for(var i = 0; i <listaRuoli.lenght; i++){
            //if(listaRuoli[i] == 'A')
                return true;
          //}

            return isAdmin;
        };


  app.displayInstalledToast = function() {
    // Check to make sure caching is actually enabled—it won't be in the dev environment.
    if (!document.querySelector('platinum-sw-cache').disabled) {
      document.querySelector('#caching-complete').show();
    }
  };

  // Listen for template bound event to know when bindings
  // have resolved and content has been stamped to the page
  app.addEventListener('dom-change', function() {
    console.log('Our app is ready to rock!');
  });

  // See https://github.com/Polymer/polymer/issues/1381
  window.addEventListener('WebComponentsReady', function() {
    // imports are loaded and elements have been registered
  });

  // Main area's paper-scroll-header-panel custom condensing transformation of
  // the appName in the middle-container and the bottom title in the bottom-container.
  // The appName is moved to top and shrunk on condensing. The bottom sub title
  // is shrunk to nothing on condensing.
  addEventListener('paper-header-transform', function(e) {
    var appName = document.querySelector('#mainToolbar .app-name');
    var middleContainer = document.querySelector('#mainToolbar .middle-container');
    var bottomContainer = document.querySelector('#mainToolbar .bottom-container');
    var detail = e.detail;
    var heightDiff = detail.height - detail.condensedHeight;
    var yRatio = Math.min(1, detail.y / heightDiff);
    var maxMiddleScale = 0.50;  // appName max size when condensed. The smaller the number the smaller the condensed size.
    var scaleMiddle = Math.max(maxMiddleScale, (heightDiff - detail.y) / (heightDiff / (1-maxMiddleScale))  + maxMiddleScale);
    var scaleBottom = 1 - yRatio;

    // Move/translate middleContainer
    Polymer.Base.transform('translate3d(0,' + yRatio * 100 + '%,0)', middleContainer);

    // Scale bottomContainer and bottom sub title to nothing and back
    Polymer.Base.transform('scale(' + scaleBottom + ') translateZ(0)', bottomContainer);

    // Scale middleContainer appName
    Polymer.Base.transform('scale(' + scaleMiddle + ') translateZ(0)', appName);
  });

  // Close drawer after menu item is selected if drawerPanel is narrow
  app.onDataRouteClick = function(event) {

    var drawerPanel = document.querySelector('#paperDrawerPanel');
    if (drawerPanel.narrow) {
      drawerPanel.closeDrawer();
    }
    var componente = event.target.attributes['comp'].value;

   // app.govpayUrlCheckSession = govpayUrl + '/logout';
   // app.$.govpayconnchecksession.generateRequest();


    document.querySelector('#'+componente).init();

     app.isFirstPage = true;
  };

  // Scroll page to top and expand header
  app.scrollPageToTop = function() {
    document.getElementById('mainContainer').scrollTop = 0;
  };

// Controlla se si tratta di una pagina di primo livello (lista) o dettaglio

  app._changedPage = function(event){
    app.isFirstPage = (event.detail===0);
    app.isEditSection = app._isEditSection();
  };


// controlla se sto navigando in una sezione di edit

  app._isEditSection = function(){
    var posizione = app.route;

    var pagina;
    if(posizione == 'intermediari' || posizione == 'domini' ||
          posizione == 'enti' || posizione == 'tributi' ||
            posizione == 'applicazioni'){
        return true;
    } else {
      return false;
    }
  };


// Chiamata quando si preme il tasto back nell'applicazione

  app._goBack = function(){
    var posizione = app.route;

    var pagina;
    if(posizione == 'intermediari'){
        pagina= document.querySelector('#lci');
    } else if(posizione == 'domini'){
        pagina= document.querySelector('#configurazioneDomini');
    }else if(posizione == 'psp'){
       pagina= document.querySelector('#configurazionePsp');
    }else if(posizione == 'tributi'){
        pagina= document.querySelector('#configurazioneTributi');
    }else if(posizione == 'enti'){
       pagina= document.querySelector('#configurazioneEnti');
    }else if(posizione == 'applicazioni'){
       pagina= document.querySelector('#configurazioneApplicazioni');
    }else if(posizione == 'operatori'){
       pagina= document.querySelector('#configurazioneOperatori');
    }else if(posizione == 'eventi'){
       pagina= document.querySelector('#listaEventi');
    }else if(posizione == 'rendicontazioni'){
       pagina= document.querySelector('#listaRendicontazioni');
    }  else if(posizione == 'operazioni'){
       pagina= document.querySelector('#listaOperazioni');
    }  else {
      pagina= document.querySelector('#listaPagamenti');
    }

    pagina.selectPrevPage();
  };

  app._nuovoElemento = function (){
    var posizione = app.route;

        var pagina;
        if(posizione == 'intermediari'){
            app._creaIntermediario();
        } else if(posizione == 'domini'){
            app._creaDominio();
        } else if(posizione == 'enti'){
          app._creaEnte();
        } else if(posizione == 'tributi'){
            app._creaTributo();
        } else if(posizione == 'applicazioni'){
          app._creaApplicazione();
        } else {
          // do nothing
        }
  };

    app._modificaElemento = function (){
      var posizione = app.route;

      var pagina;
      if(posizione == 'intermediari'){
          app._editaIntermediario();
      } else if(posizione == 'domini'){
          app._editaDominio();
      } else if(posizione == 'enti'){
          app._editaEnte();
      } else if(posizione == 'tributi'){
          app._editaTributo();
      } else if(posizione == 'applicazioni'){
          app._editaApplicazione();
      }  else {
        // do nothing
      }
    };

 app._reloadItem = function(event){
    var posizione = app.route;
    var elementoModificato = event.detail.elementoModificato;

    var pagina;
    if(posizione == 'intermediari'){
        pagina= document.querySelector('#lci');
         pagina._reloadIntermediario(elementoModificato);
    } else if(posizione == 'domini'){
        pagina= document.querySelector('#configurazioneDomini');
         pagina._reloadDominio(elementoModificato);
    }else if(posizione == 'psp'){
       pagina= document.querySelector('#configurazionePsp');
    }else if(posizione == 'tributi'){
        pagina= document.querySelector('#configurazioneTributi');
         pagina._reloadTributo(elementoModificato);
    }else if(posizione == 'enti'){
       pagina= document.querySelector('#configurazioneEnti');
        pagina._reloadEnte(elementoModificato);
    }else if(posizione == 'applicazioni'){
       pagina= document.querySelector('#configurazioneApplicazioni');
       pagina._reloadApplicazione(elementoModificato);
    }else if(posizione == 'operatori'){
       pagina= document.querySelector('#configurazioneOperatori');
    }else if(posizione == 'eventi'){
       pagina= document.querySelector('#listaEventi');
    }else if(posizione == 'rendicontazioni'){
       pagina= document.querySelector('#listaRendicontazioni');
    } else {
      pagina= document.querySelector('#listaPagamenti');
    }


  };

// imposta le variabili necessarie per la creazione di un nuovo intermediario

  app._creaIntermediario = function(){
    var lei = document.querySelector('#lei');
    lei.intermediarioCorrente = null;
    lei.open();
  };

// imposta i valori attuali di un intermediario nella form di edit.

  app._editaIntermediario = function(){
    var lei = document.querySelector('#lei');
    var lci = document.querySelector('#lci');
    lei.intermediarioCorrente = lci.dettaglioIntermediarioCorrente.response;
    lei.open();
  };

  // imposta le variabili necessarie per la creazione di un nuovo dominio

    app._creaDominio = function(){
      var editDominio = document.querySelector('#editDominio');
      editDominio.dominioCorrente = null;
      editDominio.open();
    };

  // imposta i valori attuali di un dominio nella form di edit.

    app._editaDominio = function(){
      var editDominio = document.querySelector('#editDominio');
      var configurazioneDomini = document.querySelector('#configurazioneDomini');
      editDominio.dominioCorrente = configurazioneDomini.dettaglioDominioCorrente.response;
      editDominio.open();
    };

  // imposta le variabili necessarie per la creazione di un nuovo ente

    app._creaEnte = function(){
      var editEnte = document.querySelector('#editEnte');
      editEnte.enteCorrente = null;
      editEnte.open();
    };

  // imposta i valori attuali di un ente nella form di edit.

    app._editaEnte = function(){
      var editEnte = document.querySelector('#editEnte');
      var configurazioneEnti = document.querySelector('#configurazioneEnti');
      editEnte.enteCorrente = configurazioneEnti.dettaglioEnteCorrente.response;
      editEnte.open();
    };

 // imposta le variabili necessarie per la creazione di un nuovo tributo

    app._creaTributo = function(){
      var editTributo = document.querySelector('#editTributo');
      editTributo.tributoCorrente = null;
      editTributo.open();
    };

  // imposta i valori attuali di un tributo nella form di edit.

    app._editaTributo = function(){
      var editTributo = document.querySelector('#editTributo');
      var configurazionetributi = document.querySelector('#configurazioneTributi');
      editTributo.tributoCorrente = configurazioneTributi.dettaglioTributoCorrente.response;
      editTributo.open();
    };

    // imposta le variabili necessarie per la creazione di una nuova applicazione

        app._creaApplicazione = function(){
          var editApplicazione = document.querySelector('#editApplicazione');
          editApplicazione.applicazioneCorrente = null;
          editApplicazione.open();
        };

      // imposta i valori attuali di una applicazione  nella form di edit.

        app._editaApplicazione = function(){
          var editApplicazione = document.querySelector('#editApplicazione');
          var configurazioneApplicazioni = document.querySelector('#configurazioneApplicazioni');
          editApplicazione.applicazioneCorrente = configurazioneApplicazioni.dettaglioApplicazioneCorrente.response;
          editApplicazione.open();
        };


})(document);
