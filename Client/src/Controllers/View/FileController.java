package Controllers.View;

import Models.View.FileModel;
import Modules.UserFileManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class FileController implements Initializable {
    @FXML
    JFXButton btnAddFile;

    UserFileManager userFiles;

    @FXML
    private JFXTreeTableView<FileModel> ttvFiles;

    public ObservableList<FileModel> fileModels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try { userFiles = new UserFileManager(fileModels); } catch (IOException e) { }
        userFiles.start();

        ttvFiles.getStyleClass().add("noheader");

        //Setting up columns
        JFXTreeTableColumn<FileModel, String> filename = new JFXTreeTableColumn<>("Filename");
        filename.setPrefWidth(400);
        filename.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<FileModel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<FileModel, String> param) {
                return param.getValue().getValue().filenameProperty();
            }
        });


        JFXTreeTableColumn<FileModel, String> fileSize = new JFXTreeTableColumn<>("FileSize");
        fileSize.setPrefWidth(400);
        fileSize.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<FileModel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<FileModel, String> param) {
                return param.getValue().getValue().sizeProperty();
            }
        });

        //Adding fileModels to observable array
        //fileModels.add(new FileModel("Test","1GB", "JohnDoe", "21/11/2018"));

        final TreeItem<FileModel> root = new RecursiveTreeItem<FileModel>(fileModels, RecursiveTreeObject::getChildren);
        ttvFiles.getColumns().setAll(filename, fileSize);
        ttvFiles.setRoot(root);
        ttvFiles.setShowRoot(false);
    }

    @FXML
    public void addFile(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the file to add to MegaPDFiles");
        final File file = fileChooser.showOpenDialog((Stage) ttvFiles.getScene().getWindow());
        if (file != null) {
            File destFile = new File(userFiles.getFilesFolderPath() + "/" + file.toPath().getFileName());
            String originalName = destFile.getName();
            try {
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void removeFile(ActionEvent event) {
        FileModel selectedFile = ttvFiles.getSelectionModel().getSelectedItem().getValue();

        if (Files.exists(selectedFile.getFilePath())){
            try {
                Files.delete(selectedFile.getFilePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
