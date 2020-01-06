package com.company;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.Collection;

import static java.lang.String.format;


public class FolderData {
    private File sourcePath;
    private SuffixFileFilter extensions;
    private Collection<File> files;
    private File[] filesArray;

    public FolderData() {

    }

    public FolderData(String directory, String[] extensions) {
        setSourcePath(directory);
        setExtension(extensions);
        setFiles(getSourcePath());
        setFilesArray(getFiles());
    }

    public File getSourcePath() {
        return this.sourcePath;
    }

    public void setSourcePath(String directory) {
        this.sourcePath = new File(directory);
    }

    public Collection<File> getFiles() {
        return this.files;
    }

    private void setFiles(File directory) {
        try {
            this.files = FileUtils.listFiles(getSourcePath(),
                    getExtension(),
                    DirectoryFileFilter.DIRECTORY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public SuffixFileFilter getExtension() {
        return this.extensions;
    }

    private void setExtension(String[] extensions) {
        this.extensions = new SuffixFileFilter(extensions, IOCase.INSENSITIVE);
    }

    public String toString() {
        return new StringBuilder()
                .append( format( "%s\n", getFiles() ) )
                .toString();
    }

    public File[] getFilesArray() {
        return filesArray;
    }

    private void setFilesArray(Collection<File> filesArray) {
        this.filesArray = filesArray.toArray(File[]::new);
    }
}