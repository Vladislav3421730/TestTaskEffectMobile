package com.example.testtaskeffectmobile.convertor;

import com.example.testtaskeffectmobile.exception.EncryptionFailedException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
@Slf4j
public class CardNumberCryptoConverter implements AttributeConverter<String, String> {

    @Value("${card.secret-key}")
    private String SECRET_KEY;

    @Value("${card.algorithm}")
    private String ALGORITHM;

    @Value("${card.iv-length}")
    private Integer IV_LENGTH;

    private Cipher getCipher(int mode, byte[] ivBytes) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, keySpec, iv);
        return cipher;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(IV_LENGTH);
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, iv);
            byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedWithIv = ByteBuffer.allocate(iv.length + encrypted.length)
                    .put(iv)
                    .put(encrypted)
                    .array();
            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            log.error("Failed to encrypt card number", e);
            throw new EncryptionFailedException("Card encryption failed");
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            byte[] encryptedIvAndText = Base64.getDecoder().decode(dbData);
            ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedIvAndText);
            byte[] iv = new byte[IV_LENGTH];
            byteBuffer.get(iv);
            byte[] encrypted = new byte[byteBuffer.remaining()];
            byteBuffer.get(encrypted);

            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, iv);
            String decrypted = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);

            int unmaskedLength = 4;
            int maskLength = Math.max(0, decrypted.length() - unmaskedLength);
            return "*".repeat(maskLength) + decrypted.substring(maskLength);
        } catch (Exception e) {
            log.error("Failed to decrypt card number", e);
            throw new EncryptionFailedException("Card decryption failed");
        }
    }
}

