import java.security.InvalidParameterException;

////cant take empty

/**
 * The <code>echo</code> class represents echo command objects.
 *
 * The echo command permits the user to print text to the screen. It
 * also enables the user to write (or overwrite) text to a file. In the case
 * that the required file does not exist, one is created with the
 * appropriate text.
 *
 */
public class echo extends Command {

	/**
	 *  Creates a <code>echo</code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public echo(int[] numParams){
		super(numParams);
	}

	/**
	 * Finds the indices of the content (message to be echoed)
	 * in the input, and returns it.
	 *
	 * @param input, the user input
	 * @return the content of the input (between echo and a possible chevron)
	 */
	public String findEchoContent(String input){

		//the index after echo's "o"
		int firstBoundIndex = input.indexOf("o") + 1;

		int lastBoundIndex = super.findChevronIndex(input);

		//firstBoundIndex + 1 is to take content a space after the echo
		return  input.substring(firstBoundIndex + 1 , lastBoundIndex);

	}

	/**
	 * Checks the user input to ensure that the given parameters
	 * are valid and that the input follows that the right syntax.
	 *
	 * @param input, the user input
	 * @return void
	 * @throws InvalidParameterException - if the given parameter(s) is
	 * invalid.
	 */
	public void checkEchoParams(String input)
				throws InvalidParameterException{

		 String content = this.findEchoContent(input);


		/*replaces the content an arbitrary character
		 *so that the split can still count the correct number of parameters
		 *(this has to do with the fact that there may be spaces in between
		 *the content)
		 */
		String[] remainingParams = input.replace(content, " * ").trim().
															split("\\s+");

		super.checkParams(remainingParams);

	}

	/**
	 * Finds and returns the appropriate File to store the user text.
	 * If the file does not already exist, it is created in the appropriate
	 * directory and then returned.
	 *
	 * @param shell, the current JShell object
	 * @param fileName, the name of the file
	 * @return the file that will store the user's given text.
	 * @throws Exception - if the user does not want to overwrite the file.
	 */
	public File makeOutFile(JShell shell, String fileName) throws Exception{

		DIR curDir = shell.getCurDir();

		if (curDir.getSubContents().keySet().contains(fileName) &&
				curDir.getSubContents().get(fileName) instanceof File ) {

			return (File) curDir.getSubContents().get(fileName);

		}else{// if the file is not there already, make it and insert it

 		File file = new File(fileName,  curDir.getPathway() +"/" + fileName,
 																		"");
 		curDir.insertItem(shell, file);
 		return file;
 	}
	}



	/**
	 * Depending on the user's input,
	 * 		- Prints user text to screen
	 * 		- Adds user text to file
	 * 	    - Overwrites file with user text
	 *
	 * In either of the last two scenarios, if the input file does not
	 * already exist, it will be created and will then have the text stored.
	 * Finally, if the user wishes to add to a File, the desired text
	 * will always begin on a new line in the File.
	 *
	 * @throws InvalidParameterException - if an incorrect number of
	 * parameters is given by the user.
	 * (Note: other tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		this.checkEchoParams(input);

		String content = this.findEchoContent(input);

		// the parameters after the content
		String[] params = input.substring(super.findChevronIndex(input)).trim().split("\\s+");

		// If there was no chevrons, nothing will be in params due to the split
		if (params[0].equals("")){
			return content +"\n";

		} else {

			if (params[0].equals(">>")){

				File file = this.makeOutFile(shell, params[1]);

				//add to the existing content
				file.setContents(file.getContents() + "\n" + content);

			}else if (params[0].equals(">")){

				File file = this.makeOutFile(shell, params[1]);

				//overwrite all content
				file.setContents(content);

			} else{
				throw new InvalidParameterException("Error! the echo command only takes parameters in the following formats:"
						+ "\n (1) STRING \t(2) STRING > OUTFILE \t(3) STRING > OUTFILE");
			}
			return "\n";

		}
 	}

}
