package com.wba.rxrintegration.api.test.Pi_eRxSendMessage;

import com.wba.test.utils.PropertiesReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EncryptionKeyFromFile implements IEncryptionKey {
    public static List<KeyID> keys = null;

    @Override
    public KeyID getKeyID(String id) {
        readKeyID();
        return keys.stream().filter((keyID -> keyID.iD.equals(id))).findFirst().orElse(new KeyID());
    }

    private void readKeyID() {
        if (keys == null) {

            keys = new ArrayList<>();
            String propertiesFileName = "encryption-keys.properties";
            Properties properties = PropertiesReader.read(propertiesFileName, true);
            properties.forEach((k, v) -> {
                String keyid, property;
                String[] data = k.toString().split("\\.");
                keyid = data[1];
                property = data[2];
                int keyindex = getKeyIndex(keyid);
                try {
                    keys.get(keyindex).getClass().getDeclaredField(property).set(keys.get(keyindex), v);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private int getKeyIndex(String keyid) {
        for (int index = 0; index < keys.size(); index++) {
            if (keys.get(index).iD.equals(keyid)) {
                return index;
            }
        }
        KeyID newkey = new KeyID();
        newkey.iD = keyid;
        keys.add(newkey);
        return keys.size() - 1;
    }

}
