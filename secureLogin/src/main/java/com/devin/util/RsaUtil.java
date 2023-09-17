package com.devin.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Devin
 * @createTime 2023/8/9 19:02
 * @desc RSA加解密工具类
 */
public class RsaUtil {

    public static Logger log = LoggerFactory.getLogger(RsaUtil.class);

    public static final String KEY_ALGORITHM = "RSA";
    /** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 加密
     *
     * @param publicKey 公钥字符串
     * @param plainText 明文
     * @return
     */
    public static String RSAEncode(String publicKey, String plainText) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey key = factory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encoded = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encoded);
        } catch (Exception e) {
            log.error("加密失败", e);
            throw new Exception("加密失败！！", e);
        }
    }

    /**
     * 解密
     *
     * @param privateKey  私钥字符串
     * @param encodedText 密文
     * @return
     */
    public static String RSADecode(String privateKey, String encodedText) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        byte[] encodedByte = Base64.decodeBase64(encodedText);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey key = factory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encodedByte));
        } catch (Exception e) {
            log.error("解密失败", e);
            throw new Exception("解密失败！！", e);
        }
    }

}
