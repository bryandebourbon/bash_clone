import java.security.InvalidParameterException;

public class rm extends Command {

	/**
	 * Creates a <code>rm</code> object.
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public rm(int[] numParams){
		super(numParams);
	}

	/**
	 * Copies an item and adds it to the specified directory.
	 * Note that current directory is the default directory.
	 *
	 * @param shell, the current JShell object
	 * @param item, the item that is to be copied
	 * @param itemName, the name of the item
	 * @param folder, the folder that the copy is to be inserted into
	 * @throws Exception - if the target file already exists.
	 */
	public String remove(JShell shell, Item item, boolean paramPresent) throws Exception{

		DIR folder = item.getParentDIR(shell);

		if (item instanceof DIR) {

			//recursively remove subItems
			for (Item component : ((DIR)item).getSubContents().values()) {
				this.remove(shell, component, paramPresent);

			}
		}

		if (!paramPresent){
			String result = folder.removalPrompt(shell, "are you sure you want to remove " + item.getName() + "? (yes or no) ", "yes|no" );

			if (result.equals("no")){
				return item.getName() + " was not removed.\n";
			}
		}
		folder.getSubContents().remove(item.getName());
		return item.getName() + " was removed.";

	}

	/**
	 *
	 */
	public String looper(JShell shell, String[] parameters, int i, Object[] params ) throws Exception {

		if (parameters.length == 1){
			throw new InvalidParameterException("Error! rm takes a list of parameters!");
		}

		Item item = shell.getCurDir().followPath(shell, parameters[i]);

		return this.remove(shell, item, ((String)params[0]).equals("-f"));


	}

	/**
	 *
	 */
	public String exe(JShell shell, String[] parameters, String input)
			throws Exception{

		Object [] extractedParams = super.extractOptParam(input, "-f");
		String [] nonOptParameters = (String[])extractedParams[1];

		String output = super.exeForList(shell, nonOptParameters, input, nonOptParameters.length,
							new Object[] {(String)extractedParams[0]});

		return super.passToEcho(shell, input, output);
	}


}
