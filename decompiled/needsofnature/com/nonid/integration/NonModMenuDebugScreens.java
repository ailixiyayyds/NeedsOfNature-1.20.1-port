/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.render.gecko.AfwGeckoResourceResolver
 *  com.afwid.client.render.gecko.AfwGeckoResourceResolver$ModelAndTexture
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$ActorConstraint
 *  com.afwid.data.AfwAnimationDefinitions$AnimationPackInfo
 *  com.afwid.data.AfwAnimationDefinitions$BlockPredicate
 *  com.afwid.data.AfwAnimationDefinitions$BlockRequirements
 *  com.afwid.data.AfwAnimationDefinitions$Clearance
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  com.afwid.data.AfwAnimationDefinitions$SurfaceFootprint
 *  com.afwid.data.AfwAnimationDefinitions$WallHeight
 *  com.afwid.network.AnimationStageInfo
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_10799
 *  net.minecraft.class_1132
 *  net.minecraft.class_11909
 *  net.minecraft.class_1299
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3262
 *  net.minecraft.class_327
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_342
 *  net.minecraft.class_350$class_351
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_4265
 *  net.minecraft.class_4265$class_4266
 *  net.minecraft.class_437
 *  net.minecraft.class_5348
 *  net.minecraft.class_6379
 *  net.minecraft.class_7367
 *  net.minecraft.class_7923
 *  net.minecraft.server.MinecraftServer
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.integration;

import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.NonDebugChatMode;
import com.nonid.client.NonHudOverlay;
import com.nonid.client.NonLiquidColors;
import com.nonid.data.NonEntityProfiles;
import com.nonid.data.NonLiquidGainOverrides;
import com.nonid.data.NonLoopSecondsOverrides;
import com.nonid.data.NonPeakStages;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.SettingsList;
import com.nonid.pack.NonExternalPackProvider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_10799;
import net.minecraft.class_1132;
import net.minecraft.class_11909;
import net.minecraft.class_1299;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3262;
import net.minecraft.class_327;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_350;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_4265;
import net.minecraft.class_437;
import net.minecraft.class_5348;
import net.minecraft.class_6379;
import net.minecraft.class_7367;
import net.minecraft.class_7923;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

final class NonModMenuDebugScreens {
    private NonModMenuDebugScreens() {
    }

