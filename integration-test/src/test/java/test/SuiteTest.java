package test;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.intuit.karate.FileUtils;
import com.intuit.karate.junit4.Karate;
import com.intuit.karate.netty.FeatureServer;

@RunWith(Karate.class)
public class SuiteTest {
    @BeforeClass
    public static void beforeClass() {
    	
        File file = FileUtils.getFileRelativeTo(SuiteTest.class, "../utils/mock-ente.feature");
        FeatureServer.start(file, 8888, false, null);
    }
}