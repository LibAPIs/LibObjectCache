package com.mclarkdev.tools.libobjectcache;

import java.util.concurrent.TimeUnit;

public abstract class LibObjectCacheCachedObject {

	private final long timeCreated;
	private long timeTimeout;
	private long timeTouched;
	private double touchCount;

	public LibObjectCacheCachedObject() {

		this.timeCreated = System.currentTimeMillis();

		this.touch();
		this.setTimeout(60, TimeUnit.MINUTES);
	}

	public double getTouchCount() {
		return this.touchCount;
	}

	public long getTimeout() {
		return this.timeTimeout;
	}

	public long getTimeCreated() {
		return this.timeCreated;
	}

	public long getTimeLastSeen() {
		return this.timeTouched;
	}

	public long getTimeExpires() {
		return this.timeTouched + this.timeTimeout;
	}

	public void setTimeout(long timeoutTime, TimeUnit timeoutUnit) {
		this.timeTimeout = TimeUnit.MILLISECONDS.convert(timeoutTime, timeoutUnit);
	}

	protected void touch() {

		touchCount++;
		timeTouched = System.currentTimeMillis();
	}

	public boolean isExpired() {
		return ((timeTouched + timeTimeout) < System.currentTimeMillis());
	}
}
