package Controllers;

import Core.Context;
import Helpers.ListViewSetupHelper;
import Models.View.FileModel;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseController implements Initializable{

    private String selectedUser = "none";

    @FXML
    private JFXTreeTableView<FileModel> ttvBrowse;

    public ObservableList<FileModel> userDownloads = FXCollections.observableArrayList();

    @FXML
    private JFXListView<String> lvUsersList;


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

        final TreeItem<FileModel> root = new RecursiveTreeItem<FileModel>(userDownloads, RecursiveTreeObject::getChildren);
        ttvBrowse.getColumns().setAll(filename, fileSize);
        ttvBrowse.setRoot(root);
        ttvBrowse.setShowRoot(false);

        lvUsersList.setItems(Context.getUsersList());
        //Setting up users list
        lvUsersList.setCellFactory(param -> new ListViewSetupHelper());

        //Bind to disable browser if there are no users online
        ttvBrowse.disableProperty().bind(Bindings.isEmpty(lvUsersList.getItems()));
        btnDownload.disableProperty().bind(Bindings.isEmpty(lvUsersList.getItems()));
    }

    @FXML
    public void startDownload(ActionEvent event) {
        FileModel file = ttvBrowse.getSelectionModel().getSelectedItem().getValue();
        if (file == null) return;

        //TODO: Send request to start download
    }

    @FXML
    public void selectUser(MouseEvent event) {
        if(lvUsersList.getItems().isEmpty()) return;

        String user = lvUsersList.getSelectionModel().getSelectedItem();

        if (user == null) return;
        else if (selectedUser.equals(user)) return;
        else selectedUser = user;

        userDownloads.clear();
        //TODO: Get User Downloads from server into userDownloads
        //userDownloads.add();
    }
}
