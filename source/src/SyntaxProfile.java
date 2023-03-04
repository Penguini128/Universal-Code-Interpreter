import java.io.File;
import java.util.ArrayList;

public class SyntaxProfile {

    
    boolean validProfile;

    String name;
    SyntaxConfig syntaxConfig;
    File configFile;
    File assemblerSyntaxFile;
    File compilerSyntaxFile;
    ArrayList<String> invalidFiles;

    SyntaxProfile(File profileFolder) {

        name = profileFolder.getName();

        syntaxConfig = new SyntaxConfig();
        configFile = new File(profileFolder.getPath() + "\\config.txt");
        if (configFile.exists()) {
            if (!syntaxConfig.load(configFile, false)) {
                System.out.println("Error occured when attempting to read \"config.txt\" in syntax profile\"" + name + "\"");
                validProfile = false;
            }
        }

        assemblerSyntaxFile = new File(profileFolder.getPath() + "\\" + syntaxConfig.getAssemblerSyntaxFileName());
        if (!assemblerSyntaxFile.exists()) {
            assemblerSyntaxFile = null;
            validProfile = false;
        }
        compilerSyntaxFile = new File(profileFolder.getPath() + "\\" + syntaxConfig.getCompilerSyntaxFileName());
        if (!compilerSyntaxFile.exists()) {
            compilerSyntaxFile = null;
            validProfile = false;
        }

        String[] validFileNames = { "config.txt", "README.txt", syntaxConfig.getAssemblerSyntaxFileName(), syntaxConfig.getCompilerSyntaxFileName() };
        
        invalidFiles = new ArrayList<String>();

        for (String s : profileFolder.list()) {
            boolean invalidFile = true;
            for (int i = 0; i < validFileNames.length; i++) {
                if (validFileNames[i].equals(s)) invalidFile = false;
            }
            if (invalidFile) {
                invalidFiles.add(s);
                validProfile = false;
            }
        }
    }

    public boolean isValid() { return validProfile; }

    public String toString() { return name; }
}
