import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsManagerOLDREF {

    private static String assemblerSyntaxPath = "";
	private static boolean debugAssemblerSyntax = false;
	
	private static String compilerSyntaxPath = "";
	private static boolean debugCompilerSyntax = false;
	
	private static String assembledFileExtension = "";
	private static boolean enableAssembler = false;
	private static boolean debugAssembler = false;
	
	private static String compiledFileExtension = "";
	private static boolean enableCompiler = false;
	private static boolean debugCompiler = false;

    private static ArrayList<SyntaxConfiguration> loadedConfigs = new ArrayList<SyntaxConfiguration>();

    private static int currentLine;

    public static String getAssemblerSyntaxPath() { return assemblerSyntaxPath; }
    public static String getCompilerSyntaxPath() { return compilerSyntaxPath; }
    public static String getAssembledFileExtension() { return assembledFileExtension; }
    public static String getCompiledFileExtension() { return compiledFileExtension; }

    public static boolean getDebugAssemblerSyntax() { return debugAssemblerSyntax; }
    public static boolean getDebugCompilerSyntax() { return debugCompilerSyntax; }
    public static boolean getDebugAssembler() { return debugAssembler; }
    public static boolean getDebugCompiler() { return debugCompiler; }
    public static boolean getEnableAssembler() { return enableAssembler; }
    public static boolean getEnableCompiler() { return enableCompiler; }

    public static void loadSettings(boolean debugMode) throws FileNotFoundException {
        
		File syntaxConfigFolder = new File("syntax_configurations");
		ArrayList<File> validSyntaxConfigFolders = new ArrayList<File>();

		if (syntaxConfigFolder.exists() && syntaxConfigFolder.isDirectory()) {
		} else {
			ErrorManager.printErrorMessage("Folder \"syntax_configurations\" not found. This folder must be restored");
			return;
		}

		for (File f : syntaxConfigFolder.listFiles()) {
			if (f.isDirectory()) {
				validSyntaxConfigFolders.add(f);
			} else ErrorManager.printNotification("Unexpected file \"" + f.getName() + "\" found in syntax configuration folder. This file "
												+ "will not affect program functionality, but should be removed");	
		}

		for (int i = 0; i < validSyntaxConfigFolders.size(); i++) {

			File f = validSyntaxConfigFolders.get(i);

			File configFile = new File(f.getPath() + "\\config.txt");
			String[] configs = new String[5];

			boolean configLoadFailure = false;

			configs[0] = f.getName();

			if (configFile.exists()) {
				Scanner scanner = new Scanner(configFile);
				String[] lineTokens;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assembler_syntax_file", 1, f.getName(), debugMode)) {
					if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[1] = lineTokens[2];
					} else {
						ErrorManager.printDebugErrorMessage(debugMode, "In \"config.txt\" for syntax configuration \"" + f.getName() + 
																		"\", specified syntax file must end with extension \".syntax\"", 1);
						configLoadFailure = true;
					}
				} else configLoadFailure = true;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compiler_syntax_file", 2, f.getName(), debugMode)) {
					if (lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[3] = lineTokens[2];
					} else {
						ErrorManager.printDebugNotification(debugMode, "In \"config.txt\" for syntax configuration \"" + f.getName() + 
																		"\", specified syntax file must end with extension \".syntax\"", 2);
						configLoadFailure = true;
					}
				} else configLoadFailure = true;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assemblable_file_extension", 3, f.getName(), debugMode)) {
					if (lineTokens[2].charAt(0) == '.') {
						configs[2] = lineTokens[2];
					} else {
						ErrorManager.printDebugNotification(debugMode, "In \"config.txt\" for syntax configuration \"" + f.getName() + 
																		"\", specified file extension must start with a period", 3);
						configLoadFailure = true;
					}
				} else configLoadFailure = true;
				
				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compilable_file_extension", 4, f.getName(), debugMode)) {
					if (lineTokens[2].charAt(0) == '.') {
						configs[4] = lineTokens[2];
					} else {
						ErrorManager.printDebugNotification(debugMode, "In \"config.txt\" for syntax configuration \"" + f.getName() + 
																		"\", specified file extension must start with a period", 4);
						configLoadFailure = true;
					}
				} else configLoadFailure = true;

				scanner.close();
			} else {
				configs[1] = "assembler.syntax";
				configs[2] = ".asm";
				configs[3] = "compiler.syntax";
				configs[4] = ".comp";
			}

			if (configLoadFailure && debugMode) continue;

			File assemblerSyntaxFile = new File(f.getPath() + "\\" + configs[1]);
			if (!assemblerSyntaxFile.exists()) {
				ErrorManager.printDebugNotification(debugMode, "Missing file \"" + configs[1] + "\" in syntax configuration \"" + f.getName() + "\"");
				configLoadFailure = true;
			}
			File compilerSyntaxFile = new File(f.getPath() + "\\" + configs[3]);
			if (!compilerSyntaxFile.exists()) {
				ErrorManager.printDebugNotification(debugMode, "Missing file \"" + configs[3] + "\" in syntax configuration \"" + f.getName() + "\"");
				configLoadFailure = true;
			}

			String[] validFileNames = { "config.txt", "README.txt", assemblerSyntaxFile.getName(), compilerSyntaxFile.getName() };

			for (String s : f.list()) {
				boolean valid = false;
				for (String t : validFileNames) {
					if (s.equals(t)) valid = true;
				}
				if (!valid) {
					ErrorManager.printDebugNotification(debugMode, "Syntax configuration \"" + f.getName() + "\" contains extraneous files");
					configLoadFailure = true;
					break;
				}
			}

			if (configLoadFailure) {
				if (!debugMode) ErrorManager.printNotification("Syntax configuration \"" + f.getName() + "\" failed to load due to errors in configuration");
				validSyntaxConfigFolders.remove(i);
				i--;
			}
		}
	}

	private static boolean validSetting(String[] tokens, String desiredSetting, int settingNumber, String configName, boolean debugMode) {
		if (tokens[0].equals(desiredSetting) && tokens[1].equals("=") && tokens.length == 3) {
			return true;
		} else {
			if (debugMode)
				ErrorManager.printErrorMessage("Incorrectly formatted setting \"" + desiredSetting + "\""
											 + " in \"config.txt\" for syntax configuration \"" + configName + "\"", settingNumber);
			return false;
		}
	}
}