package ir.alirezaalijani.security.authorization.service.security.util;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

    private static KeyStore getKeyStore(String path, String password){
        final File keyStoreLocation=new File(path);
        try {
            final KeyStore keyStore = KeyStore.getInstance("JKS");
            if (keyStoreLocation.exists()){
                keyStore.load(new FileInputStream(keyStoreLocation), password.toCharArray());
                return keyStore;
            }
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair getKeyPair(String path,String password,String alias){
        var keyStore= getKeyStore(path,password);
        if (keyStore!=null){
            try {
                final Key key = keyStore.getKey(alias, password.toCharArray());
                final java.security.cert.Certificate cert = keyStore.getCertificate(alias);
                final PublicKey publicKey = cert.getPublicKey();
                return new KeyPair(publicKey, (PrivateKey) key);
            } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // use keytool is better
    public static void saveKeyToKeyStore(KeyPair keyPair, String path, String password, String alias) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, OperatorCreationException {
        final File keyStoreLocation;
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStoreLocation = new File(path);
        if (keyStoreLocation.exists()) {
            keyStore.load(new FileInputStream(keyStoreLocation), password.toCharArray());
        } else {
            keyStore.load(null, password.toCharArray());
        }
        final java.security.cert.Certificate wrapped = generateCertificate(keyPair);
        KeyStore.Entry entry = new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new java.security.cert.Certificate[]{wrapped});
        keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password.toCharArray()));
        keyStore.store(new FileOutputStream(keyStoreLocation), password.toCharArray());
    }

    private static Certificate generateCertificate(KeyPair keyPair) throws CertificateException, OperatorCreationException {
        X500Name name = new X500Name("cn=Annoying Wrapper");
        SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        final Date start = new Date();
        final Date until = Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
        final X509v3CertificateBuilder builder = new X509v3CertificateBuilder(name,
                new BigInteger(10, new SecureRandom()), //Choose something better for real use
                start,
                until,
                name,
                subPubKeyInfo
        );
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(new BouncyCastleProvider()).build(keyPair.getPrivate());
        final X509CertificateHolder holder = builder.build(signer);
        return new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(holder);
    }

    private RSAKeyUtil() {}

}
