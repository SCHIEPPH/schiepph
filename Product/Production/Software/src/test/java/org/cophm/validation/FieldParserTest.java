package org.cophm.validation;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by
 * User: ralph
 * Date: 12/20/11
 * Time: 9:25 AM
 */
public class FieldParserTest extends TestCase {

   @Before
    void  setup() {

   }

    @After
    void tesrdown() {

    }

    @Test
    public void testParse() {
        String      input = "^second field^^^fifth field^^seventh field";
        FieldParser     parser = new FieldParser();

        assertEquals("", parser.parse(input, 5), "fifth field");
        assertEquals("", parser.parse(input, 2), "second field");
        assertEquals("", parser.parse(input, 7), "seventh field");
    }

}
