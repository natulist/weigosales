package com.weigo.base.http;



public abstract class SyncProvider<T> implements JsonProvider {
	private volatile boolean mResultSetted;
	private T mResultEvent;

	
	public synchronized void notifyEvent(T event) {
		mResultEvent = event;
		mResultSetted = true;
		notify();
	}
	
	public synchronized T waitForResult() {
		while (!mResultSetted) {
			try {
				wait(100);
			} catch (InterruptedException e) {
				break;
			}
		}
		
		return mResultEvent;
	}
}
