import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

public class SyntaxBuilder {

	private static String[] specialFormats = {"8BV", "3BI"};
	
	// Enumerator "tokenType" specifies different word types in the input file
	enum SyntaxFileToken {
		OPEN_BRACKET,
		CLOSED_BRACKET,
		OPEN_QUOTATION,
		CLOSED_QUOTATION,
		OPEN_CHEVRON,
		CLOSED_CHEVRON,
		OPEN_PARENTHESIS,
		CLOSED_PARENTHESIS,
		COMMA,
		SPECIAL_CASE_NAME,
		SPECIAL_CASE_ESCAPE,
		SPECIAL_CASE_FORMAT
	}
	
	private static final String defaultOpenBlock = "{";
	private static final String defaultCloseBlock = "}";
	private static final String defaultStatementDelimiter = "\\n";
	private static final String defaultTokenDelimiter = " ";

	private static boolean debugMode;
	private static boolean errorsFound;
	private static int currentLine;
	private static boolean betweenQuotes;

	private static String lastInQuotes;
	private static String lastSpecialCaseName;
	
	/*
	 *  Method takes in a file path, debug value, and array of valid special format tokens, and will
	 *  attempt to open the file in the specified path, read it to generate a Keyword heirarchy, then
	 *  return this Keyword hierarchy if there are no errors found in the input file. 
	 *  
	 *  If "debug" is set to "true", a formatted string will be printed to the console which displays 
	 *  this Keyword hierarchy, assuming no errors are found in the input file.
	 */

	public static SyntaxNode build(String filePath, boolean debug) {
		
		// Initialize values of variables
		errorsFound = false;
		int currentLine = 1;
		debugMode = debug;
		lastInQuotes = "";
		lastSpecialCaseName = "";
		Stack<Character> pairTracker = new Stack<Character>();

		ArrayList<Character> syntaxEscapeCharacters = new ArrayList<Character>();
		char[] defaultEscapeCharacters = { '\"', 'n', 't', '\\' };
		for (char c : defaultEscapeCharacters) { syntaxEscapeCharacters.add(c); }

		
		// Creates scanner for file based on the file path passed into this method. If the file is not found,
		// this method throws "FileNotFoundExeception"
		File syntaxFile = new File(filePath);
		Scanner scanner;
		try {
			scanner = new Scanner(syntaxFile);
		} catch (FileNotFoundException e) {
			ErrorManager.printDebugNotification(debugMode, "Error occured when attempting to read \"" + filePath + "\" in syntax configuration folder \"" + syntaxFile.getParent() + "\"");
			return null;
		}

		scanner.useDelimiter("");
		
		// Gets the first line of the syntax file and stores it to "syntaxString". If the file is empty, prints
		// and appropriate error message and returns
		if (!scanner.hasNext()) {
			ErrorManager.printDebugErrorMessage(debugMode, "No data found within file");
			scanner.close();
			return null;
		}

		betweenQuotes = false;
		boolean inSpecialCaseBlock = true;

		char prevChar;
		char currentChar = scanner.next().charAt(0);

		while (isWhitespace(currentChar)) {
			currentChar = scanner.next().charAt(0);
		}

		if (currentChar != '{') {
			ErrorManager.printDebugErrorMessage(debugMode, "Syntax file must start with open bracket");
			scanner.close();
			return null;
		}

		SyntaxFileToken lastToken = SyntaxFileToken.OPEN_BRACKET;

		while (scanner.hasNext()) {

			prevChar = currentChar;
			currentChar = scanner.next().charAt(0);

			if (isWhitespace(currentChar)) {
				if (currentChar == '\n') currentLine++;
				continue;
			}

			System.out.print(currentChar);

			if (betweenQuotes) {

				if (prevChar == '\\') {
					if (currentChar == '\"') {
						lastInQuotes += '\"';
					} else if (currentChar == 'n') {
						lastInQuotes += '\n';
					} else if (currentChar == 't') {
						lastInQuotes += '\t';
					} else if (syntaxEscapeCharacters.contains(currentChar)) {
						lastInQuotes += "\\" + currentChar;
					} else ErrorManager.printDebugErrorMessage(debugMode, "Quotations contain invalid escase character \"\\" + currentChar + "\"", currentLine);
				} else if (currentChar == '\"') {
					switch (lastToken) {
						case OPEN_BRACKET:
						case SPECIAL_CASE_FORMAT:
							lastToken = SyntaxFileToken.SPECIAL_CASE_NAME;
							break;
						default:
							break;
					}
				} else if (currentChar != '\\') lastInQuotes += currentChar;

			} else if (inSpecialCaseBlock) {
				switch (lastToken) {
					case OPEN_BRACKET:
						if (currentChar == '}') {
							inSpecialCaseBlock = false;
						} else attemptOpenQuotes(currentChar, "Open bracket must be followed by specail case specification, or closed bracket");
						break;
					case SPECIAL_CASE_NAME:
						if (currentChar == '(') { lastToken = SyntaxFileToken.OPEN_PARENTHESIS; }
						else if (currentChar == '<') { lastToken = SyntaxFileToken.OPEN_CHEVRON; }
						else ErrorManager.printErrorMessage("Special case name must be followed by a escape indicator or format string", currentLine);
						break;
					case OPEN_PARENTHESIS:
						if (!syntaxEscapeCharacters.contains(currentChar)) {

						} else ErrorManager.printErrorMessage("Specified escape character \'" + currentChar + "\' is already assigned (by default, or earlier in" + 
											"\n\t the syntax file. Escape character must be changed", currentLine);
						break;
					case OPEN_CHEVRON:
						attemptOpenQuotes(currentChar, "Open chevron must be followed by a token in quotes");
						break;
					default:
						break;
				}
			} else {

			}

		}

		scanner.close();
		System.out.println("\n");
		return new SyntaxNode();
	}
	
	// Method returns true if the inputted character is a space character or tab character
	private static boolean isWhitespace(char c) { return (c == ' ' || c == '\t' || c == '\r' || c == '\n'); }

	private static void attemptOpenQuotes(char c, String s) {
		if (c == '\"') {
			lastInQuotes = "";
			betweenQuotes = true;
		} else ErrorManager.printDebugErrorMessage(debugMode, s, currentLine);
	}
	
}
