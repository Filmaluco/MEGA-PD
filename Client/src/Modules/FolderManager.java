package Modules;

import Core.Context;
import Core.MegaPDFile;
import MegaPD.Core.Exeptions.MegaPDRemoteException;
import Models.View.FileModel;
import javafx.collections.ObservableList;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderManager extends Thread {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;

    private final String defaultFolderName = "/MegaPDFiles";
    private String folderPathName;
    private final List<MegaPDFile> megaPDFiles = new ArrayList<>();
    private Path folderPath;
    private ObservableList<FileModel> fileModels;

    public FolderManager(ObservableList<FileModel> fileModels) throws IOException {
        this.fileModels = fileModels;
        folderPathName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        folderPathName += defaultFolderName;
        folderPath = Paths.get(folderPathName);
        initFolder();
        registerFiles();

        //Send to the server my files


        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        WatchKey key = folderPath.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, folderPath);
    }

    private void initFolder(){
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerFiles() {
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(folderPath);

            for (Path entry : stream) {
                addFile(entry);
            }
        }catch (Exception e){}
    }

    private void processFolderFiles(){
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);

                // print out event
                //System.out.format("%s: %s\n", event.kind().name(), child);
                if (kind == ENTRY_CREATE) {
                    System.out.println("New file added: " + child);
                    addFile(child);
                }else if(kind == ENTRY_DELETE){
                    System.out.println(child + "deleted");
                    deleteFile(child);
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public boolean deleteFile(Path filePath){
        for (int i = 0; i < megaPDFiles.size(); i++) {
            String fileName = filePath.getFileName().toString();
            if (megaPDFiles.get(i).getFileName().equals(fileName)){
                try {
                    Context.getFileManager().remove(megaPDFiles.get(i));
                } catch (MegaPDRemoteException | IOException e) {
                    Context.getNotificationManager().addNotification(e.getMessage());
                    return false;
                }
                megaPDFiles.remove(i);
                fileModels.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addFile(Path path){
        // Information extraction
        String fileName = path.getFileName().toString();
        String extension = getExtensionByStringHandling(fileName);
        long fileSize = path.toFile().length();
        String filePath = folderPathName + '/' + fileName;
        //----------------------------------------------
        FileModel fileModel = new FileModel(Paths.get(filePath), fileName, getStringSizeLengthFile(fileSize));
        MegaPDFile file = new MegaPDFile(path.toAbsolutePath().toString(), fileName, extension, fileSize);
        //----------------------------------------------

        try {
            Context.getFileManager().addFile(file);
        } catch (MegaPDRemoteException | IOException e) {
            path.toFile().delete();
            Context.getNotificationManager().addNotification(e.getMessage());
            //e.printStackTrace();
            return;
        }
        fileModels.add(fileModel);
        megaPDFiles.add(file);
    }


    public String getExtensionByStringHandling(String filename) {

        int lastIndexOfDot = filename.lastIndexOf('.');

        String fileExtension = null;
        if (lastIndexOfDot > 0) {
            fileExtension = filename.substring(lastIndexOfDot+1);
        }

        return fileExtension;
    }

    public Path getFilesFolderPath() {
        return folderPath;
    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGo = sizeMb * sizeKb;
        float sizeTerra = sizeGo * sizeKb;

        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGo)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGo) + " Go";

        return "";
    }

    @Override
    public void run() {
        processFolderFiles();
    }
}
