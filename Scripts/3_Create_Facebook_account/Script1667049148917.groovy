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

int columnIDUsername  = 2
int columnIDPassword  = 3
int columnIDFirstName = 4
int columnIDLastName  = 5

//DEFINITION: ARRAYS USED TO STORE DATA:
def arrayUsername  = []
def arrayPassword  = []
def arrayFirstName = []
def arrayLastName  = []

for (int i = 0; i < totalRows; i++) {
	
	arrayUsername.add(findTestData(fileTestData).getValue(columnIDUsername,  i + 1))
	arrayPassword.add(findTestData(fileTestData).getValue(columnIDPassword,  i + 1))
	arrayFirstName.add(findTestData(fileTestData).getValue(columnIDFirstName, i + 1))
	arrayLastName.add(findTestData(fileTestData).getValue(columnIDLastName,  i + 1))
}

for (int i = 7; i < 8; i++) {
	
	WebUI.callTestCase(findTestCase('1_Open_browser_and_launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/button_Create_New_Account'))
	
	WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/textbox_Last_Name'), arrayFirstName[i])
	
	WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/textbox_First_Name'), arrayLastName[i])
	
	WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/textbox_Username'), arrayUsername[i])
	
	WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/textbox_Username_Confirm'), arrayUsername[i])
	
	WebUI.setText(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/textbox_Password'), arrayPassword[i])
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/option_Male'))
	
	WebUI.selectOptionByValue(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/select_Year'), '2001', false)
	
	WebUI.delay(3)
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/button_Sign_Up'))
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/span_Continue'))
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/checkbox_Not_A_Robot'))
	
	WebUI.delay(20)
	
	WebUI.click(findTestObject('Object Repository/Facebook/Login_Page/Create_New_Account_Page/span_Continue'))
	
	//WebUI.closeBrowser()
	
}









