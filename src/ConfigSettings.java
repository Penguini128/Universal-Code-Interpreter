import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigSettings {

    boolean validConfig;

    private String configName;
    private String assemblerSyntaxFileName;
    private String compilerSyntaxFileName;
    private String assemblableFileExtension;
    private String compilableFileExtension;

    ConfigSettings(String configName) {
        validConfig = false;
        this.configName = configName;
    }

    ConfigSettings(String[] configs) {
        validConfig = true;
        configName = configs[0];
        assemblerSyntaxFileName = configs[1];
        compilerSyntaxFileName = configs[2];
        assemblableFileExtension = configs[3];
        compilableFileExtension = configs[4];
    }

    public boolean isValid() { return validConfig; }
    public String getConfigName() { return configName; }
    public String getAssemblerSyntaxFileName() { return assemblerSyntaxFileName; }
    public String getCompilerSyntaxFileName() { return compilerSyntaxFileName; }
    public String getAssemblableFileExtension() { return assemblableFileExtension; }
    public String getCompilableFileExtension() { return compilableFileExtension; }

    public static ConfigSettings load(File configFile, boolean showErrors) {

            String[] configs = new String[5];
			boolean configLoadFailure = false;
			configs[0] = configFile.getParent();


			if (configFile.exists()) {
				Scanner scanner;
				try {
					scanner = new Scanner(configFile);
				} catch (FileNotFoundException e) {
					ErrorManager.printErrorMessage("Error occured when attempting to read \"config.txt\" in syntax configuration folder \"" + configs[0] + "\"");
					return new ConfigSettings(configs[0]);
				}
				String[] lineTokens;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assembler_syntax_file")) {
					if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[1] = lineTokens[2];
                    } else {
                        ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file must end with file extension \".syntax\"", 1);
                        configLoadFailure = true;
                    }
				} else {
                    ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"assembler_syntax_file\" not found", 1);
                    configLoadFailure = true;
                }

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compiler_syntax_file")) {
                    if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[3] = lineTokens[2];
                    } else {
                        ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file must end with file extension \".syntax\"", 2);
                        configLoadFailure = true;
                    }
				} else {
                    ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"compiler_syntax_file\" not found", 2);
                    configLoadFailure = true;
                }
					

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assemblable_file_extension")) {
                    if (lineTokens[2].charAt(0) == '.') {
						configs[2] = lineTokens[2];
                    } else {
                        ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file extension\" must start with a period", 3);
                        configLoadFailure = true;
                    }
				} else {
                    ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"assemblable_file_extension\" not found", 3);
                    configLoadFailure = true;
                }
				
				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compilable_file_extension")) {
                    if (lineTokens[2].charAt(0) == '.') {
                        configs[4] = lineTokens[2];
                    } else {
                        ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Specified file extension\" must start with a period", 4);
                        configLoadFailure = true;
                    }
                } else {
                    ErrorManager.printErrorMessage(showErrors, "In \"config.txt\": Configuration setting \"compilable_file_extension\" not found", 4);
                    configLoadFailure = true;
                }

				scanner.close();
			} else {
				configs[1] = "assembler.syntax";
				configs[2] = ".asm";
				configs[3] = "compiler.syntax";
				configs[4] = ".comp";
			}

            if (!configLoadFailure) return new ConfigSettings(configs);
            else return new ConfigSettings(configs[0]);
    }

    private static boolean validSetting(String[] tokens, String desiredSetting) {
		if (tokens[0].equals(desiredSetting) && tokens[1].equals("=") && tokens.length == 3) return true;
		else return false;
	}

}
