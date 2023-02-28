import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsManager {

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

	private static ArrayList<File> allConfigFolders = new ArrayList<File>();
	private static ArrayList<SyntaxConfiguration> allConfigs = new ArrayList<SyntaxConfiguration>();

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

    public static void loadSettings() {
        
		File syntaxConfigFolder = new File("syntax_configurations");

		if (syntaxConfigFolder.exists() && syntaxConfigFolder.isDirectory()) {
		} else {
			ErrorManager.printErrorMessage("Folder \"syntax_configurations\" not found. This folder must be restored");
			return;
		}

		for (File f : syntaxConfigFolder.listFiles()) {
			if (f.isDirectory()) {
				allConfigFolders.add(f);
			} else ErrorManager.printNotification("Unexpected file \"" + f.getName() + "\" found in syntax configuration folder. This file "
												+ "will not affect program functionality, but should be removed");	
		}

		for (File f : allConfigFolders) {

			File configFile = new File(f.getPath() + "\\config.txt");
			String[] configs = new String[5];

			boolean configLoadFailure = false;

			configs[0] = f.getName();

			if (configFile.exists()) {
				Scanner scanner;
				try {
					scanner = new Scanner(configFile);
				} catch (FileNotFoundException e) {
					ErrorManager.printErrorMessage("Error occured when attempting to read \"config.txt\" in syntax configuration folder \"" + f.getName() + "\"");
					continue;
				}
				String[] lineTokens;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assembler_syntax_file", 1, f.getName())
					&& lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[1] = lineTokens[2];
				} else configLoadFailure = true;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compiler_syntax_file", 2, f.getName())
					&& lineTokens[2].substring(lineTokens[2].length() - 7, lineTokens[2].length()).equals(".syntax")) {
						configs[3] = lineTokens[2];
				} else configLoadFailure = true;

				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "assemblable_file_extension", 3, f.getName()) && lineTokens[2].charAt(0) == '.') {
						configs[2] = lineTokens[2];
				} else configLoadFailure = true;
				
				lineTokens = scanner.nextLine().split(" ");
				if (validSetting(lineTokens, "compilable_file_extension", 4, f.getName()) && lineTokens[2].charAt(0) == '.') {
					configs[4] = lineTokens[2];
				} else configLoadFailure = true;

				scanner.close();
			} else {
				configs[1] = "assembler.syntax";
				configs[2] = ".asm";
				configs[3] = "compiler.syntax";
				configs[4] = ".comp";
			}


			File assemblerSyntaxFile = new File(f.getPath() + "\\" + configs[1]);
			SyntaxNode assemblerSyntax = null;
			if (!assemblerSyntaxFile.exists()) configLoadFailure = true;
			else {
				assemblerSyntax = SyntaxBuilder.build(assemblerSyntaxFile.getPath(), false);
				if (assemblerSyntax == null) configLoadFailure = true;
			}
			File compilerSyntaxFile = new File(f.getPath() + "\\" + configs[3]);
			SyntaxNode compilerSyntax = null;
			if (!compilerSyntaxFile.exists()) configLoadFailure = true;
			else {
				compilerSyntax = SyntaxBuilder.build(compilerSyntaxFile.getPath(), false);
				if (compilerSyntax == null) configLoadFailure = true;
			}

			String[] validFileNames = { "config.txt", "README.txt", assemblerSyntaxFile.getName(), compilerSyntaxFile.getName() };

			for (String s : f.list()) {
				boolean valid = false;

				for (String t : validFileNames) {
					if (s.equals(t)) valid = true;
				}
				
				if (!valid) {
					configLoadFailure = true;
					break;
				}
			}

			SyntaxConfiguration newConfig = new SyntaxConfiguration(configs, assemblerSyntax, compilerSyntax);

			allConfigs.add(newConfig);
			if (!configLoadFailure) {
				loadedConfigs.add(newConfig);
			} else ErrorManager.printNotification("Syntax configuration \"" + f.getName() + "\" not loaded due to errors in configuration");
		}
		System.out.println("Syntax configuration loading complete!");
	}

	private static boolean validSetting(String[] tokens, String desiredSetting, int settingNumber, String configName) {
		if (tokens[0].equals(desiredSetting) && tokens[1].equals("=") && tokens.length == 3) {
			return true;
		} else {
			return false;
		}
	}

	public static ArrayList<SyntaxConfiguration> getAllConfigurations() { return allConfigs; }
	public static ArrayList<SyntaxConfiguration> getLoadedConfigurations() { return loadedConfigs; }
}