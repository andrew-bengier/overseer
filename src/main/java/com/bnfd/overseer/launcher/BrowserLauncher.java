package com.bnfd.overseer.launcher;

import org.springframework.boot.context.event.*;
import org.springframework.context.annotation.*;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.*;

@Component
@Profile("!docker")
public class BrowserLauncher {

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        System.setProperty("java.awt.headless", "false");
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("http://localhost:8080"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
