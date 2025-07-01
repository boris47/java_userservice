package com.developer.contactsservice.extra;

public class JavaTypes
{
	boolean _boolean = false;
	float _float = 0.4f;
	double _double = 0.7d;
	int _int = 5;
	String _string = "";
	
	public record InnerJavaTypes(
		String valueA,
		int valueB
	) {
		public void recordMethod()
		{
			
		}
	}
	
	public static void M()
	{
		var innerJavaTypes = new InnerJavaTypes("", 0);
		{
			innerJavaTypes.recordMethod();
		}
	}
}
