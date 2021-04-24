package de.neocraftr.labychatapi.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CryptManager {
    public static SecretKey createNewSharedKey() {
        try {
            KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(128);
            return key.generateKey();
        } catch (NoSuchAlgorithmException var1) {
            throw new Error(var1);
        }
    }

    public static KeyPair createNewKeyPair() {
        try {
            KeyPairGenerator keyPair = KeyPairGenerator.getInstance("RSA");
            keyPair.initialize(1024);
            return keyPair.generateKeyPair();
        } catch (NoSuchAlgorithmException err) {
            err.printStackTrace();
            System.out.println("Key pair generation failed!");
            return null;
        }
    }

    public static byte[] getServerIdHash(String input, PublicKey publicKey, SecretKey secretKey) {
        try {
            return digestOperation("SHA-1", input.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded());
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
            return null;
        }
    }

    private static byte[] digestOperation(String type, byte[]... bytes) {
        try {
            MessageDigest disgest = MessageDigest.getInstance(type);

            for (byte[] b : bytes) {
                disgest.update(b);
            }

            return disgest.digest();
        } catch (NoSuchAlgorithmException err) {
            err.printStackTrace();
            return null;
        }
    }

    public static PublicKey decodePublicKey(byte[] key) {
        try {
            X509EncodedKeySpec var1 = new X509EncodedKeySpec(key);
            KeyFactory var2 = KeyFactory.getInstance("RSA");
            return var2.generatePublic(var1);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {}

        System.out.println("Public key reconstitute failed!");
        return null;
    }

    public static SecretKey decryptSharedKey(PrivateKey privateKey, byte[] key) {
        return new SecretKeySpec(decryptData(privateKey, key), "AES");
    }

    public static byte[] encryptData(Key key, byte[] data) {
        return cipherOperation(1, key, data);
    }

    public static byte[] decryptData(Key key, byte[] data) {
        return cipherOperation(2, key, data);
    }

    private static byte[] cipherOperation(int mode, Key key, byte[] data) {
        try {
            return createTheCipherInstance(mode, key.getAlgorithm(), key).doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException err) {
            err.printStackTrace();
        }

        System.out.println("Cipher data failed!");
        return null;
    }

    private static Cipher createTheCipherInstance(int mode, String algorithm, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, key);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException err) {
            err.printStackTrace();
        }

        System.out.println("Cipher creation failed!");
        return null;
    }

    public static Cipher createNetCipherInstance(int opMode, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
            return cipher;
        } catch (GeneralSecurityException err) {
            throw new RuntimeException(err);
        }
    }
}