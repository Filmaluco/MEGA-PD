package Controllers.View;

import Models.View.File;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class TransferController implements Initializable {
    @FXML
    private JFXTreeTableView<File> ttvTransfers;

    public ObservableList<File> transfers = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttvTransfers.getStyleClass().add("noheader");

        //Setting up columns
        JFXTreeTableColumn<File, String> filename = new JFXTreeTableColumn<>("Filename");
        filename.setPrefWidth(170);
        filename.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().filenameProperty();
            }
        });


        JFXTreeTableColumn<File, String> fileSize = new JFXTreeTableColumn<>("FileSize");
        fileSize.setPrefWidth(170);
        fileSize.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().sizeProperty();
            }
        });

        JFXTreeTableColumn<File, String> downloadedFrom = new JFXTreeTableColumn<>("DownloadedFrom");
        downloadedFrom.setPrefWidth(170);
        downloadedFrom.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().downloadedFromProperty();
            }
        });

        JFXTreeTableColumn<File, String> downloadedDate = new JFXTreeTableColumn<>("DownloadedDate");
        downloadedDate.setPrefWidth(170);
        downloadedDate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<File, String> param) {
                return param.getValue().getValue().downloadDateProperty();
            }
        });

        //Adding files to observable array
        //transfers.add(new File("Test","1GB", "JohnDoe", "21/11/2018"));

        final TreeItem<File> root = new RecursiveTreeItem<File>(transfers, RecursiveTreeObject::getChildren);
        ttvTransfers.getColumns().setAll(filename, fileSize, downloadedFrom, downloadedDate);
        ttvTransfers.setRoot(root);
        ttvTransfers.setShowRoot(false);
    }
}
