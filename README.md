# Cities App

This module implements the core functionality for managing and displaying a list of cities, including search, favorites, and offline support.


## Overview

- The app retrieves and displays cities from both local (Room) and remote (API) sources.
- It supports offline-first behavior, caching the data locally for a responsive user experience.
- Users can:
  - Search for cities
  - Toggle favorites
  - View detailed information on selected cities.


## Search Functionality

### Approach


- **Database Search with PagingSource**:  
  Search queries are executed directly in the database layer using Room DAO methods that return a **PagingSource** (e.g., `searchCitiesPaged(prefix)` and `searchFavouriteCitiesPaged(prefix)`).
- **Why?**  
  Leverages SQLite’s built-in query capabilities (`LIKE`, `COLLATE NOCASE`) for efficient, indexed filtering, while also integrating seamlessly with the Paging 3 library.
- **Benefit**:  
  Supports infinite scrolling and memory-efficient loading, ensuring smooth performance even with large datasets.

## Key Design Decisions

- **Offline Support**:  
  The app first loads cities from the local database using a PagingSource before fetching updated data from the API, ensuring a fast experience even with limited connectivity.

- **Favorite Persistence**:  
  The `isFavourite` field is persisted locally and merged with remote data to retain user preferences after data refresh.

- **Database-Backed Paging**:  
  The app uses the **Paging 3** library combined with Room’s PagingSource to efficiently load and display paged city data.
  - This approach supports smooth, infinite scrolling and efficient memory usage.
  
- **Testing**:  
  The app uses both instrumented and unit tests to ensure correctness. ViewModels and repositories are tested using mocked dependencies with **Mockito**.


## Assumptions

- Search functionality is implemented at the database level via Room queries that return **PagingSource** instead of pre-fetched lists.
- Data merging logic ensures that user-favorite selections persist across app sessions.
- Paging is now the default strategy for all city data displayed in the app.

## Maps Issues

-The first time the google map is being shown. The map is not rendering right.
-The second time the map will be rendered right.
-I was not able to solve that glitch

## Map Api Key needed

-create a secrets.properties on project root and ad this line 
GOOGLE_MAPS_API_KEY=AIzaSyAevfbZ03_PobVAgqNnV1111111-J0
with your api key on it.
