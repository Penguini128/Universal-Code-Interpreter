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

    public static boolean loadSettings() {
        
		File syntaxConfigFolder = new File(System.getProperty("user.dir") + "\\syntax_configurations");

		if (syntaxConfigFolder.exists() && syntaxConfigFolder.isDirectory()) {
		} else {
			ErrorManager.printErrorMessage("Folder \"syntax_configurations\" not found. This folder must be restored\n");
			return false;
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

			ConfigSettings config = ConfigSettings.load(configFile, false);

			if (!config.isValid()) {
				SyntaxConfiguration newConfig = new SyntaxConfiguration(config, null, null);
				allConfigs.add(newConfig);
				ErrorManager.printNotification("Syntax configuration \"" + f.getName() + "\" not loaded due to errors in configuration");
				continue;
			}

			boolean configLoadFailure = false;


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

			validateFiles(f.list(), assemblerSyntaxPath, compilerSyntaxPath, false);

			SyntaxConfiguration newConfig = new SyntaxConfiguration(config, assemblerSyntax, compilerSyntax);

			allConfigs.add(newConfig);
			if (!configLoadFailure) {
				loadedConfigs.add(newConfig);
			} else ErrorManager.printNotification("Syntax configuration \"" + f.getName() + "\" not loaded due to errors in configuration");
		}
		System.out.println("Syntax configuration loading complete!");
		return true;
	}	

	private static boolean validSetting(String[] tokens, String desiredSetting, int settingNumber, String configName) {
		if (tokens[0].equals(desiredSetting) && tokens[1].equals("=") && tokens.length == 3) {
			return true;
		} else {
			return false;
		}
	}

	
	private static boolean validateFiles(String[] files, String assemblerName, String compilerName, boolean showErrors) {

		boolean invalidFiles = false;
		String[] validFileNames = { "config.txt", "README.txt", assemblerName, compilerName };

		for (String s : files) {
			boolean valid = false;

			for (String t : validFileNames) {
				if (s.equals(t)) valid = true;
			}
			
			if (!valid) {
				ErrorManager.printErrorMessage(showErrors, "Extraneous file \"" + s + "\" found. Please remove this file");
				invalidFiles = true;
			}
		}

		return invalidFiles;
	}
	

	public static ArrayList<SyntaxConfiguration> getAllConfigurations() { return allConfigs; }
	public static ArrayList<SyntaxConfiguration> getLoadedConfigurations() { return loadedConfigs; }
}