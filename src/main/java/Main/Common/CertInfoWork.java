package Main.Common;

import Main.Client.MainClient;
import Main.Key.CheckSign;
import Main.Key.SignData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CertInfoWork {
    private CertInfoWork certInfoDAO;
    private static ArrayList<CertInfo> listCerts = new ArrayList<>();


    public CertInfoWork() {
        this.certInfoDAO = this;
        System.out.println(this.certInfoDAO);
    }

    public static void addCert(CertInfo certInfo){
        listCerts.add(certInfo);
    }

    public ObservableList<CertInfo> getCertInfo(){
        listCerts.clear();
        MainClient.getDataBaseHandler().getSQLCetrInfo();

        for (CertInfo cert: listCerts) {
            CheckSign checkSign = new CheckSign();
            try {
                byte[] sign = MainClient.getDataBaseHandler().getSign(cert.getId());
                if (sign != null) cert.setVerifySign(checkSign.CMSVerifyEx(sign, null));
                else cert.setVerifySign(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        ObservableList<CertInfo> list = FXCollections.observableArrayList(listCerts);
        return list;
    }

    public static byte[] getSignCertinfo(CertInfo certInfo){
        //Добавить много поточность в подписании
        SignData signData = new SignData();

        try {
        String jsonCertInfo = CertInfoToJSON(certInfo);
        System.out.println(jsonCertInfo);
        byte[] signJson = new byte[0];
        signJson = signData.CMSSignEx(jsonCertInfo.getBytes());
        return signJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String CertInfoToJSON(CertInfo certInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(certInfo);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkSigm(CertInfo certInfo){

        return false;
    }
}
