package it.govpay.core.utils.tracciati;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Tracciato;

import java.util.ArrayList;
import java.util.List;

public class TimerCaricamentoTracciatoSmistatore {

	private BasicBD singleConnectionBD;
	
	public TimerCaricamentoTracciatoSmistatore(BasicBD bd) {
		this.singleConnectionBD = bd;
	}
	
	public TimerCaricamentoTracciatoSmistatore() {
	}
	
	public int smista(List<List<byte[]>> lst, Tracciato tracciato, long lineaIniziale) throws Exception {
		List<BasicBD> bdList = new ArrayList<BasicBD>();
		
		List<TimerCaricamentoTracciatoCore> threadList = new ArrayList<TimerCaricamentoTracciatoCore>();
		try {
			for(List<byte[]> lstWrapper: lst) {

				TimerCaricamentoTracciatoCore timer = new TimerCaricamentoTracciatoCore();

				BasicBD newInstance = null;
				if(this.singleConnectionBD == null) {
					newInstance = BasicBD.newInstance("");
					newInstance.setAutoCommit(false);
					bdList.add(newInstance);
					timer.setBd(newInstance);
				} else {
					timer.setBd(this.singleConnectionBD);
				}
				timer.setLineaLst(lstWrapper);
				timer.setTracciato(tracciato);
				timer.setLineaIniziale(lineaIniziale);
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

//        ExecutorService executor = null;
        try {
//	        executor = Executors.newFixedThreadPool(lst.size());
	
	
	
	        for(TimerCaricamentoTracciatoCore thread: threadList) {
	        	returnLst.add(thread.call());
	        }

	        if(this.singleConnectionBD != null) {
				try{
					this.singleConnectionBD.commit();
				} catch(Exception e1) {}
	        	
	        } else {
	        	for(BasicBD bd: bdList) {
					try{
						bd.commit();
					} catch(Exception e1) {}
				}
	        }

        } catch(Exception e) {
	        if(this.singleConnectionBD != null) {
				try{
					this.singleConnectionBD.rollback();
				} catch(Exception e1) {}
	        } else {
	        	for(BasicBD bd: bdList) {
					try{
						bd.rollback();
					} catch(Exception e1) {}
				}
	        }
        	throw new Exception("Eccezione durante l'esecuzione dei job: " + e.getMessage(), e);
        } finally {
        	for(BasicBD bd: bdList) {
				try{
					bd.closeConnection();
				} catch(Exception e1) {}
			}
        	
//        	if(executor != null)
//        		executor.shutdown();
        }	        
        
        
        int returned = 0;
        for(Integer returnL: returnLst) {
        	returned += returnL; 
        }
        
        return returned;
		
	}

}
