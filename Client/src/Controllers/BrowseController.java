package Controllers;

import Core.Context;
import Core.MegaPDFile;
import Core.UserInfo;
import Helpers.ListViewSetupHelper;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BrowseController implements Initializable{

    private UserInfo selectedUser = null;

    @FXML
    private JFXTreeTableView<FileModel> ttvBrowse;

    public ObservableList<FileModel> userFileNames = FXCollections.observableArrayList();
    public List<MegaPDFile> userFiles;

    @FXML
    private JFXListView<String> lvUsersList;


    @FXML
    private JFXButton btnDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttvBrowse.getStyleClass().add("noheader");

        //Sample user
        selectedUser = new UserInfo(0, "", "", "",0,0,0);


        //Setting up browse columns
        JFXTreeTableColumn<FileModel, String> filename = new JFXTreeTableColumn<>("Filename");
        filename.setPrefWidth(300);
        filename.setCellValueFactory(param -> param.getValue().getValue().filenameProperty());

        JFXTreeTableColumn<FileModel, String> fileSize = new JFXTreeTableColumn<>("FileSize");
        fileSize.setPrefWidth(300);
        fileSize.setCellValueFactory(param -> param.getValue().getValue().sizeProperty());

        final TreeItem<FileModel> root = new RecursiveTreeItem<FileModel>(userFileNames, RecursiveTreeObject::getChildren);
        ttvBrowse.getColumns().setAll(filename, fileSize);
        ttvBrowse.setRoot(root);
        ttvBrowse.setShowRoot(false);

        lvUsersList.setItems(Context.getUsersList());
        //Setting up users list
        lvUsersList.setCellFactory(param -> new ListViewSetupHelper());

        //Bind to disable browser if there are no users online
        ttvBrowse.disableProperty().bind(Bindings.isEmpty(lvUsersList.getItems()));
        //btnDownload.disableProperty().bind(Bindings.isEmpty(lvUsersList.getItems()));
    }

    @FXML
    public void startDownload(ActionEvent event) {
        Context.getUsersList().add("Ola");
        FileModel file = ttvBrowse.getSelectionModel().getSelectedItem().getValue();
        if (file == null) return;

        //TODO: Send request to start download
    }

    @FXML
    public void selectUser(MouseEvent event) {
        System.out.println("user selected");
        if(lvUsersList.getItems().isEmpty()) return;

        String selectedUsername = lvUsersList.getSelectionModel().getSelectedItem();

        //Check if anything was selected
        if (selectedUsername == null) return;
        //check if selected the same user
        else if (selectedUser.getName().compareTo(selectedUsername) == 0) return;
        else {

            try {
                //retrieve new user info
                selectedUser = Context.getConnection().getUser(selectedUsername);

                userFileNames.clear();
                userFiles = Context.getFileManager().getUserFiles(selectedUser.getID());

                for (MegaPDFile file : userFiles){
                    userFileNames.add(new FileModel(file.getFileName(), Long.toString(file.getFileSize())));
                }
            } catch (MegaPDRemoteException | IOException e) {
                e.printStackTrace();
                return;
            }
        }


    }
}
