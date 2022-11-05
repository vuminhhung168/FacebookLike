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

//GET DATA FROM FILE/DATABASE:
String fileTestData = 'Data Files/Data_Sheet1'

int totalRows = findTestData(fileTestData).getRowNumbers()

int columnIDUsername = 2
int columnIDPassword = 3

//DEFINITION: ARRAYS IS USED TO STORE DATA:
def arrayUsername = []
def arrayPassword = []

//DEFINITION: array to store result:
def arrayResult = []

//GET DATA AND STORE:
for (int i = 0; i < totalRows; i++) {
    arrayUsername.add(findTestData(fileTestData).getValue(columnIDUsername, i + 1))
    arrayPassword.add(findTestData(fileTestData).getValue(columnIDPassword, i + 1))
}
//DEFINITION: 
//The URL of user you want to take action:
String urlUser = 'https://www.facebook.com/lionkingneverlose'

//PREREQUISITE STEPS:
WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)


for (int i = 0; i < totalRows; i++) {
	
//Provide credential and click login:
	//If user is locked, skip:
	if ((i + 1) == 0) {
		
		println("THE USER " + arrayUsername[i] + " IS LOCKED AND WILL BE SKIPPED !!!")
		
		continue
	}
	
	println('LOGIN BY USER: ' + (arrayUsername[i]) + "\nTHE ID IN DATA FILE IS = " + (i + 1) )
	
    WebUI.setText(findTestObject('Object Repository/Facebook/Page_Login/input_Username'), arrayUsername[i])
    WebUI.setText(findTestObject('Object Repository/Facebook/Page_Login/input_Password'), arrayPassword[i])
    WebUI.click(findTestObject('Object Repository/Facebook/Page_Login/button_Login'))
	WebUI.delay(2)
	

//Sometimes, FB will ask to update phone number:
	def buttonNotNowNotPresent = WebUI.verifyElementNotPresent(findTestObject('Facebook/Page_Login/button_NotNow'), 1, FailureHandling.OPTIONAL)
    
//Click "Not Now" if asking to update phone number:
    if(buttonNotNowNotPresent == false) WebUI.click(findTestObject('Facebook/Page_Login/button_NotNow'))
		
//Navigate to user detail page:
	WebUI.navigateToUrl(urlUser)
    WebUI.delay(5)
	
	
	
	if(i % 5 == 0) {
		
		WebUI.closeBrowser()
		
		WebUI.callTestCase(findTestCase('FB/1_Launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
		
		continue
		
	}
	
	WebUI.callTestCase(findTestCase('FB/4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	WebUI.delay(2)
	
	WebUI.navigateToUrl('https://facebook.com/')
	
}

WebUI.closeBrowser()