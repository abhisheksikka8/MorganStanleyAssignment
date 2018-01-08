package com.morgan.processor.common;

import java.util.regex.Pattern;

public class MessagePattern {
	private static String TYPE_1_PATTERN_STR = "([a-z]*)\\sat\\s([0-9]*)p";
	private static String TYPE_2_PATTERN_STR = "([0-9]*)\\ssales of\\s([a-z]*)\\sat\\s([0-9]*)p\\seach";
	private static String TYPE_3_PATTERN_STR = "([a-z]*)\\s([0-9]*)p\\sto\\seach\\ssale\\sof\\s([a-z]*)\\syou\\shave\\srecorded";
	
	public static Pattern TYPE_1 = Pattern.compile(TYPE_1_PATTERN_STR);
	public static Pattern TYPE_2 = Pattern.compile(TYPE_2_PATTERN_STR);
	public static Pattern TYPE_3 = Pattern.compile(TYPE_3_PATTERN_STR);
}
