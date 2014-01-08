package pl.jasiowka.appinpy;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AppinpyUtils {

    private static String indentString(int indent) {
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < indent; i++)
            sbuf.append(" ");
        return sbuf.toString();
    }

    private static String alignReplacement(String txt, int indent) {
        Pattern pattern = Pattern.compile("(\\r?\\n)|\\r");
        Matcher matcher = pattern.matcher(txt);
        StringBuffer sbuf = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(sbuf, matcher.group() + indentString(indent));
        matcher.appendTail(sbuf);
        return sbuf.toString();
    }

	public static String mergeCode(String code, Map<String, String> replacements, boolean align) {
        Pattern pattern = Pattern.compile("(\\{-{2}\\w+-{2}\\})|(\\r?\\n)|\\r");
        Matcher matcher = pattern.matcher(code);
        int lastEnter = 0;
        StringBuffer sbuf = new StringBuffer();
        while (matcher.find()) {
        	String group = matcher.group();
            if (group.matches("(\\r?\\n)|\\r"))
                lastEnter = matcher.end();
            else {
                String key = group.substring(3, group.length() - 3);
                String replacement = replacements.get(key);
                if (replacement == null)
                    replacement = group;
                if (align) {
                    int indent = matcher.start() - lastEnter;
                    replacement = alignReplacement(replacement, indent);
                }
                matcher.appendReplacement(sbuf, replacement);
            }
        }
        matcher.appendTail(sbuf);
        return sbuf.toString();
    }

}
