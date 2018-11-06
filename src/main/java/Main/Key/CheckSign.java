package Main.Key;

import Main.Constants.ConstantsKey;
import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.params.OID;
import userSamples.Constants;

import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Logger;

import static CMS_samples.DetachedSignatureFileExample.alias;

public class CheckSign {
    public static final String STR_CMS_OID_SIGNED = "1.2.840.113549.1.7.2";
    public static final String STR_CMS_OID_DATA = "1.2.840.113549.1.7.1";
    public static Logger logger = Logger.getLogger("LOG");

    public static boolean CMSVerifyEx(byte[] buffer, byte[] data) throws Exception {

        //System.out.println("123123131312333333131313           "+ new String(buffer));

        String digestOidValue = JCP.GOST_DIGEST_OID;
        String signAlg = JCP.GOST_EL_SIGN_NAME;
        String providerName = JCP.PROVIDER_NAME;
        KeyStore hdImageStore = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
        hdImageStore.load(null,null);
        final Certificate cert = hdImageStore.getCertificate(ConstantsKey.CONT_NAME_A_2001);

        int i;
        final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(buffer);
        final ContentInfo all = new ContentInfo();
        all.decode(asnBuf);

        if (!new OID(STR_CMS_OID_SIGNED).eq(all.contentType.value)) {
            throw new Exception("Not supported");
        } // if

        final SignedData cms = (SignedData) all.content;
        if (cms.version.value != 1) {
            throw new Exception("Incorrect version");
        } // if

        if (!new OID(STR_CMS_OID_DATA).eq(
                cms.encapContentInfo.eContentType.value)) {
            throw new Exception("Nested not supported");
        } // if

        byte[] text = null;
        if (data != null) {
            text = data;
        } // if
        else if (cms.encapContentInfo.eContent != null) {
            text = cms.encapContentInfo.eContent.value;
        } // else

        if (text == null) {
            throw new Exception("No content");
        } // if

        OID digestOid = null;
        DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(
                new OID(digestOidValue).value);

        for (i = 0; i < cms.digestAlgorithms.elements.length; i++) {
            if (cms.digestAlgorithms.elements[i].algorithm.equals(a.algorithm)) {
                digestOid = new OID(cms.digestAlgorithms.elements[i].algorithm.value);
                break;
            } // if
        } // for

        if (digestOid == null) {
            throw new Exception("Unknown digest");
        } // if

        int pos = -1;

        if (cms.certificates != null) {

            for (i = 0; i < cms.certificates.elements.length; i++) {

                final Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
                cms.certificates.elements[i].encode(encBuf);
                final byte[] in = encBuf.getMsgCopy();

                if (Arrays.equals(in, cert.getEncoded())) {
                    System.out.println("Certificate: " + ((X509Certificate)cert).getSubjectDN());
                    pos = i;
                    break;
                } // if

            } // for

            if (pos == -1) {
                throw new Exception("Not signed on certificate.");
            } // if

        }
        else if (cert == null) {
            throw new Exception("No certificate found.");
        } // else
        else {
            // Если задан {@link #cert}, то пробуем проверить
            // первую же подпись на нем.
            pos = 0;
        } // else

        final SignerInfo info = cms.signerInfos.elements[pos];
        if (info.version.value != 1) {
            throw new Exception("Incorrect version");
        } // if

        if (!digestOid.equals(new OID(info.digestAlgorithm.algorithm.value))) {
            throw new Exception("Not signed on certificate.");
        } // if

        final byte[] sign = info.signature.value;

        // check
        final Signature signature = Signature.getInstance(signAlg, providerName);
        signature.initVerify(cert);
        signature.update(text);

        final boolean checkResult = signature.verify(sign);
        if (checkResult) {
            if (logger != null) {
                logger.info("Valid signature");
                return true;
            }
        } else {
            System.out.println("Invalid signature.");
            return false;
            //throw new Exception("Invalid signature.");
        } // else
    return false;
    }

}
