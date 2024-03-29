(function (global) {

  global.GovRiconciliazioniConfig = {
    quoteOrder: ['titolo', 'tipologia', 'categoria', 'capitolo', 'articolo', 'accertamento', 'annoEsercizio', 'importo'],
    quoteLabel: {
      capitolo: 'Capitolo',
      annoEsercizio: 'Anno esercizio',
      importo: 'Importo',
      titolo: 'Titolo',
      accertamento: 'Accertamento',
      tipologia: 'Tipologia',
      categoria: 'Categoria',
      articolo: 'Articolo',
      proprietaCustom: 'Proprieta custom'
    },
    quoteHidden: ['proprietaCustom'],
    showEmpty: false,
    quoteExport: ['titolo', 'tipologia', 'categoria', 'capitolo', 'articolo', 'accertamento', 'annoEsercizio', 'importo'],
    quoteCount: 10,
    exportLabel: {
      idDominio: 'Dominio',
      idFlusso: 'Id Flusso',
      iuv: 'IUV',
      importo: 'Importo',
      data: 'Data',
      idPendenza: 'Id Pendenze',
      tipoPendenza: 'Tipo pendenza',
      idVocePendenza: 'Id voce pendenza',
      datiAllegatiPendenza: 'Dati allegati pendenza',
      datiAllegatiVocePendenza: 'Dati allegati voce pendenza'
    },
  };

  global.GovFiltersConfig = {
    pagamenti: {
      dataSub: { value: 7, type: 'days' }
    },
    pendenze: {
      dataSub: { value: 7, type: 'days' }
    },
    incassi: {
      dataSub: { value: 7, type: 'days' }
    },
    giornale_eventi: {
      dataSub: { value: 1, type: 'days' }
    },
    rendicontazioni: {
      dataSub: { value: 7, type: 'days' }
    },
    riscossioni: {
      dataSub: { value: 7, type: 'days' }
    },
    ricevute: {
      dataSub: { value: 7, type: 'days' }
    }
  };

})(window);
