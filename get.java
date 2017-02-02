import java.security.InvalidParameterException;
import java.net.*;
import java.io.*;



/**
 * The <code>get</code> class represents get command objects.
 *
 * get URL
 * The get command permits the user to retrieve the file at the user inputted
 * URL. URL is a web address. The contents of the file at the URL is added to
 * the current directory.
 *
 * If a file named as the text after the last forward slash in the URL does
 * not exist in the current directory, a new file with that name is created
 * to contain the contents of the file at the URL.
 *
 * If a current directory file named as the text after the last forward slash
 * in the URL does exist, the user has two options:
 * 1.  the current directory file is overwritten with the contents of the file
 *     at the URL
 * 2.  the user inputs a new current directory filename, and a new current
 *     directory file is created to contain the contents of the file at the
 *     URL
 *
 */
public class get extends Command {

	 /**
	  *  Creates a <code>get</code> object .
	  * This object knows the amount of parameters that it can expect.
	  *
	  * @param numParams, the number of parameters given by the user.
	  * @return void
	  */
	 public get(int[] numParams){
		 super(numParams);
	 }

	 /**
	  *
	  *
	  * @throws InvalidParameterException - if an incorrect number of
	  * parameters is given by the user.
	  * (Note: other tags are implicitly inherited)
	  */
	 public String exe(JShell shell, String[] parameters, String input)
	       throws Exception{

		  //
		  String url = parameters[1];
		  String [] elementsUrl = url.split("/");
		  String webFileName = elementsUrl[elementsUrl.length -1];

		  URLConnection connection = new URL(url).openConnection();
		  BufferedReader webLine =
		    new BufferedReader(new InputStreamReader(connection.getInputStream()));


		  String webString;
		  String content = "";
		  do{
			  webString = webLine.readLine();
			  content += webString + "\n";

		  } while ( webString != null);
		  webLine.close();

		  DIR curDir = shell.getCurDir();
		  File file = new File(webFileName,  curDir.getPathway() +"/" + webFileName, content);
		  curDir.insertItem(shell, file);

		   return "\n";
	}
	}
