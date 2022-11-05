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
 * tagName: content of the article which you want to like/unlike
 */

//===================================================================================================================

//REQUIREMENT INFOR:
//The URL of user you want to take action:
String urlUser = 'https://www.facebook.com/lionkingneverlose/'

//The hashtag or content of article:
String tagName = ''

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
//DEFINITION: the color to define article has been liked already or not:
def colorLike
def colorUnlike

//TRUE: if you want to "like", FALSE: if you want to "unlike":
def likeAction = true

if(likeAction == true) { colorLike = 'rgba(32, 120, 244, 1)'; colorUnlike = 'rgba(101, 103, 107, 1)' }
else { colorUnlike = 'rgba(32, 120, 244, 1)'; colorLike = 'rgba(101, 103, 107, 1)' }

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

//Sometimes, after login, FB will ask to send notification or not:
	/* To bypass it, follow below steps:
	 * Go to Project > Settings > Desired Capabilities > WebUI > Chrome
	 * Add args (type = List) with value "--disable-notifications"
	 */
	
//Scroll down to avoid FB lock account:
	WebUI.sendKeys("" ,Keys.chord(Keys.PAGE_DOWN))
	WebUI.delay(2)

	
//Verify the article which has the expected hashtag is present:
	WebUI.verifyElementPresent(findTestObject('Facebook/Page_User/dynamic_div_(tagName)', [('tagName') : tagName]), 1)

//Verify the span contains text "Like" is present:
	WebUI.verifyElementPresent(findTestObject('Facebook/Page_User/span_Like',[('tagName'):tagName]), 3, FailureHandling.OPTIONAL)

//Get actual color of "Like" text:
	def actualColor = WebUI.getCSSValue(findTestObject('Facebook/Page_User/span_Like',[('tagName'):tagName]),'color')
	
//Exit the loop when article is liked:
	while (actualColor != colorLike) {
		
		println('THE ARTICLE HAS NOT BEEN LIKED YET !!!')
		
		//Wait for button Like is clickable:
		WebUI.waitForElementClickable(findTestObject('Facebook/Page_User/div_Like', [('tagName') : tagName]), 60)
		
		//Click Like button:
		WebUI.enhancedClick(findTestObject('Facebook/Page_User/div_Like', [('tagName') : tagName]), FailureHandling.OPTIONAL)
		
		//Get the actual color of text Like (to verify article is liked):
		actualColor = WebUI.getCSSValue(findTestObject('Facebook/Page_User/span_Like', [('tagName') : tagName]), 'color')
		
	}
	
	//Close browser and re-open to avoid Facebook check robot:
	if(i != 0 || (i % 5) == 0) {
		
		WebUI.closeBrowser()
		
		WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
		
	}
	
	//Logout current user:
	WebUI.callTestCase(findTestCase('FB/4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	
	//Navigate to login page:
	WebUI.navigateToUrl('https://facebook.com/')
	
	println("THE USER " + arrayUsername[i] + " HAVE LIKE THE ARTICLE !!!")

}

WebUI.closeBrowser()