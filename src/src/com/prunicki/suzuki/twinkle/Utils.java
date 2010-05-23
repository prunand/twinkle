package com.prunicki.suzuki.twinkle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
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
