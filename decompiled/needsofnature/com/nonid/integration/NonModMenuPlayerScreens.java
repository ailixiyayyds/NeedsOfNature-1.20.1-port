/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1011
 *  net.minecraft.class_10799
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_746
 */
package com.nonid.integration;

import com.nonid.GenderHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NeedsOfNatureClient;
import com.nonid.NonConfig;
import com.nonid.client.NonDestroyedSkinClient;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.integration.FloatValueSliderWidget;
import com.nonid.integration.NonModMenuHudConfigScreen;
import com.nonid.integration.NonModMenuIntifaceScreen;
import com.nonid.integration.NonModMenuScreens;
import com.nonid.integration.PercentSliderWidget;
import com.nonid.integration.SettingsList;
import java.io.IOException;
import java.util.Locale;
import java.util.function.DoubleFunction;
import net.minecraft.class_1011;
import net.minecraft.class_10799;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_746;

final class NonModMenuPlayerScreens {
    private NonModMenuPlayerScreens() {
    }

    static class DestroyedSkinTintPickerScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final String initialHex;
        private class_2960 skinTexture;
        private class_1011 skinImage;
        private int selectedRgb = 13803641;
        private int skinX;
        private int skinY;
        private int skinSize;
        private int selectedPixelX = -1;
        private int selectedPixelY = -1;
        private class_342 tintField;

