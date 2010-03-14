package com.prunicki.suzuki.twinkle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;

public final class Utils {
    
    public static void addPropertyChangeListener(List<WeakReference<PropertyChangeListener>> listeners, PropertyChangeListener listener) {
        boolean add = true;
        
        for (WeakReference<PropertyChangeListener> listenerRef : listeners) {
            if (listenerRef.get() == listener) {
                add = false;
                break;
            }
        }
        
        if (add) {
            listeners.add(new WeakReference<PropertyChangeListener>(listener));
        }
    }
    
    public static void removePropertyChangeListener(List<WeakReference<PropertyChangeListener>> listeners, PropertyChangeListener listener) {
        int idx = -1;
        
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).get() == listener) {
                idx = i;
                break;
            }
        }
        
        if (idx >= 0) {
            listeners.remove(idx);
        }
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
}
