package life.qbic;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FileIndexHandler {
    static final Path SITEMAP = setOutputPath("webapp" + File.separator + "sitemap.xml");
    static final Path FILEINDEX = setOutputPath("webapp" + File.separator + "FileIndex.txt");
    static final String REMOTEINDEX = "/var/FileIndex.txt";
    public static void initiateFileIndex(ServerCommunication sc) throws IOException, JSchException, SftpException {
        Boolean existenceOfRemoteFileIndex = sc.DoesRemoteFileExist(REMOTEINDEX);
        if(existenceOfRemoteFileIndex){
            sc.getFile(REMOTEINDEX,FILEINDEX.toString());
        } else if (!FILEINDEX.toFile().isFile()) {
            FileOutputStream fileIndex = new FileOutputStream(FILEINDEX.toString());
            fileIndex.close();
        }
    }

    static void addFileToIndex(File file, Map<String,Object> dataModel) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.GERMANY);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        String lastModified = simpleDateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(file,true)) {
            writer.write(String.format("%s\t%s\t%s\t%s", dataModel.get("identifier"), dataModel.get("url"), lastModified, dataModel.get("description")));
            writer.write(System.lineSeparator());
        }
    }

    static void updateFileIndex(Map<String, Object> dataModel) throws IOException {
        final String identifier = dataModel.get("identifier").toString();
        File tempFileIndex = new File(setOutputPath("resources" + File.separator + "TempFileIndex.txt").toString());
        File originalFileIndex = new File(FILEINDEX.toUri());

        boolean wasReplaced = false;
        String currentIdOfFile;

        // create temp file that will become new Fileindex
        FileOutputStream output = new FileOutputStream(tempFileIndex);
        output.close();

        // read in current Fileindex: [id, url, lastMod, description]
        FileReader reader = new FileReader(originalFileIndex);
        BufferedReader buffReader = new BufferedReader(reader);

        String lineToRead = buffReader.readLine();
        while(lineToRead != null) {
            currentIdOfFile = lineToRead.split("\\t")[0];
            if (currentIdOfFile.equals(identifier)) {
                addFileToIndex(tempFileIndex,dataModel);
                wasReplaced = true;
            } else{
                try (FileWriter writer = new FileWriter(tempFileIndex,true)) {
                    writer.write(lineToRead);
                    writer.write(System.lineSeparator());
                }
            }
            lineToRead = buffReader.readLine();
        }
        buffReader.close();

        if (!wasReplaced){
            addFileToIndex(tempFileIndex,dataModel);
        }
        if (!originalFileIndex.delete()) {
            System.out.println("Failed to delete the file.");
        }
        if (!tempFileIndex.renameTo(originalFileIndex))
            System.out.println("Temp file could not be renamed");

    }

    public static Path setOutputPath(String resourceRelativePath){
        String fullResourceRelativePath = "dataset-pagebuilder" + File.separator + "src" + File.separator + "main" + File.separator + resourceRelativePath;
        Path userDir = Paths.get(System.getProperty("user.dir"));
        if (userDir.getFileName().toString().equals("target")){
            userDir = userDir.getParent();
            userDir = userDir.getParent();
        }
        return Paths.get(userDir.toString() + File.separator + fullResourceRelativePath);
    }

}

