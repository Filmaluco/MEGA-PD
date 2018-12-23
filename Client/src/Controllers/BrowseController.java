package Controllers;

import Models.View.FileModel;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseController implements Initializable {

    @FXML
    private JFXTreeTableView<FileModel> ttvBrowse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttvBrowse.getStyleClass().add("noheader");
    }
}
