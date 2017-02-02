import java.util.ArrayList;
import java.util.Collection;




/**
 * The <code>ls</code> class represents ls command objects.
 *
 * The ls command permits the user to view the contents of the user's current
 * directory, or view the contents of another Item, given its path, in which
 * case, DIR items will have their contents output and File items will
 * have their name output.
 *
 */
public class ls extends Command {

	/**
	 * Creates an <code>ls</code> object.
	 * This object knows the amount of parameters that it can expect.
	 *
	 * @param numParams, the number of parameters given by the user.
	 * @return void
	 */
	public ls(int[] numParams){
		super(numParams);
	}

	/**
	  * Gets the contents of an Item object
	  *
	  * @param item the Item whose contents are to be found
	  * @return the filename if item is a File, the name and contents of
	  * the directory otherwise.
	  */
	 public String getItemContents(Item item, boolean paramPresent){

	  // -R found, so recursively list sub-directories instead of just
	  // listing the contents of the given item
	  if (paramPresent){
	   return getRecursiveContents(item);
	  }

	  if (item instanceof File) {
	   return item.getName() + "\n";

	  }
	  // if item is an instance of DIR
	  else {

	   String subItems = ((DIR)item).getSubContents().keySet().toString();
	   return item.getPathway() + ":\n" + subItems.substring(1,
	     subItems.length() - 1).replace(", ","\n") + "\n";
	  }
	 }

	 /**
	  * Sorts the sub items of the given directory based on type.
	  * This method is used to avoid digging deep into sub directories (before
	  * displaying the sub Files) when a given parent directory has both sub
	  * Files and sub DIRs. This method essentially helps make the output
	  * cleaner.
	  *
	  * @param dir, the given directory whose contents are to be sorted
	  * @return the subItems list sorted by type
	  */
	 public Item[] sortByType (DIR dir){


	  Collection<Item> subItemsCol = dir.getSubContents().values();
	  Item[] subItems = subItemsCol.toArray(new Item [subItemsCol.size()]);

	  // make the respective ArrayLists, and add the appropriate elements
	  ArrayList<Item> dirList = new ArrayList<Item>();
	  ArrayList<Item> fileList = new ArrayList<Item>();

	  for (Item item: subItems){
	   if(item instanceof File){
	    fileList.add(item);
	   }
	   else{
	    dirList.add(item);
	   }
	  }
	  // combine both arrays into sortedList
	  ArrayList<Item> sorted = new ArrayList<Item>(fileList);
	  sorted.addAll(dirList);

	  Item[] sortedList = sorted.toArray(new Item[subItems.length]);

	  return sortedList;
	 }

	 /**
	  * Similar to getItemContents, this method also gets the contents of an
	  * object, but will also recursively list any directories found.
	  *
	  * @param item the Item whose contents are to be found
	  * @return the filename if item is a File, the name and contents of
	  * the directory otherwise, recursively if necessary.
	  */
	 public String getRecursiveContents(Item item){

	  String s = "";

	  if (item instanceof File) {
	   return item.getPathway() + "\n";
	  }
	  // item is an instance of DIR
	  else {

	   s += item.getPathway() + "\n";

	   Item[] subItems = sortByType((DIR)item);

	   for (Item subItem: subItems){
	    // add appropriate text to output s, and since here item is
	    // of type DIR, we are forced to dig deeper (hence recursion)
	    s += getRecursiveContents(((DIR)item).getSubContents()
	      .get(subItem.getName()));
	   }
	  }

	  return s;
	 }

	/**
	 *
	 */
	public String looper(JShell shell, String[] parameters, int i, Object[] params )
															throws Exception {

		boolean recursivePresent = (Boolean) params[0];

		if (parameters.length == 1){
			return this.getItemContents(shell.getCurDir(), recursivePresent);
		}

		Item item = shell.getCurDir().followPath(shell, parameters[i]);
		return "\n" + this.getItemContents(item, recursivePresent);

	}

	/**
	  * Lists the contents of the user's current directory/chosen path
	  * If the user called this command with no arguments,
	  * display the content of the directory.
	  * Otherwise, for each path p, the order listed:
	  *   - If p specifies a file, print p
	  *   - If p specifies a directory, print p, a colon, then the
	  *    contents of that directory, then an extra new line.
	  *   - If p does not exist, displays the appropriate message.
	  *
	  * (Note: tags are implicitly inherited)
	  */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

/////////////////CHNAGE AT HOME///////////////////////////
		Object [] extractedParams = super.extractOptParam(input, "-R");
	    parameters =  (String[])extractedParams[1];
	    String optionalParam = (String)extractedParams[0];
		boolean recursivePresent = optionalParam.equals("-R");
/////////////////////////////////////////////////////////////

		String output = super.exeForList(shell, parameters, input,
				parameters.length, new Object[] {recursivePresent});

		return super.passToEcho(shell, input, output);
	}
}
