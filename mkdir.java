import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * The <code>mkdir</code> class represents mkdir command objects.
 * It allows the user to create 1 or multiple directories at once within the
 * mock file system environment.
 *
 */
public class mkdir extends Command {

	/**
	 * Creates an <code>mkdir</code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams
	 * @return void
	 */
	public mkdir(int[] numParams){
		super(numParams);
	}
	/**
	 * Returns the future Parent DIR and name of the new object.
	 *
	 * Ensures that no other directory can not be named root to allow the
	 * full path option of parameters for functions.
	 * @param shell
	 * @param parameter
	 * @return Object Array containing the Parent DIR and name of the
	 *         future DIR at indices 0 and 1 respectively.
	 * @throws FileNotFoundException when incorrect path given
	 */
	public Object[] getNewDirInfo(JShell shell, String pathway)
									          throws Exception{
		//finding the future parent DIR
		String[] pathList = pathway.split("/");
		String[] parentPath =
			Arrays.copyOfRange(pathList, 0, (pathList.length - 1));
		DIR folder = (DIR) shell.getCurDir().followPath(shell, parentPath);

		//finding the name of the future DIR
		String name = pathList[pathList.length-1];

		//by convention, names cannot have root or ">" characters
		if (name.equals("root") || name.contains(">")){
			throw new InvalidParameterException("Error! \""+name+"\" is not allowed to be in new folder names!");
		}

		return new Object[] {folder, name};
	}
	/**
	 * Creates new directories with parameters as the names within the mock
	 * Unix shell.These parameters are relative to the current directory by
	 * default or may be a full path with "/" characters to separate folders.
	 * When making multiple directories, their names are separate by spaces
	 * on the same line (Note that if a given pathway does not exist, then
	 * all and only the directories preceding it will be created). Finally,
	 * returns the new line as a message (see final report).
	 *
	 * Note that Directory names cannot be created with spaces.
	 * (Note: tags are implicitly inherited)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		super.exeForList(shell, parameters, input, parameters.length, new Object []{});
		return super.passToEcho(shell, input, "\n");
	}

	/**
	 *
	 */
	public String looper(JShell shell, String[] parameters, int i, Object[] params ) throws Exception {
		//THIS SHOULD BE MANAGES IN OVER RIDDEN check params
		if (parameters.length == 1){
			throw new InvalidParameterException("Error! mkdir takes a list of parameters!");
		}

		Object[] dirInfo = this.getNewDirInfo(shell, parameters[i]);
		DIR folder = (DIR) dirInfo[0];
		String name = (String)  dirInfo[1];


		folder.insertItem(shell,
				new DIR(name, folder.getPathway() +"/" + name));

		return "";
	}
}
