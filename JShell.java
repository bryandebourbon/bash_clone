

import java.util.*; //to import Scanner, Regex, and HashMap functionality


/**
 * The <code>JShell</code> class represents objects that are mock Unix shells.
 * It allows the user to use various Unix commands and executes them in a mock
 * file system environment.
 *
 * A <code>JShell</code> object includes the ability to prompt users.
 *
 * @author (Rubens Mukja, Laura McIntyre, Rohan Das, and Bryan de Bourbon)
 * @version 2.0
 * @see java.util.HashMap<K,V>
 */
public class JShell {

	private DIR rootDir;
	private DIR curDir;

	//the message desired to be displayed before prompt
	private String navigator;
	private HashMap <String, Command> strToCommand;

	/**
	 * Creates a <code>JShell</code> object, which is a mock Unix Shell.
	 * This object knows its root and current directory as well as the commands
	 * it can execute.
	 *
	 * @return void
	 * @see java.util.HashMap<K,V>
	 */
	public JShell() {

		this.rootDir = new DIR("root", "root");
		this.setCurDir(rootDir);
		this.navigator = (this.rootDir.getName() +"/# ");

		/* this HashMap allows for an elegant use of Dynamic Binding, where
		 * the string of the Command can be mapped to any class that inherits
		 * from the command object (see final report).
		 */
		this.strToCommand = new HashMap <String, Command>();
		//the declaration of a command with no parameters means that there
		//is no intention to restrict on the amount of parameters
		this.strToCommand.put("mkdir", new mkdir(new int[]{}));
		this.strToCommand.put("cd", new cd(new int[]{1,3}));
		this.strToCommand.put("ls", new ls(new int[]{}));
		this.strToCommand.put("pwd",new pwd(new int[]{0,2}));
	    this.strToCommand.put("mv", new mv(new int[]{2,4}));
		this.strToCommand.put("cp", new cp(new int[]{2,4}));
		this.strToCommand.put("cat", new cat( new int[]{1,3}));
		this.strToCommand.put("echo", new echo(new int[]{1,3}));
		this.strToCommand.put("rm", new rm(new int[]{}));
		this.strToCommand.put("find", new find(new int[]{}));
		this.strToCommand.put("grep", new grep(new int[]{}));
		this.strToCommand.put("get", new get(new int[]{1, 3}));
		this.strToCommand.put("man", new man(new int[]{1, 3}));
		// exit is include so the man command can navigate correctly with "man exit"
		this.strToCommand.put("exit", new man(new int[]{1, 3}));
	}

	/**
	 * Returns the root folder of the mock file system
	 *
	 * @return root folder
	 */
	public DIR getRootDir() {
		return this.rootDir;
	}

	/**
	 * Returns the current folder that the mock file system is working
	 * with
	 *
	 * @return the current folder
	 */
	public DIR getCurDir() {
		return this.curDir;
	}

	/**
	 * Sets the inputed DIR object with the current folder that
	 * the mock file system is working with
	 *
	 * @param curDIR  the folder that desired to be the current folder
	 * @return void
	 */
	public void setCurDir(DIR curDir) {
		this.curDir = curDir;
	}

	/**
	 * Returns the navigator that the mock file system displays before prompt
	 *
	 * @return the current navigator message
	 */
	public String getNavigator() {
		return this.navigator;
	}

	/**
	 * Sets the inputed String object with the prompt symbol
	 * (known as the navigator) that the mock file system is working
	 * with
	 *
	 * @param navigator the message desired to be displayed before prompt
	 * @return void
	 */
	public void setNavigator(String navigator) {
		this.navigator = navigator;
	}

	/**
	 * Returns the strToCommand HashMap that the mock file system uses to
	 * reference known commands
	 *
	 * @return the strToCommand HashMap
	 */
	public HashMap<String, Command> getStrToCommand() {
		return this.strToCommand;
	}

	/**
	 * Prompts the user, then obtains user input and returns an
	 * <code>Object Array</code> containing the formatted input
	 * (<code>parameters</code>) and raw <code>input</code>. at indices 0 and
	 * 1 respectively. Note that the elements of this
	 * <code>Object Array</code> must be casted as their respective types
	 * before use (please find more about this in the final report).
	 *
	 * Note to the Marker:
	 * (1)The Scanner object is a parameter instead creating this in
	 * the function because this method is used in a loop (see more
	 * about this in the final report).
	 * (2) returning input is necessary for the echo command
	 * (see final report)
	 *
	 * @see (java.util.Scanner)
	 * @param in  a Scanner object (see "Note to the Marker")
	 * @return an Object[] that contains the formatted input and raw input
	 */
	public Object[] promptUser(Scanner in){

	  System.out.print(this.navigator);
	  String input = in.nextLine();
	  String[] parameters = input.trim().split("\\s+");

	  return new Object[]{parameters, input};

	 }

	 public static void main (String args[]) {

		 JShell shell = new JShell();

		 //prompts the user and stores the formatted parameters
		 Scanner in = new Scanner(System.in);
		 Object[] prompt = shell.promptUser(in);
		 String[] parameters = (String[])prompt[0];
		 String commandName = parameters[0];

		 //the program will terminate when the command is exit
		 while (!commandName.equals("exit")){

			 //checks whether the JShell can recognize the command
			 if (shell.getStrToCommand().containsKey(commandName) ) {

				 /* try-catch statements are used here to allow for modularity
				  * when all errors are handled in one central place
				  * (see final report)
				  */
				 try {
					 /* Utilizes dynamic binding to call the "exe" method of
					  * the function that is associated with the particular
					  * commandName (Note that raw input must be passed.
					  * See final report).
					  */
					 System.out.print(shell.getStrToCommand().
							 get(commandName).exe(shell, parameters,
									 //prompt[1] is the raw input
									 (String)prompt[1]));


					 //to catch recursion errors and other exceptions
				 }catch (Throwable e) {
					 String message = e.getMessage();
					 if (message.contains("!")){
						 System.out.println(message);

					 } else{
						 System.out.println("Error! Invalid Entry!");
						 e.printStackTrace();
					 }

				 }

		   //any input that is not a command or empty space
			 } else if (!commandName.equals("")){
				 System.out.println("Error! Invalid Command!");
			 }

			 prompt = shell.promptUser(in);
			 parameters = (String[])prompt[0];
			 commandName = parameters[0];
		 }

	 }


 }
