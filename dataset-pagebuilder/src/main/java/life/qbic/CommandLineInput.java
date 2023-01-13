package life.qbic;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.nonNull;

@Command(name = "fairy", mixinStandardHelpOptions = true,
        customSynopsis = {"[COMMAND] [-d=<directory> | -f=<jsonldFile>] [-hV]"},
        version = "Version: Proof of concept")
public class CommandLineInput {

    @Command(description = "Create a new Landing page for dataset(s)",
            mixinStandardHelpOptions = true)
    int create() throws IOException {
        System.out.println("Function works");
        Path fileIndex = initiateFileIndex();
        if(nonNull(jsonldFile)){
            Map dataModel = JsonReader.readFile(jsonldFile);
            System.out.println(dataModel);
            try {
                Configuration cfg = TemplateEngine.initiate();
                TemplateEngine.buildPage(dataModel, cfg.getTemplate("LandingPage_template.ftlh"));
            }catch(IOException e){
                e.printStackTrace();
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
            addFileToIndex(dataModel.get("identifier").toString(),fileIndex.toFile());
            return 0;
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

//    @CommandLine.ArgGroup(multiplicity = "1")
//    Exclusive exclusive;
//    static class Exclusive {
        @Option(names = {"-f", "--file"}, description = "the path to a json-ld file", scope = CommandLine.ScopeType.INHERIT)
        File jsonldFile;
        @Option(names = {"-d", "-dir"}, description = "the path to a directory with json-ld file(s)", scope = CommandLine.ScopeType.INHERIT)
        Path directory;
//    }

    private static Path initiateFileIndex() {
        Path filePath = Paths.get("dataset-pagebuilder/src/main/resources/FileIndex.txt");
        final Path absolutePath = filePath.toAbsolutePath();

        if(!absolutePath.toFile().isFile()) {
            try {
                System.out.println(absolutePath);
                FileOutputStream fileIndex = new FileOutputStream(absolutePath.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return absolutePath;
    }

    private static void addFileToIndex(String datasetID, File index) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMANY);
        String lastModified = simpleDateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(index,true)) {
            System.out.printf("%s\t%s%n", datasetID, lastModified);
            writer.write(String.format("%s\t%s%n", datasetID, lastModified));
        }
    }
}
