package org.example.watcher;

import java.nio.file.*;
import java.util.logging.Logger;
import org.example.crypto.GhostCrypto;
import org.example.mail.MailSender;

public class FolderWatcher {

    private static final Logger log = Logger.getLogger(FolderWatcher.class.getName());

    public static void watch(String folderPath, byte[] key) throws Exception {
        Path dir = Paths.get(folderPath);
        if (!dir.isAbsolute()) {
            dir = Paths.get("..").resolve(folderPath).normalize();
        }
        Files.createDirectories(dir);

        WatchService watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

        log.info("Слушаю папку: " + dir.toAbsolutePath());

        while (true) {
            WatchKey watchKey = watcher.take();

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                String filename = event.context().toString();

                if (filename.endsWith(".xml")) {
                    Path xmlFile = dir.resolve(filename);
                    log.info("Новый файл: " + xmlFile);

                    byte[] xmlData = Files.readAllBytes(xmlFile);
                    byte[] hash = GhostCrypto.hash(xmlData);
                    log.info("Хэш Стрибог-256: " + bytesToHex(hash));

                    byte[] encrypted = GhostCrypto.encrypt(xmlData, key);
                    Path encFile = dir.resolve("snapshot.enc");
                    Files.write(encFile, encrypted);
                    log.info("Зашифровано: " + encFile);

                    MailSender.sendMail(encFile.toString());
                }
            }

            watchKey.reset();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}