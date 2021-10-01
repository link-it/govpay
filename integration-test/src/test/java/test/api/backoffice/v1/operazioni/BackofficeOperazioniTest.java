package test.api.backoffice.v1.operazioni;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.intuit.karate.FileUtils;
import com.intuit.karate.junit4.Karate;
import com.intuit.karate.netty.FeatureServer;

@RunWith(Karate.class)
public class BackofficeOperazioniTest {
	
	private static FeatureServer mockservice;
	
    @BeforeClass
    public static void beforeClass() {
        File file = FileUtils.getFileRelativeTo(BackofficeOperazioniTest.class, "../../../../../utils/mock-ente.feature");
        mockservice = FeatureServer.start(file, 8888, false, null);
    }
    
    @AfterClass
    public static void afterClass() {
    	mockservice.stop();
    }
}