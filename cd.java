/**
 * The <code>cd</code> class represents cd command objects.
 * It allows the user to navigate through the directories of the mock file
 * system environment.
 *
 */
public class cd extends Command {

	/**
	 * Creates a <code>cd</code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams
	 * @return void
	 */
	public cd(int[] numParams){
		super(numParams);
	}

	/**
	 * Sets the directory parameter or appropriate symbol (i.e. "/" for the
	 * root directory) as the current directory that the mock Unix shell
	 * works with. This parameter is relative to the current directory by
	 * default or may be a full path with "/" characters to separate folders.
	 * Also changes the navigator to suit the new current directory.
	 * Finally, returns the new line as a message (see final report)
	 *
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		super.checkParams(parameters);

		DIR curDir = shell.getCurDir();
		String path = parameters[1];


		//this way avoids an error if cd is entered by itself
		if (path.equals("/")){
			shell.setCurDir(shell.getRootDir());

		} else if (path.equals("..")){
			shell.setCurDir(curDir.getParentDIR(shell));

		} else if (path.equals(".")){
			// Nothing is done because the current directory is already set

		}else{
			shell.setCurDir((DIR)curDir.followPath(shell, path));
		}

		//gets the updated the current directory's pathway
		shell.setNavigator(shell.getCurDir().getPathway() + "/# ");

		return super.passToEcho(shell, input, "\n");


	}

}
