package com.marnia.util;

import org.jspace.ActualField;
import org.jspace.FormalField;

public final class SpaceHelper {

	public static final FormalField STRING_MATCH = new FormalField(String.class);
	public static final FormalField INTEGER_MATCH = new FormalField(Integer.class);
	public static final FormalField FLOAT_MATCH = new FormalField(Float.class);
	public static final FormalField LONG_MATCH = new FormalField(Long.class);
	public static final FormalField DOUBLE_MATCH = new FormalField(Double.class);
	public static final FormalField BOOLEAN_MATCH = new FormalField(Boolean.class);
	public static final FormalField CHAR_MATCH = new FormalField(Character.class);
	public static final FormalField BYTE_MATCH = new FormalField(Byte.class);
	
	public static final String LOCK = "lock";
	public static final ActualField LOCK_MATCH = new ActualField(LOCK);
	
	private SpaceHelper() {
	}
}
