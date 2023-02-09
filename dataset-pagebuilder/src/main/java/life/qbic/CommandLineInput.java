package life.qbic;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.file.Path;

import static java.util.Objects.nonNull;

@Command(name = "fairy", mixinStandardHelpOptions = true,
        customSynopsis = {"[COMMAND] [-d=<directory> | -f=<jsonldFile>] [-hV]"},
        version = "Version: Proof of concept")
public class CommandLineInput {

    @Command(description = "Create a new Landing page for dataset(s) or a data catalog",
            mixinStandardHelpOptions = true)
    public int create() throws IOException {
        if(nonNull(jsonldFile) && jsonldFile.isFile()){
            return FileHandler.createLandingPage(jsonldFile);
        } else {
            System.out.println("there is no file present.");
            return 1;
        }
    }

    @Command(description = "Delete Landing page(s) for dataset(s)",
            mixinStandardHelpOptions = true)
    int delete(){
        //TODO
        return CommandLine.ExitCode.OK;
    }

    @Command(description = "Update Landing page(s) for dataset(s)",
            mixinStandardHelpOptions = true)
    int update(){
        //TODO
        return CommandLine.ExitCode.OK;
    }

    @Option(names = {"-f", "--file"}, description = "the path to a json-ld file", scope = CommandLine.ScopeType.INHERIT)
    File jsonldFile;
    @Option(names = {"-d", "-dir"}, description = "the path to a directory with json-ld file(s) to be used", scope = CommandLine.ScopeType.INHERIT)
    Path directory;

}
