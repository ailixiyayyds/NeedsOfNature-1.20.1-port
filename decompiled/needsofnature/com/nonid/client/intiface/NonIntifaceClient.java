/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice
 *  io.github.blackspherefollower.buttplug4j.client.ButtplugOutput
 *  io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client.ButtplugClientWSClient
 *  io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage
 *  net.minecraft.class_2561
 */
package com.nonid.client.intiface;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugOutput;
import io.github.blackspherefollower.buttplug4j.connectors.jetty.websocket.client.ButtplugClientWSClient;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.minecraft.class_2561;

public final class NonIntifaceClient {
    private static final int CONNECT_TIMEOUT_MS = 8000;
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "NoN Intiface");
        thread.setDaemon(true);
        return thread;
    });
    private static volatile ButtplugClientWSClient client;
    private static volatile State state;
    private static volatile String detail;
    private static volatile long lastPulseMs;
    private static volatile long pulseToken;
    private static volatile ScheduledFuture<?> pendingStop;
    private static volatile ScheduledFuture<?> pendingStrokerReturn;
    private static volatile boolean requestedAnimationActive;
    private static volatile boolean requestedPeakStage;
    private static volatile boolean requestedIntifaceEnabled;
    private static volatile int requestedBaselineStrengthPercent;
    private static volatile int requestedOscillatorRegularSpeedPercent;
    private static volatile int requestedOscillatorPeakSpeedPercent;
    private static volatile double requestedSpecialVibratorScalar;
    private static volatile int requestedEnergizedLevel;
    private static volatile int requestedEnergizedBasePercent;
    private static volatile int requestedEnergizedPulsePercent;
    private static volatile int requestedEnergizedPulsePermille;
    private static volatile boolean baselineActive;
    private static volatile int baselineStrengthPercent;
    private static volatile boolean oscillatorActive;
    private static volatile int oscillatorSpeedPercent;
    private static volatile boolean specialVibratorActive;
    private static volatile double specialVibratorScalar;
    private static volatile boolean energizedActive;
    private static volatile int energizedBasePercent;
    private static volatile int energizedPulsePercent;
    private static volatile double energizedPulse;
    private static volatile double currentScalar;
    private static volatile double currentOscillatorScalar;
    private static volatile boolean suppressBaselineUntilInactive;
    private static volatile long connectAttemptId;

    private NonIntifaceClient() {
    }

    public static void onWorldJoin(NonConfig config) {
        if (config == null || !config.isIntifaceEnabled() || !config.isIntifaceAutoConnectOnWorldJoin()) {
            NonIntifaceClient.updateDisabledState(config);
            return;
        }
        NonIntifaceClient.connect(config);
    }

    public static void onWorldDisconnect(NonConfig config) {
        if (config != null && config.isIntifaceStopOnDisconnect()) {
            NonIntifaceClient.stopAll();
        }
        NonIntifaceClient.disconnect();
    }

    public static void connect(NonConfig config) {
        if (config == null || !config.isIntifaceEnabled()) {
            state = State.DISABLED;
            detail = "";
            return;
        }
        String url = config.getIntifaceServerUrl();
        NeedsOfNature.LOGGER.info("[NoN] Buttplug.io connect requested: url={}", (Object)url);
        NonIntifaceClient.executeSafely("Connect failed", () -> NonIntifaceClient.connectInternal(url));
    }

    public static void disconnect() {
        NonIntifaceClient.executeSafely("Disconnect failed", () -> {
            NonIntifaceClient.cancelPendingStop();
            NonIntifaceClient.cancelPendingStrokerReturn();
            NonIntifaceClient.clearBaselineState();
            NonIntifaceClient.disconnectInternal();
            state = State.DISCONNECTED;
            detail = "";
        });
    }

    public static void startScanning(NonConfig config) {
        if (config == null || !config.isIntifaceEnabled()) {
            state = State.DISABLED;
            detail = "";
            return;
        }
        NonIntifaceClient.executeSafely("Scan failed", () -> {
            NonIntifaceClient.connectInternal(config.getIntifaceServerUrl());
            ButtplugClientWSClient activeClient = client;
            if (activeClient == null || !activeClient.isConnected()) {
                return;
            }
            try {
                state = State.SCANNING;
                detail = "";
                activeClient.startScanning();
            }
            catch (Exception e) {
                NonIntifaceClient.setError("Scan failed", e);
            }
        });
    }

    public static void stopAll() {
        NonIntifaceClient.executeSafely("Stop failed", () -> {
            suppressBaselineUntilInactive = true;
            baselineActive = false;
            oscillatorActive = false;
            specialVibratorActive = false;
            specialVibratorScalar = 0.0;
            energizedActive = false;
            energizedBasePercent = 0;
            energizedPulsePercent = 0;
            energizedPulse = 0.0;
            NonIntifaceClient.cancelPendingStop();
            NonIntifaceClient.cancelPendingStrokerReturn();
            NonIntifaceClient.stopAllInternal(true);
        });
    }

    public static void testPulse(NonConfig config, boolean peak) {
        if (peak) {
            NonIntifaceClient.pulsePeakReactiveImpact(config);
        } else {
            NonIntifaceClient.pulseReactiveImpact(config);
        }
    }

    public static void pulseReactiveImpact(NonConfig config) {
        NonIntifaceClient.pulseReactiveImpact(config, 0.0);
    }

    public static void pulseReactiveImpact(NonConfig config, double nextReactiveImpactDelayMs) {
        if (config == null || !config.isIntifaceEnabled()) {
            return;
        }
        NonIntifaceClient.queueReactiveImpact(config, false, nextReactiveImpactDelayMs);
    }

    public static void pulsePeakReactiveImpact(NonConfig config) {
        NonIntifaceClient.pulsePeakReactiveImpact(config, 0.0);
    }

    public static void pulsePeakReactiveImpact(NonConfig config, double nextReactiveImpactDelayMs) {
        if (config == null || !config.isIntifaceEnabled()) {
            return;
        }
        NonIntifaceClient.queueReactiveImpact(config, true, nextReactiveImpactDelayMs);
    }

    public static void clientTick(NonConfig config, boolean localAnimationActive, boolean peakStage) {
        NonIntifaceClient.clientTick(config, localAnimationActive, peakStage, Double.NaN);
    }

    public static void clientTick(NonConfig config, boolean localAnimationActive, boolean peakStage, double specialVibratorScalar) {
        NonIntifaceClient.clientTick(config, localAnimationActive, peakStage, specialVibratorScalar, 0, 0.0);
    }

    public static void clientTick(NonConfig config, boolean localAnimationActive, boolean peakStage, double specialVibratorScalar, int energizedLevel, double energizedHeartPulse) {
        boolean enabled = config != null && config.isIntifaceEnabled();
        int strengthPercent = enabled ? config.getIntifaceAnimationBaselineStrengthPercent() : 0;
        int oscillatorRegularSpeedPercent = enabled ? config.getIntifaceOscillatorRegularSpeedPercent() : 0;
        int oscillatorPeakSpeedPercent = enabled ? config.getIntifaceOscillatorPeakSpeedPercent() : 0;
        double sanitizedSpecialScalar = NonIntifaceClient.sanitizeSpecialVibratorScalar(specialVibratorScalar);
        int sanitizedEnergizedLevel = enabled ? Math.max(0, Math.min(3, energizedLevel)) : 0;
        int energizedBase = sanitizedEnergizedLevel > 0 ? config.getIntifaceEnergizedBasePercent(sanitizedEnergizedLevel) : 0;
        int energizedPeak = sanitizedEnergizedLevel > 0 ? config.getIntifaceEnergizedPulsePercent(sanitizedEnergizedLevel) : 0;
        double sanitizedEnergizedPulse = NonIntifaceClient.sanitizeUnit(energizedHeartPulse);
        int pulsePermille = (int)Math.round(sanitizedEnergizedPulse * 1000.0);
        if (!localAnimationActive && suppressBaselineUntilInactive) {
            suppressBaselineUntilInactive = false;
        }
        if (localAnimationActive == requestedAnimationActive && peakStage == requestedPeakStage && strengthPercent == requestedBaselineStrengthPercent && oscillatorRegularSpeedPercent == requestedOscillatorRegularSpeedPercent && oscillatorPeakSpeedPercent == requestedOscillatorPeakSpeedPercent && NonIntifaceClient.specialScalarEquals(sanitizedSpecialScalar, requestedSpecialVibratorScalar) && sanitizedEnergizedLevel == requestedEnergizedLevel && energizedBase == requestedEnergizedBasePercent && energizedPeak == requestedEnergizedPulsePercent && pulsePermille == requestedEnergizedPulsePermille && enabled == requestedIntifaceEnabled) {
            return;
        }
        requestedAnimationActive = localAnimationActive;
        requestedPeakStage = peakStage;
        requestedIntifaceEnabled = enabled;
        requestedBaselineStrengthPercent = strengthPercent;
        requestedOscillatorRegularSpeedPercent = oscillatorRegularSpeedPercent;
        requestedOscillatorPeakSpeedPercent = oscillatorPeakSpeedPercent;
        requestedSpecialVibratorScalar = sanitizedSpecialScalar;
        requestedEnergizedLevel = sanitizedEnergizedLevel;
        requestedEnergizedBasePercent = energizedBase;
        requestedEnergizedPulsePercent = energizedPeak;
        requestedEnergizedPulsePermille = pulsePermille;
        NonIntifaceClient.executeSafely("Update failed", () -> NonIntifaceClient.updateAnimationOutputs(enabled, localAnimationActive, peakStage, strengthPercent, oscillatorRegularSpeedPercent, oscillatorPeakSpeedPercent, sanitizedSpecialScalar, sanitizedEnergizedLevel, energizedBase, energizedPeak, sanitizedEnergizedPulse));
    }

    public static class_2561 statusText(NonConfig config) {
        NonIntifaceClient.updateDisabledState(config);
        int devices = NonIntifaceClient.deviceCount();
        String suffix = detail == null || detail.isBlank() ? "" : " - " + detail;
        return class_2561.method_43469((String)("config.needsofnature.intiface.status." + state.name().toLowerCase(Locale.ROOT)), (Object[])new Object[]{devices}).method_10852((class_2561)class_2561.method_43470((String)suffix));
    }

    public static int deviceCount() {
        ButtplugClientWSClient activeClient = client;
        if (activeClient == null || !activeClient.isConnected()) {
            return 0;
        }
        try {
            return activeClient.getDevices().size();
        }
        catch (RuntimeException e) {
            return 0;
        }
    }

    public static boolean isConnected() {
        ButtplugClientWSClient activeClient = client;
        return activeClient != null && activeClient.isConnected();
    }

    private static void queueReactiveImpact(NonConfig config, boolean peak, double nextReactiveImpactDelayMs) {
        int strengthPercent = peak ? config.getIntifacePeakReactiveImpactStrengthPercent() : config.getIntifaceReactiveImpactStrengthPercent();
        int durationMs = peak ? config.getIntifacePeakReactiveImpactDurationMs() : config.getIntifaceReactiveImpactDurationMs();
        int cooldownMs = config.getIntifaceCooldownMs();
        double scalar = Math.max(NonIntifaceClient.sustainedVibratorScalar(), Math.max(0.0, Math.min(1.0, (double)strengthPercent / 100.0)));
        int strokerMin = config.getIntifaceStrokerMinDistancePercent();
        int strokerMax = config.getIntifaceStrokerMaxDistancePercent();
        int strokerMoveDurationMs = peak ? config.getIntifaceStrokerPeakMoveDurationMs() : config.getIntifaceStrokerRegularMoveDurationMs();
        NonIntifaceClient.executeSafely("Pulse failed", () -> {
            ButtplugClientWSClient activeClient = client;
            if (activeClient == null || !activeClient.isConnected()) {
                return;
            }
            long now = System.currentTimeMillis();
            if (cooldownMs > 0 && now - lastPulseMs < (long)cooldownMs) {
                return;
            }
            boolean sentAny = false;
            if (!specialVibratorActive && scalar > 0.0 && durationMs > 0) {
                sentAny = NonIntifaceClient.sendVibrate(activeClient, scalar) || sentAny;
                long token = ++pulseToken;
                NonIntifaceClient.cancelPendingStop();
                pendingStop = EXECUTOR.schedule(() -> {
                    if (token == pulseToken) {
                        NonIntifaceClient.returnToBaselineOrStop(activeClient);
                        pendingStop = null;
                    }
                }, (long)durationMs, TimeUnit.MILLISECONDS);
            }
            boolean bl = sentAny = NonIntifaceClient.triggerStrokerImpact(activeClient, strokerMin, strokerMax, strokerMoveDurationMs, nextReactiveImpactDelayMs) || sentAny;
            if (sentAny) {
                lastPulseMs = now;
            }
        });
    }

    private static void updateAnimationOutputs(boolean enabled, boolean localAnimationActive, boolean peakStage, int strengthPercent, int oscillatorRegularSpeedPercent, int oscillatorPeakSpeedPercent, double specialScalar, int energizedLevel, int energizedBaseStrengthPercent, int energizedPulseStrengthPercent, double energizedHeartPulse) {
        double vibrateScalar;
        boolean shouldUseOscillator;
        boolean shouldUseEnergized;
        boolean shouldUseBaseline;
        if (!localAnimationActive) {
            suppressBaselineUntilInactive = false;
        }
        boolean shouldUseSpecialVibrator = enabled && localAnimationActive && !Double.isNaN(specialScalar) && !suppressBaselineUntilInactive;
        int clampedStrength = Math.max(0, Math.min(100, strengthPercent));
        baselineActive = shouldUseBaseline = enabled && localAnimationActive && clampedStrength > 0 && !suppressBaselineUntilInactive && !shouldUseSpecialVibrator;
        baselineStrengthPercent = shouldUseBaseline ? clampedStrength : 0;
        specialVibratorActive = shouldUseSpecialVibrator;
        specialVibratorScalar = shouldUseSpecialVibrator ? Math.max(0.0, Math.min(1.0, specialScalar)) : 0.0;
        int clampedEnergizedBase = Math.max(0, Math.min(100, energizedBaseStrengthPercent));
        int clampedEnergizedPeak = Math.max(0, Math.min(100, energizedPulseStrengthPercent));
        energizedActive = shouldUseEnergized = enabled && energizedLevel > 0 && Math.max(clampedEnergizedBase, clampedEnergizedPeak) > 0 && !shouldUseSpecialVibrator;
        energizedBasePercent = shouldUseEnergized ? clampedEnergizedBase : 0;
        energizedPulsePercent = shouldUseEnergized ? clampedEnergizedPeak : 0;
        energizedPulse = shouldUseEnergized ? NonIntifaceClient.sanitizeUnit(energizedHeartPulse) : 0.0;
        int requestedOscillatorSpeed = Math.max(0, Math.min(100, peakStage ? oscillatorPeakSpeedPercent : oscillatorRegularSpeedPercent));
        oscillatorActive = shouldUseOscillator = enabled && localAnimationActive && requestedOscillatorSpeed > 0 && !suppressBaselineUntilInactive;
        int n = oscillatorSpeedPercent = shouldUseOscillator ? requestedOscillatorSpeed : 0;
        if (shouldUseSpecialVibrator) {
            NonIntifaceClient.cancelPendingStop();
        }
        if (!(shouldUseBaseline || shouldUseOscillator || shouldUseSpecialVibrator || shouldUseEnergized)) {
            NonIntifaceClient.cancelPendingStop();
            NonIntifaceClient.cancelPendingStrokerReturn();
            if (currentScalar > 0.0) {
                NonIntifaceClient.sendVibrate(client, 0.0);
            }
            if (currentOscillatorScalar > 0.0) {
                NonIntifaceClient.sendOscillate(client, 0.0);
            }
            return;
        }
        ButtplugClientWSClient activeClient = client;
        if (activeClient == null || !activeClient.isConnected()) {
            return;
        }
        if (pendingStop != null && !shouldUseSpecialVibrator) {
            return;
        }
        double d = vibrateScalar = shouldUseSpecialVibrator ? specialVibratorScalar : NonIntifaceClient.sustainedVibratorScalar();
        if (vibrateScalar > 0.0 && Math.abs(currentScalar - vibrateScalar) > 0.001) {
            NonIntifaceClient.sendVibrate(activeClient, vibrateScalar);
        } else if (vibrateScalar <= 0.0 && currentScalar > 0.0) {
            NonIntifaceClient.sendVibrate(activeClient, 0.0);
        }
        double oscillateScalar = NonIntifaceClient.oscillatorScalar();
        if (oscillateScalar > 0.0 && Math.abs(currentOscillatorScalar - oscillateScalar) > 0.001) {
            NonIntifaceClient.sendOscillate(activeClient, oscillateScalar);
        } else if (oscillateScalar <= 0.0 && currentOscillatorScalar > 0.0) {
            NonIntifaceClient.sendOscillate(activeClient, 0.0);
        }
    }

    private static double baselineScalar() {
        if (!baselineActive || baselineStrengthPercent <= 0) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, (double)baselineStrengthPercent / 100.0));
    }

    private static double energizedScalar() {
        if (!energizedActive) {
            return 0.0;
        }
        double base = Math.max(0.0, Math.min(1.0, (double)energizedBasePercent / 100.0));
        double peak = Math.max(0.0, Math.min(1.0, (double)energizedPulsePercent / 100.0));
        return base + (peak - base) * NonIntifaceClient.sanitizeUnit(energizedPulse);
    }

    private static double sustainedVibratorScalar() {
        return Math.max(NonIntifaceClient.baselineScalar(), NonIntifaceClient.energizedScalar());
    }

    private static double oscillatorScalar() {
        if (!oscillatorActive || oscillatorSpeedPercent <= 0) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, (double)oscillatorSpeedPercent / 100.0));
    }

    private static double sanitizeSpecialVibratorScalar(double scalar) {
        if (Double.isNaN(scalar)) {
            return Double.NaN;
        }
        if (Double.isInfinite(scalar)) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, scalar));
    }

    private static double sanitizeUnit(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, value));
    }

    private static boolean specialScalarEquals(double first, double second) {
        if (Double.isNaN(first) || Double.isNaN(second)) {
            return Double.isNaN(first) && Double.isNaN(second);
        }
        return Math.abs(first - second) < 0.001;
    }

    private static void returnToBaselineOrStop(ButtplugClientWSClient activeClient) {
        double scalar = NonIntifaceClient.sustainedVibratorScalar();
        if (scalar > 0.0 && activeClient != null && activeClient.isConnected() && NonIntifaceClient.sendVibrate(activeClient, scalar)) {
            return;
        }
        if (activeClient != null && activeClient.isConnected()) {
            NonIntifaceClient.sendVibrate(activeClient, 0.0);
        } else {
            currentScalar = 0.0;
        }
    }

    private static void connectInternal(String url) {
        long attemptId = ++connectAttemptId;
        ButtplugClientWSClient activeClient = client;
        if (activeClient != null && activeClient.isConnected()) {
            state = State.CONNECTED;
            detail = "";
            NeedsOfNature.LOGGER.info("[NoN] Buttplug.io connect skipped: already connected. attempt={}", (Object)attemptId);
            return;
        }
        NonIntifaceClient.disconnectInternal();
        ButtplugClientWSClient nextClient = null;
        try {
            state = State.CONNECTING;
            detail = "Connecting to " + url;
            NeedsOfNature.LOGGER.info("[NoN] Buttplug.io connect started: attempt={} url={} timeout={}ms", new Object[]{attemptId, url, 8000});
            nextClient = new ButtplugClientWSClient("NeedsOfNature");
            nextClient.setDeviceAddedHandler(device -> {
                if (attemptId != connectAttemptId) {
                    return;
                }
                state = State.CONNECTED;
                detail = class_2561.method_43469((String)"config.needsofnature.intiface.status.device_added", (Object[])new Object[]{device.getDisplayName()}).getString();
                NonIntifaceClient.updateAnimationOutputs(requestedIntifaceEnabled, requestedAnimationActive, requestedPeakStage, requestedBaselineStrengthPercent, requestedOscillatorRegularSpeedPercent, requestedOscillatorPeakSpeedPercent, requestedSpecialVibratorScalar, requestedEnergizedLevel, requestedEnergizedBasePercent, requestedEnergizedPulsePercent, (double)requestedEnergizedPulsePermille / 1000.0);
            });
            nextClient.setDeviceRemovedHandler(index -> {
                if (attemptId != connectAttemptId) {
                    return;
                }
                state = State.CONNECTED;
                detail = class_2561.method_43471((String)"config.needsofnature.intiface.status.device_removed").getString();
            });
            nextClient.setScanningFinishedHandler(() -> {
                if (attemptId != connectAttemptId) {
                    return;
                }
                state = State.CONNECTED;
                detail = class_2561.method_43471((String)"config.needsofnature.intiface.status.scan_finished").getString();
            });
            nextClient.setErrorHandler(error -> {
                if (attemptId != connectAttemptId) {
                    return;
                }
                NonIntifaceClient.setError(error.getErrorMessage(), (Throwable)error.getException());
            });
            ButtplugClientWSClient connectClient = nextClient;
            FutureTask<Void> connectTask = new FutureTask<Void>(() -> {
                connectClient.connect(new URI(url));
                return null;
            });
            Thread connectThread = new Thread(connectTask, "NoN Intiface Connect");
            connectThread.setDaemon(true);
            connectThread.start();
            connectTask.get(8000L, TimeUnit.MILLISECONDS);
            if (attemptId != connectAttemptId) {
                NonIntifaceClient.disconnectClient(nextClient);
                return;
            }
            client = nextClient;
            state = State.CONNECTED;
            detail = "";
            NeedsOfNature.LOGGER.info("[NoN] Buttplug.io connect succeeded: attempt={} url={}", (Object)attemptId, (Object)url);
            NonIntifaceClient.updateAnimationOutputs(requestedIntifaceEnabled, requestedAnimationActive, requestedPeakStage, requestedBaselineStrengthPercent, requestedOscillatorRegularSpeedPercent, requestedOscillatorPeakSpeedPercent, requestedSpecialVibratorScalar, requestedEnergizedLevel, requestedEnergizedBasePercent, requestedEnergizedPulsePercent, (double)requestedEnergizedPulsePermille / 1000.0);
        }
        catch (TimeoutException e) {
            ++connectAttemptId;
            NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io connect timed out: attempt={} url={} timeout={}ms", new Object[]{attemptId, url, 8000});
            NonIntifaceClient.setError("Connect timed out after 8s", e);
            NonIntifaceClient.disconnectClient(nextClient);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ++connectAttemptId;
            NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io connect interrupted: attempt={} url={}", new Object[]{attemptId, url, e});
            NonIntifaceClient.setError("Connect interrupted", e);
            NonIntifaceClient.disconnectClient(nextClient);
        }
        catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            ++connectAttemptId;
            NonIntifaceClient.handleConnectFailure(attemptId, url, cause);
            NonIntifaceClient.disconnectClient(nextClient);
        }
        catch (Exception e) {
            ++connectAttemptId;
            NonIntifaceClient.handleConnectFailure(attemptId, url, e);
            NonIntifaceClient.disconnectClient(nextClient);
        }
        catch (Throwable e) {
            ++connectAttemptId;
            NonIntifaceClient.handleConnectFailure(attemptId, url, e);
            NonIntifaceClient.disconnectClient(nextClient);
        }
    }

    private static void handleConnectFailure(long attemptId, String url, Throwable throwable) {
        String expectedMessage = NonIntifaceClient.expectedConnectFailureMessage(throwable);
        if (expectedMessage != null) {
            NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io connect failed: attempt={} url={} reason={}", new Object[]{attemptId, url, expectedMessage});
            NonIntifaceClient.setError(expectedMessage, null);
            return;
        }
        NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io connect failed: attempt={} url={}", new Object[]{attemptId, url, throwable});
        NonIntifaceClient.setError("Connect failed: " + NonIntifaceClient.simpleThrowableName(throwable), throwable);
    }

    private static String expectedConnectFailureMessage(Throwable throwable) {
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            if (current instanceof ConnectException) {
                return "Could not reach Buttplug.io?";
            }
            if (current instanceof NoRouteToHostException || current instanceof UnknownHostException || current instanceof UnresolvedAddressException) {
                return "Could not reach Buttplug.io server";
            }
            String className = current.getClass().getSimpleName().toLowerCase(Locale.ROOT);
            if (className.contains("upgradeexception")) {
                return "Could not connect to Buttplug.io. Is Intiface Central running and using the configured WebSocket URL?";
            }
            String message = current.getMessage();
            if (message == null) continue;
            String lower = message.toLowerCase(Locale.ROOT);
            if (lower.contains("connection refused") || lower.contains("failed to connect") || lower.contains("connection reset") || lower.contains("connection timed out") || lower.contains("connection timeout") || lower.contains("connect timed out")) {
                return "Could not reach Buttplug.io. Is Intiface Central running?";
            }
            if (!lower.contains("failed to upgrade to websocket") && !lower.contains("unexpected http response status code") && !lower.contains("400 bad request")) continue;
            return "Could not connect to Buttplug.io. Is Intiface Central running and using the configured WebSocket URL?";
        }
        return null;
    }

    private static String simpleThrowableName(Throwable throwable) {
        return throwable == null ? "Unknown error" : throwable.getClass().getSimpleName();
    }

    private static boolean triggerStrokerImpact(ButtplugClientWSClient activeClient, int minDistancePercent, int maxDistancePercent, int moveDurationMs, double nextReactiveImpactDelayMs) {
        if (activeClient == null || !activeClient.isConnected()) {
            return false;
        }
        int min = Math.max(0, Math.min(100, Math.min(minDistancePercent, maxDistancePercent)));
        int max = Math.max(0, Math.min(100, Math.max(minDistancePercent, maxDistancePercent)));
        int durationMs = Math.max(0, Math.min(10000, moveDurationMs));
        if (durationMs <= 0 || max <= min) {
            return false;
        }
        double minPosition = (double)min / 100.0;
        double maxPosition = (double)max / 100.0;
        boolean sentAny = NonIntifaceClient.sendLinear(activeClient, maxPosition, durationMs);
        if (!sentAny) {
            return false;
        }
        NonIntifaceClient.cancelPendingStrokerReturn();
        long arrivalDelayMs = nextReactiveImpactDelayMs > 0.0 ? Math.max(0L, Math.round(nextReactiveImpactDelayMs / 2.0)) : 500L;
        long scheduleDelayMs = Math.max((long)durationMs, arrivalDelayMs - (long)durationMs);
        pendingStrokerReturn = EXECUTOR.schedule(() -> {
            NonIntifaceClient.sendLinear(activeClient, minPosition, durationMs);
            pendingStrokerReturn = null;
        }, scheduleDelayMs, TimeUnit.MILLISECONDS);
        return true;
    }

    private static boolean sendVibrate(ButtplugClientWSClient activeClient, double scalar) {
        List devices = activeClient.getDevices();
        if (devices.isEmpty()) {
            detail = class_2561.method_43471((String)"config.needsofnature.intiface.status.no_devices").getString();
            return false;
        }
        boolean sentAny = false;
        for (ButtplugClientDevice device : devices) {
            try {
                if (!device.hasOutput(ButtplugOutput.VIBRATE)) continue;
                NonIntifaceClient.waitForDeviceCommands(device.runVibrateFloat((float)scalar));
                sentAny = true;
            }
            catch (Exception e) {
                NeedsOfNature.LOGGER.debug("[NoN] Intiface pulse failed for device {}.", (Object)device.getDisplayName(), (Object)e);
            }
        }
        if (sentAny) {
            currentScalar = scalar;
        }
        if (!sentAny) {
            detail = class_2561.method_43471((String)"config.needsofnature.intiface.status.no_vibrate_devices").getString();
        }
        return sentAny;
    }

    private static boolean sendOscillate(ButtplugClientWSClient activeClient, double scalar) {
        if (activeClient == null || !activeClient.isConnected()) {
            currentOscillatorScalar = 0.0;
            return false;
        }
        List devices = activeClient.getDevices();
        boolean sentAny = false;
        for (ButtplugClientDevice device : devices) {
            try {
                if (!device.hasOutput(ButtplugOutput.OSCILLATE)) continue;
                NonIntifaceClient.waitForDeviceCommands(device.runOscillateFloat((float)scalar));
                sentAny = true;
            }
            catch (Exception e) {
                NeedsOfNature.LOGGER.debug("[NoN] Intiface oscillate failed for device {}.", (Object)device.getDisplayName(), (Object)e);
            }
        }
        if (sentAny) {
            currentOscillatorScalar = scalar;
        }
        return sentAny;
    }

    private static boolean sendLinear(ButtplugClientWSClient activeClient, double position, long durationMs) {
        if (activeClient == null || !activeClient.isConnected()) {
            return false;
        }
        List devices = activeClient.getDevices();
        boolean sentAny = false;
        for (ButtplugClientDevice device : devices) {
            try {
                if (device.hasOutput(ButtplugOutput.HW_POSITION_WITH_DURATION)) {
                    NonIntifaceClient.waitForDeviceCommands(device.runHwPositionWithDurationFloat((float)position, (int)Math.max(0L, durationMs)));
                } else {
                    if (!device.hasOutput(ButtplugOutput.POSITION)) continue;
                    NonIntifaceClient.waitForDeviceCommands(device.runPositionFloat((float)position));
                }
                sentAny = true;
            }
            catch (Exception e) {
                NeedsOfNature.LOGGER.debug("[NoN] Intiface linear failed for device {}.", (Object)device.getDisplayName(), (Object)e);
            }
        }
        return sentAny;
    }

    private static void waitForDeviceCommands(List<Future<ButtplugMessage>> futures) throws Exception {
        if (futures == null || futures.isEmpty()) {
            return;
        }
        for (Future<ButtplugMessage> future : futures) {
            if (future == null) continue;
            future.get(750L, TimeUnit.MILLISECONDS);
        }
    }

    private static void stopAllInternal(boolean userVisible) {
        ButtplugClientWSClient activeClient = client;
        if (activeClient == null || !activeClient.isConnected()) {
            currentScalar = 0.0;
            currentOscillatorScalar = 0.0;
            return;
        }
        try {
            activeClient.stopAllDevices();
            currentScalar = 0.0;
            currentOscillatorScalar = 0.0;
            if (userVisible) {
                detail = class_2561.method_43471((String)"config.needsofnature.intiface.status.stopped").getString();
            }
        }
        catch (Exception e) {
            NonIntifaceClient.setError("Stop failed", e);
        }
    }

    private static void disconnectInternal() {
        ButtplugClientWSClient activeClient = client;
        client = null;
        currentScalar = 0.0;
        currentOscillatorScalar = 0.0;
        if (activeClient == null) {
            return;
        }
        try {
            activeClient.disconnect();
        }
        catch (RuntimeException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Intiface disconnect failed.", (Throwable)e);
        }
    }

    private static void disconnectClient(ButtplugClientWSClient targetClient) {
        if (targetClient == null) {
            return;
        }
        if (client == targetClient) {
            client = null;
        }
        currentScalar = 0.0;
        currentOscillatorScalar = 0.0;
        try {
            targetClient.disconnect();
        }
        catch (RuntimeException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Intiface disconnect failed.", (Throwable)e);
        }
    }

    private static void cancelPendingStop() {
        ScheduledFuture<?> stop = pendingStop;
        pendingStop = null;
        if (stop != null) {
            stop.cancel(false);
        }
    }

    private static void cancelPendingStrokerReturn() {
        ScheduledFuture<?> stop = pendingStrokerReturn;
        pendingStrokerReturn = null;
        if (stop != null) {
            stop.cancel(false);
        }
    }

    private static void setError(String message, Throwable throwable) {
        state = State.ERROR;
        String string = detail = message == null || message.isBlank() ? "Unknown error" : message;
        if (throwable != null) {
            NeedsOfNature.LOGGER.debug("[NoN] Intiface error: {}", (Object)detail, (Object)throwable);
        } else {
            NeedsOfNature.LOGGER.debug("[NoN] Intiface error: {}", (Object)detail);
        }
    }

    private static void executeSafely(String errorMessage, Runnable action) {
        EXECUTOR.execute(() -> {
            try {
                action.run();
            }
            catch (Throwable throwable) {
                NeedsOfNature.LOGGER.warn("[NoN] Buttplug.io worker failure: {}", (Object)errorMessage, (Object)throwable);
                NonIntifaceClient.setError(errorMessage + ": " + throwable.getClass().getSimpleName(), throwable);
            }
        });
    }

    private static void updateDisabledState(NonConfig config) {
        if (config != null && !config.isIntifaceEnabled()) {
            state = State.DISABLED;
            detail = "";
        }
    }

    private static void clearBaselineState() {
        requestedAnimationActive = false;
        requestedPeakStage = false;
        requestedIntifaceEnabled = false;
        requestedBaselineStrengthPercent = Integer.MIN_VALUE;
        requestedOscillatorRegularSpeedPercent = Integer.MIN_VALUE;
        requestedOscillatorPeakSpeedPercent = Integer.MIN_VALUE;
        requestedSpecialVibratorScalar = Double.NaN;
        requestedEnergizedLevel = 0;
        requestedEnergizedBasePercent = 0;
        requestedEnergizedPulsePercent = 0;
        requestedEnergizedPulsePermille = 0;
        baselineActive = false;
        baselineStrengthPercent = 0;
        oscillatorActive = false;
        oscillatorSpeedPercent = 0;
        specialVibratorActive = false;
        specialVibratorScalar = 0.0;
        energizedActive = false;
        energizedBasePercent = 0;
        energizedPulsePercent = 0;
        energizedPulse = 0.0;
        currentScalar = 0.0;
        currentOscillatorScalar = 0.0;
        suppressBaselineUntilInactive = false;
    }

    static {
        state = State.DISCONNECTED;
        detail = "";
        lastPulseMs = 0L;
        pulseToken = 0L;
        requestedAnimationActive = false;
        requestedPeakStage = false;
        requestedIntifaceEnabled = false;
        requestedBaselineStrengthPercent = Integer.MIN_VALUE;
        requestedOscillatorRegularSpeedPercent = Integer.MIN_VALUE;
        requestedOscillatorPeakSpeedPercent = Integer.MIN_VALUE;
        requestedSpecialVibratorScalar = Double.NaN;
        requestedEnergizedLevel = 0;
        requestedEnergizedBasePercent = 0;
        requestedEnergizedPulsePercent = 0;
        requestedEnergizedPulsePermille = 0;
        baselineActive = false;
        baselineStrengthPercent = 0;
        oscillatorActive = false;
        oscillatorSpeedPercent = 0;
        specialVibratorActive = false;
        specialVibratorScalar = 0.0;
        energizedActive = false;
        energizedBasePercent = 0;
        energizedPulsePercent = 0;
        energizedPulse = 0.0;
        currentScalar = 0.0;
        currentOscillatorScalar = 0.0;
        suppressBaselineUntilInactive = false;
        connectAttemptId = 0L;
    }

    private static enum State {
        DISABLED,
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        SCANNING,
        ERROR;

    }
}

