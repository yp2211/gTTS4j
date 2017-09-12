import com.dragonbean.cloud.gTTS4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class gTTS4jTest {

    @Test
    public void gTTSTest() throws IOException {

        InputStream is = null;
        String text = "Surprise!";
        gTTS4j gtts = new gTTS4j();
        try {
            gtts.init(text, "en", true, false);
            is = gtts.exec();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) is.close();
        }

    }
}
