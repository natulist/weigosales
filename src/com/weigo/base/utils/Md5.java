package com.weigo.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

import com.app.framework.log.NLog;


/**
 * @author difei.zou
 * @Description: 来自 the CyanogenMod 10.2 android ROM
 * @date 2014/10/20 15:35
 * @copyright TCL-MIE
 */


public class Md5 {
    private static final String TAG = "MD5";

    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            NLog.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
        	NLog.e(TAG, "calculatedDigest null");
            return false;
        }

        NLog.i(TAG, "Calculated digest: " + calculatedDigest);
        NLog.i(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(File fileTo) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        	NLog.e(TAG, "Exception while getting digest");
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(fileTo);
        } catch (FileNotFoundException e) {
        	NLog.e(TAG, "Exception while getting FileInputStream");
            return null;
        }

        byte[] buffer = new byte[1024*128];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                NLog.e(TAG, "Exception on closing MD5 input stream");
            }
        }
    }
}