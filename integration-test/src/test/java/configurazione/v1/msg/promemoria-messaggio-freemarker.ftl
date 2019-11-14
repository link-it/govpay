Gentile ${versamento.getAnagraficaDebitore().getRagioneSociale()},

le notifichiamo che è stata emessa una richiesta di pagamento a suo carico: ${versamento.getCausaleVersamento().getSimple()}

<#if versamento.getNumeroAvviso()?has_content>
Può effettuare il pagamento tramite l'app mobile IO oppure presso uno dei prestatori di servizi di pagamento aderenti al circuito pagoPA utilizzando l'avviso di pagamento allegato.
<#else>
Puo' effettuare il pagamento on-line presso il portale dell'ente creditore: ${dominio.getRagioneSociale()} 
</#if>

Distinti saluti.