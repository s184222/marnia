package com.marnia.util;

import java.util.UUID;

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
	
	public static final FormalField UUID_MATCH = new FormalField(UUID.class);
	
	public static final FormalField STRING_ARRAY_MATCH = new FormalField(String[].class);
	public static final FormalField INT_ARRAY_MATCH = new FormalField(int[].class);
	public static final FormalField FLOAT_ARRAY_MATCH = new FormalField(float[].class);
	public static final FormalField LONG_ARRAY_MATCH = new FormalField(long[].class);
	public static final FormalField DOUBLE_ARRAY_MATCH = new FormalField(double[].class);
	public static final FormalField BOOLEAN_ARRAY_MATCH = new FormalField(boolean[].class);
	public static final FormalField CHAR_ARRAY_MATCH = new FormalField(char[].class);
	public static final FormalField BYTE_ARRAY_MATCH = new FormalField(byte[].class);
	
	public static final FormalField UUID_ARRAY_MATCH = new FormalField(UUID[].class);
	
	public static final String LOCK = "lock";
	public static final ActualField LOCK_MATCH = new ActualField(LOCK);
	
	private SpaceHelper() {
	}
}