    static class LiquidGainConfigScreen
    extends class_437 {
        private static final String MIXED_COLOR_ROW_ID = "needsofnature:mixed";
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private Map<String, Integer> appliedMap;
        private Map<String, String> appliedColorMap;
        private GainList gainList;
        private final List<GainList.GainEntry> rows = new ArrayList<GainList.GainEntry>();
        private class_4185 applyButton;
        private class_4185 doneButton;

        protected LiquidGainConfigScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, Integer>(NonLiquidGainOverrides.getGainByEntity());
            this.appliedMap.putAll(config.getLiquidGainByEntity());
            this.appliedColorMap = new LinkedHashMap<String, String>(NonLiquidGainOverrides.getColorByEntity());
            this.appliedColorMap.putAll(config.getLiquidColorByEntity());
            this.appliedMap.remove(MIXED_COLOR_ROW_ID);
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            List<RowData> preserved = this.snapshotRows();
            if (preserved == null) {
                preserved = new ArrayList<RowData>();
                preserved.add(new RowData(MIXED_COLOR_ROW_ID, "0", this.appliedColorMap.getOrDefault(MIXED_COLOR_ROW_ID, LiquidGainConfigScreen.resolveDefaultMixedColorHex())));
                LinkedHashSet<String> ids = new LinkedHashSet<String>();
                ids.addAll(this.appliedMap.keySet());
                ids.addAll(this.appliedColorMap.keySet());
                ids.remove(MIXED_COLOR_ROW_ID);
                for (String id : ids) {
                    int gain = this.appliedMap.getOrDefault(id, 0);
                    String color = this.appliedColorMap.getOrDefault(id, "");
                    preserved.add(new RowData(id, String.valueOf(gain), color));
                }
            }
            preserved = this.ensureMixedRowPresent(preserved);
            int listTop = 44;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            this.gainList = new GainList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)this.gainList);
            this.rows.clear();
            for (RowData data : preserved) {
                this.addRow(data.id(), data.value(), data.colorHex());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "0", "");
            }
            int centerX = this.field_22789 / 2;
            class_4185 addButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_add"), button -> {
                this.addRow("", "0", "");
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 52, 96, 20).method_46431();
            addButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)addButton, "config.needsofnature.tooltip.liquid_gain_add");
            this.method_37063((class_364)addButton);
            class_4185 resetDefaultsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_reset"), button -> class_310.method_1551().method_1507((class_437)new ResetLiquidGainConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                class_310.method_1551().method_1507((class_437)this);
            }))).method_46434(centerX + 4, this.field_22790 - 52, 96, 20).method_46431();
            resetDefaultsButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)resetDefaultsButton, "config.needsofnature.tooltip.liquid_gain_reset");
            this.method_37063((class_364)resetDefaultsButton);
            this.applyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.apply"), button -> {
                class_1132 server;
                if (!this.serverConfigEditable) {
                    return;
                }
                Map<String, Integer> nextGain = this.collectGainMapFromFields();
                Map<String, String> nextColor = this.collectColorMapFromFields();
                this.config.setLiquidGainByEntity(nextGain);
                this.config.setLiquidColorByEntity(nextColor);
                this.config.save();
                this.appliedMap = new LinkedHashMap<String, Integer>(nextGain);
                this.appliedColorMap = new LinkedHashMap<String, String>(nextColor);
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
                class_310 client = class_310.method_1551();
                if (client != null && (server = client.method_1576()) != null) {
                    server.execute(() -> {
                        NeedsOfNature.syncLiquidStateToAll((MinecraftServer)server);
                        NeedsOfNature.refreshLiquidBottleTintsToAll((MinecraftServer)server);
                    });
                }
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.applyButton, "config.needsofnature.tooltip.apply");
            this.method_37063((class_364)this.applyButton);
            this.doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)this.doneButton);
            this.updateApplyButton();
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
            this.drawLiquidGainHeader(context);
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        private void drawLiquidGainHeader(class_332 context) {
            if (this.gainList == null) {
                return;
            }
            int rowX = this.gainList.method_25342();
            int rowWidth = this.gainList.method_25322();
            int removeW = 20;
            int valueW = 50;
            int colorW = 80;
            int idW = Math.max(120, rowWidth - removeW - valueW - colorW - 18);
            int valueX = rowX + idW + 6;
            int colorX = valueX + valueW + 6;
            int y = 34;
            int color = -4342339;
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_column.entity"), rowX + 4, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_column.ml"), valueX + valueW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_column.color"), colorX + colorW / 2, y, color);
        }

        private void addRow(String idText, String valueText, String colorHexText) {
            GainList.GainEntry[] holder;
            GainList.GainEntry entry = new GainList.GainEntry(this.field_22793, idText, valueText, colorHexText, () -> {
                GainList.GainEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.gainList.removeEntryRow(current);
                this.updateApplyButton();
            });
            holder = new GainList.GainEntry[]{entry};
            entry.setOnChange(this::updateApplyButton);
            entry.setEditable(this.serverConfigEditable);
            if (LiquidGainConfigScreen.isMixedRowId(idText)) {
                entry.setMixedRowMode(this.serverConfigEditable, MIXED_COLOR_ROW_ID);
            }
            this.rows.add(entry);
            this.gainList.addEntryRow(entry);
        }

        private void updateApplyButton() {
            boolean hasUnsavedChanges;
            if (this.applyButton == null) {
                return;
            }
            if (!this.serverConfigEditable) {
                this.applyButton.field_22763 = false;
                if (this.doneButton != null) {
                    this.doneButton.method_25355((class_2561)class_2561.method_43471((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, Integer> currentGain = this.collectGainMapFromFields();
            Map<String, String> currentColor = this.collectColorMapFromFields();
            this.applyButton.field_22763 = hasUnsavedChanges = !currentGain.equals(this.appliedMap) || !currentColor.equals(this.appliedColorMap);
            if (this.doneButton != null) {
                this.doneButton.method_25355((class_2561)class_2561.method_43471((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
            }
        }

        private void applyDefaultsToRows() {
            LinkedHashMap<String, Integer> defaultsMap = new LinkedHashMap<String, Integer>(this.defaults.getLiquidGainByEntity());
            defaultsMap.putAll(NonLiquidGainOverrides.getGainByEntity());
            defaultsMap.remove(MIXED_COLOR_ROW_ID);
            LinkedHashMap<String, String> defaultColors = new LinkedHashMap<String, String>(this.defaults.getLiquidColorByEntity());
            defaultColors.putAll(NonLiquidGainOverrides.getColorByEntity());
            defaultColors.remove(MIXED_COLOR_ROW_ID);
            if (this.gainList != null) {
                for (GainList.GainEntry entry : new ArrayList<GainList.GainEntry>(this.rows)) {
                    this.gainList.removeEntryRow(entry);
                }
            }
            this.rows.clear();
            this.addRow(MIXED_COLOR_ROW_ID, "0", LiquidGainConfigScreen.resolveDefaultMixedColorHex());
            LinkedHashSet ids = new LinkedHashSet();
            ids.addAll(defaultsMap.keySet());
            ids.addAll(defaultColors.keySet());
            for (String id : ids) {
                int gain = defaultsMap.getOrDefault(id, 0);
                String color = defaultColors.getOrDefault(id, "");
                this.addRow(id, String.valueOf(gain), color);
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "0", "");
            }
        }

        private Map<String, Integer> collectGainMapFromFields() {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
            for (GainList.GainEntry row : this.rows) {
                String id;
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || LiquidGainConfigScreen.isMixedRowId(id)) continue;
                int value = this.parseInt(row.getValueText());
                value = Math.max(0, Math.min(1000, value));
                map.put(id, value);
            }
            return map;
        }

        private Map<String, String> collectColorMapFromFields() {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (GainList.GainEntry row : this.rows) {
                String normalized;
                String id;
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || (normalized = NonLiquidColors.normalizeHexColor(row.getColorHexText())) == null) continue;
                map.put(id, normalized);
            }
            return map;
        }

        private List<RowData> snapshotRows() {
            if (this.rows.isEmpty()) {
                return null;
            }
            ArrayList<RowData> data = new ArrayList<RowData>();
            for (GainList.GainEntry row : this.rows) {
                data.add(new RowData(row.getIdText(), row.getValueText(), row.getColorHexText()));
            }
            return data;
        }

        private List<RowData> ensureMixedRowPresent(List<RowData> source) {
            ArrayList<RowData> out = new ArrayList<RowData>();
            String mixedColor = LiquidGainConfigScreen.resolveDefaultMixedColorHex();
            if (source != null) {
                for (RowData row : source) {
                    if (row == null) continue;
                    if (LiquidGainConfigScreen.isMixedRowId(row.id())) {
                        String candidate = NonLiquidColors.normalizeHexColor(row.colorHex());
                        if (candidate == null) continue;
                        mixedColor = candidate;
                        continue;
                    }
                    out.add(row);
                }
            }
            out.add(0, new RowData(MIXED_COLOR_ROW_ID, "0", mixedColor));
            return out;
        }

        private static boolean isMixedRowId(String id) {
            if (id == null) {
                return false;
            }
            return MIXED_COLOR_ROW_ID.equals(id.trim());
        }

        private static String resolveDefaultMixedColorHex() {
            String packHex = NonLiquidGainOverrides.getMixedColorHex();
            if (packHex != null) {
                return packHex;
            }
            return String.format(Locale.ROOT, "%06X", 15920063);
        }

        private int parseInt(String value) {
            if (value == null) {
                return 0;
            }
            try {
                return Integer.parseInt(value.trim());
            }
            catch (NumberFormatException e) {
                return 0;
            }
        }

        private record RowData(String id, String value, String colorHex) {
        }

        private static final class GainList
        extends class_4265<GainEntry> {
            private GainList(class_310 client, int width, int height, int top) {
                super(client, width, height, top, 24);
                this.field_22744 = false;
            }

            private void addEntryRow(GainEntry entry) {
                super.method_25321((class_350.class_351)entry);
            }

            private void removeEntryRow(GainEntry entry) {
                super.method_25330((class_350.class_351)entry);
            }

            public int method_25322() {
                return 360;
            }

            public int method_25342() {
                return (this.method_25368() - this.method_25322()) / 2;
            }

            private static final class GainEntry
            extends class_4265.class_4266<GainEntry> {
                private final class_342 idField;
                private final class_342 valueField;
                private final class_342 colorHexField;
                private final class_4185 removeButton;
                private final List<class_339> widgets;
                private Runnable onChange;

                private GainEntry(class_327 textRenderer, String idText, String valueText, String colorHexText, Runnable onRemove) {
                    this.idField = new class_342(textRenderer, 0, 0, 160, 20, (class_2561)class_2561.method_43473());
                    this.idField.method_1852(idText == null ? "" : idText);
                    this.idField.method_1880(80);
                    NonModMenuScreens.setTooltip((class_339)this.idField, "config.needsofnature.tooltip.liquid_gain_entry_entity");
                    this.valueField = new class_342(textRenderer, 0, 0, 50, 20, (class_2561)class_2561.method_43473());
                    this.valueField.method_1852(valueText == null ? "0" : valueText);
                    this.valueField.method_1880(6);
                    NonModMenuScreens.setTooltip((class_339)this.valueField, "config.needsofnature.tooltip.liquid_gain_entry_ml");
                    this.colorHexField = new class_342(textRenderer, 0, 0, 80, 20, (class_2561)class_2561.method_43473());
                    this.colorHexField.method_1852(colorHexText == null ? "" : colorHexText);
                    this.colorHexField.method_47404((class_2561)class_2561.method_43470((String)"#RRGGBB"));
                    this.colorHexField.method_1880(7);
                    NonModMenuScreens.setTooltip((class_339)this.colorHexField, "config.needsofnature.tooltip.liquid_gain_entry_color");
                    this.removeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).method_46434(0, 0, 20, 20).method_46431();
                    NonModMenuScreens.setTooltip((class_339)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<class_339>(4);
                    this.widgets.add((class_339)this.idField);
                    this.widgets.add((class_339)this.valueField);
                    this.widgets.add((class_339)this.colorHexField);
                    this.widgets.add((class_339)this.removeButton);
                    this.idField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.valueField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.colorHexField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                }

                private void setOnChange(Runnable onChange) {
                    this.onChange = onChange;
                }

                private void setEditable(boolean editable) {
                    this.idField.method_1888(editable);
                    this.valueField.method_1888(editable);
                    this.colorHexField.method_1888(editable);
                    this.removeButton.field_22763 = editable;
                }

                private void setMixedRowMode(boolean editable, String mixedRowId) {
                    this.idField.method_1852(mixedRowId);
                    this.idField.method_1888(false);
                    this.valueField.method_1852("0");
                    this.valueField.method_1888(false);
                    this.colorHexField.method_1888(editable);
                    this.removeButton.field_22763 = false;
                }

                private String getIdText() {
                    return this.idField.method_1882();
                }

                private String getValueText() {
                    return this.valueField.method_1882();
                }

                private String getColorHexText() {
                    return this.colorHexField.method_1882();
                }

                public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                    int rowX = this.method_73380();
                    int rowY = this.method_73382();
                    int rowWidth = this.method_73387();
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.method_25368();
                    int valueW = this.valueField.method_25368();
                    int colorW = this.colorHexField.method_25368();
                    int idW = Math.max(120, rowWidth - removeW - valueW - colorW - 18);
                    int valueX = rowX + idW + 6;
                    int colorX = valueX + valueW + 6;
                    int removeX = colorX + colorW + 6;
                    this.idField.method_46421(rowX);
                    this.idField.method_46419(widgetY);
                    this.idField.method_25358(idW);
                    this.valueField.method_46421(valueX);
                    this.valueField.method_46419(widgetY);
                    this.colorHexField.method_46421(colorX);
                    this.colorHexField.method_46419(widgetY);
                    this.removeButton.method_46421(removeX);
                    this.removeButton.method_46419(widgetY);
                    for (class_339 widget : this.widgets) {
                        widget.method_25394(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends class_6379> method_37025() {
                    return List.copyOf(this.widgets);
                }

                public void method_48206(Consumer<class_339> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends class_364> method_25396() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private static final class ResetLiquidGainConfirmScreen
        extends class_437 {
            private final class_437 parent;
            private final Runnable onConfirm;

            private ResetLiquidGainConfirmScreen(class_437 parent, Runnable onConfirm) {
                super((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void method_25426() {
                int centerX = this.field_22789 / 2;
                int buttonY = this.field_22790 / 2 + 20;
                class_4185 yesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    class_310.method_1551().method_1507(this.parent);
                }).method_46434(centerX - 100, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)yesButton);
                class_4185 noButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)noButton);
            }

            public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
                super.method_25394(context, mouseX, mouseY, delta);
                context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, this.field_22790 / 2 - 20, -1);
                context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_reset_body"), this.field_22789 / 2, this.field_22790 / 2 - 4, -4144960);
            }
        }
    }

    static class OffspringCountConfigScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private boolean serverConfigEditable;
        private Map<String, NonConfig.EntityProfile> appliedMap;
        private OffspringCountList offspringList;
        private final List<OffspringCountList.OffspringEntry> rows = new ArrayList<OffspringCountList.OffspringEntry>();
        private class_4185 applyButton;
        private class_4185 doneButton;

        protected OffspringCountConfigScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, NonConfig.EntityProfile>(config.getEntityProfilesByEntity());
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            List<RowData> preserved = this.snapshotRows();
            if (preserved == null) {
                preserved = new ArrayList<RowData>();
                for (Map.Entry<String, NonConfig.EntityProfile> entry : NonEntityProfiles.resolveEffectiveProfilesMap().entrySet()) {
                    NonConfig.EntityProfile profile = entry.getValue() == null ? NonEntityProfiles.resolve(class_2960.method_12829((String)entry.getKey())).toConfigProfile() : entry.getValue().sanitized();
                    NonConfig.EntityProfile configOverride = this.config.getEntityProfilesByEntity().get(entry.getKey());
                    preserved.add(new RowData(entry.getKey(), configOverride == null || configOverride.pregnancyChancePercent() == null ? "" : String.valueOf(configOverride.pregnancyChancePercent()), String.valueOf(profile.offspringMin() == null ? 1 : profile.offspringMin()), String.valueOf(profile.offspringMax() == null ? 1 : profile.offspringMax()), profile.birthEntity() == null ? entry.getKey() : profile.birthEntity(), profile.birthMode() == null ? "direct" : profile.birthMode(), profile.egg() == null ? NonConfig.EggProfile.defaults() : profile.egg()));
                }
            }
            int listTop = 44;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            this.offspringList = new OffspringCountList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)this.offspringList);
            this.rows.clear();
            for (RowData data : preserved) {
                this.addRow(data.id(), data.chance(), data.min(), data.max(), data.birthEntity(), data.birthMode(), data.eggProfile());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "", "1", "1", "", "direct", NonConfig.EggProfile.defaults());
            }
            int centerX = this.field_22789 / 2;
            class_4185 addButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_add"), button -> {
                this.addRow("", "", "1", "1", "", "direct", NonConfig.EggProfile.defaults());
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 52, 96, 20).method_46431();
            addButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)addButton, "config.needsofnature.tooltip.offspring_count_add");
            this.method_37063((class_364)addButton);
            class_4185 resetDefaultsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_reset"), button -> class_310.method_1551().method_1507((class_437)new ResetOffspringCountConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                class_310.method_1551().method_1507((class_437)this);
            }))).method_46434(centerX + 4, this.field_22790 - 52, 96, 20).method_46431();
            resetDefaultsButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)resetDefaultsButton, "config.needsofnature.tooltip.offspring_count_reset");
            this.method_37063((class_364)resetDefaultsButton);
            this.applyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.apply"), button -> {
                if (!this.serverConfigEditable) {
                    return;
                }
                Map<String, NonConfig.EntityProfile> next = this.collectMapFromFields();
                this.config.setEntityProfilesByEntity(next);
                this.config.save();
                this.appliedMap = new LinkedHashMap<String, NonConfig.EntityProfile>(this.config.getEntityProfilesByEntity());
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.applyButton, "config.needsofnature.tooltip.apply");
            this.method_37063((class_364)this.applyButton);
            this.doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)this.doneButton);
            this.updateApplyButton();
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
            this.drawHeader(context);
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        private void drawHeader(class_332 context) {
            if (this.offspringList == null) {
                return;
            }
            int rowX = this.offspringList.method_25342();
            int rowWidth = this.offspringList.method_25322();
            int removeW = 20;
            int chanceW = 44;
            int minW = 34;
            int maxW = 34;
            int birthW = 140;
            int modeW = 54;
            int eggW = 42;
            int idW = Math.max(105, rowWidth - removeW - chanceW - minW - maxW - birthW - modeW - eggW - 48);
            int chanceX = rowX + idW + 6;
            int minX = chanceX + chanceW + 6;
            int maxX = minX + minW + 6;
            int birthX = maxX + maxW + 6;
            int modeX = birthX + birthW + 6;
            int eggX = modeX + modeW + 6;
            int y = 34;
            int color = -4342339;
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_column.entity"), rowX + 4, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.entity_profile_column.chance"), chanceX + chanceW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_column.min"), minX + minW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_column.max"), maxX + maxW / 2, y, color);
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.entity_profile_column.birth_entity"), birthX + 4, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.entity_profile_column.birth_mode"), modeX + modeW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.entity_profile_column.egg"), eggX + eggW / 2, y, color);
        }

        private void addRow(String idText, String chanceText, String minText, String maxText, String birthEntityText, String birthModeText, NonConfig.EggProfile eggProfile) {
            OffspringCountList.OffspringEntry[] holder;
            OffspringCountList.OffspringEntry entry = new OffspringCountList.OffspringEntry(this.field_22793, idText, chanceText, minText, maxText, birthEntityText, birthModeText, eggProfile, () -> {
                OffspringCountList.OffspringEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.offspringList.removeEntryRow(current);
                this.updateApplyButton();
            }, this::chanceSuggestionFor, this::openEggSettings);
            holder = new OffspringCountList.OffspringEntry[]{entry};
            entry.setOnChange(this::updateApplyButton);
            entry.setEditable(this.serverConfigEditable);
            this.rows.add(entry);
            this.offspringList.addEntryRow(entry);
        }

        private void updateApplyButton() {
            boolean hasUnsavedChanges;
            if (this.applyButton == null) {
                return;
            }
            if (!this.serverConfigEditable) {
                this.applyButton.field_22763 = false;
                if (this.doneButton != null) {
                    this.doneButton.method_25355((class_2561)class_2561.method_43471((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, NonConfig.EntityProfile> current = this.collectMapFromFields();
            this.applyButton.field_22763 = hasUnsavedChanges = !current.equals(this.appliedMap);
            if (this.doneButton != null) {
                this.doneButton.method_25355((class_2561)class_2561.method_43471((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
            }
        }

        private void applyDefaultsToRows() {
            if (this.offspringList != null) {
                for (OffspringCountList.OffspringEntry offspringEntry : new ArrayList<OffspringCountList.OffspringEntry>(this.rows)) {
                    this.offspringList.removeEntryRow(offspringEntry);
                }
            }
            this.rows.clear();
            for (Map.Entry entry : NonEntityProfiles.resolveDefaultProfilesMap().entrySet()) {
                NonConfig.EntityProfile profile = (NonConfig.EntityProfile)entry.getValue();
                this.addRow((String)entry.getKey(), "", String.valueOf(profile.offspringMin() == null ? 1 : profile.offspringMin()), String.valueOf(profile.offspringMax() == null ? 1 : profile.offspringMax()), profile.birthEntity() == null ? (String)entry.getKey() : profile.birthEntity(), profile.birthMode() == null ? "direct" : profile.birthMode(), profile.egg() == null ? NonConfig.EggProfile.defaults() : profile.egg());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "", "1", "1", "", "direct", NonConfig.EggProfile.defaults());
            }
        }

        private Map<String, NonConfig.EntityProfile> collectMapFromFields() {
            LinkedHashMap<String, NonConfig.EntityProfile> map = new LinkedHashMap<String, NonConfig.EntityProfile>();
            for (OffspringCountList.OffspringEntry row : this.rows) {
                NonConfig.EggProfile defaultEgg;
                NonConfig.EggProfile eggOverride;
                String birthModeOverride;
                String birthEntityOverride;
                Integer maxOverride;
                Integer minOverride;
                String birthMode;
                String birthEntity;
                class_2960 parsedId;
                String id;
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || (parsedId = class_2960.method_12829((String)id)) == null) continue;
                String rawChance = row.getChanceText() == null ? "" : row.getChanceText().trim();
                int min = this.parseInt(row.getMinText(), 1);
                int max = this.parseInt(row.getMaxText(), min);
                if (max < min) {
                    max = min;
                }
                if (class_2960.method_12829((String)(birthEntity = (birthEntity = row.getBirthEntityText()) == null || birthEntity.trim().isEmpty() ? id : birthEntity.trim())) == null) {
                    birthEntity = id;
                }
                if ((birthMode = NonConfig.normalizeBirthMode(row.getBirthModeText())) == null) {
                    birthMode = "direct";
                }
                NonConfig.EggProfile eggProfile = row.getEggProfile();
                NonConfig.EntityProfile existing = this.config.getEntityProfilesByEntity().get(id);
                NonConfig.EntityProfile defaultProfile = NonEntityProfiles.resolveDefaultProfile(parsedId).toConfigProfile();
                int defaultChance = defaultProfile.pregnancyChancePercent() == null ? this.config.getPregnancyChancePercent() : defaultProfile.pregnancyChancePercent().intValue();
                Integer chanceOverride = rawChance.isEmpty() ? null : Integer.valueOf(Math.max(0, Math.min(100, this.parseInt(rawChance, defaultChance))));
                NonConfig.EntityProfile next = new NonConfig.EntityProfile(chanceOverride == null || chanceOverride == defaultChance ? null : chanceOverride, minOverride = min == defaultProfile.offspringMin() ? null : Integer.valueOf(min), maxOverride = max == defaultProfile.offspringMax() ? null : Integer.valueOf(max), birthEntityOverride = birthEntity.equals(defaultProfile.birthEntity()) ? null : birthEntity, birthModeOverride = birthMode.equals(defaultProfile.birthMode()) ? null : birthMode, existing == null ? null : existing.genderSpawn(), eggOverride = Objects.equals(eggProfile, defaultEgg = defaultProfile.egg() == null ? NonConfig.EggProfile.defaults() : defaultProfile.egg()) ? null : eggProfile).sanitized();
                if (next.isEmpty()) continue;
                map.put(id, next);
            }
            return map;
        }

        private String chanceSuggestionFor(String idText) {
            class_2960 id = idText == null || idText.isBlank() ? null : class_2960.method_12829((String)idText.trim());
            int defaultChance = id == null ? this.config.getPregnancyChancePercent() : NonEntityProfiles.resolveDefaultProfile(id).pregnancyChancePercent();
            return String.valueOf(defaultChance);
        }

        private List<RowData> snapshotRows() {
            if (this.rows.isEmpty()) {
                return null;
            }
            ArrayList<RowData> data = new ArrayList<RowData>();
            for (OffspringCountList.OffspringEntry row : this.rows) {
                data.add(new RowData(row.getIdText(), row.getChanceText(), row.getMinText(), row.getMaxText(), row.getBirthEntityText(), row.getBirthModeText(), row.getEggProfile()));
            }
            return data;
        }

        private int parseInt(String value, int fallback) {
            if (value == null) {
                return fallback;
            }
            try {
                return Integer.parseInt(value.trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private void openEggSettings(OffspringCountList.OffspringEntry row) {
            if (row == null) {
                return;
            }
            class_2960 id = class_2960.method_12829((String)(row.getIdText() == null ? "" : row.getIdText().trim()));
            NonConfig.EggProfile defaultEgg = id == null ? NonConfig.EggProfile.defaults() : NonEntityProfiles.resolveDefaultProfile(id).eggProfile();
            class_310.method_1551().method_1507((class_437)new EggProfileSettingsScreen(this, row.getEggProfile(), defaultEgg, updated -> {
                row.setEggProfile((NonConfig.EggProfile)updated);
                this.updateApplyButton();
            }, this.serverConfigEditable));
        }

        private record RowData(String id, String chance, String min, String max, String birthEntity, String birthMode, NonConfig.EggProfile eggProfile) {
        }

        private static final class OffspringCountList
        extends class_4265<OffspringEntry> {
            private OffspringCountList(class_310 client, int width, int height, int top) {
                super(client, width, height, top, 24);
                this.field_22744 = false;
            }

            private void addEntryRow(OffspringEntry entry) {
                super.method_25321((class_350.class_351)entry);
            }

            private void removeEntryRow(OffspringEntry entry) {
                super.method_25330((class_350.class_351)entry);
            }

            public int method_25322() {
                return 620;
            }

            public int method_25342() {
                return (this.method_25368() - this.method_25322()) / 2;
            }

            private static final class OffspringEntry
            extends class_4265.class_4266<OffspringEntry> {
                private final class_342 idField;
                private final class_342 chanceField;
                private final class_342 minField;
                private final class_342 maxField;
                private final class_342 birthEntityField;
                private final class_342 birthModeField;
                private final class_4185 eggButton;
                private final class_4185 removeButton;
                private final List<class_339> widgets;
                private final Function<String, String> chanceSuggestionSupplier;
                private NonConfig.EggProfile eggProfile;
                private boolean editable;
                private Runnable onChange;

                private OffspringEntry(class_327 textRenderer, String idText, String chanceText, String minText, String maxText, String birthEntityText, String birthModeText, NonConfig.EggProfile eggProfile, Runnable onRemove, Function<String, String> chanceSuggestionSupplier, Consumer<OffspringEntry> onEggSettings) {
                    this.chanceSuggestionSupplier = chanceSuggestionSupplier;
                    this.eggProfile = eggProfile == null ? NonConfig.EggProfile.defaults() : eggProfile.sanitized();
                    this.idField = new class_342(textRenderer, 0, 0, 120, 20, (class_2561)class_2561.method_43473());
                    this.idField.method_1852(idText == null ? "" : idText);
                    this.idField.method_1880(80);
                    NonModMenuScreens.setTooltip((class_339)this.idField, "config.needsofnature.tooltip.offspring_count_entry_entity");
                    this.chanceField = new class_342(textRenderer, 0, 0, 44, 20, (class_2561)class_2561.method_43473());
                    this.chanceField.method_1852(chanceText == null ? "" : chanceText);
                    this.chanceField.method_1880(3);
                    this.updateChanceSuggestion();
                    NonModMenuScreens.setTooltip((class_339)this.chanceField, "config.needsofnature.tooltip.entity_profile_entry_chance");
                    this.minField = new class_342(textRenderer, 0, 0, 34, 20, (class_2561)class_2561.method_43473());
                    this.minField.method_1852(minText == null ? "1" : minText);
                    this.minField.method_1880(3);
                    NonModMenuScreens.setTooltip((class_339)this.minField, "config.needsofnature.tooltip.offspring_count_entry_min");
                    this.maxField = new class_342(textRenderer, 0, 0, 34, 20, (class_2561)class_2561.method_43473());
                    this.maxField.method_1852(maxText == null ? "1" : maxText);
                    this.maxField.method_1880(3);
                    NonModMenuScreens.setTooltip((class_339)this.maxField, "config.needsofnature.tooltip.offspring_count_entry_max");
                    this.birthEntityField = new class_342(textRenderer, 0, 0, 140, 20, (class_2561)class_2561.method_43473());
                    this.birthEntityField.method_1852(birthEntityText == null ? "" : birthEntityText);
                    this.birthEntityField.method_1880(80);
                    NonModMenuScreens.setTooltip((class_339)this.birthEntityField, "config.needsofnature.tooltip.entity_profile_entry_birth_entity");
                    this.birthModeField = new class_342(textRenderer, 0, 0, 54, 20, (class_2561)class_2561.method_43473());
                    this.birthModeField.method_1852(birthModeText == null ? "direct" : birthModeText);
                    this.birthModeField.method_1880(6);
                    NonModMenuScreens.setTooltip((class_339)this.birthModeField, "config.needsofnature.tooltip.entity_profile_entry_birth_mode");
                    this.eggButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.entity_profile_egg_button"), button -> {
                        if (onEggSettings != null) {
                            onEggSettings.accept(this);
                        }
                    }).method_46434(0, 0, 42, 20).method_46431();
                    NonModMenuScreens.setTooltip((class_339)this.eggButton, "config.needsofnature.tooltip.entity_profile_entry_egg");
                    this.removeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).method_46434(0, 0, 20, 20).method_46431();
                    NonModMenuScreens.setTooltip((class_339)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<class_339>(8);
                    this.widgets.add((class_339)this.idField);
                    this.widgets.add((class_339)this.chanceField);
                    this.widgets.add((class_339)this.minField);
                    this.widgets.add((class_339)this.maxField);
                    this.widgets.add((class_339)this.birthEntityField);
                    this.widgets.add((class_339)this.birthModeField);
                    this.widgets.add((class_339)this.eggButton);
                    this.widgets.add((class_339)this.removeButton);
                    this.idField.method_1863(ignored -> {
                        this.updateChanceSuggestion();
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.chanceField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.minField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.maxField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.birthEntityField.method_1863(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.birthModeField.method_1863(ignored -> {
                        this.updateEggButtonActive();
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.updateEggButtonActive();
                }

                private void setOnChange(Runnable onChange) {
                    this.onChange = onChange;
                }

                private void updateChanceSuggestion() {
                    String suggestion = this.chanceSuggestionSupplier == null ? null : this.chanceSuggestionSupplier.apply(this.idField.method_1882());
                    this.chanceField.method_47404(suggestion == null || suggestion.isBlank() ? null : class_2561.method_43470((String)suggestion));
                }

                private void setEditable(boolean editable) {
                    this.editable = editable;
                    this.idField.method_1888(editable);
                    this.chanceField.method_1888(editable);
                    this.minField.method_1888(editable);
                    this.maxField.method_1888(editable);
                    this.birthEntityField.method_1888(editable);
                    this.birthModeField.method_1888(editable);
                    this.updateEggButtonActive();
                    this.removeButton.field_22763 = editable;
                }

                private void updateEggButtonActive() {
                    this.eggButton.field_22763 = this.editable && "egg".equals(NonConfig.normalizeBirthMode(this.birthModeField.method_1882()));
                }

                private String getIdText() {
                    return this.idField.method_1882();
                }

                private String getMinText() {
                    return this.minField.method_1882();
                }

                private String getMaxText() {
                    return this.maxField.method_1882();
                }

                private String getChanceText() {
                    return this.chanceField.method_1882();
                }

                private String getBirthEntityText() {
                    return this.birthEntityField.method_1882();
                }

                private String getBirthModeText() {
                    return this.birthModeField.method_1882();
                }

                private NonConfig.EggProfile getEggProfile() {
                    return this.eggProfile == null ? NonConfig.EggProfile.defaults() : this.eggProfile.sanitized();
                }

                private void setEggProfile(NonConfig.EggProfile eggProfile) {
                    NonConfig.EggProfile eggProfile2 = this.eggProfile = eggProfile == null ? NonConfig.EggProfile.defaults() : eggProfile.sanitized();
                    if (this.onChange != null) {
                        this.onChange.run();
                    }
                }

                public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                    int rowX = this.method_73380();
                    int rowY = this.method_73382();
                    int rowWidth = this.method_73387();
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.method_25368();
                    int chanceW = this.chanceField.method_25368();
                    int minW = this.minField.method_25368();
                    int maxW = this.maxField.method_25368();
                    int birthW = this.birthEntityField.method_25368();
                    int modeW = this.birthModeField.method_25368();
                    int eggW = this.eggButton.method_25368();
                    int idW = Math.max(105, rowWidth - removeW - chanceW - minW - maxW - birthW - modeW - eggW - 48);
                    int chanceX = rowX + idW + 6;
                    int minX = chanceX + chanceW + 6;
                    int maxX = minX + minW + 6;
                    int birthX = maxX + maxW + 6;
                    int modeX = birthX + birthW + 6;
                    int eggX = modeX + modeW + 6;
                    int removeX = eggX + eggW + 6;
                    this.idField.method_46421(rowX);
                    this.idField.method_46419(widgetY);
                    this.idField.method_25358(idW);
                    this.chanceField.method_46421(chanceX);
                    this.chanceField.method_46419(widgetY);
                    this.minField.method_46421(minX);
                    this.minField.method_46419(widgetY);
                    this.maxField.method_46421(maxX);
                    this.maxField.method_46419(widgetY);
                    this.birthEntityField.method_46421(birthX);
                    this.birthEntityField.method_46419(widgetY);
                    this.birthModeField.method_46421(modeX);
                    this.birthModeField.method_46419(widgetY);
                    this.eggButton.method_46421(eggX);
                    this.eggButton.method_46419(widgetY);
                    this.removeButton.method_46421(removeX);
                    this.removeButton.method_46419(widgetY);
                    for (class_339 widget : this.widgets) {
                        widget.method_25394(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends class_6379> method_37025() {
                    return List.copyOf(this.widgets);
                }

                public void method_48206(Consumer<class_339> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends class_364> method_25396() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private static final class EggProfileSettingsScreen
        extends class_437 {
            private final class_437 parent;
            private final NonConfig.EggProfile initialProfile;
            private final NonConfig.EggProfile defaultProfile;
            private final Consumer<NonConfig.EggProfile> onApply;
            private final boolean editable;
            private class_342 startSizeField;
            private class_342 endSizeField;
            private class_342 textureField;
            private class_342 healthField;

            private EggProfileSettingsScreen(class_437 parent, NonConfig.EggProfile initialProfile, NonConfig.EggProfile defaultProfile, Consumer<NonConfig.EggProfile> onApply, boolean editable) {
                super((class_2561)class_2561.method_43471((String)"config.needsofnature.egg_profile_title"));
                this.parent = parent;
                this.initialProfile = initialProfile == null ? NonConfig.EggProfile.defaults() : initialProfile.sanitized();
                this.defaultProfile = defaultProfile == null ? NonConfig.EggProfile.defaults() : defaultProfile.sanitized();
                this.onApply = onApply;
                this.editable = editable;
            }

            protected void method_25426() {
                int centerX = this.field_22789 / 2;
                int labelX = centerX - 180;
                int fieldX = centerX - 40;
                int y = 58;
                int textureFieldW = Math.min(460, Math.max(220, this.field_22789 - fieldX - 20));
                this.startSizeField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.startSize(), this.defaultProfile.startSize(), 0.5f)));
                this.endSizeField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.endSize(), this.defaultProfile.endSize(), 1.0f)));
                this.textureField = this.newTextField(textureFieldW, EggProfileSettingsScreen.valueOrDefault(this.initialProfile.texture(), this.defaultProfile.texture(), ""));
                this.healthField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.health(), this.defaultProfile.health(), 2.0f)));
                this.addField(this.startSizeField, fieldX, y, "config.needsofnature.tooltip.egg_profile_start_size");
                this.addField(this.endSizeField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_end_size");
                this.addField(this.textureField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_texture");
                this.addField(this.healthField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_health");
                class_4185 resetButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset"), button -> {
                    this.startSizeField.method_1852(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.startSize(), null, 0.5f)));
                    this.endSizeField.method_1852(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.endSize(), null, 1.0f)));
                    this.textureField.method_1852(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.texture(), null, ""));
                    this.healthField.method_1852(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.health(), null, 2.0f)));
                }).method_46434(centerX - 154, this.field_22790 - 52, 96, 20).method_46431();
                resetButton.field_22763 = this.editable;
                NonModMenuScreens.setTooltip((class_339)resetButton, "config.needsofnature.tooltip.egg_profile_reset");
                this.method_37063((class_364)resetButton);
                class_4185 applyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.apply"), button -> {
                    if (!this.editable) {
                        return;
                    }
                    if (this.onApply != null) {
                        this.onApply.accept(this.collectProfile());
                    }
                    class_310.method_1551().method_1507(this.parent);
                }).method_46434(centerX - 50, this.field_22790 - 52, 96, 20).method_46431();
                applyButton.field_22763 = this.editable;
                NonModMenuScreens.setTooltip((class_339)applyButton, "config.needsofnature.tooltip.apply");
                this.method_37063((class_364)applyButton);
                class_4185 cancelButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 54, this.field_22790 - 52, 96, 20).method_46431();
                this.method_37063((class_364)cancelButton);
                this.startSizeField.method_1888(this.editable);
                this.endSizeField.method_1888(this.editable);
                this.textureField.method_1888(this.editable);
                this.healthField.method_1888(this.editable);
            }

            public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
                super.method_25394(context, mouseX, mouseY, delta);
                int centerX = this.field_22789 / 2;
                int labelX = centerX - 180;
                int y = 63;
                context.method_27534(this.field_22793, this.field_22785, centerX, 24, -1);
                this.drawLabel(context, "config.needsofnature.egg_profile_start_size", labelX, y);
                this.drawLabel(context, "config.needsofnature.egg_profile_end_size", labelX, y += 28);
                this.drawLabel(context, "config.needsofnature.egg_profile_texture", labelX, y += 28);
                this.drawLabel(context, "config.needsofnature.egg_profile_health", labelX, y += 28);
            }

            public void method_25419() {
                class_310.method_1551().method_1507(this.parent);
            }

            private void addField(class_342 field, int x, int y, String tooltipKey) {
                field.method_46421(x);
                field.method_46419(y);
                NonModMenuScreens.setTooltip((class_339)field, tooltipKey);
                this.method_37063((class_364)field);
            }

            private class_342 newTextField(int width, String value) {
                class_342 field = new class_342(this.field_22793, 0, 0, width, 20, (class_2561)class_2561.method_43473());
                field.method_1880(128);
                field.method_1852(value == null ? "" : value);
                return field;
            }

            private void drawLabel(class_332 context, String key, int x, int y) {
                context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)key), x, y, -2039584);
            }

            private NonConfig.EggProfile collectProfile() {
                NonConfig.EggProfile raw = new NonConfig.EggProfile(EggProfileSettingsScreen.parseFloat(this.startSizeField.method_1882(), this.defaultProfile.startSize(), 0.5f), EggProfileSettingsScreen.parseFloat(this.endSizeField.method_1882(), this.defaultProfile.endSize(), 1.0f), this.textureField.method_1882() == null || this.textureField.method_1882().trim().isEmpty() ? null : this.textureField.method_1882().trim(), EggProfileSettingsScreen.parseFloat(this.healthField.method_1882(), this.defaultProfile.health(), 2.0f));
                return raw.resolvedOver(this.defaultProfile);
            }

            private static Float parseFloat(String raw, Float fallback, float hardFallback) {
                if (raw == null || raw.trim().isEmpty()) {
                    return Float.valueOf(fallback == null ? hardFallback : fallback.floatValue());
                }
                try {
                    return Float.valueOf(Float.parseFloat(raw.trim()));
                }
                catch (NumberFormatException e) {
                    return Float.valueOf(fallback == null ? hardFallback : fallback.floatValue());
                }
            }

            private static float valueOrDefault(Float value, Float fallback, float hardFallback) {
                if (value != null) {
                    return value.floatValue();
                }
                if (fallback != null) {
                    return fallback.floatValue();
                }
                return hardFallback;
            }

            private static String valueOrDefault(String value, String fallback, String hardFallback) {
                if (value != null && !value.isBlank()) {
                    return value;
                }
                if (fallback != null && !fallback.isBlank()) {
                    return fallback;
                }
                return hardFallback;
            }

            private static String formatFloat(float value) {
                String formatted = String.format(Locale.ROOT, "%.3f", Float.valueOf(value));
                while (formatted.contains(".") && formatted.endsWith("0")) {
                    formatted = formatted.substring(0, formatted.length() - 1);
                }
                if (formatted.endsWith(".")) {
                    formatted = formatted.substring(0, formatted.length() - 1);
                }
                return formatted;
            }
        }

        private static final class ResetOffspringCountConfirmScreen
        extends class_437 {
            private final class_437 parent;
            private final Runnable onConfirm;

            private ResetOffspringCountConfirmScreen(class_437 parent, Runnable onConfirm) {
                super((class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void method_25426() {
                int centerX = this.field_22789 / 2;
                int buttonY = this.field_22790 / 2 + 20;
                class_4185 yesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    class_310.method_1551().method_1507(this.parent);
                }).method_46434(centerX - 100, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)yesButton);
                class_4185 noButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)noButton);
            }

            public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
                super.method_25394(context, mouseX, mouseY, delta);
                context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, this.field_22790 / 2 - 20, -1);
                context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.offspring_count_reset_body"), this.field_22789 / 2, this.field_22790 / 2 - 4, -4144960);
            }
        }
    }

    static class GenderSpawnConfigScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private boolean serverConfigEditable;
        private Map<String, NonConfig.GenderSpawnChances> appliedMap;
        private GenderSpawnList genderList;
        private final List<GenderSpawnList.GenderEntry> rows = new ArrayList<GenderSpawnList.GenderEntry>();
        private class_4185 applyButton;
        private class_4185 doneButton;

        protected GenderSpawnConfigScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, NonConfig.GenderSpawnChances>(NonEntityProfiles.resolveEffectiveGenderSpawnMap());
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            List<GenderRowData> preserved = this.snapshotRows();
            if (preserved == null) {
                preserved = new ArrayList<GenderRowData>();
                for (Map.Entry<String, NonConfig.GenderSpawnChances> entry : this.appliedMap.entrySet()) {
                    NonConfig.GenderSpawnChances chances = entry.getValue();
                    if (chances == null) continue;
                    preserved.add(new GenderRowData(entry.getKey(), String.valueOf(chances.male()), String.valueOf(chances.female()), String.valueOf(chances.both())));
                }
            }
            int listTop = 44;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            this.genderList = new GenderSpawnList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)this.genderList);
            this.rows.clear();
            for (GenderRowData data : preserved) {
                this.addRow(data.id(), data.male(), data.female(), data.both());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "48", "47", "5");
            }
            int centerX = this.field_22789 / 2;
            class_4185 addButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_add"), button -> {
                this.addRow("", "48", "47", "5");
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 52, 96, 20).method_46431();
            addButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)addButton, "config.needsofnature.tooltip.gender_spawn_add");
            this.method_37063((class_364)addButton);
            class_4185 resetDefaultsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_reset"), button -> class_310.method_1551().method_1507((class_437)new ResetGenderSpawnConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                class_310.method_1551().method_1507((class_437)this);
            }))).method_46434(centerX + 4, this.field_22790 - 52, 96, 20).method_46431();
            resetDefaultsButton.field_22763 = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((class_339)resetDefaultsButton, "config.needsofnature.tooltip.gender_spawn_reset");
            this.method_37063((class_364)resetDefaultsButton);
            this.applyButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.apply"), button -> {
                if (!this.serverConfigEditable) {
                    return;
                }
                if (this.hasInvalidGenderChanceRows()) {
                    this.updateApplyButton();
                    return;
                }
                Map<String, NonConfig.GenderSpawnChances> next = this.collectGenderMapFromFields();
                this.config.setEntityProfilesByEntity(this.mergeGenderSpawnIntoEntityProfiles(next));
                this.config.save();
                this.appliedMap = new LinkedHashMap<String, NonConfig.GenderSpawnChances>(NonEntityProfiles.resolveEffectiveGenderSpawnMap());
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
                this.updateApplyButton();
            }).method_46434(centerX - 100, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.applyButton, "config.needsofnature.tooltip.apply");
            this.method_37063((class_364)this.applyButton);
            this.doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, this.field_22790 - 28, 96, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.method_37063((class_364)this.doneButton);
            this.updateApplyButton();
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
            this.drawGenderSpawnHeader(context);
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        private void drawGenderSpawnHeader(class_332 context) {
            if (this.genderList == null) {
                return;
            }
            int rowX = this.genderList.method_25342();
            int rowWidth = this.genderList.method_25322();
            int fieldW = 42;
            int removeW = 20;
            int idW = Math.max(130, rowWidth - removeW - fieldW * 3 - 30);
            int maleX = rowX + idW + 6;
            int femaleX = maleX + fieldW + 6;
            int bothX = femaleX + fieldW + 6;
            int y = 34;
            int color = -4342339;
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_column.entity"), rowX + 4, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_column.male"), maleX + fieldW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_column.female"), femaleX + fieldW / 2, y, color);
            context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_column.both"), bothX + fieldW / 2, y, color);
        }

        private void addRow(String idText, String maleText, String femaleText, String bothText) {
            GenderSpawnList.GenderEntry[] holder;
            GenderSpawnList.GenderEntry entry = new GenderSpawnList.GenderEntry(this.field_22793, idText, maleText, femaleText, bothText, () -> {
                GenderSpawnList.GenderEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.genderList.removeEntryRow(current);
                this.updateApplyButton();
            });
            holder = new GenderSpawnList.GenderEntry[]{entry};
            entry.setOnChange(this::updateApplyButton);
            entry.setEditable(this.serverConfigEditable);
            this.rows.add(entry);
            this.genderList.addEntryRow(entry);
        }

        private void updateApplyButton() {
            if (this.applyButton == null) {
                return;
            }
            if (!this.serverConfigEditable) {
                this.applyButton.field_22763 = false;
                if (this.doneButton != null) {
                    this.doneButton.method_25355((class_2561)class_2561.method_43471((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, NonConfig.GenderSpawnChances> current = this.collectGenderMapFromFields();
            boolean hasUnsavedChanges = !current.equals(this.appliedMap);
            boolean hasInvalidRows = this.hasInvalidGenderChanceRows();
            this.applyButton.field_22763 = hasUnsavedChanges && !hasInvalidRows;
            this.applyButton.method_25355((class_2561)class_2561.method_43471((String)(hasInvalidRows ? "config.needsofnature.gender_spawn_invalid_total" : "config.needsofnature.apply")));
            if (this.doneButton != null) {
                this.doneButton.method_25355((class_2561)class_2561.method_43471((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
            }
        }

        private boolean hasInvalidGenderChanceRows() {
            for (GenderSpawnList.GenderEntry row : this.rows) {
                int total;
                String id;
                if (row == null || (id = row.getIdText()) == null || id.trim().isEmpty() || (total = this.parseInt(row.getMaleText()) + this.parseInt(row.getFemaleText()) + this.parseInt(row.getBothText())) == 100) continue;
                return true;
            }
            return false;
        }

        private void applyDefaultsToRows() {
            NonConfig.GenderSpawnChances chances;
            LinkedHashMap<String, NonConfig.GenderSpawnChances> defaultsMap = new LinkedHashMap<String, NonConfig.GenderSpawnChances>();
            for (Map.Entry<String, NonConfig.EntityProfile> entry : NonEntityProfiles.resolveDefaultProfilesMap().entrySet()) {
                chances = entry.getValue().genderSpawn();
                if (chances == null) continue;
                defaultsMap.put(entry.getKey(), chances);
            }
            if (this.genderList != null) {
                for (GenderSpawnList.GenderEntry genderEntry : new ArrayList<GenderSpawnList.GenderEntry>(this.rows)) {
                    this.genderList.removeEntryRow(genderEntry);
                }
            }
            this.rows.clear();
            for (Map.Entry entry : defaultsMap.entrySet()) {
                chances = (NonConfig.GenderSpawnChances)entry.getValue();
                if (chances == null) continue;
                this.addRow((String)entry.getKey(), String.valueOf(chances.male()), String.valueOf(chances.female()), String.valueOf(chances.both()));
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "48", "47", "5");
            }
        }

        private Map<String, NonConfig.EntityProfile> mergeGenderSpawnIntoEntityProfiles(Map<String, NonConfig.GenderSpawnChances> genderMap) {
            LinkedHashMap<String, NonConfig.EntityProfile> profiles = new LinkedHashMap<String, NonConfig.EntityProfile>();
            if (genderMap != null) {
                for (Map.Entry<String, NonConfig.GenderSpawnChances> entry : genderMap.entrySet()) {
                    NonConfig.GenderSpawnChances savedGender;
                    class_2960 id = class_2960.method_12829((String)entry.getKey());
                    if (id == null || entry.getValue() == null) continue;
                    NonConfig.EntityProfile base = this.config.getEntityProfilesByEntity().get(entry.getKey());
                    NonConfig.GenderSpawnChances defaultGender = NonEntityProfiles.resolveDefaultProfile(id).genderSpawn();
                    NonConfig.EntityProfile next = new NonConfig.EntityProfile(base == null ? null : base.pregnancyChancePercent(), base == null ? null : base.offspringMin(), base == null ? null : base.offspringMax(), base == null ? null : base.birthEntity(), base == null ? null : base.birthMode(), savedGender = entry.getValue().equals(defaultGender) ? null : entry.getValue()).sanitized();
                    if (next.isEmpty()) continue;
                    profiles.put(entry.getKey(), next);
                }
            }
            return profiles;
        }

        private Map<String, NonConfig.GenderSpawnChances> collectGenderMapFromFields() {
            LinkedHashMap<String, NonConfig.GenderSpawnChances> map = new LinkedHashMap<String, NonConfig.GenderSpawnChances>();
            for (GenderSpawnList.GenderEntry row : this.rows) {
                NonConfig.GenderSpawnChances chances;
                String id;
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || class_2960.method_12829((String)id) == null || (chances = NonConfig.sanitizeGenderSpawnChances(new NonConfig.GenderSpawnChances(this.parseInt(row.getMaleText()), this.parseInt(row.getFemaleText()), this.parseInt(row.getBothText())))) == null) continue;
                map.put(id, chances);
            }
            return map;
        }

        private List<GenderRowData> snapshotRows() {
            if (this.rows.isEmpty()) {
                return null;
            }
            ArrayList<GenderRowData> data = new ArrayList<GenderRowData>();
            for (GenderSpawnList.GenderEntry row : this.rows) {
                data.add(new GenderRowData(row.getIdText(), row.getMaleText(), row.getFemaleText(), row.getBothText()));
            }
            return data;
        }

        private int parseInt(String value) {
            if (value == null) {
                return 0;
            }
            try {
                return Integer.parseInt(value.trim());
            }
            catch (NumberFormatException e) {
                return 0;
            }
        }

        private record GenderRowData(String id, String male, String female, String both) {
        }

        private static class GenderSpawnList
        extends class_4265<GenderEntry> {
            private GenderSpawnList(class_310 client, int width, int height, int top) {
                super(client, width, height, top, 24);
                this.field_22744 = false;
            }

            private void addEntryRow(GenderEntry entry) {
                super.method_25321((class_350.class_351)entry);
            }

            private void removeEntryRow(GenderEntry entry) {
                super.method_25330((class_350.class_351)entry);
            }

            public int method_25322() {
                return 420;
            }

            public int method_25342() {
                return (this.method_25368() - this.method_25322()) / 2;
            }

            private static final class GenderEntry
            extends class_4265.class_4266<GenderEntry> {
                private final class_342 idField;
                private final class_342 maleField;
                private final class_342 femaleField;
                private final class_342 bothField;
                private final class_4185 removeButton;
                private final List<class_339> widgets;
                private Runnable onChange;

                private GenderEntry(class_327 textRenderer, String idText, String maleText, String femaleText, String bothText, Runnable onRemove) {
                    this.idField = new class_342(textRenderer, 0, 0, 170, 20, (class_2561)class_2561.method_43473());
                    this.idField.method_1852(idText == null ? "" : idText);
                    this.idField.method_1880(80);
                    NonModMenuScreens.setTooltip((class_339)this.idField, "config.needsofnature.tooltip.gender_spawn_entry_entity");
                    this.maleField = GenderEntry.percentField(textRenderer, maleText);
                    NonModMenuScreens.setTooltip((class_339)this.maleField, "config.needsofnature.tooltip.gender_spawn_entry_male");
                    this.femaleField = GenderEntry.percentField(textRenderer, femaleText);
                    NonModMenuScreens.setTooltip((class_339)this.femaleField, "config.needsofnature.tooltip.gender_spawn_entry_female");
                    this.bothField = GenderEntry.percentField(textRenderer, bothText);
                    NonModMenuScreens.setTooltip((class_339)this.bothField, "config.needsofnature.tooltip.gender_spawn_entry_both");
                    this.removeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).method_46434(0, 0, 20, 20).method_46431();
                    NonModMenuScreens.setTooltip((class_339)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<class_339>(5);
                    this.widgets.add((class_339)this.idField);
                    this.widgets.add((class_339)this.maleField);
                    this.widgets.add((class_339)this.femaleField);
                    this.widgets.add((class_339)this.bothField);
                    this.widgets.add((class_339)this.removeButton);
                    this.idField.method_1863(ignored -> this.notifyChanged());
                    this.maleField.method_1863(ignored -> this.notifyChanged());
                    this.femaleField.method_1863(ignored -> this.notifyChanged());
                    this.bothField.method_1863(ignored -> this.notifyChanged());
                }

                private static class_342 percentField(class_327 textRenderer, String text) {
                    class_342 field = new class_342(textRenderer, 0, 0, 42, 20, (class_2561)class_2561.method_43473());
                    field.method_1852(text == null ? "0" : text);
                    field.method_1880(3);
                    return field;
                }

                private void setOnChange(Runnable onChange) {
                    this.onChange = onChange;
                }

                private void notifyChanged() {
                    if (this.onChange != null) {
                        this.onChange.run();
                    }
                }

                private void setEditable(boolean editable) {
                    this.idField.method_1888(editable);
                    this.maleField.method_1888(editable);
                    this.femaleField.method_1888(editable);
                    this.bothField.method_1888(editable);
                    this.removeButton.field_22763 = editable;
                }

                private String getIdText() {
                    return this.idField.method_1882();
                }

                private String getMaleText() {
                    return this.maleField.method_1882();
                }

                private String getFemaleText() {
                    return this.femaleField.method_1882();
                }

                private String getBothText() {
                    return this.bothField.method_1882();
                }

                public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                    int rowX = this.method_73380();
                    int rowY = this.method_73382();
                    int rowWidth = this.method_73387();
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.method_25368();
                    int fieldW = this.maleField.method_25368();
                    int idW = Math.max(130, rowWidth - removeW - fieldW * 3 - 30);
                    int maleX = rowX + idW + 6;
                    int femaleX = maleX + fieldW + 6;
                    int bothX = femaleX + fieldW + 6;
                    int removeX = bothX + fieldW + 6;
                    this.idField.method_46421(rowX);
                    this.idField.method_46419(widgetY);
                    this.idField.method_25358(idW);
                    this.maleField.method_46421(maleX);
                    this.maleField.method_46419(widgetY);
                    this.femaleField.method_46421(femaleX);
                    this.femaleField.method_46419(widgetY);
                    this.bothField.method_46421(bothX);
                    this.bothField.method_46419(widgetY);
                    this.removeButton.method_46421(removeX);
                    this.removeButton.method_46419(widgetY);
                    for (class_339 widget : this.widgets) {
                        widget.method_25394(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends class_6379> method_37025() {
                    return this.widgets;
                }

                public List<? extends class_364> method_25396() {
                    return this.widgets;
                }
            }
        }

        private static final class ResetGenderSpawnConfirmScreen
        extends class_437 {
            private final class_437 parent;
            private final Runnable onConfirm;

            private ResetGenderSpawnConfirmScreen(class_437 parent, Runnable onConfirm) {
                super((class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void method_25426() {
                int centerX = this.field_22789 / 2;
                int buttonY = this.field_22790 / 2 + 20;
                class_4185 yesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    class_310.method_1551().method_1507(this.parent);
                }).method_46434(centerX - 100, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)yesButton);
                class_4185 noButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 4, buttonY, 96, 20).method_46431();
                this.method_37063((class_364)noButton);
            }

            public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
                super.method_25394(context, mouseX, mouseY, delta);
                context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, this.field_22790 / 2 - 20, -1);
                context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.gender_spawn_reset_body"), this.field_22789 / 2, this.field_22790 / 2 - 4, -3355444);
            }
        }
    }

    static class LoadOrderScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private boolean editable;
        private final List<String> externalPackOrder = new ArrayList<String>();
        private List<String> appliedExternalOrder = List.of();
        private List<String> defaultExternalOrder = List.of();
        private PackOrderList packOrderList;
        private class_4185 resetButton;

        protected LoadOrderScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order_title"));
            this.parent = parent;
            this.config = config == null ? new NonConfig() : config;
        }

        protected void method_25426() {
            this.editable = NonModMenuScreens.canEditServerGameplaySettings();
            this.reloadExternalOrder();
            int centerX = this.field_22789 / 2;
            int listTop = 40;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            this.packOrderList = new PackOrderList(this.field_22787, this.field_22789, listHeight, listTop, this.field_22793, this::moveUp, this::moveDown, this.editable);
            this.method_37063((class_364)this.packOrderList);
            this.rebuildRows();
            this.resetButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_gain_reset"), button -> {
                this.externalPackOrder.clear();
                this.externalPackOrder.addAll(this.defaultExternalOrder);
                this.rebuildRows();
                this.updateButtons();
            }).method_46434(centerX - 100, this.field_22790 - 50, 98, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetButton, "config.needsofnature.tooltip.loaded_animations.load_order.reset");
            this.method_37063((class_364)this.resetButton);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> {
                boolean changed;
                boolean bl = changed = !this.externalPackOrder.equals(this.appliedExternalOrder);
                if (this.editable) {
                    this.config.setExternalNoNPackLoadOrder(this.externalPackOrder);
                    this.config.save();
                    this.appliedExternalOrder = List.copyOf(this.externalPackOrder);
                    if (changed) {
                        this.triggerInWorldReload();
                    }
                }
                class_310.method_1551().method_1507(this.parent);
            }).method_46434(centerX + 2, this.field_22790 - 50, 98, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
            this.method_37063((class_364)doneButton);
            this.updateButtons();
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 12, -1);
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order.summary"), 8, 24, -5197648);
            if (!this.editable) {
                context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order.read_only"), 8, 33, -14739);
            }
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        private void reloadExternalOrder() {
            List<String> discovered = NonExternalPackProvider.listExternalPackFileNames(NonExternalPackProvider.resolveDefaultPacksDir());
            this.defaultExternalOrder = List.copyOf(discovered);
            this.externalPackOrder.clear();
            this.externalPackOrder.addAll(NonExternalPackProvider.normalizeConfiguredOrder(this.config.getExternalNoNPackLoadOrder(), discovered));
            this.appliedExternalOrder = List.copyOf(this.externalPackOrder);
        }

        private void triggerInWorldReload() {
            class_310 client = class_310.method_1551();
            if (client == null || client.field_1687 == null) {
                return;
            }
            class_1132 server = client.method_1576();
            if (server != null) {
                server.execute(() -> {
                    try {
                        server.method_3734().method_9235().execute("reload", (Object)server.method_3739());
                    }
                    catch (Exception e) {
                        server.method_29439(server.method_3836().method_29210());
                    }
                });
            }
            client.method_1521();
        }

        private void moveUp(String key) {
            this.moveBy(key, -1);
        }

        private void moveDown(String key) {
            this.moveBy(key, 1);
        }

        private void moveBy(String key, int delta) {
            if (!this.editable || key == null || key.isBlank()) {
                return;
            }
            int index = this.externalPackOrder.indexOf(key);
            if (index < 0) {
                return;
            }
            int target = index + delta;
            if (target < 0 || target >= this.externalPackOrder.size()) {
                return;
            }
            String tmp = this.externalPackOrder.get(index);
            this.externalPackOrder.set(index, this.externalPackOrder.get(target));
            this.externalPackOrder.set(target, tmp);
            this.rebuildRows();
            this.updateButtons();
        }

        private void rebuildRows() {
            if (this.packOrderList == null) {
                return;
            }
            ArrayList<PackOrderRow> rows = new ArrayList<PackOrderRow>();
            for (int i = 0; i < this.externalPackOrder.size(); ++i) {
                String fileName = this.externalPackOrder.get(i);
                rows.add(PackOrderRow.external(fileName, i, this.externalPackOrder.size()));
            }
            rows.add(PackOrderRow.locked("builtin:needsofnature", (class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order.base_non")));
            rows.add(PackOrderRow.locked("builtin:animationframework", (class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order.base_afw")));
            this.packOrderList.setRows(rows);
        }

        private void updateButtons() {
            if (this.resetButton != null) {
                this.resetButton.field_22763 = this.editable && !this.externalPackOrder.equals(this.defaultExternalOrder);
            }
        }

        private static final class PackOrderList
        extends class_4265<RowEntry> {
            private final class_327 textRenderer;
            private final Consumer<String> onMoveUp;
            private final Consumer<String> onMoveDown;
            private final boolean editable;

            private PackOrderList(class_310 client, int width, int height, int top, class_327 textRenderer, Consumer<String> onMoveUp, Consumer<String> onMoveDown, boolean editable) {
                super(client, width, height, top, 22);
                this.textRenderer = textRenderer;
                this.onMoveUp = onMoveUp;
                this.onMoveDown = onMoveDown;
                this.editable = editable;
                this.field_22744 = false;
            }

            private void setRows(List<PackOrderRow> rows) {
                this.method_25339();
                for (PackOrderRow row : rows) {
                    this.method_73370((class_350.class_351)new RowEntry(row, this.textRenderer, this.onMoveUp, this.onMoveDown, this.editable), 22);
                }
            }

            public int method_25322() {
                return Math.max(120, this.method_25368() - 24);
            }

            public int method_25342() {
                return 8;
            }

            protected int method_65507() {
                return this.method_25368() - 8;
            }

            private static final class RowEntry
            extends class_4265.class_4266<RowEntry> {
                private final PackOrderRow row;
                private final class_327 textRenderer;
                private final Consumer<String> onMoveUp;
                private final Consumer<String> onMoveDown;
                private final boolean editable;
                private final NonModMenuScreens.FlatTextButton upButton;
                private final NonModMenuScreens.FlatTextButton downButton;
                private final List<class_339> widgets;

                private RowEntry(PackOrderRow row, class_327 textRenderer, Consumer<String> onMoveUp, Consumer<String> onMoveDown, boolean editable) {
                    this.row = row;
                    this.textRenderer = textRenderer;
                    this.onMoveUp = onMoveUp;
                    this.onMoveDown = onMoveDown;
                    this.editable = editable;
                    if (row.movable()) {
                        this.upButton = new NonModMenuScreens.FlatTextButton(16, (class_2561)class_2561.method_43470((String)"^"), button -> {
                            if (this.onMoveUp != null) {
                                this.onMoveUp.accept(this.row.key());
                            }
                        });
                        this.downButton = new NonModMenuScreens.FlatTextButton(16, (class_2561)class_2561.method_43470((String)"v"), button -> {
                            if (this.onMoveDown != null) {
                                this.onMoveDown.accept(this.row.key());
                            }
                        });
                        this.widgets = List.of(this.upButton, this.downButton);
                    } else {
                        this.upButton = null;
                        this.downButton = null;
                        this.widgets = List.of();
                    }
                }

                public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                    int x = this.method_73380() + 4;
                    int y = this.method_73382() + 7;
                    int right = this.method_73380() + this.method_73387() - 2;
                    int textWidth = Math.max(20, this.method_73387() - (this.row.movable() ? 46 : 0));
                    int color = this.row.movable() ? -2565928 : -7362305;
                    context.method_25303(this.textRenderer, this.textRenderer.method_27523(this.row.label().getString(), textWidth), x, y, color);
                    if (this.upButton != null && this.downButton != null) {
                        this.upButton.field_22763 = this.editable && this.row.canMoveUp();
                        this.downButton.field_22763 = this.editable && this.row.canMoveDown();
                        this.upButton.method_46421(right - 34);
                        this.upButton.method_46419(y - 2);
                        this.downButton.method_46421(right - 18);
                        this.downButton.method_46419(y - 2);
                        this.upButton.method_25394(context, mouseX, mouseY, delta);
                        this.downButton.method_25394(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends class_6379> method_37025() {
                    return List.copyOf(this.widgets);
                }

                public void method_48206(Consumer<class_339> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends class_364> method_25396() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private record PackOrderRow(String key, class_2561 label, boolean movable, boolean canMoveUp, boolean canMoveDown) {
            private static PackOrderRow external(String fileName, int index, int total) {
                String safeName = fileName == null || fileName.isBlank() ? "<unknown>" : fileName;
                return new PackOrderRow(safeName, (class_2561)class_2561.method_43470((String)(index + 1 + ". " + safeName)), true, index > 0, index < total - 1);
            }

            private static PackOrderRow locked(String key, class_2561 label) {
                String safeKey = key == null || key.isBlank() ? "<locked>" : key;
                class_2561 safeLabel = label == null ? class_2561.method_43470((String)safeKey) : label;
                return new PackOrderRow(safeKey, safeLabel, false, false, false);
            }
        }
    }

    static class LoadedAnimationsScreen
    extends class_437 {
        private final class_437 parent;
        private final Set<String> expandedRowKeys = new HashSet<String>();
        private final Set<String> expandedPackIds = new HashSet<String>();
        private final Map<String, String> sourceHintByDefinitionId = new LinkedHashMap<String, String>();
        private class_342 filterField;
        private class_4185 filterAllButton;
        private class_4185 filterMissingButton;
        private class_4185 filterPartialButton;
        private class_4185 filterOkButton;
        private class_4185 sortModeButton;
        private class_4185 sortOrderButton;
        private LoadedAnimationsList loadedAnimationsList;
        private List<LoadedAnimationRow> allRows = List.of();
        private final Map<String, class_2960> packIconByResourcePackId = new LinkedHashMap<String, class_2960>();
        private final Set<String> disabledAnimationPacks = new LinkedHashSet<String>();
        private final Set<String> disabledAnimations = new LinkedHashSet<String>();
        private int filteredCount = 0;
        private boolean editable = true;
        private StatusFilter statusFilter = StatusFilter.ALL;
        private SortMode sortMode = SortMode.ID;
        private boolean sortAscending = true;
        private class_2561 copyFeedbackText = class_2561.method_43473();
        private long copyFeedbackUntilMs = 0L;
        private static final long COPY_FEEDBACK_DURATION_MS = 1600L;
        private static final int MISSING_ANIM_PREVIEW_COUNT = 3;
        private static final int MISSING_MODEL_PREVIEW_COUNT = 3;
        private static final class_2960 PLAYER_ID = class_2960.method_60655((String)"minecraft", (String)"player");

        protected LoadedAnimationsScreen(class_437 parent) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations_title"));
            this.parent = parent;
        }

        protected void method_25426() {
            String previousFilter = this.filterField == null ? "" : this.filterField.method_1882();
            this.editable = NonModMenuScreens.canEditServerGameplaySettings();
            this.reloadDisabledAnimationState();
            int centerX = this.field_22789 / 2;
            int filterY = 34;
            int horizontalPadding = 8;
            int filterWidth = Math.max(120, this.field_22789 - horizontalPadding * 2);
            this.filterField = new class_342(this.field_22793, horizontalPadding, filterY, filterWidth, 20, (class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations_search"));
            this.filterField.method_1880(128);
            this.filterField.method_1852(previousFilter);
            this.filterField.method_1863(this::applyFilter);
            this.method_37063((class_364)this.filterField);
            int controlsY = filterY + 24;
            int buttonH = 20;
            int smallButtonW = 62;
            int filterGap = 4;
            int startX = horizontalPadding;
            this.filterAllButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"All"), button -> {
                this.statusFilter = StatusFilter.ALL;
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882());
            }).method_46434(startX, controlsY, smallButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.filterAllButton);
            this.filterMissingButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"Missing"), button -> {
                this.statusFilter = StatusFilter.MISSING;
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882());
            }).method_46434(startX + (smallButtonW + filterGap), controlsY, smallButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.filterMissingButton);
            this.filterPartialButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"Partial"), button -> {
                this.statusFilter = StatusFilter.PARTIAL;
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882());
            }).method_46434(startX + (smallButtonW + filterGap) * 2, controlsY, smallButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.filterPartialButton);
            this.filterOkButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"OK"), button -> {
                this.statusFilter = StatusFilter.OK;
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882());
            }).method_46434(startX + (smallButtonW + filterGap) * 3, controlsY, smallButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.filterOkButton);
            int orderButtonW = 56;
            int sortButtonW = 116;
            int orderX = this.field_22789 - horizontalPadding - orderButtonW;
            int sortX = orderX - 6 - sortButtonW;
            this.sortModeButton = class_4185.method_46430((class_2561)this.sortModeLabel(), button -> {
                this.sortMode = switch (this.sortMode.ordinal()) {
                    default -> throw new MatchException(null, null);
                    case 0 -> SortMode.STATUS;
                    case 1 -> SortMode.MISSING;
                    case 2 -> SortMode.ENTITIES;
                    case 3 -> SortMode.ID;
                };
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882(), false);
            }).method_46434(sortX, controlsY, sortButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.sortModeButton);
            this.sortOrderButton = class_4185.method_46430((class_2561)this.sortOrderLabel(), button -> {
                this.sortAscending = !this.sortAscending;
                this.updateControlButtons();
                this.applyFilter(this.filterField.method_1882(), false);
            }).method_46434(orderX, controlsY, orderButtonW, buttonH).method_46431();
            this.method_37063((class_364)this.sortOrderButton);
            int listTop = controlsY + buttonH + 6;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            this.loadedAnimationsList = new LoadedAnimationsList(this.field_22787, this.field_22789, listHeight, listTop, this.field_22793, this::togglePackExpanded, this::toggleRowExpanded, this::copyRowMissingDetails, this::togglePackEnabled, this::toggleAnimationEnabled, () -> this.editable);
            this.method_37063((class_364)this.loadedAnimationsList);
            this.rebuildSourceHints();
            this.rebuildRows();
            this.updateControlButtons();
            this.applyFilter(previousFilter);
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 12, -1);
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43469((String)"config.needsofnature.loaded_animations_summary", (Object[])new Object[]{this.allRows.size(), this.filteredCount}), 8, 22, -5197648);
            if (this.copyFeedbackText != null && !this.copyFeedbackText.getString().isBlank() && System.currentTimeMillis() < this.copyFeedbackUntilMs) {
                int feedbackWidth = this.field_22793.method_27525((class_5348)this.copyFeedbackText);
                int feedbackX = Math.max(8, this.field_22789 - feedbackWidth - 8);
                context.method_27535(this.field_22793, this.copyFeedbackText, feedbackX, 22, -6560902);
            }
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        private void updateControlButtons() {
            if (this.filterAllButton != null) {
                this.filterAllButton.method_25355(this.statusFilterLabel(StatusFilter.ALL));
                boolean bl = this.filterAllButton.field_22763 = this.statusFilter != StatusFilter.ALL;
            }
            if (this.filterMissingButton != null) {
                this.filterMissingButton.method_25355(this.statusFilterLabel(StatusFilter.MISSING));
                boolean bl = this.filterMissingButton.field_22763 = this.statusFilter != StatusFilter.MISSING;
            }
            if (this.filterPartialButton != null) {
                this.filterPartialButton.method_25355(this.statusFilterLabel(StatusFilter.PARTIAL));
                boolean bl = this.filterPartialButton.field_22763 = this.statusFilter != StatusFilter.PARTIAL;
            }
            if (this.filterOkButton != null) {
                this.filterOkButton.method_25355(this.statusFilterLabel(StatusFilter.OK));
                boolean bl = this.filterOkButton.field_22763 = this.statusFilter != StatusFilter.OK;
            }
            if (this.sortModeButton != null) {
                this.sortModeButton.method_25355(this.sortModeLabel());
            }
            if (this.sortOrderButton != null) {
                this.sortOrderButton.method_25355(this.sortOrderLabel());
            }
        }

        private class_2561 statusFilterLabel(StatusFilter mode) {
            boolean selected = this.statusFilter == mode;
            String base = switch (mode.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> "All";
                case 1 -> "Missing";
                case 2 -> "Partial";
                case 3 -> "OK";
            };
            return class_2561.method_43470((String)(selected ? "[" + base + "]" : base));
        }

        private class_2561 sortModeLabel() {
            String value = switch (this.sortMode.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> "ID";
                case 1 -> "Status";
                case 2 -> "Missing";
                case 3 -> "Entities";
            };
            return class_2561.method_43470((String)("Sort: " + value));
        }

        private class_2561 sortOrderLabel() {
            return class_2561.method_43470((String)(this.sortAscending ? "Asc" : "Desc"));
        }

        private void rebuildSourceHints() {
            this.sourceHintByDefinitionId.clear();
            class_3300 manager = this.resolveResourceManagerForAnimdefs();
            if (manager == null) {
                return;
            }
            try {
                Map resources = manager.method_41265("afw_animdefs", id -> id.method_12832().endsWith(".json"));
                for (Map.Entry entry : resources.entrySet()) {
                    class_2960 definitionId = LoadedAnimationsScreen.definitionIdFromFile((class_2960)entry.getKey());
                    String sourceHint = LoadedAnimationsScreen.summarizeSources((List)entry.getValue());
                    this.sourceHintByDefinitionId.put(definitionId.toString(), sourceHint);
                }
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve animation definition source hints.", (Throwable)e);
            }
        }

        private class_3300 resolveResourceManagerForAnimdefs() {
            class_310 client = class_310.method_1551();
            if (client == null) {
                return null;
            }
            try {
                if (client.method_1576() != null) {
                    return client.method_1576().method_34864();
                }
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve server resource manager for loaded animations.", (Throwable)e);
            }
            try {
                return client.method_1478();
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve client resource manager for loaded animations.", (Throwable)e);
                return null;
            }
        }

        private static class_2960 definitionIdFromFile(class_2960 fileId) {
            String prefix;
            if (fileId == null) {
                return class_2960.method_60655((String)"animationframework", (String)"unknown");
            }
            String path = fileId.method_12832();
            if (path.startsWith(prefix = "afw_animdefs/")) {
                path = path.substring(prefix.length());
            }
            if (path.endsWith(".json")) {
                path = path.substring(0, path.length() - 5);
            }
            return class_2960.method_60655((String)fileId.method_12836(), (String)path);
        }

        private static String summarizeSources(List<class_3298> resources) {
            if (resources == null || resources.isEmpty()) {
                return "unknown";
            }
            LinkedHashSet<String> sources = new LinkedHashSet<String>();
            for (class_3298 resource : resources) {
                if (resource == null) continue;
                sources.add(LoadedAnimationsScreen.normalizeSourceId(resource.method_14480()));
            }
            if (sources.isEmpty()) {
                return "unknown";
            }
            ArrayList list = new ArrayList(sources);
            int take = Math.min(2, list.size());
            Object summary = String.join((CharSequence)", ", list.subList(0, take));
            int more = list.size() - take;
            if (more > 0) {
                summary = (String)summary + ", +" + more + " more";
            }
            return summary;
        }

        private static String normalizeSourceId(String rawPackId) {
            if (rawPackId == null || rawPackId.isBlank()) {
                return "unknown";
            }
            if ("vanilla".equals(rawPackId)) {
                return "builtin:vanilla";
            }
            if (rawPackId.startsWith("needsofnature/")) {
                String value = rawPackId.substring("needsofnature/".length());
                int slash = value.lastIndexOf(47);
                String tail = slash >= 0 && slash < value.length() - 1 ? value.substring(slash + 1) : value;
                int underscore = tail.indexOf(95);
                if (underscore > 0) {
                    boolean numericPrefix = true;
                    for (int i = 0; i < underscore; ++i) {
                        char c = tail.charAt(i);
                        if (c >= '0' && c <= '9') continue;
                        numericPrefix = false;
                        break;
                    }
                    if (numericPrefix && underscore + 1 < tail.length()) {
                        tail = tail.substring(underscore + 1);
                    }
                }
                return "nonpack:" + tail;
            }
            return rawPackId;
        }

        private void reloadDisabledAnimationState() {
            this.disabledAnimationPacks.clear();
            this.disabledAnimations.clear();
            NonConfig config = NeedsOfNature.getConfig();
            if (config == null) {
                return;
            }
            this.disabledAnimationPacks.addAll(config.getDisabledAnimationPacks());
            this.disabledAnimations.addAll(config.getDisabledAnimations());
        }

        private void togglePackEnabled(String packId) {
            if (!this.editable || packId == null || packId.isBlank()) {
                return;
            }
            if (this.disabledAnimationPacks.contains(packId)) {
                this.disabledAnimationPacks.remove(packId);
            } else {
                this.disabledAnimationPacks.add(packId);
            }
            this.saveDisabledAnimationState();
        }

        private void toggleAnimationEnabled(String animationId) {
            if (!this.editable || animationId == null || animationId.isBlank()) {
                return;
            }
            if (this.disabledAnimations.contains(animationId)) {
                this.disabledAnimations.remove(animationId);
            } else {
                this.disabledAnimations.add(animationId);
            }
            this.saveDisabledAnimationState();
        }

        private void saveDisabledAnimationState() {
            NonConfig config = NeedsOfNature.getConfig();
            if (config == null) {
                return;
            }
            config.setDisabledAnimationPacks(new ArrayList<String>(this.disabledAnimationPacks));
            config.setDisabledAnimations(new ArrayList<String>(this.disabledAnimations));
            config.save();
            NeedsOfNature.applyAnimationToggleConfig();
            this.rebuildRows();
            this.applyFilter(this.filterField == null ? "" : this.filterField.method_1882(), false);
        }

        @Nullable
        private class_2960 resolvePackIcon(String resourcePackId) {
            if (resourcePackId == null || resourcePackId.isBlank()) {
                return null;
            }
            if (this.packIconByResourcePackId.containsKey(resourcePackId)) {
                return this.packIconByResourcePackId.get(resourcePackId);
            }
            class_2960 textureId = this.loadPackIcon(resourcePackId);
            this.packIconByResourcePackId.put(resourcePackId, textureId);
            return textureId;
        }

        /*
         * Enabled aggressive exception aggregation
         */
        @Nullable
        private class_2960 loadPackIcon(String resourcePackId) {
            class_310 client = class_310.method_1551();
            class_3300 manager = this.resolveResourceManagerForAnimdefs();
            if (client == null || manager == null) {
                return null;
            }
            try (Stream stream = manager.method_29213();){
                class_2960 class_29602;
                block21: {
                    InputStream input;
                    block19: {
                        class_2960 class_29603;
                        block20: {
                            class_3262 pack = stream.filter(candidate -> resourcePackId.equals(candidate.method_14409())).findFirst().orElse(null);
                            if (pack == null) {
                                class_2960 class_29604 = null;
                                return class_29604;
                            }
                            input = LoadedAnimationsScreen.openRootResource(pack, "pack.png");
                            try {
                                if (input != null) break block19;
                                class_29603 = null;
                                if (input == null) break block20;
                            }
                            catch (Throwable throwable) {
                                if (input != null) {
                                    try {
                                        input.close();
                                    }
                                    catch (Throwable throwable2) {
                                        throwable.addSuppressed(throwable2);
                                    }
                                }
                                throw throwable;
                            }
                            input.close();
                        }
                        return class_29603;
                    }
                    class_1011 image = class_1011.method_4309((InputStream)input);
                    class_2960 id = class_2960.method_60655((String)"needsofnature", (String)("dynamic/animation_pack_icons/" + LoadedAnimationsScreen.sanitizeTexturePath(resourcePackId)));
                    client.method_1531().method_4616(id, (class_1044)new class_1043(() -> ((class_2960)id).toString(), image));
                    class_29602 = id;
                    if (input == null) break block21;
                    input.close();
                }
                return class_29602;
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to load animation pack icon for {}", (Object)resourcePackId, (Object)e);
                return null;
            }
        }

        @Nullable
        private static InputStream openRootResource(class_3262 pack, String fileName) {
            if (pack == null || fileName == null) {
                return null;
            }
            class_7367 supplier = pack.method_14410(new String[]{fileName});
            if (supplier == null) {
                return null;
            }
            try {
                return (InputStream)supplier.get();
            }
            catch (IOException | RuntimeException e) {
                return null;
            }
        }

        private static String sanitizeTexturePath(String raw) {
            if (raw == null || raw.isBlank()) {
                return "unknown";
            }
            String lower = raw.toLowerCase(Locale.ROOT);
            StringBuilder sb = new StringBuilder(lower.length());
            for (int i = 0; i < lower.length(); ++i) {
                char c = lower.charAt(i);
                if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_' || c == '-' || c == '/') {
                    sb.append(c);
                    continue;
                }
                sb.append('_');
            }
            return sb.isEmpty() ? "unknown" : sb.toString();
        }

        private void copyRowMissingDetails(LoadedAnimationRow row) {
            if (row == null || !row.hasMissingDetails()) {
                return;
            }
            class_310 client = class_310.method_1551();
            if (client == null) {
                return;
            }
            String text = String.join((CharSequence)"\n", row.missingLines());
            client.field_1774.method_1455(text);
            this.showCopyFeedback((class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.copied_missing"));
        }

        private void showCopyFeedback(class_2561 message) {
            this.copyFeedbackText = message == null ? class_2561.method_43473() : message;
            this.copyFeedbackUntilMs = System.currentTimeMillis() + 1600L;
        }

        private void rebuildRows() {
            ArrayList<AfwAnimationDefinitions.Definition> definitions = new ArrayList<AfwAnimationDefinitions.Definition>(LoadedAnimationsScreen.getLoadedDefinitionsSnapshot());
            definitions.sort(Comparator.comparing(def -> def.id().toString()));
            ArrayList<LoadedAnimationRow> rows = new ArrayList<LoadedAnimationRow>(definitions.size());
            for (AfwAnimationDefinitions.Definition definition : definitions) {
                rows.add(this.buildRow(definition));
            }
            this.allRows = List.copyOf(rows);
        }

        private static List<AfwAnimationDefinitions.Definition> getLoadedDefinitionsSnapshot() {
            return AfwAnimationDefinitions.getLoadedDefinitionsSnapshot();
        }

        private LoadedAnimationRow buildRow(AfwAnimationDefinitions.Definition definition) {
            List<String> contentTags;
            List<String> actorKeys = LoadedAnimationsScreen.buildUniqueActorKeys(definition.actors());
            ArrayList<CallSite> actorParts = new ArrayList<CallSite>();
            for (int i = 0; i < definition.actors().size(); ++i) {
                AfwAnimationDefinitions.ActorConstraint actor = (AfwAnimationDefinitions.ActorConstraint)definition.actors().get(i);
                String key = actorKeys.get(i);
                actorParts.add((CallSite)((Object)(key + "=" + LoadedAnimationsScreen.summarizeEntityTypes(actor.entityTypes()))));
            }
            Integer peakStage = NonPeakStages.getPeakStage(definition.id());
            Object peakLabel = peakStage == null ? "-" : "p" + peakStage;
            ResourceCheck check = LoadedAnimationsScreen.evaluateResourceCheck(definition, actorKeys);
            ResourceStatus status = check.status();
            String statusLabel = class_2561.method_43471((String)status.translationKey).getString();
            String key = definition.id().toString();
            AfwAnimationDefinitions.AnimationPackInfo packInfo = definition.packInfo();
            String packId = packInfo == null ? "unknown" : packInfo.id();
            String resourcePackId = packInfo == null ? "unknown" : packInfo.resourcePackId();
            String packName = packInfo == null ? "Unknown Pack" : packInfo.name();
            String packAuthor = packInfo == null ? "Unknown" : packInfo.author();
            boolean packDisabled = this.disabledAnimationPacks.contains(packId);
            boolean animationDisabled = this.disabledAnimations.contains(key);
            String title = LoadedAnimationsScreen.shortId(definition.id()) + (packDisabled ? " [pack off]" : (animationDisabled ? " [off]" : ""));
            String actorsLine = "actors: " + (actorParts.isEmpty() ? "-" : String.join((CharSequence)", ", actorParts));
            String stagesLine = "stages: " + definition.stages().size() + " | peak: " + (String)peakLabel + " | " + LoadedAnimationsScreen.summarizeStages(definition.stages());
            String resourcesLine = "resources: anim " + check.animationFound + "/" + check.animationTotal + " | model " + check.modelFound + "/" + check.modelTotal;
            if (check.modelUnchecked > 0) {
                resourcesLine = resourcesLine + " | model n/a " + check.modelUnchecked;
            }
            String contentTagsLine = "content tags: " + ((contentTags = LoadedAnimationsScreen.readContentTags(definition)).isEmpty() ? "-" : String.join((CharSequence)", ", contentTags));
            String blockRequirementLine = LoadedAnimationsScreen.summarizeBlockRequirements(definition.blockRequirements());
            ArrayList<Object> infoLines = new ArrayList<Object>();
            infoLines.add(actorsLine);
            infoLines.add(stagesLine);
            if (blockRequirementLine != null) {
                infoLines.add(blockRequirementLine);
            }
            if (status != ResourceStatus.OK) {
                infoLines.add(resourcesLine);
            }
            infoLines.add(contentTagsLine);
            boolean hasMissingDetails = check.hasMissingDetails();
            List<String> missingLines = check.missingLinesStructured();
            int entityTypeCount = LoadedAnimationsScreen.countRuntimeEntityTypes(definition);
            int missingCount = check.totalMissingCount();
            String searchBlob = (String.valueOf(definition.id()) + " " + String.join((CharSequence)" ", infoLines) + " " + resourcesLine + " " + packName + " " + packAuthor + " " + this.sourceHintByDefinitionId.getOrDefault(key, "unknown") + (String)(hasMissingDetails ? " " + String.join((CharSequence)" ", missingLines) : "")).toLowerCase(Locale.ROOT);
            return new LoadedAnimationRow(key, packId, resourcePackId, packName, packAuthor, title, List.copyOf(infoLines), missingLines, searchBlob, status, missingCount, entityTypeCount, packDisabled, animationDisabled, hasMissingDetails, hasMissingDetails && this.expandedRowKeys.contains(key));
        }

        private void applyFilter(String rawFilter) {
            this.applyFilter(rawFilter, true);
        }

        private void applyFilter(String rawFilter, boolean resetScroll) {
            String filter = rawFilter == null ? "" : rawFilter.trim().toLowerCase(Locale.ROOT);
            ArrayList<LoadedAnimationRow> filtered = new ArrayList<LoadedAnimationRow>();
            for (LoadedAnimationRow row : this.allRows) {
                if (!row.matches(filter) || !row.matchesStatus(this.statusFilter)) continue;
                filtered.add(row);
            }
            filtered.sort(this.groupedRowComparator());
            this.filteredCount = filtered.size();
            if (this.loadedAnimationsList != null) {
                this.loadedAnimationsList.setRows(this.buildGroupedDisplayRows(filtered));
                if (resetScroll) {
                    this.loadedAnimationsList.method_44382(0.0);
                }
            }
        }

        private List<LoadedListRow> buildGroupedDisplayRows(List<LoadedAnimationRow> filtered) {
            if (filtered == null || filtered.isEmpty()) {
                return List.of();
            }
            LinkedHashMap<String, Integer> totalsByPack = new LinkedHashMap<String, Integer>();
            for (LoadedAnimationRow row : this.allRows) {
                totalsByPack.merge(row.packId(), 1, Integer::sum);
            }
            ArrayList<LoadedListRow> out = new ArrayList<LoadedListRow>();
            String currentPackId = null;
            ArrayList<LoadedAnimationRow> currentRows = new ArrayList<LoadedAnimationRow>();
            for (LoadedAnimationRow row : filtered) {
                if (!Objects.equals(currentPackId, row.packId())) {
                    this.appendPackGroup(out, currentPackId, currentRows, totalsByPack);
                    currentRows.clear();
                    currentPackId = row.packId();
                }
                currentRows.add(row);
            }
            this.appendPackGroup(out, currentPackId, currentRows, totalsByPack);
            return List.copyOf(out);
        }

        private void appendPackGroup(List<LoadedListRow> out, @Nullable String packId, List<LoadedAnimationRow> rows, Map<String, Integer> totalsByPack) {
            if (out == null || packId == null || rows == null || rows.isEmpty()) {
                return;
            }
            LoadedAnimationRow first = rows.getFirst();
            out.add(new LoadedPackHeaderRow("pack:" + packId, packId, first.resourcePackId(), first.packName(), first.packAuthor(), rows.size(), totalsByPack.getOrDefault(packId, rows.size()), this.disabledAnimationPacks.contains(packId), this.expandedPackIds.contains(packId), this.resolvePackIcon(first.resourcePackId())));
            if (this.expandedPackIds.contains(packId)) {
                out.addAll(rows);
            }
        }

        private void togglePackExpanded(String packId) {
            if (packId == null || packId.isBlank()) {
                return;
            }
            if (this.expandedPackIds.contains(packId)) {
                this.expandedPackIds.remove(packId);
            } else {
                this.expandedPackIds.add(packId);
            }
            this.applyFilter(this.filterField == null ? "" : this.filterField.method_1882(), false);
        }

        private void toggleRowExpanded(String key) {
            if (key == null || key.isBlank()) {
                return;
            }
            if (this.expandedRowKeys.contains(key)) {
                this.expandedRowKeys.remove(key);
            } else {
                this.expandedRowKeys.add(key);
            }
            this.rebuildRows();
            this.applyFilter(this.filterField == null ? "" : this.filterField.method_1882(), false);
        }

        private Comparator<LoadedAnimationRow> rowComparator() {
            Comparator<LoadedAnimationRow> comparator = switch (this.sortMode.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> Comparator.comparing(LoadedAnimationRow::key);
                case 1 -> Comparator.comparingInt(row -> row.status().sortRank()).thenComparing(LoadedAnimationRow::key);
                case 2 -> Comparator.comparingInt(row -> row.missingCount()).thenComparing(LoadedAnimationRow::key);
                case 3 -> Comparator.comparingInt(row -> row.entityTypeCount()).thenComparing(LoadedAnimationRow::key);
            };
            return this.sortAscending ? comparator : comparator.reversed();
        }

        private Comparator<LoadedAnimationRow> groupedRowComparator() {
            return Comparator.comparing(row -> row.packName().toLowerCase(Locale.ROOT)).thenComparing(row -> row.packId().toLowerCase(Locale.ROOT)).thenComparing(this.rowComparator());
        }

        private static List<class_2960> stageIdsFor(AfwAnimationDefinitions.Definition definition) {
            LinkedHashSet<class_2960> ids = new LinkedHashSet<class_2960>();
            if (definition.stages().isEmpty()) {
                ids.add(definition.id());
            } else {
                for (AnimationStageInfo stage : definition.stages()) {
                    ids.add(stage.effectiveAnimationId());
                }
            }
            return List.copyOf(ids);
        }

        private static List<String> readContentTags(AfwAnimationDefinitions.Definition definition) {
            if (definition == null) {
                return List.of();
            }
            try {
                Object value = definition.getClass().getMethod("contentTags", new Class[0]).invoke((Object)definition, new Object[0]);
                if (!(value instanceof Iterable)) {
                    return List.of();
                }
                Iterable iterable = (Iterable)value;
                ArrayList<String> out = new ArrayList<String>();
                for (Object entry : iterable) {
                    String tag;
                    if (entry == null || (tag = entry.toString().trim()).isEmpty()) continue;
                    out.add(tag);
                }
                return out.isEmpty() ? List.of() : List.copyOf(out);
            }
            catch (ReflectiveOperationException | RuntimeException ignored) {
                return List.of();
            }
        }

        @Nullable
        private static String summarizeBlockRequirements(@Nullable AfwAnimationDefinitions.BlockRequirements requirements) {
            AfwAnimationDefinitions.BlockPredicate blocks;
            if (requirements == null) {
                return null;
            }
            ArrayList<Object> parts = new ArrayList<Object>();
            String type = requirements.type() == null || requirements.type().isBlank() ? "unknown" : requirements.type();
            parts.add(type);
            if (requirements.isWall()) {
                AfwAnimationDefinitions.WallHeight height = requirements.height();
                if (height != null) {
                    parts.add(height.max() == null ? "height " + height.min() + "+" : "height " + height.min() + "-" + height.max());
                }
            } else if (requirements.isCenterSupport()) {
                if (requirements.support() != null) {
                    parts.add(requirements.support().id());
                }
                if (requirements.placement() != null) {
                    parts.add(requirements.placement().id());
                }
                if (requirements.surfaceFootprint() != null) {
                    AfwAnimationDefinitions.SurfaceFootprint footprint = requirements.surfaceFootprint();
                    parts.add("footprint " + footprint.width() + "x" + footprint.depth() + "x" + footprint.height());
                    if (footprint.margin() > 0) {
                        parts.add("margin " + footprint.margin());
                    }
                } else if (requirements.surfaceRadius() > 0) {
                    parts.add("surface radius " + requirements.surfaceRadius());
                }
            }
            AfwAnimationDefinitions.Clearance clearance = requirements.clearance();
            if (clearance != null) {
                parts.add("clearance " + clearance.width() + "x" + clearance.height() + "x" + clearance.depth());
            }
            if ((blocks = requirements.blocks()) != null && !blocks.isEmpty()) {
                parts.add("blocks " + LoadedAnimationsScreen.summarizeBlockPredicate(blocks));
            }
            return "block: " + String.join((CharSequence)" | ", parts);
        }

        private static String summarizeBlockPredicate(AfwAnimationDefinitions.BlockPredicate blocks) {
            if (blocks == null || blocks.isEmpty()) {
                return "-";
            }
            ArrayList<Object> values = new ArrayList<Object>();
            if (blocks.blockIds() != null) {
                for (class_2960 id : blocks.blockIds()) {
                    if (id == null) continue;
                    values.add(LoadedAnimationsScreen.shortId(id));
                }
            }
            if (blocks.tagIds() != null) {
                for (class_2960 id : blocks.tagIds()) {
                    if (id == null) continue;
                    values.add("#" + LoadedAnimationsScreen.shortId(id));
                }
            }
            if (values.isEmpty()) {
                return "-";
            }
            int take = Math.min(3, values.size());
            String summary = String.join((CharSequence)", ", values.subList(0, take));
            int remaining = values.size() - take;
            return remaining > 0 ? summary + ", +" + remaining : summary;
        }

        private static ResourceCheck evaluateResourceCheck(AfwAnimationDefinitions.Definition definition, List<String> actorKeys) {
            List<class_2960> stageIds = LoadedAnimationsScreen.stageIdsFor(definition);
            int animationTotal = 0;
            int animationFound = 0;
            int modelTotal = 0;
            int modelFound = 0;
            int modelUnchecked = 0;
            LinkedHashSet<CallSite> missingAnimations = new LinkedHashSet<CallSite>();
            LinkedHashSet<String> missingModels = new LinkedHashSet<String>();
            for (int i = 0; i < definition.actors().size(); ++i) {
                AfwAnimationDefinitions.ActorConstraint actor = (AfwAnimationDefinitions.ActorConstraint)definition.actors().get(i);
                String actorKey = actorKeys.get(i);
                List<class_2960> runtimeTypes = LoadedAnimationsScreen.normalizeEntityTypesForRuntime(actor.entityTypes());
                if (runtimeTypes.isEmpty()) {
                    ++modelUnchecked;
                    for (class_2960 stageId : stageIds) {
                        class_2960 animationResource = AfwGeckoResourceResolver.resolveAnimationResource((class_2960)stageId, (String)actorKey, null);
                        ++animationTotal;
                        if (AfwGeckoResourceResolver.hasBakedAnimation((class_2960)animationResource)) {
                            ++animationFound;
                            continue;
                        }
                        missingAnimations.add((CallSite)((Object)("actor=" + actorKey + " -> " + LoadedAnimationsScreen.shortId(animationResource))));
                    }
                    continue;
                }
                for (class_2960 entityTypeId : runtimeTypes) {
                    ModelCoverage coverage = LoadedAnimationsScreen.evaluateModelCoverage(actor, entityTypeId);
                    ++modelTotal;
                    if (coverage.found()) {
                        ++modelFound;
                    } else {
                        missingModels.add(coverage.missingDetail());
                    }
                    for (class_2960 stageId : stageIds) {
                        class_2960 animationResource = AfwGeckoResourceResolver.resolveAnimationResource((class_2960)stageId, (String)actorKey, (class_2960)entityTypeId);
                        ++animationTotal;
                        if (AfwGeckoResourceResolver.hasBakedAnimation((class_2960)animationResource)) {
                            ++animationFound;
                            continue;
                        }
                        missingAnimations.add((CallSite)((Object)("actor=" + actorKey + " type=" + String.valueOf(entityTypeId) + " -> " + LoadedAnimationsScreen.shortId(animationResource))));
                    }
                }
            }
            return new ResourceCheck(animationTotal, animationFound, modelTotal, modelFound, modelUnchecked, List.copyOf(missingAnimations), List.copyOf(missingModels));
        }

        private static String shortId(class_2960 id) {
            if (id == null) {
                return "null";
            }
            if ("animationframework".equals(id.method_12836())) {
                return id.method_12832();
            }
            return id.toString();
        }

        private static List<class_2960> normalizeEntityTypesForRuntime(Set<class_2960> entityTypes) {
            if (entityTypes == null || entityTypes.isEmpty()) {
                return List.of();
            }
            LinkedHashSet<class_2960> normalized = new LinkedHashSet<class_2960>();
            for (class_2960 raw : entityTypes) {
                if (raw == null) continue;
                normalized.add(LoadedAnimationsScreen.normalizeEntityTypeForRuntime(raw));
            }
            ArrayList<class_2960> sorted = new ArrayList<class_2960>(normalized);
            sorted.sort(Comparator.comparing(class_2960::toString));
            return List.copyOf(sorted);
        }

        private static int countRuntimeEntityTypes(AfwAnimationDefinitions.Definition definition) {
            if (definition == null || definition.actors() == null || definition.actors().isEmpty()) {
                return 0;
            }
            int count = 0;
            for (AfwAnimationDefinitions.ActorConstraint actor : definition.actors()) {
                List<class_2960> types = LoadedAnimationsScreen.normalizeEntityTypesForRuntime(actor.entityTypes());
                count += types.size();
            }
            return count;
        }

        private static class_2960 normalizeEntityTypeForRuntime(class_2960 entityTypeId) {
            if (entityTypeId == null) {
                return PLAYER_ID;
            }
            return entityTypeId;
        }

        private static ModelCoverage evaluateModelCoverage(AfwAnimationDefinitions.ActorConstraint actor, class_2960 entityTypeId) {
            boolean found;
            AfwGeckoResourceResolver.ModelAndTexture modelAndTexture = AfwGeckoResourceResolver.resolveModelAndTexture((class_2960)entityTypeId);
            class_2960 baseModelId = modelAndTexture.model();
            boolean baseExists = !modelAndTexture.missingModel();
            class_2960 maleModel = class_2960.method_60655((String)baseModelId.method_12836(), (String)(baseModelId.method_12832() + ".m"));
            class_2960 femaleModel = class_2960.method_60655((String)baseModelId.method_12836(), (String)(baseModelId.method_12832() + ".f"));
            boolean maleExists = AfwGeckoResourceResolver.hasGeoModel((class_2960)maleModel);
            boolean femaleExists = AfwGeckoResourceResolver.hasGeoModel((class_2960)femaleModel);
            GenderRequirement requirement = LoadedAnimationsScreen.genderRequirement(actor.requiredTags());
            switch (requirement.ordinal()) {
                default: {
                    throw new MatchException(null, null);
                }
                case 1: {
                    boolean bl;
                    if (baseExists || maleExists) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case 2: {
                    boolean bl;
                    if (baseExists || femaleExists) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case 0: {
                    boolean bl = found = baseExists || maleExists && femaleExists;
                }
            }
            if (found) {
                return ModelCoverage.present();
            }
            String detail = switch (requirement.ordinal()) {
                default -> throw new MatchException(null, null);
                case 1 -> String.valueOf(entityTypeId) + " (need base or .m)";
                case 2 -> String.valueOf(entityTypeId) + " (need base or .f)";
                case 0 -> String.valueOf(entityTypeId) + " (need base or both .m/.f)";
            };
            return ModelCoverage.missingCoverage(detail);
        }

        private static GenderRequirement genderRequirement(Set<String> requiredTags) {
            if (requiredTags == null || requiredTags.isEmpty()) {
                return GenderRequirement.ANY;
            }
            boolean needsMale = requiredTags.contains("gender.male");
            boolean needsFemale = requiredTags.contains("gender.female");
            if (needsMale && !needsFemale) {
                return GenderRequirement.MALE_ONLY;
            }
            if (needsFemale && !needsMale) {
                return GenderRequirement.FEMALE_ONLY;
            }
            return GenderRequirement.ANY;
        }

        private static String summarizeStages(List<AnimationStageInfo> stages) {
            if (stages == null || stages.isEmpty()) {
                return "loop/cycle: -";
            }
            ArrayList<CallSite> parts = new ArrayList<CallSite>();
            int previewCount = Math.min(3, stages.size());
            for (int i = 0; i < previewCount; ++i) {
                AnimationStageInfo stage = stages.get(i);
                Integer stageNumber = NonPeakStages.stageNumberFromId(stage.animationId());
                String stageLabel = "p" + String.valueOf(stageNumber == null ? "?" : stageNumber);
                String loopLabel = stage.loop() ? "loop" : "once";
                double cycleTicks = NonLoopSecondsOverrides.getCycleTicks(stage.animationId());
                if (cycleTicks <= 0.0) {
                    cycleTicks = stage.lengthTicks();
                }
                String cycle = cycleTicks > 0.0 ? String.format(Locale.ROOT, "%.2fs", cycleTicks / 20.0) : "-";
                String speed = String.format(Locale.ROOT, "%.2fx", stage.speed());
                parts.add((CallSite)((Object)(stageLabel + " " + loopLabel + " " + cycle + " @" + speed)));
            }
            if (stages.size() > previewCount) {
                parts.add((CallSite)((Object)("+" + (stages.size() - previewCount) + " more")));
            }
            return "loop/cycle: " + String.join((CharSequence)", ", parts);
        }

        private static String summarizeEntityTypes(Set<class_2960> entityTypes) {
            if (entityTypes == null || entityTypes.isEmpty()) {
                return "any";
            }
            ArrayList<class_2960> sorted = new ArrayList<class_2960>(entityTypes);
            sorted.sort(Comparator.comparing(class_2960::toString));
            ArrayList<CallSite> names = new ArrayList<CallSite>();
            for (class_2960 id : sorted) {
                String displayName = id.toString();
                class_1299 entityType = (class_1299)class_7923.field_41177.method_63535(id);
                displayName = entityType.method_5897().getString();
                names.add((CallSite)((Object)(String.valueOf(id) + "(" + displayName + ")")));
            }
            return String.join((CharSequence)"|", names);
        }

        private static List<String> buildUniqueActorKeys(List<AfwAnimationDefinitions.ActorConstraint> actors) {
            ArrayList<String> keys = new ArrayList<String>(actors.size());
            LinkedHashMap<String, Integer> counts = new LinkedHashMap<String, Integer>();
            for (AfwAnimationDefinitions.ActorConstraint actor : actors) {
                String base = actor.derivedKey();
                int count = counts.merge(base, 1, Integer::sum);
                keys.add((String)(count == 1 ? base : base + "_" + count));
            }
            return keys;
        }

        private static enum StatusFilter {
            ALL,
            MISSING,
            PARTIAL,
            OK;

        }

        private static enum SortMode {
            ID,
            STATUS,
            MISSING,
            ENTITIES;

        }

        private static final class LoadedAnimationsList
        extends class_4265<RowEntry> {
            private final class_327 textRenderer;
            private final Consumer<String> onTogglePackExpanded;
            private final Consumer<String> onToggleRow;
            private final Consumer<LoadedAnimationRow> onCopyMissing;
            private final Consumer<String> onTogglePackEnabled;
            private final Consumer<String> onToggleAnimationEnabled;
            private final BooleanSupplier editableSupplier;

            private LoadedAnimationsList(class_310 client, int width, int height, int top, class_327 textRenderer, Consumer<String> onTogglePackExpanded, Consumer<String> onToggleRow, Consumer<LoadedAnimationRow> onCopyMissing, Consumer<String> onTogglePackEnabled, Consumer<String> onToggleAnimationEnabled, BooleanSupplier editableSupplier) {
                super(client, width, height, top, 60);
                this.textRenderer = textRenderer;
                this.onTogglePackExpanded = onTogglePackExpanded;
                this.onToggleRow = onToggleRow;
                this.onCopyMissing = onCopyMissing;
                this.onTogglePackEnabled = onTogglePackEnabled;
                this.onToggleAnimationEnabled = onToggleAnimationEnabled;
                this.editableSupplier = editableSupplier;
                this.field_22744 = false;
            }

            private void setRows(List<LoadedListRow> rows) {
                this.method_25339();
                for (LoadedListRow row : rows) {
                    this.method_73370((class_350.class_351)new RowEntry(row, this.textRenderer, this.onToggleRow, this.onCopyMissing, this.onTogglePackExpanded, this.onTogglePackEnabled, this.onToggleAnimationEnabled, this.editableSupplier), LoadedAnimationsList.rowHeight(row));
                }
            }

            private static int rowHeight(LoadedListRow row) {
                if (row instanceof LoadedPackHeaderRow) {
                    return 34;
                }
                if (!(row instanceof LoadedAnimationRow)) {
                    return 48;
                }
                LoadedAnimationRow animationRow = (LoadedAnimationRow)row;
                int infoLines = Math.max(0, animationRow.infoLines() == null ? 0 : animationRow.infoLines().size());
                int base = 13 + infoLines * 9 + 4;
                if (!animationRow.hasMissingDetails()) {
                    return Math.max(30, base);
                }
                if (!animationRow.expanded()) {
                    return base + 9;
                }
                int lineCount = animationRow.missingLines() == null ? 0 : animationRow.missingLines().size();
                int shown = Math.max(1, Math.min(10, lineCount));
                return base + shown * 9;
            }

            public int method_25322() {
                return Math.max(120, this.method_25368() - 24);
            }

            public int method_25342() {
                return 8;
            }

            protected int method_65507() {
                return this.method_25368() - 8;
            }

            private static final class RowEntry
            extends class_4265.class_4266<RowEntry> {
                private final LoadedListRow row;
                private final class_327 textRenderer;
                private final Consumer<String> onToggleRow;
                private final Consumer<LoadedAnimationRow> onCopyMissing;
                private final Consumer<String> onTogglePackExpanded;
                private final Consumer<String> onTogglePackEnabled;
                private final Consumer<String> onToggleAnimationEnabled;
                private final BooleanSupplier editableSupplier;
                private final NonModMenuScreens.FlatTextButton toggleButton;
                private final NonModMenuScreens.FlatTextButton copyMissingButton;
                private final class_4185 enabledButton;
                private final List<class_339> widgets;

                private RowEntry(LoadedListRow row, class_327 textRenderer, Consumer<String> onToggleRow, Consumer<LoadedAnimationRow> onCopyMissing, Consumer<String> onTogglePackExpanded, Consumer<String> onTogglePackEnabled, Consumer<String> onToggleAnimationEnabled, BooleanSupplier editableSupplier) {
                    LoadedAnimationRow animationRow;
                    LoadedAnimationRow animationRow2;
                    this.row = row;
                    this.textRenderer = textRenderer;
                    this.onToggleRow = onToggleRow;
                    this.onCopyMissing = onCopyMissing;
                    this.onTogglePackExpanded = onTogglePackExpanded;
                    this.onTogglePackEnabled = onTogglePackEnabled;
                    this.onToggleAnimationEnabled = onToggleAnimationEnabled;
                    this.editableSupplier = editableSupplier == null ? () -> true : editableSupplier;
                    this.toggleButton = new NonModMenuScreens.FlatTextButton(18, (class_2561)class_2561.method_43470((String)(row instanceof LoadedAnimationRow && (animationRow2 = (LoadedAnimationRow)row).expanded() ? "-" : "+")), button -> {
                        LoadedListRow patt0$temp = this.row;
                        if (patt0$temp instanceof LoadedPackHeaderRow) {
                            LoadedPackHeaderRow headerRow = (LoadedPackHeaderRow)patt0$temp;
                            if (this.onTogglePackExpanded != null) {
                                this.onTogglePackExpanded.accept(headerRow.packId());
                            }
                        } else {
                            LoadedAnimationRow animationRow;
                            LoadedListRow patt1$temp = this.row;
                            if (patt1$temp instanceof LoadedAnimationRow && (animationRow = (LoadedAnimationRow)patt1$temp).hasMissingDetails() && this.onToggleRow != null) {
                                this.onToggleRow.accept(animationRow.key());
                            }
                        }
                    });
                    this.copyMissingButton = new NonModMenuScreens.FlatTextButton(24, (class_2561)class_2561.method_43470((String)"MS"), button -> {
                        LoadedAnimationRow animationRow;
                        LoadedListRow patt0$temp = this.row;
                        if (patt0$temp instanceof LoadedAnimationRow && (animationRow = (LoadedAnimationRow)patt0$temp).hasMissingDetails() && this.onCopyMissing != null) {
                            this.onCopyMissing.accept(animationRow);
                        }
                    });
                    this.enabledButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"ON"), button -> {
                        if (!this.editableSupplier.getAsBoolean()) {
                            return;
                        }
                        LoadedListRow patt0$temp = this.row;
                        if (patt0$temp instanceof LoadedPackHeaderRow) {
                            LoadedPackHeaderRow headerRow = (LoadedPackHeaderRow)patt0$temp;
                            if (this.onTogglePackEnabled != null) {
                                this.onTogglePackEnabled.accept(headerRow.packId());
                            }
                        } else {
                            LoadedListRow patt1$temp = this.row;
                            if (patt1$temp instanceof LoadedAnimationRow) {
                                LoadedAnimationRow animationRow = (LoadedAnimationRow)patt1$temp;
                                if (this.onToggleAnimationEnabled != null) {
                                    this.onToggleAnimationEnabled.accept(animationRow.key());
                                }
                            }
                        }
                    }).method_46434(0, 0, 42, 18).method_46431();
                    ArrayList<Object> ws = new ArrayList<Object>();
                    ws.add(this.enabledButton);
                    if (row instanceof LoadedAnimationRow && (animationRow = (LoadedAnimationRow)row).hasMissingDetails()) {
                        ws.add((Object)this.copyMissingButton);
                        ws.add((Object)this.toggleButton);
                    }
                    this.widgets = List.copyOf(ws);
                }

                public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                    int x = this.method_73380() + 4;
                    int y = this.method_73382() + 3;
                    int maxWidth = Math.max(20, this.method_73387() - 70);
                    int right = this.method_73380() + this.method_73387() - 2;
                    LoadedListRow loadedListRow = this.row;
                    if (loadedListRow instanceof LoadedPackHeaderRow) {
                        LoadedPackHeaderRow headerRow = (LoadedPackHeaderRow)loadedListRow;
                        this.renderPackHeader(context, mouseX, mouseY, delta, x, y, right, headerRow);
                        return;
                    }
                    loadedListRow = this.row;
                    if (!(loadedListRow instanceof LoadedAnimationRow)) {
                        return;
                    }
                    LoadedAnimationRow animationRow = (LoadedAnimationRow)loadedListRow;
                    this.enabledButton.method_25355((class_2561)class_2561.method_43470((String)(animationRow.animationDisabled() ? "OFF" : "ON")));
                    this.enabledButton.field_22763 = this.editableSupplier.getAsBoolean();
                    this.enabledButton.method_46421(right - 44);
                    this.enabledButton.method_46419(y - 4);
                    this.enabledButton.method_25394(context, mouseX, mouseY, delta);
                    if (animationRow.hasMissingDetails()) {
                        this.copyMissingButton.method_46421(right - 86);
                        this.copyMissingButton.method_46419(y - 1);
                        this.copyMissingButton.method_25394(context, mouseX, mouseY, delta);
                    }
                    int textX = x;
                    int statusButtonLeft = animationRow.hasMissingDetails() ? right - 86 : right - 44;
                    String statusText = "[" + class_2561.method_43471((String)animationRow.status().translationKey).getString() + "]";
                    int statusWidth = this.textRenderer.method_1727(statusText);
                    int statusX = Math.max(textX, statusButtonLeft - statusWidth - 6);
                    int textWidth = Math.max(20, statusX - textX - 6);
                    int titleColor = animationRow.effectivelyDisabled() ? -8947849 : animationRow.status().color;
                    int statusColor = animationRow.effectivelyDisabled() ? -8947849 : animationRow.status().color;
                    context.method_25303(this.textRenderer, this.textRenderer.method_27523(animationRow.title(), textWidth), textX, y, titleColor);
                    context.method_25303(this.textRenderer, statusText, statusX, y, statusColor);
                    List<Object> infoLines = animationRow.infoLines() == null ? List.of() : animationRow.infoLines();
                    for (int i = 0; i < infoLines.size(); ++i) {
                        int n;
                        if (animationRow.effectivelyDisabled()) {
                            n = -8947849;
                        } else {
                            switch (i) {
                                case 0: {
                                    n = -3092272;
                                    break;
                                }
                                case 1: {
                                    n = -4671304;
                                    break;
                                }
                                default: {
                                    n = ((String)infoLines.get(i)).startsWith("block:") ? -4795766 : -7303024;
                                }
                            }
                        }
                        int color = n;
                        context.method_25303(this.textRenderer, this.textRenderer.method_27523((String)infoLines.get(i), textWidth), textX, y + 9 + i * 9, color);
                    }
                    int missingY = y + 9 + infoLines.size() * 9;
                    if (animationRow.hasMissingDetails() && animationRow.expanded()) {
                        missingTextX = x + 20;
                        int missingWidth = Math.max(20, maxWidth - 20);
                        this.toggleButton.method_25355((class_2561)class_2561.method_43470((String)"-"));
                        this.toggleButton.method_46421(x);
                        this.toggleButton.method_46419(missingY - 1);
                        this.toggleButton.method_25394(context, mouseX, mouseY, delta);
                        List<String> missingLines = animationRow.missingLines();
                        int maxLines = Math.min(10, missingLines == null ? 0 : missingLines.size());
                        for (int i = 0; i < maxLines; ++i) {
                            String line = missingLines.get(i);
                            context.method_25303(this.textRenderer, this.textRenderer.method_27523(line, missingWidth), missingTextX, missingY + i * 9, -7303024);
                        }
                    } else if (animationRow.hasMissingDetails()) {
                        missingTextX = x + 20;
                        int missingWidth = Math.max(20, maxWidth - 20);
                        this.toggleButton.method_25355((class_2561)class_2561.method_43470((String)"+"));
                        this.toggleButton.method_46421(x);
                        this.toggleButton.method_46419(missingY - 1);
                        this.toggleButton.method_25394(context, mouseX, mouseY, delta);
                        context.method_25303(this.textRenderer, this.textRenderer.method_27523(class_2561.method_43471((String)"config.needsofnature.loaded_animations.expand_hint").getString(), missingWidth), missingTextX, missingY, -7303024);
                    }
                }

                public boolean method_25402(class_11909 click, boolean doubled) {
                    LoadedListRow loadedListRow = this.row;
                    if (loadedListRow instanceof LoadedPackHeaderRow) {
                        LoadedPackHeaderRow headerRow = (LoadedPackHeaderRow)loadedListRow;
                        if (click.method_74245() == 0 && this.onTogglePackExpanded != null && !this.enabledButton.method_25405(click.comp_4798(), click.comp_4799())) {
                            this.onTogglePackExpanded.accept(headerRow.packId());
                            return true;
                        }
                    }
                    return super.method_25402(click, doubled);
                }

                private void renderPackHeader(class_332 context, int mouseX, int mouseY, float delta, int x, int y, int right, LoadedPackHeaderRow headerRow) {
                    this.enabledButton.method_25355((class_2561)class_2561.method_43470((String)(headerRow.disabled() ? "OFF" : "ON")));
                    this.enabledButton.field_22763 = this.editableSupplier.getAsBoolean();
                    this.enabledButton.method_46421(right - 44);
                    this.enabledButton.method_46419(y + 4);
                    this.enabledButton.method_25394(context, mouseX, mouseY, delta);
                    int iconSize = 24;
                    int iconX = x;
                    int iconY = y + 1;
                    context.method_25294(iconX - 1, iconY - 1, iconX + iconSize + 1, iconY + iconSize + 1, -14671840);
                    if (headerRow.iconTexture() != null) {
                        context.method_25290(class_10799.field_56883, headerRow.iconTexture(), iconX, iconY, 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize);
                    } else {
                        context.method_25294(iconX, iconY, iconX + iconSize, iconY + iconSize, -12829636);
                        context.method_25303(this.textRenderer, "AFW", iconX + 3, iconY + 8, -4671304);
                    }
                    int textX = iconX + iconSize + 7;
                    int textWidth = Math.max(20, right - textX - 42);
                    int titleColor = headerRow.disabled() ? -7829368 : -1;
                    String title = headerRow.packName() + " (" + headerRow.visibleAnimations() + "/" + headerRow.totalAnimations() + ")" + (headerRow.expanded() ? "" : " [collapsed]");
                    context.method_25303(this.textRenderer, this.textRenderer.method_27523(title, textWidth), textX, y + 2, titleColor);
                    String subtitle = "author: " + headerRow.packAuthor() + " | id: " + headerRow.packId();
                    context.method_25303(this.textRenderer, this.textRenderer.method_27523(subtitle, textWidth), textX, y + 13, -6645094);
                }

                public List<? extends class_6379> method_37025() {
                    return List.copyOf(this.widgets);
                }

                public void method_48206(Consumer<class_339> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends class_364> method_25396() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private record LoadedAnimationRow(String key, String packId, String resourcePackId, String packName, String packAuthor, String title, List<String> infoLines, List<String> missingLines, String searchBlob, ResourceStatus status, int missingCount, int entityTypeCount, boolean packDisabled, boolean animationDisabled, boolean hasMissingDetails, boolean expanded) implements LoadedListRow
        {
            boolean effectivelyDisabled() {
                return this.packDisabled || this.animationDisabled;
            }

            private boolean matches(String filter) {
                if (filter == null || filter.isBlank()) {
                    return true;
                }
                return this.searchBlob.contains(filter);
            }

            private boolean matchesStatus(StatusFilter filter) {
                return switch (filter.ordinal()) {
                    default -> throw new MatchException(null, null);
                    case 0 -> true;
                    case 1 -> {
                        if (this.status == ResourceStatus.MISSING) {
                            yield true;
                        }
                        yield false;
                    }
                    case 2 -> {
                        if (this.status == ResourceStatus.PARTIAL) {
                            yield true;
                        }
                        yield false;
                    }
                    case 3 -> this.status == ResourceStatus.OK;
                };
            }
        }

        private record ResourceCheck(int animationTotal, int animationFound, int modelTotal, int modelFound, int modelUnchecked, List<String> missingAnimationDetails, List<String> missingModelDetails) {
            private ResourceStatus status() {
                boolean anyChecked;
                boolean modelsOk;
                boolean animationsOk = this.animationTotal == 0 || this.animationFound == this.animationTotal;
                boolean bl = modelsOk = this.modelTotal == 0 || this.modelFound == this.modelTotal;
                if (animationsOk && modelsOk) {
                    return ResourceStatus.OK;
                }
                boolean anyFound = this.animationFound > 0 || this.modelFound > 0;
                boolean bl2 = anyChecked = this.animationTotal > 0 || this.modelTotal > 0;
                if (anyChecked && !anyFound) {
                    return ResourceStatus.MISSING;
                }
                return ResourceStatus.PARTIAL;
            }

            private boolean hasMissingDetails() {
                return this.missingAnimationDetails != null && !this.missingAnimationDetails.isEmpty() || this.missingModelDetails != null && !this.missingModelDetails.isEmpty();
            }

            private int totalMissingCount() {
                int anim = this.missingAnimationDetails == null ? 0 : this.missingAnimationDetails.size();
                int model = this.missingModelDetails == null ? 0 : this.missingModelDetails.size();
                return anim + model;
            }

            private List<String> missingLinesStructured() {
                if (!this.hasMissingDetails()) {
                    return List.of();
                }
                ArrayList<String> lines = new ArrayList<String>();
                lines.add("missing:");
                if (this.missingAnimationDetails != null && !this.missingAnimationDetails.isEmpty()) {
                    lines.add("anim:");
                    ResourceCheck.addMissingLines(lines, this.missingAnimationDetails, 3);
                } else {
                    lines.add("anim: none");
                }
                if (this.missingModelDetails != null && !this.missingModelDetails.isEmpty()) {
                    lines.add("model:");
                    ResourceCheck.addMissingLines(lines, this.missingModelDetails, 3);
                } else {
                    lines.add("model: none");
                }
                return List.copyOf(lines);
            }

            private static void addMissingLines(List<String> out, List<String> values, int maxEntries) {
                if (values == null || values.isEmpty()) {
                    return;
                }
                int safeMax = Math.max(1, maxEntries);
                int take = Math.min(safeMax, values.size());
                for (int i = 0; i < take; ++i) {
                    out.add(values.get(i));
                }
                int remaining = values.size() - take;
                if (remaining > 0) {
                    out.add("+" + remaining + " more");
                }
            }
        }

        private static enum ResourceStatus {
            OK("config.needsofnature.loaded_animations.status.ok", -11141291),
            PARTIAL("config.needsofnature.loaded_animations.status.partial", -8090),
            MISSING("config.needsofnature.loaded_animations.status.missing", -43691);

            private final String translationKey;
            private final int color;

            private ResourceStatus(String translationKey, int color) {
                this.translationKey = translationKey;
                this.color = color;
            }

            private int sortRank() {
                return switch (this.ordinal()) {
                    default -> throw new MatchException(null, null);
                    case 2 -> 0;
                    case 1 -> 1;
                    case 0 -> 2;
                };
            }
        }

        private record LoadedPackHeaderRow(String key, String packId, String resourcePackId, String packName, String packAuthor, int visibleAnimations, int totalAnimations, boolean disabled, boolean expanded, @Nullable class_2960 iconTexture) implements LoadedListRow
        {
        }

        private record ModelCoverage(boolean found, String missingDetail) {
            private static ModelCoverage present() {
                return new ModelCoverage(true, "");
            }

            private static ModelCoverage missingCoverage(String detail) {
                return new ModelCoverage(false, detail);
            }
        }

        private static enum GenderRequirement {
            ANY,
            MALE_ONLY,
            FEMALE_ONLY;

        }

        private static interface LoadedListRow {
            public String key();
        }
    }

    static class DebugAdvancedScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private int scanBudget;
        private NonDebugChatMode debugChatMode;
        private boolean debugSpinMode;
        private boolean convertMaleOnlyVInjectionsToA;
        private boolean attackCreativePlayers;
        private class_342 budgetField;
        private class_4185 debugModeButton;
        private class_4185 debugSpinModeButton;
        private class_4185 convertMaleOnlyVInjectionsToAButton;
        private class_4185 attackCreativePlayersButton;
        private class_4185 resetBudgetButton;
        private class_4185 resetDebugModeButton;
        private class_4185 resetDebugSpinModeButton;
        private class_4185 resetConvertMaleOnlyVInjectionsToAButton;
        private class_4185 resetAttackCreativePlayersButton;

        protected DebugAdvancedScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.debug_advanced_title"));
            this.parent = parent;
            this.config = config;
            this.scanBudget = config.getScanBudgetPerTick();
            this.debugChatMode = config.debugChatMode();
            this.debugSpinMode = config.isDebugSpinMode();
            this.convertMaleOnlyVInjectionsToA = config.convertMaleOnlyVInjectionsToA();
            this.attackCreativePlayers = NonHudOverlay.getRuntimeAttackCreativePlayers();
        }

        protected void method_25426() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.budgetField != null) {
                this.scanBudget = this.parseField(this.budgetField, this.scanBudget);
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.runtime_budget")));
            this.budgetField = this.newNumberField(fieldWidth, this.scanBudget);
            this.resetBudgetButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> this.budgetField.method_1852(String.valueOf(this.defaults.getScanBudgetPerTick()))).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetBudgetButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.scan_budget"), (class_339)this.budgetField, (class_339)this.resetBudgetButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.scan_budget")));
            this.budgetField.method_1888(this.serverConfigEditable);
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.debug_feedback")));
            this.debugModeButton = class_4185.method_46430((class_2561)this.debugChatLabel(this.debugChatMode), button -> {
                this.debugChatMode = this.nextDebugChatMode(this.debugChatMode);
                button.method_25355(this.debugChatLabel(this.debugChatMode));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetDebugModeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.debugChatMode = this.defaults.debugChatMode();
                this.debugModeButton.method_25355(this.debugChatLabel(this.debugChatMode));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDebugModeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.debug_chat_mode"), (class_339)this.debugModeButton, (class_339)this.resetDebugModeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.debug_chat_mode")));
            this.debugSpinModeButton = class_4185.method_46430((class_2561)this.debugSpinModeLabel(this.debugSpinMode), button -> {
                this.debugSpinMode = !this.debugSpinMode;
                button.method_25355(this.debugSpinModeLabel(this.debugSpinMode));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetDebugSpinModeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.debugSpinMode = this.defaults.isDebugSpinMode();
                this.debugSpinModeButton.method_25355(this.debugSpinModeLabel(this.debugSpinMode));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetDebugSpinModeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.debug_spin_mode"), (class_339)this.debugSpinModeButton, (class_339)this.resetDebugSpinModeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.debug_spin_mode")));
            this.convertMaleOnlyVInjectionsToAButton = class_4185.method_46430((class_2561)this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA), button -> {
                this.convertMaleOnlyVInjectionsToA = !this.convertMaleOnlyVInjectionsToA;
                button.method_25355(this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetConvertMaleOnlyVInjectionsToAButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.convertMaleOnlyVInjectionsToA = this.defaults.convertMaleOnlyVInjectionsToA();
                this.convertMaleOnlyVInjectionsToAButton.method_25355(this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetConvertMaleOnlyVInjectionsToAButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.convert_male_v_to_a"), (class_339)this.convertMaleOnlyVInjectionsToAButton, (class_339)this.resetConvertMaleOnlyVInjectionsToAButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.convert_male_v_to_a")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.attack_rules")));
            this.attackCreativePlayersButton = class_4185.method_46430((class_2561)this.onOffLabel(this.attackCreativePlayers), button -> {
                this.attackCreativePlayers = !this.attackCreativePlayers;
                button.method_25355(this.onOffLabel(this.attackCreativePlayers));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            this.resetAttackCreativePlayersButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.attackCreativePlayers = this.defaults.canAttackCreativePlayers();
                this.attackCreativePlayersButton.method_25355(this.onOffLabel(this.attackCreativePlayers));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetAttackCreativePlayersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.attack_creative_players"), (class_339)this.attackCreativePlayersButton, (class_339)this.resetAttackCreativePlayersButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_creative_players")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.animation_data")));
            class_4185 loadedAnimationsButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations"), button -> class_310.method_1551().method_1507((class_437)new LoadedAnimationsScreen(this))).method_46434(0, 0, 220, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)loadedAnimationsButton, "config.needsofnature.tooltip.loaded_animations");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)loadedAnimationsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loaded_animations")));
            class_4185 loadOrderButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.loaded_animations.load_order"), button -> class_310.method_1551().method_1507((class_437)new LoadOrderScreen(this, this.config))).method_46434(0, 0, 220, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)loadOrderButton, "config.needsofnature.tooltip.loaded_animations.load_order");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)loadOrderButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loaded_animations.load_order")));
            this.budgetField.method_1863(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> this.saveAndClose()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        public void method_25419() {
            this.saveAndClose();
        }

        private void saveAndClose() {
            if (this.serverConfigEditable) {
                this.scanBudget = this.clampBudget(this.parseField(this.budgetField, this.scanBudget));
                this.config.setScanBudgetPerTick(this.scanBudget);
            }
            this.config.setDebugChatMode(this.debugChatMode);
            this.config.setDebugSpinMode(this.debugSpinMode);
            this.config.setConvertMaleOnlyVInjectionsToA(this.convertMaleOnlyVInjectionsToA);
            if (this.serverConfigEditable) {
                this.config.setAttackCreativePlayers(this.attackCreativePlayers);
            }
            this.config.save();
            if (this.serverConfigEditable) {
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
            }
            class_310.method_1551().method_1507(this.parent);
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            field.method_1852(String.valueOf(initial));
            field.method_1880(8);
            field.method_1888(true);
            return field;
        }

        private int parseField(class_342 field, int fallback) {
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int clampBudget(int v) {
            return Math.max(1, Math.min(512, v));
        }

        private class_2561 debugChatLabel(NonDebugChatMode mode) {
            NonDebugChatMode resolved = mode == null ? NonDebugChatMode.SETUP_ERRORS : mode;
            return class_2561.method_43471((String)("config.needsofnature.debug_chat_mode." + resolved.id()));
        }

        private NonDebugChatMode nextDebugChatMode(NonDebugChatMode current) {
            if (current == null) {
                return NonDebugChatMode.SETUP_ERRORS;
            }
            return switch (current) {
                default -> throw new MatchException(null, null);
                case NonDebugChatMode.ALL -> NonDebugChatMode.SETUP_WARNINGS_ERRORS;
                case NonDebugChatMode.SETUP_WARNINGS_ERRORS -> NonDebugChatMode.SETUP_ERRORS;
                case NonDebugChatMode.SETUP_ERRORS -> NonDebugChatMode.ERRORS_ONLY;
                case NonDebugChatMode.ERRORS_ONLY -> NonDebugChatMode.ALL;
            };
        }

        private class_2561 debugSpinModeLabel(boolean enabled) {
            return class_2561.method_43469((String)"config.needsofnature.debug_spin_mode.value", (Object[])new Object[]{class_2561.method_43471((String)(enabled ? "options.on" : "options.off"))});
        }

        private class_2561 debugBooleanLabel(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private class_2561 onOffLabel(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private void updateResetButtons() {
            if (this.resetBudgetButton != null) {
                boolean bl = this.resetBudgetButton.field_22763 = this.serverConfigEditable && this.parseField(this.budgetField, this.defaults.getScanBudgetPerTick()) != this.defaults.getScanBudgetPerTick();
            }
            if (this.resetDebugModeButton != null) {
                boolean bl = this.resetDebugModeButton.field_22763 = this.debugChatMode != this.defaults.debugChatMode();
            }
            if (this.resetDebugSpinModeButton != null) {
                boolean bl = this.resetDebugSpinModeButton.field_22763 = this.debugSpinMode != this.defaults.isDebugSpinMode();
            }
            if (this.resetConvertMaleOnlyVInjectionsToAButton != null) {
                boolean bl = this.resetConvertMaleOnlyVInjectionsToAButton.field_22763 = this.convertMaleOnlyVInjectionsToA != this.defaults.convertMaleOnlyVInjectionsToA();
            }
            if (this.resetAttackCreativePlayersButton != null) {
                boolean bl = this.resetAttackCreativePlayersButton.field_22763 = this.serverConfigEditable && this.attackCreativePlayers != this.defaults.canAttackCreativePlayers();
            }
            if (this.attackCreativePlayersButton != null) {
                this.attackCreativePlayersButton.field_22763 = this.serverConfigEditable;
            }
        }
    }
}

