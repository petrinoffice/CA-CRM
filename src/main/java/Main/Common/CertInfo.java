package Main.Common;

import Main.Client.MainClient;
import Main.Key.CheckSign;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class CertInfo {


    private ArrayList<CertInfo> listOfCerts = new ArrayList<>();
    private int id;
    private String certSN;
    private String subject;
    private String passport;
    private String SNILS;
    private String INN;
    private String OGRN;
    private Boolean verifySign;

    public CertInfo(int id, String certSN, String subject, String pasport, String SNILS, String INN) {
        this.id = id;
        this.certSN = certSN;
        this.subject = subject;
        this.passport = pasport;
        this.SNILS = SNILS;
        this.INN = INN;
    }

    public CertInfo(String certSN, String subject, String pasport, String SNILS) {
        this.certSN = certSN;
        this.subject = subject;
        this.passport = pasport;
        this.SNILS = SNILS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCertSN() {
        return certSN;
    }

    public void setCertSN(String certSN) {
        this.certSN = certSN;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getSNILS() {
        return SNILS;
    }

    public void setSNILS(String SNILS) {
        this.SNILS = SNILS;
    }

    public Boolean getVerifySign() {
        return verifySign;
    }

    public void setVerifySign(Boolean verifySign) {
        this.verifySign = verifySign;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getOGRN() {
        return OGRN;
    }

    public void setOGRN(String OGRN) {
        this.OGRN = OGRN;
    }

}
