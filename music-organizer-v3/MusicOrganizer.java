//Below are exercises 4.24, 4.26 and 4.27 for music-organizer-v3

import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * @author David J. Barnes and Michael Kölling
 * @version 2017.09.23
 */
public class MusicOrganizer
{
    // An ArrayList for storing the file names of music files.
    private ArrayList<String> files;
    // A player for the music files.
    private MusicPlayer player;

    //Constructor
    public MusicOrganizer()
    {
        files = new ArrayList<>();
        player = new MusicPlayer();
    }

    public static void main(String[] args) {
      MusicOrganizer a = new MusicOrganizer();
      a.addFile("../audio/BlindLemonJefferson-OneDimeBlues-8kbps.mp3");
      a.addFile("../audio/BlindLemonJefferson-matchBoxBlues-8kbps.mp3");
      a.addFile("../audio/BlindBlake-EarlyMorningBlues-8kbps.mp3");
      a.addFile("../audio/BigBillBroonzy-BabyPleaseDontGo1-8kbps.mp3");
      a.listAllFiles();
      a.listMatching("Nej");
      a.playList("BlindLemonJefferson");
    }

    //Exercise 4.27
    public void playList(String artist) {

      int position;

      for(String filename : files) {
        position = files.indexOf(filename);

        if(filename.contains(artist)) {
          playAndWait(position);
        }
      }
    }

    /**
     * @param filename The file to be added.
     */
    public void addFile(String filename)
    {
        files.add(filename);
    }

    /**
     * @return The number of files in the collection.
     */
    public int getNumberOfFiles()
    {
        return files.size();
    }

    /**
     * @param index
     */
    public void listFile(int index)
    {
        if(validIndex(index)) {
            String filename = files.get(index);
            System.out.println(filename);
        }
    }


     //Exercise 4.24
    public void listAllFiles()
    {
      int position;
        for(String filename : files) {
          position = files.indexOf(filename);
            System.out.println(position + ":" + filename);
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

    /**
     * Start playing a file in the collection.
     * Use stopPlaying() to stop it playing.
     * @param index The index of the file to be played.
     */
    public void startPlaying(int index)
    {
        if(validIndex(index)) {
            String filename = files.get(index);
            player.startPlaying(filename);
        }
    }

    /**
     * Stop the player.
     */
    public void stopPlaying()
    {
        player.stop();
    }

    /**
     * Play a file in the collection. Only return once playing has finished.
     * @param index The index of the file to be played.
     */
    public void playAndWait(int index)
    {
        if(validIndex(index)) {
            String filename = files.get(index);
            player.playSample(filename);
        }
    }

    /**
     * @param index The index to be checked.
     * @return true if the index is valid, false otherwise.
     */
    private boolean validIndex(int index)
    {
        boolean valid;

        if(index < 0) {
            System.out.println("Index cannot be negative: " + index);
            valid = false;
        }
        else if(index >= files.size()) {
            System.out.println("Index is too large: " + index);
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }

    /**
     * @param searchString The string to match.
     */
     //Exercise 4.26
    public void listMatching(String searchString)
    {
      boolean f = false;
        for(String filename : files) {
            if(filename.contains(searchString)) {
                // A match.
                System.out.println(filename);
                f = true;
            }
        }
        if(f == false) {
          System.out.println("No file name matches the search");
        }

    }

  //________________________________________________________________________________________________________-
  //________________________________________________________________________________________________________-

/**
 * Provide basic playing of MP3 files via the javazoom library.
 * See http://www.javazoom.net/
 *
 * @author David J. Barnes and Michael Kölling.
 * @version 2016.02.29
 */
public class MusicPlayer
{
    // The current player. It might be null.
    private AdvancedPlayer player;

    /**
     * Constructor for objects of class MusicFilePlayer
     */
    public MusicPlayer()
    {
        player = null;
    }

    /**
     * Play a part of the given file.
     * The method returns once it has finished playing.
     * @param filename The file to be played.
     */
    public void playSample(String filename)
    {
        try {
            setupPlayer(filename);
            player.play(500);
        }
        catch(JavaLayerException e) {
            reportProblem(filename);
        }
        finally {
            killPlayer();
        }
    }

    /**
     * Start playing the given audio file.
     * The method returns once the playing has been started.
     * @param filename The file to be played.
     */
    public void startPlaying(final String filename)
    {
        try {
            setupPlayer(filename);
            Thread playerThread = new Thread() {
                public void run()
                {
                    try {
                        player.play(5000);
                    }
                    catch(JavaLayerException e) {
                        reportProblem(filename);
                    }
                    finally {
                        killPlayer();
                    }
                }
            };
            playerThread.start();
        }
        catch (Exception ex) {
            reportProblem(filename);
        }
    }

    public void stop()
    {
        killPlayer();
    }

    /**
     * Set up the player ready to play the given file.
     * @param filename The name of the file to play.
     */
    private void setupPlayer(String filename)
    {
        try {
            InputStream is = getInputStream(filename);
            player = new AdvancedPlayer(is, createAudioDevice());
        }
        catch (IOException e) {
            reportProblem(filename);
            killPlayer();
        }
        catch(JavaLayerException e) {
            reportProblem(filename);
            killPlayer();
        }
    }

    /**
     * Return an InputStream for the given file.
     * @param filename The file to be opened.
     * @throws IOException If the file cannot be opened.
     * @return An input stream for the file.
     */
    private InputStream getInputStream(String filename)
        throws IOException
    {
        return new BufferedInputStream(
                    new FileInputStream(filename));
    }

    /**
     * Create an audio device.
     * @throws JavaLayerException if the device cannot be created.
     * @return An audio device.
     */
    private AudioDevice createAudioDevice()
        throws JavaLayerException
    {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }

    /**
     * Terminate the player, if there is one.
     */
    private void killPlayer()
    {
        synchronized(this) {
            if(player != null) {
                player.stop();
                player = null;
            }
        }
    }

    /**
     * Report a problem playing the given file.
     * @param filename The file being played.
     */
    private void reportProblem(String filename)
    {
        System.out.println("There was a problem playing: " + filename);
    }
}
}
