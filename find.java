import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.*;

public class find extends Command {
    public find(int[] numParams){
        super(numParams);
    }

    //Recursively search file names in given item
    public ArrayList<String> search(JShell shell, String fileName, Item item, ArrayList<String> searchResult) {

    	Pattern fileNamePattern = Pattern.compile(fileName);
        Matcher fileNameMatcher = fileNamePattern.matcher(item.getName());

        if (item instanceof File) {
        	//Add file name to array if it matches regex
            if (fileNameMatcher.find()) {
                searchResult.add(item.getPathway());
            }
        }

        // Recursively apply the function to contents of DIR
        else if (item instanceof DIR) {
            for (Item component : ((DIR)item).getSubContents().values()) {
                this.search(shell, fileName, component, searchResult);
            }
        }

        return searchResult;
    }


    public String exe(JShell shell, String[] parameters, String input)
                            throws Exception {



        String fileName = parameters[1];
        String output =  super.exeForList(shell, parameters, input, parameters.length -1, new Object[]{fileName});


		return super.passToEcho(shell, input, output);
    }


	public String looper(JShell shell, String[] parameters, int i, Object[] params)

			throws FileNotFoundException {

		ArrayList<String> searchResult = new ArrayList<String>();

		if (parameters.length == 1){
			throw new InvalidParameterException("Error! find takes a list of parameters!");
		}


		String fileName = (String) params[0];
		// Item to be searched
        Item searchItem = shell.getCurDir().followPath(shell, parameters[i+1]);


        // Apply function search() which was previously defined
        ArrayList<String> result = this.search(shell, fileName, searchItem, searchResult);

        String output = "";
        // Print the file paths in the returned array
        for(String n: result){
            output += n + "\n";
        }
		return output;
	}
}
