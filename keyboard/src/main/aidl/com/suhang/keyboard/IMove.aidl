// IMove.aidl
package com.suhang.keyboard;
import com.suhang.keyboard.IShowKeyboard;

// Declare any non-default types here with import statements

interface IMove {
    void setEdit(in boolean isEdit);
    boolean isEdit();
    void setOnShowListener(in IShowKeyboard listener);
    void addKey(in String key);
}
