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
    static final Path FILEINDEX = Paths.get("dataset-pagebuilder/src/main/resources/FileIndex.txt").toAbsolutePath();
    static final String REMOTEFI = "/var/FileIndex.txt";
    public static void initiateFileIndex(ServerCommunication sc) throws IOException, JSchException, SftpException {
        Boolean existenceOfRemoteFileIndex = sc.DoesRemoteFileExist(REMOTEFI);
        System.out.println(existenceOfRemoteFileIndex);
        if(existenceOfRemoteFileIndex){
            System.out.println("FileIndex exists on server");
            sc.getFile(REMOTEFI,FILEINDEX.toString());
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

    private static void updateFileIndex(Map<String,Object> dataModel) throws IOException {
        final String identifier = dataModel.get("identifier").toString();
        final File tempFileIndex = Paths.get("dataset-pagebuilder/src/main/resources/TempFileIndex.txt").toFile();
        boolean wasReplaced = false;
        String currentIdOfFile;

        // create temp file that will become new Fileindex
        FileOutputStream output = new FileOutputStream(tempFileIndex);
        output.close();

        // read in current Fileindex: [id, url, lastMod, description]
        FileReader reader = new FileReader(FILEINDEX.toFile());
        BufferedReader buffReader = new BufferedReader(reader);

        String lineToRead = buffReader.readLine();
        while(lineToRead != null) {
            currentIdOfFile = lineToRead.split("\\t")[0];
            System.out.println(currentIdOfFile);
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
        if (!wasReplaced){
            addFileToIndex(tempFileIndex,dataModel);
        }
        System.out.println(tempFileIndex.renameTo(FILEINDEX.toFile()));
    }
}
