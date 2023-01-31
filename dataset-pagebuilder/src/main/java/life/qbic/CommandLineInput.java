package life.qbic;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Objects.nonNull;

@Command(name = "fairy", mixinStandardHelpOptions = true,
        customSynopsis = {"[COMMAND] [-d=<directory> | -f=<jsonldFile>] [-hV]"},
        version = "Version: Proof of concept")
public class CommandLineInput {
    final Path sitemap = Paths.get("dataset-pagebuilder/src/main/resources/sitemap.xml");

    @Command(description = "Create a new Landing page for dataset(s)",
            mixinStandardHelpOptions = true)
    public int create() throws IOException {
        Path fileIndex = initiateFileIndex();
        if(nonNull(jsonldFile) && jsonldFile.isFile()){
            // read the json file into java
            Map<String,String> dataModel = FileHandler.readJsonFile(jsonldFile);
            try {
                Configuration cfg = TemplateEngine.initiate();
                String newPage = Paths.get("dataset-pagebuilder/src/main/resources/" + dataModel.get("identifier") + ".html").toAbsolutePath().toString();
                // Build the new Landing page
                TemplateEngine.buildPage(dataModel, cfg.getTemplate("LandingPage_template.ftlh"), newPage);
                addFileToIndex(dataModel.get("identifier"),fileIndex.toFile(), dataModel.get("url"));
                // Build the new sitemap
                TemplateEngine.buildPage(FileHandler.createDataModelFromIndex(),cfg.getTemplate("Sitemap_template.ftlx"), sitemap.toString());

            }catch(IOException e){
                e.printStackTrace();
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
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
        System.out.println(absolutePath);

        if(!absolutePath.toFile().isFile()) {
            try {
                FileOutputStream fileIndex = new FileOutputStream(absolutePath.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return absolutePath;
    }

    private static void addFileToIndex(String datasetID, File index, String url) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.GERMANY);
        String lastModified = simpleDateFormat.format(new Date());

        try (FileWriter writer = new FileWriter(index,true)) {
            writer.write(String.format("%s\t%s\t%s%n", datasetID, url, lastModified));
        }
    }
}
