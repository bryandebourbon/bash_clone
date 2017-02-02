import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.RejectedExecutionException;
import java.util.regex.*;

/**
 * The <code> DIR </code> class represents directory objects.
 * It allows the user to organize and store various items in the mock
 * file system environment.
 *
 * A DIR object manages the items (either files or directories) that it holds
 * and can prevent them from unknowingly being overridden.
 * DIR objects are also capable of following full path with "/" characters to
 * separate folders, or lists of these folder paths and respond to false
 * pathways
 * @see java.util.HashMap<K,V>
 */
public class DIR extends Item {


	private HashMap <String, Item> subContents;

	 /**
	  * Creates a DIR object.
	  * Every DIR has a name, pathway and HashMap for storing subContents.
	  *
	  * @param name the DIR name
	  * @param pathway the DIR pathway
	  * @return void
	  * @see java.util.HashMap<K,V>
	  */
	public DIR (String name, String pathway) {
		super(name, pathway);
		this.subContents = new HashMap <String, Item>();
	}

	/**
	 * Returns the subContents (ie. a HashMap of the items within this
	 * directory) of this DIR folder.
	 *
	 * NOTE: Values acquired from the returned HashMap should be type-casted
	 * appropriately.
	 *
	 * @return the subContents of the directory
	 */
	public HashMap<String, Item> getSubContents() {
		return this.subContents;
	}

	/**
	 *
	 * @param shell
	 * @param message
	 */
	public String removalPrompt(JShell shell, String message, String regex)
		throws RejectedExecutionException{


		//needed to eventually reset the navigator back
		String oldNav = shell.getNavigator();


		shell.setNavigator(message);
		Object[] prompt = shell.promptUser(new Scanner(System.in));
		String answer = ((String[])prompt[0])[0];

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(answer);

		while(!m.find()){
			prompt = shell.promptUser(new Scanner(System.in));
			answer = ((String[])prompt[0])[0];
			m = p.matcher(answer);
		}

		shell.setNavigator(oldNav);
		return answer;


	}

	/**
	 * Puts the item into this DIR's HashMap. If the item already exists,
	 * the user is prompted (through the shell) for an overwrite decision.
	 *
	 * @param shell a  which is a mock Unix Shell
	 * @param item the file or DIR to be inserted
	 * @return void
	 * @throws RejectedExecutionException to handle when the user wishes
	 * to not override a file.
	 */
	public void insertItem(JShell shell, Item item)
		throws RejectedExecutionException{

		if (this.getSubContents().containsKey(item.getName())){


			String response = removalPrompt(shell, item.getName() + " already exists!\n" +
                    "Type \"ow\" to overwrite the file, or any name to rename the file!",".");


			if (!response.equals("ow")){
				item.setName(response);

			}
		}
		this.getSubContents().put(item.getName(), item);


	}

	/**
	 * Follows the pathway that the user has entered in List form, and handles
	 * the error of a false path.
	 *
	 * @param pathList which contains the folders of a pathway in List form
	 * @return the item at the end of the pathway
	 * @throws FileNotFoundException to handle when an invalid pathway is
	 * 								 inputed
	 */
	public Item followPath(JShell shell, String[] pathList)
												throws FileNotFoundException {

		DIR dir;

		//support for the full path functionality.
		//the length of pathList equals 0 when mkdir finds the future parentDir
		if (pathList.length != 0 && (pathList[0]).equals("root")){
			dir = shell.getRootDir();
			pathList = Arrays.copyOfRange(pathList, 1, pathList.length);
		} else{
			dir = this;
		}

		for (int i = 0; i < pathList.length; i++){

			String folder = pathList[i];

			if (dir.getSubContents().get(folder) == null){
				throw new FileNotFoundException("Error! " + folder + " not found!");
			}

			Item item = dir.getSubContents().get(folder);
///////////weird
			if (item instanceof File && i == pathList.length-1){
				return item;

			}else{
//////////////////
				dir = (DIR)item;
			}
		}
		return dir;

	}

	/**
	 * Follows the full path that the user has entered with "/" characters to
	 * separate folders.
	 *
	 * @param pathway which contains the folders of a pathway
	 * @return the item at the end of the pathway
	 * @throws FileNotFoundException to handle when an invalid pathway is
	 * 								 inputed
	 */
	public Item followPath(JShell shell, String pathway) throws FileNotFoundException{

		String[] pathList = pathway.split("/");
		return this.followPath(shell, pathList);
	}

}
