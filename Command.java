import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.regex.*;

/**
 * The <code> Command </code> class represents command objects.
 * It allows the user to use various Unix commands and executes them in a mock
 * file system environment.
 *
 * A command object knows the amount of parameters it can accept and can react
 * accordingly if the user has not inputed the correct amount(s).
 * It can also execute its primary directive with the passed
 * parameters or input from the user
 *

 */
public class Command{

	//a list of the acceptable amounts of parameters that this command excepts
	private int[] numParams;

	/**
	 * Creates a <code> Command </code> object .
	 * This object knows the amount of parameters that it can expect.
	 *
	 * Note to the Marker:
	 * the declaration of a command with no parameters means that there
	 * is no intention to restrict on the amount of parameters.
	 *
	 * @param numParams
	 * @return void
	 */
	public Command(int[] numParams){
		this.numParams = numParams;
	}

	/**
	 * Returns the <code>int Array</code> which contains a list of the
	 * acceptable amounts of parameters that this command excepts.
	 *
	 * @return <code>int Array</code> specifying optional parameter amounts
	 */
	public int[] getNumParams(){
		return this.numParams;
	}

	/**
	 * Checks whether the amount of parameters entered into the function is
	 * expected.
	 *
	 * @param parameters String Array of formatter user entries
	 * @return <code> true </code> signifying that parameter requirements
	 * are met
	 * @throws InvalidParameterException when the parameter amount is
	 * incorrect
	 */
	public int checkParams(String[] parameters)
						throws InvalidParameterException{

		String numOfParams = "";
		for (int num:this.getNumParams()){

			// the sum of the parameters that the command takes along with command itself
			if ((num + 1) == parameters.length){
				return num;
			}
			numOfParams += num + " ";
		}
		throw new InvalidParameterException("Error! this command only takes the following number(s) of parameters:" +
				numOfParams);
	}

	/**
	 *
	 * @param input
	 * @return
	 */
	public int findChevronIndex(String input) {

		int cheveronIndex = input.indexOf(">");

		//if the cheveron is not found, include the rest on the input as content
		if (cheveronIndex == -1){
			cheveronIndex = input.length();
		}
		return cheveronIndex;
	}

	/**
	 *
	 * @param input
	 * @return
	 */
	public Object[] extractOptParam(String input, String validOptParam){

		//if no optional parameters are found, the replace below will not effect the string.
		String optionalParam = " ";

		//all optional parameters start with a "-"
		Pattern optParamPattern = Pattern.compile("-\\w+");
		Matcher m = optParamPattern.matcher(input);

		if (m.find()){
			optionalParam = input.substring(m.start(), m.end());
		}

		if (!optionalParam.equals(validOptParam) && !optionalParam.equals(" ")){
			throw new InvalidParameterException("Error! "+ optionalParam +" is not a valid parameter, try "+ validOptParam +"!");
		}

		//removes the optional parameter from the input, if present
		String [] newParameters = input.replace(optionalParam, " ").trim().split("\\s+");
		return new Object[]{optionalParam, newParameters};

	}

	/**
	 * /remember that > assumes to overwrite, so no need to check
	 * goes in the curdir or the destination folder if cd
	 * if part of the parameters is invalid the files is not made
	 *
	 *
	 * @param shell
	 * @param parameters
	 * @param input
	 * @param output
	 * @param numParams
	 * @return
	 * @throws Exception
	 */
	public String passToEcho(JShell shell, String input, String output)
														throws Exception{

		//convert the output into an echo command
		String newInput = "echo " + output + " " + input.substring(
											this.findChevronIndex(input));

		return shell.getStrToCommand().get("echo").exe(shell,
										newInput.split("\\s+"),  newInput);

		}

	/**
	 * Checks parameters, executes its primary directive, and returns a String
	 *  (a new line in this case - see final report).
	 *
	 * @param shell a  which is a mock Unix Shell
	 * @param parameters String Array of formatter user entries
	 * @param input String of unformatted user entry
	 * @return a String message
	 * @throws Exception to handle any invalid input (see final report)
	 */
	public String exe(JShell shell, String[] parameters, String input)
														throws Exception{

		this.checkParams(parameters);
		return "\n";

	}

	/**
	 *
	 * @param shell
	 * @param parameters
	 * @param input
	 * @param loopParams
	 * @return
	 * @throws Exception
	 */
	public String exeForList (JShell shell, String[] parameters, String input, int timesExecuted,  Object[] loopParams)
			throws Exception{

		String output = "";

		//input.contains(">") &&  is to work around the "ls" case without throwing an error
		if (input.contains(">") &&  parameters[parameters.length - 2].contains(">")){
			parameters = Arrays.copyOfRange(parameters, 0, parameters.length-2);
			timesExecuted = timesExecuted -2;

		}

		int i = 1;

		do{
			output += this.looper(shell, parameters, i, loopParams);
			i++;

		}while(i < timesExecuted);

		return output;

	}

	/**
	 *
	 * @param shell
	 * @param parameters
	 * @param i
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String looper(JShell shell, String[] parameters, int i, Object[] params ) throws Exception {
		return (String)params[0];
	}


}
