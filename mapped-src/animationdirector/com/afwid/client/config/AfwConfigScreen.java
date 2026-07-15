/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.EntryListWidget$Entry
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.widget.ElementListWidget
 *  net.minecraft.client.gui.widget.ElementListWidget$Entry
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.MutableText
 *  net.minecraft.client.gui.Selectable
 *  net.minecraft.client.gui.widget.TextWidget
 */
package com.afwid.client.config;

import com.afwid.AfwDebugChatMode;
import com.afwid.AnimationFrameworkClient;
import com.afwid.api.AfwDamageBehavior;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.client.render.gecko.AfwVanillaTextureResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.TextWidget;

public class AfwConfigScreen
extends Screen {
    private static final boolean DEFAULT_FORCE_VANILLA = false;
    private static final AfwDebugChatMode DEFAULT_DEBUG_CHAT_MODE = AfwDebugChatMode.SETUP_ERRORS;
    private static final AfwDamageBehavior DEFAULT_DEBUG_DAMAGE = AfwDamageBehavior.STOP_ON_DAMAGE;
    private static final boolean DEFAULT_DEBUG_IGNORE = false;
    private static final boolean DEFAULT_ANCHOR_LAST_SELECTED = false;
    private static final int DEFAULT_BLOCK_SEARCH_RADIUS = 3;
    private static final boolean DEFAULT_AUTO_SWITCH_THIRD_PERSON = true;
    private static final int MIN_BLOCK_SEARCH_RADIUS = 1;
    private static final int MAX_BLOCK_SEARCH_RADIUS = 16;
    private final Screen parent;
    private final boolean initialForceVanillaTextures;
    private boolean forceVanillaTextures;
    private int blockSearchRadius;
    private boolean autoSwitchThirdPerson;
    private ButtonWidget toggleButton;
    private ButtonWidget resetToggleButton;
    private ButtonWidget blockSearchButton;
    private ButtonWidget resetBlockSearchButton;
    private ButtonWidget autoSwitchPerspectiveButton;
    private ButtonWidget resetAutoSwitchPerspectiveButton;

    public AfwConfigScreen(Screen parent) {
        super((Text)Text.translatable((String)"config.animationframework.title"));
        int initialBlockSearchRadius;
        this.parent = parent;
        this.initialForceVanillaTextures = AfwClientConfig.get().forceVanillaEntityTextures();
        this.forceVanillaTextures = AfwClientConfig.get().forceVanillaEntityTextures();
        this.blockSearchRadius = initialBlockSearchRadius = AfwClientConfig.get().blockSearchRadius();
        this.autoSwitchThirdPerson = AfwClientConfig.get().autoSwitchThirdPersonOnAnimationStart();
    }

    protected void init() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.height - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
        this.addDrawableChild((Element)settingsList);
        int resetW = 20;
        this.toggleButton = ButtonWidget.builder((Text)Text.empty(), button -> {
            this.forceVanillaTextures = !this.forceVanillaTextures;
            this.updateToggleLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, 120, 20).build();
        this.updateToggleLabel();
        this.resetToggleButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
            this.forceVanillaTextures = false;
            this.updateToggleLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, resetW, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.force_vanilla_entity_textures"), (ClickableWidget)this.toggleButton, (ClickableWidget)this.resetToggleButton));
        this.blockSearchButton = ButtonWidget.builder((Text)Text.empty(), button -> {
            int next = this.blockSearchRadius + 1;
            if (next > 16) {
                next = 1;
            }
            this.blockSearchRadius = next;
            this.updateBlockSearchLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, 120, 20).build();
        this.updateBlockSearchLabel();
        this.resetBlockSearchButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
            this.blockSearchRadius = 3;
            this.updateBlockSearchLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, resetW, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.block_search_radius"), (ClickableWidget)this.blockSearchButton, (ClickableWidget)this.resetBlockSearchButton));
        this.autoSwitchPerspectiveButton = ButtonWidget.builder((Text)Text.empty(), button -> {
            this.autoSwitchThirdPerson = !this.autoSwitchThirdPerson;
            this.updateAutoSwitchPerspectiveLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, 120, 20).build();
        this.updateAutoSwitchPerspectiveLabel();
        this.resetAutoSwitchPerspectiveButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
            this.autoSwitchThirdPerson = true;
            this.updateAutoSwitchPerspectiveLabel();
            this.updateResetButtons();
        }).dimensions(0, 0, resetW, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.auto_switch_third_person"), (ClickableWidget)this.autoSwitchPerspectiveButton, (ClickableWidget)this.resetAutoSwitchPerspectiveButton));
        ButtonWidget debugMenuButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.open"), button -> MinecraftClient.getInstance().setScreen((Screen)new DebugConfigScreen(this))).dimensions(0, 0, 120, 20).build();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.debug_menu"), (ClickableWidget)debugMenuButton, null));
        int centerX = this.width / 2;
        ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.done"), button -> this.closeWithSave()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
        this.addDrawableChild((Element)doneButton);
        this.updateResetButtons();
    }

    public void close() {
        this.closeWithSave();
    }

    private void closeWithSave() {
        AfwClientConfig.get().setForceVanillaEntityTextures(this.forceVanillaTextures);
        AfwClientConfig.get().setBlockSearchRadius(this.blockSearchRadius);
        AfwClientConfig.get().setAutoSwitchThirdPersonOnAnimationStart(this.autoSwitchThirdPerson);
        AfwClientConfig.get().save();
        if (this.forceVanillaTextures != this.initialForceVanillaTextures) {
            AfwVanillaTextureResolver.clearCache();
        }
        AnimationFrameworkClient.sendDebugChatPreference();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.updateResetButtons();
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    private void updateResetButtons() {
        if (this.resetToggleButton != null) {
            boolean bl = this.resetToggleButton.active = this.forceVanillaTextures;
        }
        if (this.resetBlockSearchButton != null) {
            boolean bl = this.resetBlockSearchButton.active = this.blockSearchRadius != 3;
        }
        if (this.resetAutoSwitchPerspectiveButton != null) {
            this.resetAutoSwitchPerspectiveButton.active = !this.autoSwitchThirdPerson;
        }
    }

    private void updateToggleLabel() {
        if (this.toggleButton != null) {
            this.toggleButton.setMessage((Text)(this.forceVanillaTextures ? Text.translatable((String)"options.on") : Text.translatable((String)"options.off")));
        }
    }

    private void updateBlockSearchLabel() {
        if (this.blockSearchButton != null) {
            this.blockSearchButton.setMessage((Text)Text.literal((String)String.valueOf(this.blockSearchRadius)));
        }
    }

    private void updateAutoSwitchPerspectiveLabel() {
        if (this.autoSwitchPerspectiveButton != null) {
            this.autoSwitchPerspectiveButton.setMessage((Text)(this.autoSwitchThirdPerson ? Text.translatable((String)"options.on") : Text.translatable((String)"options.off")));
        }
    }

    private static AfwDebugChatMode nextDebugChatMode(AfwDebugChatMode current) {
        if (current == null) {
            return DEFAULT_DEBUG_CHAT_MODE;
        }
        return switch (current) {
            default -> throw new MatchException(null, null);
            case AfwDebugChatMode.ALL -> AfwDebugChatMode.SETUP_WARNINGS_ERRORS;
            case AfwDebugChatMode.SETUP_WARNINGS_ERRORS -> AfwDebugChatMode.SETUP_ERRORS;
            case AfwDebugChatMode.SETUP_ERRORS -> AfwDebugChatMode.ERRORS_ONLY;
            case AfwDebugChatMode.ERRORS_ONLY -> AfwDebugChatMode.ALL;
        };
    }

    private static AfwDamageBehavior nextDamageBehavior(AfwDamageBehavior current) {
        if (current == null) {
            return AfwDamageBehavior.STOP_ON_DAMAGE;
        }
        return switch (current) {
            default -> throw new MatchException(null, null);
            case AfwDamageBehavior.STOP_ON_DAMAGE -> AfwDamageBehavior.IGNORE_DAMAGE;
            case AfwDamageBehavior.IGNORE_DAMAGE -> AfwDamageBehavior.BLOCK_DAMAGE;
            case AfwDamageBehavior.BLOCK_DAMAGE -> AfwDamageBehavior.STOP_ON_DAMAGE;
        };
    }

    private static final class SettingsList
    extends ElementListWidget<RowEntry> {
        private SettingsList(MinecraftClient client, int width, int height, int top) {
            super(client, width, height, top, 24);
            this.centerListVertically = false;
        }

        private void addEntryRow(RowEntry entry) {
            super.addEntry((EntryListWidget.Entry)entry);
        }

        public int getRowWidth() {
            return 360;
        }

        public int getRowLeft() {
            return (this.getWidth() - this.getRowWidth()) / 2;
        }

        private static final class RowEntry
        extends ElementListWidget.Entry<RowEntry> {
            private final TextWidget labelWidget;
            private final ClickableWidget primary;
            private final ClickableWidget reset;
            private final List<ClickableWidget> widgets;

            private RowEntry(TextRenderer textRenderer, Text label, ClickableWidget primary, ClickableWidget reset) {
                this.labelWidget = label == null ? null : new TextWidget(0, 0, 0, 20, label, textRenderer);
                this.primary = primary;
                this.reset = reset;
                this.widgets = new ArrayList<ClickableWidget>(2);
                if (primary != null) {
                    this.widgets.add(primary);
                }
                if (reset != null) {
                    this.widgets.add(reset);
                }
            }

            static RowEntry labeledButton(TextRenderer textRenderer, Text label, ClickableWidget button, ClickableWidget reset) {
                return new RowEntry(textRenderer, label, button, reset);
            }

            public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float delta) {
                int rowX = this.getContentX();
                int rowY = this.getContentY();
                int rowWidth = this.getContentWidth();
                int widgetY = rowY + 2;
                int resetW = this.reset == null ? 0 : this.reset.getWidth();
                int buttonW = this.primary == null ? 0 : this.primary.getWidth();
                int resetX = rowX + rowWidth - resetW;
                int buttonX = resetX - 6 - buttonW;
                if (this.primary != null) {
                    this.primary.setX(buttonX);
                    this.primary.setY(widgetY);
                }
                if (this.reset != null) {
                    this.reset.setX(resetX);
                    this.reset.setY(widgetY);
                }
                if (this.labelWidget != null) {
                    int labelWidth = Math.max(20, buttonX - rowX - 8);
                    this.labelWidget.setX(rowX + 4);
                    this.labelWidget.setY(rowY + 6);
                    this.labelWidget.setWidth(labelWidth);
                    this.labelWidget.render(context, mouseX, mouseY, delta);
                }
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

    private static final class DebugConfigScreen
    extends Screen {
        private final Screen parent;
        private AfwDebugChatMode debugChatMode;
        private AfwDamageBehavior debugDamageBehavior;
        private boolean debugIgnoreAttackers;
        private boolean anchorAtLastSelected;
        private ButtonWidget debugChatButton;
        private ButtonWidget debugDamageButton;
        private ButtonWidget debugIgnoreButton;
        private ButtonWidget anchorModeButton;
        private ButtonWidget resetDebugChatButton;
        private ButtonWidget resetDebugDamageButton;
        private ButtonWidget resetDebugIgnoreButton;
        private ButtonWidget resetAnchorModeButton;

        private DebugConfigScreen(Screen parent) {
            super((Text)Text.translatable((String)"config.animationframework.debug_title"));
            this.parent = parent;
            this.debugChatMode = AfwClientConfig.get().debugChatMode();
            this.debugDamageBehavior = AfwClientConfig.get().debugDamageBehavior();
            this.debugIgnoreAttackers = AfwClientConfig.get().debugIgnoreAttackers();
            this.anchorAtLastSelected = AfwClientConfig.get().anchorAtLastSelected();
        }

        protected void init() {
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.height - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.client, this.width, listHeight, listTop);
            this.addDrawableChild((Element)settingsList);
            int resetW = 20;
            this.debugChatButton = ButtonWidget.builder((Text)Text.empty(), button -> {
                this.debugChatMode = AfwConfigScreen.nextDebugChatMode(this.debugChatMode);
                this.updateDebugLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, 200, 20).build();
            this.updateDebugLabel();
            this.resetDebugChatButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
                this.debugChatMode = DEFAULT_DEBUG_CHAT_MODE;
                this.updateDebugLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.debug_chat_mode"), (ClickableWidget)this.debugChatButton, (ClickableWidget)this.resetDebugChatButton));
            this.debugDamageButton = ButtonWidget.builder((Text)Text.empty(), button -> {
                this.debugDamageBehavior = AfwConfigScreen.nextDamageBehavior(this.debugDamageBehavior);
                this.updateDebugDamageLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, 160, 20).build();
            this.updateDebugDamageLabel();
            this.resetDebugDamageButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
                this.debugDamageBehavior = DEFAULT_DEBUG_DAMAGE;
                this.updateDebugDamageLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.debug_damage_behavior"), (ClickableWidget)this.debugDamageButton, (ClickableWidget)this.resetDebugDamageButton));
            this.debugIgnoreButton = ButtonWidget.builder((Text)Text.empty(), button -> {
                this.debugIgnoreAttackers = !this.debugIgnoreAttackers;
                this.updateDebugIgnoreLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, 120, 20).build();
            this.updateDebugIgnoreLabel();
            this.resetDebugIgnoreButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
                this.debugIgnoreAttackers = false;
                this.updateDebugIgnoreLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.debug_ignore_attackers"), (ClickableWidget)this.debugIgnoreButton, (ClickableWidget)this.resetDebugIgnoreButton));
            this.anchorModeButton = ButtonWidget.builder((Text)Text.empty(), button -> {
                this.anchorAtLastSelected = !this.anchorAtLastSelected;
                this.updateAnchorLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, 140, 20).build();
            this.updateAnchorLabel();
            this.resetAnchorModeButton = ButtonWidget.builder((Text)Text.translatable((String)"config.animationframework.reset_icon"), button -> {
                this.anchorAtLastSelected = false;
                this.updateAnchorLabel();
                this.updateResetButtons();
            }).dimensions(0, 0, resetW, 20).build();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.textRenderer, (Text)Text.translatable((String)"config.animationframework.debug_anchor_mode"), (ClickableWidget)this.anchorModeButton, (ClickableWidget)this.resetAnchorModeButton));
            int centerX = this.width / 2;
            ButtonWidget doneButton = ButtonWidget.builder((Text)Text.translatable((String)"gui.done"), button -> this.closeWithSave()).dimensions(centerX - 100, this.height - 28, 200, 20).build();
            this.addDrawableChild((Element)doneButton);
            this.updateResetButtons();
        }

        public void close() {
            this.closeWithSave();
        }

        private void closeWithSave() {
            AfwClientConfig.get().setDebugChatMode(this.debugChatMode);
            AfwClientConfig.get().setDebugDamageBehavior(this.debugDamageBehavior);
            AfwClientConfig.get().setDebugIgnoreAttackers(this.debugIgnoreAttackers);
            AfwClientConfig.get().setAnchorAtLastSelected(this.anchorAtLastSelected);
            AfwClientConfig.get().save();
            AnimationFrameworkClient.sendDebugChatPreference();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        private void updateResetButtons() {
            if (this.resetDebugChatButton != null) {
                boolean bl = this.resetDebugChatButton.active = this.debugChatMode != DEFAULT_DEBUG_CHAT_MODE;
            }
            if (this.resetDebugDamageButton != null) {
                boolean bl = this.resetDebugDamageButton.active = this.debugDamageBehavior != DEFAULT_DEBUG_DAMAGE;
            }
            if (this.resetDebugIgnoreButton != null) {
                boolean bl = this.resetDebugIgnoreButton.active = this.debugIgnoreAttackers;
            }
            if (this.resetAnchorModeButton != null) {
                this.resetAnchorModeButton.active = this.anchorAtLastSelected;
            }
        }

        private void updateDebugLabel() {
            if (this.debugChatButton != null) {
                this.debugChatButton.setMessage(this.debugChatModeLabel(this.debugChatMode));
            }
        }

        private void updateDebugDamageLabel() {
            if (this.debugDamageButton != null) {
                MutableText label = switch (this.debugDamageBehavior) {
                    default -> throw new MatchException(null, null);
                    case AfwDamageBehavior.STOP_ON_DAMAGE -> Text.translatable((String)"config.animationframework.debug_damage_behavior.stop_on_damage");
                    case AfwDamageBehavior.IGNORE_DAMAGE -> Text.translatable((String)"config.animationframework.debug_damage_behavior.ignore_damage");
                    case AfwDamageBehavior.BLOCK_DAMAGE -> Text.translatable((String)"config.animationframework.debug_damage_behavior.block_damage");
                };
                this.debugDamageButton.setMessage((Text)label);
            }
        }

        private void updateDebugIgnoreLabel() {
            if (this.debugIgnoreButton != null) {
                this.debugIgnoreButton.setMessage((Text)(this.debugIgnoreAttackers ? Text.translatable((String)"options.on") : Text.translatable((String)"options.off")));
            }
        }

        private void updateAnchorLabel() {
            if (this.anchorModeButton != null) {
                MutableText label = this.anchorAtLastSelected ? Text.translatable((String)"config.animationframework.debug_anchor_mode.last_selected") : Text.translatable((String)"config.animationframework.debug_anchor_mode.midpoint");
                this.anchorModeButton.setMessage((Text)label);
            }
        }

        private Text debugChatModeLabel(AfwDebugChatMode mode) {
            AfwDebugChatMode resolved = mode == null ? DEFAULT_DEBUG_CHAT_MODE : mode;
            return Text.translatable((String)("config.animationframework.debug_chat_mode." + resolved.id()));
        }
    }
}

