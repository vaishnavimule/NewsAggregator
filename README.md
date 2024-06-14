# 📱 News Gateway App

Welcome to the **News Gateway App** repository! This Android application displays current news articles from a wide variety of news sources covering a range of categories. It leverages the NewsAPI.org service to fetch news sources and articles.

## 🌟 App Highlights

- **News Variety**: Displays news from various sources across different categories.
- **Smooth Orientation Transition**: Maintains app state between portrait and landscape orientations.
- **Category Filtering**: Select a category to filter news sources.
- **Swipe Navigation**: Swipe right to read the next article, and left to go back to the previous one.
- **View Full Articles**: Tap on the article content to view the extended article on the source's website.

## 🏛️ Application Architecture

The app architecture consists of several components that communicate via broadcasts and services to manage data retrieval and display.

### Diagram

```
Broadcast Request
For News Source Data      ->       NewsAPI.org
Broadcast Request
For News Article Data     ->       NewsAPI.org

App/Activity
Receives Broadcast
Requests for Sources and  <-->     Service
News                     Broadcast
                          Sources and News
```

## 📰 News Data

The app uses the NewsAPI.org service to fetch news sources and articles. Ensure you sign up for an API key at [NewsAPI.org](https://newsapi.org/register).

### API Endpoints

- **News Sources**:
  - All sources: `https://newsapi.org/v1/sources?language=en&country=us&apiKey=YOUR_API_KEY`
  - By category: `https://newsapi.org/v1/sources?language=en&country=us&category=CATEGORY&apiKey=YOUR_API_KEY`

- **News Articles**:
  - By source: `https://newsapi.org/v1/articles?source=SOURCE&apiKey=YOUR_API_KEY`

## 🧩 Key Features

- **User-Friendly Interface**: Simple and intuitive design for easy navigation and interaction.
- **Customizable**: Filter news by categories and navigate through articles with swipes.
- **Dynamic Updates**: Real-time updates of articles as you navigate through the app.
- **History Section**: Optionally keep track of viewed articles for future reference.

## 💻 Technologies Used

- **Java**: The primary programming language used for development.
- **Android Studio**: The official IDE for Android app development.
- **XML**: For designing the user interface.
- **Git**: Version control system for efficient collaboration and tracking of changes.

## 🚀 Getting Started

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/yourusername/news-gateway-app.git
    ```

2. **Open in Android Studio**:
    - Launch Android Studio and select "Open an existing Android Studio project."
    - Navigate to the cloned repository and open it.

3. **Setup API Key**:
    - Register at [NewsAPI.org](https://newsapi.org/register) to get your API key.
    - Replace `YOUR_API_KEY` in the code with your actual API key.

4. **Build and Run**:
    - Build the project and run it on an emulator or physical device.

## ✨ Extra Credit Features

- **Extended Articles**: Tap on the article headline, text, or image to go to the full article on the news source's website.
- **Professional Launcher Icon**: A well-designed launcher icon for the app.


---

Thank you for checking out the **News Gateway App**! If you have any questions or feedback, feel free to reach out. Happy coding! 😊



---

**Instructor:** Christopher Hield  
**Course:** CS 442: Mobile Applications Development
