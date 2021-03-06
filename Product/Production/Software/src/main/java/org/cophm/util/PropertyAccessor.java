/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.cophm.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class PropertyAccessor
{
    private static String         configDirectory = "";
    private static String         configFileName = "schiepph.properties";
    private static Properties     props = null;


    static
    {
        Logger log = Logger.getLogger(PropertyAccessor.class.getName());

        configDirectory = System.getenv(Constants.SCHIEPPH_PROPERTIES_DIR_ENVIRONMENT_VARIABLE_NAME);

        if(configDirectory == null) {
            configDirectory = System.getProperty(Constants.SCHIEPPH_PROPERTIES_DIR_SYSTEM_VARIABLE_NAME, "conf");
        }

        //
        // Make sure we have a "/" at the end
        //
        if (configDirectory.endsWith(File.separator) == false) {
            configDirectory = configDirectory + File.separator;
        }

        props = new Properties();

        try {
            props.load(new InputStreamReader(new FileInputStream(configDirectory + configFileName)));
        }
        catch(IOException e) {
            log.error("Caught a " + e.getClass().getName() + ": " + e.toString());

            //
            // It's a choice between not finding any properties and an NPE.  I think this
            // approach is kinder.
            //
            props = new Properties();
        }

        props.put(Constants.SCHIEPPH_PROPERTIES_DIR_PROPERTIES_FILE_VARIABLE_NAME, configDirectory);
    }


    /**
     * Default constructor.
     */
    private PropertyAccessor() {
    }

    public static void  loadAdapterProperties(String  adapterPropertyFileName)
            throws IOException {
        props.load(new InputStreamReader(new FileInputStream(configDirectory + adapterPropertyFileName)));
    }

    public static String getProperty(String name)
        throws      PropertyAccessException     {
        String      propertyValue;

        propertyValue = (String)props.get(name);

        return propertyValue;
    }

    public static String getProperty(String name, String  defaultValue)
        throws      PropertyAccessException     {
        String      propertyValue;

        propertyValue = (String)props.get(name);

        if(propertyValue == null) {
            propertyValue = defaultValue;
        }

        return propertyValue;
    }

    public static boolean getPropertyBoolean(String name)
        throws      PropertyAccessException {
        String      propertyValue;

        propertyValue = (String)props.get(name);

        if(propertyValue == null) {
            return false;
        }

        return (propertyValue.equalsIgnoreCase("true") || propertyValue.equalsIgnoreCase("yes"));
    }

}