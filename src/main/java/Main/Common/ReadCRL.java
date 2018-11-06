package Main.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.util.ArrayList;

public class ReadCRL {
    private static ArrayList<String> listCRLFiles = new ArrayList();
    private static ArrayList<X509CRL> listCRL = new ArrayList();

    public ReadCRL(ArrayList<String> listCRLFiles) {
        this.listCRLFiles = listCRLFiles;

        for (String xxx : listCRLFiles) {
            listCRL.add(readCRLFile(xxx));
        }
    }

    public static ArrayList<X509CRL> getListCRL() {
        return listCRL;
    }

    public static X509CRL readCRLFile(String fileName) {

        File file = new File(fileName);

        FileInputStream in = null;
        try {
            if (file.exists() && file.length() > 0) {
                in = new FileInputStream(file);
            } else {
            }
            X509CRL x509crl = null;
            if (in != null) {
                x509crl = (X509CRL) CertificateFactory.getInstance("X.509").generateCRL(in);
            }
            return x509crl;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CRLException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    public static X509CRL readCRLFile(File file) {

        FileInputStream in = null;
        try {
            if (file.exists() && file.length() > 0) {
                in = new FileInputStream(file);
            } else {
            }
            X509CRL x509crl = null;
            if (in != null) {
                x509crl = (X509CRL) CertificateFactory.getInstance("X.509").generateCRL(in);
            }
            return x509crl;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CRLException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }
}


