import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class AccessControlEngine {

	private static final int bFactor = 4;
	private static long start = 0, delta = 0;
	private static int numRoles = 0, numFiles = 0, numUsers = 0;

	public static void main(String[] args) 
	{
		System.out.println("Reading from init.txt...\n");
		Scanner in = null;
		try { in = new Scanner(new File("init1.txt")); }
		catch(FileNotFoundException e) {
			System.out.println("!!! ERROR - init.txt could not be found. !!!");
			System.exit(0);
		}

		AccessControlTree act = null;
		String[] entries;
		String[][] roleInfo, userInfo, fileInfo;

		start = System.currentTimeMillis();
		for(int i = 0; i < 3; i++)								// For each entries in init.txt (roles, users, files)
		{
			entries = in.nextLine().split(",");					// Split up entries of section (separated by commas)
			if(i == 0) 											// If role entries...
			{
				roleInfo = new String[entries.length][3];		// Create object to hold role info (3 Fields: name, parent, permissions)
				for(int j = 0; j < entries.length; j++)			// For each role defined in init.txt...
					roleInfo[j] = entries[j].split(":");		// Split up the entries into name, parent, and permissions
				
				act = new AccessControlTree(new Role(roleInfo[0][0],null,roleInfo[0][2],bFactor));	// First role will always be admin, so initialize Access Control Tree with it as root
				
				for(int j = 1; j < entries.length; j++) { 				// For each role defined in init.txt after admin, add to tree
					Role parent = act.findRole(roleInfo[j][1]);
					Role r = new Role(roleInfo[j][0],parent,roleInfo[j][2],bFactor);
					parent.addChild(r);
					numRoles++;
				}
				System.out.println("\n Number of Roles: " + numRoles +" \n");
			}
			else if (i == 1) {									// If user entries...
				userInfo = new String[entries.length][2];		// Only 2 fields for user (name, role)
				for(int j = 0; j < entries.length; j++)			// For each entry
					userInfo[j] = entries[j].split(":");		// Split entry data into name and role

				for(int j = 0; j < entries.length; j++) {		// For each role
					String[] temp = userInfo[j][1].split("&");	// Split data since multiple role definitions are supported
					for(int k = 0; k < temp.length; k++) {		// For each role 
						Role t = act.findRole(temp[k]);
						t.addUser(userInfo[j][0]);
						numUsers++;
					}
				}
				System.out.println("\n Number of Users: " + numUsers +" \n");
			}
			else {	// File Parsing
				fileInfo = new String[entries.length][2];		// Same thing as users but without multiple role processing
				for(int j = 0; j < entries.length; j++)
					fileInfo[j] = entries[j].split(":");

				for(int j = 0; j < entries.length; j++) {
					Role t = act.findRole(fileInfo[j][1]);
					t.addFile(fileInfo[j][0]);
					numFiles++;
				}
				System.out.println("\n Number of Files: " + numFiles +" \n");
			}
		}

		delta = System.currentTimeMillis() - start;
		System.out.println(delta + " millis to process policy file.");

		in = new Scanner(System.in);
		System.out.println("\nAccess queries must be of the form: access USER:FILE\nFile location queries must be of the form: where f FILE\nUser location queries must be of the form: where u USER\nExit command: exit");

		System.out.print("\nPlease enter a query: ");
		String[] query = in.nextLine().split("\\s");

		start = System.currentTimeMillis();
		if(query[0].compareTo("access") == 0) {
			String[] params = query[1].split(":");
			ArrayList<Role> targets = act.findUser(params[0]);
				
			System.out.print("Checking if " + params[0] + " has access to " + params[1] + "... ");
			AccessControlTree fileSearch;
			for(int i = 0; i < targets.size(); i++) {
				fileSearch = new AccessControlTree(targets.get(i));
				//Change print statement so discovery status printed here instead
				if(fileSearch.findFile(params[1]) != null) {	//fileSearch.findFile(params[1]).getUsers().contains(params[0])
					System.out.println("access allowed");
					break;
				}
				else if(i == targets.size() - 1 && fileSearch.findFile(params[1]) == null) 
				System.out.println("access denied");
			}
		}
		else if(query[0].compareTo("where") == 0) {
			if(query[1].compareTo("u") == 0) {
				//Change print statement so discovery status printed here instaed
				 	ArrayList<Role> userRoles = act.findUser(query[2]);
				 	if(userRoles.size() != 0) {
				 		System.out.print("Found user " + query[2] + " in roles: ");
				 		for(int i = 0; i < userRoles.size(); i++)
							System.out.print(userRoles.get(i).getName() + "  ");
					}
					else System.out.println("User "+query[2]+" does not exist.");
			}
			else if(query[1].compareTo("f") == 0) {
				//Change print statement so discovery status printed here instaed
				Role fileRole = act.findFile(query[2]);
				if(fileRole != null) System.out.println("File " + query[2] + " found in role: " + fileRole.getName());
				else 	  		 	 System.out.println("File could not be found.");
			}
		}
		else if(query[0].compareTo("exit") == 0) {
			System.exit(0);
		}
		else System.out.println("!!! ERROR - invalid query !!!");		
		delta = System.currentTimeMillis() - start;
		System.out.println(delta +" milliseconds to process query.");
	}
}


