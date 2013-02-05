/**
 * Flexmojos is a set of maven goals to allow maven users to compile,
 * optimize and test Flex SWF, Flex SWC, Air SWF and Air SWC.
 * Copyright (C) 2008-2012  Marvin Froeder <marvin@flexmojos.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.flexmojos.oss.unitestingsupport
{
	import flash.events.DataEvent;
	import flash.events.Event;
	import flash.net.XMLSocket;
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	import flash.utils.getTimer;

	import net.flexmojos.oss.test.monitor.CommConstraints;
	import net.flexmojos.oss.test.report.ErrorReport;
	import net.flexmojos.oss.test.report.TestCaseReport;
	import net.flexmojos.oss.test.report.TestMethodReport;

	public class SocketReporter
	{

		[Inspectable]
		public var port:uint=1024;

		[Inspectable]
		public var server:String="127.0.0.1";

		private var socket:XMLSocket;

		private var reports:Dictionary=new Dictionary();

		/**
		 * TestMethodReport -> milliseconds
		 */
		private var testTimes:Dictionary = new Dictionary();

		private var closeController:CloseController=CloseController.getInstance();

		public function SocketReporter()
		{
			super();
		}

		/**
		 * Called when an error occurs.
		 * @param test the Test that generated the error.
		 * @param error the Error.
		 */
		public function addError(testName:String, methodName:String, error:ErrorReport):void
		{
			// Increment error count.
			var report:TestCaseReport=getReport(testName);
			report.errors++;

			// Add the error to the method.
			var methodObject:TestMethodReport=report.getMethod(methodName);
			methodObject.error=error;
		}

		/**
		 * Add the currently executing method on a Test to the internal report
		 * model.
		 * @param test the Test.
		 */
		public function addMethod(testName:String, methodName:String):void
		{
			var reportObject:TestCaseReport=getReport(testName);
			testTimes[reportObject.getMethod(methodName)] = getTimer();
			reportObject.tests++;
		}

		/**
		 * Called when a failure occurs.
		 * @param test the Test that generated the failure.
		 * @param error the failure.
		 */
		public function addFailure(testName:String, methodName:String, failure:ErrorReport):void
		{
			// Increment failure count.
			var report:TestCaseReport=getReport(testName);
			report.failures++;

			// Add the failure to the method.
			var methodObject:TestMethodReport=report.getMethod(methodName);
			methodObject.failure=failure;
		}

		public function testFinished(testName:String, methodName:String=null, timeTaken:int=0):void
		{
			var reportObject:TestCaseReport=reports[testName];
			reportObject.time=timeTaken;
			
			if( methodName && !timeTaken )
			{
				var methodObject:TestMethodReport = reportObject.getMethod(methodName);
				methodObject.time = (getTimer() - int(testTimes[methodObject]))/1000.0;
			}
		}

		/**
		 * Return the report Object from the internal report model for the
		 * currently executing Test.
		 * @param Test the test.
		 */
		public function getReport(testName:String):TestCaseReport
		{
			var reportObject:TestCaseReport;

			// Check we have a report Object for the executing Test, if not
			// create a new one.
			if (reports[testName])
			{
				reportObject=reports[testName];
			}
			else
			{
				reportObject=new TestCaseReport();
				reportObject.name=testName;

				reports[testName]=reportObject;
			}

			return reportObject;
		}

		/**
		 * Sends the results. This sends the reports back to the controlling Ant
		 * task using an XMLSocket.
		 */
		public function sendResults():void
		{
			// Open an XML socket.
			socket=new XMLSocket();
			socket.addEventListener(Event.CONNECT, handleConnect);
			socket.addEventListener(DataEvent.DATA, dataHandler);
			socket.connect(server, port);
		}

		private function handleConnect(event:Event):void
		{
			for (var className:String in reports)
			{
				var testReport:TestCaseReport=reports[className];
				// Create the XML report.
				var xmlString:String=testReport.toXml();

				// Send the XML report.
				socket.send(xmlString);
			}

			// Send the end of reports terminator.
			socket.send(CommConstraints.END_OF_TEST_RUN);
		}

		/**
		 * Event listener to handle data received on the socket.
		 * @param event the DataEvent.
		 */
		private function dataHandler(event:DataEvent):void
		{
			var data:String=event.data;

			// If we received an acknowledgement finish-up.			
			if (data == CommConstraints.ACK_OF_TEST_RESULT)
			{
				exit();
			}
		}

		/**
		 * Exit the test runner and enabling to close the player.
		 */
		private function exit():void
		{
			// Close the socket.
			if (socket)
			{
				socket.close();
			}

			// Enabling to close flashplayer
			closeController.canClose=true;
		}

		public function runTests(testApplication:ITestApplication):void
		{
			var def:Class;

			//flexunit supported
			if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.flexunit.FlexUnitListener")) != null)
			{
				trace("Running tests using Flexunit");
			}

			//flexunit4 supported
			else if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.flexunit4.FlexUnit4Listener")) != null)
			{
				trace("Running tests using Flexunit4");
			}

			//funit supported			
			else if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.funit.FUnitListener")) != null)
			{
				trace("Running tests using FUnit");
			}

			//fluint supported
			else if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.fluint.FluintListener")) != null)
			{
				trace("Running tests using Fluint");
			}

			//asunit supported
			else if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.asunit.AsUnitListener")) != null)
			{
				trace("Running tests using asunit");
			}

			//advancedflex supported
			else if ((def=tryGetDefinitionByName("net.flexmojos.oss.unitestingsupport.advancedflex.AdvancedFlexListener")) != null)
			{
				trace("Running tests using Advanced Flex tests");
			}

			//not found
			else
			{
				trace("No test runner found, exiting");
				exit();
			}

			var runner:UnitTestRunner=new def();
			runner.socketReporter=this;
			var totalTestFunctionCount:int=runner.run(testApplication);
			trace("Running " + totalTestFunctionCount + " test functions");

			if (totalTestFunctionCount == 0)
			{
				trace("No tests to run, exiting");
				exit();
			}
		}

		private static function tryGetDefinitionByName(classname:String):Class
		{
			try
			{
				return getDefinitionByName(classname) as Class;
			}
			catch (e:ReferenceError)
			{
			}
			return null;
		}

		private static var instance:SocketReporter;

		public static function getInstance():SocketReporter
		{
			if (instance == null)
			{
				instance=new SocketReporter();
			}
			return instance;
		}

	}
}