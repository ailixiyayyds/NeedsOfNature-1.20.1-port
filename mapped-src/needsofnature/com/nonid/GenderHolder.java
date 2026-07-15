/*
 * Decompiled with CFR 0.152.
 */
package com.nonid;

public interface GenderHolder {
    public static final int GENDER_NONE = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_BOTH = 3;

    public int getGenderMask();

    public void setGenderMask(int var1);

    default public boolean hasGender(int mask) {
        return (this.getGenderMask() & mask) != 0;
    }
}

