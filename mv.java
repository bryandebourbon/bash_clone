/**
 * The <code>mv</code> class represents mv objects.
 *
 * The mv command allows the user to move an item from an old path
 * to a new path.
 *
 */
public class mv extends cp {


	/**
	 * Creates an <code>mv</code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 */
	public mv(int[] numParams){
		super(numParams);
	}

	/**
	 * Moves an item from an old path to a new path. If the new path
	 * is a directory, the item is moved into the new path. Otherwise, if new
	 * path is a file, the item from the old path is renamed to the file
	 * at the new path. If the new path is neither an already existing item,
	 * nor a directory, the copy of the Item at old path has the name of
	 * new path, and will be placed in the current directory.
	 *
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		super.exe(shell, parameters, input);

		Item item = shell.getCurDir().followPath(shell, parameters[1]);

		DIR oldFolder = item.getParentDIR(shell);

		(oldFolder.getSubContents()).remove(item.getName());

		return super.passToEcho(shell, input, "\n");
	}
}
