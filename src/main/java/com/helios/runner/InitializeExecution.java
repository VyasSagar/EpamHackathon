package com.helios.runner;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.helios.excel.ExcelExecutionProperty;
import com.helios.slack.SlackClient;


@Test
public class InitializeExecution {
	@BeforeClass(alwaysRun = true)
	public void initializeExecutionModel() {
		System.out.println("=============================Execution Start====================================");
		if(ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equalsIgnoreCase("Yes")) SlackClient.initSlack();
		
		System.setOut(new java.io.PrintStream(System.out) {

			 

            private StackTraceElement getCallSite() {

                for (StackTraceElement e : Thread.currentThread()

                        .getStackTrace())

                    if (!e.getMethodName().equals("getStackTrace")

                            && !e.getClassName().equals(getClass().getName()))

                        return e;

                return null;

            }

 

            @Override

            public void println(String s) {

                println((Object) s);

            }

 

            @Override

            public void println(Object o) {

                StackTraceElement e = getCallSite();

                String callSite = e == null ? "??" :

//                    String.format("%s.%s(%s:%d)",

//                                  e.getClassName(),

//                                  e.getMethodName(),

//                                  e.getFileName(),

//                                  e.getLineNumber());

                String.format("(%s:%d)",

                        e.getFileName(),

                        e.getLineNumber());

                super.println(o + "\t >>>>>> " + callSite);

            }

        });

       
	}
	
	
	@Test
	public void stub() {
		//Placeholder function
	}
	
	
	
	
	
	
	
	
}
