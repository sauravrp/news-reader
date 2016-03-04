# news-reader
News reader that scrolls infinitely (interview question)

Requirement is to show thumbnail and title on one line and author's info on the next line.

the network call to be used is : http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873&offset=0  

note that the offset can be used to pull data for the infinite scroll.

Solution:
Tried to use robospice caching but that didn't work. It might be due to the overriden JsonDeserializer for 'MediaList'. Maybe adding a JsonSerializer to it would have worked.

In retrospect, it would have easier to shove the data in sharedpreferences than trying to get it to work using robospice framework (in order to meet the 4 hour deadline). Also launching webactivity makes it seem like its launching double activities.

Also, restore on the scroll position is a hacky solution currently. Better approach would have been to use View.onSaveInstanceState and View.onRestoreInstanceState to restore it on rotation.

Scrollig infinitely was copied from: http://www.avocarrot.com/blog/implement-infinitely-scrolling-list-android/

Screenshots:
![alt tag](https://raw.githubusercontent.com/sauravrp/news-reader/master/screenshots/demo1.png)
![alt tag](https://raw.githubusercontent.com/sauravrp/news-reader/master/screenshots/demo2.png)


