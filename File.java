/**
 * The <code>File</code> is used to represent mock Files in the system.
 *
 * Files inherit from the more general Item class. In addition to their Item
 * properties, Files may also have contents, but will not contain other
 * Items, unlike DIRs (directories).
 *
 */
public class File extends Item{

	private String contents;

	/**
	 * Creates a <code>File</code> object. Every File has a name, pathway
	 * and possibly contents.
	 *
	 * @param name, the File name
	 * @param pathway, the File pathway
	 * @param contents, the contents of the File
	 * @return void
	 */
	public File(String name, String pathway, String contents) {
		super(name, pathway);
	    this.setContents(contents);
		}

	/**
	 * Returns the contents of the File.
	 * @return contents, the File contents.
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * Changes the contents of the File.
	 * @param contents, the new File contents.
	 * @return void
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

}
