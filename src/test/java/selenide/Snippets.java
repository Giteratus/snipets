package selenide;

import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.*;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// this is not a full list, just the most common
public class Snippets {

  void browser_command_examples() {
    // Открывает страницу по полному URL
    open("https://google.com");
    // Открывает страницу по относительному пути (требуется -Dselenide.baseUrl=...)
    open("/customer/orders");     // -Dselenide.baseUrl=http://123.23.23.1
    // Открывает страницу с HTTP Basic-аутентификацией
    open("/", AuthenticationType.BASIC,
            new BasicAuthCredentials("", "user", "password"));

    // Имитирует нажатие кнопки «Назад» в браузере
    Selenide.back();
    // Обновляет текущую страницу (F5)
    Selenide.refresh();

    // Очищает все cookies в браузере
    Selenide.clearBrowserCookies();
    // Очищает localStorage браузера
    Selenide.clearBrowserLocalStorage();
    // Очищает sessionStorage через JS (нет отдельной команды Selenide)
    executeJavaScript("sessionStorage.clear();"); // no Selenide command for this yet

    // Нажимает OK в модном/алерт-диалоге
    Selenide.confirm(); // OK in alert dialogs
    // Нажимает Cancel в модном/алерт-диалоге
    Selenide.dismiss(); // Cancel in alert dialogs

    // Закрывает текущее окно/вкладку
    Selenide.closeWindow(); // close active tab
    // Полностью закрывает браузер (WebDriver)
    Selenide.closeWebDriver(); // close browser completely

    // Переключает фокус на фрейм по имени/ID
    Selenide.switchTo().frame("new");
    // Возвращает фокус из фрейма в основной DOM
    Selenide.switchTo().defaultContent(); // return from frame back to the main DOM

    // Переключает фокус на окно/вкладку по имени
    Selenide.switchTo().window("The Internet");

    // Создаёт cookie и добавляет её в браузер через WebDriver
    var cookie = new Cookie("foo", "bar");
    WebDriverRunner.getWebDriver().manage().addCookie(cookie);


  }

  void selectors_examples() {
    // Находит первый div на странице и кликает
    $("div").click();
    // То же самое, но через метод element()
    element("div").click();

    // Находит третий div (индексация с 0: 0,1,2 → 2 = третий)
    $("div", 2).click(); // the third div

    // Поиск по XPath и клик
    $x("//h1/div").click();
    // То же самое, но через селектор byXpath
    $(byXpath("//h1/div")).click();

    // Поиск элемента, содержащего точный текст "full text"
    $(byText("full text")).click();
    // Поиск элемента, содержащего подстроку "ull tex"
    $(withText("ull tex")).click();

    // Поиск по тегу и тексту
    $(byTagAndText("div", "full text"));
    // Поиск по тегу и подстроке в тексте
    $(withTagAndText("div", "ull text"));

    // Переход к родительскому элементу
    $("").parent();
    // Переход к соседнему элементу (индекс 1 = второй сосед)
    $("").sibling(1);
    // Переход к предыдущему элементу (1 шаг назад)
    $("").preceding(1);
    // Поиск ближайшего родительского div (по CSS-селектору)
    $("").closest("div");
    // То же самое, что closest (устаревшее/альтернативное название)
    $("").ancestor("div"); // the same as closest
    // Выбор последнего child-элемента div
    $("div:last-child");

    // Цепочка: от div к вложенному h1, затем поиск по тексту и клик
    $("div").$("h1").find(byText("abc")).click();
    // Поиск по атрибуту (устаревший стиль, редко нужен)
    // very optional
    $(byAttribute("abc", "x")).click();
    // То же самое, но через CSS-селектор [abc=x]
    $("[abc=x]").click();

    // Поиск элемента по id="mytext"
    $(byId("mytext")).click();
    // То же самое через CSS-селектор #mytext
    $("#mytext").click();

    // Поиск по классу .red
    $(byClassName("red")).click();
    // То же самое через CSS-селектор .red
    $(".red").click();
  }

