package com.github.multumit.buyme;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class SearchMenuDropdown {
    private final WebElement listContainer;

    public SearchMenuDropdown(WebElement listContainer) {
        this.listContainer = listContainer;
    }

    // reveal will click on the dropdown icon and reveal the menu itself
    private void reveal(Actions actions) {
        WebElement listReveal = listContainer.findElement(By.cssSelector("a[class='chosen-single']"));
        actions.moveToElement(listReveal).click(listReveal).perform();
    }

    // selectItem assumes that the selection menu is visible and it will select the i-th entry that was revealed
    private void selectItem(Actions actions, int listItemToSelect) {
        WebElement unorderedList = this.listContainer.findElement(By.cssSelector("ul[class='chosen-results'"));
        List<WebElement> li = unorderedList.findElements(By.tagName("li"));
        WebElement targetLi = li.get(listItemToSelect);

        actions.moveToElement(targetLi).click().perform();
    }

    public void makeMenuSelection(Actions actions, int actionWaitTime, int listItemToSelect) throws InterruptedException {
        this.reveal(actions);
        Thread.sleep(actionWaitTime);
        this.selectItem(actions, listItemToSelect);
        Thread.sleep(actionWaitTime);
    }
}
