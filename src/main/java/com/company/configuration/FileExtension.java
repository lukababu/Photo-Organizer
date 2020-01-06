package com.company.configuration;

public final class FileExtension {
    private String extension;

    public FileExtension(String extension) {
        this.extension = extension;
    }

    public FileExtension() {
        this("");
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "." + getExtension();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileExtension) {
            FileExtension extension = (FileExtension) obj;
            return this.extension.equals(extension.getExtension());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(extension).hashCode();
    }
}
