package Main.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.*;

public class ReadCER {

    public static X509Certificate readCERFile(File file) {
        FileInputStream in = null;
        try {
            if (file.exists() && file.length() > 0) {
                in = new FileInputStream(file);
            }

            X509Certificate certificate = null;
            if (in != null) {
                certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
            }
            return certificate;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                e.printStackTrace();
                }
            }
        }
        return null;
    }
}
