practical_test
==============

practical_test


ANDROID PRACTICAL TEST
======================

1. create one simple screen which will show the data got from api using get or post method and dislplay it on the screen using scoll view or table view or any other component you can feel comfortable with.

2. this is a small chat screen where only data is displayed of chat history between two users.

3. the api to get the chat history is : http://50.2.223.175/androidtest/getchathistory.php?page=1 . ths api takes just one parameter and it's a "page".

4. the screen should display only records from page=1 first and when user scrolls down it should detect the scroll event and detect the end of sceen and display a toast message "loading more data" and then fetch next page from the api and add those data to the existing screen at the end and same goes on when user again scroll down to the end of the page. If the api returns blank json array then display toast message "no more chat history to diaplay".

5. each row on the screen should contain one record and each record should display userid, msg, datetime. Refer to screen of watsapp chat screen for the design reference. just make simple design not the complicated one.

6. the api requests made to the server should be done using asynctask class of the android.

#IMPORTANT#

7. The screen should be able to set the text size and height and width of the other used components automatically as per the screen resolution. It should be atleast able to detect and set the screen automatically for the screen sizes hdpi, xhdpi, xxhdpi, xxxhdpi and if possible should be able to set screen for landscape and portrait modes.