package org.cophm.util;

import com.sun.tools.internal.ws.wsdl.document.Input;

import java.util.StringTokenizer;

/**
 * Created by
 * User: ralph
 * Date: 12/20/11
 * Time: 9:31 AM
 */
public class FieldParser {

    public String  parse(String  inputString, int  position) {

        return parse(inputString, position, "^");
    }

    public String  parse(String  inputString, int  position, String  delimiter) {
        StringTokenizer     strtok;
        String              value;
        int                 counter;

        strtok = new StringTokenizer(inputString, delimiter, true);

        counter = 0;
        while(strtok.hasMoreTokens()) {
            value = (String)strtok.nextToken();
            counter++;
            if(counter == position) {
                return (value.equals(delimiter) ? "" : value);
            }
            //
            // Eat the next token and do not advance the counter.
            // Note that if this is a delimiter, it means we had
            // an empty field and do not need to eat the next token
            // because we already have.
            //
            if(value.equals(delimiter) == false) {
                strtok.nextToken();
            }
        }

        return "";
    }
}
