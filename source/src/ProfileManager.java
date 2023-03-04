import java.io.File;
import java.util.ArrayList;

public class ProfileManager {

	public static ArrayList<SyntaxProfile> allProfiles = new ArrayList<SyntaxProfile>();
    public static ArrayList<SyntaxProfile> validProfiles = new ArrayList<SyntaxProfile>();

    public static boolean loadProfiles() {
        
		String syntaxFolderName = "syntax_profiles";
		File syntaxFolder = new File(System.getProperty("user.dir") + "\\" + syntaxFolderName);

		if (!(syntaxFolder.exists() && syntaxFolder.isDirectory())) {
			ErrorManager.printErrorMessage("Folder \"" + syntaxFolder + "\" not found. This folder must be restored\n");
			return false;
		}

		ArrayList<File> allSyntaxFolders = new ArrayList<File>();

		for (File f : syntaxFolder.listFiles()) {
			if (f.isDirectory()) {
				allSyntaxFolders.add(f);
			} else ErrorManager.printNotification("Unexpected file \"" + f.getName() + "\" found in syntax profile folder. This file "
												+ "will not affect program functionality, but should be removed");	
		}

		for (File f : allSyntaxFolders) {

			SyntaxProfile currentProfile = new SyntaxProfile(f);
			allProfiles.add(currentProfile);

			if (!currentProfile.isValid())  {
				ErrorManager.printNotification("Syntax profile \"" + f.getName() + "\" not loaded due to errors in configuration");
				continue;
			} else {
				validProfiles.add(currentProfile);
			}
		}

		System.out.println("Syntax profile loading complete!");
		return true;
	}
}