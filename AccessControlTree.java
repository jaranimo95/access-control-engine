import java.util.ArrayList;

public class AccessControlTree 
{
	// Tree Parameters
	private Role root;
	private int recurseLevels = 0;
	
	public AccessControlTree() {
		this.root = null;
	}

	public AccessControlTree(Role root) {
		this.root = root;
	}

	public void setRoot(Role r) {
		root = r;
	}

	public Role getRoot() {
		return root;
	}

	//public Role findUser(String name) {}
	//public Role findFile(String name) {}

	public Role findRole(String name) {
		//System.out.println("Current Root: " + root.getName() + "\nChecking for role: " + name+"\n");
		Role ret = findRoleHelper(root,name);
		if(ret == null) System.out.println("\n\n!!! ERROR - role could not be found. !!!\n\n");
		//else 			System.out.println("Found role " +ret.getName());
		
		return ret;
	}

	private Role findRoleHelper(Role role, String name) 
	{
		//System.out.println("At role: "+role.getName()+", # children: "+role.getNumChildren());
		if(role.getName().compareTo(name) != 0) 	// If name of current role isn't the one requested, traverse children of current role
		{
			if(role.getNumChildren() == 0) return null;
			for(int i = 0; i < role.getNumChildren(); i++) {
				Role temp = findRoleHelper(role.getChild(i),name);	// Traverse to next child
				if(temp != null) return temp;
			}
			return null;
		}				
		return role;	// Else name of role == requested name, return current role.
	}


	// findUserHelper Fields
	private ArrayList<Role> userRoles = new ArrayList<Role>();
	private int userNum = 0;

	public ArrayList<Role> findUser(String name) {
		findUserHelper(root,name);
		return userRoles;
	}

	private void findUserHelper(Role role, String name) 
	{	
		if(userNum > 4) return;
		//System.out.println("At role: "+role.getName()+", # children: "+role.getNumChildren()+", # user instances: "+userNum);
		if(role.getUsers().contains(name)) {
			userRoles.add(role);
			userNum++;
		}
		if(role.getNumChildren() == 0) return;

		for(int i = 0; i < role.getNumChildren(); i++)
			findUserHelper(role.getChild(i),name);	// Traverse to next child
		return;	// Else name of role == requested name, return current role.
	}

	// findFile Fields
	private Role someRole = null;
	private boolean found = false;

	public Role findFile(String file) {
		findFileHelper(root,file);
		return someRole;
	}

	private void findFileHelper(Role role, String name) 
	{
		//System.out.println("At role: "+role.getName()+", # children: "+role.getNumChildren());
		if(role.getFiles().contains(name)) {
			someRole = role;
			found = true;
			return;
		}
		if(role.getNumChildren() == 0) return;
		for(int i = 0; i < role.getNumChildren(); i++) {
			findFileHelper(role.getChild(i),name);
			if(found) return;
		}
		return;
	}
/*
	public void run() 
	{
		Role a = new Role("a",new Role(),"rw",3);
		AccessControlTree act = new AccessControlTree(a);
		Role b = new Role("b",a,"rw",3);
		Role c = new Role("c",a,"rw",3);
		Role d = new Role("d",b,"rw",3);
		Role e = new Role("e",b,"rw",3);
		Role f = new Role("f",c,"rw",3);

		a.addChild(b);
		a.addChild(c);
		b.addChild(d);
		b.addChild(e);
		c.addChild(f);

		a.printRole();
		b.printRole();
		c.printRole();

		act.findRole("e");
	}

	public static void main(String[] args) {
		AccessControlTree a = new AccessControlTree();
		a.run();
	}
*/
}


