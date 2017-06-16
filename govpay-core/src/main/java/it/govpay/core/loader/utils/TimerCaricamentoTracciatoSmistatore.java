package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Tracciato;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TimerCaricamentoTracciatoSmistatore {

	
	public int smista(List<List<byte[]>> lst, Tracciato tracciato) throws Exception {
		List<BasicBD> bdList = new ArrayList<BasicBD>();
		
		List<TimerCaricamentoTracciatoCore> threadList = new ArrayList<TimerCaricamentoTracciatoCore>();
		try {
			for(List<byte[]> lstWrapper: lst) {
				BasicBD newInstance = BasicBD.newInstance("");
				bdList.add(newInstance);
				TimerCaricamentoTracciatoCore timer = new TimerCaricamentoTracciatoCore();
				timer.setBd(newInstance);
				timer.setLineaLst(lstWrapper);
				timer.setTracciato(tracciato);
				threadList.add(timer);
			}
		} catch(Exception e) {
			for(BasicBD bd: bdList) {
				try{
					bd.closeConnection();
				} catch(Exception e1) {}
			}
			
			throw new Exception("Errore durante l'inizializzazione delle connessioni: " +e.getMessage(), e);
		}

        List<Integer> returnLst = new ArrayList<Integer>();        

        ExecutorService executor = null;
        try {
	        executor = Executors.newFixedThreadPool(lst.size());
	
	
	
	        List<Future<Integer>> lstFuture = new ArrayList<Future<Integer>>(); 
	        for(TimerCaricamentoTracciatoCore thread: threadList) {
	        	lstFuture.add(executor.submit(thread));
	        }
	
	        for(Future<Integer> future: lstFuture) {
	        	returnLst.add(future.get());
	        }

        	for(BasicBD bd: bdList) {
				try{
					bd.commit();
				} catch(Exception e1) {}
			}

        } catch(Exception e) {
        	for(BasicBD bd: bdList) {
				try{
					bd.rollback();
				} catch(Exception e1) {}
			}
        	throw new Exception("Eccezione durante l'esecuzione dei job: " + e.getMessage(), e);
        } finally {
        	for(BasicBD bd: bdList) {
				try{
					bd.closeConnection();
				} catch(Exception e1) {}
			}
        	
        	if(executor != null)
        		executor.shutdown();
        }	        
        
        
        int returned = 0;
        for(Integer returnL: returnLst) {
        	returned += returnL; 
        }
        
        return returned;
		
	}
}
