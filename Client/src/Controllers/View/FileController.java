package Controllers.View;

import Models.ViewModels.File;
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
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class FileController implements Initializable {

    @FXML
    private JFXTreeTableView<File> ttvFiles;

    public ObservableList<File> files = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttvFiles.getStyleClass().add("noheader");

        //Setting up columns
        JFXTreeTableColumn<File, String> filename = new JFXTreeTableColumn<>("Filename");
        filename.setPrefWidth(400);
        filename.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().filenameProperty();
            }
        });


        JFXTreeTableColumn<File, String> fileSize = new JFXTreeTableColumn<>("FileSize");
        fileSize.setPrefWidth(400);
        fileSize.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().sizeProperty();
            }
        });

        //Adding files to observable array
        //files.add(new File("Test","1GB", "JohnDoe", "21/11/2018"));

        final TreeItem<File> root = new RecursiveTreeItem<File>(files, RecursiveTreeObject::getChildren);
        ttvFiles.getColumns().setAll(filename, fileSize);
        ttvFiles.setRoot(root);
        ttvFiles.setShowRoot(false);
    }

    public void addFile(ActionEvent event) {
        //TODO: Show file chooser and get the file to send
    }
}
