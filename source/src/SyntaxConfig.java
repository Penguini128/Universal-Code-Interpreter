import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;;

public class SyntaxConfig {

    private boolean validConfig;
    private String assemblerSyntaxFileName;
    private String compilerSyntaxFileName;
    private String assemblableFileExtension;
    private String compilableFileExtension;

    SyntaxConfig() {
        assemblerSyntaxFileName = "assembler.syntax";
        compilerSyntaxFileName = "compiler.syntax";
        assemblableFileExtension = ".asm";
        compilableFileExtension = ".comp";
    }

    public boolean isValid() { return validConfig; }
    public String getAssemblerSyntaxFileName() { return assemblerSyntaxFileName; }
    public String getCompilerSyntaxFileName() { return compilerSyntaxFileName; }
    public String getAssemblableFileExtension() { return assemblableFileExtension; }
    public String getCompilableFileExtension() { return compilableFileExtension; }

    public boolean load(File configFile, boolean showErrors) {

        boolean configLoadSuccessful = true;

		Scanner scanner;
		try {
			scanner = new Scanner(configFile);
		} catch (FileNotFoundException e) {
            validConfig = false;
			return false;
		}

		String[] lineTokens = scanner.nextLine().split(" ");
		if (validSetting(lineTokens, "assembler_syntax_file")) {
			if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
				assemblerSyntaxFileName = lineTokens[2];
            } else {
                ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file must end with file extension \".syntax\"", 1);
                configLoadSuccessful = false;
            }
		} else {
            ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"assembler_syntax_file\" not found", 1);
            configLoadSuccessful = false;
        }

		lineTokens = scanner.nextLine().split(" ");
		if (validSetting(lineTokens, "compiler_syntax_file")) {
            if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
				compilerSyntaxFileName = lineTokens[2];
            } else {
                ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file must end with file extension \".syntax\"", 2);
                configLoadSuccessful = false;
            }
		} else {
            ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"compiler_syntax_file\" not found", 2);
            configLoadSuccessful = false;
        }
		
		lineTokens = scanner.nextLine().split(" ");
		if (validSetting(lineTokens, "assemblable_file_extension")) {
            if (lineTokens[2].charAt(0) == '.') {
				assemblableFileExtension = lineTokens[2];
            } else {
                ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file extension\" must start with a period", 3);
                configLoadSuccessful = false;
            }
		} else {
            ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"assemblable_file_extension\" not found", 3);
            configLoadSuccessful = false;
        }
		
		lineTokens = scanner.nextLine().split(" ");
		if (validSetting(lineTokens, "compilable_file_extension")) {
            if (lineTokens[2].charAt(0) == '.') {
                compilableFileExtension = lineTokens[2];
            } else {
                ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file extension\" must start with a period", 4);
                configLoadSuccessful = false;
            }
        } else {
            ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"compilable_file_extension\" not found", 4);
            configLoadSuccessful = false;
        
        }
		scanner.close();

        if (!configLoadSuccessful) validConfig = false;
        return configLoadSuccessful;
    }
    
    private boolean validSetting(String[] tokens, String desiredSetting) {
		if (tokens[0].equals(desiredSetting) && tokens[1].equals("=") && tokens.length == 3) return true;
		else return false;
	}
}
