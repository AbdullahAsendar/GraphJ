package com.asendar.model.event;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * @author Asendar
 *
 */
public abstract class AbstractChangeSupport {
	private transient PropertyChangeSupport changeSupport;
	private transient Object objectLock = new Object();
	
	private Object getObjectLock() {
		return objectLock;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		synchronized (getObjectLock()) {
			if (listener == null) {
				return;
			}
			if (changeSupport == null) {
				changeSupport = new PropertyChangeSupport(this);
			}
			changeSupport.addPropertyChangeListener(listener);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		synchronized (getObjectLock()) {
			if (listener == null || changeSupport == null) {
				return;
			}
			changeSupport.removePropertyChangeListener(listener);
		}
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		synchronized (getObjectLock()) {
			if (changeSupport == null) {
				return new PropertyChangeListener[0];
			}
			return changeSupport.getPropertyChangeListeners();
		}
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		synchronized (getObjectLock()) {
			if (listener == null) {
				return;
			}
			if (changeSupport == null) {
				changeSupport = new PropertyChangeSupport(this);
			}
			changeSupport.addPropertyChangeListener(propertyName, listener);
		}
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		synchronized (getObjectLock()) {
			if (listener == null || changeSupport == null) {
				return;
			}
			changeSupport.removePropertyChangeListener(propertyName, listener);
		}
	}

	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		synchronized (getObjectLock()) {
			if (changeSupport == null) {
				return new PropertyChangeListener[0];
			}
			return changeSupport.getPropertyChangeListeners(propertyName);
		}
	}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null || oldValue == newValue) {
			return;
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, Byte.valueOf(oldValue), Byte.valueOf(newValue));
	}

	public void firePropertyChange(String propertyName, char oldValue, char newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, new Character(oldValue), new Character(newValue));
	}

	public void firePropertyChange(String propertyName, short oldValue, short newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, Short.valueOf(oldValue), Short.valueOf(newValue));
	}

	public void firePropertyChange(String propertyName, long oldValue, long newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
	}

	public void firePropertyChange(String propertyName, float oldValue, float newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
	}

	public void firePropertyChange(String propertyName, double oldValue, double newValue) {
		if (!wouldFire(propertyName) || oldValue == newValue) {
			return;
		}
		firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (!wouldFire(propertyName) || (oldValue == newValue)
				|| (oldValue != null && newValue != null && oldValue.equals(newValue))) {
			return;
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public boolean wouldFire(String propertyName) {
		return this.changeSupport != null && this.changeSupport.hasListeners(propertyName);
	}
	
	public void firePropertyChange(String property) {
		firePropertyChange(property, true, false);
	}

}
