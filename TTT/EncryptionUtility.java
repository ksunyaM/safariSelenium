package com.wba.rxrintegration.api.test.Pi_eRxSendMessage;

import com.voltage.securedata.enterprise.AES;
import com.voltage.securedata.enterprise.FPE;
import com.voltage.securedata.enterprise.LibraryContext;
import com.voltage.securedata.enterprise.VeException;

import java.util.Base64;

public class EncryptionUtility {
    static LibraryContext lcontext;
    static EncryptionProperty property;

    public static final String SIMPLE_API_JAVA_FPE_SAMPLE = "Simple_API_Java_FPE_Sample";
    public static final String VIBE_SIMPLE_JAVA = "vibesimplejava";

    static {
        System.loadLibrary(VIBE_SIMPLE_JAVA);
        System.out.println("SimpleAPI version: " + LibraryContext.getVersion());
    }

    public enum EncryptionType {
        FPE, AES
    }

    public EncryptionUtility(String propertiesPrefix) throws VeException {
        if (lcontext == null) {
            property = new EncryptionProperty(propertiesPrefix);
            lcontext = new LibraryContext.Builder()
                    .setPolicyURL(property.policyURL)
                    .setFileCachePath(property.cachePath)
                    .setTrustStorePath(property.trustStorePath)
                    .setClientIdProduct(SIMPLE_API_JAVA_FPE_SAMPLE, "1.2")
                    .build();
        }
    }


    public EncryptionUtility() throws VeException {
        this("default");

    }

    public String encrypt(String value, String keyId, EncryptionType encryptionType) throws VeException {
        IEncryptionKey keys = new EncryptionKeyFromFile();
        KeyID ids = keys.getKeyID(keyId);
        if (encryptionType == EncryptionType.FPE) {
            FPE fpe = lcontext.getFPEBuilder(ids.format)
                    .setSharedSecret(property.sharedSecret)
                    .setIdentity(ids.identifier)
                    .build();
            return fpe.protect(value);
        } else if (encryptionType == EncryptionType.AES) {
            AES aes = lcontext.getAESBuilder()
                    .setIdentity(ids.identifier)
                    .setSharedSecret(property.sharedSecret)
                    .build();
            return new String(Base64.getEncoder().encode(aes.protect(value.getBytes())));
        }
        return "";
    }

    public String decrypt(String value, String keyId, EncryptionType encryptionType) throws VeException {
        IEncryptionKey keys = new EncryptionKeyFromFile();
        KeyID ids = keys.getKeyID(keyId);
        if (encryptionType == EncryptionType.FPE) {
            FPE fpe = lcontext.getFPEBuilder(ids.format)
                    .setSharedSecret(property.sharedSecret)
                    .setIdentity(ids.identifier)
                    .build();
            return fpe.access(value);
        } else if (encryptionType == EncryptionType.AES) {
            AES aes = lcontext.getAESBuilder()
                    .setIdentity(ids.identifier)
                    .setSharedSecret(property.sharedSecret)
                    .build();
            return new String(aes.access((Base64.getDecoder().decode(value))));
        }
        return "";
    }

}
