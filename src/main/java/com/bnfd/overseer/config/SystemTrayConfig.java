package com.bnfd.overseer.config;

import com.bnfd.overseer.exception.OverseerException;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URI;

@Slf4j
public class SystemTrayConfig {
    private static TrayIcon trayIcon;

    // Menu Items:
    // - open
    // - export logs?
    // - settings?
    // - schedule?
    // - exit

    public static void setupTray() {
        PopupMenu popup = new PopupMenu();

        MenuItem open = new MenuItem("Open");
        open.addActionListener(event -> {
            log.info("Opening from tray");

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI("http://localhost:8080"));
            } catch (Exception e) {
                log.error("Failed to open browser: {}", e.getLocalizedMessage());
            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(event -> {
            log.info("Exiting from tray");

            SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);

            // TODO: add client call here to close window if currently opened

            System.exit(0);
        });

        popup.add(open);
        popup.add(exit);

        trayIcon = new TrayIcon(getIcon(), "Overseer", popup);
        trayIcon.setImageAutoSize(true);

        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.error("TrayIcon could not be added: {}", String.valueOf(e));
        }
    }

    private static Image getIcon() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            return toolkit.getImage("src/main/resources/icon.png");
        } catch (Exception e) {
            log.error("Failed to load tray icon: {}", e.getLocalizedMessage());
            throw new OverseerException(String.format("Error getting tray icon: %s", e.getLocalizedMessage()));
        }
    }
}
