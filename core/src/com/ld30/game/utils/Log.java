package com.ld30.game.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BooleanArray;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongArray;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public abstract class Log {
	
	private static boolean enabled = true;
	private static boolean disallow = false;
	private static String newLine = System.getProperty("line.separator");
	
	public static final void setEnabled(final boolean enabled) {
		Log.enabled = enabled;
	}
	
	public static final boolean isEnabled() {
		return enabled;
	}
	
	public static final void setDisallow(final boolean disallow) {
		Log.disallow = disallow;
	}
	
	public static final boolean isDisallowed() {
		return disallow;
	}
	
	private static final void printObject(final Object object,
										  final int depth) {
		
		if(object == null) {
			System.out.print("null");
			return;
		}
		
		System.out.print(ClassReflection.getSimpleName(object.getClass()));
		System.out.print(" Instance {");
		System.out.print(newLine);
		
		Class nextClass = object.getClass();
		while(nextClass != Object.class) {
			final Field[] fields = ClassReflection.getDeclaredFields(nextClass);
			
			for(final Field field : fields) {
				if(field.isTransient() || field.isStatic() || field.isSynthetic()) {
					continue;
				}
				
				try {
					if(!field.isAccessible()) {
						field.setAccessible(true);
					}
	
					System.out.print(field.getName());
					System.out.print(": ");
					
					final Object value = field.get(object);
					final Class type = value.getClass();
					
					if(type.isPrimitive() || 
					   type == String.class || 
					   type == Integer.class || 
					   type == Boolean.class || 
					   type == Float.class || 
					   type == Long.class || 
					   type == Double.class || 
					   type == Short.class || 
					   type == Byte.class || 
					   type == Character.class ||
					   depth == 0) {
						
						System.out.print(value);
						System.out.print(newLine);
					}
					else if(type.isArray() ||
							value instanceof IntArray ||
							value instanceof ByteArray ||
							value instanceof LongArray ||
							value instanceof Array ||
							value instanceof ShortArray ||
							value instanceof FloatArray ||
							value instanceof CharArray ||
							value instanceof BooleanArray) {
						
						printArray(value, depth - 1);
					} else {
						printObject(value, depth - 1);
					}
				} catch(final ReflectionException ex ) {
					System.out.println(ex);
				}
			}
			
			nextClass = nextClass.getSuperclass();
		}
		
		System.out.print("}");
		System.out.print(newLine);
	}
	
	public static void traceObject(final Object location, 
								   final Object object) {
		
		traceObject(location, object, 0);
	}
	
	public static void traceObject(final Object location, 
								   final Object object,
								   final int depth) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		printObject(object, depth);
	}
	
	private static void printArrayChildren(final Object array, 
										   final Class elementType,
										   final int depth) {
		
		final int length = ArrayReflection.getLength(array);
		
		System.out.print("Array Length: ");
		System.out.print(length);
		System.out.print(newLine);
		
		if(elementType.isPrimitive() || 
		   elementType == String.class || 
		   elementType == Integer.class || 
		   elementType == Boolean.class || 
		   elementType == Float.class || 
		   elementType == Long.class || 
		   elementType == Double.class || 
		   elementType == Short.class || 
		   elementType == Byte.class || 
		   elementType == Character.class ||
		   depth == 0) {
			
			for(int i = 0; i < length; i++) {
				System.out.print(ArrayReflection.get(array, i));
				System.out.print(newLine);
			}
		}
		else if(elementType.isArray() ||
				ClassReflection.isAssignableFrom(elementType, IntArray.class) ||
				ClassReflection.isAssignableFrom(elementType, CharArray.class) ||
				ClassReflection.isAssignableFrom(elementType, FloatArray.class) ||
				ClassReflection.isAssignableFrom(elementType, BooleanArray.class) ||
				ClassReflection.isAssignableFrom(elementType, LongArray.class) ||
				ClassReflection.isAssignableFrom(elementType, ShortArray.class) ||
				ClassReflection.isAssignableFrom(elementType, ByteArray.class) ||
				ClassReflection.isAssignableFrom(elementType, Array.class)) {
			
			for(int i = 0; i < length; i++) {
				printArray(ArrayReflection.get(array, i), depth - 1);
			}
		} else {
			for(int i = 0; i < length; i++) {
				printObject(ArrayReflection.get(array, i), depth - 1);
			}
		}
	}
	
	private static void printArrayChildren(final Array array, 
										   final Class elementType,
										   final int depth) {
		
		final int length = array.size;
		
		System.out.print("Array Length: ");
		System.out.print(length);
		System.out.print(newLine);
		
		if(elementType.isPrimitive() || 
		   elementType == String.class || 
		   elementType == Integer.class || 
		   elementType == Boolean.class || 
		   elementType == Float.class || 
		   elementType == Long.class || 
		   elementType == Double.class || 
		   elementType == Short.class || 
		   elementType == Byte.class || 
		   elementType == Character.class ||
		   depth == 0) {
			
			for(int i = 0; i < length; i++) {
				System.out.print(array.get(i));
				System.out.print(newLine);
			}
		}
		else if(elementType.isArray() ||
				ClassReflection.isAssignableFrom(elementType, IntArray.class) ||
				ClassReflection.isAssignableFrom(elementType, CharArray.class) ||
				ClassReflection.isAssignableFrom(elementType, FloatArray.class) ||
				ClassReflection.isAssignableFrom(elementType, BooleanArray.class) ||
				ClassReflection.isAssignableFrom(elementType, LongArray.class) ||
				ClassReflection.isAssignableFrom(elementType, ShortArray.class) ||
				ClassReflection.isAssignableFrom(elementType, ByteArray.class) ||
				ClassReflection.isAssignableFrom(elementType, Array.class)) {
			
			for(int i = 0; i < length; i++) {
				printArray(ArrayReflection.get(array, i), depth - 1);
			}
		} else {
			for(int i = 0; i < length; i++) {
				printObject(ArrayReflection.get(array, i), depth - 1);
			}
		}
	}
	
	private static void printArray(final Object array,
								   final int depth) {
		
		final Class type = array.getClass();
		if(type.isArray()) {
			printArrayChildren(array, type.getComponentType(), depth - 1);
		}
		else if(array instanceof IntArray) {
			final IntArray arr = (IntArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof ByteArray) {
			final ByteArray arr = (ByteArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof LongArray) {
			final LongArray arr = (LongArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof Array) {
			final Array arr = (Array)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			if(arr.size > 0) {
				printArrayChildren(arr, arr.get(0).getClass(), depth - 1);
			}
		}
		else if(array instanceof ShortArray) {
			final ShortArray arr = (ShortArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof FloatArray) {
			final FloatArray arr = (FloatArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof CharArray) {
			final CharArray arr = (CharArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
		else if(array instanceof BooleanArray) {
			final BooleanArray arr = (BooleanArray)array;
			
			System.out.print("Array Length: ");
			System.out.print(arr.size);
			System.out.print(newLine);
			
			for(int i = 0, n = arr.size; i < n; ++i) {
				System.out.print(arr.get(i));
				System.out.print(newLine);
			}
		}
	}
	
	public static void traceArray(final Object location, 
								  final Object array) {
		
		traceArray(location, array, 0);
	}
	
	public static void traceArray(final Object location, 
								  final Object array,
								  final int depth) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		printArray(array, depth);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location) {
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.println(location.toString());
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
			   		  			   final Object print1, 
			   		  			   final Object print2) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
			   		  			   final Object print1, 
			   		  			   final Object print2,
			   		  			   final Object print3) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5,
					  			   final Object print6) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(' ');
		System.out.print(print6);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5,
					  			   final Object print6,
					  			   final Object print7) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(' ');
		System.out.print(print6);
		System.out.print(' ');
		System.out.print(print7);
		System.out.print(newLine);
	}
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5,
					  			   final Object print6,
					  			   final Object print7,
					  			   final Object print8) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(' ');
		System.out.print(print6);
		System.out.print(' ');
		System.out.print(print7);
		System.out.print(' ');
		System.out.print(print8);
		System.out.print(newLine);
	}
	
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5,
					  			   final Object print6,
					  			   final Object print7,
					  			   final Object print8,
					  			   final Object print9) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(' ');
		System.out.print(print6);
		System.out.print(' ');
		System.out.print(print7);
		System.out.print(' ');
		System.out.print(print8);
		System.out.print(' ');
		System.out.print(print9);
		System.out.print(newLine);
	}
	
	
	public static final void trace(final Object location, 
					  			   final Object print1, 
					  			   final Object print2,
					  			   final Object print3,
					  			   final Object print4,
					  			   final Object print5,
					  			   final Object print6,
					  			   final Object print7,
					  			   final Object print8,
					  			   final Object print9,
					  			   final Object print10) {
		
		if(!enabled) {
			if(disallow) {
				throw new RuntimeException("Logs are disallowed!");
			}
			return;
		}
		
		System.out.print('@');
		System.out.print(location.toString());
		System.out.print(" | ");
		System.out.print(print1);
		System.out.print(' ');
		System.out.print(print2);
		System.out.print(' ');
		System.out.print(print3);
		System.out.print(' ');
		System.out.print(print4);
		System.out.print(' ');
		System.out.print(print5);
		System.out.print(' ');
		System.out.print(print6);
		System.out.print(' ');
		System.out.print(print7);
		System.out.print(' ');
		System.out.print(print8);
		System.out.print(' ');
		System.out.print(print9);
		System.out.print(' ');
		System.out.print(print10);
		System.out.print(newLine);
	}
	
	
	private Log() {}

}
