/*
 * Copyright 2010 Andrew Prunicki
 * 
 * This file is part of Twinkle.
 * 
 * Twinkle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Twinkle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Twinkle.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.prunicki.twinkle.util;

import static com.prunicki.twinkle.Constants.TAG;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

public class ExceptionUtil {
    public static void displayException(RuntimeException e, int maxStack, Context context) {
        Log.d(TAG, "Caught runtime exception: " + e.getMessage());
        
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
