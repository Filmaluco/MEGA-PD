package Core;

import java.io.Serializable;
import java.nio.file.Path;

public class MegaPDFile implements Serializable{

    private static final long serialVersionUID = 1L;

    private int ID;
    private int ownerID;
    private Path path;
    private String name;
    private String extension;
    private long size;

    public MegaPDFile(Path filePath,String fileName, String extension, long fileSize) {
        this.path = filePath;
        this.name = fileName;
        this.extension = extension;
        this.size = fileSize;
    }

    public MegaPDFile(int ID, int ownerID, Path path, String name, String extension, long size) {
        this.ID = ID;
        this.ownerID = ownerID;
        this.path = path;
        this.name = name;
        this.extension = extension;
        this.size = size;
    }

    public int getID() { return ID; }
    public int getOwnerID() {
        return ownerID;
    }
    public Path getFilePath() { return path; }
    public String getFileName() { return name; }
    public String getExtension() { return extension; }
    public long getFileSize() { return size; }

    public void setID(int id){this.ID = id;}
    public void setOwnerID(int id){this.ownerID = id;}
    public void setFileSize(long fileSize) { this.size = fileSize; }

    @Override
    public String toString() {
        return "Filename: " + name + "\nExtension: " + extension + "\nFilesize: " + size + '\n';
    }
}