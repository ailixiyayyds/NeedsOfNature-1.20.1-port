/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_350$class_351
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_4265
 *  net.minecraft.class_4265$class_4266
 *  net.minecraft.class_437
 *  net.minecraft.class_5250
 *  net.minecraft.class_6379
 *  net.minecraft.class_7842
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
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_350;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_4265;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_6379;
import net.minecraft.class_7842;

public class AfwConfigScreen
extends class_437 {
    private static final boolean DEFAULT_FORCE_VANILLA = false;
    private static final AfwDebugChatMode DEFAULT_DEBUG_CHAT_MODE = AfwDebugChatMode.SETUP_ERRORS;
    private static final AfwDamageBehavior DEFAULT_DEBUG_DAMAGE = AfwDamageBehavior.STOP_ON_DAMAGE;
    private static final boolean DEFAULT_DEBUG_IGNORE = false;
    private static final boolean DEFAULT_ANCHOR_LAST_SELECTED = false;
    private static final int DEFAULT_BLOCK_SEARCH_RADIUS = 3;
    private static final boolean DEFAULT_AUTO_SWITCH_THIRD_PERSON = true;
    private static final int MIN_BLOCK_SEARCH_RADIUS = 1;
    private static final int MAX_BLOCK_SEARCH_RADIUS = 16;
    private final class_437 parent;
    private final boolean initialForceVanillaTextures;
    private boolean forceVanillaTextures;
    private int blockSearchRadius;
    private boolean autoSwitchThirdPerson;
    private class_4185 toggleButton;
    private class_4185 resetToggleButton;
    private class_4185 blockSearchButton;
    private class_4185 resetBlockSearchButton;
    private class_4185 autoSwitchPerspectiveButton;
    private class_4185 resetAutoSwitchPerspectiveButton;

    public AfwConfigScreen(class_437 parent) {
        super((class_2561)class_2561.method_43471((String)"config.animationframework.title"));
        int initialBlockSearchRadius;
        this.parent = parent;
        this.initialForceVanillaTextures = AfwClientConfig.get().forceVanillaEntityTextures();
        this.forceVanillaTextures = AfwClientConfig.get().forceVanillaEntityTextures();
        this.blockSearchRadius = initialBlockSearchRadius = AfwClientConfig.get().blockSearchRadius();
        this.autoSwitchThirdPerson = AfwClientConfig.get().autoSwitchThirdPersonOnAnimationStart();
    }

    protected void method_25426() {
        int listTop = 32;
        int bottomArea = 40;
        int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
        SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
        this.method_37063((class_364)settingsList);
        int resetW = 20;
        this.toggleButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
            this.forceVanillaTextures = !this.forceVanillaTextures;
            this.updateToggleLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, 120, 20).method_46431();
        this.updateToggleLabel();
        this.resetToggleButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
            this.forceVanillaTextures = false;
            this.updateToggleLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, resetW, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.force_vanilla_entity_textures"), (class_339)this.toggleButton, (class_339)this.resetToggleButton));
        this.blockSearchButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
            int next = this.blockSearchRadius + 1;
            if (next > 16) {
                next = 1;
            }
            this.blockSearchRadius = next;
            this.updateBlockSearchLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, 120, 20).method_46431();
        this.updateBlockSearchLabel();
        this.resetBlockSearchButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
            this.blockSearchRadius = 3;
            this.updateBlockSearchLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, resetW, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.block_search_radius"), (class_339)this.blockSearchButton, (class_339)this.resetBlockSearchButton));
        this.autoSwitchPerspectiveButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
            this.autoSwitchThirdPerson = !this.autoSwitchThirdPerson;
            this.updateAutoSwitchPerspectiveLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, 120, 20).method_46431();
        this.updateAutoSwitchPerspectiveLabel();
        this.resetAutoSwitchPerspectiveButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
            this.autoSwitchThirdPerson = true;
            this.updateAutoSwitchPerspectiveLabel();
            this.updateResetButtons();
        }).method_46434(0, 0, resetW, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.auto_switch_third_person"), (class_339)this.autoSwitchPerspectiveButton, (class_339)this.resetAutoSwitchPerspectiveButton));
        class_4185 debugMenuButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.open"), button -> class_310.method_1551().method_1507((class_437)new DebugConfigScreen(this))).method_46434(0, 0, 120, 20).method_46431();
        settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.debug_menu"), (class_339)debugMenuButton, null));
        int centerX = this.field_22789 / 2;
        class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.done"), button -> this.closeWithSave()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
        this.method_37063((class_364)doneButton);
        this.updateResetButtons();
    }

    public void method_25419() {
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
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateResetButtons();
        super.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
    }

    private void updateResetButtons() {
        if (this.resetToggleButton != null) {
            boolean bl = this.resetToggleButton.field_22763 = this.forceVanillaTextures;
        }
        if (this.resetBlockSearchButton != null) {
            boolean bl = this.resetBlockSearchButton.field_22763 = this.blockSearchRadius != 3;
        }
        if (this.resetAutoSwitchPerspectiveButton != null) {
            this.resetAutoSwitchPerspectiveButton.field_22763 = !this.autoSwitchThirdPerson;
        }
    }

    private void updateToggleLabel() {
        if (this.toggleButton != null) {
            this.toggleButton.method_25355((class_2561)(this.forceVanillaTextures ? class_2561.method_43471((String)"options.on") : class_2561.method_43471((String)"options.off")));
        }
    }

    private void updateBlockSearchLabel() {
        if (this.blockSearchButton != null) {
            this.blockSearchButton.method_25355((class_2561)class_2561.method_43470((String)String.valueOf(this.blockSearchRadius)));
        }
    }

    private void updateAutoSwitchPerspectiveLabel() {
        if (this.autoSwitchPerspectiveButton != null) {
            this.autoSwitchPerspectiveButton.method_25355((class_2561)(this.autoSwitchThirdPerson ? class_2561.method_43471((String)"options.on") : class_2561.method_43471((String)"options.off")));
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
    extends class_4265<RowEntry> {
        private SettingsList(class_310 client, int width, int height, int top) {
            super(client, width, height, top, 24);
            this.field_22744 = false;
        }

        private void addEntryRow(RowEntry entry) {
            super.method_25321((class_350.class_351)entry);
        }

        public int method_25322() {
            return 360;
        }

        public int method_25342() {
            return (this.method_25368() - this.method_25322()) / 2;
        }

        private static final class RowEntry
        extends class_4265.class_4266<RowEntry> {
            private final class_7842 labelWidget;
            private final class_339 primary;
            private final class_339 reset;
            private final List<class_339> widgets;

            private RowEntry(class_327 textRenderer, class_2561 label, class_339 primary, class_339 reset) {
                this.labelWidget = label == null ? null : new class_7842(0, 0, 0, 20, label, textRenderer);
                this.primary = primary;
                this.reset = reset;
                this.widgets = new ArrayList<class_339>(2);
                if (primary != null) {
                    this.widgets.add(primary);
                }
                if (reset != null) {
                    this.widgets.add(reset);
                }
            }

            static RowEntry labeledButton(class_327 textRenderer, class_2561 label, class_339 button, class_339 reset) {
                return new RowEntry(textRenderer, label, button, reset);
            }

            public void method_25343(class_332 context, int mouseX, int mouseY, boolean hovered, float delta) {
                int rowX = this.method_73380();
                int rowY = this.method_73382();
                int rowWidth = this.method_73387();
                int widgetY = rowY + 2;
                int resetW = this.reset == null ? 0 : this.reset.method_25368();
                int buttonW = this.primary == null ? 0 : this.primary.method_25368();
                int resetX = rowX + rowWidth - resetW;
                int buttonX = resetX - 6 - buttonW;
                if (this.primary != null) {
                    this.primary.method_46421(buttonX);
                    this.primary.method_46419(widgetY);
                }
                if (this.reset != null) {
                    this.reset.method_46421(resetX);
                    this.reset.method_46419(widgetY);
                }
                if (this.labelWidget != null) {
                    int labelWidth = Math.max(20, buttonX - rowX - 8);
                    this.labelWidget.method_46421(rowX + 4);
                    this.labelWidget.method_46419(rowY + 6);
                    this.labelWidget.method_25358(labelWidth);
                    this.labelWidget.method_25394(context, mouseX, mouseY, delta);
                }
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

    private static final class DebugConfigScreen
    extends class_437 {
        private final class_437 parent;
        private AfwDebugChatMode debugChatMode;
        private AfwDamageBehavior debugDamageBehavior;
        private boolean debugIgnoreAttackers;
        private boolean anchorAtLastSelected;
        private class_4185 debugChatButton;
        private class_4185 debugDamageButton;
        private class_4185 debugIgnoreButton;
        private class_4185 anchorModeButton;
        private class_4185 resetDebugChatButton;
        private class_4185 resetDebugDamageButton;
        private class_4185 resetDebugIgnoreButton;
        private class_4185 resetAnchorModeButton;

        private DebugConfigScreen(class_437 parent) {
            super((class_2561)class_2561.method_43471((String)"config.animationframework.debug_title"));
            this.parent = parent;
            this.debugChatMode = AfwClientConfig.get().debugChatMode();
            this.debugDamageBehavior = AfwClientConfig.get().debugDamageBehavior();
            this.debugIgnoreAttackers = AfwClientConfig.get().debugIgnoreAttackers();
            this.anchorAtLastSelected = AfwClientConfig.get().anchorAtLastSelected();
        }

        protected void method_25426() {
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int resetW = 20;
            this.debugChatButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
                this.debugChatMode = AfwConfigScreen.nextDebugChatMode(this.debugChatMode);
                this.updateDebugLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, 200, 20).method_46431();
            this.updateDebugLabel();
            this.resetDebugChatButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
                this.debugChatMode = DEFAULT_DEBUG_CHAT_MODE;
                this.updateDebugLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.debug_chat_mode"), (class_339)this.debugChatButton, (class_339)this.resetDebugChatButton));
            this.debugDamageButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
                this.debugDamageBehavior = AfwConfigScreen.nextDamageBehavior(this.debugDamageBehavior);
                this.updateDebugDamageLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, 160, 20).method_46431();
            this.updateDebugDamageLabel();
            this.resetDebugDamageButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
                this.debugDamageBehavior = DEFAULT_DEBUG_DAMAGE;
                this.updateDebugDamageLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.debug_damage_behavior"), (class_339)this.debugDamageButton, (class_339)this.resetDebugDamageButton));
            this.debugIgnoreButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
                this.debugIgnoreAttackers = !this.debugIgnoreAttackers;
                this.updateDebugIgnoreLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, 120, 20).method_46431();
            this.updateDebugIgnoreLabel();
            this.resetDebugIgnoreButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
                this.debugIgnoreAttackers = false;
                this.updateDebugIgnoreLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.debug_ignore_attackers"), (class_339)this.debugIgnoreButton, (class_339)this.resetDebugIgnoreButton));
            this.anchorModeButton = class_4185.method_46430((class_2561)class_2561.method_43473(), button -> {
                this.anchorAtLastSelected = !this.anchorAtLastSelected;
                this.updateAnchorLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, 140, 20).method_46431();
            this.updateAnchorLabel();
            this.resetAnchorModeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.animationframework.reset_icon"), button -> {
                this.anchorAtLastSelected = false;
                this.updateAnchorLabel();
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            settingsList.addEntryRow(SettingsList.RowEntry.labeledButton(this.field_22793, (class_2561)class_2561.method_43471((String)"config.animationframework.debug_anchor_mode"), (class_339)this.anchorModeButton, (class_339)this.resetAnchorModeButton));
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.done"), button -> this.closeWithSave()).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            this.method_37063((class_364)doneButton);
            this.updateResetButtons();
        }

        public void method_25419() {
            this.closeWithSave();
        }

        private void closeWithSave() {
            AfwClientConfig.get().setDebugChatMode(this.debugChatMode);
            AfwClientConfig.get().setDebugDamageBehavior(this.debugDamageBehavior);
            AfwClientConfig.get().setDebugIgnoreAttackers(this.debugIgnoreAttackers);
            AfwClientConfig.get().setAnchorAtLastSelected(this.anchorAtLastSelected);
            AfwClientConfig.get().save();
            AnimationFrameworkClient.sendDebugChatPreference();
            if (this.field_22787 != null) {
                this.field_22787.method_1507(this.parent);
            }
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            this.updateResetButtons();
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
        }

        private void updateResetButtons() {
            if (this.resetDebugChatButton != null) {
                boolean bl = this.resetDebugChatButton.field_22763 = this.debugChatMode != DEFAULT_DEBUG_CHAT_MODE;
            }
            if (this.resetDebugDamageButton != null) {
                boolean bl = this.resetDebugDamageButton.field_22763 = this.debugDamageBehavior != DEFAULT_DEBUG_DAMAGE;
            }
            if (this.resetDebugIgnoreButton != null) {
                boolean bl = this.resetDebugIgnoreButton.field_22763 = this.debugIgnoreAttackers;
            }
            if (this.resetAnchorModeButton != null) {
                this.resetAnchorModeButton.field_22763 = this.anchorAtLastSelected;
            }
        }

        private void updateDebugLabel() {
            if (this.debugChatButton != null) {
                this.debugChatButton.method_25355(this.debugChatModeLabel(this.debugChatMode));
            }
        }

        private void updateDebugDamageLabel() {
            if (this.debugDamageButton != null) {
                class_5250 label = switch (this.debugDamageBehavior) {
                    default -> throw new MatchException(null, null);
                    case AfwDamageBehavior.STOP_ON_DAMAGE -> class_2561.method_43471((String)"config.animationframework.debug_damage_behavior.stop_on_damage");
                    case AfwDamageBehavior.IGNORE_DAMAGE -> class_2561.method_43471((String)"config.animationframework.debug_damage_behavior.ignore_damage");
                    case AfwDamageBehavior.BLOCK_DAMAGE -> class_2561.method_43471((String)"config.animationframework.debug_damage_behavior.block_damage");
                };
                this.debugDamageButton.method_25355((class_2561)label);
            }
        }

        private void updateDebugIgnoreLabel() {
            if (this.debugIgnoreButton != null) {
                this.debugIgnoreButton.method_25355((class_2561)(this.debugIgnoreAttackers ? class_2561.method_43471((String)"options.on") : class_2561.method_43471((String)"options.off")));
            }
        }

        private void updateAnchorLabel() {
            if (this.anchorModeButton != null) {
                class_5250 label = this.anchorAtLastSelected ? class_2561.method_43471((String)"config.animationframework.debug_anchor_mode.last_selected") : class_2561.method_43471((String)"config.animationframework.debug_anchor_mode.midpoint");
                this.anchorModeButton.method_25355((class_2561)label);
            }
        }

        private class_2561 debugChatModeLabel(AfwDebugChatMode mode) {
            AfwDebugChatMode resolved = mode == null ? DEFAULT_DEBUG_CHAT_MODE : mode;
            return class_2561.method_43471((String)("config.animationframework.debug_chat_mode." + resolved.id()));
        }
    }
}

