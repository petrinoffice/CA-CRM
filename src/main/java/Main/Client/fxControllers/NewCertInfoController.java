package Main.Client.fxControllers;

import Main.Client.MainClient;
import Main.Common.CertInfo;
import Main.Common.ReadCRL;
import Main.Common.ReadCertImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AVA;
import sun.security.x509.X500Name;
import ru.CryptoPro.reprov.x509.*;

import java.io.*;
import java.math.BigInteger;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateParsingException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
//import java.security.cert.X509Certificate;
import java.util.*;

public class NewCertInfoController {

    @FXML
    Button openButton;

    @FXML
    TextField newSN;

    @FXML
    TextField newSybject;

    @FXML
    TextField newPassport;

    @FXML
    TextField newSNILS;

    @FXML
    AnchorPane newCert;

    @FXML
    TextField newDateStart;

    @FXML
    TextField newDateEnd;

    @FXML
    TextField newINN;

    @FXML
    TextField newOGRN;

    public void loadFile(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("Cert file", "*.req", "*.cer", "*.crt"),
                new FileChooser.ExtensionFilter("CRL file", "*.crl"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
//                new FileChooser.ExtensionFilter("Web pages", "*.tpl", "*.html", "*.htm"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        String fileExtension = file.getName().replaceAll("^.*\\.(.*)$", "$1");
        System.out.println(fileExtension);
        switch (fileExtension) {
            case "cer":
                x509CER(file);
                break;
            case "req":
                System.out.println("написать обработку");
                break;
            case "crl":
                x509CRL(file);
                break;
            default:
                System.out.println("написать обработку ошибок");
                break;
        }

    }

    public void newSaveCert(ActionEvent actionEvent) {
        MainClient.getDataBaseHandler().saveCert(new CertInfo(newSN.getText(), newSybject.getText(), newPassport.getText(), newSNILS.getText()));
        newCert.getScene().getWindow().hide();
    }

    public void canceled(ActionEvent actionEvent) {
        newCert.getScene().getWindow().hide();
    }

    public void x509CER(File file) {
        X509CertImpl c = ReadCertImpl.readCERFile(file);
        newSybject.setText(c.getSubjectDN().toString());
        System.out.println(c.getSubjectDN().toString());

        HashMap<String,String> d = new HashMap<>();
        String[] DN = c.getSubjectDN().toString().split(", ");
        for (int i = 0; i < DN.length ; i++) {
            String[] name = DN[i].split("=");
     //       System.out.println(Arrays.toString(name));
            if (name.length > 1) d.put(name[0],name[1]);
        }

        newSN.setText(c.getSerialNumber().toString(16));
        newDateStart.setText(c.getNotBefore().toString());
        newDateEnd.setText(c.getNotAfter().toString());
        newSybject.setText(d.get("CN"));
        newSNILS.setText(d.get("СНИЛС"));
        newINN.setText(d.get("INN"));
        newOGRN.setText(d.get("ОГРН"));


//        for (Map.Entry<String,String> x: d.entrySet()) {
//            System.out.println(x.getKey() +" WWWWWW "+ x.getValue());
//        }

//
//        System.out.println(c.getSubjectDN().toString());
//        try {
//        X509CertImpl certificate = null;
//
//            certificate = new X509CertImpl(new FileInputStream(file));
//            System.out.println(certificate.getSubjectDN().toString());
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
/////////////////////////////////////////////////////////////////////////
//        String s = certificate.getSubjectDN().toString();
//        newSybject.setText(s);
        //newSybject.setText(certificate.getSubjectDN().toString());
        //newDateEnd.setText(certificate.getNotAfter().toString());
//        try {
//            X509Certificate certificate = ReadCER.readCERFile(file);
//            newSN.setText(certificate.getSerialNumber().toString(16));
//            newSybject.setText(X500Name.asX500Name(certificate.getSubjectX500Principal()).getCommonName());
//
//            ru.CryptoPro.reprov.x509.X509CertImpl cert = new ru.CryptoPro.reprov.x509.X509CertImpl(new FileInputStream(file));
//            System.out.println(cert.getSubjectDN());
//
//
//            for (AVA xxx: X500Name.asX500Name(certificate.getSubjectX500Principal()).allAvas()) {
//
//                ru.CryptoPro.reprov.x509.AVA test = new ru.CryptoPro.reprov.x509.AVA(
//                        new ru.CryptoPro.reprov.array.ObjectIdentifier(xxx.getObjectIdentifier().toString()),
//                        new ru.CryptoPro.reprov.array.d(xxx.getDerValue().getData().getGeneralString().getBytes()));
//
//                System.out.println(test.getOID());
//
//                System.out.print(xxx.getObjectIdentifier() + "  ");
//                System.out.println(xxx.getDerValue());
//
//                if(xxx.getObjectIdentifier().toString().equals("1.2.643.3.141.1.1")) {
//                    System.out.print(xxx.getObjectIdentifier() + "  ");
//                    System.out.println(xxx.getDerValue().tag);
//                   // System.out.println(xxx.getDerValue().data.);
//                    DerInputStream derInputStream = xxx.getDerValue().data;
//                    //System.out.println(derInputStream.getSequence(0));
//
//                }
//            }
//            newDateStart.setText(certificate.getNotBefore().toString());
//            newDateEnd.setText(certificate.getNotAfter().toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }

    }

    public void x509CRL(File file) {
        X509CRL x509CRL = ReadCRL.readCRLFile(file);
        newSN.setText(x509CRL.getSigAlgName());
        newSybject.setText(x509CRL.getSigAlgOID());
        newPassport.setText(x509CRL.getNextUpdate().toString());
        System.out.println(x509CRL.getNextUpdate());
    }
}