        protected DestroyedSkinTintPickerScreen(class_437 parent, NonConfig config, String initialHex) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint.pick_title"));
            this.parent = parent;
            this.config = config;
            this.initialHex = initialHex == null ? "" : initialHex;
            this.selectedRgb = DestroyedSkinTintPickerScreen.parseHexOrDefault(this.initialHex, this.selectedRgb);
        }

        protected void method_25426() {
            this.loadSkinImage();
            this.skinSize = Math.min(320, Math.max(128, Math.min(this.field_22789 - 40, this.field_22790 - 136)));
            this.skinX = (this.field_22789 - this.skinSize) / 2;
            this.skinY = 40;
            int centerX = this.field_22789 / 2;
            this.tintField = new class_342(this.field_22793, centerX - 15, this.field_22790 - 62, 80, 20, (class_2561)class_2561.method_43473());
            this.tintField.method_1880(7);
            this.tintField.method_1852(DestroyedSkinTintPickerScreen.formatRgb(this.selectedRgb));
            this.tintField.method_1863(ignored -> this.updateSelectedRgbFromField());
            this.method_37063((class_364)this.tintField);
            class_4185 useButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint.use"), button -> {
                String pickedHex = this.sanitizeHexInput(this.tintField, DestroyedSkinTintPickerScreen.formatRgb(this.selectedRgb));
                this.selectedRgb = DestroyedSkinTintPickerScreen.parseHexOrDefault(pickedHex, this.selectedRgb);
                this.config.setDestroyedSkinTintHex(pickedHex);
                this.config.save();
                NonDestroyedSkinClient.uploadLocalSkinIfPresent();
                class_310.method_1551().method_1507(this.parent);
            }).method_46434(centerX - 155, this.field_22790 - 28, 100, 20).method_46431();
            this.method_37063((class_364)useButton);
            class_4185 autoButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint.auto"), button -> {
                this.setSelectedRgb(NonDestroyedSkinClient.detectSkinTintFromCurrentPlayer(this.selectedRgb));
                this.selectedPixelX = -1;
                this.selectedPixelY = -1;
            }).method_46434(centerX - 50, this.field_22790 - 28, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)autoButton, "config.needsofnature.tooltip.destroyed_skin_tint.auto");
            this.method_37063((class_364)autoButton);
            class_4185 cancelButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 55, this.field_22790 - 28, 100, 20).method_46431();
            this.method_37063((class_364)cancelButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 16, 0xFFFFFF);
            if (this.skinTexture != null) {
                context.method_25294(this.skinX, this.skinY, this.skinX + this.skinSize, this.skinY + this.skinSize, 0xFF000000 | this.selectedRgb);
                context.method_25302(class_10799.field_56883, this.skinTexture, this.skinX, this.skinY, 0.0f, 0.0f, this.skinSize, this.skinSize, 64, 64, 64, 64);
                context.method_25294(this.skinX - 1, this.skinY - 1, this.skinX + this.skinSize + 1, this.skinY, -1);
                context.method_25294(this.skinX - 1, this.skinY + this.skinSize, this.skinX + this.skinSize + 1, this.skinY + this.skinSize + 1, -1);
                context.method_25294(this.skinX - 1, this.skinY - 1, this.skinX, this.skinY + this.skinSize + 1, -1);
                context.method_25294(this.skinX + this.skinSize, this.skinY - 1, this.skinX + this.skinSize + 1, this.skinY + this.skinSize + 1, -1);
            } else {
                context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint.no_skin"), this.field_22789 / 2, this.skinY + this.skinSize / 2, 0xFF8080);
            }
            if (this.selectedPixelX >= 0 && this.selectedPixelY >= 0) {
                int px = this.skinX + this.selectedPixelX * this.skinSize / 64;
                int py = this.skinY + this.selectedPixelY * this.skinSize / 64;
                int cell = Math.max(1, this.skinSize / 64);
                context.method_25294(px - 1, py - 1, px + cell + 1, py, -256);
                context.method_25294(px - 1, py + cell, px + cell + 1, py + cell + 1, -256);
                context.method_25294(px - 1, py - 1, px, py + cell + 1, -256);
                context.method_25294(px + cell, py - 1, px + cell + 1, py + cell + 1, -256);
            }
            int previewY = this.field_22790 - 58;
            int previewX = this.field_22789 / 2 - 78;
            context.method_25294(previewX, previewY, previewX + 24, previewY + 24, 0xFF000000 | this.selectedRgb);
            context.method_25294(previewX - 1, previewY - 1, previewX + 25, previewY, -1);
            context.method_25294(previewX - 1, previewY + 24, previewX + 25, previewY + 25, -1);
            context.method_25294(previewX - 1, previewY - 1, previewX, previewY + 25, -1);
            context.method_25294(previewX + 24, previewY - 1, previewX + 25, previewY + 25, -1);
            context.method_27535(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint"), previewX + 34, previewY + 8, 0xFFFFFF);
        }

        public boolean method_25402(class_11909 click, boolean doubled) {
            if (this.tryPickColor(click.comp_4798(), click.comp_4799())) {
                return true;
            }
            return super.method_25402(click, doubled);
        }

        public void method_25419() {
            class_310.method_1551().method_1507(this.parent);
        }

        public void method_25432() {
            if (this.skinImage != null) {
                this.skinImage.close();
                this.skinImage = null;
            }
        }

        private void loadSkinImage() {
            class_310 client = class_310.method_1551();
            if (client == null || client.field_1724 == null) {
                return;
            }
            this.skinTexture = client.field_1724.method_52814().comp_1626().comp_3627();
            try {
                this.skinImage = NonDestroyedSkinClient.readTextureImage(client, this.skinTexture);
            }
            catch (IOException | RuntimeException e) {
                NeedsOfNature.LOGGER.warn("[NoN] Failed to read player skin for tint picker.", (Throwable)e);
                this.skinImage = null;
            }
        }

        private boolean tryPickColor(double mouseX, double mouseY) {
            if (this.skinImage == null) {
                return false;
            }
            if (mouseX < (double)this.skinX || mouseX >= (double)(this.skinX + this.skinSize) || mouseY < (double)this.skinY || mouseY >= (double)(this.skinY + this.skinSize)) {
                return false;
            }
            int pixelX = Math.max(0, Math.min(63, (int)((mouseX - (double)this.skinX) * 64.0 / (double)this.skinSize)));
            int pixelY = Math.max(0, Math.min(63, (int)((mouseY - (double)this.skinY) * 64.0 / (double)this.skinSize)));
            if (pixelX >= this.skinImage.method_4307() || pixelY >= this.skinImage.method_4323()) {
                return false;
            }
            int argb = this.skinImage.method_61940(pixelX, pixelY);
            if ((argb >>> 24 & 0xFF) < 32) {
                return true;
            }
            this.setSelectedRgb(argb & 0xFFFFFF);
            this.selectedPixelX = pixelX;
            this.selectedPixelY = pixelY;
            return true;
        }

        private void setSelectedRgb(int rgb) {
            this.selectedRgb = rgb & 0xFFFFFF;
            if (this.tintField != null) {
                this.tintField.method_1852(DestroyedSkinTintPickerScreen.formatRgb(this.selectedRgb));
            }
        }

        private void updateSelectedRgbFromField() {
            this.selectedRgb = DestroyedSkinTintPickerScreen.parseHexOrDefault(this.sanitizeHexInput(this.tintField, DestroyedSkinTintPickerScreen.formatRgb(this.selectedRgb)), this.selectedRgb);
        }

        private String sanitizeHexInput(class_342 field, String fallback) {
            String value;
            if (field == null) {
                return fallback == null ? "" : fallback;
            }
            String string = value = field.method_1882() == null ? "" : field.method_1882().trim();
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            if ((value = value.toUpperCase(Locale.ROOT)).isEmpty()) {
                return "";
            }
            return value.matches("[0-9A-F]{6}") ? value : (fallback == null ? "" : fallback);
        }

        private static int parseHexOrDefault(String hex, int fallback) {
            if (hex == null) {
                return fallback;
            }
            String value = hex.trim();
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            if (!value.matches("[0-9a-fA-F]{6}")) {
                return fallback;
            }
            try {
                return Integer.parseUnsignedInt(value, 16) & 0xFFFFFF;
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private static String formatRgb(int rgb) {
            return String.format(Locale.ROOT, "%06X", rgb & 0xFFFFFF);
        }
    }

    static class FemaleGenderDestroyedOverridesScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private ProfileFields lowFields;
        private ProfileFields highFields;

        protected FemaleGenderDestroyedOverridesScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profiles_title"));
            this.parent = parent;
            this.config = config;
        }

        protected void method_25426() {
            int listTop = 156;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            this.lowFields = new ProfileFields(this, this.config.getFemaleGenderDestroyedLow());
            this.highFields = new ProfileFields(this, this.config.getFemaleGenderDestroyedHigh());
            this.lowFields.addRows(settingsList, this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profile.low"));
            this.highFields.addRows(settingsList, this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profile.high"));
            int centerX = this.field_22789 / 2;
            class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> {
                this.saveValues();
                class_310.method_1551().method_1507(this.parent);
            }).method_46434(centerX - 100, this.field_22790 - 28, 200, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)doneButton, "config.needsofnature.tooltip.done_save");
            this.method_37063((class_364)doneButton);
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            super.method_25394(context, mouseX, mouseY, delta);
            context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 0xFFFFFF);
            this.renderPreviewPanel(context, mouseX, mouseY);
        }

        public void method_25419() {
            this.saveValues();
            class_310.method_1551().method_1507(this.parent);
        }

        private void saveValues() {
            if (this.lowFields != null) {
                this.config.setFemaleGenderDestroyedLow(this.lowFields.toProfile(this.config.getFemaleGenderDestroyedLow()));
            }
            if (this.highFields != null) {
                this.config.setFemaleGenderDestroyedHigh(this.highFields.toProfile(this.config.getFemaleGenderDestroyedHigh()));
            }
            this.config.save();
            NonWildfireGenderSync.syncDestroyedSkinOverrides(this.config);
        }

        private static class_2561 toggleText(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private void renderPreviewPanel(class_332 context, int mouseX, int mouseY) {
            int panelTop = 34;
            int panelBottom = 148;
            int panelWidth = 160;
            int gap = 14;
            int leftX = this.field_22789 / 2 - panelWidth - gap / 2;
            int rightX = this.field_22789 / 2 + gap / 2;
            this.renderSinglePreview(context, mouseX, mouseY, leftX, panelTop, panelWidth, panelBottom - panelTop, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profile.low"), this.lowFields == null ? this.config.getFemaleGenderDestroyedLow() : this.lowFields.toProfile(this.config.getFemaleGenderDestroyedLow()));
            this.renderSinglePreview(context, mouseX, mouseY, rightX, panelTop, panelWidth, panelBottom - panelTop, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profile.high"), this.highFields == null ? this.config.getFemaleGenderDestroyedHigh() : this.highFields.toProfile(this.config.getFemaleGenderDestroyedHigh()));
        }

        private void renderSinglePreview(class_332 context, int mouseX, int mouseY, int x, int y, int w, int h, class_2561 label, NonConfig.FemaleGenderDestroyedProfile profile) {
            context.method_25294(x, y, x + w, y + h, 0x66000000);
            context.method_25294(x, y, x + w, y + 1, -1996488705);
            context.method_25294(x, y + h - 1, x + w, y + h, -2013265920);
            context.method_25294(x, y, x + 1, y + h, -1996488705);
            context.method_25294(x + w - 1, y, x + w, y + h, -2013265920);
            context.method_27534(this.field_22793, label, x + w / 2, y + 6, -2039584);
            boolean rendered = NonWildfireGenderSync.renderBreastPreview(this, context, profile, x + w / 2, y + h - 12, mouseX, mouseY);
            if (!rendered) {
                context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_gender.preview_unavailable"), x + w / 2, y + h / 2, -32640);
            }
        }

        private final class ProfileFields {
            private final FloatValueSliderWidget breastSize;
            private final FloatValueSliderWidget separation;
            private final FloatValueSliderWidget height;
            private final FloatValueSliderWidget depth;
            private final FloatValueSliderWidget rotation;
            private final class_4185 breastPhysics;
            private final class_4185 dualPhysics;
            private final FloatValueSliderWidget intensity;
            private final FloatValueSliderWidget momentum;
            private boolean breastPhysicsValue;
            private boolean dualPhysicsValue;

            private ProfileFields(FemaleGenderDestroyedOverridesScreen femaleGenderDestroyedOverridesScreen, NonConfig.FemaleGenderDestroyedProfile profile) {
                this.breastSize = this.newFloatSlider(profile.breastSize(), 0.0f, 0.8f, 0.008f, value -> this.formatPercent(value * 125.0));
                this.separation = this.newFloatSlider(profile.separation(), -1.0f, 1.0f, 0.1f, value -> this.formatPlain(value * 10.0));
                this.height = this.newFloatSlider(profile.height(), -1.0f, 1.0f, 0.1f, value -> this.formatPlain(value * 10.0));
                this.depth = this.newFloatSlider(profile.depth(), -1.0f, 0.0f, 0.1f, value -> this.formatPlain(value * 10.0));
                this.rotation = this.newFloatSlider(profile.rotation(), 0.0f, 0.1f, 0.01f, value -> this.formatPlain(value * 100.0) + "\u00b0");
                this.breastPhysicsValue = profile.breastPhysics();
                this.dualPhysicsValue = profile.dualPhysics();
                this.breastPhysics = class_4185.method_46430((class_2561)FemaleGenderDestroyedOverridesScreen.toggleText(this.breastPhysicsValue), button -> {
                    this.breastPhysicsValue = !this.breastPhysicsValue;
                    button.method_25355(FemaleGenderDestroyedOverridesScreen.toggleText(this.breastPhysicsValue));
                }).method_46434(0, 0, 70, 20).method_46431();
                this.dualPhysics = class_4185.method_46430((class_2561)FemaleGenderDestroyedOverridesScreen.toggleText(this.dualPhysicsValue), button -> {
                    this.dualPhysicsValue = !this.dualPhysicsValue;
                    button.method_25355(FemaleGenderDestroyedOverridesScreen.toggleText(this.dualPhysicsValue));
                }).method_46434(0, 0, 70, 20).method_46431();
                this.intensity = this.newFloatSlider(profile.intensity(), 0.0f, 0.5f, 0.0033333334f, value -> this.formatPercent(value * 300.0));
                this.momentum = this.newFloatSlider(profile.momentum(), 0.25f, 1.0f, 0.0075f, value -> this.formatPercent((value - 0.25) / 0.75 * 100.0));
            }

            private void addRows(SettingsList settingsList, class_327 textRenderer, class_2561 title) {
                settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(textRenderer, title));
                settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(textRenderer, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_gender.customization")));
                this.addFloatRow(settingsList, textRenderer, "breast_size", this.breastSize, "0% - 100%");
                this.addFloatRow(settingsList, textRenderer, "separation", this.separation, "-10 - 10");
                this.addFloatRow(settingsList, textRenderer, "height", this.height, "-10 - 10");
                this.addFloatRow(settingsList, textRenderer, "depth", this.depth, "-10 - 0");
                this.addFloatRow(settingsList, textRenderer, "rotation", this.rotation, "0\u00b0 - 10\u00b0");
                settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(textRenderer, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_gender.physics")));
                settingsList.addEntryRow(SettingsList.RowEntry.labeledField(textRenderer, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_gender.breast_physics"), (class_339)this.breastPhysics, null, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_gender.breast_physics")));
                settingsList.addEntryRow(SettingsList.RowEntry.labeledField(textRenderer, (class_2561)class_2561.method_43471((String)"config.needsofnature.female_gender.dual_physics"), (class_339)this.dualPhysics, null, NonModMenuScreens.tooltip("config.needsofnature.tooltip.female_gender.dual_physics")));
                this.addFloatRow(settingsList, textRenderer, "intensity", this.intensity, "0% - 150%");
                this.addFloatRow(settingsList, textRenderer, "momentum", this.momentum, "0% - 100%");
            }

            private void addFloatRow(SettingsList settingsList, class_327 textRenderer, String key, FloatValueSliderWidget field, String range) {
                settingsList.addEntryRow(SettingsList.RowEntry.labeledField(textRenderer, (class_2561)class_2561.method_43471((String)("config.needsofnature.female_gender." + key)), (class_339)field, null, NonModMenuScreens.tooltip((class_2561)class_2561.method_43469((String)"config.needsofnature.tooltip.female_gender.range", (Object[])new Object[]{range}))));
            }

            private NonConfig.FemaleGenderDestroyedProfile toProfile(NonConfig.FemaleGenderDestroyedProfile fallback) {
                return new NonConfig.FemaleGenderDestroyedProfile(this.breastSize.getFloatValue(), this.separation.getFloatValue(), this.height.getFloatValue(), this.depth.getFloatValue(), this.rotation.getFloatValue(), this.breastPhysicsValue, this.dualPhysicsValue, this.intensity.getFloatValue(), this.momentum.getFloatValue());
            }

            private FloatValueSliderWidget newFloatSlider(float initial, float min, float max, float step, DoubleFunction<String> formatter) {
                return new FloatValueSliderWidget(0, 0, 160, 20, initial, min, max, step, ignored -> {}, formatter);
            }

            private String formatPercent(double value) {
                return this.formatPlain(value) + "%";
            }

            private String formatPlain(double value) {
                return String.format(Locale.ROOT, "%.0f", value);
            }
        }
    }

    static class PlayerPreferencesScreen
    extends class_437 {
        private final class_437 parent;
        private final NonConfig config;
        private final NonConfig defaults = new NonConfig();
        private NonConfig.PlayerGenderSelection playerGenderSelection;
        private boolean syncFemaleGenderModWithNonGender;
        private boolean destroyedSkinFemaleGenderOverridesEnabled;
        private boolean armorShedEffectEnabled;
        private int actionSoundVolumePercent;
        private int liquidPuddleDespawnSeconds;
        private class_4185 playerGenderButton;
        private class_4185 resetPlayerGenderButton;
        private class_4185 syncFemaleGenderModWithNonGenderButton;
        private class_4185 resetSyncFemaleGenderModWithNonGenderButton;
        private class_4185 destroyedSkinFemaleGenderOverridesButton;
        private class_4185 resetDestroyedSkinFemaleGenderOverridesButton;
        private class_4185 armorShedEffectButton;
        private class_4185 resetArmorShedEffectButton;
        private PercentSliderWidget actionSoundVolumeSlider;
        private class_4185 resetActionSoundVolumeButton;
        private class_342 liquidPuddleDespawnField;
        private class_4185 resetLiquidPuddleDespawnButton;
        private boolean playerGenderEditable;

        protected PlayerPreferencesScreen(class_437 parent, NonConfig config) {
            super((class_2561)class_2561.method_43471((String)"config.needsofnature.player_preferences_title"));
            this.parent = parent;
            this.config = config;
            this.playerGenderSelection = config.getPlayerGenderSelection();
            this.syncFemaleGenderModWithNonGender = config.isSyncFemaleGenderModWithNonGender();
            this.destroyedSkinFemaleGenderOverridesEnabled = config.isDestroyedSkinFemaleGenderOverridesEnabled();
            this.armorShedEffectEnabled = config.isArmorShedEffectEnabled();
            this.actionSoundVolumePercent = config.getActionSoundVolumePercent();
            this.liquidPuddleDespawnSeconds = config.getLiquidPuddleDespawnSeconds();
        }

        protected void method_25426() {
            if (this.liquidPuddleDespawnField != null) {
                this.liquidPuddleDespawnSeconds = this.clampLiquidPuddleDespawnSeconds(this.parseField(this.liquidPuddleDespawnField, this.liquidPuddleDespawnSeconds));
            }
            int listTop = 32;
            int bottomArea = 40;
            int listHeight = Math.max(0, this.field_22790 - listTop - bottomArea);
            SettingsList settingsList = new SettingsList(this.field_22787, this.field_22789, listHeight, listTop);
            this.method_37063((class_364)settingsList);
            int fieldWidth = 50;
            int resetW = 20;
            this.playerGenderEditable = this.config.allowPlayerGenderChangeAnytime();
            if (!this.playerGenderEditable) {
                this.playerGenderSelection = this.currentPlayerGenderSelection(this.playerGenderSelection);
            }
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.player_identity")));
            this.playerGenderButton = class_4185.method_46430((class_2561)this.playerGenderText(this.playerGenderSelection), button -> {
                this.playerGenderSelection = this.playerGenderSelection.next();
                button.method_25355(this.playerGenderText(this.playerGenderSelection));
                this.updateResetButtons();
            }).method_46434(0, 0, 140, 20).method_46431();
            this.playerGenderButton.field_22763 = this.playerGenderEditable;
            NonModMenuScreens.setTooltip((class_339)this.playerGenderButton, this.playerGenderEditable ? "config.needsofnature.tooltip.player_gender" : "config.needsofnature.tooltip.player_gender.locked");
            this.resetPlayerGenderButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.playerGenderSelection = this.defaults.getPlayerGenderSelection();
                if (this.playerGenderButton != null) {
                    this.playerGenderButton.method_25355(this.playerGenderText(this.playerGenderSelection));
                }
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetPlayerGenderButton, "config.needsofnature.tooltip.reset");
            this.resetPlayerGenderButton.field_22763 = this.playerGenderEditable;
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.player_gender"), (class_339)this.playerGenderButton, (class_339)this.resetPlayerGenderButton, NonModMenuScreens.tooltip(this.playerGenderEditable ? "config.needsofnature.tooltip.player_gender" : "config.needsofnature.tooltip.player_gender.locked")));
            if (NonWildfireGenderSync.isAvailable()) {
                this.syncFemaleGenderModWithNonGenderButton = class_4185.method_46430((class_2561)this.toggleText(this.syncFemaleGenderModWithNonGender), button -> {
                    this.syncFemaleGenderModWithNonGender = !this.syncFemaleGenderModWithNonGender;
                    button.method_25355(this.toggleText(this.syncFemaleGenderModWithNonGender));
                    this.updateResetButtons();
                }).method_46434(0, 0, 100, 20).method_46431();
                NonModMenuScreens.setTooltip((class_339)this.syncFemaleGenderModWithNonGenderButton, "config.needsofnature.tooltip.sync_female_gender_mod");
                this.resetSyncFemaleGenderModWithNonGenderButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                    this.syncFemaleGenderModWithNonGender = this.defaults.isSyncFemaleGenderModWithNonGender();
                    if (this.syncFemaleGenderModWithNonGenderButton != null) {
                        this.syncFemaleGenderModWithNonGenderButton.method_25355(this.toggleText(this.syncFemaleGenderModWithNonGender));
                    }
                    this.updateResetButtons();
                }).method_46434(0, 0, resetW, 20).method_46431();
                NonModMenuScreens.setTooltip((class_339)this.resetSyncFemaleGenderModWithNonGenderButton, "config.needsofnature.tooltip.reset");
                settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.sync_female_gender_mod"), (class_339)this.syncFemaleGenderModWithNonGenderButton, (class_339)this.resetSyncFemaleGenderModWithNonGenderButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.sync_female_gender_mod")));
                this.destroyedSkinFemaleGenderOverridesButton = class_4185.method_46430((class_2561)this.toggleText(this.destroyedSkinFemaleGenderOverridesEnabled), button -> {
                    this.destroyedSkinFemaleGenderOverridesEnabled = !this.destroyedSkinFemaleGenderOverridesEnabled;
                    button.method_25355(this.toggleText(this.destroyedSkinFemaleGenderOverridesEnabled));
                    this.updateResetButtons();
                }).method_46434(0, 0, 100, 20).method_46431();
                NonModMenuScreens.setTooltip((class_339)this.destroyedSkinFemaleGenderOverridesButton, "config.needsofnature.tooltip.destroyed_skin_female_gender_overrides");
                this.resetDestroyedSkinFemaleGenderOverridesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                    this.destroyedSkinFemaleGenderOverridesEnabled = this.defaults.isDestroyedSkinFemaleGenderOverridesEnabled();
                    if (this.destroyedSkinFemaleGenderOverridesButton != null) {
                        this.destroyedSkinFemaleGenderOverridesButton.method_25355(this.toggleText(this.destroyedSkinFemaleGenderOverridesEnabled));
                    }
                    this.updateResetButtons();
                }).method_46434(0, 0, resetW, 20).method_46431();
                NonModMenuScreens.setTooltip((class_339)this.resetDestroyedSkinFemaleGenderOverridesButton, "config.needsofnature.tooltip.reset");
                settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_overrides"), (class_339)this.destroyedSkinFemaleGenderOverridesButton, (class_339)this.resetDestroyedSkinFemaleGenderOverridesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.destroyed_skin_female_gender_overrides")));
                class_4185 destroyedSkinFemaleGenderProfilesButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_female_gender_profiles"), button -> {
                    this.config.setDestroyedSkinFemaleGenderOverridesEnabled(this.destroyedSkinFemaleGenderOverridesEnabled);
                    this.config.save();
                    class_310.method_1551().method_1507((class_437)new FemaleGenderDestroyedOverridesScreen(this, this.config));
                }).method_46434(0, 0, 220, 20).method_46431();
                NonModMenuScreens.setTooltip((class_339)destroyedSkinFemaleGenderProfilesButton, "config.needsofnature.tooltip.destroyed_skin_female_gender_profiles");
                settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)destroyedSkinFemaleGenderProfilesButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.destroyed_skin_female_gender_profiles")));
            }
            boolean destroyedSkinSystemEnabled = this.config.isDestroyedSkinSystemEnabled();
            boolean hasCustomDestroyedSkin = NonDestroyedSkinClient.hasLocalCustomDestroyedSkin();
            boolean hasDefaultDestroyedSkin = NonDestroyedSkinClient.hasCurrentDefaultDestroyedSkin();
            String destroyedSkinTintTooltip = !destroyedSkinSystemEnabled ? "config.needsofnature.tooltip.destroyed_skin_tint.disabled_system" : (hasCustomDestroyedSkin ? "config.needsofnature.tooltip.destroyed_skin_tint.custom_skin" : (hasDefaultDestroyedSkin ? "config.needsofnature.tooltip.destroyed_skin_tint.default_skin" : "config.needsofnature.tooltip.destroyed_skin_tint.pick"));
            class_4185 pickerButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.destroyed_skin_tint.pick"), button -> class_310.method_1551().method_1507((class_437)new DestroyedSkinTintPickerScreen(this, this.config, this.config.getDestroyedSkinTintHex()))).method_46434(0, 0, 220, 20).method_46431();
            pickerButton.field_22763 = destroyedSkinSystemEnabled && !hasCustomDestroyedSkin && !hasDefaultDestroyedSkin;
            NonModMenuScreens.setTooltip((class_339)pickerButton, destroyedSkinTintTooltip);
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)pickerButton, NonModMenuScreens.tooltip(destroyedSkinTintTooltip)));
            settingsList.addEntryRow(SettingsList.RowEntry.sectionHeader(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.section.local_visuals")));
            this.armorShedEffectButton = class_4185.method_46430((class_2561)this.toggleText(this.armorShedEffectEnabled), button -> {
                this.armorShedEffectEnabled = !this.armorShedEffectEnabled;
                button.method_25355(this.toggleText(this.armorShedEffectEnabled));
                this.updateResetButtons();
            }).method_46434(0, 0, 100, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.armorShedEffectButton, "config.needsofnature.tooltip.armor_shed_effect");
            this.resetArmorShedEffectButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.armorShedEffectEnabled = this.defaults.isArmorShedEffectEnabled();
                if (this.armorShedEffectButton != null) {
                    this.armorShedEffectButton.method_25355(this.toggleText(this.armorShedEffectEnabled));
                }
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetArmorShedEffectButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.armor_shed_effect"), (class_339)this.armorShedEffectButton, (class_339)this.resetArmorShedEffectButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.armor_shed_effect")));
            this.actionSoundVolumeSlider = new PercentSliderWidget(0, 0, 180, 20, this.actionSoundVolumePercent, value -> {
                this.actionSoundVolumePercent = value;
                this.updateResetButtons();
            });
            NonModMenuScreens.setTooltip((class_339)this.actionSoundVolumeSlider, "config.needsofnature.tooltip.action_sound_volume");
            this.resetActionSoundVolumeButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.actionSoundVolumePercent = this.defaults.getActionSoundVolumePercent();
                if (this.actionSoundVolumeSlider != null) {
                    this.actionSoundVolumeSlider.setPercent(this.actionSoundVolumePercent);
                }
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetActionSoundVolumeButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.action_sound_volume"), (class_339)this.actionSoundVolumeSlider, (class_339)this.resetActionSoundVolumeButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.action_sound_volume")));
            this.liquidPuddleDespawnField = this.newNumberField(fieldWidth, this.liquidPuddleDespawnSeconds);
            this.resetLiquidPuddleDespawnButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.reset_icon"), button -> {
                this.liquidPuddleDespawnField.method_1852(String.valueOf(this.defaults.getLiquidPuddleDespawnSeconds()));
                this.updateResetButtons();
            }).method_46434(0, 0, resetW, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)this.resetLiquidPuddleDespawnButton, "config.needsofnature.tooltip.reset");
            settingsList.addEntryRow(SettingsList.RowEntry.labeledField(this.field_22793, (class_2561)class_2561.method_43471((String)"config.needsofnature.liquid_puddle_despawn_seconds"), (class_339)this.liquidPuddleDespawnField, (class_339)this.resetLiquidPuddleDespawnButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.liquid_puddle_despawn_seconds")));
            this.liquidPuddleDespawnField.method_1863(ignored -> this.updateResetButtons());
            class_4185 intifaceButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.intiface"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuIntifaceScreen(this, this.config))).method_46434(0, 0, 220, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)intifaceButton, "config.needsofnature.tooltip.category.intiface");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)intifaceButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.category.intiface")));
            class_4185 interfaceButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.category.interface"), button -> class_310.method_1551().method_1507((class_437)new NonModMenuHudConfigScreen.UiConfigScreen(this, this.config))).method_46434(0, 0, 220, 20).method_46431();
            NonModMenuScreens.setTooltip((class_339)interfaceButton, "config.needsofnature.tooltip.category.interface");
            settingsList.addEntryRow(SettingsList.RowEntry.buttonWithReset(this.field_22793, (class_339)interfaceButton, NonModMenuScreens.tooltip("config.needsofnature.tooltip.category.interface")));
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
            this.liquidPuddleDespawnSeconds = this.clampLiquidPuddleDespawnSeconds(this.parseField(this.liquidPuddleDespawnField, this.liquidPuddleDespawnSeconds));
            if (this.playerGenderEditable) {
                this.config.setPlayerGenderSelection(this.playerGenderSelection);
            }
            this.config.setSyncFemaleGenderModWithNonGender(this.syncFemaleGenderModWithNonGender);
            this.config.setDestroyedSkinFemaleGenderOverridesEnabled(this.destroyedSkinFemaleGenderOverridesEnabled);
            this.config.setArmorShedEffectEnabled(this.armorShedEffectEnabled);
            this.config.setActionSoundVolumePercent(this.actionSoundVolumePercent);
            this.config.setLiquidPuddleDespawnSeconds(this.liquidPuddleDespawnSeconds);
            this.config.save();
            if (this.playerGenderEditable) {
                NeedsOfNatureClient.pushConfiguredPlayerGenderToServer(this.config);
            }
            NonWildfireGenderSync.syncDestroyedSkinOverrides(this.config);
            class_310.method_1551().method_1507(this.parent);
        }

        private class_2561 playerGenderText(NonConfig.PlayerGenderSelection selection) {
            NonConfig.PlayerGenderSelection resolved = selection == null ? NonConfig.PlayerGenderSelection.FEMALE : selection;
            return switch (resolved) {
                default -> throw new MatchException(null, null);
                case NonConfig.PlayerGenderSelection.MALE -> class_2561.method_43471((String)"config.needsofnature.player_gender.male");
                case NonConfig.PlayerGenderSelection.FEMALE -> class_2561.method_43471((String)"config.needsofnature.player_gender.female");
                case NonConfig.PlayerGenderSelection.BOTH -> class_2561.method_43471((String)"config.needsofnature.player_gender.both");
            };
        }

        private class_2561 toggleText(boolean enabled) {
            return class_2561.method_43471((String)(enabled ? "options.on" : "options.off"));
        }

        private NonConfig.PlayerGenderSelection currentPlayerGenderSelection(NonConfig.PlayerGenderSelection fallback) {
            GenderHolder holder;
            int mask;
            class_746 class_7462;
            class_310 client = class_310.method_1551();
            if (client != null && (class_7462 = client.field_1724) instanceof GenderHolder && (mask = (holder = (GenderHolder)class_7462).getGenderMask() & 3) != 0) {
                return NonConfig.PlayerGenderSelection.fromMask(mask, fallback);
            }
            return fallback == null ? NonConfig.PlayerGenderSelection.FEMALE : fallback;
        }

        private class_342 newNumberField(int w, int initial) {
            class_342 field = new class_342(this.field_22793, 0, 0, w, 20, (class_2561)class_2561.method_43473());
            field.method_1852(String.valueOf(initial));
            field.method_1880(8);
            field.method_1888(true);
            return field;
        }

        private int parseField(class_342 field, int fallback) {
            if (field == null) {
                return fallback;
            }
            try {
                return Integer.parseInt(field.method_1882().trim());
            }
            catch (NumberFormatException e) {
                return fallback;
            }
        }

        private int clampLiquidPuddleDespawnSeconds(int v) {
            return Math.max(1, Math.min(300, v));
        }

        private void updateResetButtons() {
            if (this.resetPlayerGenderButton != null) {
                boolean bl = this.resetPlayerGenderButton.field_22763 = this.playerGenderEditable && this.playerGenderSelection != this.defaults.getPlayerGenderSelection();
            }
            if (this.playerGenderButton != null) {
                this.playerGenderButton.field_22763 = this.playerGenderEditable;
            }
            if (this.resetSyncFemaleGenderModWithNonGenderButton != null) {
                boolean bl = this.resetSyncFemaleGenderModWithNonGenderButton.field_22763 = this.syncFemaleGenderModWithNonGender != this.defaults.isSyncFemaleGenderModWithNonGender();
            }
            if (this.resetDestroyedSkinFemaleGenderOverridesButton != null) {
                boolean bl = this.resetDestroyedSkinFemaleGenderOverridesButton.field_22763 = this.destroyedSkinFemaleGenderOverridesEnabled != this.defaults.isDestroyedSkinFemaleGenderOverridesEnabled();
            }
            if (this.resetArmorShedEffectButton != null) {
                boolean bl = this.resetArmorShedEffectButton.field_22763 = this.armorShedEffectEnabled != this.defaults.isArmorShedEffectEnabled();
            }
            if (this.resetActionSoundVolumeButton != null) {
                boolean bl = this.resetActionSoundVolumeButton.field_22763 = this.actionSoundVolumePercent != this.defaults.getActionSoundVolumePercent();
            }
            if (this.resetLiquidPuddleDespawnButton != null) {
                this.resetLiquidPuddleDespawnButton.field_22763 = this.parseField(this.liquidPuddleDespawnField, this.defaults.getLiquidPuddleDespawnSeconds()) != this.defaults.getLiquidPuddleDespawnSeconds();
            }
        }
    }
}

