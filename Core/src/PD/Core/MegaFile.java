package PD.Core;

public class MegaFile {
    int ID;
    int OwnerID;
    String name;
    String directory;
    int size;

    public MegaFile(int ID, int ownerID, String name, String directory, int size) {
        this.ID = ID;
        OwnerID = ownerID;
        this.name = name;
        this.directory = directory;
        this.size = size;
    }

    public int getID() {
        return ID;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public int getSize() {
        return size;
    }
}
