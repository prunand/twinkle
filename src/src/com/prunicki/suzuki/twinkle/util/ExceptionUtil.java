package com.prunicki.suzuki.twinkle.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.prunicki.suzuki.twinkle.Main;

public class ExceptionUtil {
    public static void displayException(RuntimeException e, int maxStack, Context context) {
        Log.d(Main.TAG, "Caught runtime exception: " + e.getMessage());
        
        AlertDialog.Builder exceptionDialogBldr = new AlertDialog.Builder(context);
        
        String exceptionName = e.getClass().getSimpleName();
        exceptionDialogBldr.setTitle(exceptionName + " Caught.");
        StringBuilder txt = new StringBuilder();
        String msg = e.getMessage();
        if (msg != null) {
            txt.append(msg);
            txt.append('\n');
        }
        
        StackTraceElement[] stackTrace = e.getStackTrace();
        int stackCnt = stackTrace.length > maxStack ? maxStack : stackTrace.length;
        for (int i = 0; i < stackCnt; i++) {
            txt.append(stackTrace[i].getClassName());
            txt.append(".");
            txt.append(stackTrace[i].getMethodName());
            txt.append("():");
            txt.append(stackTrace[i].getLineNumber());
            txt.append('\n');
        }
        
        exceptionDialogBldr.setMessage(txt);
        
        AlertDialog exceptionDialog = exceptionDialogBldr.create();
        exceptionDialog.show();
    }
}
