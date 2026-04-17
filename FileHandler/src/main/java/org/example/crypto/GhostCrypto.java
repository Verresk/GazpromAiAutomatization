package org.example.crypto;

import org.bouncycastle.crypto.digests.GOST3411_2012_256Digest;
import org.bouncycastle.crypto.engines.GOST3412_2015Engine;
import org.bouncycastle.crypto.modes.GCFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.SecureRandom;

public class GhostCrypto {

    public static byte[] hash(byte[] data) {
        GOST3411_2012_256Digest digest = new GOST3411_2012_256Digest();
        digest.update(data, 0, data.length);
        byte[] result = new byte[32];
        digest.doFinal(result, 0);
        return result;
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        GCFBBlockCipher cipher = new GCFBBlockCipher(new GOST3412_2015Engine());
        cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv));

        byte[] encrypted = new byte[data.length];
        cipher.processBytes(data, 0, data.length, encrypted, 0);

        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
        return result;
    }

    public static byte[] generateKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return key;
    }
}
