package life.qbic;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.*;
import java.util.Map;

import static java.util.Objects.nonNull;

@Command(name = "fairy", mixinStandardHelpOptions = true,
        customSynopsis = {"[COMMAND] -f=<tsvFile> -u=<username> [-hV]"},
        version = "Version: Proof of concept")
public class CommandLineInput {
    @Option(names = {"-f", "--file"}, description = "The path to a tsv file describing dataset metadata", scope = CommandLine.ScopeType.INHERIT, required = true)
    File tsvFile;

    @Option(names = {"-u", "--username"}, description = "Username for connecting to the server", scope = CommandLine.ScopeType.INHERIT, required = true)
    String user;

    @Command(description = "Create a new Landing page for dataset(s)",
            mixinStandardHelpOptions = true)
    public int create() throws IOException, JSchException, SftpException {
        if(nonNull(tsvFile) && tsvFile.isFile()){
            ServerCommunication sc = ServerCommunication.authenticate(user);
            FileIndexHandler.initiateFileIndex(sc);

            BufferedReader buffReader = new BufferedReader(new FileReader(tsvFile));
            String[] keys = buffReader.readLine().split("\\t");
            String[] values;
            String lineToRead = buffReader.readLine();

            while(lineToRead != null){
                System.out.println("Reading file...");
                values = lineToRead.split("\\t");
                TSVDataModel tsvModel = new TSVDataModel();
                Map<String,Object> dataModel = tsvModel.createDataModel(keys, values);
                PageBuilder.createLandingPage(dataModel,sc);
                lineToRead = buffReader.readLine();
            }
            // creating/updating the dataset navigation page and the sitemap
            PageBuilder.createSEOPages(sc);
            sc.closeConnections();

            return 0;
        } else {
            System.out.println("Please provide a Path to a tsv file by -f <path>");
            return 1;
        }
    }
}
