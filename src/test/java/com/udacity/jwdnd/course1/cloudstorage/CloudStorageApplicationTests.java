package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait webDriverWait;
	private NotePage notePage;
	private CredentialPage credentialPage;

	@BeforeAll
	static void beforeAll() {WebDriverManager.chromedriver().setup();}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depend ing on the rest of your code.
		*/
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() throws InterruptedException{
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		Thread.sleep(2000);
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("file")));
		WebElement fileSelectButton = driver.findElement(By.id("file"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}
	public void doLogOut() {
		WebElement Logout = driver.findElement(By.id("submit-button"));
		Logout.click();
	}
	@Test
	public void create_edit_delete_notes() throws InterruptedException {
		doMockSignUp("Note","Test","XYZ","123");
		doLogIn("XYZ", "123");
		webDriverWait = new WebDriverWait(driver, 2);
		notePage = new NotePage(driver);
	    noteAdd(notePage);
		noteEdit(notePage);
		noteDelete(notePage);
	    doLogOut();
	    webDriverWait.until(ExpectedConditions.titleContains("Login"));
	    Assertions.assertEquals("You have been logged out", driver.findElement(By.id("logout")).getText());
	}
	private void noteAdd(NotePage notePage) throws InterruptedException {

		Thread.sleep(2000);
		notePage.noteTab.click();
		Thread.sleep(2000);
		notePage.addNoteButton.click();
		Thread.sleep(2000);
		notePage.postNote("Note Title", "Note Description");
		notePage.noteSubmit.click();

	}

	private void noteEdit(NotePage notePage) throws InterruptedException {

		Thread.sleep(2000);
		notePage.noteTab.click();
		Thread.sleep(2000);
		notePage.noteEdit.click();
		Thread.sleep(2000);
		notePage.postNote("Edit Note Title", "Edit Note Description");
		notePage.noteSubmit.click();


	}

	private void noteDelete(NotePage notePage) throws InterruptedException {

		Thread.sleep(2000);
		notePage.noteDelete.click();
		Thread.sleep(2000);

	}
	@Test
	public void create_edit_delete_credentials() throws InterruptedException {
		doMockSignUp("Credential","Test","ABC","123");
		doLogIn("ABC", "123");
		webDriverWait = new WebDriverWait(driver, 2);
		credentialPage = new CredentialPage(driver);
		credentialAdd(credentialPage);
		credentialEdit(credentialPage);
		credentialDelete(credentialPage);
		doLogOut();
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("You have been logged out", driver.findElement(By.id("logout")).getText());
	}
	private void credentialAdd(CredentialPage credentialPage) throws InterruptedException {
		Thread.sleep(2000);
		credentialPage.navCredential.click();
		Thread.sleep(2000);
		credentialPage.addCredential.click();
		Thread.sleep(2000);
		credentialPage.credentialNote("https://www.google.com", "User", "abc123");
		credentialPage.credentialSave.click();
	}

	private void credentialEdit(CredentialPage credentialPage) throws InterruptedException {
		Thread.sleep(2000);
		credentialPage.navCredential.click();

		Thread.sleep(2000);
		credentialPage.credentialEdit.click();
		Thread.sleep(2000);
		credentialPage.credentialNote("https://www.youtube.com", "username", "987654321");
		credentialPage.credentialSave.click();
	}

	private void credentialDelete(CredentialPage credentialPage) throws InterruptedException {

		Thread.sleep(2000);
		credentialPage.credentialDelete.click();
		Thread.sleep(2000);

	}



}
