package Models.View;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class FileModel extends RecursiveTreeObject<FileModel> {
    Path filePath;
    StringProperty filename;
    StringProperty size;
    StringProperty downloadedFrom;
    StringProperty downloadDate;


    public FileModel(Path filePath, String filename, String size, String downloadedFrom, String downloadDate) {
        this.filename = new SimpleStringProperty(filename);
        this.size = new SimpleStringProperty(size);
        this.downloadedFrom = new SimpleStringProperty(downloadedFrom);
        this.downloadDate = new SimpleStringProperty(downloadDate);
    }

    public FileModel(Path filePath, String filename, String size) {
        this.filePath = filePath;
        this.filename = new SimpleStringProperty(filename);
        this.size = new SimpleStringProperty(size);
    }

    public FileModel(String filename, String size) {
        this.filename = new SimpleStringProperty(filename);
        this.size = new SimpleStringProperty(size);
    }


    public String getFilename() {
        return filename.get();
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public String getDownloadedFrom() {
        return downloadedFrom.get();
    }

    public StringProperty downloadedFromProperty() {
        return downloadedFrom;
    }

    public String getDownloadDate() {
        return downloadDate.get();
    }

    public StringProperty downloadDateProperty() {
        return downloadDate;
    }

    public void setSize(String size) { this.size.set(size); }

    public Path getFilePath() { return filePath; }
}
