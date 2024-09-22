package task1.stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class StepDefinition {
    public WebDriver driver;
    List<WebElement> toys;
    List<WebElement> toysWithAddToCart;
    double[] searchPagePrices = new double[2];
    double summaryPrice;
    double[] cartPagePrices = new double[2];
    double cartPrice;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\AunAbbas\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\AunAbbas\\Downloads\\chrome-win64\\chrome-win64\\chrome.exe");
        options.addArguments("--user-data-dir=C:\\Users\\AunAbbas\\AppData\\Local\\Google\\Chrome for Testing\\User Data\\");
        options.addArguments("--profile-directory=Default");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    }

    public double toyPrice(WebElement element) {
        String priceText = element.getText();
        String[] priceParts = priceText.split("\n");
        String dollars = priceParts[0].replace("$", "");
        String cents = priceParts[1];
        String fullPrice = dollars + "." + cents;
        return Double.parseDouble(fullPrice);
    }

    @Given("The user navigates to Amazon Website")
    public void the_user_navigates_to_amazon_website() {
        driver.get("https://www.amazon.com");
    }

    @And("Searches for toys")
    public void searches_for_toys() {
            driver.findElement(By.id("twotabsearchtextbox")).sendKeys("toys" + Keys.ENTER);
            toys = driver.findElements(By.xpath("//div[contains(@cel_widget_id,'MAIN-SEARCH_RESULTS')]"));

            toysWithAddToCart = toys.stream().filter(toy -> {
                try {
                    toy.findElement(By.xpath(".//div[@data-csa-c-action-name='addToCart']"));
                    return true;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }).toList();
    }

    @When("Add two Products to the Cart")
    public void add_two_products_to_the_cart() throws InterruptedException {
        searchPagePrices[0] = toyPrice(toysWithAddToCart.get(0).findElement(By.xpath(".//span[@class='a-price']")));
        searchPagePrices[1] = toyPrice(toysWithAddToCart.get(1).findElement(By.xpath(".//span[@class='a-price']")));
        toysWithAddToCart.get(0).findElement(By.xpath(".//div[@data-csa-c-action-name='addToCart']")).click();
        toysWithAddToCart.get(1).findElement(By.xpath(".//div[@data-csa-c-action-name='addToCart']")).click();
        Thread.sleep(2000);
        summaryPrice = Double.parseDouble(driver.findElement(By.xpath("//span[@class='ewc-subtotal-amount']")).getText().replace("$", ""));
        driver.findElement(By.xpath("//div[@data-cart-type='Retail_Cart']//span[@class='a-button-inner']")).click();
        Thread.sleep(5000);
        cartPagePrices[0] = Double.parseDouble(driver.findElements(By.xpath("//span[@class='a-size-medium a-color-base sc-price sc-white-space-nowrap sc-product-price a-text-bold']")).get(0).getText().replace("$", ""));
        cartPagePrices[1] = Double.parseDouble(driver.findElements(By.xpath("//span[@class='a-size-medium a-color-base sc-price sc-white-space-nowrap sc-product-price a-text-bold']")).get(1).getText().replace("$", ""));
        cartPrice = Double.parseDouble(driver.findElement(By.id("sc-subtotal-amount-activecart")).getText().replace("$",""));
        Arrays.sort(cartPagePrices);
        Arrays.sort(searchPagePrices);
    }

    @Then("Validate the prices")
    public void validate_the_prices() {
        Assert.assertTrue(Arrays.equals(cartPagePrices,searchPagePrices), "Prices in search and cart pages don't match!");
        System.out.println("Item Prices in Summary Page and Cart Page is same");
        Assert.assertEquals(summaryPrice, cartPrice, "Total prices are not same");
        System.out.println("Total Cart in Summary Page and Cart Page is same");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
