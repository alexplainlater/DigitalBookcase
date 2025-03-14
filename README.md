# Digital Bookcase

An Android tablet app built with Kotlin and Jetpack Compose that showcases digital books.

I always enjoy browsing bookcases while at friends' houses, sometimes just to see what interesting things they're into and what they want to showcase to the world.  Over the last few years, most of my reading is done on my tablet with ebooks and there's no way to show those off.  I had an old tablet lying around that I thought would be perfect to scroll through book covers just like a physical bookshelf.  From that idea, this project was born.

## Phase 1

In phase 1, I wanted to get a working prototype working that has all of the core features I'm looking for including:

- Add books to bookshelf
- Import book covers from an external API
- Have a slideshow of the book covers
- Include a details section that provides a description of the book (from the API)
- Ability to rate the book
- Ability to remove books from the bookshelf

Here is a demo of the app in phase 1:

![Digital Bookcase App Demo](demo/DigitalBookcase_Phase_1_Demo.gif)

## Phase 2 Ideas

- I'd like to improve the slideshow component to be more smooth
  - Maybe also include a play/pause button
  - Currently gets a little hard to select a book if it's in the middle of a scroll.
  - Would like kind of a glassy mirror feature on the bottom of the books
  - I need to research some inspiration on what I'm looking for
- I'd like to improve the user interface so its more attractive to use
  - One really noticeable area here is the titles of my screens are pretty vanilla
  - Also need to research some inspiration on what I'm looking for
- I'd like to include other APIs or sources for book cover images as the Google - Books API has pretty awful resolution for book covers.
  - Started looking into OpenBooks in phase 1
  - Amazon requires an affiliate account, which I don't currently have
- I'd like a way to import lists from other services like GoodReads
- Maybe also create a TV version of the app for those that would like to show on a TV
- Have a setting for scroll speed for the slideshow
- Have a setting to keep the power on when plugged in
  - Not sure if this is happening already or not, need to test on actual tablet and not just the emulator
- Search is a little wonky at times, maybe look into improving that in case of typos or something
- Have a suggested book section
- Improve workflow of Add Books
  - If I type an author and add a book, it currently resets and I have to retype the author.  May want to explore keeping previous results showing in case the user wants to select multiple search results to add.
- more to come
