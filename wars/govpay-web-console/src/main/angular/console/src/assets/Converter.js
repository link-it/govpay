(function (global) {

  /**
   * Papa config parameters
   * @private
   */
  const _config = {
    delimiter: "",	// auto-detect
    newline: "",	// auto-detect
    quoteChar: '"',
    escapeChar: '"',
    header: true,
    trimHeaders: false,
    dynamicTyping: false,
    preview: 0,
    encoding: "ISO-8859-1",
    worker: false,
    comments: false,
    step: undefined,
    download: false,
    skipEmptyLines: true,
    chunk: undefined,
    fastMode: undefined,
    beforeFirstChunk: undefined,
    withCredentials: undefined,
    transform: undefined
  };

  /**
   * Internal config log
   * @type {boolean}
   */
  var VERBOSE = false;

  /** Services **/
  /** Begin: Standard Method Converter **/
  var _Config_StandardMethod = {
    tassonomiaAvviso: [
      'Cartelle esattoriali',
      'Diritti e concessioni',
      'Imposte e tasse',
      'IMU',
      'TASI e altre tasse comunali',
      'Ingressi a mostre e musei',
      'Multe e sanzioni amministrative',
      'Previdenza e infortuni',
      'Servizi erogati dal comune',
      'Servizi erogati da altri enti',
      'Servizi scolastici',
      'Tassa automobilistica',
      'Ticket e prestazioni sanitarie',
      'Trasporti, mobilità e parcheggi'
    ],
    tracciato: {},
    parsedData: [],
    init: function() {
      this.parsedData = [];
      this.tracciato = {
        idTracciato: '',
        idDominio: '',
        inserimenti: [],
        annullamenti: []
      }
    }
  };

  /**
   * Standard Method Converter CSV-JSON
   * @param result
   * @returns {*}
   * @private
   */
  function _standard(result) {
    _Config_StandardMethod.init();
    _Config_StandardMethod.parsedData = result.data;
    if (VERBOSE) {
      console.log('PARSED', _Config_StandardMethod.parsedData);
    }
    try {
      if (_Config_StandardMethod.parsedData.length > 0) {
        $.each(_Config_StandardMethod.parsedData, function (index, riga) {
          if (VERBOSE) {
            console.log('RIGA', index, riga);
          }
          GestisciRiga(riga, index);
        });
        if (VERBOSE) {
          console.log('TRACCIATO', _Config_StandardMethod.tracciato);
        }
        return _Config_StandardMethod.tracciato;
      } else {
        return '';
      }
    } catch (e) {
      if (VERBOSE) {
        console.log('ERRORE', e);
      }
      return '';
    }
  }

  function GestisciRiga(riga, index) {
    if (CheckCampiTracciato(riga)) {
      if (VerificaCampiTracciato(riga, index)) {
        var campiPendenza = CheckCampiPendenza(riga);
        if (campiPendenza && riga.motivoAnnullamento.length) {
          if (VERBOSE) {
            console.log('campi pendenza e annullamento presenti contemporaneamente alla riga', index);
          }
        } else if (campiPendenza && !riga.motivoAnnullamento.length) {
          if (ValidaPendenza(riga)) {
            var pendenze = _Config_StandardMethod.tracciato.inserimenti.filter(
              function (pendenza) {
                return pendenza.idPendenza == riga.idPendenza;
              }
            );
            if (pendenze.length) {
              if (pendenze[0].voci.length < 5) {
                if (riga.voce_idVocePendenza.length && riga.voce_importo.length && riga.voce_descrizione.length) {
                  CreaVoce(riga, index);
                } else {
                  if (VERBOSE) {
                    console.log('CAMPI OBBLIGATORI VOCE MANCANTI ALLA RIGA', index);
                  }
                }
              } else {
                if (VERBOSE) {
                  console.log('numero massimo di voci raggiunto per la pendenza', riga.idPendenza, 'la riga', index, 'verrà ignorata');
                }
              }
            } else {
              CreaPendenza(riga, index);
            }
          } else {
            if (VERBOSE) {
              console.log('dati pendenza mancanti alla riga', index);
            }
          }
        } else if (!campiPendenza && riga.motivoAnnullamento.length) {
          CreaAnnullamento(riga);
        } else {
          if (VERBOSE) {
            console.log('nessun campo presente per pendenza o annullamento');
          }
        }
      } else {
        if (VERBOSE) {
          console.log('incongruenze nei campi tracciato alla riga', index);
        }
      }
    } else {
      if (VERBOSE) {
        console.log('mancano i campi tracciato alla riga', index);
      }
    }
  }

  function CheckCampiTracciato(riga) {
    if (riga.idTracciato.length && riga.idDominio.length && riga.idA2A.length && riga.idPendenza.length) {
      return true
    } else {
      return false
    }
  }

  function VerificaCampiTracciato(riga, index) {
    var verificato = true;
    // controllo preliminare riga
    if (riga.idTracciato == '') {
      verificato = false;
      if (VERBOSE) {
        console.log('idTracciato non presente alla riga', index);
      }
    } else if (_Config_StandardMethod.tracciato.idTracciato != '') {
      if (_Config_StandardMethod.tracciato.idTracciato != riga.idTracciato) {
        verificato = false;
        if (VERBOSE) {
          console.log('idTracciato non corrispondente alla riga', index);
        }
      }
    } else {
      _Config_StandardMethod.tracciato.idTracciato = riga.idTracciato
    }

    if (riga.idDominio == '') {
      verificato = false;
      if (VERBOSE) {
        console.log('idDominio non presente alla riga', index);
      }
    } else if (_Config_StandardMethod.tracciato.idDominio != '') {
      if (_Config_StandardMethod.tracciato.idDominio != riga.idDominio) {
        verificato = false;
        if (VERBOSE) {
          console.log('idDominio non corrispondente alla riga', index);
        }
      }
    } else {
      _Config_StandardMethod.tracciato.idDominio = riga.idDominio
    }

    return verificato;
  }

  function CheckCampiPendenza(riga, index) {
    if (riga.causale.length ||
      riga.soggetto_tipo.length ||
      riga.soggetto_identificativo.length ||
      riga.soggetto_anagrafica.length ||
      riga.soggetto_indirizzo.length ||
      riga.soggetto_civico.length ||
      riga.soggetto_cap.length ||
      riga.soggetto_localita.length ||
      riga.soggetto_provincia.length ||
      riga.soggetto_nazione.length ||
      riga.soggetto_email.length ||
      riga.soggetto_cellulare.length ||
      riga.importo.length ||
      riga.dataValidita.length ||
      riga.dataScadenza.length ||
      riga.tassonomia.length ||
      riga.tassonomiaAvviso.length ||
      riga.voce_indice.length ||
      riga.voce_idVocePendenza.length ||
      riga.voce_importo.length ||
      riga.voce_descrizione.length ||
      riga.voce_datiAllegati.length ||
      riga.voce_codEntrata.length ||
      riga.voce_ibanAccredito.length ||
      riga.voce_ibanAppoggio.length ||
      riga.voce_tipoContabilita.length ||
      riga.voce_tipoBollo.length ||
      riga.voce_hashDocumento.length ||
      riga.voce_provinciaResidenza.length
    ) {
      return true;
    } else {
      return false;
    }
  }

  function ValidaPendenza(riga) {
    if (riga.causale.length && riga.importo.length) {
      return true;
    } else {
      return false;
    }
  }

  function CreaPendenza(riga, index) {
    const pendenza = {};

    pendenza.idA2A = riga.idA2A;
    pendenza.idPendenza = riga.idPendenza;
    pendenza.causale = riga.causale;
    pendenza.importo = FormattaValuta(riga.importo);
    pendenza.soggettoPagatore = {};
    pendenza.voci = [];

    // CAMPI OPZIONALI PENDENZA
    if (riga.idUnitaOperativa != '') {
      pendenza.idUnitaOperativa = riga.idUnitaOperativa;
    }

    if (riga.nome != '') {
      pendenza.nome = riga.nome;
    }

    if (riga.numeroAvviso != '') {
      pendenza.numeroAvviso = riga.numeroAvviso;
    }

    if (riga.dataCaricamento != '') {
      pendenza.dataCaricamento = FormattaData(riga.dataCaricamento);
    }

    if (riga.dataValidita != '') {
      pendenza.dataValidita = FormattaData(riga.dataValidita);
    }

    if (riga.dataScadenza != '') {
      pendenza.dataScadenza = FormattaData(riga.dataScadenza);
    }

    if (riga.annoRiferimento != '') {
      pendenza.annoRiferimento = riga.annoRiferimento;
    }

    if (riga.cartellaPagamento != '') {
      pendenza.cartellaPagamento = riga.cartellaPagamento;
    }

    if (riga.datiAllegati != '') {
      pendenza.datiAllegati = riga.datiAllegati;
    }

    if (riga.tassonomia != '') {
      pendenza.tassonomia = riga.tassonomia;
    }

    if (riga.tassonomiaAvviso != '') {
      if (_Config_StandardMethod.tassonomiaAvviso.indexOf(riga.tassonomiaAvviso) != -1) {
        pendenza.tassonomiaAvviso = riga.tassonomiaAvviso;
      } else {
        if (VERBOSE) {
          console.log('tassonomiaAvviso ERRATA ALLA RIGA', index)
        }
      }
    }

    // controllo soggettoPagatore
    if (riga.soggetto_tipo != '' && riga.soggetto_identificativo != '') {
      if (riga.soggetto_tipo == 'F' || riga.soggetto_tipo == 'G') {
        pendenza.soggettoPagatore.tipo = riga.soggetto_tipo;
      } else {
        if (VERBOSE) {
          console.log('TIPO SOGGETTO PAGATORE NON CORRETTO ALLA RIGA', index)
        }
      }
      pendenza.soggettoPagatore.identificativo = riga.soggetto_identificativo;
    } else {
      if (VERBOSE) {
        console.log('DATI soggettoPagatore MANCANTI ALLA RIGA', index)
      }
    }

    // CAMPI OPZIONALI SOGGETTO PAGATORE
    if (riga.soggetto_anagrafica != '') {
      pendenza.soggettoPagatore.anagrafica = riga.soggetto_anagrafica;
    }

    if (riga.soggetto_indirizzo != '') {
      pendenza.soggettoPagatore.indirizzo = riga.soggetto_indirizzo;
    }

    if (riga.soggetto_civico != '') {
      pendenza.soggettoPagatore.civico = riga.soggetto_civico;
    }

    if (riga.soggetto_cap != '') {
      pendenza.soggettoPagatore.cap = riga.soggetto_cap;
    }

    if (riga.soggetto_localita != '') {
      pendenza.soggettoPagatore.localita = riga.soggetto_localita;
    }

    if (riga.soggetto_provincia != '') {
      pendenza.soggettoPagatore.provincia = riga.soggetto_provincia;
    }

    if (riga.soggetto_nazione != '') {
      pendenza.soggettoPagatore.nazione = riga.soggetto_nazione;
    }

    if (riga.soggetto_email != '') {
      pendenza.soggettoPagatore.email = riga.soggetto_email;
    }

    if (riga.soggetto_cellulare != '') {
      pendenza.soggettoPagatore.cellulare = riga.soggetto_cellulare;
    }

    if (riga.voce_idVocePendenza.length && riga.voce_importo.length && riga.voce_descrizione.length) {
      _Config_StandardMethod.tracciato.inserimenti.push(pendenza);
      CreaVoce(riga, index);
    } else {
      if (VERBOSE) {
        console.log('CAMPI OBBLIGATORI VOCE MANCANTI ALLA RIGA', index);
      }
    }

  }

  function CreaVoce(riga, index) {
    const voce = {
      idVocePendenza: riga.voce_idVocePendenza,
      importo: FormattaValuta(riga.voce_importo),
      descrizione: riga.voce_descrizione
    };

    // campi opzionali
    if (riga.voce_indice.length) {
      voce.indice = riga.voce_indice;
    }

    if (riga.voce_datiAllegati.length) {
      voce.datiAllegati = riga.voce_datiAllegati;
    }

    // campi condizionali
    if (CheckVoceCodEntrata(riga) && !CheckVoceCampiIban(riga) && !CheckVoceCampiBollo(riga)) {
      voce.codEntrata = riga.voce_codEntrata;
      AggiungiVocePendenza(riga, index, voce)
    } else if (CheckVoceCampiIban(riga) && !CheckVoceCodEntrata(riga) && !CheckVoceCampiBollo(riga)) {
      if (VerifyVoceCampiIban(riga)) {
        voce.ibanAccredito = riga.voce_ibanAccredito;
        voce.tipoCompatibilita = riga.voce_tipoContabilita;
        voce.codiceContabilita = riga.voce_tipoContabilita;
        if (riga.voce_ibanAppoggio.length) {
          voce.ibanAppoggio = riga.voce_ibanAppoggio
        }
        AggiungiVocePendenza(riga, index, voce)
      } else {
        if (VERBOSE) {
          console.log('CAMPI IBAN DELLA VOCE MANCANTI NELLA RIGA', index);
        }
      }
    } else if (CheckVoceCampiBollo(riga) && !CheckVoceCodEntrata(riga) && !CheckVoceCampiIban(riga)) {
      if (VerifyVoceCampiBollo(riga)) {
        voce.tipoBollo = riga.voce_tipoBollo;
        voce.hashDocumento = riga.voce_hashDocumento;
        voce.provinciaResidenza = riga.voce_provinciaResidenza;
        AggiungiVocePendenza(riga, index, voce)
      } else {
        if (VERBOSE) {
          console.log('CAMPI BOLLO DELLA VOCE MANCANTI NELLA RIGA', index);
        }
      }
    } else {
      if (VERBOSE) {
        console.log('CAMPI VOCE NON CONFORMI ALLA RIGA', index)
      }
    }
  }

  function CheckVoceCodEntrata(riga) {
    if (riga.voce_codEntrata.length) {
      return true;
    } else {
      return false;
    }
  }

  function CheckVoceCampiIban(riga) {
    if (riga.voce_ibanAccredito.length || riga.voce_tipoContabilita.length || riga.voce_codiceContabilita.length || riga.voce_ibanAppoggio.length) {
      return true;
    } else {
      return false;
    }
  }

  function VerifyVoceCampiIban(riga) {
    if (riga.voce_ibanAccredito.length && riga.voce_tipoContabilita.length && riga.voce_codiceContabilita.length) {
      return true;
    } else {
      return false;
    }
  }

  function CheckVoceCampiBollo(riga) {
    if (riga.voce_tipoBollo.length || riga.voce_hashDocumento.length || riga.voce_provinciaResidenza.length) {
      return true;
    } else {
      return false;
    }
  }

  function VerifyVoceCampiBollo(riga) {
    if (riga.voce_tipoBollo.length && riga.voce_hashDocumento.length && riga.voce_provinciaResidenza.length) {
      return true;
    } else {
      return false;
    }
  }

  function AggiungiVocePendenza(riga, index, voce) {
    $.each(_Config_StandardMethod.tracciato.inserimenti, function (index, value) {
      if (value.idPendenza == riga.idPendenza) {
        value.voci.push(voce);
        return;
      }
    })
  }

  function CreaAnnullamento(riga) {
    const annullamento = {
      idA2A: riga.idA2A,
      idPendenza: riga.idPendenza,
      motivoAnnullamento: riga.motivoAnnullamento
    };

    _Config_StandardMethod.tracciato.annullamenti.push(annullamento);
  }

  function FormattaValuta(valuta) {
    var valutaCorretta = valuta.replace(',', '.');
    return parseFloat(valutaCorretta).toFixed(2)
  }

  function FormattaData(data) {
    var dataSplittata = data.split('/');
    var dataRiordinata = dataSplittata[2] + '/' + dataSplittata[1] + '/' + dataSplittata[0];
    return new Date(dataRiordinata).toISOString().substring(0, 16)
  }
  /** End: Standard Method Converter **/


  /** Converter **/
  function _methods() {
    return [
    //{ id: #i, name: 'Method name', method: function, [ auto?: true ] }
      { id: 1, name: 'CSV v1', method: _standard, auto: true },
      { id: 2, name: 'Standard JSON', method: null }
    ];
  }

  /**
   * Set console log
   * @param value
   * @private
   */
  function _setLog(value) {
    VERBOSE = value;
  }

  global.Converter = {
    verbose: VERBOSE,
    setLog: _setLog,
    filename: 'File_'+ Date.now() +'.json',
    config: _config,
    methods: _methods
  };

})(this);
