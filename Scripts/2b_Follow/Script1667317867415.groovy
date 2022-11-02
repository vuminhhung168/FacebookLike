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
String urlUser = 'https://www.facebook.com/lenguyenkhtn'
//Thuy: 'https://www.facebook.com/profile.php?id=100026190698253'

//PREREQUISITE STEPS:
WebUI.callTestCase(findTestCase('1_Open_browser_and_launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)


for (int i = 13; i < totalRows; i++) {
    
//Provide credential and click login:
	//If user is locked, skip:
	if ((i + 1) == 33) continue
	
	println('Login by user: ' + (arrayUsername[i]) + "\nThe ID in data file ̣̣̣(i + 1) = " + (i + 1) )
	
    WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/input_Username'), arrayUsername[i])
    WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/input_Password'), arrayPassword[i])
    WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/button_Login'))
	

//Sometimes, FB will ask to update phone number:
	def buttonNotNowNotPresent = WebUI.verifyElementNotPresent(findTestObject('Facebook/Login_Page/button_NotNow'), 1, FailureHandling.OPTIONAL)
    
//Click "Not Now" if asking to update phone number:
    if(buttonNotNowNotPresent == false) WebUI.click(findTestObject('Facebook/Login_Page/button_NotNow'))
		
//Navigate to user detail page:
	WebUI.navigateToUrl(urlUser)

//Sometimes, after login, FB will ask to send notification or not:
	/* To bypass it, follow below steps:
	 * Go to Project > Settings > Desired Capabilities > WebUI > Chrome
	 * Add args (type = List) with value "--disable-notifications"
	 */
	
    WebUI.delay(3)
	
//Verify the button "Follow" is present or not:
    def isPresent = WebUI.verifyElementPresent(findTestObject('Object Repository/Facebook/Main_Page/div_(role-button)_contains(Follow)'), 5, FailureHandling.OPTIONAL)

//If the button is not present, it maybe hidden:
	if(isPresent == false) {
		
		WebUI.click(findTestObject('Facebook/Main_Page/div_contains(3dots)'))
		
		isPresent = WebUI.verifyElementPresent(findTestObject('Object Repository/Facebook/Main_Page/div_(role-button)_contains(Follow)'), 5, FailureHandling.OPTIONAL)
		
		if(isPresent == false) arrayResult.add("The user " + arrayUsername[i] + " has already followed")
		
	}
	
//If the button is present, it means user is not followed, click the button to follow:
	while (isPresent == true) {
		
		WebUI.waitForElementClickable(findTestObject('Object Repository/Facebook/Main_Page/div_(role-button)_contains(Follow)'), 30)
		
		WebUI.click(findTestObject('Object Repository/Facebook/Main_Page/div_(role-button)_contains(Follow)'))
		
		isPresent = WebUI.verifyElementPresent(findTestObject('Object Repository/Facebook/Main_Page/div_(role-button)_contains(Follow)'), 5, FailureHandling.OPTIONAL)
		
		arrayResult.add("The user " + arrayUsername[i] + " is now following the target user")
		
	}
	
	if(i % 5 == 0) {
		
		WebUI.closeBrowser()
		
		WebUI.callTestCase(findTestCase('1_Open_browser_and_launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
		
		continue
		
	}
	
	WebUI.callTestCase(findTestCase('4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	
	WebUI.navigateToUrl('https://facebook.com/')
	
}

WebUI.closeBrowser()

println("Total result is: " + arrayResult.size())
for (int i = 0; i < arrayResult.size(); i++) {
	
	println(arrayResult[i])
	
}