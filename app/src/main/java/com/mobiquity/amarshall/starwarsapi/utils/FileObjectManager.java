package com.mobiquity.amarshall.starwarsapi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.mobiquity.amarshall.starwarsapi.objects.Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by amarshall on 7/20/15.
 */
public class FileObjectManager {

    /**
     * Adds an Entry to the cache. Returns TRUE if Entry is being overwritten.
     *
     * @param _id Name of the entry.
     * @param _entry The Entry object you are adding.
     * @param _context The Context
     * @return boolean indicating if Entry was overwritten
     */
    public static boolean add_to_cache(String _id, Entry _entry, Context _context) {

        String file_name = _id + ".sw";
        boolean overwritten = false;

        try {
            FileOutputStream fos = _context.openFileOutput(file_name, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(_entry);
            oos.close();
            fos.close();

            overwritten = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            overwritten = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return overwritten;
    }

    /**
     * Checks to see if this name is in the Cache.
     *
     * If no entry exists, it returns null
     *
     * @param _id The name of the character you are checking
     * @param _context The context of the application
     * @return The entry if it exists. Otherwise Null.
     */
    public static Entry get_from_cache(String _id, Context _context) {

        Entry entry = null;
        String file_name = _id + ".sw";

        try {
            FileInputStream fis = _context.openFileInput(file_name);

            ObjectInputStream ois = new ObjectInputStream(fis);

            entry = (Entry) ois.readObject();

            ois.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return entry;
    }

}
