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

//DEFINITION: ARRAYS USED TO STORE DATA:
def arrayUsername = []
def arrayPassword = []

def likeAction = true

//DEFINITION: the color to define article has been liked already or not:
def colorLike
def colorUnlike

if(likeAction == true) { colorLike = 'rgba(32, 120, 244, 1)'; colorUnlike = 'rgba(101, 103, 107, 1)' }
else { colorUnlike = 'rgba(32, 120, 244, 1)'; colorLike = 'rgba(101, 103, 107, 1)' }

//GET DATA AND STORE:
for (int i = 0; i < totalRows; i++) {
    arrayUsername.add(findTestData(fileTestData).getValue(columnIDUsername, i + 1))
    arrayPassword.add(findTestData(fileTestData).getValue(columnIDPassword, i + 1))
}
//DEFINITION: 
//The URL of user you want to take action:
String urlUser = 'https://www.facebook.com/lionkingneverlose/'

//The hashtag of article:
String tagName = '011122-2346'

//PREREQUISITE STEPS:
WebUI.callTestCase(findTestCase('1_Open_browser_and_launch_Facebook'), [:], FailureHandling.STOP_ON_FAILURE)


for (int i = 22; i < totalRows; i++) {
    
//Provide credential and click login:
	//If user is locked, skip:
	if ((i + 1) == 22 || (i + 1) == 24) continue
	
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
	
//Scroll down to view the expected status (make it loaded):
    //WebUI.sendKeys("" ,Keys.chord(Keys.PAGE_DOWN))
    WebUI.delay(3)

    
//Verify the article which has the expected hashtag is present: 
    WebUI.verifyElementPresent(findTestObject('Facebook/Main_Page/dynamic_div_(tagName)', [('tagName') : tagName]), 1)

//Verify the span contains text "Like" is present:
	WebUI.verifyElementPresent(findTestObject('Facebook/Main_Page/span_Like',[('tagName'):tagName]), 3, FailureHandling.OPTIONAL)

//Get actual color of "Like" text:
	def actualColor = WebUI.getCSSValue(findTestObject('Facebook/Main_Page/span_Like',[('tagName'):tagName]),'color')
	
//Exit the loop when article is liked:
	while (actualColor != colorLike) {
		
		println('The acticle is not liked\n')
		
		WebUI.waitForElementClickable(findTestObject('Facebook/Main_Page/div_Like', [('tagName') : tagName]), 60)
		
		//Sometimes, click is failed with error: Element is not clickable at point (x,y):
		/*
		 * SOLUTIONS:
		 * WebElement element = WebUiCommonHelper.findWebElement(test_object), 30)
		 * WebUI.executeJavaScript("arguments[0].click", Arrays.asList(element))
		 */
		
		WebUI.enhancedClick(findTestObject('Facebook/Main_Page/div_Like', [('tagName') : tagName]), FailureHandling.OPTIONAL)
		
		actualColor = WebUI.getCSSValue(findTestObject('Facebook/Main_Page/span_Like', [('tagName') : tagName]), 'color')
		
	}
	
	WebUI.callTestCase(findTestCase('4_Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	
	WebUI.navigateToUrl('https://facebook.com/')
	
}

WebUI.closeBrowser()