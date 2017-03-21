import org.junit.Test;
import util.IPWebServiceHelper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 17.21.3.
 */
public class IPWebServiceHelperTest {

    @Test
    public void testDetermineCountryByIP() throws IOException {
        assertEquals("DE", IPWebServiceHelper.getCountry("37.201.240.221"));
    }

    @Test
    public void testDetermineEmptyCountryByIP() throws IOException {
        assertEquals("LV", IPWebServiceHelper.getCountry("127.0.0.1"));
    }

}
