package ir.alirezaalijani.security.authorization.service.security.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import static org.junit.jupiter.api.Assertions.*;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;


public class JasyptEncryptTest {

    private static final String encKey="password1515";
    private static final String dataToEncrypt="some string data to enc";
    private static final String encDataToDecrypt="pdPgxuBr9sft0qhQThS6cOJf96mCI3gtCYzzJ7NxeH0VgqXgipwqK9xgpghzHaBN7qKdFfnbl1uINYYOS4peLg==";

    @Test
    void jasyptTest1(){
        System.out.println("--- StandardPBEStringEncryptor ---");
        StandardPBEStringEncryptor encryptor=new StandardPBEStringEncryptor();
        encryptor.setPassword(encKey);
                                // PBEWithHMACSHA512AndAES_256
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());

        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("enc string:"+encData2);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
        assertEquals(encryptor.decrypt(encDataToDecrypt),dataToEncrypt);
    }

    @Test
    void jasyptTest2(){
        System.out.println("--- StrongPasswordEncryptor ---");
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(dataToEncrypt);
        System.out.println("encryptedPassword:"+encryptedPassword);
        assertTrue(passwordEncryptor.checkPassword(dataToEncrypt, encryptedPassword));
    }

    @Test
    void jasyptTest3(){
        System.out.println("--- BasicTextEncryptor ---");
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encKey);
        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("encData1:"+encData1);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
    }

    @Test
    void jasyptTest4(){
        System.out.println("--- StrongTextEncryptor ---");
        StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword(encKey);
        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("encData1:"+encData1);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
    }

    @Test
    void jasyptTest5(){
        System.out.println("--- AES256TextEncryptor ---");
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encKey);
        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("encData1:"+encData1);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
    }

    @Test
    void springEncryptorsTest1(){
        System.out.println("--- TextEncryptor text ---");
        TextEncryptor encryptor = Encryptors.text(encKey,"5c0744940b5c369b");
        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("encData1:"+encData1);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
    }
    @Test
    void springEncryptorsTest(){
        System.out.println("--- TextEncryptor delux ---");
        TextEncryptor encryptor = Encryptors.delux(encKey,"5c0744940b5c369b");
        var encData1 = encryptor.encrypt(dataToEncrypt);
        var encData2=encryptor.encrypt(dataToEncrypt);
        System.out.println("encData1:"+encData1);
        assertNotEquals(encData1,encData2);
        assertEquals(encryptor.decrypt(encData2),dataToEncrypt);
    }
}
