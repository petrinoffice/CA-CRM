package Main.Client.fxControllers;

import Main.Client.MainClient;
import Main.Common.CertInfo;
import Main.Common.CertInfoWork;
import Main.Common.WorkWithFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;


public class Controller {
    CertInfoWork certInfoDAO = new CertInfoWork();
    FileInputStream inputstream;
    Image imageTrue = new Image("/valid.jpg");
    Image imageFalse = new Image("/invalid.jpg");

    @FXML
    TableView<CertInfo> tableview = new TableView();

    TableColumn<CertInfo, Integer> columnID = new TableColumn<CertInfo, Integer>("ID");
    TableColumn<CertInfo, String> columnCertSN = new TableColumn<CertInfo, String>("Certificate SN");
    TableColumn<CertInfo, String> columnSubject = new TableColumn<CertInfo, String>("Certificate Subject");
    TableColumn<CertInfo, String> columnPasport = new TableColumn<CertInfo, String>("Certificate Pasport");
    TableColumn<CertInfo, String> columnSNILS = new TableColumn<CertInfo, String>("Certificate SNILS");
    TableColumn<CertInfo, Boolean> columnverifySign = new TableColumn<CertInfo, Boolean>("Certificate Verify");
    TableColumn<CertInfo, String> columnINN = new TableColumn<CertInfo, String>("INN");
    TableColumn<CertInfo, String> columnOGRN = new TableColumn<CertInfo, String>("OGRN");


    @FXML
    private void initialize() {
        setupTableColumn();
        editTableColumn();
        imageCheckSign();

        tableview.setItems(certInfoDAO.getCertInfo());
        tableview.getColumns().addAll(columnID, columnverifySign , columnCertSN, columnSubject, columnPasport, columnSNILS, columnOGRN, columnINN);
    }

    public void setupTableColumn() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnCertSN.setCellValueFactory(new PropertyValueFactory<>("certSN"));
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        columnPasport.setCellValueFactory(new PropertyValueFactory<>("passport"));
        columnSNILS.setCellValueFactory(new PropertyValueFactory<>("SNILS"));
        columnOGRN.setCellValueFactory(new PropertyValueFactory<>("OGRN"));
        columnINN.setCellValueFactory(new PropertyValueFactory<>("INN"));
        columnverifySign.setCellValueFactory(new PropertyValueFactory<>("verifySign"));
    }

    public void editTableColumn() {
        columnCertSN.setCellFactory(TextFieldTableCell.<CertInfo>forTableColumn());
        columnCertSN.setOnEditCommit((TableColumn.CellEditEvent<CertInfo, String> event) -> {
            TablePosition<CertInfo, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();

            int row = pos.getRow();
            CertInfo certInfo = event.getTableView().getItems().get(row);
            certInfo.setCertSN(newFullName);
        });
    }

    private void imageCheckSign(){
        columnverifySign.setStyle( "-fx-alignment: CENTER;");
        columnverifySign.setCellFactory(col -> new TableCell<CertInfo, Boolean>() {
            private final ImageView imageView = new ImageView();

            {
                // initialize ImageView + set as graphic
                imageView.setFitWidth(90);
                imageView.setFitHeight(20);
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                if (empty || item == null) { // no image for empty cells
                    imageView.setImage(null);
                } else {
                    imageView.setImage(item ? imageTrue : imageFalse);
                }
            }
        });
    }

    public void run(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            LoginController lc = (LoginController) loader.getController();
            lc.id = 100;
            stage.setTitle("JavaFX Autorization");
            stage.setScene(new Scene(root, 400, 200));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startEdit(TableColumn.CellEditEvent cellEditEvent) {
        System.out.println("23133");
    }

    public void refreshTable() {
        tableview.setItems(certInfoDAO.getCertInfo());
    }

    public void createNew(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newCertInfo.fxml"));
            loader.setLocation(getClass().getResource("/newCertInfo.fxml")) ;
            Parent root = loader.load();
            NewCertInfoController lc = (NewCertInfoController) loader.getController();
            stage.setTitle("JavaFX Autorization");
            stage.setScene(new Scene(root, 500, 200));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSignFile(ActionEvent actionEvent) {
        CertInfo certInfo = tableview.getSelectionModel().getSelectedItem();
        if (certInfo != null) {

            byte[] sign = MainClient.getDataBaseHandler().getSign(certInfo.getId());
            if (sign == null) {
                errorWindow("Подпись не обнаруженна!");
                return;
            }

            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(//
                    new FileChooser.ExtensionFilter("Sign Files", "*.sgn"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {

                WorkWithFile.SaveFile(sign, file);
            }
        } else {
            errorWindow("Выберите запись!");
        }
    }

    public static void errorWindow(String textError) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, textError, ButtonType.OK);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getText().equals("OK")) {
            System.out.println("You clicked OK");
        }
    }
}
