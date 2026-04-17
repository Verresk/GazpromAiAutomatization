package org.example;

import org.example.crypto.GhostCrypto;
import org.example.watcher.FolderWatcher;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        String folder = args.length > 0 ? args[0] : "output_data";

        byte[] key = GhostCrypto.generateKey();
        logger.info("Ключ ГОСТ сгенерирован, запуск наблюдателя за папкой: " + folder);

        FolderWatcher.watch(folder, key);
    }
}
