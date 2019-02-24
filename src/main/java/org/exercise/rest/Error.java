package org.exercise.rest;

public class Error
{
    private String type;
    private String cause;

    public Error(String type, String cause)
    {
        this.type = type;
        this.cause = cause;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getCause()
    {
        return cause;
    }

    public void setCause(String cause)
    {
        this.cause = cause;
    }
}
