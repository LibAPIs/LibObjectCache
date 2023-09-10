package com.mclarkdev.tools.libobjectcache;

/**
 * LibObjectCache // LibObjectCacheCachedObject
 */
public abstract class LibObjectCacheCachedObject {

	public static final long _1S = (1000);
	public static final long _1M = (_1S * 60);
	public static final long _1H = (_1M * 60);
	public static final long _1D = (_1H * 24);

	private final long timeCreated;
	private long timeTimeout;
	private long timeTouched;
	private double touchCount;

	/**
	 * Instantiate a new instance of a CachedObject
	 */
	public LibObjectCacheCachedObject() {

		this.timeCreated = System.currentTimeMillis();

		this.touch();
		this.setTimeout(_1H);
	}

	/**
	 * Returns the number of times the object has been touched
	 * 
	 * @return number of touches
	 */
	public double getTouchCount() {
		return this.touchCount;
	}

	/**
	 * Returns the object timeout setting.
	 * 
	 * @return object timeout
	 */
	public long getTimeout() {
		return this.timeTimeout;
	}

	/**
	 * Returns the time the object was initially created.
	 * 
	 * @return time of creation
	 */
	public long getTimeCreated() {
		return this.timeCreated;
	}

	/**
	 * Returns the time the object was last touched.
	 * 
	 * @return last touched time
	 */
	public long getTimeLastSeen() {
		return this.timeTouched;
	}

	/**
	 * Returns the time at which the object expires.
	 * 
	 * @return expiration time
	 */
	public long getTimeExpires() {
		return this.timeTouched + this.timeTimeout;
	}

	/**
	 * Set the expiration timeout for the object.
	 * 
	 * @param timeout timeout time
	 */
	public void setTimeout(long timeout) {
		this.timeTimeout = timeout;
	}

	/**
	 * Update the last touched time of the object.
	 */
	protected void touch() {

		touchCount++;
		timeTouched = System.currentTimeMillis();
	}

	/**
	 * Returns true if the object is expired.
	 * 
	 * @return object expired
	 */
	public boolean isExpired() {
		return ((timeTouched + timeTimeout) < System.currentTimeMillis());
	}
}
