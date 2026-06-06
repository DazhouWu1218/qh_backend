package com.njht.webyun.management.common.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Administrator
 */
public class RSAUtil {

    static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    // 加密算法
    private static String algorithm = "RSA";

    /**
     * 生成密钥对并保存在本地文件中
     *
     * @param algorithm : 算法
     * @param pubPath   : 公钥保存路径
     * @param priPath   : 私钥保存路径
     * @throws Exception
     */
    public static void generateKeyToFile(String algorithm, String pubPath, String priPath) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] publicKeyEncoded = publicKey.getEncoded();
            byte[] privateKeyEncoded = privateKey.getEncoded();

            String publicKeyString = Base64.getEncoder().encodeToString(publicKeyEncoded);
            String privateKeyString = Base64.getEncoder().encodeToString(privateKeyEncoded);
            // 保存公私钥到文件
            FileUtils.writeStringToFile(new File(pubPath), publicKeyString, "UTF-8");
            FileUtils.writeStringToFile(new File(priPath), privateKeyString, "UTF-8");
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

    }

    /**
     * @param privateKey
     * @param encrypted  : 密文
     * @return : 明文
     * @throws Exception
     */
    public static String decryptRSA(String privateKey, String encrypted) {
        try {
            if (!StringUtils.hasText(encrypted)) {
                return "";
            }
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            // 生成私钥
            PrivateKey key = keyFactory.generatePrivate(spec);

            // 加密
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = Base64.getDecoder().decode(encrypted);
            byte[] bytes1 = cipher.doFinal(decode);
            return new String(bytes1);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return "";
        }
    }

    /**
     * @param publicKey
     * @param input     : 明文
     * @return :密文
     * @throws Exception
     */
    public static String encryptRSA(String publicKey, String input) throws Exception {
        try {
            if (!StringUtils.hasText(input)) {
                return "";
            }
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));

            PublicKey key = keyFactory.generatePublic(spec);

            // 加密
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return "";
        }
    }
}