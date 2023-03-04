import java.io.File;

public class SyntaxProfile {


    File profileFolder;
    boolean validProfile;
    String name;
    SyntaxConfig syntaxConfig;
    File configFile;
    File assemblerSyntaxFile;
    File compilerSyntaxFile;

    SyntaxProfile(File profileFolder) {

        this.profileFolder = profileFolder;
        name = profileFolder.getName();

        reloadProfile(false);
    }

    public void reloadProfile(boolean showErrors) { 
        validProfile = true;
        syntaxConfig = new SyntaxConfig();
        configFile = new File(profileFolder.getPath() + "\\config.txt");
        if (configFile.exists()) {
            if (!syntaxConfig.load(configFile, showErrors)) {
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

        if (!checkFiles(showErrors)) {
            validProfile = false;
        }
    }

    public boolean checkFiles(boolean showErrors) {

        boolean anyInvalid = false;
        String[] validFileNames = { "config.txt", "README.txt", syntaxConfig.getAssemblerSyntaxFileName(), syntaxConfig.getCompilerSyntaxFileName() };
        boolean containsAssmblerSyntax = false;
        boolean containsCompilerSyntax = false;

        if (validFileNames[2].equals(validFileNames[3])) {
            ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": \"assembler_syntax_file\" and \"compiler_syntax_file\" cannot be set to the same file");
            anyInvalid = true;
        }

        for (String s : profileFolder.list()) {
            boolean validFile = false;
            if (s.equals(validFileNames[2])) containsAssmblerSyntax = true;
            if (s.equals(validFileNames[3])) containsCompilerSyntax = true;
            for (int i = 0; i < validFileNames.length; i++) {
                if (validFileNames[i].equals(s)) validFile = true;
            }
            if (!validFile) {
                ErrorManager.printErrorMessage(showErrors, "Syntax profile \"" + name + "\" contains invalid file \"" + s + "\"");
                anyInvalid = true;
            }
        }

        if (!containsAssmblerSyntax) {
            ErrorManager.printErrorMessage(showErrors, "Syntax profile \"" + name + "\" does not contain assembler syntax file \"" + validFileNames[2] + "\"");
            anyInvalid = true;
        }

        if (!containsCompilerSyntax) {
            ErrorManager.printErrorMessage(showErrors, "Syntax profile \"" + name + "\" does not contain compiler syntax file \"" + validFileNames[3] + "\"");
            anyInvalid = true;
        }

        if (anyInvalid) return false; else return true;
    }

    public SyntaxConfig getSyntaxConfig() { return syntaxConfig; }
    public File getAssemblerSyntaxFile() { return assemblerSyntaxFile; }
    public File getCompilerSyntaxFile() { return compilerSyntaxFile; }

    public boolean isValid() { return validProfile; }

    public String toString() { return name; }
}
