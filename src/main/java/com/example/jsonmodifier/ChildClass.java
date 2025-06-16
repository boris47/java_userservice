package com.example.jsonmodifier;

// Inheritance and Overriding
public final class ChildClass extends ParentClass
{
	@Override
	public void Method()
	{
		super.Method(); // Call the ParentClass method
		
		System.out.println("ChildClass");
	}
}
