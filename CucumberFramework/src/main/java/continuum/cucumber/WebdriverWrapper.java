package continuum.cucumber;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;


/*********
 * Utility to reusable webdriveer commands
 * @author sneha.chemburkar
 *
 */


/**
 * @author sneha.chemburkar
 *
 */
public class WebdriverWrapper {
	WebDriver driver=null;

	
	public WebdriverWrapper(){
	   driver = DriverFactory.getDriver();
	}
	

	
	/**
	 * @return webdriver instance
	 */
	public WebDriver getWebdriver(){
		return driver;
	}
	
	/**
	 * @param newDriver
	 * assign newdriver to current driver thread
	 */
	public void switchDriver(WebDriver newDriver){
		this.driver=newDriver;
		
		
	}
	
//	public WebDriver openNewWebdriver(WebDriver driver2, String browserName,String url){
//		 Reporter.log("Opening new application with url"+url+" for browser "+browserName);
//		 driver2=WebDriverInitialization.createInstance((RemoteWebDriver) driver2,browserName);
//		 DriverFactory.setWebDriver2(driver2);
//	    
//		 driver2.get(url);
//		 return driver2;
//	  }
	
  /**
 * @param url
 * open application in browser
 */
public void openApplication(String url){
	  Reporter.log("Opening application with url"+url);
	  driver.get(url);    
	  waitFor(3000);
	 }
  
 // public void openOtherApplication(String url ){
//	  RemoteWebDriver driver2=null;
//	  driver2=WebDriverInitialization.createInstance(driver2,browser);
//  	 
//	    DriverFactory.setWebDriver2(driver2);
//	    driver2.get(url);
//        driver2.manage().deleteAllCookies();
//    	driver2.manage().window().maximize();
//	  driver.navigate().to(url);
	 
//    }
       
  
  
  /**
 * @param driver2
 * close 
 */
public void closeApplication(WebDriver driver2){
	  driver2.close();
      driver2.quit();
  }
	/**
	 * @param loc
	 * @return webelement
	 */
	public  WebElement getWebElement(Locator loc)
	{
		 String by=loc.getByType().toLowerCase();
		 String ele=loc.getValue();
		 String key=loc.getKey();
		WebElement webEle=null;
		try{
		if(by.equalsIgnoreCase("id"))
			webEle=driver.findElement(By.id(ele));
		else if(by.equalsIgnoreCase("class"))
			webEle=driver.findElement(By.className(ele));
		else if(by.equalsIgnoreCase("name"))
			webEle=driver.findElement(By.name(ele));
		else if(by.equalsIgnoreCase("link"))
			webEle=driver.findElement(By.linkText(ele));
		else if(by.equalsIgnoreCase("xpath"))
			webEle=driver.findElement(By.xpath(ele));
		else if(by.equalsIgnoreCase("css"))
			webEle=driver.findElement(By.cssSelector(ele));
		else if(by.equalsIgnoreCase("tag"))
			webEle=driver.findElement(By.tagName(ele));
		if(by.toString().equals(null))
			webEle=driver.findElement(By.xpath(ele));
		if(!Utilities.getMavenProperties("browser").equalsIgnoreCase("IE"))
		{
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
					webEle, "color: orange; border: 3px solid orange;");
		}
		}catch(WebDriverException e){
			System.out.println("Not able to locate element : "+key +" with locator :" +ele);
			Assert.fail("Not able to locate element : "+key +" with locator :" +ele, e);
			System.out.println("Exception "+e);

		}
		
		return webEle;
}
	
	public By getBy(Locator loc){
		By by=null;
		String locValue= null;
		locValue = loc.getValue();
		
					switch(loc.byType.toUpperCase()){
					   case "ID":
						by = By.name(locValue);
						break;
						case "NAME":
							by = By.name(locValue);
							break;
						case "LINKTEXT":
							by = By.linkText(locValue);
							break;
						case "XPATH":
							by = By.xpath(locValue);
							break;
						case "TAGNAME":
							by = By.tagName(locValue);
							break;
						case "CSS":  case "CSSSELECTOR":
							by = By.cssSelector(locValue);
							break;	
							default:
								by = By.xpath(locValue);
			
						}
			
				return by;
		}
	

