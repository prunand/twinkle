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
package com.prunicki.suzuki.twinkle.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class PropertyChangeUtil {
    public static void addPropertyChangeListener(List<WeakReference<PropertyChangeListener>> listeners, PropertyChangeListener listener) {
        boolean add = true;
        ArrayList<WeakReference<PropertyChangeListener>> deadListeners = null;
        
        PropertyChangeListener currentListener = null;
        for (WeakReference<PropertyChangeListener> listenerRef : listeners) {
            currentListener = listenerRef.get();
            if (currentListener == listener) {
                add = false;
                break;
            } else if (currentListener == null) {
                if (deadListeners == null) {
                    deadListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
                }
                deadListeners.add(listenerRef);
            }
        }
        
        if (deadListeners != null) {
            removeDeadListeners(listeners, deadListeners);
        }
        if (add) {
            listeners.add(new WeakReference<PropertyChangeListener>(listener));
        }
    }
    
    public static void removePropertyChangeListener(List<WeakReference<PropertyChangeListener>> listeners, PropertyChangeListener listener) {
        ArrayList<WeakReference<PropertyChangeListener>> deadListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
        
        PropertyChangeListener currentListener = null;
        for (WeakReference<PropertyChangeListener> listenerRef : listeners) {
            currentListener = listenerRef.get();
            if (currentListener == listener) {
                deadListeners.add(listenerRef);
                break;
            } else if (currentListener == null) {
                deadListeners.add(listenerRef);
            }
        }
        
        removeDeadListeners(listeners, deadListeners);
    }

    public static final void firePropertyChangeEvent(List<WeakReference<PropertyChangeListener>> listeners,
            PropertyChangeEvent event) {
        PropertyChangeListener listener = null;
        
        for (WeakReference<PropertyChangeListener> listenerRef : listeners) {
            listener = listenerRef.get();
            if (listener != null) {
                listener.propertyChange(event);
            }
        }
    }
    
    private static void removeDeadListeners(List<WeakReference<PropertyChangeListener>> listeners,
            List<WeakReference<PropertyChangeListener>> deadListeners) {
        for (WeakReference<PropertyChangeListener> listenerRef : deadListeners) {
            listeners.remove(listenerRef);
        }
    }
}
