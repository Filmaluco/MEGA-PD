package PD.Core;

import java.util.Date;

public class FileHistory {

    int ID;
    String ownerNanme;
    String targetName;
    String fileName;
    Date date;
    boolean status;

    public FileHistory(int ID, String ownerNanme, String targetName, String fileName, Date date, boolean status) {
        this.ID = ID;
        this.ownerNanme = ownerNanme;
        this.targetName = targetName;
        this.fileName = fileName;
        this.date = date;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public String getOwnerNanme() {
        return ownerNanme;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getDate() {
        return date;
    }

    public boolean isStatus() {
        return status;
    }
}
