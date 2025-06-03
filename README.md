# Cities App

This module implements the core functionality for managing and displaying a list of cities, including search, favorites, and offline support.

---

## Overview

- The app retrieves and displays cities from both local (Room) and remote (API) sources.
- It supports offline-first behavior, caching the data locally for a responsive user experience.
- Users can:
  - Search for cities
  - Toggle favorites
  - View detailed information on selected cities.

---

## Search Functionality

### Approach

- **Database Search**:  
  Search queries are executed directly in the database layer using Room DAO methods (dao.searchCities(prefix) and dao.searchFavouriteCities(prefix)).
- **Why?**  
  This approach leverages SQLite’s built-in query capabilities (e.g. "LIKE") for efficient, indexed filtering — especially valuable with large datasets.
- **Benefit**:  
  Avoids loading all cities into memory, ensuring better performance and lower memory usage compared to an in-memory search.

## Key Design Decisions

- **Offline Support**:  
  The app first loads cities from the local database before fetching updated data from the API, providing a fast user experience even with limited connectivity.

- **Measured fetchin times**:
  5 sec with no paged api call
  13 sec witn paged api call
  
- **How to use both options**:
  on CitiesNavGraph, row 39 , toogle this line between false and true . "usePaging = false,"

- **Favorite Persistence**:  
  The "isFavourite" field is persisted locally and merged with remote data to retain user preferences after a data refresh.
- **Paging**:  
  For large datasets, the app uses the Paging 3 library with a RemoteMediator to efficiently load paged city data.
- **Testing**:  
  The app uses both instrumented and unit tests to ensure correctness. For ViewModels, dependencies are mocked using "mockito" in Android tests.


## Assumptions

- The search functionality is implemented at the database level via Room queries rather than in-memory filtering.
- Data merging logic ensures that user-favorite selections are maintained across app sessions.