//	public void elementHighlight(Locator loc ) {
//		WebElement element = null;
//		element=getWebElement(loc);
//		if(!(element==null)){
//		for (int i = 0; i < 10; i++) {
//			
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript(
//					"arguments[0].setAttribute('style', arguments[1]);",
//					element, "color: orange; border: 3px solid orange;");
//			js.executeScript(
//					"arguments[0].setAttribute('style', arguments[1]);",
//					element, "");
//		}
//		}else{
//			Reporter.log( "Highlight Element "+loc.getKey()+" Fail Found Exception while accessing  ");	
//		}
//	}


	
	  /**
	 * wait for page to load using jquery param
	 */
	public void waitForPageLoad() {
		   
		    (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
		        public Boolean apply(WebDriver d) {
		            JavascriptExecutor js = (JavascriptExecutor) d;
		            String readyState = js.executeScript("return document.readyState").toString();
		          //  System.out.println("Ready State: " + readyState);
		            return (Boolean) readyState.equals("complete");
		        }
		    });
		}

	  
	   /**
	 * wait for ajax action to complete
	 */
	public void waitForAjax() {
		    (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
		        public Boolean apply(WebDriver d) {
		            JavascriptExecutor js = (JavascriptExecutor) d;
		            return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
		        }
		    });
		}
	/**
	 * @param loc
	 * @param timeOutPeriod
	 * @return Webelement
	 * wait till element is dispalyed
	 */
	public WebElement waitForElementToBeDisplayed(final Locator loc,
			int timeOutPeriod) {
		Reporter.log("Waiting for webelement"+loc.getKey());
		final WebElement element= getWebElement(loc); 
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);

		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					if (element.isDisplayed())
					{
						Reporter.log(loc.getKey()+ "Element is displayed");
						return element;
						
					}
						else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});
	}

	/**
	 * @param timeOut
	 * wait implicitly for mention timeunit
	 */
	public void waitImplicit(int timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.MILLISECONDS);;

	}
	
	/**
	 * @param loc
	 * @param timeOutPeriod
	 * @return
	 * wait for elemnt to be invisible
	 */
	public boolean waitForElementToInvisible(Locator loc,
			int timeOutPeriod) {
		Reporter.log("Waiting for webelement to be invisible"+loc.getKey());
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);

		if (driver.findElements(getBy(loc)).size()==0)
			{
				Reporter.log(loc.getKey()+ "Element is invisble now");
				return true;
			}
			else
				return false;	
		
	}


	/**
	 * @param locate
	 * @param timeOutPeriod
	 * @return
	 * wait for element to be clickable
	 */
	public WebElement waitForElementToBeClickable( final Locator locate,int timeOutPeriod) {
		Reporter.log("Waiting for webelement"+locate.getKey());
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					WebElement element=getWebElement(locate); 
					if (element.isEnabled() && element.isDisplayed())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}
	
	/**
	 * @param locate
	 * @param timeOutPeriod
	 * @return webelement
	 * wait for frame
	 */
	public WebElement waitForFrame( final Locator locate,int timeOutPeriod) {
		Reporter.log("Waiting for frame"+locate.getKey());
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver driver) {
						try {
							WebElement frameLocator= getWebElement(locate);
							if(frameLocator.isDisplayed())
							    return frameLocator;
							else
								return null;
						} catch (NoSuchFrameException e) {
							return null;
					} catch (NoSuchElementException ex) {
						return null;
					} catch (StaleElementReferenceException ex) {
						return null;
					} catch (NullPointerException ex) {
						return null;
					}
				}		
				});
		}



	

	

	

	/**
	 * @param loc
	 * @param timeOutPeriod
	 * @return webelement
	 * wait for element to be enabled
	 */
	public WebElement waitForElementToBeEnabled(final Locator loc, int timeOutPeriod) {
		Reporter.log("Waiting for webelement"+loc.getKey());
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {
			 WebElement element=getWebElement(loc);
			public WebElement apply(WebDriver driver) {
				try {
					
					if (element.isEnabled())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	



	/**
	 * @param loc
	 * @param timeOutPeriod
	 * @return weblement
	 * wait for text to apprear
	 */
	public WebElement waitForTextToAppearInTextField(final Locator loc, int timeOutPeriod) {
		Reporter.log("Waiting for text in locator"+loc.getKey());
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {
            WebElement webElement=getWebElement(loc);
			public WebElement apply(WebDriver driver) {
				try {
					String text = webElement.getText();
					if (text != null && text.equals("")) {
						return webElement;
					} else
						return null;

				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

		
		
	}

	/**
	 * @param loc
	 * @param value
	 * @param timeOutPeriod
	 * @return webelment
	 * wait for options to appear in drop down list
	 */
	public boolean waitForOptionToBePresentInList(Locator loc, String value,
			int timeOutPeriod) {
		Reporter.log("Waiting for webelement"+loc.getKey());
		By by=getBy(loc);
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		try {
			webDriverWait.until(ExpectedConditions.textToBePresentInElement(
					driver.findElement(by), value));
			return true;
		} catch (TimeoutException e) {
			return false;
		}

		
	}

	/**
	 * switch to alert
	 */
	public void switchToAlert() {
		waitForAlert(2000);
		Reporter.log("Switch to alert");
		
		driver.switchTo().alert();
	}
	
	/**
	 * switch to frame
	 * @param loc
	 */
	public void switchToFrame(Locator loc) {
		waitForFrame(loc,2000);
		Reporter.log("Switch to frame "+loc.getKey());
		driver.switchTo().frame(getWebElement(loc));
		
	}
	/**
	 * @param i
	 * switch to frame by index i
	 */
	public void switchToFrameByIndex(int i) {
		
		Reporter.log("Switch to frameof index "+i);
		driver.switchTo().frame(i);
		
	}
	
	/**
	 * @return
	 * get current window handle
	 */
	public String getWindowHandle()
	{
		Reporter.log("Getting window handle");
		String winHandleBefore = driver.getWindowHandle();
		return winHandleBefore;
	}
	
	/**
	 *switch to newly opened window 
	 */
	public void switchToNewWindow() {
		Reporter.log("Switching to new window");
		// Switch to new window opened
		Set<String> winHandles = driver.getWindowHandles();
		driver.switchTo().window((String) winHandles.toArray()[winHandles.size() - 1]);

        waitFor(2000);
		
	}
	
	/**
	 * switch to parent window
	 */
	public  void switchtoParentWindow() {
		Reporter.log("Switch to main window");
		driver.switchTo().defaultContent();
		waitFor(2000);
	}
	

	/**
	 * @param loc
	 * bring elemnt in view using javascript
	 */
	public void bringElementInView(Locator loc) {
		WebElement element=getWebElement(loc);
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", element);
		waitFor(2000);
	}

	

	/**
	 * @param timeOutPeriod
	 * wait for alert
	 */
	public void waitForAlert(int timeOutPeriod) {
		Reporter.log("Waiting to alert");
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait
				.ignoring(NoSuchElementException.class,
						StaleElementReferenceException.class)
				.pollingEvery(10, TimeUnit.MILLISECONDS)
				.until(ExpectedConditions.alertIsPresent());
	}


	/**
	 * @param timeOutPeriod
	 * @return alertmessage
	 */
	public String getAlertMessage(int timeOutPeriod) {
		Reporter.log("Getting alert message");
		waitForAlert(timeOutPeriod);
		Alert alert = driver.switchTo().alert();
		String AlertMessage = alert.getText();
		return AlertMessage;
	}


	

	/**
	 * @param timeOutPeriod
	 * dismiss alert
	 */
	public String dismissAlert(int timeOutPeriod) {
		waitForAlert(timeOutPeriod);
		Reporter.log("Dismissing alert");
		Alert alert = driver.switchTo().alert();
		String alertMessage = alert.getText();

		alert.dismiss();
		return alertMessage;
	}

	/**
	 * accept alert
	 */
	public void acceptAlert() {
		Reporter.log("Accepting alert");
			try {
				waitForAlert(200);
				driver.switchTo().alert().accept();
			} catch (Exception e) {

			System.out.println("Not able to accept alert"+e.getMessage());
		}
	}



	/**
	 * @return true if alert present
	 * else false
	 */
	public boolean isAlertPresent() {
		boolean isAlertPresent = false;
		try {
			waitForAlert(300);
			driver.switchTo().alert();
			Reporter.log("Alert is present");
			isAlertPresent = true;
		} catch (Exception e) {
			Reporter.log("Alert is not present");
		}
		return isAlertPresent;
	}

	/**
	 * @param errorMessage
	 * @return true if error message is present
	 */
	public boolean isAlertWithSpecifiedMessagePresent(String errorMessage) {
		boolean isAlertPresent = false;
		try {
			// waitForAlert(3);
			isAlertPresent = driver.switchTo().alert().getText()
					.contains(errorMessage);
			Reporter.log("Alert is present");
		} catch (Exception e) {
			Reporter.log("Alert is not present");
		}
		return isAlertPresent;
	}

	/**
	 * @return true if alert is not present
	 */
	public boolean isAlertNotPresent() {
		boolean isAlertNotPresent = false;
		try {
			driver.switchTo().alert();
			isAlertNotPresent = false;
		} catch (Exception e) {
			isAlertNotPresent = true;
		}
		Reporter.log("Alert is not present");
		return isAlertNotPresent;
	}

	/**
	 * @param waitTime
	 * wait for specified time
	 */
	public void waitFor(int waitTime) {

		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {

		}
	}



	/**
	 * @param loc
	 * @return text 
	 */
	public String getText( Locator loc){
		
		String strTextValue=null;
		try{
			WebElement element = null;
			element=getWebElement(loc);	

			strTextValue=element.getText();
			Reporter.log("Get Value from " +loc.getKey()+ "Value stored is "+strTextValue);
			
		}catch(Exception e){
			Reporter.log("Failed to Fetch Text Value"+loc.getKey());
			Assert.fail("Found Exception while fetching value " +e.getMessage());
		}
		return strTextValue;	
	}
	
	/**
	 * @param loc
	 * verify text is present on specified locator
	 */
	public void verifyTextPresent(Locator loc) {
		waitFor(2000);
       try{
			
			
			WebElement element =getWebElement(loc);	
			
			String strTextValue=element.getText();
			
			
			if(!strTextValue.isEmpty()){
				Reporter.log(strTextValue+"Text is present");
				
			}
			else{
				Reporter.log(strTextValue+" Text is not present");
				
			}
			
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Not able to fetch text from " +loc.getKey()+"</font></I></B>");
			
		}
	}
	
	/**
	 * @param loc
	 * @param expectedValue
	 * verify expected value is present on specified locator
	 */
	public void verifyTextValue(Locator loc,String expectedValue){
		waitFor(2000);
		String strTextValue=null;
		try{
			
			
			WebElement element = null;
			element=getWebElement(loc);	
			
			strTextValue=element.getText();
			
			
			if(strTextValue.equalsIgnoreCase(expectedValue)){
				Reporter.log("Verify Text value of " +loc.getKey()+" Expected value is "+expectedValue+" Pass Actual value  is "+strTextValue);
				
			}
			else{
				Reporter.log("Verify Text value of "+loc.getKey()+" Expected value is "+expectedValue+" Fail Actual value  is "+strTextValue);
				
			}
			
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Verify Text value of " +loc.getKey()+" Expected value is "+expectedValue+"</font></I></B>");
			Assert.fail("Verify Text value of " +loc.getKey()+" Expected value is "+expectedValue+" Fail Found Exception while comparing "+e.getMessage());


		}
			
	}
	
	
	
	
	/**
	 * @param key
	 * @param loc
	 * write charcters 
	 */
	public void sendKeys( String key,Locator loc){
		waitFor(2000);
	   Reporter.log("Writing "+key+ "to "+loc.getKey());
		try{
			
			getWebElement(loc).sendKeys(key);

		}catch(Exception e){
		
			Reporter.log( "Send key command "+key+" Fail Found Exception while sending keys"+e.getMessage());
			
		}
		
	}
	
	/**
	 * @return title
	 * get current driver title
	 */
	public String getDriverTitle( ){
		String strTextValue=null;
		
		try{
		
			strTextValue=driver.getTitle();
			Reporter.log( "Driver Title"+strTextValue );
			
		}catch(Exception e){
			Reporter.log("Get Browser Title"+strTextValue+"Fail Found Exception while storing driver title ");
			Assert.fail( "Get Browser Title"+strTextValue+"Fail Found Exception while storing driver title "+e.getMessage());
			
		
		}
		return strTextValue;	
	}

	
	
	/**
	 * @param textTobeEntered
	 * @param loc
	 * clear text box and send keys
	 */
	public void clearandSendKeys(String textTobeEntered,Locator loc){
		try{
			if(textTobeEntered == null || textTobeEntered == ""||textTobeEntered.equalsIgnoreCase("NA")){
				
				return;
			}

			Reporter.log("Writing "+textTobeEntered+ "to "+loc.getKey());		
			WebElement element = null;
			element=getWebElement(loc);
			
			element.clear();
			waitFor(2000);
			
			 element.sendKeys(textTobeEntered);
			
			
			//System.out.println("Enter Value in " +loc.getKey()+" text field "+textTobeEntered+" Pass Value "+textTobeEntered+ " entered successfully");
			Reporter.log( "Enter Value in " +loc.getKey()+" text field "+textTobeEntered+" Pass Value "+textTobeEntered+ " entered successfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Enter Value in " +loc.getKey()+" text field "+textTobeEntered+" Fail Found Exception "+e.getMessage()+"</font></I></B>");
		//	System.out.println("Enter Value in " +loc.getKey()+" text field "+textTobeEntered+" Fail Found Exception  "+e.getMessage());
		}
		
	}
	

	/**
	 * @return current window id
	 */
	public String getWindowId(){
		Reporter.log("Get window id");
		String existingWindowID=null;
		
			try{
			existingWindowID = driver.getWindowHandle();
			}catch(Exception e){
				Reporter.log("<B><I><font size='4' color='Blue'>"+"Get current  window id Fail Found Exception while getting window id"+e.getMessage()+"</font></I></B>");
				
			}
		
			return existingWindowID;
			
		}

	/**
	 * @param winHandle
	 * switch to particular window
	 */
	public void switchToWindow(String winHandle){
	//	Reporter.log("Switching to window with handle "+winHandle);
		try{
			driver.switchTo().window(winHandle);
						
			Reporter.log( "Switch to window with handle "+winHandle);
			
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Switch to window Fail Found Exception while switching to new window "+e.getMessage()+"</font></I></B>");
			
		}
		driver.manage().window().maximize();
		waitFor(2000);
	}
	
	/**
	 * @param strElementName
	 * @param loc
	 * @param attribute
	 * @return attribute of object
	 */
	public String getAttributeValue(String strElementName, Locator loc, String attribute){
		String strTextValue=null;
		
		try{
			
			WebElement element = null;
			element=getWebElement(loc);
			
			strTextValue=element.getAttribute(attribute);
			Reporter.log( "Get "+attribute+" from " +strElementName+" "+strTextValue+" Pass Value stored is "+strTextValue);
			
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Get "+attribute+" from " +strElementName+" "+strTextValue+" Fail Found Exception while storing value "+e.getMessage()+"</font></I></B>");
			
		  
		}
		return strTextValue;	
	}
	
	/**
	 * @param loc
	 * @param statuReq
	 * check checkbox status
	 */
	public void changeCheckboxStatus(Locator loc, String statuReq){
				
	
				WebElement element = null;
				try{
				
				
				element=getWebElement(loc);
				if(statuReq.equalsIgnoreCase("uncheck") && element.isSelected()){
					element.click();	
				}
				if(statuReq.equalsIgnoreCase("check") && !element.isSelected()){
					element.click();
				}
				Reporter.log( "Change "+loc.getKey()+" checkbox status to "+statuReq);
		}catch(Exception e1){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Change "+loc.getKey()+" checkbox status"+statuReq+" Fail Found Exception "+e1.getMessage()+"</font></I></B>");
			
		}
	}
	
	/**
	 * @param loc
	 * mouse hover and click using javascript
	 * specific to IE
	 */
	public void mouseHoverAndClick(Locator loc){
		try{
		String javaScript = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";

		  ((JavascriptExecutor)driver).executeScript(javaScript,getWebElement(loc));
		}catch(Exception e)
		{System.out.println("Error in exceuting javascript");}
		
		  waitFor(2000);
	}
	
	/**
	 * @param loc
	 * click element using javascript
	 */
	public void clickUsingJavaScript(Locator loc){
		try{
		WebElement element = getWebElement(loc);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
		}catch(Exception e)
		{System.out.println("Error in exceuting javascript");}
		waitFor(2000);
		}
		
		/**
		 * @param id
		 * click using javascript for id of element
		 */
		public void clickIdusingJavaScript(String id){
			try{
		String script="document.getElementById('"+id+"').click()";
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript(script);
			}catch(Exception e)
			{System.out.println("Error in exceuting javascript");}
		waitFor(2000);
		}
		
	/**
	 * @param loc
	 * @return checkbox status true or false
	 */
	public boolean getCheckboxStatus(Locator loc){
		boolean status= false;
		waitFor(2000);
		WebElement element = null;
		
		try{
		
		
		element=getWebElement(loc);
		status= element.isSelected();
		Reporter.log( "Verify checkbox status "+loc.getKey()+" Pass Checkbox is " + status);
		}catch(Exception e1){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Verify checkbox status "+loc.getKey()+" Fail Found Exception " + status+"</font></I></B>");

		  }
		
		return status;
		}

	/**
	 * @param strElementName
	 * @param loc
	 * @param expectedValue
	 * @param attribute
	 * @param driver
	 * verify if attribute for element
	 */
	public void verifyAttributeValueContains(String strElementName, Locator loc, String expectedValue,String attribute,RemoteWebDriver driver){
		
		String strTextValue=null;
		try{
			
			WebElement element = null;
			element=waitForElementToBeDisplayed(loc,2000);
			strTextValue=element.getAttribute(attribute);
			if(strTextValue.contains(",")){
				strTextValue=strTextValue.replace(",", "");
			}
			if(expectedValue.contains(",")){
				expectedValue=expectedValue.replace(",", "");
			}
			if(strTextValue.contains(expectedValue)){
				Reporter.log( "Verify "+attribute+" of " +strElementName+" Expected : "+expectedValue+" should contain "+strTextValue+" Pass Actual value  is "+strTextValue);	
			}
			else{
				Reporter.log( "Verify "+attribute+" of " +strElementName+" Expected : "+expectedValue+" should contain "+strTextValue+" Fail Actual value  is "+strTextValue);
			}
			
		}catch(Exception e){
			Reporter.log("Verify "+attribute+" of " +strElementName+" Expected : "+expectedValue+" should contain "+strTextValue );
			
		}
		
	}
		
	
	/**
	 * @param loc
	 * click on webelement
	 */
	public void clickElement(Locator loc){
//		
//		
			WebElement element = null;
			element=getWebElement(loc);
			if(element.isDisplayed())
			{
				element.click();
				Reporter.log("Clicking on  "+loc.getKey());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Reporter.log("Not able to click on "+e.getMessage());
				}
				
			}
			else
			{
				
			    Reporter.log(loc.getKey()+" Element is not displayed for clicking");
			}
	}
	
	
	/**
	 * @param loc
	 * @param identification
	 * click on weblist element
	 */
	public void clickWebListElement(Locator loc,String identification){
		
		try{
			
			String[] identity=identification.split(":");
			List<WebElement> lst= getWebList(loc);
			WebElement element = null;
			if(identity[0].equalsIgnoreCase("index")){
				element=lst.get(Integer.parseInt(identity[1]));
			}else if(identity[0].equalsIgnoreCase("text")){
				for(int i=0; i<lst.size();i++){
					try{
						if(lst.get(i).getText().equalsIgnoreCase(identity[1])) element=lst.get(i);
					}catch(Exception e ){
						e.printStackTrace();
					}
				}
			}
			
		if(!(element== null)) element.click();	
			Reporter.log( "Clicked on " +loc.getKey()+" Successfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Clicked on " +loc.getKey()+" Failed Found Exception "+e.getMessage()+"</font></I></B>");
			
		}
			
	}
		
	/**
	 * @param loc
	 * double click on webelement
	 */
	public void doubleClick(Locator loc){
		
		try{
		
			Actions doubleClickAction = new Actions(driver); 
			
			
			WebElement element = null;
			element=getWebElement(loc);
			
			doubleClickAction.moveToElement(element).doubleClick().build().perform();
			Reporter.log( "Double Click " +loc.getKey()+" Pass Clicked "+loc.getKey()+" Successfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Double Click " +loc.getKey()+" Fail Found Exception "+e.getMessage()+"</font></I></B>");
		//	executionFlag=false;
		}
			
	}
	
	/**
	 * @param loc
	 * @param valueTobeSelected
	 * select option from dropdown using text
	 */
	public void selectByTextFromDropDown(Locator loc,String valueTobeSelected){
		if(valueTobeSelected == null || valueTobeSelected == ""||valueTobeSelected.equalsIgnoreCase("NA")){
			return;
		}
		try{
			
			
			WebElement element = null;
			element=getWebElement(loc);
			//System.out.println("Selecting "+valueTobeSelected);
			Select webCheckBox= new Select(element);
			if(valueTobeSelected.equalsIgnoreCase("EMPTY")){
				webCheckBox.selectByVisibleText("");
			}else{
				webCheckBox.selectByVisibleText(valueTobeSelected);
			}
			
			Reporter.log( "Select value from " +loc.getKey()+" "+valueTobeSelected+" sucessfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Select value from " +loc.getKey()+" "+valueTobeSelected+" Fail  Found Exception while selecting value "+e.getMessage()+"</font></I></B>");	
			
		}
			
	}
		
	/**
	 * @param loc
	 * @param index
	 * select option from dropdown using index
	 */
	public void selectValueByIndexFromDropDown(Locator loc,int index ){
		String valueSelected="";
		try{
			
			
			WebElement element = null;
			element=getWebElement(loc);
			Select webCheckBox= new Select(element);
			webCheckBox.selectByIndex(index);
			valueSelected=webCheckBox.getFirstSelectedOption().getText();
			Reporter.log( "Select value from " +loc.getKey()+" "+valueSelected+" Selected  Successfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Select value from " +loc.getKey()+" "+valueSelected+" Fail  Found Exception while selecting value "+e.getMessage()+"</font></I></B>");	
			
		}
			
	}
			
	/**
	 * @param loc
	 * @param valueTobeSelected
	 * select option from dropdown using value
	 */
	public void selectByValueFromDropDown(Locator loc,String valueTobeSelected){
		if(valueTobeSelected == null || valueTobeSelected == ""||valueTobeSelected.equalsIgnoreCase("NA")){
			return;
		}
		try{
			
			
			WebElement element = null;
			element=getWebElement(loc);
			//System.out.println("Selecting by value "+valueTobeSelected);
			Select webCheckBox= new Select(element);
			if(valueTobeSelected.equalsIgnoreCase("EMPTY")){
				webCheckBox.selectByValue("");
			}else{
				webCheckBox.selectByValue(valueTobeSelected);
			}
			
			Reporter.log( "Select value from " +loc.getKey()+" "+valueTobeSelected+" Selected  Successfully");	
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Select value from " +loc.getKey()+" "+valueTobeSelected+" Fail  Found Exception while selecting value "+e.getMessage()+"</font></I></B>");	
			
		}
			
	}
	
	
	/**
	 * @param loc
	 * @param expectedValue
	 * verify expected value is selected
	 */
	public void verifySelectedValue( Locator loc,String expectedValue){
		
		try{
			WebElement element = null;
			element=waitForElementToBeDisplayed(loc,2000);
			Select webSelectList= new Select(element);
			String item = webSelectList.getFirstSelectedOption().getText();
				if (item.trim().equals(expectedValue.trim()))
				{
				Reporter.log( "Verify Selected List value from " +loc.getKey()+" "+ expectedValue+" Pass Selected value is"+expectedValue);
				}
				else
				{
				Reporter.log( "Verify Selected List value from " +loc.getKey()+" "+expectedValue+" Fail Selected value "+item+" does not match "+expectedValue);
				}
		}catch(Exception e){
		Reporter.log("Verify Selected List value from  " +loc.getKey()+" "+expectedValue+"Fail Found Exception while verifying selected element "+e.getMessage());
		 
		}
	}
	
	/**
	 * @param loc
	 * @return string
	 * get selected value string
	 */
	public String getSelectedValue(Locator loc){
	String item =null;
	
		try{
			WebElement element = null;
			element=getWebElement(loc);
			Select webSelectList= new Select(element);
			item = webSelectList.getFirstSelectedOption().getText();
			Reporter.log( "Get Selected List value from " +loc.getKey()+" Selected value is "+item);
		}catch(Exception e){
		Reporter.log("<B><I><font size='4' color='Blue'>"+"Get Selected List value from " +loc.getKey()+"  Done Found Exception while verifying selected element "+e.getMessage()+"</font></I></B>");
		}
	
	return item;
	}

			
	/**
	 * @param loc
	 * @param valueTobeSelected
	 * select value from table
	 */
	public void selectValueFromTable(Locator loc,String valueTobeSelected){
		if(valueTobeSelected == null || valueTobeSelected == ""||valueTobeSelected.equalsIgnoreCase("NA")){
			return;
		}
		try{

			

			WebElement element = null;
			element=getWebElement(loc);
			List<WebElement> options1 = element.findElements(By.tagName("option"));
			boolean isPresent = false;
			for(WebElement option : options1){
				if(option.getText().equals(valueTobeSelected)){
					element.click();
					option.click();
					isPresent = true;
					break;
				}
			}
			if(isPresent){
				Reporter.log( "Select value from " +loc.getKey()+ " Listbox "+ valueTobeSelected+" Pass "+valueTobeSelected+" option is selected from the dropdown");
			}else{
				Reporter.log( "Select value from " +loc.getKey()+ " Listbox "+ valueTobeSelected+ " Fail "+valueTobeSelected+" option is not selected from the dropdown");
			}
				
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Select value from " +loc.getKey()+" "+valueTobeSelected+" Fail Found Exception while selecting value "+e.getMessage()+"</font></I></B>");	
		
		}
			
	}
	
	
	
	
	
	
	
	
	/**
	 * @param loc
	 * @return Webelment list
	 * get list of webelement
	 */
	public List<WebElement> getWebList(Locator loc ){
		Reporter.log("Getting list of webElemënt");
		List<WebElement> lst = null;
		String[] objProperties= null;
	//	objProperties = identifier.split("_", 2);
		
				try{
					By by=getBy(loc);
					lst=driver.findElements(by);
					} catch(Exception elenotFound){
						Reporter.log("<B><I><font size='4' color='Blue'>"+"Get Web List "+objProperties[1]+" Fail Found Exception while accessing element "+elenotFound.getMessage()+"</font></I></B>");
					}
				
				return lst;
			}
	
/**
 * @param loc
 * verify element is present
 */
public void verifyElementPresent(Locator loc){
	waitFor(2000);
		
	Assert.assertEquals(getWebElement(loc).isDisplayed(),true,"PASS:Element "+loc.getKey()+" is present");
		Reporter.log("PASS:Element "+loc.getKey()+" is present");	    
	}

/**
 * @param loc
 * @return true if elemnt present
 * else retrun false
 */
public boolean findElementPresent(Locator loc){
	
	boolean exists = false;
	//CreateResult result = new CreateResult();
	
	if(getWebdriver().findElements(getBy(loc)).size()!=0)
		    {
			 exists=true;
		    }
	Reporter.log(loc.getKey()+" exists "+exists);
	return exists;
	
}


/**
 * @param url
 * verify current url
 */
public void verifyCurrentUrl(String url) {
   Assert.assertEquals(url,driver.getCurrentUrl(), "Navigated to correct url");
   Reporter.log("Navigated to correct url");	
}
/**
 * @param tittle
 * verify window title
 */
public void verifyTitle(String tittle) {
	 Assert.assertEquals(tittle,driver.getTitle(), "Navigated to correct application");
	 Reporter.log("Navigated to correct application");	
	}

/**
 * @param loc
 * mouse hover on certain element
 */
public void mouseHover(Locator loc) {
	Reporter.log("Mouse hovering over "+loc.key);
	WebElement hoverElement=getWebElement(loc);
	Actions builder = new Actions(driver);
	builder.moveToElement(hoverElement).build().perform();
	waitFor(2000);
	
}


/**
 * @param loc
 * @return
 * verify certail element is not present
 */
public boolean verifyElementNotPresent(Locator loc){
	boolean status=false;
		
		try{
		
		
	
		By by1=getBy(loc);
		
		 if(driver.findElements(by1).size()==0){
			 Reporter.log( "Expected: Element "+loc.getKey()+"is not present");	 
			 status= true;
		 }
		 else{
			 Reporter.log( "Fail: Element "+loc.getKey()+"is present");	 
			
		 }
		
		}catch(Exception e){
			Reporter.log("<B><I><font size='4' color='Blue'>"+"Expected: Element "+loc.getKey()+"is not present"+"</font></I></B>");	 
	
		}
		return status;
	}

    /**
     * @param loc
     * @param attribute
     * @return attribute of an webelemnt
     */
    public String getAttribute( Locator loc,String attribute){
    	Reporter.log("Getting "+attribute+" of "+loc.getKey());
	    return getWebElement(loc).getAttribute(attribute);
    }

  
   /**
 * @param partialTitle
 * verify if certain keyword is present in title
 */
public void verifyPageTitleContains(String partialTitle) {
		String actualTitle = driver.getTitle();
		Assert.assertTrue(actualTitle.contains(partialTitle),
				"Expected text in page title '" + partialTitle
						+ "', but actual title was '" + actualTitle + "'");
		 Reporter.log("Expected text in page title '" + partialTitle
						+ "', but actual title was '" + actualTitle + "'");
	}

	/**
	 * @param locator
	 * @param expectedCount
	 * verify element count, on specified webelement
	 */
	public void verifyElementCount(Locator locator, int expectedCount) {
		int actualCount = driver.findElements(getBy(locator)).size();
		Assert.assertEquals(actualCount, expectedCount,
				"Expected locator count '" + expectedCount + "', but was '"
						+ actualCount + "'");
		 Reporter.log("Expected locator count '" + expectedCount + "', but was '"
						+ actualCount + "'");
	}
	
	
	/**
	 * @param javascriptToExecute
	 * @param locator
	 * execute javascript
	 */
	public void executeScript(String javascriptToExecute, Locator locator) {
		try {
			
			WebElement webElement = driver.findElement(getBy(locator));

			
			((JavascriptExecutor) this.driver).executeScript(
					javascriptToExecute, new Object[] { webElement });
		} catch (WebDriverException webDrivExc) {
			Reporter.log(
					"Exception on Javascript Execution occurred while performing executeScript");
		     }
	}

	/**
	 * refresh page
	 */
	public void refreshPage() {
		Reporter.log("Refreshing page");
		driver.navigate().refresh();
	}
	
	/**
	 * refresh frame
	 */
	public void refreshFrame() {
		Reporter.log("Refreshing frame");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.location.reload(true);");
	}
	/**
	 * @return parent webelement
	 */
	public WebElement getParentElement() {
		Reporter.log("Getting parent element");
		return driver.findElement(By.xpath(".."));
	}


	/**
	 * @return true if vertical scrool bar is present
	 * else false
	 */
	public boolean isVerticalScrollBarPresent() {
		Reporter.log("Verify vertical scroll bar is present");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js.executeScript("return document.documentElement.scrollHeight > document.documentElement.clientHeight;");
	}

	/**
	 * @return true if horizntal scrool bar is present 
	 * else false 
	 */
	public boolean isHorizontalScrollBarPresent() {
		Reporter.log("Verify horizontal scroll bar is present");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js.executeScript("return document.documentElement.scrollWidth > document.documentElement.clientWidth;");
	}

   



	

		/**
		 * @param driver2
		 * @param browserName
		 * @param url
		 * @return
		 * open new instance of webdriver and set it to thread 2
		 */
		public WebDriver openNewWebdriver(WebDriver driver2, String browserName,String url){
			 Reporter.log("Opening new application with url"+url+" for browser "+browserName);
			 driver2=WebDriverInitialization.createInstance((RemoteWebDriver) driver2,browserName);
			 DriverFactory.setWebDriver2(driver2);
		    
			 driver2.get(url);
		      return driver2;
		}
}
