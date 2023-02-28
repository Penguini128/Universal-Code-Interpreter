import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static boolean debugMode = false;

	private static enum MenuScreen {
		START,
		DEBUG_START,
		DEBUG_FILE_SELECTION,
		EXIT
	}
	
	public static void main(String[] args) {

		System.out.println();
		SettingsManager.loadSettings();
		System.out.println();

		int selectedOption = promptConfigSelection();

		if (selectedOption == 0) return;

		if (debugMode) {
			SyntaxConfiguration selectedFolder = SettingsManager.getAllConfigurations().get(selectedOption - 1);
			System.out.println("Syntax configuration \"" + selectedFolder.getConfigName() + "\" has been selected\n");
			if (selectedFolder.getConfigTxtErrors()) System.out.println("File \"config.txt\" in \"" + selectedFolder.getConfigName() + "\" contains errors");
		} else {

		}
	}

	private static int promptConfigSelection() {

		ArrayList<SyntaxConfiguration> validConfigs = SettingsManager.getLoadedConfigurations();
		ArrayList<SyntaxConfiguration> allConfigs = SettingsManager.getAllConfigurations();

		MenuScreen currentMenu = MenuScreen.START;
		SyntaxConfiguration currentSyntaxConfiguration = null;

		int input = -2;

		System.out.println("To select and option, type the corresponding number next to the option and press enter\nAt any point, enter 0 to return to the previous prompt/exit the program\n");

		if (SettingsManager.getLoadedConfigurations().size() == 0) {
			System.out.println("No valid syntax configurations found. Please enter -1 to enter debug mode, or press enter to end the program\n");
			if (scanner.nextLine().equals("-1")) {
				System.out.println("Debug mode enabled\n\nPlease select one of the following syntax configurations to debug:\n");
				currentMenu = MenuScreen.DEBUG_START;
				debugMode = true;
			} else {
				currentMenu = MenuScreen.EXIT;
			}
		} else System.out.println("Please select one of the following syntax configurations:\n");

		while (currentMenu != MenuScreen.EXIT) {
			
			switch (currentMenu) {
				case START:
					for (int i = 0; i < validConfigs.size(); i++) {
						System.out.println((i + 1) + ". " + validConfigs.get(i));
					}
					break;
				case DEBUG_START:
					for (int i = 0; i < allConfigs.size(); i++) {
						System.out.print((i + 1) + ". " + allConfigs.get(i));
						boolean isValid = false;
						for (int j = 0; j < validConfigs.size(); j++) {
							if (allConfigs.get(i).equals(validConfigs.get(j))) {
								isValid = true;
							}
						}
						if (!isValid) System.out.print(" (!)");
						System.out.println();
					}
					break;
				case DEBUG_FILE_SELECTION:
					if (currentSyntaxConfiguration.getConfigTxtErrors())
						System.out.println("File \"config.txt\" in \"" + currentSyntaxConfiguration + "\" contains errors");
					break;
				default:
					break;
			}
	
			System.out.println();
			try {
				input = scanner.nextInt();
			} catch (InputMismatchException e) {
				input = -2;
			}

			switch (currentMenu) {
				case START:
					if (input < -1 || input > validConfigs.size()) input = -2;
					if (input == -2) System.out.println("Invalid input. Please try again\n");
					else if (input == 0) currentMenu = MenuScreen.EXIT;
					else if (input == -1) {
						System.out.println("Debug mode enabled\n\nPlease select one of the following syntax configurations to debug:\n");
						debugMode = true;
						currentMenu = MenuScreen.DEBUG_START;
					} else {

					}
					break;
				case DEBUG_START:
					if (input < -1 || input > allConfigs.size()) input = -2;
					if (input == -2) System.out.println("Invalid input. Please try again\n");
					else if (input == 0) currentMenu = MenuScreen.EXIT;
					else if (input == -1) {
						System.out.println("Debug mode disabled\n\nPlease select one of the following syntax configurations:\n");
						debugMode = false;
						currentMenu = MenuScreen.START;
					} else {
						currentSyntaxConfiguration = SettingsManager.getAllConfigurations().get(input - 1);
						System.out.println("Syntax configuration \"" + currentSyntaxConfiguration.getConfigSettings().getConfigName() + "\" has been selected\n");
						currentMenu = MenuScreen.DEBUG_FILE_SELECTION;
					}
					break;
				case DEBUG_FILE_SELECTION:
					if (input != 0) System.out.println("Invalid input. Please try again\n");
					if (input == 0) {
						System.out.println("Returning to syntax cofiguration selection\n\nPlease select one of the following syntax configurations:\n");
						currentMenu = MenuScreen.DEBUG_START;
					}
					break;
				default:
					break;
			}

		}

		System.out.println("Exiting program");
		return input;

	}

}
