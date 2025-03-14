# Digital Bookcase - Android App

Have you ever enjoyed browsing someone's physical bookcase, discovering their interests and the stories they value?  In our increasingly digital world, many of us have transitioned to ebooks, losing that tangible bookshelf experience.  **Digital Bookcase** aims to bring that browsing pleasure to your digital library.

This Android tablet application, built with Kotlin and Jetpack Compose, transforms your ebook collection into a visually appealing and interactive digital bookshelf.  Utilizing cover images fetched from external APIs, Digital Bookcase creates a dynamic slideshow of your books, allowing you to showcase and easily access your digital reads on an old tablet or dedicated display.

## Technologies Used

Digital Bookcase is built using modern Android development tools and libraries:

* **Kotlin:**  The primary programming language, leveraging its conciseness and modern features.
* **Jetpack Compose:** Android's modern UI toolkit for building declarative and reactive user interfaces.
* **Room Persistence Library:** For efficient and robust local database management of book data.
* **Ktor Client:** For making asynchronous network requests to book information APIs.
* **Coil:** For efficient and performant image loading and caching, especially for book covers.
* **Android Navigation Component:** For managing in-app navigation between screens (Bookcase, Details, Add Book, Settings).
* **DataStore (Preferences DataStore):** For storing user settings like preferred search API.

## Phase 1: Core Features Prototype

Phase 1 focused on developing a functional prototype with all essential features for a digital bookcase experience.  This initial version includes:

* **Adding Books to Your Bookcase:**  Search and add books to your personal digital bookshelf.
* **External API Integration for Book Covers:**  Import book cover images and book details from the Google Books API (with initial OpenLibrary API integration).
* **Book Cover Slideshow:**  A visually engaging, automatically scrolling carousel of your added book covers, mimicking a physical bookcase.
* **Book Details Screen:**  Access a dedicated screen for each book, displaying:
  * Book description fetched from the API.
  * Book title, author(s), and publication date.
* **User Rating System:**  Ability to rate books on a star scale and save your personal ratings.
* **Remove from Bookcase Functionality:**  Easily remove books from your digital bookshelf.

**Demo:**

As showcased in the GIF below, Phase 1 delivers a working prototype demonstrating the core functionality.

[![Digital Bookcase App Demo](demo/DigitalBookcase_Phase_1_Demo.gif)](demo/DigitalBookcase_Phase_1_Demo.gif)

[**View Demo in Full Size**](demo/DigitalBookcase_Phase_1_Demo_900_600.gif)

## Phase 2: Future Enhancements & Ideas

Phase 2 will concentrate on refining the user experience, expanding features, and exploring new possibilities for Digital Bookcase.  Here are some ideas currently under consideration:

**UI/UX Improvements:**

* **Smoother Slideshow & Enhanced Navigation:**
  * Refine the slideshow animation for a more fluid and polished visual experience.
  * Implement a play/pause control for user-directed slideshow management.
  * Address book selection during slideshow scrolling to improve user interaction.
  * Explore adding a "glassy mirror" effect or other visual enhancements to the book cover display for added aesthetic appeal.
  * Research and implement UI/UX best practices and inspiration from existing bookcase designs.
* **Improved User Interface Design:**
  * Overhaul screen titles and overall UI elements to create a more visually appealing and engaging application.
  * Conduct UI/UX research to identify areas for improvement and inspiration for a more attractive design.

**Feature Expansion:**

* **Enhanced Book Search & API Integration:**
  * Incorporate additional book information APIs beyond Google Books (OpenLibrary integration is in progress) to improve cover image resolution and data richness.
  * Investigate and potentially integrate with the Amazon Books API (considering affiliate account requirements).
* **Import from External Services:**
  * Implement functionality to import book lists from services like Goodreads, simplifying the process of populating your digital bookcase.
* **Cross-Platform Expansion:**
  * Explore creating a TV-optimized version of the app for display on larger screens, enhancing the "showcase" aspect.
* **Customization & Settings:**
  * **Slideshow Speed Setting:** Allow users to customize the slideshow scrolling speed to their preference.
  * **"Keep Screen On" Option:** Implement a setting to prevent the tablet screen from turning off during slideshow mode, particularly when plugged in (requires testing on physical devices).
* **Search Refinements:**
  * Improve search functionality to be more robust and handle typos or partial queries more effectively, providing more relevant results.
* **Book Discovery & Recommendations:**
  * Incorporate a "Suggested Books" section to recommend books based on user ratings or other criteria (future feature exploration).
* **Add Book Workflow Enhancements:**
  * Streamline the "Add Book" workflow. For example, retain search results after adding a book to allow users to easily add multiple books from the same search query without retyping.
* **Further Ideas (To Be Explored):**  Continuously brainstorm and add new features to enhance the Digital Bookcase experience.

## Setup and Installation

To run Digital Bookcase on your Android device or emulator:

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/alexplainlater/DigitalBookcase.git
    cd digital-bookcase
    ```

2. **Open in Android Studio:** Open Android Studio and select "Open an existing project." Navigate to the cloned `digital-bookcase` directory and select the project.

3. **Build the Project:**  In Android Studio, go to **"Build" -> "Make Project"** or **"Build" -> "Rebuild Project"**.

4. **Run the App:** Select your desired Android device or emulator in the run configuration dropdown and click the "Run" button (green play icon).

**Note:** You will need to configure an API key for the Google Books API in the build.gradle.kts (Module :app) file under the buildConfigField.  Refer to Google's API documentation for instructions on setting up API keys.

## Contributions

Contributions to Digital Bookcase are welcome, however this is mainly a personal project at this time! If you have ideas for improvements, bug fixes, or new features, feel free to:

* **Report Bugs:**  Open an issue on GitHub to report any bugs or unexpected behavior you encounter.
* **Suggest Enhancements:**  Submit feature requests or suggestions as issues to share your ideas for improving the app.

## License

This project is licensed under the **[MIT License]**. See the `LICENSE` file for more details.
