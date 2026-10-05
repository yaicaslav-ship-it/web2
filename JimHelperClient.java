package com.example.jimhelper;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JimHelperClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("jimhelper");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[JimHelper] Initialized successfully for Minecraft 1.21.4!");
    }
}
