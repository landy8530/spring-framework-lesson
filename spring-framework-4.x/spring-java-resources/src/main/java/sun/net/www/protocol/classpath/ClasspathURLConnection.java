package sun.net.www.protocol.classpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Landy on 2019/1/10.
 */
public class ClasspathURLConnection extends URLConnection {

    public ClasspathURLConnection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {

    }
}
