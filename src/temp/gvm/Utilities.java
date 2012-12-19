package temp.gvm;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class Utilities
{
	/**
	 * Escapes a raw text to be used in Regular Expression patterns
	 * @param text	The text needing to be used in regular expressions
	 * @param nonCapturing	Whether or not this text needs be grouped as a non capturing group
	 * @return	Regex escaped text
	 */
	private static final String escapeForRegEx(String text, boolean nonCapturing)
	{
		String[] reservedChars = { "(", ")", "[", "]", "\\", "|", "{", "}", "?", "+", ".", "*", "$", "^" };
		String ret = text;
		
		for(String resChar : reservedChars) {
			ret = ret.replace(resChar, "\\" + resChar);
		}
		
		if(nonCapturing) {
			ret = "(?:" + ret + ")";
		}
		
		return ret;
	}
	
	public static final String getSubstring(String text, String startToken, String endToken)
	{
		return getSubstring(text, startToken, endToken, false, false, false);
	}
	
	public static final String getSubstring(String text, String startToken, String endToken, boolean includeNewLines, boolean gotoEnd, boolean includeTokens)
	{
		String rgxStartToken = escapeForRegEx(startToken, true);
		String rgxEndToken = escapeForRegEx(endToken, true);
		String ret = null;
		Pattern rgxPattern = null;
		Matcher rgxMatch = null;
		
		if(includeNewLines) {
			rgxPattern = Pattern.compile(rgxStartToken + "((.|\n)*?)" + rgxEndToken);
		} else {
			rgxPattern = Pattern.compile(rgxStartToken + "(.*?)" + rgxEndToken);
		}
		
		rgxMatch = rgxPattern.matcher(text);
		if(rgxMatch.find()) {
			//Found, start/end tokens are non-capturing so grab first group
			ret = rgxMatch.group(0);
		} else if(gotoEnd) {
			if(includeNewLines) {
				rgxPattern = Pattern.compile(rgxStartToken + "((.|\n)*?)(?:($|\\Z))");
			} else {
				rgxPattern = Pattern.compile(rgxStartToken + "(.*?)(?:($|\\Z))");
			}
			
			rgxMatch = rgxPattern.matcher(text);
			if(rgxMatch.find()) {
				//Found, start/end tokens are non-capturing so grab first group
				ret = rgxMatch.group(0);
			}
		}
		
		//ret == null if not found
		if (ret != null && includeTokens) ret = startToken + ret + endToken;
		
		return ret;
	}
}
