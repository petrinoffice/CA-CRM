package Main;

import ru.CryptoPro.JCP.KeyStore.HDImage.HDImageStore;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.params.AlgIdSpec;
import ru.CryptoPro.JCP.params.CryptDhAllowedSpec;
import ru.CryptoPro.JCP.params.OID;
import ru.CryptoPro.JCPRequest.GostCertificateRequest;
import userSamples.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println(HDImageStore.getDir());

        try {
            Signature sig = Signature.getInstance("GOST3411withGOST3410EL");

            KeyStore hdImageStore = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
            hdImageStore.load(null,null);

            Enumeration<String> aliases = hdImageStore.aliases();
            while (aliases.hasMoreElements()){
                System.out.println(aliases.nextElement());
            }


            //Создаем подпись
            PrivateKey privateKey = (PrivateKey)hdImageStore.getKey("Cont_A","a".toCharArray());
            sig.initSign(privateKey);

            sig.update("test".getBytes());
            byte[] sign = sig.sign();
            System.out.println(Arrays.toString(sign));

            //Проверяем подпись
            Certificate cert = hdImageStore.getCertificate("Cont_A");
            PublicKey publicKey = cert.getPublicKey();
            sig.initVerify(publicKey);
            sig.update("test".getBytes());
            System.out.println(sig.verify(sign));


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }


    public static void checkGOST() {
        Provider[] p = Security.getProviders();

        for (Provider pp: p) {
            System.out.println("== "+pp.getName()+" ==");

            Set<Provider.Service> a = pp.getServices();

            for (Provider.Service aa: a) {
                System.out.println(aa.getClassName() + " " + aa.getAlgorithm());
            }
        }
    }
}
