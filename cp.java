/**
 * The <code>cp</code> class represents cp command objects.
 *
 * The cp command allows the user to copy Items. If the user
 * wishes to [fix this].
 *
 */
public class cp extends Command {

	/**
	 * Creates a <code>cp</code> object.
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public cp(int[] numParams){
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
	public void copy(JShell shell, Item item, DIR folder)
															throws Exception{

		String itemName = item.getName();

		if (item instanceof File) {

			File fileCopy =  new File(itemName, folder.getPathway() +"/" +
										itemName,((File)item).getContents());

			folder.insertItem(shell, fileCopy);
		}

		else if (item instanceof DIR) {

			// New empty dir to be placed on target folder
			DIR dirCopy = new DIR(itemName, folder.getPathway() +"/" +
																itemName);

			// place dir in target folder
			folder.insertItem(shell, dirCopy);

			// recursively copy
			for (Item component : ((DIR)item).getSubContents().values()) {
				copy(shell, component, dirCopy);
			}
		}

	}

	/**
	 * Makes a copy of the Item at the old path and places it into the DIR
	 * at the new path.
	 *
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		super.checkParams(parameters);

		DIR curDir = shell.getCurDir();

		Item oldItem = curDir.followPath(shell, parameters[1]);

		DIR newPathDIR = (DIR) curDir.followPath(shell, parameters[2]);

		this.copy(shell, oldItem, newPathDIR);

		return super.passToEcho(shell, input, "\n");
	}

}
