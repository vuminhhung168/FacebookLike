import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

//IMPORTANT NOTE:
/* Parameters need to be provided values:
 * urlUser: personal URL of expected user
 */

//===================================================================================================================

//REQUIREMENT INFOR:
//The URL of user you want to take action:
String urlUser = 'https://www.facebook.com/lionkingneverlose/'

//===================================================================================================================

//GET DATA FROM FILE/DATABASE:
String fileTestData = 'Data Files/Data_Sheet1'

int totalRows = findTestData(fileTestData).getRowNumbers()

int columnIDUsername = 2
int columnIDPassword = 3

println("The total users is: " + totalRows)

//===================================================================================================================

//DEFINITION: ARRAYS USED TO STORE DATA:
def arrayUsername = []
def arrayPassword = []

//===================================================================================================================

//DEFINITION: array to store result:
def arrayResult = []

//===================================================================================================================

//GET DATA AND STORE:
for (int i = 0; i < totalRows; i++) {
	arrayUsername.add(findTestData(fileTestData).getValue(columnIDUsername, i + 1))
	arrayPassword.add(findTestData(fileTestData).getValue(columnIDPassword, i + 1))
}

//===================================================================================================================

//PREREQUISITE STEPS:
WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)

//===================================================================================================================

for (int i = 0; i < totalRows; i++) {
	
//Provide credential and click login:
	//If user is locked, skip:
	if ((i + 1) == 0) {
		
		println("THE USER " + arrayUsername[i] + " IS LOCKED AND WILL BE SKIPPED !!!")
		
		continue
	}
	
	println('LOGIN BY USER: ' + (arrayUsername[i]) + "\nTHE ID IN DATA FILE IS = " + (i + 1) )
	
	WebUI.setText(findTestObject('Facebook/Page_Login/input_Username'), arrayUsername[i])
	WebUI.setText(findTestObject('Facebook/Page_Login/input_Password'), arrayPassword[i])
	WebUI.click(findTestObject('Facebook/Page_Login/button_Login'))
	

//Sometimes, FB will ask to update phone number:
	def buttonNotNowNotPresent = WebUI.verifyElementNotPresent(findTestObject('Facebook/Page_Login/button_NotNow'), 1, FailureHandling.OPTIONAL)
	
//Click "Not Now" if asking to update phone number:
	if(buttonNotNowNotPresent == false) WebUI.click(findTestObject('Facebook/Page_Login/button_NotNow'))
		
//Navigate to user detail page:
	WebUI.navigateToUrl(urlUser)
	WebUI.delay(2)
	
//Verify the button "Follow" is present or not:
	def isPresent = WebUI.verifyElementPresent(findTestObject('Facebook/Page_User/div_contains(Follow)'), 5, FailureHandling.OPTIONAL)

//If the button is not present, it maybe hidden:
	if(isPresent == false) {
		
		WebUI.click(findTestObject('Facebook/Page_User/div_contains(3dots)'))
		
		//To avoid FB lock account:
		WebUI.delay(3)
		
		isPresent = WebUI.verifyElementPresent(findTestObject('Facebook/Page_User/div_contains(Follow)'), 5, FailureHandling.OPTIONAL)
		
		if(isPresent == false) arrayResult.add("THE USER " + arrayUsername[i] + " HAS FOLLOWED ALREADY !!!")
		
	}
	
//If the button is present, it means user is not followed, click the button to follow:
	while (isPresent == true) {
		
		WebUI.waitForElementClickable(findTestObject('Facebook/Page_User/div_contains(Follow)'), 30)
		
		WebUI.click(findTestObject('Facebook/Page_User/div_contains(Follow)'))
		
		isPresent = WebUI.verifyElementPresent(findTestObject('Facebook/Page_User/div_contains(Follow)'), 5, FailureHandling.OPTIONAL)
		
		arrayResult.add("THE USER " + arrayUsername[i] + " IS NOW FOLLOWING THE TARGET USER !!!")
		
	}
	
	if(i % 5 == 0) {
		
		WebUI.closeBrowser()
		
		WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
		
		continue
		
	}
	
	WebUI.callTestCase(findTestCase('FB/4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	
	WebUI.navigateToUrl('https://facebook.com/')
	
}

WebUI.closeBrowser()

println("Total result is: " + arrayResult.size())
for (int i = 0; i < arrayResult.size(); i++) {
	
	println(arrayResult[i])
	
}