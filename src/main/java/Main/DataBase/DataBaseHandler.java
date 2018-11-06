package Main.DataBase;

import Main.Common.CertInfo;
import Main.Common.CertInfoWork;
import Main.Constants.ConctantsSQL;

import java.sql.*;

public class DataBaseHandler {
    private static Connection connection;
    private static Statement statement;

    public DataBaseHandler() {
        try {
            connect();
            //getSQLCetrInfo();
            //getSign();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uzicrm?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", "root", "Q12345678q");
        statement = connection.createStatement();
    }

    public static void getSQLCetrInfo() {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("SELECT * FROM " + ConctantsSQL.CERTINFO + "");

            if (rs.next()) {
                do {
                    System.out.println(
                                    "ID: " + rs.getString(1) +
                                    " certSN " + rs.getString(2) +
                                    " subject " + rs.getString(3) +
                                    " Pasport " + rs.getString(4) +
                                    " SNILS " + rs.getString(5));

                    CertInfoWork.addCert(new CertInfo(
                            Integer.parseInt(rs.getString(1)),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(7)));
                } while (rs.next());
            } else {
                System.out.println("По запросу ничего не найдено. Empty :(");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCert(CertInfo certInfo){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `uzicrm`.`crmcertdate` (`CRMCertcertSN`, `CRMCertSubject`, `CRMCertPassport`, `CRMCertSNILS`, `CRMCetrSign`) VALUES ( ?, ?, ?, ?, ?);");
            ps.setString(1,certInfo.getCertSN());
            ps.setString(2,certInfo.getSubject());
            ps.setString(3,certInfo.getPassport());
            ps.setString(4,certInfo.getSNILS());
            ps.setBytes(5, CertInfoWork.getSignCertinfo(certInfo));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getSign(int id){
        try {
        ResultSet rs = statement.executeQuery("SELECT CRMCetrSign FROM `uzicrm`.`crmcertdate` WHERE idCertDate = " + id +" LIMIT 1;");

            if (rs.next()) {
                return rs.getBytes(1);
            } else {
                System.out.println("По запросу ничего не найдено. Empty :(");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
