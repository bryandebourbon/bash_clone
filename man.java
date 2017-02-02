import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.StringBuilder;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * The <code>man</code> class represents man command objects.
 * It allows the user to get help with regards to a specific command
 * (itself included!)
 *
 */
public class man extends Command {

	public man(int[] numParams){
		super(numParams);
	}

	/**
	 * Finds the appropriate help information for the given command
	 * from the navigation file.
	 *
	 * @param command, the command whose information is to be displayed
	 * @return output, which is information about the command from the file
	 * @throws IOException, if there is an exception while reading the file
	 */
	public String findText (String command) throws IOException{
		String output = "";
		boolean foundCommand = false;

		// this needs to work regardless of the user's unique directory
		FileInputStream fstream = new FileInputStream("src/manOutput.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));


	    try {
	        StringBuilder builder = new StringBuilder();
	        String line = br.readLine();

	        while (line != null && !(line.equals("}") && foundCommand)) {
	        	if (line.equals(command)){
	        		foundCommand = true;
	        		line = br.readLine();

	        	} if (foundCommand && !line.equals("{")){
	        		builder.append(line + "\n");
	        		line = br.readLine();

	        	} else{
	        		line = br.readLine();
	        	}

	        }
	        output = builder.toString();
	    }catch (IOException i) {
	    	System.out.println("Error while reading navigation file!");
	    }finally {
	        br.close();
	    }

		return output;
	}

	/**
	 * Displays information about a certain command, including what
	 * the command is used for, as well as appropriate syntax for using the
	 * command.
	 *
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
			  throws Exception{

		super.checkParams(parameters);
		String command = parameters[1];
		String output = "Manual for the [" + command + "] command. "
				+ "Please recall that all commands can take chevrons"
				+ " to redirect any output to a file.\n\n";
		HashMap<String, Command> strToCommand = shell.getStrToCommand();

		if (strToCommand.keySet().contains(command)){
			output += (this.findText(command));
		}
		else{
			throw new InvalidParameterException("Error! " + command +
					" is not a valid command!");
		}

		return super.passToEcho(shell, input, output);

	}

}
