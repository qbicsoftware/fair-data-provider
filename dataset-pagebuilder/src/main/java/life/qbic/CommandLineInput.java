package life.qbic;
import com.jcraft.jsch.JSchException;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.*;
import java.util.Map;

import static java.util.Objects.nonNull;

@Command(name = "fairy", mixinStandardHelpOptions = true,
        customSynopsis = {"[COMMAND] [-d=<directory> | -f=<jsonldFile>] [-hV]"},
        version = "Version: Proof of concept")
public class CommandLineInput {
    @Option(names = {"-f", "--file"}, description = "the path to a tsv file describing dataset metadata", scope = CommandLine.ScopeType.INHERIT)
    File tsvFile;

    @Command(description = "Create a new Landing page for dataset(s) or a data catalog",
            mixinStandardHelpOptions = true)
    public int create() throws IOException, JSchException {
        if(nonNull(tsvFile) && tsvFile.isFile()){
            BufferedReader buffReader = new BufferedReader(new FileReader(tsvFile));
            String[] keys = buffReader.readLine().split("\\t");
            String[] values;
            String lineToRead = buffReader.readLine();
            ServerCommunication sc = ServerCommunication.authenticate();

            while(lineToRead != null){
                System.out.println("Reading file...");
                values = lineToRead.split("\\t");
                Map<String,Object> dataModel = FileHandler.createDatasetDataModel(keys, values);
                FileHandler.createLandingPage(dataModel,sc);
                lineToRead = buffReader.readLine();
            }

            sc.closeConnections();
            return 0;
        } else {
            System.out.println("Please provide a Path to a tsv file by -f <path>");
            return 1;
        }
    }

    @Command(description = "Delete Landing page(s) for dataset(s)",
            mixinStandardHelpOptions = true)
    int delete(){
        //TODO
        return CommandLine.ExitCode.OK;
    }
}
