/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 */
package com.nonid.integration;

import com.nonid.NonConfig;
import com.nonid.client.intiface.NonIntifaceDependencyManager;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

final class NonModMenuIntifaceInstallScreen
extends class_437 {
    private final class_437 parent;
    private final NonConfig config;
    private final boolean enableAfterInstall;
    private class_4185 downloadButton;

    NonModMenuIntifaceInstallScreen(class_437 parent, NonConfig config, boolean enableAfterInstall) {
        super((class_2561)class_2561.method_43471((String)"config.needsofnature.intiface.libs.install_title"));
        this.parent = parent;
        this.config = config;
        this.enableAfterInstall = enableAfterInstall;
    }

    protected void method_25426() {
        int centerX = this.field_22789 / 2;
        this.downloadButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)(NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.download")), button -> this.startInstall()).method_46434(centerX - 155, this.field_22790 - 52, 150, 20).method_46431();
        this.method_37063((class_364)this.downloadButton);
        class_4185 cancelButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.cancel"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX + 5, this.field_22790 - 52, 150, 20).method_46431();
        this.method_37063((class_364)cancelButton);
        class_4185 doneButton = class_4185.method_46430((class_2561)class_2561.method_43471((String)"config.needsofnature.done"), button -> class_310.method_1551().method_1507(this.parent)).method_46434(centerX - 75, this.field_22790 - 28, 150, 20).method_46431();
        this.method_37063((class_364)doneButton);
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 28, 0xFFFFFF);
        int y = 58;
        this.drawCentered(context, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface.libs.install_line_1"), y);
        this.drawCentered(context, (class_2561)class_2561.method_43469((String)"config.needsofnature.intiface.libs.install_line_2", (Object[])new Object[]{NonIntifaceDependencyManager.totalSizeMbText()}), y += 14);
        this.drawCentered(context, (class_2561)class_2561.method_43471((String)"config.needsofnature.intiface.libs.install_line_3"), y += 14);
        this.drawCentered(context, NonIntifaceDependencyManager.statusText(), y += 22);
        if (this.downloadButton != null) {
            this.downloadButton.method_25355((class_2561)class_2561.method_43471((String)(NonIntifaceDependencyManager.generatedBundleOutdated() ? "config.needsofnature.intiface.libs.redownload" : "config.needsofnature.intiface.libs.download")));
            this.downloadButton.field_22763 = !NonIntifaceDependencyManager.installing() && !NonIntifaceDependencyManager.restartRequired() && NonIntifaceDependencyManager.installRequired();
        }
    }

    public void method_25419() {
        class_310.method_1551().method_1507(this.parent);
    }

    private void startInstall() {
        if (this.downloadButton != null) {
            this.downloadButton.field_22763 = false;
        }
        NonIntifaceDependencyManager.installAsync(() -> {
            if (this.enableAfterInstall && NonIntifaceDependencyManager.restartRequired()) {
                this.config.setIntifaceEnabled(true);
                this.config.save();
            }
        });
    }

    private void drawCentered(class_332 context, class_2561 text, int y) {
        context.method_27534(this.field_22793, text, this.field_22789 / 2, y, -2039584);
    }
}

