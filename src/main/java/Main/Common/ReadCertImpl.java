package Main.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadCertImpl {

    public static ru.CryptoPro.reprov.x509.X509CertImpl readCERFile(File file) {
        FileInputStream in = null;
        try {
            if (file.exists() && file.length() > 0) {
                in = new FileInputStream(file);
            }

            ru.CryptoPro.reprov.x509.X509CertImpl certificate = null;
            if (in != null) {
                certificate = new ru.CryptoPro.reprov.x509.X509CertImpl(in);
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
