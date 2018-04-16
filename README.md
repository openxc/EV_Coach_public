# EV_Coach
Wouldn’t it be nice to know what you, as a driver, can do to better the efficiency for an electric vehicle as you’re driving it? Michigan Technological University and Ford have paired up to create an app, EV Coach, that works hand in hand with Ford’s electric vehicles to help drivers better their driving experience and battery life.

EV Coach

The EV Coach App is an Android application to help drivers of Ford Electric Vehicles(EV) drive more efficiently to get the most out of their vehicle. The application is a Multiview Android application working with the OpenXC application to calculate and score how efficiently a user is driving. Along with scoring an individual, the application also works with a Google Wear Smartwatch to give the driver haptic feedback while on the road. This feedback should teach the driver optimal driving techniques in order to maximize driving range. This app will track driver performance and provide suggestions on improving efficiency. 

Haptic Feedback

The haptic feedback tool used is a Google Wear Watch which receives messages from the main application to warn drivers when they pass a certain threshold in our scoring algorithm that will negatively affect the score they would receive. The variables that the driver has the most control over, are the easiest to improve, and that affect the battery efficiency of an Electric Vehicle are acceleration, vehicle speed, and RPM. Each variable has its own warning which has its own vibration pattern allowing the driver to distinguish them without needing to look at their watch while driving. The three patterns are also described in the vibration info page that can be accessed in the main application to allow them to learn the patterns while not driving.

Scoring Algorithm

For the scoring of the driver there are four elements that are used to calculate a total score which is then translated into a letter grade for easy understanding. The four areas that can be used to tell how efficiently a user is driving and are used in this scoring are the RPM, acceleration, speed, and MPG. Each of these categories have a threshold for what is considered a good value for each category these good and bad points are added together to create the total score for each category. This allows the scores to be calculated in real time allowing for the use of haptic feedback. To help a driver see improvements and add a social aspect to the application it also uses a remote database and local storage to store driving history for the driver.

 Storage

For the storage there are two types used. The first is the local storage that is used to save a database of past driving scores. This record of scores allows the application to create history graphs allowing the user to look at their improvement in driving as while as a pattern of how they drive whether it be on the way to work or on the way to the grocery store. The local storage only stores the previous drive. The second storage that is used for this application is a remote database through Google Firebase. Firebase stores all past drives and scores. This information is shown on a breakdown page in the app for users to explore their past grades and examine how their score has changed over time. This Firebase is used to create a leaderboard of the top three drivers in the four categories and in the total score. 

Leaderboard

This leaderboard allows users to see how they compare against others and makes the simple task of driving efficiently more social. The drivers now can see how other drivers score in acceleration, speed, MPG, and PRM so that the driver understands where they stand in the ranking and where they can do better. It gives the driver an incentive to become a better driver and also gives the user a challenge. The Leaderboard generates usernames based on the user’s email account so that the user understands who they are in the ranking but it also keeps the users email in discretion so no one else will know the user’s email. 

GraphView

GraphView is a open-source graphing framework which can be found at http://android-graphview.org/. The purpose and use for EV Coach is to allow both the EV Coach team and the potential user to be able to watch can signals in both real time and from past journeys. The reasoning is that this will allow both the user and the team to be able to see how different driving techniques ultimately affect battery life of an electric vehicle.  Currently the signals that are implemented are the battery state of charge, engine speed, and vehicle speed.

Overall, this app is designed to be a user friendly application and tool to help drivers become more efficient when driving Ford’s Electric Vehicles. With instantaneous haptic feedback, tips on how to improve, and viewable progress this app has everything needed to help improve battery efficiency. Wouldn’t you want to know how to get the most out of your vehicle?

