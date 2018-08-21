# Near Me
  Near Me is my Capstone project for Udacity Android Developer Nanodegree. This app is suitable to travelers
who want to explore new places around their current location. The user can search for places via some categories or
via text. The user can also save his favorite places so as to find them faster and he can keep in them on his home screen via app's widget.
## Features
Near Me uses the following:
* Firebase Realtime Database
* Maps SDK for Android
* Places API
* Firebase Authentication
* AdMob
* Material Design
## Prerequisites for own use
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

You need to create a resource file under values directory.
```
app/main/res/values/secrets.xml
```
In this file, you need to add your API key as below:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="API_KEY">"YOUR_API_KEY"</string>
</resources>
```
## Screenshots
When the user opens the app for the first time, he needs to sign in:
![Sign In](/screenshots/sign_in.png)
Or he creates a new account:
![Sign Up](/screenshots/sign_up.png)
Then, he goes to the main activity where are 2 tabs, the home:
![Home](/screenshots/home_screen.png)
And the favorites:
![Favorites](/screenshots/favorites_screen.png)
He can logout pressing this button:
![Logout](/screenshots/home_logout_screen.png)
He can search writing text here or pressing one of the categories below:
![Text Search](/screenshots/text_search_screen.png)
These are the results:
![Search Results](/screenshots/text_search_results_screen.png)
After choosing one of the results, the user can see more details about the place:
![Place Details](/screenshots/place_details_screen.png)
He can save this place pressing the fab button:
![Place Details Favorite](/screenshots/place_details_favorite_screen.png)
When the user creates a widget on his home screen, he needs to choose what favorites wants to keep on his widget:
![Widget Configuration](/screenshots/widget_configuration_screen.png)
The widget looks like this:
![Widget](/screenshots/widget.png)

## License
See the [LICENCSE](/LICENSE.md) file for license rights and limitations (MIT).
