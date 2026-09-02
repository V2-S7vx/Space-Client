package com.spaceclient;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SpaceClient implements ClientModInitializer {
    public static final String MOD_ID = "spaceclient";
    public static final String NAME = "Space Client";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public void onInitializeClient() {
        LOGGER.info("{} {} initialized on Minecraft 1.21.11", NAME, getVersion());
    }

    private static String getVersion() {
        return "0.1.0";
    }
}
