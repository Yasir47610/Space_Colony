# ⚽ Soccer Team Management App
### OOP Week 11 Exercise — Generics · Iterators · Lambdas · Android

---

## Project Overview

An Android application that manages soccer teams, players, and matches using
hardcoded sample data. The app showcases generics, the Iterator pattern, and
lambda expressions across a clean layered architecture.

---

## Architecture & Package Structure

```
com.soccerapp/
├── model/                  ← Domain entities
│   ├── SoccerEntity.java   ← Interface: getId(), getName()
│   ├── Team.java
│   ├── Player.java
│   └── Match.java
│
├── iterator/               ← Custom Iterator pattern
│   ├── CustomIterator.java ← Generic interface: hasNext(), next()
│   ├── TeamIterator.java   ← Implements CustomIterator<Team>
│   └── MatchIterator.java  ← Implements CustomIterator<Match>
│
├── repository/             ← Generic data layer
│   ├── Repository.java         ← Generic<T extends SoccerEntity>: add/getAll/filter
│   ├── TeamRepository.java     ← extends Repository<Team>
│   ├── PlayerRepository.java   ← extends Repository<Player>
│   └── MatchRepository.java    ← extends Repository<Match>
│
├── data/
│   └── DataProvider.java   ← Hardcoded sample data factory
│
├── adapter/
│   └── SoccerAdapter.java  ← Generic RecyclerView adapter
│
├── ui/
│   ├── teams/TeamsFragment.java
│   ├── players/PlayersFragment.java
│   └── matches/MatchesFragment.java
│
└── MainActivity.java       ← TabLayout + global search bar
```

---

## Requirements Coverage

### 1. Generic Classes (≥ 2)

| Class | Type Parameter | Bound |
|-------|---------------|-------|
| `Repository<T>` | T | `extends SoccerEntity` |
| `SoccerAdapter<T>` | T | `extends SoccerEntity` |
| `CustomIterator<T>` | T | unbounded interface |

`Repository<T extends SoccerEntity>` is the core generic class. It stores any
entity that implements `SoccerEntity`, giving access to `getId()` and `getName()`
without casting. `TeamRepository`, `PlayerRepository`, and `MatchRepository` all
extend it with concrete type arguments.

`SoccerAdapter<T extends SoccerEntity>` is a single generic RecyclerView adapter
reused across all three tabs.

### 2. Custom Iterators (≥ 1)

`CustomIterator<T>` — our own interface mirroring `java.util.Iterator`:
```java
public interface CustomIterator<T> {
    boolean hasNext();
    T next();
}
```

`TeamIterator` implements `CustomIterator<Team>`:
```java
TeamIterator it = new TeamIterator(teamRepo.getAll());
while (it.hasNext()) {
    Team t = it.next();
    Log.d("TeamIterator", "Visited: " + t.getName());
}
```

`MatchIterator` implements `CustomIterator<Match>` — demonstrated in
`MatchesFragment`.

Both iterators support `reset()` so they can be traversed more than once.

### 3. Lambda Expressions (≥ 2 in different contexts)

| # | Location | Lambda Type | Example |
|---|----------|-------------|---------|
| 1 | `Repository.filter()` | `Predicate<T>` | `filter(t -> t.getLeague().equals("La Liga"))` |
| 2 | `Repository.searchByName()` | `Predicate<T>` | `filter(item -> item.getName().toLowerCase().contains(lower))` |
| 3 | `TeamRepository.sortedByName()` | `Comparator<Team>` | `(a,b) -> a.getName().compareToIgnoreCase(b.getName())` |
| 4 | `SoccerAdapter` | `Function<T,String>` | `team -> team.getName() + " · " + team.getLeague()` |
| 5 | `SoccerAdapter` click listener | callback / SAM | `adapter.setOnItemClickListener(team -> Toast.show(...))` |
| 6 | `MainActivity` TabLayoutMediator | SAM lambda | `(tab, pos) -> tab.setText(...)` |
| 7 | `MainActivity` TextWatcher | SAM lambda | `onTextChanged: s -> fragment.applySearch(query)` |

### 4. OOP Principles Applied

- **Interface segregation** — `SoccerEntity` is minimal (2 methods); keeps
  entity classes free to add their own fields without constraint.
- **Open/closed** — `Repository<T>` is open for extension
  (`TeamRepository`, etc.) but closed for modification.
- **Encapsulation** — all model fields are `private final` where possible;
  setters only where business logic requires mutation.
- **Error handling** — constructors throw `IllegalArgumentException` for
  invalid data; iterators throw `NoSuchElementException` when exhausted.

---

## UI Features

| Feature | Implementation |
|---------|---------------|
| 3-tab navigation | `TabLayout` + `ViewPager2` + `FragmentStateAdapter` |
| Live search | `EditText` with lambda `TextWatcher` |
| Sort / Filter | `Spinner` per tab, delegates to repository methods |
| Item click detail | Lambda `OnItemClickListener` showing a `Toast` |
| Card list design | `RecyclerView` + `CardView` items |

---

## How to Build & Run

1. **Clone / download** this repository into Android Studio (Arctic Fox or newer).
2. **Sync Gradle** — Android Studio will download dependencies automatically.
3. **Run** on an emulator or device with API 26+.

### Dependencies (`app/build.gradle`)

```groovy
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
implementation 'androidx.fragment:fragment:1.6.2'
```

Java 8 source compatibility is enabled for lambda and stream support:
```groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

---

## Running the Unit Tests

```bash
./gradlew test
```

Tests in `SoccerAppTest.java` cover:
- Repository CRUD and lambda filter correctness
- `TeamIterator` / `MatchIterator` traversal, reset, and exhaustion handling
- Domain-specific repository query methods
- Model constructor validation (invalid inputs throw exceptions)
- DataProvider data integrity

---

## Sample Data Summary

| Collection | Count |
|-----------|-------|
| Teams | 8 (FC Barcelona, Man Utd, Bayern, Juventus, PSG, Ajax, River Plate, Flamengo) |
| Players | 12 (Messi, Ronaldo, Lewandowski, De Bruyne, Van Dijk, Neuer, Mbappé, Haaland, Fernandes, Kimmich, Oblak, Neymar) |
| Matches | 8 (La Liga, Premier League, Bundesliga, Serie A, Ligue 1, Champions League) |

---

## Class Diagram Summary

```
SoccerEntity (interface)
    └── implemented by: Team, Player, Match

CustomIterator<T> (interface)
    └── implemented by: TeamIterator, MatchIterator

Repository<T extends SoccerEntity>
    ├── extended by: TeamRepository   — manages Team,   adds filterByLeague()
    ├── extended by: PlayerRepository — manages Player, adds filterByTeam(), filterByPosition()
    └── extended by: MatchRepository  — manages Match,  adds filterByTeam(), filterByLeague()

DataProvider
    └── creates: Team list, Player list, Match list → feeds the repositories

SoccerAdapter<T extends SoccerEntity>  (generic RecyclerView adapter)
    └── used by: TeamsFragment, PlayersFragment, MatchesFragment
```
