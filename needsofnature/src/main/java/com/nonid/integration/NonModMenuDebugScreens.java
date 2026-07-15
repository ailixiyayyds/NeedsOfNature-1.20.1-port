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
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.texture.NativeImageBackedTexture
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.client.gl.RenderPipelines
 *  net.minecraft.server.integrated.IntegratedServer
 *  net.minecraft.client.gui.Click
 *  net.minecraft.entity.EntityType
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.ResourcePack
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.TextFieldWidget
 *  net.minecraft.client.gui.widget.EntryListWidget$Entry
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.widget.ElementListWidget
 *  net.minecraft.client.gui.widget.ElementListWidget$Entry
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.StringVisitable
 *  net.minecraft.client.gui.Selectable
 *  net.minecraft.resource.InputSupplier
 *  net.minecraft.registry.Registries
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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.StringVisitable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.resource.InputSupplier;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

final class NonModMenuDebugScreens {
    private NonModMenuDebugScreens() {
    }

    static class LiquidGainConfigScreen
    extends Screen {
        private static final String MIXED_COLOR_ROW_ID = "needsofnature:mixed";
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private Map<String, Integer> appliedMap;
        private Map<String, String> appliedColorMap;
        private GainList gainList;
        private final List<GainList.GainEntry> rows = new ArrayList<GainList.GainEntry>();
        private ButtonWidget applyButton;
        private ButtonWidget doneButton;

        protected LiquidGainConfigScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.liquid_gain_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, Integer>(NonLiquidGainOverrides.getGainByEntity());
            this.appliedMap.putAll(config.getLiquidGainByEntity());
            this.appliedColorMap = new LinkedHashMap<String, String>(NonLiquidGainOverrides.getColorByEntity());
            this.appliedColorMap.putAll(config.getLiquidColorByEntity());
            this.appliedMap.remove(MIXED_COLOR_ROW_ID);
        }

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            this.gainList = new GainList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild(this.gainList);
            this.rows.clear();
            for (RowData data : preserved) {
                this.addRow(data.id(), data.value(), data.colorHex());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "0", "");
            }
            int centerX = this.width / 2;
            ButtonWidget addButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.liquid_gain_add"), button -> {
                this.addRow("", "0", "");
                this.updateApplyButton();
            }).dimensions(centerX - 100, this.height - 52, 96, 20).build();
            addButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)addButton, "config.needsofnature.tooltip.liquid_gain_add");
            this.addDrawableChild(addButton);
            ButtonWidget resetDefaultsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.liquid_gain_reset"), button -> MinecraftClient.getInstance().setScreen((Screen)new ResetLiquidGainConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                MinecraftClient.getInstance().setScreen((Screen)this);
            }))).dimensions(centerX + 4, this.height - 52, 96, 20).build();
            resetDefaultsButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)resetDefaultsButton, "config.needsofnature.tooltip.liquid_gain_reset");
            this.addDrawableChild(resetDefaultsButton);
            this.applyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.apply"), button -> {
                IntegratedServer server;
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
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null && (server = client.getServer()) != null) {
                    server.execute(() -> {
                        NeedsOfNature.syncLiquidStateToAll((MinecraftServer)server);
                        NeedsOfNature.refreshLiquidBottleTintsToAll((MinecraftServer)server);
                    });
                }
                this.updateApplyButton();
            }).dimensions(centerX - 100, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.applyButton, "config.needsofnature.tooltip.apply");
            this.addDrawableChild(this.applyButton);
            this.doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild(this.doneButton);
            this.updateApplyButton();
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
            this.drawLiquidGainHeader(context);
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void drawLiquidGainHeader(DrawContext context) {
            if (this.gainList == null) {
                return;
            }
            int rowX = this.gainList.getRowLeft();
            int rowWidth = this.gainList.getRowWidth();
            int removeW = 20;
            int valueW = 50;
            int colorW = 80;
            int idW = Math.max(120, rowWidth - removeW - valueW - colorW - 18);
            int valueX = rowX + idW + 6;
            int colorX = valueX + valueW + 6;
            int y = 34;
            int color = -4342339;
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_gain_column.entity"), rowX + 4, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_gain_column.ml"), valueX + valueW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_gain_column.color"), colorX + colorW / 2, y, color);
        }

        private void addRow(String idText, String valueText, String colorHexText) {
            GainList.GainEntry[] holder = new GainList.GainEntry[1];
            GainList.GainEntry entry = new GainList.GainEntry(this.textRenderer, idText, valueText, colorHexText, () -> {
                GainList.GainEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.gainList.removeEntryRow(current);
                this.updateApplyButton();
            });
            holder[0] = entry;
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
                this.applyButton.active = false;
                if (this.doneButton != null) {
                    this.doneButton.setMessage((Text)Text.translatable((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, Integer> currentGain = this.collectGainMapFromFields();
            Map<String, String> currentColor = this.collectColorMapFromFields();
            this.applyButton.active = hasUnsavedChanges = !currentGain.equals(this.appliedMap) || !currentColor.equals(this.appliedColorMap);
            if (this.doneButton != null) {
                this.doneButton.setMessage((Text)Text.translatable((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
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
            LinkedHashSet<String> ids = new LinkedHashSet<String>();
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
        extends ElementListWidget<GainList.GainEntry> {
            private GainList(MinecraftClient client, int width, int height, int top) {
                super(client, width, height, top, height, 24);
                this.centerListVertically = false;
            }

            private void addEntryRow(GainEntry entry) {
                super.addEntry(entry);
            }

            private void removeEntryRow(GainEntry entry) {
                super.removeEntry(entry);
            }

            public int getRowWidth() {
                return 360;
            }

            public int getRowLeft() {
                return (this.width - this.getRowWidth()) / 2;
            }

            private static final class GainEntry
            extends ElementListWidget.Entry<GainEntry> {
                private final TextFieldWidget idField;
                private final TextFieldWidget valueField;
                private final TextFieldWidget colorHexField;
                private final ButtonWidget removeButton;
                private final List<ClickableWidget> widgets;
                private Runnable onChange;

                private GainEntry(TextRenderer textRenderer, String idText, String valueText, String colorHexText, Runnable onRemove) {
                    this.idField = new TextFieldWidget(textRenderer, 0, 0, 160, 20, (Text)Text.empty());
                    this.idField.setText(idText == null ? "" : idText);
                    this.idField.setMaxLength(80);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.idField, "config.needsofnature.tooltip.liquid_gain_entry_entity");
                    this.valueField = new TextFieldWidget(textRenderer, 0, 0, 50, 20, (Text)Text.empty());
                    this.valueField.setText(valueText == null ? "0" : valueText);
                    this.valueField.setMaxLength(6);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.valueField, "config.needsofnature.tooltip.liquid_gain_entry_ml");
                    this.colorHexField = new TextFieldWidget(textRenderer, 0, 0, 80, 20, (Text)Text.empty());
                    this.colorHexField.setText(colorHexText == null ? "" : colorHexText);
                    this.colorHexField.setPlaceholder((Text)Text.literal((String)"#RRGGBB"));
                    this.colorHexField.setMaxLength(7);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.colorHexField, "config.needsofnature.tooltip.liquid_gain_entry_color");
                    this.removeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).dimensions(0, 0, 20, 20).build();
                    NonModMenuScreens.setTooltip((ClickableWidget)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<ClickableWidget>(4);
                    this.widgets.add((ClickableWidget)this.idField);
                    this.widgets.add((ClickableWidget)this.valueField);
                    this.widgets.add((ClickableWidget)this.colorHexField);
                    this.widgets.add((ClickableWidget)this.removeButton);
                    this.idField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.valueField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.colorHexField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                }

                private void setOnChange(Runnable onChange) {
                    this.onChange = onChange;
                }

                private void setEditable(boolean editable) {
                    this.idField.setEditable(editable);
                    this.valueField.setEditable(editable);
                    this.colorHexField.setEditable(editable);
                    this.removeButton.active = editable;
                }

                private void setMixedRowMode(boolean editable, String mixedRowId) {
                    this.idField.setText(mixedRowId);
                    this.idField.setEditable(false);
                    this.valueField.setText("0");
                    this.valueField.setEditable(false);
                    this.colorHexField.setEditable(editable);
                    this.removeButton.active = false;
                }

                private String getIdText() {
                    return this.idField.getText();
                }

                private String getValueText() {
                    return this.valueField.getText();
                }

                private String getColorHexText() {
                    return this.colorHexField.getText();
                }

                public void render(DrawContext context, int index, int rowY, int rowX, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.getWidth();
                    int valueW = this.valueField.getWidth();
                    int colorW = this.colorHexField.getWidth();
                    int idW = Math.max(120, rowWidth - removeW - valueW - colorW - 18);
                    int valueX = rowX + idW + 6;
                    int colorX = valueX + valueW + 6;
                    int removeX = colorX + colorW + 6;
                    this.idField.setX(rowX);
                    this.idField.setY(widgetY);
                    this.idField.setWidth(idW);
                    this.valueField.setX(valueX);
                    this.valueField.setY(widgetY);
                    this.colorHexField.setX(colorX);
                    this.colorHexField.setY(widgetY);
                    this.removeButton.setX(removeX);
                    this.removeButton.setY(widgetY);
                    for (ClickableWidget widget : this.widgets) {
                        widget.render(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends Selectable> selectableChildren() {
                    return List.copyOf(this.widgets);
                }

                public void forEachChild(Consumer<ClickableWidget> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends Element> children() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private static final class ResetLiquidGainConfirmScreen
        extends Screen {
            private final Screen parent;
            private final Runnable onConfirm;

            private ResetLiquidGainConfirmScreen(Screen parent, Runnable onConfirm) {
                super((Text)Text.translatable((String)"config.needsofnature.liquid_gain_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void init() {
                int centerX = this.width / 2;
                int buttonY = this.height / 2 + 20;
                ButtonWidget yesButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    MinecraftClient.getInstance().setScreen(this.parent);
                }).dimensions(centerX - 100, buttonY, 96, 20).build();
                this.addDrawableChild(yesButton);
                ButtonWidget noButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, buttonY, 96, 20).build();
                this.addDrawableChild(noButton);
            }

            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 20, -1);
                context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.liquid_gain_reset_body"), this.width / 2, this.height / 2 - 4, -4144960);
            }
        }
    }

    static class OffspringCountConfigScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private boolean serverConfigEditable;
        private Map<String, NonConfig.EntityProfile> appliedMap;
        private OffspringCountList offspringList;
        private final List<OffspringCountList.OffspringEntry> rows = new ArrayList<OffspringCountList.OffspringEntry>();
        private ButtonWidget applyButton;
        private ButtonWidget doneButton;

        protected OffspringCountConfigScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.offspring_count_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, NonConfig.EntityProfile>(config.getEntityProfilesByEntity());
        }

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            List<RowData> preserved = this.snapshotRows();
            if (preserved == null) {
                preserved = new ArrayList<RowData>();
                for (Map.Entry<String, NonConfig.EntityProfile> entry : NonEntityProfiles.resolveEffectiveProfilesMap().entrySet()) {
                    NonConfig.EntityProfile profile = entry.getValue() == null ? NonEntityProfiles.resolve(Identifier.tryParse((String)entry.getKey())).toConfigProfile() : entry.getValue().sanitized();
                    NonConfig.EntityProfile configOverride = this.config.getEntityProfilesByEntity().get(entry.getKey());
                    preserved.add(new RowData(entry.getKey(), configOverride == null || configOverride.pregnancyChancePercent() == null ? "" : String.valueOf(configOverride.pregnancyChancePercent()), String.valueOf(profile.offspringMin() == null ? 1 : profile.offspringMin()), String.valueOf(profile.offspringMax() == null ? 1 : profile.offspringMax()), profile.birthEntity() == null ? entry.getKey() : profile.birthEntity(), profile.birthMode() == null ? "direct" : profile.birthMode(), profile.egg() == null ? NonConfig.EggProfile.defaults() : profile.egg()));
                }
            }
            int listTop = 44;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            this.offspringList = new OffspringCountList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild(this.offspringList);
            this.rows.clear();
            for (RowData data : preserved) {
                this.addRow(data.id(), data.chance(), data.min(), data.max(), data.birthEntity(), data.birthMode(), data.eggProfile());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "", "1", "1", "", "direct", NonConfig.EggProfile.defaults());
            }
            int centerX = this.width / 2;
            ButtonWidget addButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.offspring_count_add"), button -> {
                this.addRow("", "", "1", "1", "", "direct", NonConfig.EggProfile.defaults());
                this.updateApplyButton();
            }).dimensions(centerX - 100, this.height - 52, 96, 20).build();
            addButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)addButton, "config.needsofnature.tooltip.offspring_count_add");
            this.addDrawableChild(addButton);
            ButtonWidget resetDefaultsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.offspring_count_reset"), button -> MinecraftClient.getInstance().setScreen((Screen)new ResetOffspringCountConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                MinecraftClient.getInstance().setScreen((Screen)this);
            }))).dimensions(centerX + 4, this.height - 52, 96, 20).build();
            resetDefaultsButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)resetDefaultsButton, "config.needsofnature.tooltip.offspring_count_reset");
            this.addDrawableChild(resetDefaultsButton);
            this.applyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.apply"), button -> {
                if (!this.serverConfigEditable) {
                    return;
                }
                Map<String, NonConfig.EntityProfile> next = this.collectMapFromFields();
                this.config.setEntityProfilesByEntity(next);
                this.config.save();
                this.appliedMap = new LinkedHashMap<String, NonConfig.EntityProfile>(this.config.getEntityProfilesByEntity());
                NonModMenuScreens.syncHostConfigIfIntegratedServer();
                this.updateApplyButton();
            }).dimensions(centerX - 100, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.applyButton, "config.needsofnature.tooltip.apply");
            this.addDrawableChild(this.applyButton);
            this.doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild(this.doneButton);
            this.updateApplyButton();
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
            this.drawHeader(context);
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void drawHeader(DrawContext context) {
            if (this.offspringList == null) {
                return;
            }
            int rowX = this.offspringList.getRowLeft();
            int rowWidth = this.offspringList.getRowWidth();
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
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.offspring_count_column.entity"), rowX + 4, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.entity_profile_column.chance"), chanceX + chanceW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.offspring_count_column.min"), minX + minW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.offspring_count_column.max"), maxX + maxW / 2, y, color);
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.entity_profile_column.birth_entity"), birthX + 4, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.entity_profile_column.birth_mode"), modeX + modeW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.entity_profile_column.egg"), eggX + eggW / 2, y, color);
        }

        private void addRow(String idText, String chanceText, String minText, String maxText, String birthEntityText, String birthModeText, NonConfig.EggProfile eggProfile) {
            OffspringCountList.OffspringEntry[] holder = new OffspringCountList.OffspringEntry[1];
            OffspringCountList.OffspringEntry entry = new OffspringCountList.OffspringEntry(this.textRenderer, idText, chanceText, minText, maxText, birthEntityText, birthModeText, eggProfile, () -> {
                OffspringCountList.OffspringEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.offspringList.removeEntryRow(current);
                this.updateApplyButton();
            }, this::chanceSuggestionFor, this::openEggSettings);
            holder[0] = entry;
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
                this.applyButton.active = false;
                if (this.doneButton != null) {
                    this.doneButton.setMessage((Text)Text.translatable((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, NonConfig.EntityProfile> current = this.collectMapFromFields();
            this.applyButton.active = hasUnsavedChanges = !current.equals(this.appliedMap);
            if (this.doneButton != null) {
                this.doneButton.setMessage((Text)Text.translatable((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
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
                Identifier parsedId;
                String id;
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || (parsedId = Identifier.tryParse((String)id)) == null) continue;
                String rawChance = row.getChanceText() == null ? "" : row.getChanceText().trim();
                int min = this.parseInt(row.getMinText(), 1);
                int max = this.parseInt(row.getMaxText(), min);
                if (max < min) {
                    max = min;
                }
                if (Identifier.tryParse((String)(birthEntity = (birthEntity = row.getBirthEntityText()) == null || birthEntity.trim().isEmpty() ? id : birthEntity.trim())) == null) {
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
            Identifier id = idText == null || idText.isBlank() ? null : Identifier.tryParse((String)idText.trim());
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
            Identifier id = Identifier.tryParse((String)(row.getIdText() == null ? "" : row.getIdText().trim()));
            NonConfig.EggProfile defaultEgg = id == null ? NonConfig.EggProfile.defaults() : NonEntityProfiles.resolveDefaultProfile(id).eggProfile();
            MinecraftClient.getInstance().setScreen((Screen)new EggProfileSettingsScreen(this, row.getEggProfile(), defaultEgg, updated -> {
                row.setEggProfile((NonConfig.EggProfile)updated);
                this.updateApplyButton();
            }, this.serverConfigEditable));
        }

        private record RowData(String id, String chance, String min, String max, String birthEntity, String birthMode, NonConfig.EggProfile eggProfile) {
        }

        private static final class OffspringCountList
        extends ElementListWidget<OffspringCountList.OffspringEntry> {
            private OffspringCountList(MinecraftClient client, int width, int height, int top) {
                super(client, width, height, top, height, 24);
                this.centerListVertically = false;
            }

            private void addEntryRow(OffspringEntry entry) {
                super.addEntry(entry);
            }

            private void removeEntryRow(OffspringEntry entry) {
                super.removeEntry(entry);
            }

            public int getRowWidth() {
                return 620;
            }

            public int getRowLeft() {
                return (this.width - this.getRowWidth()) / 2;
            }

            private static final class OffspringEntry
            extends ElementListWidget.Entry<OffspringEntry> {
                private final TextFieldWidget idField;
                private final TextFieldWidget chanceField;
                private final TextFieldWidget minField;
                private final TextFieldWidget maxField;
                private final TextFieldWidget birthEntityField;
                private final TextFieldWidget birthModeField;
                private final ButtonWidget eggButton;
                private final ButtonWidget removeButton;
                private final List<ClickableWidget> widgets;
                private final Function<String, String> chanceSuggestionSupplier;
                private NonConfig.EggProfile eggProfile;
                private boolean editable;
                private Runnable onChange;

                private OffspringEntry(TextRenderer textRenderer, String idText, String chanceText, String minText, String maxText, String birthEntityText, String birthModeText, NonConfig.EggProfile eggProfile, Runnable onRemove, Function<String, String> chanceSuggestionSupplier, Consumer<OffspringEntry> onEggSettings) {
                    this.chanceSuggestionSupplier = chanceSuggestionSupplier;
                    this.eggProfile = eggProfile == null ? NonConfig.EggProfile.defaults() : eggProfile.sanitized();
                    this.idField = new TextFieldWidget(textRenderer, 0, 0, 120, 20, (Text)Text.empty());
                    this.idField.setText(idText == null ? "" : idText);
                    this.idField.setMaxLength(80);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.idField, "config.needsofnature.tooltip.offspring_count_entry_entity");
                    this.chanceField = new TextFieldWidget(textRenderer, 0, 0, 44, 20, (Text)Text.empty());
                    this.chanceField.setText(chanceText == null ? "" : chanceText);
                    this.chanceField.setMaxLength(3);
                    this.updateChanceSuggestion();
                    NonModMenuScreens.setTooltip((ClickableWidget)this.chanceField, "config.needsofnature.tooltip.entity_profile_entry_chance");
                    this.minField = new TextFieldWidget(textRenderer, 0, 0, 34, 20, (Text)Text.empty());
                    this.minField.setText(minText == null ? "1" : minText);
                    this.minField.setMaxLength(3);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.minField, "config.needsofnature.tooltip.offspring_count_entry_min");
                    this.maxField = new TextFieldWidget(textRenderer, 0, 0, 34, 20, (Text)Text.empty());
                    this.maxField.setText(maxText == null ? "1" : maxText);
                    this.maxField.setMaxLength(3);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.maxField, "config.needsofnature.tooltip.offspring_count_entry_max");
                    this.birthEntityField = new TextFieldWidget(textRenderer, 0, 0, 140, 20, (Text)Text.empty());
                    this.birthEntityField.setText(birthEntityText == null ? "" : birthEntityText);
                    this.birthEntityField.setMaxLength(80);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.birthEntityField, "config.needsofnature.tooltip.entity_profile_entry_birth_entity");
                    this.birthModeField = new TextFieldWidget(textRenderer, 0, 0, 54, 20, (Text)Text.empty());
                    this.birthModeField.setText(birthModeText == null ? "direct" : birthModeText);
                    this.birthModeField.setMaxLength(6);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.birthModeField, "config.needsofnature.tooltip.entity_profile_entry_birth_mode");
                    this.eggButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.entity_profile_egg_button"), button -> {
                        if (onEggSettings != null) {
                            onEggSettings.accept(this);
                        }
                    }).dimensions(0, 0, 42, 20).build();
                    NonModMenuScreens.setTooltip((ClickableWidget)this.eggButton, "config.needsofnature.tooltip.entity_profile_entry_egg");
                    this.removeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).dimensions(0, 0, 20, 20).build();
                    NonModMenuScreens.setTooltip((ClickableWidget)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<ClickableWidget>(8);
                    this.widgets.add((ClickableWidget)this.idField);
                    this.widgets.add((ClickableWidget)this.chanceField);
                    this.widgets.add((ClickableWidget)this.minField);
                    this.widgets.add((ClickableWidget)this.maxField);
                    this.widgets.add((ClickableWidget)this.birthEntityField);
                    this.widgets.add((ClickableWidget)this.birthModeField);
                    this.widgets.add((ClickableWidget)this.eggButton);
                    this.widgets.add((ClickableWidget)this.removeButton);
                    this.idField.setChangedListener(ignored -> {
                        this.updateChanceSuggestion();
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.chanceField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.minField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.maxField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.birthEntityField.setChangedListener(ignored -> {
                        if (this.onChange != null) {
                            this.onChange.run();
                        }
                    });
                    this.birthModeField.setChangedListener(ignored -> {
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
                    String suggestion = this.chanceSuggestionSupplier == null ? null : this.chanceSuggestionSupplier.apply(this.idField.getText());
                    this.chanceField.setPlaceholder(suggestion == null || suggestion.isBlank() ? null : Text.literal((String)suggestion));
                }

                private void setEditable(boolean editable) {
                    this.editable = editable;
                    this.idField.setEditable(editable);
                    this.chanceField.setEditable(editable);
                    this.minField.setEditable(editable);
                    this.maxField.setEditable(editable);
                    this.birthEntityField.setEditable(editable);
                    this.birthModeField.setEditable(editable);
                    this.updateEggButtonActive();
                    this.removeButton.active = editable;
                }

                private void updateEggButtonActive() {
                    this.eggButton.active = this.editable && "egg".equals(NonConfig.normalizeBirthMode(this.birthModeField.getText()));
                }

                private String getIdText() {
                    return this.idField.getText();
                }

                private String getMinText() {
                    return this.minField.getText();
                }

                private String getMaxText() {
                    return this.maxField.getText();
                }

                private String getChanceText() {
                    return this.chanceField.getText();
                }

                private String getBirthEntityText() {
                    return this.birthEntityField.getText();
                }

                private String getBirthModeText() {
                    return this.birthModeField.getText();
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

                public void render(DrawContext context, int index, int rowY, int rowX, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.getWidth();
                    int chanceW = this.chanceField.getWidth();
                    int minW = this.minField.getWidth();
                    int maxW = this.maxField.getWidth();
                    int birthW = this.birthEntityField.getWidth();
                    int modeW = this.birthModeField.getWidth();
                    int eggW = this.eggButton.getWidth();
                    int idW = Math.max(105, rowWidth - removeW - chanceW - minW - maxW - birthW - modeW - eggW - 48);
                    int chanceX = rowX + idW + 6;
                    int minX = chanceX + chanceW + 6;
                    int maxX = minX + minW + 6;
                    int birthX = maxX + maxW + 6;
                    int modeX = birthX + birthW + 6;
                    int eggX = modeX + modeW + 6;
                    int removeX = eggX + eggW + 6;
                    this.idField.setX(rowX);
                    this.idField.setY(widgetY);
                    this.idField.setWidth(idW);
                    this.chanceField.setX(chanceX);
                    this.chanceField.setY(widgetY);
                    this.minField.setX(minX);
                    this.minField.setY(widgetY);
                    this.maxField.setX(maxX);
                    this.maxField.setY(widgetY);
                    this.birthEntityField.setX(birthX);
                    this.birthEntityField.setY(widgetY);
                    this.birthModeField.setX(modeX);
                    this.birthModeField.setY(widgetY);
                    this.eggButton.setX(eggX);
                    this.eggButton.setY(widgetY);
                    this.removeButton.setX(removeX);
                    this.removeButton.setY(widgetY);
                    for (ClickableWidget widget : this.widgets) {
                        widget.render(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends Selectable> selectableChildren() {
                    return List.copyOf(this.widgets);
                }

                public void forEachChild(Consumer<ClickableWidget> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends Element> children() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private static final class EggProfileSettingsScreen
        extends Screen {
            private final Screen parent;
            private final NonConfig.EggProfile initialProfile;
            private final NonConfig.EggProfile defaultProfile;
            private final Consumer<NonConfig.EggProfile> onApply;
            private final boolean editable;
            private TextFieldWidget startSizeField;
            private TextFieldWidget endSizeField;
            private TextFieldWidget textureField;
            private TextFieldWidget healthField;

            private EggProfileSettingsScreen(Screen parent, NonConfig.EggProfile initialProfile, NonConfig.EggProfile defaultProfile, Consumer<NonConfig.EggProfile> onApply, boolean editable) {
                super((Text)Text.translatable((String)"config.needsofnature.egg_profile_title"));
                this.parent = parent;
                this.initialProfile = initialProfile == null ? NonConfig.EggProfile.defaults() : initialProfile.sanitized();
                this.defaultProfile = defaultProfile == null ? NonConfig.EggProfile.defaults() : defaultProfile.sanitized();
                this.onApply = onApply;
                this.editable = editable;
            }

            protected void init() {
                int centerX = this.width / 2;
                int labelX = centerX - 180;
                int fieldX = centerX - 40;
                int y = 58;
                int textureFieldW = Math.min(460, Math.max(220, this.width - fieldX - 20));
                this.startSizeField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.startSize(), this.defaultProfile.startSize(), 0.5f)));
                this.endSizeField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.endSize(), this.defaultProfile.endSize(), 1.0f)));
                this.textureField = this.newTextField(textureFieldW, EggProfileSettingsScreen.valueOrDefault(this.initialProfile.texture(), this.defaultProfile.texture(), ""));
                this.healthField = this.newTextField(70, EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.initialProfile.health(), this.defaultProfile.health(), 2.0f)));
                this.addField(this.startSizeField, fieldX, y, "config.needsofnature.tooltip.egg_profile_start_size");
                this.addField(this.endSizeField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_end_size");
                this.addField(this.textureField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_texture");
                this.addField(this.healthField, fieldX, y += 28, "config.needsofnature.tooltip.egg_profile_health");
                ButtonWidget resetButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset"), button -> {
                    this.startSizeField.setText(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.startSize(), null, 0.5f)));
                    this.endSizeField.setText(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.endSize(), null, 1.0f)));
                    this.textureField.setText(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.texture(), null, ""));
                    this.healthField.setText(EggProfileSettingsScreen.formatFloat(EggProfileSettingsScreen.valueOrDefault(this.defaultProfile.health(), null, 2.0f)));
                }).dimensions(centerX - 154, this.height - 52, 96, 20).build();
                resetButton.active = this.editable;
                NonModMenuScreens.setTooltip((ClickableWidget)resetButton, "config.needsofnature.tooltip.egg_profile_reset");
                this.addDrawableChild(resetButton);
                ButtonWidget applyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.apply"), button -> {
                    if (!this.editable) {
                        return;
                    }
                    if (this.onApply != null) {
                        this.onApply.accept(this.collectProfile());
                    }
                    MinecraftClient.getInstance().setScreen(this.parent);
                }).dimensions(centerX - 50, this.height - 52, 96, 20).build();
                applyButton.active = this.editable;
                NonModMenuScreens.setTooltip((ClickableWidget)applyButton, "config.needsofnature.tooltip.apply");
                this.addDrawableChild(applyButton);
                ButtonWidget cancelButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 54, this.height - 52, 96, 20).build();
                this.addDrawableChild(cancelButton);
                this.startSizeField.setEditable(this.editable);
                this.endSizeField.setEditable(this.editable);
                this.textureField.setEditable(this.editable);
                this.healthField.setEditable(this.editable);
            }

            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
                int centerX = this.width / 2;
                int labelX = centerX - 180;
                int y = 63;
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, 24, -1);
                this.drawLabel(context, "config.needsofnature.egg_profile_start_size", labelX, y);
                this.drawLabel(context, "config.needsofnature.egg_profile_end_size", labelX, y += 28);
                this.drawLabel(context, "config.needsofnature.egg_profile_texture", labelX, y += 28);
                this.drawLabel(context, "config.needsofnature.egg_profile_health", labelX, y += 28);
            }

            public void close() {
                MinecraftClient.getInstance().setScreen(this.parent);
            }

            private void addField(TextFieldWidget field, int x, int y, String tooltipKey) {
                field.setX(x);
                field.setY(y);
                NonModMenuScreens.setTooltip((ClickableWidget)field, tooltipKey);
                this.addDrawableChild(field);
            }

            private TextFieldWidget newTextField(int width, String value) {
                TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, width, 20, (Text)Text.empty());
                field.setMaxLength(128);
                field.setText(value == null ? "" : value);
                return field;
            }

            private void drawLabel(DrawContext context, String key, int x, int y) {
                context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)key), x, y, -2039584);
            }

            private NonConfig.EggProfile collectProfile() {
                NonConfig.EggProfile raw = new NonConfig.EggProfile(EggProfileSettingsScreen.parseFloat(this.startSizeField.getText(), this.defaultProfile.startSize(), 0.5f), EggProfileSettingsScreen.parseFloat(this.endSizeField.getText(), this.defaultProfile.endSize(), 1.0f), this.textureField.getText() == null || this.textureField.getText().trim().isEmpty() ? null : this.textureField.getText().trim(), EggProfileSettingsScreen.parseFloat(this.healthField.getText(), this.defaultProfile.health(), 2.0f));
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
        extends Screen {
            private final Screen parent;
            private final Runnable onConfirm;

            private ResetOffspringCountConfirmScreen(Screen parent, Runnable onConfirm) {
                super((Text)Text.translatable((String)"config.needsofnature.offspring_count_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void init() {
                int centerX = this.width / 2;
                int buttonY = this.height / 2 + 20;
                ButtonWidget yesButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    MinecraftClient.getInstance().setScreen(this.parent);
                }).dimensions(centerX - 100, buttonY, 96, 20).build();
                this.addDrawableChild(yesButton);
                ButtonWidget noButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, buttonY, 96, 20).build();
                this.addDrawableChild(noButton);
            }

            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 20, -1);
                context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.offspring_count_reset_body"), this.width / 2, this.height / 2 - 4, -4144960);
            }
        }
    }

    static class GenderSpawnConfigScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private boolean serverConfigEditable;
        private Map<String, NonConfig.GenderSpawnChances> appliedMap;
        private GenderSpawnList genderList;
        private final List<GenderSpawnList.GenderEntry> rows = new ArrayList<GenderSpawnList.GenderEntry>();
        private ButtonWidget applyButton;
        private ButtonWidget doneButton;

        protected GenderSpawnConfigScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.gender_spawn_title"));
            this.parent = parent;
            this.config = config;
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            this.appliedMap = new LinkedHashMap<String, NonConfig.GenderSpawnChances>(NonEntityProfiles.resolveEffectiveGenderSpawnMap());
        }

        protected void init() {
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
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            this.genderList = new GenderSpawnList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild(this.genderList);
            this.rows.clear();
            for (GenderRowData data : preserved) {
                this.addRow(data.id(), data.male(), data.female(), data.both());
            }
            if (this.rows.isEmpty()) {
                this.addRow("", "48", "47", "5");
            }
            int centerX = this.width / 2;
            ButtonWidget addButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.gender_spawn_add"), button -> {
                this.addRow("", "48", "47", "5");
                this.updateApplyButton();
            }).dimensions(centerX - 100, this.height - 52, 96, 20).build();
            addButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)addButton, "config.needsofnature.tooltip.gender_spawn_add");
            this.addDrawableChild(addButton);
            ButtonWidget resetDefaultsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.gender_spawn_reset"), button -> MinecraftClient.getInstance().setScreen((Screen)new ResetGenderSpawnConfirmScreen(this, () -> {
                this.applyDefaultsToRows();
                this.updateApplyButton();
                MinecraftClient.getInstance().setScreen((Screen)this);
            }))).dimensions(centerX + 4, this.height - 52, 96, 20).build();
            resetDefaultsButton.active = this.serverConfigEditable;
            NonModMenuScreens.setTooltip((ClickableWidget)resetDefaultsButton, "config.needsofnature.tooltip.gender_spawn_reset");
            this.addDrawableChild(resetDefaultsButton);
            this.applyButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.apply"), button -> {
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
            }).dimensions(centerX - 100, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.applyButton, "config.needsofnature.tooltip.apply");
            this.addDrawableChild(this.applyButton);
            this.doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, this.height - 28, 96, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.doneButton, "config.needsofnature.tooltip.done_unsaved");
            this.addDrawableChild(this.doneButton);
            this.updateApplyButton();
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateApplyButton();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
            this.drawGenderSpawnHeader(context);
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void drawGenderSpawnHeader(DrawContext context) {
            if (this.genderList == null) {
                return;
            }
            int rowX = this.genderList.getRowLeft();
            int rowWidth = this.genderList.getRowWidth();
            int fieldW = 42;
            int removeW = 20;
            int idW = Math.max(130, rowWidth - removeW - fieldW * 3 - 30);
            int maleX = rowX + idW + 6;
            int femaleX = maleX + fieldW + 6;
            int bothX = femaleX + fieldW + 6;
            int y = 34;
            int color = -4342339;
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_spawn_column.entity"), rowX + 4, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_spawn_column.male"), maleX + fieldW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_spawn_column.female"), femaleX + fieldW / 2, y, color);
            context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_spawn_column.both"), bothX + fieldW / 2, y, color);
        }

        private void addRow(String idText, String maleText, String femaleText, String bothText) {
            GenderSpawnList.GenderEntry[] holder = new GenderSpawnList.GenderEntry[1];
            GenderSpawnList.GenderEntry entry = new GenderSpawnList.GenderEntry(this.textRenderer, idText, maleText, femaleText, bothText, () -> {
                GenderSpawnList.GenderEntry current = holder[0];
                if (current == null) {
                    return;
                }
                this.rows.removeIf(row -> row == current);
                this.genderList.removeEntryRow(current);
                this.updateApplyButton();
            });
            holder[0] = entry;
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
                this.applyButton.active = false;
                if (this.doneButton != null) {
                    this.doneButton.setMessage((Text)Text.translatable((String)"config.needsofnature.done"));
                }
                return;
            }
            Map<String, NonConfig.GenderSpawnChances> current = this.collectGenderMapFromFields();
            boolean hasUnsavedChanges = !current.equals(this.appliedMap);
            boolean hasInvalidRows = this.hasInvalidGenderChanceRows();
            this.applyButton.active = hasUnsavedChanges && !hasInvalidRows;
            this.applyButton.setMessage((Text)Text.translatable((String)(hasInvalidRows ? "config.needsofnature.gender_spawn_invalid_total" : "config.needsofnature.apply")));
            if (this.doneButton != null) {
                this.doneButton.setMessage((Text)Text.translatable((String)(hasUnsavedChanges ? "config.needsofnature.done_unsaved" : "config.needsofnature.done")));
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
                    Identifier id = Identifier.tryParse((String)entry.getKey());
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
                if (row == null || (id = row.getIdText()) == null || (id = id.trim()).isEmpty() || Identifier.tryParse((String)id) == null || (chances = NonConfig.sanitizeGenderSpawnChances(new NonConfig.GenderSpawnChances(this.parseInt(row.getMaleText()), this.parseInt(row.getFemaleText()), this.parseInt(row.getBothText())))) == null) continue;
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
        extends ElementListWidget<GenderSpawnList.GenderEntry> {
            private GenderSpawnList(MinecraftClient client, int width, int height, int top) {
                super(client, width, height, top, height, 24);
                this.centerListVertically = false;
            }

            private void addEntryRow(GenderEntry entry) {
                super.addEntry(entry);
            }

            private void removeEntryRow(GenderEntry entry) {
                super.removeEntry(entry);
            }

            public int getRowWidth() {
                return 420;
            }

            public int getRowLeft() {
                return (this.width - this.getRowWidth()) / 2;
            }

            private static final class GenderEntry
            extends ElementListWidget.Entry<GenderEntry> {
                private final TextFieldWidget idField;
                private final TextFieldWidget maleField;
                private final TextFieldWidget femaleField;
                private final TextFieldWidget bothField;
                private final ButtonWidget removeButton;
                private final List<ClickableWidget> widgets;
                private Runnable onChange;

                private GenderEntry(TextRenderer textRenderer, String idText, String maleText, String femaleText, String bothText, Runnable onRemove) {
                    this.idField = new TextFieldWidget(textRenderer, 0, 0, 170, 20, (Text)Text.empty());
                    this.idField.setText(idText == null ? "" : idText);
                    this.idField.setMaxLength(80);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.idField, "config.needsofnature.tooltip.gender_spawn_entry_entity");
                    this.maleField = GenderEntry.percentField(textRenderer, maleText);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.maleField, "config.needsofnature.tooltip.gender_spawn_entry_male");
                    this.femaleField = GenderEntry.percentField(textRenderer, femaleText);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.femaleField, "config.needsofnature.tooltip.gender_spawn_entry_female");
                    this.bothField = GenderEntry.percentField(textRenderer, bothText);
                    NonModMenuScreens.setTooltip((ClickableWidget)this.bothField, "config.needsofnature.tooltip.gender_spawn_entry_both");
                    this.removeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.remove_icon"), button -> {
                        if (onRemove != null) {
                            onRemove.run();
                        }
                    }).dimensions(0, 0, 20, 20).build();
                    NonModMenuScreens.setTooltip((ClickableWidget)this.removeButton, "config.needsofnature.tooltip.remove");
                    this.widgets = new ArrayList<ClickableWidget>(5);
                    this.widgets.add((ClickableWidget)this.idField);
                    this.widgets.add((ClickableWidget)this.maleField);
                    this.widgets.add((ClickableWidget)this.femaleField);
                    this.widgets.add((ClickableWidget)this.bothField);
                    this.widgets.add((ClickableWidget)this.removeButton);
                    this.idField.setChangedListener(ignored -> this.notifyChanged());
                    this.maleField.setChangedListener(ignored -> this.notifyChanged());
                    this.femaleField.setChangedListener(ignored -> this.notifyChanged());
                    this.bothField.setChangedListener(ignored -> this.notifyChanged());
                }

                private static TextFieldWidget percentField(TextRenderer textRenderer, String text) {
                    TextFieldWidget field = new TextFieldWidget(textRenderer, 0, 0, 42, 20, (Text)Text.empty());
                    field.setText(text == null ? "0" : text);
                    field.setMaxLength(3);
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
                    this.idField.setEditable(editable);
                    this.maleField.setEditable(editable);
                    this.femaleField.setEditable(editable);
                    this.bothField.setEditable(editable);
                    this.removeButton.active = editable;
                }

                private String getIdText() {
                    return this.idField.getText();
                }

                private String getMaleText() {
                    return this.maleField.getText();
                }

                private String getFemaleText() {
                    return this.femaleField.getText();
                }

                private String getBothText() {
                    return this.bothField.getText();
                }

                public void render(DrawContext context, int index, int rowY, int rowX, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                    int widgetY = rowY + 2;
                    int removeW = this.removeButton.getWidth();
                    int fieldW = this.maleField.getWidth();
                    int idW = Math.max(130, rowWidth - removeW - fieldW * 3 - 30);
                    int maleX = rowX + idW + 6;
                    int femaleX = maleX + fieldW + 6;
                    int bothX = femaleX + fieldW + 6;
                    int removeX = bothX + fieldW + 6;
                    this.idField.setX(rowX);
                    this.idField.setY(widgetY);
                    this.idField.setWidth(idW);
                    this.maleField.setX(maleX);
                    this.maleField.setY(widgetY);
                    this.femaleField.setX(femaleX);
                    this.femaleField.setY(widgetY);
                    this.bothField.setX(bothX);
                    this.bothField.setY(widgetY);
                    this.removeButton.setX(removeX);
                    this.removeButton.setY(widgetY);
                    for (ClickableWidget widget : this.widgets) {
                        widget.render(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends Selectable> selectableChildren() {
                    return this.widgets;
                }

                public List<? extends Element> children() {
                    return this.widgets;
                }
            }
        }

        private static final class ResetGenderSpawnConfirmScreen
        extends Screen {
            private final Screen parent;
            private final Runnable onConfirm;

            private ResetGenderSpawnConfirmScreen(Screen parent, Runnable onConfirm) {
                super((Text)Text.translatable((String)"config.needsofnature.gender_spawn_reset_title"));
                this.parent = parent;
                this.onConfirm = onConfirm;
            }

            protected void init() {
                int centerX = this.width / 2;
                int buttonY = this.height / 2 + 20;
                ButtonWidget yesButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.yes"), button -> {
                    if (this.onConfirm != null) {
                        this.onConfirm.run();
                    }
                    MinecraftClient.getInstance().setScreen(this.parent);
                }).dimensions(centerX - 100, buttonY, 96, 20).build();
                this.addDrawableChild(yesButton);
                ButtonWidget noButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.cancel"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX + 4, buttonY, 96, 20).build();
                this.addDrawableChild(noButton);
            }

            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 20, -1);
                context.drawCenteredTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.gender_spawn_reset_body"), this.width / 2, this.height / 2 - 4, -3355444);
            }
        }
    }

    static class LoadOrderScreen
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private boolean editable;
        private final List<String> externalPackOrder = new ArrayList<String>();
        private List<String> appliedExternalOrder = List.of();
        private List<String> defaultExternalOrder = List.of();
        private PackOrderList packOrderList;
        private ButtonWidget resetButton;

        protected LoadOrderScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order_title"));
            this.parent = parent;
            this.config = config == null ? new NonConfig() : config;
        }

        protected void init() {
            this.editable = NonModMenuScreens.canEditServerGameplaySettings();
            this.reloadExternalOrder();
            int centerX = this.width / 2;
            int listTop = 40;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            this.packOrderList = new PackOrderList(this.client, this.width, listHeight, listTop, this.textRenderer, this::moveUp, this::moveDown, this.editable);
            this.addDrawableChild(this.packOrderList);
            this.rebuildRows();
            this.resetButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.liquid_gain_reset"), button -> {
                this.externalPackOrder.clear();
                this.externalPackOrder.addAll(this.defaultExternalOrder);
                this.rebuildRows();
                this.updateButtons();
            }).dimensions(centerX - 100, this.height - 50, 98, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetButton, "config.needsofnature.tooltip.loaded_animations.load_order.reset");
            this.addDrawableChild(this.resetButton);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> {
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
                MinecraftClient.getInstance().setScreen(this.parent);
            }).dimensions(centerX + 2, this.height - 50, 98, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
            this.addDrawableChild(doneButton);
            this.updateButtons();
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, -1);
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order.summary"), 8, 24, -5197648);
            if (!this.editable) {
                context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order.read_only"), 8, 33, -14739);
            }
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void reloadExternalOrder() {
            List<String> discovered = NonExternalPackProvider.listExternalPackFileNames(NonExternalPackProvider.resolveDefaultPacksDir());
            this.defaultExternalOrder = List.copyOf(discovered);
            this.externalPackOrder.clear();
            this.externalPackOrder.addAll(NonExternalPackProvider.normalizeConfiguredOrder(this.config.getExternalNoNPackLoadOrder(), discovered));
            this.appliedExternalOrder = List.copyOf(this.externalPackOrder);
        }

        private void triggerInWorldReload() {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.world == null) {
                return;
            }
            IntegratedServer server = client.getServer();
            if (server != null) {
                server.execute(() -> {
                    try {
                        server.getCommandManager().getDispatcher().execute("reload", server.getCommandSource());
                    }
                    catch (Exception e) {
                        server.reloadResources(server.getDataPackManager().getEnabledNames());
                    }
                });
            }
            client.reloadResources();
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
            rows.add(PackOrderRow.locked("builtin:needsofnature", (Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order.base_non")));
            rows.add(PackOrderRow.locked("builtin:animationframework", (Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order.base_afw")));
            this.packOrderList.setRows(rows);
        }

        private void updateButtons() {
            if (this.resetButton != null) {
                this.resetButton.active = this.editable && !this.externalPackOrder.equals(this.defaultExternalOrder);
            }
        }

        private static final class PackOrderList
        extends ElementListWidget<PackOrderList.RowEntry> {
            private final TextRenderer textRenderer;
            private final Consumer<String> onMoveUp;
            private final Consumer<String> onMoveDown;
            private final boolean editable;

            private PackOrderList(MinecraftClient client, int width, int height, int top, TextRenderer textRenderer, Consumer<String> onMoveUp, Consumer<String> onMoveDown, boolean editable) {
                super(client, width, height, top, height, 22);
                this.textRenderer = textRenderer;
                this.onMoveUp = onMoveUp;
                this.onMoveDown = onMoveDown;
                this.editable = editable;
                this.centerListVertically = false;
            }

            private void setRows(List<PackOrderRow> rows) {
                this.clearEntries();
                for (PackOrderRow row : rows) {
                    this.addEntry(new RowEntry(row, this.textRenderer, this.onMoveUp, this.onMoveDown, this.editable));
                }
            }

            public int getRowWidth() {
                return Math.max(120, this.width - 24);
            }

            public int getRowLeft() {
                return 8;
            }

            protected int getScrollbarX() {
                return this.width - 8;
            }

            private static final class RowEntry
            extends ElementListWidget.Entry<RowEntry> {
                private final PackOrderRow row;
                private final TextRenderer textRenderer;
                private final Consumer<String> onMoveUp;
                private final Consumer<String> onMoveDown;
                private final boolean editable;
                private final NonModMenuScreens.FlatTextButton upButton;
                private final NonModMenuScreens.FlatTextButton downButton;
                private final List<ClickableWidget> widgets;

                private RowEntry(PackOrderRow row, TextRenderer textRenderer, Consumer<String> onMoveUp, Consumer<String> onMoveDown, boolean editable) {
                    this.row = row;
                    this.textRenderer = textRenderer;
                    this.onMoveUp = onMoveUp;
                    this.onMoveDown = onMoveDown;
                    this.editable = editable;
                    if (row.movable()) {
                        this.upButton = new NonModMenuScreens.FlatTextButton(16, (Text)Text.literal((String)"^"), button -> {
                            if (this.onMoveUp != null) {
                                this.onMoveUp.accept(this.row.key());
                            }
                        });
                        this.downButton = new NonModMenuScreens.FlatTextButton(16, (Text)Text.literal((String)"v"), button -> {
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

                public void render(DrawContext context, int index, int rowY, int rowX, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                    int x = rowX + 4;
                    int y = rowY + 7;
                    int right = rowX + rowWidth - 2;
                    int textWidth = Math.max(20, rowWidth - (this.row.movable() ? 46 : 0));
                    int color = this.row.movable() ? -2565928 : -7362305;
                    context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(this.row.label().getString(), textWidth), x, y, color);
                    if (this.upButton != null && this.downButton != null) {
                        this.upButton.active = this.editable && this.row.canMoveUp();
                        this.downButton.active = this.editable && this.row.canMoveDown();
                        this.upButton.setX(right - 34);
                        this.upButton.setY(y - 2);
                        this.downButton.setX(right - 18);
                        this.downButton.setY(y - 2);
                        this.upButton.render(context, mouseX, mouseY, delta);
                        this.downButton.render(context, mouseX, mouseY, delta);
                    }
                }

                public List<? extends Selectable> selectableChildren() {
                    return List.copyOf(this.widgets);
                }

                public void forEachChild(Consumer<ClickableWidget> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends Element> children() {
                    return List.copyOf(this.widgets);
                }
            }
        }

        private record PackOrderRow(String key, Text label, boolean movable, boolean canMoveUp, boolean canMoveDown) {
            private static PackOrderRow external(String fileName, int index, int total) {
                String safeName = fileName == null || fileName.isBlank() ? "<unknown>" : fileName;
                return new PackOrderRow(safeName, (Text)Text.literal((String)(index + 1 + ". " + safeName)), true, index > 0, index < total - 1);
            }

            private static PackOrderRow locked(String key, Text label) {
                String safeKey = key == null || key.isBlank() ? "<locked>" : key;
                Text safeLabel = label == null ? Text.literal((String)safeKey) : label;
                return new PackOrderRow(safeKey, safeLabel, false, false, false);
            }
        }
    }

    static class LoadedAnimationsScreen
    extends Screen {
        private final Screen parent;
        private final Set<String> expandedRowKeys = new HashSet<String>();
        private final Set<String> expandedPackIds = new HashSet<String>();
        private final Map<String, String> sourceHintByDefinitionId = new LinkedHashMap<String, String>();
        private TextFieldWidget filterField;
        private ButtonWidget filterAllButton;
        private ButtonWidget filterMissingButton;
        private ButtonWidget filterPartialButton;
        private ButtonWidget filterOkButton;
        private ButtonWidget sortModeButton;
        private ButtonWidget sortOrderButton;
        private LoadedAnimationsList loadedAnimationsList;
        private List<LoadedAnimationRow> allRows = List.of();
        private final Map<String, Identifier> packIconByResourcePackId = new LinkedHashMap<String, Identifier>();
        private final Set<String> disabledAnimationPacks = new LinkedHashSet<String>();
        private final Set<String> disabledAnimations = new LinkedHashSet<String>();
        private int filteredCount = 0;
        private boolean editable = true;
        private StatusFilter statusFilter = StatusFilter.ALL;
        private SortMode sortMode = SortMode.ID;
        private boolean sortAscending = true;
        private Text copyFeedbackText = Text.empty();
        private long copyFeedbackUntilMs = 0L;
        private static final long COPY_FEEDBACK_DURATION_MS = 1600L;
        private static final int MISSING_ANIM_PREVIEW_COUNT = 3;
        private static final int MISSING_MODEL_PREVIEW_COUNT = 3;
        private static final Identifier PLAYER_ID = Identifier.of((String)"minecraft", (String)"player");

        protected LoadedAnimationsScreen(Screen parent) {
            super((Text)Text.translatable((String)"config.needsofnature.loaded_animations_title"));
            this.parent = parent;
        }

        protected void init() {
            String previousFilter = this.filterField == null ? "" : this.filterField.getText();
            this.editable = NonModMenuScreens.canEditServerGameplaySettings();
            this.reloadDisabledAnimationState();
            int centerX = this.width / 2;
            int filterY = 34;
            int horizontalPadding = 8;
            int filterWidth = Math.max(120, this.width - horizontalPadding * 2);
            this.filterField = new TextFieldWidget(this.textRenderer, horizontalPadding, filterY, filterWidth, 20, (Text)Text.translatable((String)"config.needsofnature.loaded_animations_search"));
            this.filterField.setMaxLength(128);
            this.filterField.setText(previousFilter);
            this.filterField.setChangedListener(this::applyFilter);
            this.addDrawableChild(this.filterField);
            int controlsY = filterY + 24;
            int buttonH = 20;
            int smallButtonW = 62;
            int filterGap = 4;
            int startX = horizontalPadding;
            this.filterAllButton = ButtonWidget.builder((Text)Text.literal((String)"All"), button -> {
                this.statusFilter = StatusFilter.ALL;
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText());
            }).dimensions(startX, controlsY, smallButtonW, buttonH).build();
            this.addDrawableChild(this.filterAllButton);
            this.filterMissingButton = ButtonWidget.builder((Text)Text.literal((String)"Missing"), button -> {
                this.statusFilter = StatusFilter.MISSING;
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText());
            }).dimensions(startX + (smallButtonW + filterGap), controlsY, smallButtonW, buttonH).build();
            this.addDrawableChild(this.filterMissingButton);
            this.filterPartialButton = ButtonWidget.builder((Text)Text.literal((String)"Partial"), button -> {
                this.statusFilter = StatusFilter.PARTIAL;
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText());
            }).dimensions(startX + (smallButtonW + filterGap) * 2, controlsY, smallButtonW, buttonH).build();
            this.addDrawableChild(this.filterPartialButton);
            this.filterOkButton = ButtonWidget.builder((Text)Text.literal((String)"OK"), button -> {
                this.statusFilter = StatusFilter.OK;
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText());
            }).dimensions(startX + (smallButtonW + filterGap) * 3, controlsY, smallButtonW, buttonH).build();
            this.addDrawableChild(this.filterOkButton);
            int orderButtonW = 56;
            int sortButtonW = 116;
            int orderX = this.width - horizontalPadding - orderButtonW;
            int sortX = orderX - 6 - sortButtonW;
            this.sortModeButton = ButtonWidget.builder((Text)this.sortModeLabel(), button -> {
                this.sortMode = switch (this.sortMode) {
                    case ID -> SortMode.STATUS;
                    case STATUS -> SortMode.MISSING;
                    case MISSING -> SortMode.ENTITIES;
                    case ENTITIES -> SortMode.ID;
                };
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText(), false);
            }).dimensions(sortX, controlsY, sortButtonW, buttonH).build();
            this.addDrawableChild(this.sortModeButton);
            this.sortOrderButton = ButtonWidget.builder((Text)this.sortOrderLabel(), button -> {
                this.sortAscending = !this.sortAscending;
                this.updateControlButtons();
                this.applyFilter(this.filterField.getText(), false);
            }).dimensions(orderX, controlsY, orderButtonW, buttonH).build();
            this.addDrawableChild(this.sortOrderButton);
            int listTop = controlsY + buttonH + 6;
            int bottomArea = 64;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            this.loadedAnimationsList = new LoadedAnimationsList(this.client, this.width, listHeight, listTop, this.textRenderer, this::togglePackExpanded, this::toggleRowExpanded, this::copyRowMissingDetails, this::togglePackEnabled, this::toggleAnimationEnabled, () -> this.editable);
            this.addDrawableChild(this.loadedAnimationsList);
            this.rebuildSourceHints();
            this.rebuildRows();
            this.updateControlButtons();
            this.applyFilter(previousFilter);
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> MinecraftClient.getInstance().setScreen(this.parent)).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done");
            this.addDrawableChild(doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, -1);
            context.drawTextWithShadow(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.loaded_animations_summary", (Object[])new Object[]{this.allRows.size(), this.filteredCount}), 8, 22, -5197648);
            if (this.copyFeedbackText != null && !this.copyFeedbackText.getString().isBlank() && System.currentTimeMillis() < this.copyFeedbackUntilMs) {
                int feedbackWidth = this.textRenderer.getWidth((StringVisitable)this.copyFeedbackText);
                int feedbackX = Math.max(8, this.width - feedbackWidth - 8);
                context.drawTextWithShadow(this.textRenderer, this.copyFeedbackText, feedbackX, 22, -6560902);
            }
        }

        public void close() {
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private void updateControlButtons() {
            if (this.filterAllButton != null) {
                this.filterAllButton.setMessage(this.statusFilterLabel(StatusFilter.ALL));
                boolean bl = this.filterAllButton.active = this.statusFilter != StatusFilter.ALL;
            }
            if (this.filterMissingButton != null) {
                this.filterMissingButton.setMessage(this.statusFilterLabel(StatusFilter.MISSING));
                boolean bl = this.filterMissingButton.active = this.statusFilter != StatusFilter.MISSING;
            }
            if (this.filterPartialButton != null) {
                this.filterPartialButton.setMessage(this.statusFilterLabel(StatusFilter.PARTIAL));
                boolean bl = this.filterPartialButton.active = this.statusFilter != StatusFilter.PARTIAL;
            }
            if (this.filterOkButton != null) {
                this.filterOkButton.setMessage(this.statusFilterLabel(StatusFilter.OK));
                boolean bl = this.filterOkButton.active = this.statusFilter != StatusFilter.OK;
            }
            if (this.sortModeButton != null) {
                this.sortModeButton.setMessage(this.sortModeLabel());
            }
            if (this.sortOrderButton != null) {
                this.sortOrderButton.setMessage(this.sortOrderLabel());
            }
        }

        private Text statusFilterLabel(StatusFilter mode) {
            boolean selected = this.statusFilter == mode;
            String base = switch (mode) {
                case ALL -> "All";
                case MISSING -> "Missing";
                case PARTIAL -> "Partial";
                case OK -> "OK";
            };
            return Text.literal((String)(selected ? "[" + base + "]" : base));
        }

        private Text sortModeLabel() {
            String value = switch (this.sortMode) {
                case ID -> "ID";
                case STATUS -> "Status";
                case MISSING -> "Missing";
                case ENTITIES -> "Entities";
            };
            return Text.literal((String)("Sort: " + value));
        }

        private Text sortOrderLabel() {
            return Text.literal((String)(this.sortAscending ? "Asc" : "Desc"));
        }

        private void rebuildSourceHints() {
            this.sourceHintByDefinitionId.clear();
            ResourceManager manager = this.resolveResourceManagerForAnimdefs();
            if (manager == null) {
                return;
            }
            try {
                Map<Identifier, List<Resource>> resources = manager.findAllResources("afw_animdefs", id -> id.getPath().endsWith(".json"));
                for (Map.Entry<Identifier, List<Resource>> entry : resources.entrySet()) {
                    Identifier definitionId = LoadedAnimationsScreen.definitionIdFromFile(entry.getKey());
                    String sourceHint = LoadedAnimationsScreen.summarizeSources(entry.getValue());
                    this.sourceHintByDefinitionId.put(definitionId.toString(), sourceHint);
                }
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve animation definition source hints.", (Throwable)e);
            }
        }

        private ResourceManager resolveResourceManagerForAnimdefs() {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) {
                return null;
            }
            try {
                if (client.getServer() != null) {
                    return client.getServer().getResourceManager();
                }
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve server resource manager for loaded animations.", (Throwable)e);
            }
            try {
                return client.getResourceManager();
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.debug("[NoN] Failed to resolve client resource manager for loaded animations.", (Throwable)e);
                return null;
            }
        }

        private static Identifier definitionIdFromFile(Identifier fileId) {
            String prefix;
            if (fileId == null) {
                return Identifier.of((String)"animationframework", (String)"unknown");
            }
            String path = fileId.getPath();
            if (path.startsWith(prefix = "afw_animdefs/")) {
                path = path.substring(prefix.length());
            }
            if (path.endsWith(".json")) {
                path = path.substring(0, path.length() - 5);
            }
            return Identifier.of((String)fileId.getNamespace(), (String)path);
        }

        private static String summarizeSources(List<Resource> resources) {
            if (resources == null || resources.isEmpty()) {
                return "unknown";
            }
            LinkedHashSet<String> sources = new LinkedHashSet<String>();
            for (Resource resource : resources) {
                if (resource == null) continue;
                sources.add(LoadedAnimationsScreen.normalizeSourceId(resource.getResourcePackName()));
            }
            if (sources.isEmpty()) {
                return "unknown";
            }
            ArrayList<String> list = new ArrayList<String>(sources);
            int take = Math.min(2, list.size());
            String summary = String.join(", ", list.subList(0, take));
            int more = list.size() - take;
            if (more > 0) {
                summary = summary + ", +" + more + " more";
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
            this.applyFilter(this.filterField == null ? "" : this.filterField.getText(), false);
        }

        @Nullable
        private Identifier resolvePackIcon(String resourcePackId) {
            if (resourcePackId == null || resourcePackId.isBlank()) {
                return null;
            }
            if (this.packIconByResourcePackId.containsKey(resourcePackId)) {
                return this.packIconByResourcePackId.get(resourcePackId);
            }
            Identifier textureId = this.loadPackIcon(resourcePackId);
            this.packIconByResourcePackId.put(resourcePackId, textureId);
            return textureId;
        }

        /*
         * Enabled aggressive exception aggregation
         */
        @Nullable
        private Identifier loadPackIcon(String resourcePackId) {
            MinecraftClient client = MinecraftClient.getInstance();
            ResourceManager manager = this.resolveResourceManagerForAnimdefs();
            if (client == null || manager == null) {
                return null;
            }
            try (Stream<ResourcePack> stream = manager.streamResourcePacks();){
                Identifier class_29602;
                block21: {
                    InputStream input;
                    block19: {
                        Identifier class_29603;
                        block20: {
                            ResourcePack pack = stream.filter(candidate -> resourcePackId.equals(candidate.getName())).findFirst().orElse(null);
                            if (pack == null) {
                                Identifier class_29604 = null;
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
                    NativeImage image = NativeImage.read((InputStream)input);
                    Identifier id = Identifier.of((String)"needsofnature", (String)("dynamic/animation_pack_icons/" + LoadedAnimationsScreen.sanitizeTexturePath(resourcePackId)));
                    client.getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
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
        private static InputStream openRootResource(ResourcePack pack, String fileName) {
            if (pack == null || fileName == null) {
                return null;
            }
            InputSupplier supplier = pack.openRoot(new String[]{fileName});
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
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) {
                return;
            }
            String text = String.join((CharSequence)"\n", row.missingLines());
            client.keyboard.setClipboard(text);
            this.showCopyFeedback((Text)Text.translatable((String)"config.needsofnature.loaded_animations.copied_missing"));
        }

        private void showCopyFeedback(Text message) {
            this.copyFeedbackText = message == null ? Text.empty() : message;
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
            ArrayList<String> actorParts = new ArrayList<String>();
            for (int i = 0; i < definition.actors().size(); ++i) {
                AfwAnimationDefinitions.ActorConstraint actor = (AfwAnimationDefinitions.ActorConstraint)definition.actors().get(i);
                String key = actorKeys.get(i);
                actorParts.add(key + "=" + LoadedAnimationsScreen.summarizeEntityTypes(actor.entityTypes()));
            }
            Integer peakStage = NonPeakStages.getPeakStage(definition.id());
            String peakLabel = peakStage == null ? "-" : "p" + peakStage;
            ResourceCheck check = LoadedAnimationsScreen.evaluateResourceCheck(definition, actorKeys);
            ResourceStatus status = check.status();
            String statusLabel = Text.translatable((String)status.translationKey).getString();
            String key = definition.id().toString();
            AfwAnimationDefinitions.AnimationPackInfo packInfo = definition.packInfo();
            String packId = packInfo == null ? "unknown" : packInfo.id();
            String resourcePackId = packInfo == null ? "unknown" : packInfo.resourcePackId();
            String packName = packInfo == null ? "Unknown Pack" : packInfo.name();
            String packAuthor = packInfo == null ? "Unknown" : packInfo.author();
            boolean packDisabled = this.disabledAnimationPacks.contains(packId);
            boolean animationDisabled = this.disabledAnimations.contains(key);
            String title = LoadedAnimationsScreen.shortId(definition.id()) + (packDisabled ? " [pack off]" : (animationDisabled ? " [off]" : ""));
            String actorsLine = "actors: " + (actorParts.isEmpty() ? "-" : String.join(", ", actorParts));
            String stagesLine = "stages: " + definition.stages().size() + " | peak: " + peakLabel + " | " + LoadedAnimationsScreen.summarizeStages(definition.stages());
            String resourcesLine = "resources: anim " + check.animationFound + "/" + check.animationTotal + " | model " + check.modelFound + "/" + check.modelTotal;
            if (check.modelUnchecked > 0) {
                resourcesLine = resourcesLine + " | model n/a " + check.modelUnchecked;
            }
            String contentTagsLine = "content tags: " + ((contentTags = LoadedAnimationsScreen.readContentTags(definition)).isEmpty() ? "-" : String.join((CharSequence)", ", contentTags));
            String blockRequirementLine = LoadedAnimationsScreen.summarizeBlockRequirements(definition.blockRequirements());
            ArrayList<String> infoLines = new ArrayList<String>();
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
            String searchBlob = (String.valueOf(definition.id()) + " " + String.join(" ", infoLines) + " " + resourcesLine + " " + packName + " " + packAuthor + " " + this.sourceHintByDefinitionId.getOrDefault(key, "unknown") + (hasMissingDetails ? " " + String.join(" ", missingLines) : "")).toLowerCase(Locale.ROOT);
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
                    this.loadedAnimationsList.setScrollAmount(0.0);
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
            LoadedAnimationRow first = rows.get(0);
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
            this.applyFilter(this.filterField == null ? "" : this.filterField.getText(), false);
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
            this.applyFilter(this.filterField == null ? "" : this.filterField.getText(), false);
        }

        private Comparator<LoadedAnimationRow> rowComparator() {
            Comparator<LoadedAnimationRow> comparator = switch (this.sortMode) {
                case ID -> Comparator.comparing(LoadedAnimationRow::key);
                case STATUS -> Comparator.<LoadedAnimationRow>comparingInt(row -> row.status().sortRank()).thenComparing(LoadedAnimationRow::key);
                case MISSING -> Comparator.<LoadedAnimationRow>comparingInt(row -> row.missingCount()).thenComparing(LoadedAnimationRow::key);
                case ENTITIES -> Comparator.<LoadedAnimationRow>comparingInt(row -> row.entityTypeCount()).thenComparing(LoadedAnimationRow::key);
            };
            return this.sortAscending ? comparator : comparator.reversed();
        }

        private Comparator<LoadedAnimationRow> groupedRowComparator() {
            return Comparator.<LoadedAnimationRow, String>comparing(row -> row.packName().toLowerCase(Locale.ROOT)).thenComparing(row -> row.packId().toLowerCase(Locale.ROOT)).thenComparing(this.rowComparator());
        }

        private static List<Identifier> stageIdsFor(AfwAnimationDefinitions.Definition definition) {
            LinkedHashSet<Identifier> ids = new LinkedHashSet<Identifier>();
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
            ArrayList<String> parts = new ArrayList<String>();
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
            return "block: " + String.join(" | ", parts);
        }

        private static String summarizeBlockPredicate(AfwAnimationDefinitions.BlockPredicate blocks) {
            if (blocks == null || blocks.isEmpty()) {
                return "-";
            }
            ArrayList<String> values = new ArrayList<String>();
            if (blocks.blockIds() != null) {
                for (Identifier id : blocks.blockIds()) {
                    if (id == null) continue;
                    values.add(LoadedAnimationsScreen.shortId(id));
                }
            }
            if (blocks.tagIds() != null) {
                for (Identifier id : blocks.tagIds()) {
                    if (id == null) continue;
                    values.add("#" + LoadedAnimationsScreen.shortId(id));
                }
            }
            if (values.isEmpty()) {
                return "-";
            }
            int take = Math.min(3, values.size());
            String summary = String.join(", ", values.subList(0, take));
            int remaining = values.size() - take;
            return remaining > 0 ? summary + ", +" + remaining : summary;
        }

        private static ResourceCheck evaluateResourceCheck(AfwAnimationDefinitions.Definition definition, List<String> actorKeys) {
            List<Identifier> stageIds = LoadedAnimationsScreen.stageIdsFor(definition);
            int animationTotal = 0;
            int animationFound = 0;
            int modelTotal = 0;
            int modelFound = 0;
            int modelUnchecked = 0;
            LinkedHashSet<String> missingAnimations = new LinkedHashSet<String>();
            LinkedHashSet<String> missingModels = new LinkedHashSet<String>();
            for (int i = 0; i < definition.actors().size(); ++i) {
                AfwAnimationDefinitions.ActorConstraint actor = (AfwAnimationDefinitions.ActorConstraint)definition.actors().get(i);
                String actorKey = actorKeys.get(i);
                List<Identifier> runtimeTypes = LoadedAnimationsScreen.normalizeEntityTypesForRuntime(actor.entityTypes());
                if (runtimeTypes.isEmpty()) {
                    ++modelUnchecked;
                    for (Identifier stageId : stageIds) {
                        Identifier animationResource = AfwGeckoResourceResolver.resolveAnimationResource((Identifier)stageId, (String)actorKey, null);
                        ++animationTotal;
                        if (AfwGeckoResourceResolver.hasBakedAnimation((Identifier)animationResource)) {
                            ++animationFound;
                            continue;
                        }
                        missingAnimations.add("actor=" + actorKey + " -> " + LoadedAnimationsScreen.shortId(animationResource));
                    }
                    continue;
                }
                for (Identifier entityTypeId : runtimeTypes) {
                    ModelCoverage coverage = LoadedAnimationsScreen.evaluateModelCoverage(actor, entityTypeId);
                    ++modelTotal;
                    if (coverage.found()) {
                        ++modelFound;
                    } else {
                        missingModels.add(coverage.missingDetail());
                    }
                    for (Identifier stageId : stageIds) {
                        Identifier animationResource = AfwGeckoResourceResolver.resolveAnimationResource((Identifier)stageId, (String)actorKey, (Identifier)entityTypeId);
                        ++animationTotal;
                        if (AfwGeckoResourceResolver.hasBakedAnimation((Identifier)animationResource)) {
                            ++animationFound;
                            continue;
                        }
                        missingAnimations.add("actor=" + actorKey + " type=" + String.valueOf(entityTypeId) + " -> " + LoadedAnimationsScreen.shortId(animationResource));
                    }
                }
            }
            return new ResourceCheck(animationTotal, animationFound, modelTotal, modelFound, modelUnchecked, List.copyOf(missingAnimations), List.copyOf(missingModels));
        }

        private static String shortId(Identifier id) {
            if (id == null) {
                return "null";
            }
            if ("animationframework".equals(id.getNamespace())) {
                return id.getPath();
            }
            return id.toString();
        }

        private static List<Identifier> normalizeEntityTypesForRuntime(Set<Identifier> entityTypes) {
            if (entityTypes == null || entityTypes.isEmpty()) {
                return List.of();
            }
            LinkedHashSet<Identifier> normalized = new LinkedHashSet<Identifier>();
            for (Identifier raw : entityTypes) {
                if (raw == null) continue;
                normalized.add(LoadedAnimationsScreen.normalizeEntityTypeForRuntime(raw));
            }
            ArrayList<Identifier> sorted = new ArrayList<Identifier>(normalized);
            sorted.sort(Comparator.comparing(Identifier::toString));
            return List.copyOf(sorted);
        }

        private static int countRuntimeEntityTypes(AfwAnimationDefinitions.Definition definition) {
            if (definition == null || definition.actors() == null || definition.actors().isEmpty()) {
                return 0;
            }
            int count = 0;
            for (AfwAnimationDefinitions.ActorConstraint actor : definition.actors()) {
                List<Identifier> types = LoadedAnimationsScreen.normalizeEntityTypesForRuntime(actor.entityTypes());
                count += types.size();
            }
            return count;
        }

        private static Identifier normalizeEntityTypeForRuntime(Identifier entityTypeId) {
            if (entityTypeId == null) {
                return PLAYER_ID;
            }
            return entityTypeId;
        }

        private static ModelCoverage evaluateModelCoverage(AfwAnimationDefinitions.ActorConstraint actor, Identifier entityTypeId) {
            AfwGeckoResourceResolver.ModelAndTexture modelAndTexture = AfwGeckoResourceResolver.resolveModelAndTexture((Identifier)entityTypeId);
            Identifier baseModelId = modelAndTexture.model();
            boolean baseExists = !modelAndTexture.missingModel();
            Identifier maleModel = Identifier.of((String)baseModelId.getNamespace(), (String)(baseModelId.getPath() + ".m"));
            Identifier femaleModel = Identifier.of((String)baseModelId.getNamespace(), (String)(baseModelId.getPath() + ".f"));
            boolean maleExists = AfwGeckoResourceResolver.hasGeoModel((Identifier)maleModel);
            boolean femaleExists = AfwGeckoResourceResolver.hasGeoModel((Identifier)femaleModel);
            GenderRequirement requirement = LoadedAnimationsScreen.genderRequirement(actor.requiredTags());
            boolean found = switch (requirement) {
                case MALE_ONLY -> baseExists || maleExists;
                case FEMALE_ONLY -> baseExists || femaleExists;
                case ANY -> baseExists || maleExists && femaleExists;
            };
            if (found) {
                return ModelCoverage.present();
            }
            String detail = switch (requirement) {
                case MALE_ONLY -> String.valueOf(entityTypeId) + " (need base or .m)";
                case FEMALE_ONLY -> String.valueOf(entityTypeId) + " (need base or .f)";
                case ANY -> String.valueOf(entityTypeId) + " (need base or both .m/.f)";
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
            ArrayList<String> parts = new ArrayList<String>();
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
                parts.add(stageLabel + " " + loopLabel + " " + cycle + " @" + speed);
            }
            if (stages.size() > previewCount) {
                parts.add("+" + (stages.size() - previewCount) + " more");
            }
            return "loop/cycle: " + String.join(", ", parts);
        }

        private static String summarizeEntityTypes(Set<Identifier> entityTypes) {
            if (entityTypes == null || entityTypes.isEmpty()) {
                return "any";
            }
            ArrayList<Identifier> sorted = new ArrayList<Identifier>(entityTypes);
            sorted.sort(Comparator.comparing(Identifier::toString));
            ArrayList<String> names = new ArrayList<String>();
            for (Identifier id : sorted) {
                String displayName = id.toString();
                EntityType entityType = (EntityType)Registries.ENTITY_TYPE.get(id);
                displayName = entityType.getName().getString();
                names.add(String.valueOf(id) + "(" + displayName + ")");
            }
            return String.join("|", names);
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
        extends ElementListWidget<LoadedAnimationsList.RowEntry> {
            private final TextRenderer textRenderer;
            private final Consumer<String> onTogglePackExpanded;
            private final Consumer<String> onToggleRow;
            private final Consumer<LoadedAnimationRow> onCopyMissing;
            private final Consumer<String> onTogglePackEnabled;
            private final Consumer<String> onToggleAnimationEnabled;
            private final BooleanSupplier editableSupplier;

            private LoadedAnimationsList(MinecraftClient client, int width, int height, int top, TextRenderer textRenderer, Consumer<String> onTogglePackExpanded, Consumer<String> onToggleRow, Consumer<LoadedAnimationRow> onCopyMissing, Consumer<String> onTogglePackEnabled, Consumer<String> onToggleAnimationEnabled, BooleanSupplier editableSupplier) {
                super(client, width, height, top, height, 60);
                this.textRenderer = textRenderer;
                this.onTogglePackExpanded = onTogglePackExpanded;
                this.onToggleRow = onToggleRow;
                this.onCopyMissing = onCopyMissing;
                this.onTogglePackEnabled = onTogglePackEnabled;
                this.onToggleAnimationEnabled = onToggleAnimationEnabled;
                this.editableSupplier = editableSupplier;
                this.centerListVertically = false;
            }

            private void setRows(List<LoadedListRow> rows) {
                this.clearEntries();
                for (LoadedListRow row : rows) {
                    this.addEntry(new RowEntry(row, this.textRenderer, this.onToggleRow, this.onCopyMissing, this.onTogglePackExpanded, this.onTogglePackEnabled, this.onToggleAnimationEnabled, this.editableSupplier));
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

            public int getRowWidth() {
                return Math.max(120, this.width - 24);
            }

            public int getRowLeft() {
                return 8;
            }

            protected int getScrollbarX() {
                return this.width - 8;
            }

            private static final class RowEntry
            extends ElementListWidget.Entry<RowEntry> {
                private final LoadedListRow row;
                private final TextRenderer textRenderer;
                private final Consumer<String> onToggleRow;
                private final Consumer<LoadedAnimationRow> onCopyMissing;
                private final Consumer<String> onTogglePackExpanded;
                private final Consumer<String> onTogglePackEnabled;
                private final Consumer<String> onToggleAnimationEnabled;
                private final BooleanSupplier editableSupplier;
                private final NonModMenuScreens.FlatTextButton toggleButton;
                private final NonModMenuScreens.FlatTextButton copyMissingButton;
                private final ButtonWidget enabledButton;
                private final List<ClickableWidget> widgets;

                private RowEntry(LoadedListRow row, TextRenderer textRenderer, Consumer<String> onToggleRow, Consumer<LoadedAnimationRow> onCopyMissing, Consumer<String> onTogglePackExpanded, Consumer<String> onTogglePackEnabled, Consumer<String> onToggleAnimationEnabled, BooleanSupplier editableSupplier) {
                    this.row = row;
                    this.textRenderer = textRenderer;
                    this.onToggleRow = onToggleRow;
                    this.onCopyMissing = onCopyMissing;
                    this.onTogglePackExpanded = onTogglePackExpanded;
                    this.onTogglePackEnabled = onTogglePackEnabled;
                    this.onToggleAnimationEnabled = onToggleAnimationEnabled;
                    this.editableSupplier = editableSupplier == null ? () -> true : editableSupplier;
                    this.toggleButton = new NonModMenuScreens.FlatTextButton(18, Text.literal(row instanceof LoadedAnimationRow && ((LoadedAnimationRow)row).expanded() ? "-" : "+"), button -> {
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
                    this.copyMissingButton = new NonModMenuScreens.FlatTextButton(24, (Text)Text.literal((String)"MS"), button -> {
                        LoadedAnimationRow animationRow;
                        LoadedListRow patt0$temp = this.row;
                        if (patt0$temp instanceof LoadedAnimationRow && (animationRow = (LoadedAnimationRow)patt0$temp).hasMissingDetails() && this.onCopyMissing != null) {
                            this.onCopyMissing.accept(animationRow);
                        }
                    });
                    this.enabledButton = ButtonWidget.builder((Text)Text.literal((String)"ON"), button -> {
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
                    }).dimensions(0, 0, 42, 18).build();
                    ArrayList<ClickableWidget> ws = new ArrayList<ClickableWidget>();
                    ws.add(this.enabledButton);
                    if (row instanceof LoadedAnimationRow && ((LoadedAnimationRow)row).hasMissingDetails()) {
                        ws.add(this.copyMissingButton);
                        ws.add(this.toggleButton);
                    }
                    this.widgets = List.copyOf(ws);
                }

                public void render(DrawContext context, int index, int rowY, int rowX, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                    int x = rowX + 4;
                    int y = rowY + 3;
                    int maxWidth = Math.max(20, rowWidth - 70);
                    int right = rowX + rowWidth - 2;
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
                    this.enabledButton.setMessage((Text)Text.literal((String)(animationRow.animationDisabled() ? "OFF" : "ON")));
                    this.enabledButton.active = this.editableSupplier.getAsBoolean();
                    this.enabledButton.setX(right - 44);
                    this.enabledButton.setY(y - 4);
                    this.enabledButton.render(context, mouseX, mouseY, delta);
                    if (animationRow.hasMissingDetails()) {
                        this.copyMissingButton.setX(right - 86);
                        this.copyMissingButton.setY(y - 1);
                        this.copyMissingButton.render(context, mouseX, mouseY, delta);
                    }
                    int textX = x;
                    int statusButtonLeft = animationRow.hasMissingDetails() ? right - 86 : right - 44;
                    String statusText = "[" + Text.translatable((String)animationRow.status().translationKey).getString() + "]";
                    int statusWidth = this.textRenderer.getWidth(statusText);
                    int statusX = Math.max(textX, statusButtonLeft - statusWidth - 6);
                    int textWidth = Math.max(20, statusX - textX - 6);
                    int titleColor = animationRow.effectivelyDisabled() ? -8947849 : animationRow.status().color;
                    int statusColor = animationRow.effectivelyDisabled() ? -8947849 : animationRow.status().color;
                    context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(animationRow.title(), textWidth), textX, y, titleColor);
                    context.drawTextWithShadow(this.textRenderer, statusText, statusX, y, statusColor);
                    List<String> infoLines = animationRow.infoLines() == null ? List.of() : animationRow.infoLines();
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
                        context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth((String)infoLines.get(i), textWidth), textX, y + 9 + i * 9, color);
                    }
                    int missingY = y + 9 + infoLines.size() * 9;
                    if (animationRow.hasMissingDetails() && animationRow.expanded()) {
                        int missingTextX = x + 20;
                        int missingWidth = Math.max(20, maxWidth - 20);
                        this.toggleButton.setMessage((Text)Text.literal((String)"-"));
                        this.toggleButton.setX(x);
                        this.toggleButton.setY(missingY - 1);
                        this.toggleButton.render(context, mouseX, mouseY, delta);
                        List<String> missingLines = animationRow.missingLines();
                        int maxLines = Math.min(10, missingLines == null ? 0 : missingLines.size());
                        for (int i = 0; i < maxLines; ++i) {
                            String line = missingLines.get(i);
                            context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(line, missingWidth), missingTextX, missingY + i * 9, -7303024);
                        }
                    } else if (animationRow.hasMissingDetails()) {
                        int missingTextX = x + 20;
                        int missingWidth = Math.max(20, maxWidth - 20);
                        this.toggleButton.setMessage((Text)Text.literal((String)"+"));
                        this.toggleButton.setX(x);
                        this.toggleButton.setY(missingY - 1);
                        this.toggleButton.render(context, mouseX, mouseY, delta);
                        context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(Text.translatable((String)"config.needsofnature.loaded_animations.expand_hint").getString(), missingWidth), missingTextX, missingY, -7303024);
                    }
                }

                public boolean mouseClicked(double mouseX, double mouseY, int button) {
                    LoadedListRow loadedListRow = this.row;
                    if (loadedListRow instanceof LoadedPackHeaderRow) {
                        LoadedPackHeaderRow headerRow = (LoadedPackHeaderRow)loadedListRow;
                        if (button == 0 && this.onTogglePackExpanded != null && !this.enabledButton.isMouseOver(mouseX, mouseY)) {
                            this.onTogglePackExpanded.accept(headerRow.packId());
                            return true;
                        }
                    }
                    return super.mouseClicked(mouseX, mouseY, button);
                }

                private void renderPackHeader(DrawContext context, int mouseX, int mouseY, float delta, int x, int y, int right, LoadedPackHeaderRow headerRow) {
                    this.enabledButton.setMessage((Text)Text.literal((String)(headerRow.disabled() ? "OFF" : "ON")));
                    this.enabledButton.active = this.editableSupplier.getAsBoolean();
                    this.enabledButton.setX(right - 44);
                    this.enabledButton.setY(y + 4);
                    this.enabledButton.render(context, mouseX, mouseY, delta);
                    int iconSize = 24;
                    int iconX = x;
                    int iconY = y + 1;
                    context.fill(iconX - 1, iconY - 1, iconX + iconSize + 1, iconY + iconSize + 1, -14671840);
                    if (headerRow.iconTexture() != null) {
                        context.drawTexture(headerRow.iconTexture(), iconX, iconY, 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize);
                    } else {
                        context.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, -12829636);
                        context.drawTextWithShadow(this.textRenderer, "AFW", iconX + 3, iconY + 8, -4671304);
                    }
                    int textX = iconX + iconSize + 7;
                    int textWidth = Math.max(20, right - textX - 42);
                    int titleColor = headerRow.disabled() ? -7829368 : -1;
                    String title = headerRow.packName() + " (" + headerRow.visibleAnimations() + "/" + headerRow.totalAnimations() + ")" + (headerRow.expanded() ? "" : " [collapsed]");
                    context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(title, textWidth), textX, y + 2, titleColor);
                    String subtitle = "author: " + headerRow.packAuthor() + " | id: " + headerRow.packId();
                    context.drawTextWithShadow(this.textRenderer, this.textRenderer.trimToWidth(subtitle, textWidth), textX, y + 13, -6645094);
                }

                public List<? extends Selectable> selectableChildren() {
                    return List.copyOf(this.widgets);
                }

                public void forEachChild(Consumer<ClickableWidget> consumer) {
                    this.widgets.forEach(consumer);
                }

                public List<? extends Element> children() {
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
                return switch (filter) {
                    case ALL -> true;
                    case MISSING -> this.status == ResourceStatus.MISSING;
                    case PARTIAL -> this.status == ResourceStatus.PARTIAL;
                    case OK -> this.status == ResourceStatus.OK;
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
                return switch (this) {
                    case MISSING -> 0;
                    case PARTIAL -> 1;
                    case OK -> 2;
                };
            }
        }

        private record LoadedPackHeaderRow(String key, String packId, String resourcePackId, String packName, String packAuthor, int visibleAnimations, int totalAnimations, boolean disabled, boolean expanded, @Nullable Identifier iconTexture) implements LoadedListRow
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
    extends Screen {
        private final Screen parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private boolean serverConfigEditable;
        private int scanBudget;
        private NonDebugChatMode debugChatMode;
        private boolean debugSpinMode;
        private boolean convertMaleOnlyVInjectionsToA;
        private boolean attackCreativePlayers;
        private TextFieldWidget budgetField;
        private ButtonWidget debugModeButton;
        private ButtonWidget debugSpinModeButton;
        private ButtonWidget convertMaleOnlyVInjectionsToAButton;
        private ButtonWidget attackCreativePlayersButton;
        private ButtonWidget resetBudgetButton;
        private ButtonWidget resetDebugModeButton;
        private ButtonWidget resetDebugSpinModeButton;
        private ButtonWidget resetConvertMaleOnlyVInjectionsToAButton;
        private ButtonWidget resetAttackCreativePlayersButton;

        protected DebugAdvancedScreen(Screen parent, NonConfig config) {
            super((Text)Text.translatable((String)"config.needsofnature.debug_advanced_title"));
            this.parent = parent;
            this.config = config;
            this.scanBudget = config.getScanBudgetPerTick();
            this.debugChatMode = config.debugChatMode();
            this.debugSpinMode = config.isDebugSpinMode();
            this.convertMaleOnlyVInjectionsToA = config.convertMaleOnlyVInjectionsToA();
            this.attackCreativePlayers = NonHudOverlay.getRuntimeAttackCreativePlayers();
        }

        protected void init() {
            this.serverConfigEditable = NonModMenuScreens.canEditServerGameplaySettings();
            if (this.budgetField != null) {
                this.scanBudget = this.parseField(this.budgetField, this.scanBudget);
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild(settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.runtime_budget")));
            this.budgetField = this.newNumberField(fieldWidth, this.scanBudget);
            this.resetBudgetButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> this.budgetField.setText(String.valueOf(this.defaults.getScanBudgetPerTick()))).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetBudgetButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.scan_budget"), (ClickableWidget)this.budgetField, (ClickableWidget)this.resetBudgetButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.scan_budget")));
            this.budgetField.setEditable(this.serverConfigEditable);
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.debug_feedback")));
            this.debugModeButton = ButtonWidget.builder((Text)this.debugChatLabel(this.debugChatMode), button -> {
                this.debugChatMode = this.nextDebugChatMode(this.debugChatMode);
                button.setMessage(this.debugChatLabel(this.debugChatMode));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetDebugModeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.debugChatMode = this.defaults.debugChatMode();
                this.debugModeButton.setMessage(this.debugChatLabel(this.debugChatMode));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDebugModeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.debug_chat_mode"), (ClickableWidget)this.debugModeButton, (ClickableWidget)this.resetDebugModeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.debug_chat_mode")));
            this.debugSpinModeButton = ButtonWidget.builder((Text)this.debugSpinModeLabel(this.debugSpinMode), button -> {
                this.debugSpinMode = !this.debugSpinMode;
                button.setMessage(this.debugSpinModeLabel(this.debugSpinMode));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetDebugSpinModeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.debugSpinMode = this.defaults.isDebugSpinMode();
                this.debugSpinModeButton.setMessage(this.debugSpinModeLabel(this.debugSpinMode));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetDebugSpinModeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.debug_spin_mode"), (ClickableWidget)this.debugSpinModeButton, (ClickableWidget)this.resetDebugSpinModeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.debug_spin_mode")));
            this.convertMaleOnlyVInjectionsToAButton = ButtonWidget.builder((Text)this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA), button -> {
                this.convertMaleOnlyVInjectionsToA = !this.convertMaleOnlyVInjectionsToA;
                button.setMessage(this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetConvertMaleOnlyVInjectionsToAButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.convertMaleOnlyVInjectionsToA = this.defaults.convertMaleOnlyVInjectionsToA();
                this.convertMaleOnlyVInjectionsToAButton.setMessage(this.debugBooleanLabel(this.convertMaleOnlyVInjectionsToA));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetConvertMaleOnlyVInjectionsToAButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.convert_male_v_to_a"), (ClickableWidget)this.convertMaleOnlyVInjectionsToAButton, (ClickableWidget)this.resetConvertMaleOnlyVInjectionsToAButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.convert_male_v_to_a")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.attack_rules")));
            this.attackCreativePlayersButton = ButtonWidget.builder((Text)this.onOffLabel(this.attackCreativePlayers), button -> {
                this.attackCreativePlayers = !this.attackCreativePlayers;
                button.setMessage(this.onOffLabel(this.attackCreativePlayers));
                this.updateResetButtons();
            }).dimensions(0, 0, 100, 20).build();
            this.resetAttackCreativePlayersButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.reset_icon"), button -> {
                this.attackCreativePlayers = this.defaults.canAttackCreativePlayers();
                this.attackCreativePlayersButton.setMessage(this.onOffLabel(this.attackCreativePlayers));
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)this.resetAttackCreativePlayersButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.attack_creative_players"), (ClickableWidget)this.attackCreativePlayersButton, (ClickableWidget)this.resetAttackCreativePlayersButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.attack_creative_players")));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.textRenderer, (Text)Text.translatable((String)"config.needsofnature.section.animation_data")));
            ButtonWidget loadedAnimationsButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.loaded_animations"), button -> MinecraftClient.getInstance().setScreen((Screen)new LoadedAnimationsScreen(this))).dimensions(0, 0, 220, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)loadedAnimationsButton, "config.needsofnature.tooltip.loaded_animations");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)loadedAnimationsButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loaded_animations")));
            ButtonWidget loadOrderButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.loaded_animations.load_order"), button -> MinecraftClient.getInstance().setScreen((Screen)new LoadOrderScreen(this, this.config))).dimensions(0, 0, 220, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)loadOrderButton, "config.needsofnature.tooltip.loaded_animations.load_order");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.textRenderer, (ClickableWidget)loadOrderButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.loaded_animations.load_order")));
            this.budgetField.setChangedListener(ignored -> this.updateResetButtons());
            this.updateResetButtons();
            int centerX = this.width / 2;
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"config.needsofnature.done"), button -> this.saveAndClose()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            NonModMenuScreens.setTooltip((ClickableWidget)doneButton, "config.needsofnature.tooltip.done_save");
            this.addDrawableChild(doneButton);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        public void close() {
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
            MinecraftClient.getInstance().setScreen(this.parent);
        }

        private TextFieldWidget newNumberField(int w, int initial) {
            TextFieldWidget field = new TextFieldWidget(this.textRenderer, 0, 0, w, 20, (Text)Text.empty());
            field.setText(String.valueOf(initial));
            field.setMaxLength(8);
            field.setEditable(true);
            return field;
        }

        private int parseField(TextFieldWidget field, int fallback) {
            try {
                return Integer.parseInt(field.getText().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int clampBudget(int v) {
            return Math.max(1, Math.min(512, v));
        }

        private Text debugChatLabel(NonDebugChatMode mode) {
            NonDebugChatMode resolved = mode == null ? NonDebugChatMode.SETUP_ERRORS : mode;
            return Text.translatable((String)("config.needsofnature.debug_chat_mode." + resolved.id()));
        }

        private NonDebugChatMode nextDebugChatMode(NonDebugChatMode current) {
            if (current == null) {
                return NonDebugChatMode.SETUP_ERRORS;
            }
            return switch (current) {
                case ALL -> NonDebugChatMode.SETUP_WARNINGS_ERRORS;
                case SETUP_WARNINGS_ERRORS -> NonDebugChatMode.SETUP_ERRORS;
                case SETUP_ERRORS -> NonDebugChatMode.ERRORS_ONLY;
                case ERRORS_ONLY -> NonDebugChatMode.ALL;
            };
        }

        private Text debugSpinModeLabel(boolean enabled) {
            return Text.translatable((String)"config.needsofnature.debug_spin_mode.value", (Object[])new Object[]{Text.translatable((String)(enabled ? "options.on" : "options.off"))});
        }

        private Text debugBooleanLabel(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }

        private Text onOffLabel(boolean enabled) {
            return Text.translatable((String)(enabled ? "options.on" : "options.off"));
        }

        private void updateResetButtons() {
            if (this.resetBudgetButton != null) {
                boolean bl = this.resetBudgetButton.active = this.serverConfigEditable && this.parseField(this.budgetField, this.defaults.getScanBudgetPerTick()) != this.defaults.getScanBudgetPerTick();
            }
            if (this.resetDebugModeButton != null) {
                boolean bl = this.resetDebugModeButton.active = this.debugChatMode != this.defaults.debugChatMode();
            }
            if (this.resetDebugSpinModeButton != null) {
                boolean bl = this.resetDebugSpinModeButton.active = this.debugSpinMode != this.defaults.isDebugSpinMode();
            }
            if (this.resetConvertMaleOnlyVInjectionsToAButton != null) {
                boolean bl = this.resetConvertMaleOnlyVInjectionsToAButton.active = this.convertMaleOnlyVInjectionsToA != this.defaults.convertMaleOnlyVInjectionsToA();
            }
            if (this.resetAttackCreativePlayersButton != null) {
                boolean bl = this.resetAttackCreativePlayersButton.active = this.serverConfigEditable && this.attackCreativePlayers != this.defaults.canAttackCreativePlayers();
            }
            if (this.attackCreativePlayersButton != null) {
                this.attackCreativePlayersButton.active = this.serverConfigEditable;
            }
        }
    }
}

