package Controllers;

import Models.View.FileModel;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeItem;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowseController implements Initializable {

    @FXML
    private JFXTreeTableView<FileModel> ttvBrowse;

    public ObservableList<FileModel> fileModels = FXCollections.observableArrayList();

    @FXML
    private JFXListView<String> lvUsers;

    public ObservableList<String> usersList = FXCollections.observableArrayList();

    @FXML
    private JFXButton btnDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttvBrowse.getStyleClass().add("noheader");

        //Setting up browse columns
        JFXTreeTableColumn<FileModel, String> filename = new JFXTreeTableColumn<>("Filename");
        filename.setPrefWidth(300);
        filename.setCellValueFactory(param -> param.getValue().getValue().filenameProperty());

        JFXTreeTableColumn<FileModel, String> fileSize = new JFXTreeTableColumn<>("FileSize");
        fileSize.setPrefWidth(300);
        fileSize.setCellValueFactory(param -> param.getValue().getValue().sizeProperty());

        final TreeItem<FileModel> root = new RecursiveTreeItem<FileModel>(fileModels, RecursiveTreeObject::getChildren);
        ttvBrowse.getColumns().setAll(filename, fileSize);
        ttvBrowse.setRoot(root);
        ttvBrowse.setShowRoot(false);

        lvUsers.setItems(usersList);
        //Setting up users list
        lvUsers.setCellFactory(param -> new ListCell<String>(){
            public void updateItem(String name, boolean empty){
                super.updateItem(name, empty);

                if (empty)
                    setText(null);
                else
                    setText(name);

            }
        });


    }

    @FXML
    public void startDownload(ActionEvent event) {
        //TODO: Send request to start download
    }
}
