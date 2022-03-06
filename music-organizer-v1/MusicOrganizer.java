//Below are exercises 4.14, 4.15 and 4.16
import java.util.ArrayList;

/**
 * A class to hold details of audio files.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class MusicOrganizer
{

  public static void main(String[] args) {
    MusicOrganizer a = new MusicOrganizer();
    a.addFile("Hello");
    a.addFile("How are you?");
    a.addFile("I'm good thank you");
    a.addFile("Bye");
    a.validIndex(2);


  }
    // An ArrayList for storing the file names of music files.
    private ArrayList<String> files;

    /**
     * Create a MusicOrganizer
     */
    public MusicOrganizer()
    {
        files = new ArrayList<>();
    }

    /**
     * Add a file to the collection.
     * @param filename The file to be added.
     */
    public void addFile(String filename)
    {
        files.add(filename);
    }

    //Exercise 4.14
/*
    public int checkIndex(int valIndex) {
      int val = valIndex;
      if (val >= 0 && val < files.size()) {
        return val;
      }
      else {
        System.out.println("The valid range is from 0 to " + files.size());
        System.exit(0);
      }
      return val;
    }
*/

      //Exercise 4.15
    public boolean validIndex(int okIndex) {
      int ok = okIndex;
      if (ok >= 0 && ok < files.size()) {
        return true;
      }
      else {
        return false;
      }
    }

    /**
     * Return the number of files in the collection.
     * @return The number of files in the collection.
     */
    public int getNumberOfFiles()
    {
        return files.size();
    }

    /**
     * List a file from the collection.
     * @param index The index of the file to be listed.
     */
     //Exercise 4.16
    public void listFile(int index)
    {
        if(validIndex(index)) {
            String filename = files.get(index);
            System.out.println(filename);
        }
    }

    /**
     * Remove a file from the collection.
     * @param index The index of the file to be removed.
     */
    public void removeFile(int index)
    {
        if(validIndex(index)) {
            files.remove(index);
        }
    }
}
