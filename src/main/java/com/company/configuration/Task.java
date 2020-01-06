package com.company.configuration;

import java.util.List;

import static java.lang.String.format;

public final class Task {
    private String source;
    private String destination;
    private char flag;
    private List< FileExtension > extension;

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List< FileExtension > getExtension() {
        return this.extension;
    }

    public void setExtension(List< FileExtension > extension) {
        this.extension = extension;
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append( format( "Source: %s\n", getSource() ) )
                .append( format( "Destination: %s\n", getDestination() ) )
                .append( format( "Flag: %c\n", getFlag() ) )
                .append( format( "File Extensions: %s\n", getExtension() ) )
                .toString();
    }


}
