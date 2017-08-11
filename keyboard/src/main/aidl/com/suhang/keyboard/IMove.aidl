// IMove.aidl
package com.suhang.keyboard;

// Declare any non-default types here with import statements

interface IMove {
    void setEdit(in boolean isEdit);
    boolean isEdit();
    void addKey(in String key);
    void destroy();
    void check(in boolean isChecked);
    void setVisible(in boolean isVisible);
}
