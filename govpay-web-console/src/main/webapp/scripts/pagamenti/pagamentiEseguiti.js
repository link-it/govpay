 (function() {
 'use strict';

 function getPagamentoIconaStato(item) {
  var stato = item.stato;

  if(stato == 'PAGAMENTO_ESEGUITO'){
    return 'done';
  } else if(stato == 'IN_ATTESA'){
    return 'hourglass-empty';
  } else if(stato == 'FALLITO'){
    return 'clear';
  } else if(stato == 'IN_CORSO'){
    return 'receipt';
  } else if(stato == 'AUTORIZZATO_DIFFERITO'){
    return 'perm-identity';
  } else if(stato == 'PAGAMENTO_NON_ESEGUITO'){
    return 'clear';
  } else if(stato == 'PAGAMENTO_PARZIALMENTE_ESEGUITO'){
      return 'star-half';
  }  else if(stato == 'DECORRENZA_TERMINI_PARZIALE'){
      return 'question-answer';
  }  else if(stato == 'DECORRENZA_TERMINI'){
      return 'clear';
  }  else if(stato == 'AUTORIZZATO_IMMEDIATO'){
      return 'done';
  }

  return '';
}

function getPagamentoColoreStato(item) {
  var stato = item.stato;

  if(stato == 'PAGAMENTO_ESEGUITO'){
    return 'green';
  } else if(stato == 'IN_ATTESA'){
    return 'orange';
  } else if(stato == 'FALLITO'){
    return 'red';
  } else if(stato == 'IN_CORSO'){
    return 'orange';
  } else if(stato == 'AUTORIZZATO_DIFFERITO'){
    return 'blue';
  } else if(stato == 'PAGAMENTO_NON_ESEGUITO'){
    return 'red';
  } else if(stato == 'PAGAMENTO_PARZIALMENTE_ESEGUITO'){
      return 'orange';
  }  else if(stato == 'DECORRENZA_TERMINI_PARZIALE'){
      return 'orange';
  }  else if(stato == 'DECORRENZA_TERMINI'){
      return 'red';
  }  else if(stato == 'AUTORIZZATO_IMMEDIATO'){
      return 'blue';
  }

  return '';
}

})();