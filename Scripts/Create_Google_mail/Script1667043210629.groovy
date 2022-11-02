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

def fileTestData = 'Data Files/Data_Sheet2'

int totalRows = findTestData(fileTestData).getRowNumbers()

int columnIDUsername = 2
int columnIDPassword = 3

//DEFINITION: ARRAYS USED TO STORE DATA:
def arrayUsername = []
def arrayPassword = []

def arrayPhone = ['0909505534','0909505534','0909505534','0909505533']

for (int i = 0; i < totalRows; i++) {
	
	arrayUsername.add(findTestData(fileTestData).getValue(columnIDUsername, i + 1))
	arrayPassword.add(findTestData(fileTestData).getValue(columnIDPassword, i + 1))
}

for (int i = 0; i < 12; i++) {

	WebUI.openBrowser('https://mail.google.com/')
	
	WebUI.maximizeWindow()
	
	WebUI.click(findTestObject('Google_Mail/button_Create_account'))
	
	WebUI.click(findTestObject('Google_Mail/option_For_my_personal_use'))
	
	WebUI.setText(findTestObject('Google_Mail/textbox_FirstName'), 'Vu')
	
	WebUI.setText(findTestObject('Google_Mail/textbox_LastName'), 'Hung')
	
	WebUI.setText(findTestObject('Google_Mail/textbox_Username'), arrayUsername[i])
	
	WebUI.setText(findTestObject('Google_Mail/textbox_Password'), '1_Abc_123')
	
	WebUI.setText(findTestObject('Google_Mail/textbox_Password_confirm'), '1_Abc_123')
	
	def userExist = WebUI.verifyElementPresent(findTestObject('Object Repository/Google_Mail/div_contains_text_warning'), 2, FailureHandling.OPTIONAL)
	
	while (userExist == true) { 
		
		println("The user " + arrayUsername[i] + " is existing")
		
		i++
		
		WebUI.setText(findTestObject('Google_Mail/textbox_Username'), arrayUsername[i])
		
		WebUI.setText(findTestObject('Google_Mail/textbox_Password'), '1_Abc_123')
		
		WebUI.setText(findTestObject('Google_Mail/textbox_Password_confirm'), '1_Abc_123')
		
		userExist = WebUI.verifyElementPresent(findTestObject('Object Repository/Google_Mail/div_contains_text_warning'), 2, FailureHandling.OPTIONAL)
	
	}
	
	WebUI.click(findTestObject('Google_Mail/button_Next_in_1st_page'))

	WebUI.setText(findTestObject('Google_Mail/textbox_PhoneNumber'), '0909505534')

	WebUI.click(findTestObject('Google_Mail/button_Next_in_2nd_page'))
	
	def invalidPhone = WebUI.verifyElementPresent(findTestObject('Object Repository/Google_Mail/div_contains_text_warning'), 2, FailureHandling.OPTIONAL)
	
	int phoneID = 0
	
	def isBreak
	
	while (invalidPhone == true) {
		
		phoneID++
		
		if (arrayPhone[phoneID] == null) {
			
			WebUI.closeBrowser()
			
			println("All phone numbers are checked and all are invalid")
			
			isBreak = true
			
			break
			
		}
		
		WebUI.setText(findTestObject('Google_Mail/textbox_PhoneNumber'), arrayPhone[phoneID])
		
		WebUI.click(findTestObject('Google_Mail/button_Next_in_2nd_page'))
		
		invalidPhone = WebUI.verifyElementPresent(findTestObject('Object Repository/Google_Mail/div_contains_text_warning'), 2, FailureHandling.OPTIONAL)
		
	}
	
	if (isBreak == true) {
		
		println("Stop execution !!! No more user is checked")
		
		break
		
	}
	def verificationCode = WebUI.getAttribute(findTestObject('Object Repository/Google_Mail/textbox_VerificationCode'), 'data-initial-value')
	
	println("The verification code is: " + verificationCode)
	
	while(verificationCode == "" || verificationCode == null) {
		
		println("The verification code is empty. Will wait 10 seconds")
		
		WebUI.delay(10)
		 
		verificationCode = WebUI.getAttribute(findTestObject('Object Repository/Google_Mail/textbox_VerificationCode'), 'data-initial-value')
		
	}
	
	println("Before end - The verification code is: " + verificationCode)
	
	
	//WebUI.closeBrowser()

}