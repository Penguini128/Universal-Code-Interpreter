public class ErrorManager {

    private static boolean errorsFound = false;

    public static boolean errorsFound() { return errorsFound; }

    public static void printErrorMessage(String s) {
		System.out.println("ERROR:\t" + s);
		errorsFound = true;
	}
	
	public static void printErrorMessage(String s, int lineNumber) {
		System.out.println("ERROR:\t" + s + " (at line " + lineNumber + ")");
		errorsFound = true;
	}

	public static void printErrorMessage(boolean debug, String s) {
		if (debug) {
 			System.out.println("ERROR:\t" + s);
			errorsFound = true;
		}
	}
	
	public static void printErrorMessage(boolean debug, String s, int lineNumber) {
		if (debug) {
			System.out.println("ERROR:\t" + s + " (at line " + lineNumber + ")");
			errorsFound = true;
		}
	}

	public static void printNotification(String s) {
		System.out.println("(!):\t" + s);
	}
	
	public static void printNotification(String s, int lineNumber) {
		System.out.println("(!):\t" + s + " (at line " + lineNumber + ")");
	}

	public static void printNotification(boolean debug, String s) {
		if (debug) {
 			System.out.println("(!):\t" + s);
		}
	}
	
	public static void printNotification(boolean debug, String s, int lineNumber) {
		if (debug) {
			System.out.println("(!):\t" + s + " (at line " + lineNumber + ")");
		}
	}

}