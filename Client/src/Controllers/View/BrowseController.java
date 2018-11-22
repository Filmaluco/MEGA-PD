package Controllers.View;

import Models.ViewModels.File;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseController implements Initializable {

    @FXML
    private JFXTreeTableView<File> ttvBrowse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
