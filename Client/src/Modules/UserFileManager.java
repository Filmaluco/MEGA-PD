package Modules;

import Core.MegaPDFile;
import Models.View.FileModel;
import javafx.collections.ObservableList;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class UserFileManager extends Thread {

    private final String defaultFolderName = "/MegaPDFiles";
    private String folderPathName;
    private final List<MegaPDFile> megaPDFiles = new ArrayList<>();
    private Path folderPath;
    private WatchService watchService;
    private ObservableList<FileModel> fileModels;

    public UserFileManager(ObservableList<FileModel> fileModels) {
        this.fileModels = fileModels;
        folderPathName = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        folderPathName += defaultFolderName;
        folderPath = Paths.get(folderPathName);
        initFolder();
        registerFiles();
        setWatcher();
    }

    private void initFolder(){
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerFiles(){
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(folderPath);

            for (Path entry : stream) {
                addFile(entry);
            }

            stream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setWatcher(){
        try {
            watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = folderPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processFolderFiles(){
        WatchKey key;
        try {
            while ((key = watchService.take()) != null) {
                WatchEvent.Kind<?> kind = null;

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path newPath;
                    kind = event.kind();

                    if (OVERFLOW == kind) {
                        continue; // loop
                    } else if (ENTRY_CREATE == kind) {
                        // A new Path was created
                        newPath = ((WatchEvent<Path>) event).context();
                        addFile(newPath);
                        // Output
                        System.out.println("New folderPath created: " + newPath);
                    } else if (ENTRY_MODIFY == kind) {
                        // modified
                        newPath = ((WatchEvent<Path>) event).context();
                        updateFile(newPath);
                        // Output
                        System.out.println("New folderPath modified: " + newPath);
                    } else if (ENTRY_DELETE == kind){
                        newPath = ((WatchEvent<Path>) event).context();
                        deleteFile(newPath);
                        // Output
                        System.out.println("Path deleted: " + newPath);
                    }
                }

                listAllFiles();

                if(!key.reset()){
                    break;
                }

            }
        }catch (InterruptedException e){

        }
    }

    public boolean deleteFile(Path filePath){
        for (int i = 0; i < megaPDFiles.size(); i++) {
            String fileName = filePath.getFileName().toString();
            if (megaPDFiles.get(i).getFileName().equals(fileName)){
                megaPDFiles.remove(i);
                fileModels.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean addFile(Path filePath){
        String filename = filePath.getFileName().toString();
        String extension = getExtensionByStringHandling(filename);
        long filesize = getFileSize(filename);
        String filepath = folderPathName + '/' + filename;
        fileModels.add(new FileModel(Paths.get(filepath), filename, getStringSizeLengthFile(filesize)));
        return megaPDFiles.add(new MegaPDFile(filePath.toAbsolutePath().toString(), filename, extension, filesize));
    }

    public boolean updateFile(Path filePath){
        for (int i = 0; i < megaPDFiles.size(); i++) {
            String fileName = filePath.getFileName().toString();
            if (megaPDFiles.get(i).getFileName().equals(fileName)){
                long fileSize = getFileSize(filePath.getFileName().toString());
                megaPDFiles.get(i).setFileSize(fileSize);
                fileModels.get(i).setSize(getStringSizeLengthFile(fileSize));
                return true;
            }
        }
        return false;
    }


    public void listAllFiles(){
//        for (Path entry: fileModels) {
//            //List Directory name and
//            //System.out.println(entry.toString());
//            System.out.println(entry.getFileName().toString());
//        }

        for(MegaPDFile megaPDFile : megaPDFiles){
            System.out.println(megaPDFile);
        }
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

    public String getFilesFolderPathString() {
        return folderPathName;
    }

    public long getFileSize(String filename){
        //float sizeKb = 1024.0f;
        //float sizeMb = sizeKb * sizeKb;

        String filePath = folderPathName + '/' + filename;

        long imageFileSize = -1;

        Path imageFilePath = Paths.get(filePath);

        try {
            FileChannel imageFileChannel = FileChannel.open(imageFilePath);
            imageFileSize = imageFileChannel.size();
            //imageFileSize /= sizeMB;
        }catch (IOException e){
            e.printStackTrace();
        }

        return imageFileSize;
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
