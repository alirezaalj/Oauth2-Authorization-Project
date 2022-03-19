package ir.alirezaalijani.security.authorization.service.security.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyUtil {

    public static KeyPair generateNewRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    public static boolean saveKeyToDir(KeyPair keyPair,String path){
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        // Store Public Key.
        try {
            Path publicKeyPath= Paths.get(path, "public.key");
            EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                    publicKey.getEncoded());
            Files.write(publicKeyPath,x509EncodedKeySpec.getEncoded());
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        // Store Private Key.
        try {
            Path privateKeyPath=Paths.get(path,"private.key");
            EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            Files.write(privateKeyPath,pkcs8EncodedKeySpec.getEncoded());
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static KeyPair loadKeyFromDir(String path){
        PublicKey publicKey;
        PrivateKey privateKey;
        // Read Public Key.
        Path publicKeyPath= Path.of(path,"public.key");
        try {
            byte[] encodedPublicKey = Files.readAllBytes(publicKeyPath);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    encodedPublicKey);
            publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
        }catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        // Read Private Key.
        Path privateKeyPath=Path.of(path,"private.key");
        try{
            byte[] encodedPrivateKey = Files.readAllBytes(privateKeyPath);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivateKey);
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
        }catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        return new KeyPair(publicKey, privateKey);
    }

    private RSAKeyUtil() {}

}
