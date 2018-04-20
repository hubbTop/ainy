package com.hhdb.csadmin.common.util;



public class EnumUtil {
	public static <T extends Enum<T>> boolean contains(Class<T> enumerator, String value)
	{
	    for (T c : enumerator.getEnumConstants()) {
	        if (c.name().equals(value)) {
	            return true;
	        }
	    }
	    return false;
	}

}
