import java.util.ArrayList;

public class Role 
{
	// Role Parameters
	private String name = "";									// Name of role
	private String permissions = "";								// Permissions associated with role
	private ArrayList<String> users = new ArrayList<String>(), 	// List of users with current role priviledges
					 	 	  files = new ArrayList<String>();			// List of files accessible to current role & above
	private int bFactor = 0;
	private Role parent = null;									// Superior role
	private Role[] children = null;								// Inferior roles
	private int numUsers = 0, numFiles = 0, numChildren = 0;		// Number of users, files within role & children of role

	public Role()
	{
		this.name = null;
		this.permissions = null;
		this.bFactor = 0;
		this.parent = null;
		this.children = null;
	}

	public Role(String name, Role parent, String permissions, int bFactor) {
		this.name = name;
		this.parent = parent;
		this.permissions = permissions;
		this.bFactor = bFactor;
		this.children = new Role[bFactor];
	}
	
	public Role(String name, Role parent, String permissions, int bFactor, ArrayList<String> users, ArrayList<String> files)
	{
		this.name = name;	// Set name of role

		// Define permissions of role
		this.permissions = permissions;

		// Initialize users of role
		this.users.addAll(users);
		numUsers += users.size();

		// Initialize files accessible by role
		this.files.addAll(files);
		numFiles += files.size();

		this.bFactor = bFactor;
		this.parent = parent;
		this.children = new Role[bFactor];
	}

	public String getName() {
		return name;
	}

	public String getPermissions() {
		return permissions;
	}

	public Role getParent() {
		return parent;
	}

	public Role[] getChildren() {
		return children;
	}

	public Role getChild(int index){
		if(index >= numChildren) {
			System.out.println("\n\n!!! ERROR - Invalid child index. !!!\n\n");
			return null;
		}
		return children[index];
	}

	public int getNumChildren() {
		return numChildren;
	}

	public int getBranchFactor() {
		return bFactor;
	}

	public ArrayList<String> getUsers() {
		return users;
	}

	public ArrayList<String> getFiles() {
		return files;
	}

	//Modify to step through user list instead of array
	public boolean addUser(String name) 
	{
		// Check if user already exists in list
		for(int i = 0; i < numUsers; i++) {	
			if(name.compareTo(users.get(i)) == 0){
				System.out.println("\n\n!!! ERROR - User already exists. !!!\n");
				return false;
			}
		}
		users.add(name); // Then add to list
		numUsers++;		 // Increment number of users
		System.out.println("User "+name+" added to "+this.name+"!");
		return true;
	}

	public boolean addUsers(ArrayList<String> names) {
		users.addAll(names); 		// Then add to list
		numUsers += names.size();	// Increment number of users
		return true;
	}

	public String removeUser(String name, Role role) 
	{
		// Check if user exists in list
		for(int i = 0; i < numUsers; i++) {	
			if(name.compareTo(users.get(i)) == 0){
				String temp = users.get(i);
				users.remove(temp);
				numUsers--;		 	// Decrement number of users
				return temp;
			}
		}
		System.out.println("\n\n!!! ERROR - User does not exist. !!!\n");
		return null;
	}

	public boolean addFile(String name)
	{
		// Check if file already exists in list
		for(int i = 0; i < numFiles; i++) {	
			if(name.compareTo(files.get(i)) == 0){
				System.out.println("\n\n!!! ERROR - File already exists. !!!\n");
				return false;
			}
		}
		files.add(name); // Then add to list
		numFiles++;		 // Increment number of files
		System.out.println("File "+name+" added to "+this.name+".");
		return true;
	}

	public boolean addFiles(ArrayList<String> names) {
		files.addAll(names); 		// Then add to list
		numFiles += names.size();	// Increment number of users
		return true;
	}

	public String removeFile(String name, Role role) 
	{
		// Check if user exists in list
		for(int i = 0; i < numFiles; i++) {	
			if(name.compareTo(files.get(i)) == 0){
				String temp = files.get(i);
				files.remove(temp);
				numFiles--;		 	// Decrement number of users
				return temp;
			}
		}
		System.out.println("\n\n!!! ERROR - File does not exist. !!!\n");
		return null;
	}

	public boolean addChild(Role role)
	{
		if (numChildren < bFactor) children[numChildren++] = role;	// Add child to parent role if max children not reached	
		else {
			System.out.println("\n\n!!! ERROR - child capacity reached. !!!\n\n");
			return false;
		}
		return true;
	}

	public void printRole() {
		System.out.print("\n"+ name + "\nParent: "+ parent.getName() +"\nPermissions: "+permissions+"\nBranching Factor: "+bFactor+"\nChildren: ");
		for(int i = 0; i < numChildren; i++){
			if(i != numChildren - 1) System.out.print(children[i].getName()+", ");
			else System.out.println(children[i].getName()+".");
		}
	}

	public Role copy() {
		Role r = new Role(name,parent,permissions,bFactor);
		for(int i = 0; i < numChildren; i++)
			r.addChild(children[i]);
		return r;
	}

	/*public void run() {
		Role src = new Role();
		Role a = new Role("a",src,"a",4);
		Role b = new Role("b",a,"r",4);
		a.addChild(b);
		Role c = new Role("c",a,"r",4);
		a.addChild(c);

		a.printRole();
		b.printRole();
		c.printRole();
	}

	public static void main(String[] args) {
		Role role = new Role();
		role.run();
	}*/

		//private void changeUserRole(Role caller, Role target, String user) {}
		//private void changeFileRole(Role caller, Role target, String file) {}
		//private void changeRolePerms(Role caller, char[] permissions) {}
}

