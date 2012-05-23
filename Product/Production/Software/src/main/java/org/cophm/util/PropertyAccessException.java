/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.cophm.util;

/**
 * This exception is thrown when an error occurs accessing properties.
 *
 * @author Les Westberg
 */
public class PropertyAccessException extends Exception
{
    /**
     * Default constructor.
     */
    public PropertyAccessException()
    {
        super();
    }

    /**
     * Constructor with an envloping exception.
     *
     * @param e  The exception that caused this one.
     */
    public PropertyAccessException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     *
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public PropertyAccessException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     *
     * @param sMessage The message for the exception.
     */
    public PropertyAccessException(String sMessage)
    {
        super(sMessage);
    }

}
