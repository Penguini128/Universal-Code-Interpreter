import java.io.File;
import java.util.Scanner;
public class Main {

	private static Scanner scanner = new Scanner(System.in);

	private static enum MenuScreen {
		START,
		DEBUG_START,
		DEBUG_PROFILE_ERRORS,
		DEBUG_PROFILE_FILE_SELECTION,
		DEBUG_FILE_ERRORS,
		EXIT
	}
	
	public static void main(String[] args) {

		System.out.println();
		if (!ProfileManager.loadProfiles()) {
			System.out.println("Press enter to terminate the program");
			scanner.nextLine();
			return;
		}
		System.out.println();

		int selectedOption = promptConfigSelection();

		if (selectedOption == 0) return;

	}

	private static int promptConfigSelection() {

		MenuScreen currentMenu = MenuScreen.START;
		SyntaxProfile currentProfile = null;
		File currentFile = null;
		boolean currentFileContainsErrors = false;

		int input = -2;

		System.out.println("To select and option, type the corresponding number next to the option and press enter\nAt any point, enter 0 to return to the previous prompt/exit the program\n");

		if (ProfileManager.validProfiles.size() == 0) {
			System.out.println("No valid syntax profiles found. Please enter -1 to enter debug mode, or press enter to end the program\n");
			if (scanner.nextLine().equals("-1")) {
				System.out.println("Debug mode enabled\n\nPlease select one of the following syntax profiles to debug:");
				currentMenu = MenuScreen.DEBUG_START;
			} else {
				currentMenu = MenuScreen.EXIT;
			}
		} else System.out.println("Please select one of the following syntax profiles:");

		while (currentMenu != MenuScreen.EXIT) {
			System.out.println();
			switch (currentMenu) {
				case START:
					for (int i = 0; i < ProfileManager.validProfiles.size(); i++) {
						System.out.println((i + 1) + ". " + ProfileManager.validProfiles.get(i));
					}
					break;
				case DEBUG_START:
					for (int i = 0; i < ProfileManager.allProfiles.size(); i++) {
						System.out.print((i + 1) + ". " + ProfileManager.allProfiles.get(i));
						boolean isValid = false;
						for (int j = 0; j < ProfileManager.validProfiles.size(); j++) {
							if (ProfileManager.allProfiles.get(i).equals(ProfileManager.validProfiles.get(j))) {
								isValid = true;
							}
						}
						if (!isValid) System.out.print(" (!)");
						System.out.println();
					}
					break;
				case DEBUG_PROFILE_ERRORS:
						System.out.println("Please fix the above errors, then press enter to view updated error list, or enter 0 to go back");
					break;
				case DEBUG_PROFILE_FILE_SELECTION:
					System.out.println("Please select one of the following syntax files to debug:\n\n"
									+  "1. " + currentProfile.getAssemblerSyntaxFile().getName() + "\n"
									+  "2. " + currentProfile.getCompilerSyntaxFile().getName());
					break;
				case DEBUG_FILE_ERRORS:
					SyntaxBuilder.build(currentFile.getPath(), true);
					/*
					 * eventually must replace false with a "does file contain errors" test
					 */
					if (false) {
						currentFileContainsErrors = true;
					} else {
						System.out.println("No errors found!\n\nPress enter to return to syntax file selection");
						currentFileContainsErrors = false;
					}
					break;
				default:
					break;
			}
	
			System.out.println();
			try {
				input = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				input = -2;
			}

			switch (currentMenu) {
				case START:
					if (input < -1 || input > ProfileManager.validProfiles.size()) input = -2;
					if (input == -2) System.out.println("Invalid input. Please try again");
					else if (input == 0) currentMenu = MenuScreen.EXIT;
					else if (input == -1) {
						System.out.println("Debug mode enabled\n\nPlease select one of the following syntax profiles to debug:");
						currentMenu = MenuScreen.DEBUG_START;
					} else {

					}
					break;
				case DEBUG_START:
					if (input < -1 || input > ProfileManager.allProfiles.size()) input = -2;
					if (input == -2) System.out.println("Invalid input. Please try again");
					else if (input == 0) currentMenu = MenuScreen.EXIT;
					else if (input == -1) {
						System.out.println("Debug mode disabled\n\nPlease select one of the following syntax profiles:");
						currentMenu = MenuScreen.START;
					} else {
						currentProfile = ProfileManager.allProfiles.get(input - 1);
						System.out.println("Syntax profile \"" + currentProfile + "\" has been selected\n");
						if (!currentProfile.isValid()) {
							System.out.println("Syntax profile \"" + currentProfile + "\" contains the following errorrs:");
							currentProfile.reloadProfile(true);
							currentMenu = MenuScreen.DEBUG_PROFILE_ERRORS;
						} else {
							System.out.println("Syntax profile \"" + currentProfile + "\" contains no errors.");
							currentMenu = MenuScreen.DEBUG_PROFILE_FILE_SELECTION;			
						}
					}
					break;
				case DEBUG_PROFILE_ERRORS:
					if (input != 0) {
						if (!currentProfile.getSyntaxConfig().isValid()) {
							System.out.println("Reloading syntax profile\n");
							currentProfile.reloadProfile(false);
							if (currentProfile.isValid()) {
								System.out.println("All errors fixed!");
								currentMenu = MenuScreen.DEBUG_PROFILE_FILE_SELECTION;
							} else {
								System.out.println("Syntax profile \"" + currentProfile + "\" contains the following errorrs:");
								currentProfile.reloadProfile(true);
							}
						}
					} else if (input == 0) {
						System.out.println("Returning to syntax profile selection\n\nPlease select one of the following syntax profiles:");
						currentMenu = MenuScreen.DEBUG_START;
					}
					break;
				case DEBUG_PROFILE_FILE_SELECTION:
					if (input == 1) {
						currentFile = currentProfile.getAssemblerSyntaxFile();
						System.out.println("Syntax file \"" + currentFile.getName() + "\" contains the following errors:");
						currentMenu = MenuScreen.DEBUG_FILE_ERRORS;
					} else if (input == 2){
						currentFile = currentProfile.getCompilerSyntaxFile();
						System.out.println("Syntax file \"" + currentFile.getName() + "\" contains the following errors:");
						currentMenu = MenuScreen.DEBUG_FILE_ERRORS;
					} else if (input == 0) {
						System.out.println("Returning to syntax profile selection\n\nPlease select one of the following syntax profiles:");
						currentMenu = MenuScreen.DEBUG_START;
					} else {
						System.out.println("Invalid input. Please try again");
					}
					break;
				case DEBUG_FILE_ERRORS:
					if (input == 0 || !currentFileContainsErrors) {
						System.out.println("Returning to syntax file selection");
						currentMenu = MenuScreen.DEBUG_PROFILE_FILE_SELECTION;
					} else {
						System.out.println("Reloading syntax file\n\nSyntax file \"" + currentFile.getName() + "\" contains the following errors:");
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
