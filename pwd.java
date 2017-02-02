/**
 * The <code>pwd</code> class represents pwd command objects.
 *
 * The pwd command displays the pathway of the current directory
 * to the user, which can be useful for navigation purposes.
 *
 */
public class pwd extends Command {

	/**
	 * Creates a <code>pwd</code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public pwd(int[] numParams){
		super(numParams);
	}

	/**
	 *  Returns the pathway of the user's current directory.
	 *
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		super.checkParams(parameters);

		String output =  shell.getCurDir().getPathway();

		return super.passToEcho(shell, input, output);
	}

}
