import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.*;

/**
 * The <code>grep</code> class represents grep command objects.
 *
 * The grep command allows the user to search for words matching a regex in
 * contents and prints the lines containing the matches
 *
 */
public class grep extends Command {

	/**
	 * Creates an <code>grep</code> object.
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public grep(int[] numParams){
		super(numParams);
	}
	//recursively searches items in item or item contents if item is File

	/**
	 * Returns fileLines, a HashMap where keys are file paths and values
	 * are lines (in the files) which match the given regular expression
	 *
	 * @param shell, the current JShell object
	 * @param regex, the regular expression to be searched for
	 * @param item, the item that is to be searched
	 * @param fileLines, the HashMap where the matches are inserted,
	 * is returned at the end
	 */
	public HashMap <String, ArrayList<String>> searchContent(JShell shell, String regex, Item item, HashMap <String, ArrayList<String>> fileLines) {

		ArrayList<String> lines = new ArrayList<String>();
		if (item instanceof File) {
			// Array with all the lines in the File
			String[] contentLines = ((File)item).getContents().split("\\\\n"); //splits the lines

			Pattern p = Pattern.compile(regex);

			for(int i = 0; i < contentLines.length; i++){
				Matcher m = p.matcher(contentLines[i]);
				if (m.find()) {
					lines.add(contentLines[i]);
				}
			}
			// keys are file paths and value is array with lines which match regex
			fileLines.put(item.getPathway(), lines);
		}

		else if (item instanceof DIR) {
			// Recursively search the files in DIR and its descendants
			for (Item component : ((DIR)item).getSubContents().values()) {
				searchContent(shell, regex, component, fileLines);
			}
		}
		return fileLines;
	}

	/**
	 *
	 */
	public String looper(JShell shell, String[] parameters, int i, Object[] params)
	throws FileNotFoundException {

		if (parameters.length == 1){
			throw new InvalidParameterException("Error! grep takes a list of parameters!");
		}

		HashMap <String, ArrayList<String>> fileLines = new HashMap <String, ArrayList<String>> ();

		Item searchItem = shell.getCurDir().followPath(shell,  parameters[i+1]);
		HashMap <String, ArrayList<String>> searchResult = searchContent(shell, (String)params[1], searchItem, fileLines);
		String output = "";

		for (Map.Entry<String, ArrayList<String>> entry : searchResult.entrySet()) {

			String path = entry.getKey();
			ArrayList<String> lines = entry.getValue();

			// For the lines in values of each key (file) print file path
			//and then colon and then each line (repeats process for each line)
			for (String s : lines) {
				if ((Boolean) params[0]){
					output += path + ": ";

				}
				output += s + "\n";

			}
		}
		return output;
	}

	/**
	 * Finds and returns lines in files that contain the given
	 * Regex pattern.
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
	throws Exception {

		Object [] extractedParams = super.extractOptParam(input, "-R");
	    parameters =  (String[])extractedParams[1];
	    String optionalParam = (String)extractedParams[0];
		boolean paramPresent = optionalParam.equals("-R");


		String output =  super.exeForList(shell, parameters, input, parameters.length - 1, new Object[]{paramPresent, parameters[1]});


		return super.passToEcho(shell, input, output);

	}


}
