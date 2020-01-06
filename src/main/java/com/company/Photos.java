package com.company;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.psd.PsdMetadataReader;
import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class Photos extends FolderData {
    public static final char RAW = 'r';
    public static final char REGULAR = 's';
    public static final char EDITED = 'e';
    private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    private String destinationFolder;
    private LocalDateTime originalCreationDate;
    private boolean isTagged;
    private char flag;

    public Photos() {
        super();
    }

    public Photos(String sourceFolder, String[] extensions, String destinationFolder, char flag) {
        super(sourceFolder, extensions);
        setDestination(destinationFolder);
        setFlag(flag);
    }
    public void process() {
        String destinationFileName;
        String destinationDirectoryAddress;

        for(int i = 0; i < super.getFiles().size(); i++) {
            // get name from EXIF tag it exists
            destinationFileName = getNameFromTag(getFilesArray()[i]);

            // Figure out the destination for file
            destinationDirectoryAddress = getDirectoryAddress(getFilesArray()[i], destinationFileName);

            // Rename and move to new directory
            if (!renameMove(getFilesArray()[i], destinationFileName, destinationDirectoryAddress)) {
                System.out.println("Error");
            }
        }
    }

    private String getNameFromTag(File file) {
        Metadata metadata;
        //ExifSubIFDDirectory directory;
        Date dateOriginal = null;
        Date dateModified = new Date(file.lastModified());
        Date dateExif = null;
        Date dateDigitized = null;
        String newName = "";

        try {
            if (getFlag() == REGULAR) {
                metadata = JpegMetadataReader.readMetadata(file);
            }
            else if (getFlag() == RAW) {
                metadata = TiffMetadataReader.readMetadata(file);
            }
            else if (getFlag() == EDITED) {
                if(FilenameUtils.getExtension(file.getName()).equals("psd"))
                    metadata = PsdMetadataReader.readMetadata(file);
                else
                    metadata = ImageMetadataReader.readMetadata(file);
            }
            else {
                metadata = ImageMetadataReader.readMetadata(file);
            }

            Iterable<Directory> exif = metadata.getDirectories();
            for (Directory directory : exif) {
                Collection<Tag> tags = directory.getTags();
                for (Tag tag : tags) {
                    if ("Date/Time".equals(tag.getTagName())) {
                        dateExif = sdfDate.parse(tag.getDescription());
                    } else if ("Date/Time Original".equals(tag.getTagName())) {
                        dateOriginal = sdfDate.parse(tag.getDescription());
                    } else if ("Date/Time Digitized".equals(tag.getTagName())) {
                        dateDigitized = sdfDate.parse(tag.getDescription());
                    }
                    /*
                    if (tag.getDescription() != null && tag.getDescription().contains("201")) {
                        System.out.println(tag.getTagName() + ":" + tag.getDescription());
                    }
                    */
                }
            }

            if (dateOriginal != null) {
                setOriginalCreationDate(dateOriginal);
            }
            else if (dateDigitized != null) {
                setOriginalCreationDate(dateDigitized);
            }
            else if (dateExif != null) {
                setOriginalCreationDate(dateExif);
            }

            newName += getOriginalCreationDate().getYear();
            newName += "-";
            if (getOriginalCreationDate().getMonthValue() < 10)
                newName += "0";
            newName += getOriginalCreationDate().getMonthValue();
            newName += "-";
            if (getOriginalCreationDate().getDayOfMonth() < 10)
                newName += "0";
            newName += getOriginalCreationDate().getDayOfMonth();
            newName += " ";
            if (getOriginalCreationDate().getHour()<10)
                newName += "0";
            newName += getOriginalCreationDate().getHour();
            newName += ".";
            if (getOriginalCreationDate().getMinute()<10)
                newName += "0";
            newName += getOriginalCreationDate().getMinute();
            newName += ".";
            if (getOriginalCreationDate().getSecond()<10)
                newName += "0";
            newName += getOriginalCreationDate().getSecond();
            setTagged(true);
        } catch (IOException e) { // If the image has no metadata use original file name
            e.printStackTrace();
            setTagged(false);
        } catch (ImageProcessingException e) { // File format incompatibility
            e.printStackTrace();
            System.out.println("Incompatible File Format " + file + ". Using lastModifyTime.");
            setTagged(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No EXIF found for " + file + ". Using lastModifyTime.");
            setTagged(false);
        }

        return newName;
    }

    private String getDirectoryAddress(File file, String destinationFileName) {
        String finalDestination = getDestination();

        if (isTagged()) {
            finalDestination +=
                    "\\" +
                    getOriginalCreationDate().getYear() +
                    "\\" +
                    getOriginalCreationDate().getMonth().getDisplayName(TextStyle.FULL, Locale.CANADA) +
                    "\\";
            if (getOriginalCreationDate().getDayOfMonth() < 10)
                finalDestination += "0";
            finalDestination += getOriginalCreationDate().getDayOfMonth() +
                    "\\";
            if (getFlag() == REGULAR) {
                finalDestination += destinationFileName +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else if (getFlag() == RAW) {
                finalDestination += "RAW" +
                        "\\" +
                        destinationFileName +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else if (getFlag() == EDITED) {
                finalDestination += "Edited" +
                        "\\" +
                        destinationFileName +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else {
                finalDestination += "Unknown flag" + "(" + getFlag() + ")" +
                        "\\" +
                        destinationFileName +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
        }
        else {
            finalDestination += "Unknown YearTaken" +
                    "\\" +
                    "Unknown MonthNameTaken" +
                    "\\" +
                    "Unknown DayTaken" +
                    "\\";
            if (getFlag() == 's') {
                finalDestination += FilenameUtils.getBaseName(file.getName()) +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else if (getFlag() == 'r') {
                finalDestination += "RAW" +
                        "\\" +
                        FilenameUtils.getBaseName(file.getName()) +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else if (getFlag() == 'e') {
                finalDestination += "Edited" +
                        "\\" +
                        FilenameUtils.getBaseName(file.getName()) +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
            else {
                finalDestination += "Unknown flag" + "(" + getFlag() + ")" +
                        "\\" +
                        FilenameUtils.getBaseName(file.getName()) +
                        "." +
                        FilenameUtils.getExtension(file.getName());
            }
        }

        // TODO
        /*
        if (getFlag() == 'e') {

        }
        else {
            System.out.println("Unknown flag type: '" + getFlag() + "'");
        }
        */

        return finalDestination;
    }

    public boolean renameMove(File file, String destinationFileName, String destinationDirectoryAddress) {
        boolean fileMoved = false;
        Path finalPath;

        // Move the file
        int increment = 1; // used in case same file name
        try {
            finalPath = Paths.get(destinationDirectoryAddress); // Cast string to Path object
            Files.createDirectories(finalPath.getParent()); // Create folders in case it does not exist
            Files.move(Paths.get(file.getAbsolutePath()), finalPath); // Execute move
            fileMoved = true;
        } catch (FileAlreadyExistsException e) { // Handle files with same name by adding a number extension at the end
            while (!fileMoved) {
                try {
                    Files.move(Paths.get(file.getAbsolutePath()),
                            Paths.get(getDestination() +
                                    "\\" +
                                    destinationFileName +
                                    " " +
                                    "("+increment+")" +
                                    "." +
                                    FilenameUtils.getExtension(file.getName())));
                    fileMoved = true;
                    increment = 1;
                } catch (FileAlreadyExistsException e1) {
                    increment++;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileMoved;
    }


    // setter and getters
    public String getDestination() {
        return this.destinationFolder;
    }

    public void setDestination(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public LocalDateTime getOriginalCreationDate() {
        return originalCreationDate;
    }

    public void setOriginalCreationDate(Date date) {
        this.originalCreationDate = LocalDateTime.ofInstant(date.toInstant(),
                ZoneId.of("UTC"));
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void setTagged(boolean tagged) {
        isTagged = tagged;
    }
}
