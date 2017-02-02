import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * The <code>Item</code> class is the Parent of File and DIR objects.
 * Items, so as to be a general version of either Files or DIRs, have
 * names and pathways. Furthermore, Items can also find their parent
 * directory.
 *
 */
public class Item{

	 private String name;
	 private String pathway;

	/**
	 * Creates an <code>Item</code> object. Every Item has a name
	 * and pathway.
	 *
	 * @param name the Item name
	 * @param pathway the Item pathway
	 * @return void
	 */
	 public Item(String name, String pathway) {

    	 this.name = name;
    	 this.pathway = pathway;
     }

	/**
	 * Returns the name of the Item.
	 *
	 * @return Item name
	 */
	 public String getName() {
		return name;
	}

	/**
	 * Changes the Item's name.
	 *
	 * @param name, the new Item name.
	 * @return void
	 */
    public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the pathway of the Item.
	 *
	 * @return Item pathway
	 */
    public String getPathway() {
		return pathway;
	}

	/**
	 * Changes the pathway of the Item.
	 *
	 * @param pathway, the new Item pathway.
	 * @return void
	 */
	public void setPathway(String pathway) {
		this.pathway = pathway;
	}

	/**
	 * Returns an Item's parent directory. In the case of a File,
	 * returns the folder that the File is contained in.
	 *
	 * @param shell the current JShell object.
	 * @return folder the parent directory.
	 * @throws FileNotFoundException - if the required file does not exist.
	 */
	public DIR getParentDIR(JShell shell) throws FileNotFoundException{

		   String[] pathList = this.pathway.split("/");

		   // the root dir is its own parent (when the pathway only has 1 element which is just root)
		   if (pathList.length == 1){
			   return shell.getRootDir();
		   }

		   //Starts from 1 to avoid root dir because we start at root DIR
		   String[] parentPath = Arrays.copyOfRange(pathList, 1,
				   								(pathList.length -1));

		   //followPath throws the exception
		   DIR folder = (DIR) shell.getRootDir().followPath(shell, parentPath);

		   return folder;
	}
}