  void actions_examples() {
    // Обычный клик по элементу
    $("").click();
    // Двойной клик
    $("").doubleClick();
    // Клик правой кнопкой (контекстное меню)
    $("").contextClick();

    // Наведение курсора (hover)
    $("").hover();

    // Ввод текста в поле (заменяет текущее значение)
    $("").setValue("text");
    // Добавление текста к текущему значению
    $("").append("text");
    // Очистка поля
    $("").clear();
    // Ещё один способ очистки (установка пустой строки)
    $("").setValue(""); // clear

    // Отправка символа/клавиши на элемент
    $("div").sendKeys("c"); // hotkey c on element
    // Отправка клавиши на всё приложение (глобально)
    actions().sendKeys("c").perform(); //hotkey c on whole application
    // Комбинация Ctrl+F через Actions
    actions().sendKeys(Keys.chord(Keys.CONTROL, "f")).perform(); // Ctrl + F
    // То же самое, но отправляется на элемент html
    $("html").sendKeys(Keys.chord(Keys.CONTROL, "f"));

    // Нажатие Enter в поле
    $("").pressEnter();
    // Нажатие Escape
    $("").pressEscape();
    // Нажатие Tab
    $("").pressTab();


    // Сложное действие: переместить курсор, зажать кнопку мыши, сдвинуть, отпустить
    // complex actions with keybord and mouse, example
    actions().moveToElement($("div")).clickAndHold().moveByOffset(300, 200).release().perform();

    // Выбор опции в <select> (не всегда работает с современными фреймворками)
    // old html actions don't work with many modern frameworks
    $("").selectOption("dropdown_option");
    // Выбор радио-кнопки по значению
    $("").selectRadio("radio_options");

  }

  void assertions_examples() {
    // Проверка, что элемент видим
    $("").shouldBe(visible);
    // Проверка, что элемент не видим
    $("").shouldNotBe(visible);
    // Проверка, что текст элемента содержит подстроку
    $("").shouldHave(text("abc"));
    // Проверка, что текст элемента НЕ содержит подстроку
    $("").shouldNotHave(text("abc"));
    // Проверка, что элемент появится (условие appear)
    $("").should(appear);
    // Проверка, что элемент НЕ появится
    $("").shouldNot(appear);


    // Увеличенный таймаут ожидания (здесь 30 секунд)
    //longer timeouts
    $("").shouldBe(visible, Duration.ofSeconds(30));

  }

  void conditions_examples() {
    // Условие: элемент должен быть видим
    $("").shouldBe(visible);
    // Условие: элемент должен быть скрыт
    $("").shouldBe(hidden);

    // Текст содержит подстроку
    $("").shouldHave(text("abc"));
    // Точное совпадение всего текста
    $("").shouldHave(exactText("abc"));
    // Текст с учётом регистра (содержит подстроку)
    $("").shouldHave(textCaseSensitive("abc"));
    // Точное совпадение текста с учётом регистра
    $("").shouldHave(exactTextCaseSensitive("abc"));
    // Проверка текста по регулярному выражению
    $("").should(matchText("[0-9]abc$"));

    // Проверка CSS-класса
    $("").shouldHave(cssClass("red"));
    // Проверка конкретного CSS-свойства и значения
    $("").shouldHave(cssValue("font-size", "12"));

    // Проверка значения input/textarea (частичное совпадение)
    $("").shouldHave(value("25"));
    // Точное совпадение значения
    $("").shouldHave(exactValue("25"));
    // Проверка, что поле пустое
    $("").shouldBe(empty);

    // Проверка наличия атрибута
    $("").shouldHave(attribute("disabled"));
    // Проверка атрибута и его значения
    $("").shouldHave(attribute("name", "example"));
    // Проверка атрибута по регулярному выражению
    $("").shouldHave(attributeMatching("name", "[0-9]abc$"));

    // Для чекбоксов: должен быть отмечен
    $("").shouldBe(checked); // for checkboxes

    // Проверка, что элемент есть в DOM (не обязательно видим!)
    // Warning! Only checks if it is in DOM, not if it is visible! You don't need it in most tests!
    $("").should(exist);

    // Проверка атрибута disabled (не всегда работает с современными фреймворками)
    // Warning! Checks only the "disabled" attribute! Will not work with many modern frameworks
    $("").shouldBe(disabled);
    // Проверка, что элемент не disabled
    $("").shouldBe(enabled);
  }

