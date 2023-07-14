# InnoBookingBot

This bot allows to make bookings in Innopolis University, look through them, update and delete.
Moreover, it provides an opportunity to overview all available rooms and timeslots.

### You can try to use our bot [here](https://t.me/InnoBooking_bot)

Or you can use telegram tag: @InnoBooking_bot

## How to use?

When you enter the bot, you need to log in (using your Innopolis (Outlook) email),
then enter the code that was sent to your email. After that, you need to open
WebApp and use navigation page to select option you're interested in.
Then just follow instructions on corresponding pages and make the following:

1. `Book Room` - main page for making your books
2. `All Bookings` - page with all bookings at the calendar
3. `My Bookings` - list of your bookings
4. `Rules` - list of rules that you need to notice

You can delete your booking at both `My Bookings` and `All Bookings` pages

### For your convenience, Russian localization was added

## Project Demo

### General view of WebApp:

![general_view](https://i.ibb.co/jfjSPKg/general.png)

### Booking Page:

![book](https://i.ibb.co/JtHQt4y/book.png)

### Booking Confirmation

![confirm](https://i.ibb.co/jMztQ66/book-fin.png)

### The list of your bookings

![my](https://i.ibb.co/0Y8nx8C/my.png)

### Choosing rooms in All Bookings

![chooseRoom](https://i.ibb.co/9qvS0Tv/rooms-Choose.jpg)

### Detailed bookings

![current](https://i.ibb.co/0fjG85z/current.jpg)

### Rules

![rules](https://i.ibb.co/7z4YpvL/rules.png)

## Setup

Link to the WebApp: https://gitlab.pg.innopolis.university/swp29/innobookingfrontend

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
6. [logback](https://github.com/qos-ch/logback)

### WebApp:

1. [React](https://react.dev/)
2. [Ant Design of React](https://ant.design/docs/react/introduce)
3. [styled-components](https://styled-components.com/)
4. [React Router](https://reactrouter.com/en/main)
5. [react-telegram-web-app](https://github.com/vkruglikov/react-telegram-web-app)

### Testing:

1. [Mockito](https://site.mockito.org/), [JUnit 5](https://junit.org/junit5/), [rest-assured](https://github.com/rest-assured/rest-assured)
2. [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)