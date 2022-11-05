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

import java.util.concurrent.ConcurrentHashMap.KeySetView

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

//Status content:
String contentLine1 = 'Automation Test line 1'
String contentLine2 = 'Automation Test line 2'
String contentLine3 = 'Automation Test line 3'

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
	
	//Scroll down to avoid FB lock account:
	WebUI.sendKeys("" ,Keys.chord(Keys.PAGE_DOWN))
	WebUI.delay(2)
		
	WebUI.click(findTestObject('Facebook/Page_Personal/PostStatus/div_contains(question)'))
	WebUI.delay(3)
	
	WebUI.click(findTestObject('Facebook/Page_Personal/PostStatus/span_select_objective'))
	WebUI.delay(1)

	WebUI.click(findTestObject('Facebook/Page_Personal/PostStatus/div_public'))
	WebUI.delay(1)
	
	WebUI.click(findTestObject('Facebook/Page_Personal/PostStatus/div_button_Done'))
	WebUI.delay(1)
	
	//Line 1:
	WebUI.setText(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), contentLine1)
	WebUI.delay(1)
	WebUI.sendKeys(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), Keys.chord(Keys.ENTER))
	WebUI.delay(1)
	
	//Line 2:
	WebUI.setText(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), contentLine2)
	WebUI.delay(1)
	WebUI.sendKeys(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), Keys.chord(Keys.ENTER))
	WebUI.delay(1)
	
	//Line 3:
	WebUI.setText(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), contentLine3)
	WebUI.delay(1)
	WebUI.sendKeys(findTestObject('Facebook/Page_Personal/PostStatus/span_article_content'), Keys.chord(Keys.ENTER))
	WebUI.delay(1)
	
	WebUI.click(findTestObject('Facebook/Page_Personal/PostStatus/div_button_Post'))
	WebUI.delay(3)
	
	if(i % 5 == 0) {
		
		WebUI.closeBrowser()
		
		WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
		
		continue
		
	}
	
	WebUI.callTestCase(findTestCase('FB/4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	
	WebUI.navigateToUrl('https://facebook.com/')
	
}

WebUI.closeBrowser()