  void collections_examples() {

    // Просто получает коллекцию всех div, без проверок
    $$("div"); // does nothing!

    // Коллекция по XPath
    $$x("//div"); // by XPath

    // selections
    // Фильтрация коллекции по тексту, проверка размера
    $$("div").filterBy(text("123")).shouldHave(size(1));
    // Исключение элементов с текстом "123", проверка размера
    $$("div").excludeWith(text("123")).shouldHave(size(1));

    // Клик по первому элементу коллекции
    $$("div").first().click();
    // То же самое через elements()
    elements("div").first().click();
    // $("div").click();
    // Клик по последнему элементу коллекции
    $$("div").last().click();
    // Клик по элементу с индексом 1 (второй в списке)
    $$("div").get(1).click(); // the second! (start with 0)
    // То же самое, но короче
    $("div", 1).click(); // same as previous
    // Поиск первого элемента с текстом "123" в коллекции и клик
    $$("div").findBy(text("123")).click(); //  finds first

    // assertions
    // Проверка, что коллекция пуста (размер 0)
    $$("").shouldHave(size(0));
    // То же самое через CollectionCondition.empty
    $$("").shouldBe(CollectionCondition.empty); // the same

    // Проверка текстов элементов (частичное совпадение, порядок важен)
    $$("").shouldHave(texts("Alfa", "Beta", "Gamma"));
    // Точное совпадение текстов всех элементов
    $$("").shouldHave(exactTexts("Alfa", "Beta", "Gamma"));

    // Тексты должны совпасть, но порядок может быть любым
    $$("").shouldHave(textsInAnyOrder("Beta", "Gamma", "Alfa"));
    // Точное совпадение текстов в любом порядке, с учётом регистра
    $$("").shouldHave(exactTextsCaseSensitiveInAnyOrder("Beta", "Gamma", "Alfa"));

    // В коллекции есть элемент с текстом "Gamma" (хотя бы один)
    $$("").shouldHave(itemWithText("Gamma")); // only one text

    // Проверки размера коллекции
    $$("").shouldHave(sizeGreaterThan(0));
    $$("").shouldHave(sizeGreaterThanOrEqual(1));
    $$("").shouldHave(sizeLessThan(3));
    $$("").shouldHave(sizeLessThanOrEqual(2));


  }

  void file_operation_examples() throws FileNotFoundException {

    // Скачивание файла по ссылке <a href="...">
    File file1 = $("a.fileLink").download(); // only for <a href=".."> links
    // Скачивание файла через контейнер (div и т.п.), более универсально, но могут быть нюансы с Grid/Selenoid
    File file2 = $("div").download(DownloadOptions.using(FileDownloadMode.FOLDER)); // more common options, but may have problems with Grid/Selenoid

    // Загрузка файла с диска в input type="file"
    File file = new File("src/test/resources/readme.txt");
    $("#file-upload").uploadFile(file);
    // Загрузка файла из classpath (ресурсы теста)
    $("#file-upload").uploadFromClasspath("readme.txt");
    // Не забыть отправить форму (если это форма загрузки)
    // don't forget to submit!
    $("uploadButton").click();
  }

  void javascript_examples() {
    // Выполнение JS в браузере: показать alert
    executeJavaScript("alert('selenide')");
    // Выполнение JS с аргументами
    executeJavaScript("alert(arguments[0]+arguments[1])", "abc", 12);
    // Выполнение JS с возвратом значения в Java-переменную
    long fortytwo = executeJavaScript("return arguments[0]*arguments[1];", 6, 7);

  }
}