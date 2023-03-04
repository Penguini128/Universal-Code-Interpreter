import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {

	private static Scanner scanner = new Scanner(System.in);

	private static enum MenuScreen {
		START,
		DEBUG_START,
		DEBUG_FILE_SELECTION,
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
				case DEBUG_FILE_SELECTION:
					System.out.println("Test debug file selection message");
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
						}
						currentMenu = MenuScreen.DEBUG_FILE_SELECTION;
					}
					break;
				case DEBUG_FILE_SELECTION:
					if (input != 0) System.out.println("Invalid input. Please try again");
					if (input == 0) {
						System.out.println("Returning to syntax profile selection\n\nPlease select one of the following syntax profiles:");
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
