package org.cophm.test;

/**
 * Created by
 * User: ralph
 * Date: 12/6/11
 * Time: 2:20 PM
 */
public class YoBubba {

    public  String  getMessage() {

        return "Yo Bubba!";
    }

    public static void main(String[] args) {
        YoBubba     yb = new YoBubba();

        System.out.println(yb.getMessage());
    }
}
