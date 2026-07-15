/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 */
package com.nonid.client.intiface;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.intiface.NonIntifaceClient;
import com.nonid.client.intiface.NonIntifaceDependencyManager;
import net.minecraft.text.Text;

public final class NonIntifaceBridge {
    private static final String CORE_CLASS = "io.github.blackspherefollower.buttplug4j.ButtplugException";
    private static final String CONNECTOR_CLASS = "io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client.ButtplugClientWSClient";
    private static final String JACKSON_CORE_CLASS = "com.fasterxml.jackson.core.JsonProcessingException";
    private static final String JACKSON_DATABIND_CLASS = "com.fasterxml.jackson.databind.ObjectMapper";
    private static volatile Boolean available;
    private static volatile boolean warnedUnavailable;

    private NonIntifaceBridge() {
    }

    public static boolean isRuntimeAvailable() {
        return NonIntifaceBridge.isAvailable();
    }

    public static Text statusText(NonConfig config) {
        if (!NonIntifaceBridge.isUsable()) {
            return Text.translatable((String)"config.needsofnature.intiface.status.unavailable");
        }
        try {
            return NonIntifaceClient.statusText(config);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
            return Text.translatable((String)"config.needsofnature.intiface.status.unavailable");
        }
    }

    public static boolean isConnected() {
        if (!NonIntifaceBridge.isUsable()) {
            return false;
        }
        try {
            return NonIntifaceClient.isConnected();
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
            return false;
        }
    }

    public static void onWorldJoin(NonConfig config) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.onWorldJoin(config);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void onWorldDisconnect(NonConfig config) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.onWorldDisconnect(config);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void connect(NonConfig config) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.connect(config);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void disconnect() {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.disconnect();
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void startScanning(NonConfig config) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.startScanning(config);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void stopAll() {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.stopAll();
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void testPulse(NonConfig config, boolean peak) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.testPulse(config, peak);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void pulseReactiveImpact(NonConfig config, double nextReactiveImpactDelayMs) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.pulseReactiveImpact(config, nextReactiveImpactDelayMs);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void pulsePeakReactiveImpact(NonConfig config, double nextReactiveImpactDelayMs) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.pulsePeakReactiveImpact(config, nextReactiveImpactDelayMs);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void clientTick(NonConfig config, boolean localAnimationActive, boolean peakStage, double specialVibratorScalar) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.clientTick(config, localAnimationActive, peakStage, specialVibratorScalar);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    public static void clientTick(NonConfig config, boolean localAnimationActive, boolean peakStage, double specialVibratorScalar, int energizedLevel, double energizedHeartPulse) {
        if (!NonIntifaceBridge.isUsable()) {
            return;
        }
        try {
            NonIntifaceClient.clientTick(config, localAnimationActive, peakStage, specialVibratorScalar, energizedLevel, energizedHeartPulse);
        }
        catch (LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
        }
    }

    private static boolean isAvailable() {
        Boolean cached = available;
        if (cached != null) {
            return cached;
        }
        try {
            ClassLoader loader = NonIntifaceBridge.class.getClassLoader();
            Class.forName(CORE_CLASS, false, loader);
            Class.forName(CONNECTOR_CLASS, false, loader);
            Class.forName(JACKSON_CORE_CLASS, false, loader);
            Class.forName(JACKSON_DATABIND_CLASS, false, loader);
            available = true;
            return true;
        }
        catch (ClassNotFoundException | LinkageError e) {
            NonIntifaceBridge.markUnavailable(e);
            return false;
        }
    }

    private static boolean isUsable() {
        return NonIntifaceBridge.isAvailable() && !NonIntifaceDependencyManager.generatedBundleOutdated() && !NonIntifaceDependencyManager.restartRequired();
    }

    private static void markUnavailable(Throwable error) {
        available = false;
        if (warnedUnavailable) {
            return;
        }
        warnedUnavailable = true;
        NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io integration is unavailable; disabling toy output.", error);
    }
}

