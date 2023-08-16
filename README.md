# InnoBookingBot

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-purple.svg)](https://opensource.org/licenses/MIT)

This bot allows to make bookings in Innopolis University, look through them, update and delete.
Moreover, it provides an opportunity to overview all available rooms and timeslots.

### Project status

Both bot and its API are **offline**.
Project can be enhanced to your needs and launched again on your personal hosting.
See **Setup** section for more details

### You can try to use our bot [here](https://t.me/InnoBooking_bot)

Or you can use telegram tag: @InnoBooking_bot

## How to use?

When you enter the bot, you need to log in (using your Innopolis (Outlook) email),
then enter the code that was sent to your email. After that, you need to open
WebApp and use navigation page to select option you're interested in.
Then just follow instructions on corresponding pages and make the following:

<ul>
    <li>Book Room - main page for making your books</li>
    <li>All Bookings - page with all bookings at the calendar</li>
    <li>My Bookings - list of your bookings</li>
    <li>Rules - list of rules that you need to notice</li>
</ul>

You can delete your booking at both `My Bookings` and `All Bookings` pages

### For your convenience, Russian localization was added

## Features list:

<ul>
    <li>Bot displays available rooms and allow to book them for any suitable period of time</li>
    <li>Bot provides a handler registration mechanism</li>
    <li>User can see booking rules</li>
    <li>User can see his bookings and delete them</li>
    <li>User can see all bookings</li>
    <li>User is notified a few minutes before the end of his booking</li>
    <li>Web-app inside the bot provides the same functionality</li>
</ul>

## Project Demo

### General view of WebApp:

<img src="https://i.ibb.co/jfjSPKg/general.png" alt="general" width="600">

### Booking Page:

<img src="https://i.ibb.co/JtHQt4y/book.png" alt="booking" width="350">

### Booking Confirmation

<img src="https://i.ibb.co/jMztQ66/book-fin.png" alt="notification" width="350">

### The list of your bookings

<img src="https://i.ibb.co/0Y8nx8C/my.png" alt="my" width="350">

### Choosing rooms in All Bookings

<img src="https://i.ibb.co/hdw2BcB/current.png" alt="current" width="350">

### Detailed bookings

<img src="https://i.ibb.co/hdw2BcB/current.png" alt="current" width="350">

### Rules

<img src="https://i.ibb.co/7z4YpvL/rules.png" alt="rules" width="350">

## Setup

### Bot

Bot can be deployed with docker:

```shell
docker build -t innobookingbot .
docker run -p <port>:<port> innobookingbot
```

Next environmental variables should be provided:

```
BOT_TOKEN='your bot token'
EMAIL_AUTH='email sender, e.g. i.ivanov@innopolis.university'
EMAIL_AUTH_PASSWORD='email password'
DATABASE_URL='firestore database url'
PROJECT_ID='firebase project ID'
CREDENTIALS_PATH='firebase project .json file'
PORT='desired port (maybe chosen by the host)'
```

If you want to build bot on your machine explicitly, you can use gradle:

```shell
./gradlew build
./gradlew run
```

### WebApp

[Link to the WebApp](https://github.com/dmhd6219/booking-bot-frontend)

In a WebApp directory you can find corresponding README file that will you launch
our project on your machine.

## Technologies and Tools:

### Languages:

Kotlin (Bot itself) + TypeScript (WebApp)

### Bot + API:

1. [Ktor (Client + Server)](https://ktor.io/)
2. [Exposed ORM + JDBC (SQLite)](https://github.com/JetBrains/Exposed)
3. [kotlin-telegram-bot](https://github.com/kotlin-telegram-bot/kotlin-telegram-bot)
4. [firebase-admin](https://firebase.google.com/docs/admin/setup)
5. [JavaMail](https://javaee.github.io/javamail/)
6. [Kaml](https://github.com/charleskorn/kaml)
7. [logback](https://github.com/qos-ch/logback)

### WebApp:

1. [React](https://react.dev/)
2. [Ant Design of React](https://ant.design/docs/react/introduce)
3. [styled-components](https://styled-components.com/)
4. [React Router](https://reactrouter.com/en/main)
5. [react-telegram-web-app](https://github.com/vkruglikov/react-telegram-web-app)
6. [firebase](https://www.npmjs.com/package/firebase)

### Testing:

1. [Mockito](https://site.mockito.org/), [JUnit 5](https://junit.org/junit5/), [rest-assured](https://github.com/rest-assured/rest-assured)
2. [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)
