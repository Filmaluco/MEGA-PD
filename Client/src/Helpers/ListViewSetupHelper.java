package Helpers;

import javafx.scene.control.ListCell;

public final class ListViewSetupHelper extends ListCell<String> {
    public void updateItem(String name, boolean empty){
        super.updateItem(name, empty);

        if (empty)
            setText(null);
        else
            setText(name);
    }
}
