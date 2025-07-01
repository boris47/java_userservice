package com.developer.contactsservice.extra;


public abstract class ClassInheritance
{
	protected void Method()
	{
		System.out.println("ParentClass");
	}
	
	
	// Inheritance and Overriding
	public final class ChildClass extends ClassInheritance
	{
		@Override
		public void Method()
		{
			super.Method(); // OPT: Call the ParentClass method
			
			System.out.println("ChildClass");
		}
	}
	
	
	public class GenericParentClassNoAbstract<T>
	{
		protected final T Data;
		
		protected GenericParentClassNoAbstract(T InData)
		{
			Data = InData;
		}
		
		protected void Method()
		{
			System.out.println("GenericParentClass" + Data);
		}
		
		public class InnerClass
		{
			
		}
	}
	
	// Inheritance and Overriding
	public final class GenericChildClass extends GenericParentClassNoAbstract<String>
	{
		protected GenericChildClass(String InData)
		{
			super(InData);
		}

		@Override
		public void Method()
		{
			super.Method(); // OPT: Call the GenericParentClass method
			
			System.out.println("GenericChildClass");
		}
	}
	
	private class OuterClass
	{
		String outerField = "Outer field";
    	static String staticOuterField = "Static outer field";
		
		public class InnerClass
		{
			public final void accessMembers()
			{
				System.out.println(outerField);
				System.out.println(staticOuterField);
			}
		}
		
		static class StaticNestedClass
		{
			void accessMembers(OuterClass outer)
			{
				// Compiler error: Cannot make a static reference to the non-static
				//     field outerField
				// System.out.println(outerField);
				System.out.println(outer.outerField);
				System.out.println(staticOuterField);
			}
		}
	}
	
	public class ShadowTest
	{
		public int x = 0;
		
		void Method()
		{
			System.out.println("this.x = " + this.x); // -> 0
			System.out.println("ShadowTest.this.x = " + ShadowTest.this.x); // -> 0
		}
		
		class FirstLevel
		{
			public int x = 1;
			
			void Method()
			{
				System.out.println("this.x = " + this.x); // -> 1
				System.out.println("ShadowTest.this.x = " + ShadowTest.this.x); // -> 0
			}
		}
		
	}
	
	private final class ClassPlayground
	{
		private ClassPlayground()
		{
			// Ref: https://projectlombok.org/features/val
			var outer = new OuterClass();
			var inner = outer.new InnerClass();
			
			var genNoAbs = new GenericParentClassNoAbstract<String>("");
			// GenericChildClass not nested in GenericParentClassNoAbs
			var genChild = new GenericChildClass("");
			
			// InnerClass is nested in GenericParentClassNoAbs
			// C# style: Cannot allocate the member type ... using a parameterized compound name;
			//var innerr = new GenericParentClassNoAbs<String>.InnerClass();
			var innerr = genNoAbs.new InnerClass();
			var innerrr = genChild.new InnerClass();
		}
	}
}