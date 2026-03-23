# GameOfCricket 🏏

A Spring Boot application that simulates cricket matches with comprehensive match statistics, player management, and team management capabilities.

## 📋 Table of Contents
- [Overview](#overview)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Data Models](#-data-models)
  - [Entity Classes](#entity-classes)
  - [Enumerations](#enumerations)
  - [Database Collections](#database-collections)
- [API Documentation](#-api-documentation)
  - [Player APIs](#-player-apis)
  - [Team APIs](#-team-apis)
  - [Match APIs](#-match-apis)
- [Match Simulation Engine](#-match-simulation-engine)
  - [Role-Based Ball Outcome Generator](#role-based-ball-outcome-generator)
- [Error Handling](#-error-handling)
- [Example Usage Flow](#-example-usage-flow)

## 🎯 Overview

GameOfCricket is a cricket match simulation system that allows you to:
- Create and manage players with different roles (Batsman, Bowler, All-rounder)
- Build teams with 11 players each
- Simulate complete cricket matches with realistic ball-by-ball commentary
- View detailed scorecards, innings details, and match results
- Track batting and bowling statistics

## 🛠 Technologies

- **Java 21**
- **Spring Boot 4.0.3**
- **Spring Data MongoDB**
- **Spring Web**
- **Spring Validation**
- **Lombok**
- **MongoDB**
- **Gradle**

## 📦 Prerequisites

- Java 21 or higher
- MongoDB running on `localhost:27017`
- Gradle (or use the included Gradle wrapper)

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd GameOfCricket
   ```

2. **Start MongoDB**
   ```bash
   # Make sure MongoDB is running on localhost:27017
   mongod
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

---

## 📊 Data Models

### Entity Classes

The application uses MongoDB for persistence with the following entity models:

#### 1. Player Entity
**Collection:** `players`

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier (MongoDB ObjectId) |
| `name` | String | Player name |
| `role` | PlayerRole | Player role (BATSMAN, BOWLER, ALL_ROUNDER) |

---

#### 2. Team Entity
**Collection:** `teams`

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier |
| `name` | String | Team name |
| `type` | TeamType | Team type (NATIONAL, FRANCHISE, CLUB) |
| `playerIds` | List\<String\> | List of 11 player IDs |

---

#### 3. Match Entity
**Collection:** `matches`

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier |
| `teamAId` | String | First team ID |
| `teamBId` | String | Second team ID |
| `toss` | Toss | Toss details (embedded) |
| `maxOvers` | int | Maximum overs per innings |
| `firstInningsId` | String | Reference to first innings |
| `secondInningsId` | String | Reference to second innings |
| `result` | MatchResult | Match result (embedded) |
| `status` | MatchStatus | Match status |

---

#### 4. Innings Entity
**Collection:** `innings`

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier |
| `battingTeamId` | String | Batting team ID |
| `bowlingTeamId` | String | Bowling team ID |
| `targetScore` | int | Target score to chase (0 for first innings) |
| `totalRuns` | int | Total runs scored |
| `totalWickets` | int | Total wickets fallen |
| `totalExtras` | int | Total extra runs |
| `legalBallsBowled` | int | Number of legal balls bowled |
| `battingCards` | List\<BattingScoreCard\> | Batting statistics for all players |
| `bowlingCards` | List\<BowlingScoreCard\> | Bowling statistics for all bowlers |
| `fallOfWickets` | List\<FallOfWicket\> | Wicket fall details |
| `isComplete` | boolean | Whether innings is complete |

---

#### 5. Over Entity
**Collection:** `overs`

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier |
| `overNumber` | int | Over number (1-based) |
| `inningsId` | String | Reference to innings |
| `bowler` | String | Bowler player ID |
| `balls` | List\<Ball\> | List of balls in this over |
| `runsInOver` | int | Total runs in this over |
| `wicketsInOver` | int | Total wickets in this over |

---

#### 6. Ball (Embedded)
**Not a separate collection - embedded in Over**

| Field | Type | Description |
|-------|------|-------------|
| `ballNumberInOver` | int | Ball number within the over (1-6) |
| `striker` | String | Batsman on strike (player ID) |
| `bowler` | String | Bowler (player ID) |
| `outcome` | BallOutcome | Outcome of the ball |
| `runScored` | int | Runs scored on this ball |
| `isWicket` | boolean | Whether this ball resulted in a wicket |
| `isExtra` | boolean | Whether this ball was an extra |

---

#### 7. BattingScoreCard (Embedded)
**Embedded in Innings**

| Field | Type | Description |
|-------|------|-------------|
| `playerId` | String | Player ID |
| `runsScored` | int | Total runs scored |
| `ballsFaced` | int | Total balls faced |
| `fours` | int | Number of fours hit |
| `sixes` | int | Number of sixes hit |
| `dotBalls` | int | Number of dot balls faced |
| `isOut` | boolean | Whether player got out |
| `dismissedBy` | Player | Bowler who took the wicket (null if not out) |

---

#### 8. BowlingScoreCard (Embedded)
**Embedded in Innings**

| Field | Type | Description |
|-------|------|-------------|
| `playerId` | String | Player ID |
| `ballsBowled` | int | Total balls bowled |
| `runsConceded` | int | Total runs conceded |
| `wicketsTaken` | int | Total wickets taken |
| `maidenOvers` | int | Number of maiden overs |
| `dotBalls` | int | Number of dot balls bowled |
| `widesConceded` | int | Number of wides bowled |

---

#### 9. FallOfWicket (Embedded)
**Embedded in Innings**

| Field | Type | Description |
|-------|------|-------------|
| `wicketNumber` | int | Wicket number (1-10) |
| `playerOut` | Player | Player who got out |
| `teamScoreAtFall` | int | Team score when wicket fell |
| `legalBallsAtFall` | int | Legal balls bowled when wicket fell |

---

#### 10. Toss (Embedded)
**Embedded in Match**

| Field | Type | Description |
|-------|------|-------------|
| `winner` | String | Team name that won the toss |
| `decision` | TossDecision | Decision made (BAT or BOWL) |

---

#### 11. MatchResult (Embedded)
**Embedded in Match**

| Field | Type | Description |
|-------|------|-------------|
| `winner` | String | Winning team name (null if tie) |
| `isTie` | boolean | Whether match was a tie |
| `winMarginRuns` | int | Winning margin in runs (0 if won by wickets) |
| `winMarginWickets` | int | Winning margin in wickets (0 if won by runs) |
| `summary` | String | Human-readable match summary |

---

### Enumerations

#### Player Roles
- `BATSMAN`: Specialized in batting, higher scoring probability
- `BOWLER`: Specialized in bowling, defensive batting style
- `ALL_ROUNDER`: Skilled in both batting and bowling, balanced approach

#### Team Types
- `NATIONAL`: National team
- `FRANCHISE`: Franchise team (e.g., IPL teams)
- `CLUB`: Club team

#### Match Status
- `TOSS_DONE`: Toss completed
- `FIRST_INNINGS`: First innings in progress
- `SECOND_INNINGS`: Second innings in progress
- `COMPLETED`: Match has finished

#### Ball Outcomes
- `DOT`: No runs scored (0 runs)
- `ONE`: Single run (1 run)
- `TWO`: Two runs (2 runs)
- `THREE`: Three runs (3 runs)
- `FOUR`: Boundary (4 runs)
- `FIVE`: Five runs (5 runs, rare)
- `SIX`: Over boundary (6 runs)
- `WICKET`: Batsman dismissed (-1 runs)
- `WIDE`: Wide ball (1 extra run)
- `NO_BALL`: No ball (1 extra run)

#### Toss Decisions
- `BAT`: Choose to bat first
- `BOWL`: Choose to bowl first

---

### Database Collections

The application uses the following MongoDB collections:

1. **players** - Stores all player information
2. **teams** - Stores team information with player references
3. **matches** - Stores match metadata and references to innings
4. **innings** - Stores detailed innings data with embedded scorecards
5. **overs** - Stores ball-by-ball data for each over

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

---

## 👤 Player APIs

### 1. Create Player
Creates a new player in the system.

**Endpoint:** `POST /api/players`

**Request Body:**
```json
{
  "name": "Virat Kohli",
  "role": "BATSMAN"
}
```

**Request Fields:**
- `name` (string, required): Player name (2-30 characters)
- `role` (enum, required): Player role - `BATSMAN`, `BOWLER`, or `ALL_ROUNDER`

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Virat Kohli",
  "role": "BATSMAN"
}
```

---

### 2. Get All Players
Retrieves all players in the system.

**Endpoint:** `GET /api/players`

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "name": "Virat Kohli",
    "role": "BATSMAN"
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "name": "Jasprit Bumrah",
    "role": "BOWLER"
  }
]
```

---

### 3. Get Player by ID
Retrieves a specific player by their ID.

**Endpoint:** `GET /api/players/{id}`

**Path Parameters:**
- `id` (string): Player ID

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Virat Kohli",
  "role": "BATSMAN"
}
```

---

### 4. Get Players by Role
Retrieves all players with a specific role.

**Endpoint:** `GET /api/players/role/{role}`

**Path Parameters:**
- `role` (enum): Player role - `BATSMAN`, `BOWLER`, or `ALL_ROUNDER`

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "name": "Virat Kohli",
    "role": "BATSMAN"
  },
  {
    "id": "507f1f77bcf86cd799439013",
    "name": "Rohit Sharma",
    "role": "BATSMAN"
  }
]
```

---

### 5. Update Player
Updates an existing player's information.

**Endpoint:** `PUT /api/players/{id}`

**Path Parameters:**
- `id` (string): Player ID

**Request Body:**
```json
{
  "name": "Virat Kohli Updated",
  "role": "ALL_ROUNDER"
}
```

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Virat Kohli Updated",
  "role": "ALL_ROUNDER"
}
```

---

## 🏆 Team APIs

### 1. Create Team
Creates a new team with 11 players.

**Endpoint:** `POST /api/teams`

**Request Body:**
```json
{
  "name": "India",
  "type": "NATIONAL",
  "playerIds": [
    "507f1f77bcf86cd799439011",
    "507f1f77bcf86cd799439012",
    "507f1f77bcf86cd799439013",
    "507f1f77bcf86cd799439014",
    "507f1f77bcf86cd799439015",
    "507f1f77bcf86cd799439016",
    "507f1f77bcf86cd799439017",
    "507f1f77bcf86cd799439018",
    "507f1f77bcf86cd799439019",
    "507f1f77bcf86cd799439020",
    "507f1f77bcf86cd799439021"
  ]
}
```

**Request Fields:**
- `name` (string, required): Team name (2-30 characters)
- `type` (enum, required): Team type - `NATIONAL`, `FRANCHISE`, or `CLUB`
- `playerIds` (array, required): Exactly 11 player IDs

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439100",
  "name": "India",
  "type": "NATIONAL",
  "players": [
    {
      "id": "507f1f77bcf86cd799439011",
      "name": "Virat Kohli",
      "role": "BATSMAN"
    },
    {
      "id": "507f1f77bcf86cd799439012",
      "name": "Jasprit Bumrah",
      "role": "BOWLER"
    }
    // ... 9 more players
  ]
}
```

---

### 2. Get All Teams
Retrieves all teams with player IDs.

**Endpoint:** `GET /api/teams`

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439100",
    "name": "India",
    "type": "NATIONAL",
    "playerIds": [
      "507f1f77bcf86cd799439011",
      "507f1f77bcf86cd799439012"
      // ... more player IDs
    ]
  }
]
```

---

### 3. Get Team by ID
Retrieves a specific team with full player details.

**Endpoint:** `GET /api/teams/{id}`

**Path Parameters:**
- `id` (string): Team ID

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439100",
  "name": "India",
  "type": "NATIONAL",
  "players": [
    {
      "id": "507f1f77bcf86cd799439011",
      "name": "Virat Kohli",
      "role": "BATSMAN"
    }
    // ... 10 more players
  ]
}
```

---

### 4. Update Team
Updates an existing team's information.

**Endpoint:** `PUT /api/teams/{id}`

**Path Parameters:**
- `id` (string): Team ID

**Request Body:**
```json
{
  "name": "India Updated",
  "type": "NATIONAL",
  "playerIds": [
    // ... 11 player IDs
  ]
}
```

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439100",
  "name": "India Updated",
  "type": "NATIONAL",
  "players": [
    // ... player details
  ]
}
```

---

### 5. Delete Team
Deletes a team from the system.

**Endpoint:** `DELETE /api/teams/{id}`

**Path Parameters:**
- `id` (string): Team ID

**Response:** `204 No Content`

---

## 🏏 Match APIs

### 1. Simulate Match
Simulates a complete cricket match between two teams.

**Endpoint:** `POST /api/matches`

**Request Body:**
```json
{
  "teamAId": "507f1f77bcf86cd799439100",
  "teamBId": "507f1f77bcf86cd799439101",
  "maxOvers": 20
}
```

**Request Fields:**
- `teamAId` (string, required): ID of the first team
- `teamBId` (string, required): ID of the second team
- `maxOvers` (integer, required): Number of overs per innings (minimum: 1)

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439200",
  "teamA": {
    "id": "507f1f77bcf86cd799439100",
    "name": "India",
    "type": "NATIONAL",
    "playerIds": ["..."]
  },
  "teamB": {
    "id": "507f1f77bcf86cd799439101",
    "name": "Australia",
    "type": "NATIONAL",
    "playerIds": ["..."]
  },
  "toss": {
    "winner": "India",
    "decision": "BAT"
  },
  "maxOvers": 20,
  "firstInnings": {
    "inningsId": "507f1f77bcf86cd799439300",
    "battingTeamId": "507f1f77bcf86cd799439100",
    "bowlingTeamId": "507f1f77bcf86cd799439101",
    "targetScore": 0,
    "totalRuns": 185,
    "totalWickets": 7,
    "totalExtras": 12,
    "legalBallsBowled": 120,
    "battingCards": [
      {
        "playerId": "507f1f77bcf86cd799439011",
        "runsScored": 45,
        "ballsFaced": 32,
        "fours": 5,
        "sixes": 2,
        "dotBalls": 10,
        "isOut": true,
        "dismissedBy": {
          "id": "507f1f77bcf86cd799439050",
          "name": "Pat Cummins",
          "role": "BOWLER"
        }
      }
      // ... more batting cards
    ],
    "bowlingCards": [
      {
        "playerId": "507f1f77bcf86cd799439050",
        "ballsBowled": 24,
        "runsConceded": 35,
        "wicketsTaken": 2,
        "maidenOvers": 0,
        "dotBalls": 8,
        "widesConceded": 2
      }
      // ... more bowling cards
    ],
    "fallOfWickets": [
      {
        "wicketNumber": 1,
        "playerOut": {
          "id": "507f1f77bcf86cd799439011",
          "name": "Virat Kohli",
          "role": "BATSMAN"
        },
        "teamScoreAtFall": 45,
        "legalBallsAtFall": 32
      }
      // ... more wickets
    ],
    "isComplete": true
  },
  "secondInnings": {
    // ... similar structure to firstInnings
  },
  "result": {
    "winner": "India",
    "isTie": false,
    "winMarginRuns": 0,
    "winMarginWickets": 3,
    "summary": "India won by 3 wickets"
  },
  "status": "COMPLETED"
}
```

---

### 2. Get Match by ID
Retrieves complete match details including both innings.

**Endpoint:** `GET /api/matches/{id}`

**Path Parameters:**
- `id` (string): Match ID

**Response:** `200 OK`
```json
{
  // Same structure as Simulate Match response
}
```

---

### 3. Get Match Scorecard
Retrieves a simplified scorecard for the match.

**Endpoint:** `GET /api/matches/{id}/scorecard`

**Path Parameters:**
- `id` (string): Match ID

**Response:** `200 OK`
```json
{
  "matchId": "507f1f77bcf86cd799439200",
  "teamA": "India",
  "teamB": "Australia",
  "overs": 20,
  "toss": {
    "winner": "India",
    "decision": "BAT"
  },
  "firstInnings": {
    "inningsId": "507f1f77bcf86cd799439300",
    "battingTeamId": "507f1f77bcf86cd799439100",
    "bowlingTeamId": "507f1f77bcf86cd799439101",
    "targetScore": 0,
    "totalRuns": 185,
    "totalWickets": 7,
    "totalExtras": 12,
    "legalBallsBowled": 120,
    "battingCards": [
      // ... batting statistics
    ],
    "bowlingCards": [
      // ... bowling statistics
    ],
    "fallOfWickets": [
      // ... wicket details
    ],
    "isComplete": true
  },
  "secondInnings": {
    // ... similar structure
  },
  "result": {
    "winner": "India",
    "isTie": false,
    "winMarginRuns": 0,
    "winMarginWickets": 3,
    "summary": "India won by 3 wickets"
  }
}
```

---

### 4. Get Innings Details
Retrieves detailed ball-by-ball information for a specific innings.

**Endpoint:** `GET /api/matches/{id}/innings/{inningsNum}`

**Path Parameters:**
- `id` (string): Match ID
- `inningsNum` (integer): Innings number (1 or 2)

**Response:** `200 OK`
```json
{
  "battingTeam": "India",
  "bowlingTeam": "Australia",
  "maxOvers": 20,
  "targetScore": 0,
  "totalRuns": 185,
  "totalWickets": 7,
  "totalExtras": 12,
  "oversPlayed": 20,
  "battingCard": [
    {
      "playerId": "507f1f77bcf86cd799439011",
      "runsScored": 45,
      "ballsFaced": 32,
      "fours": 5,
      "sixes": 2,
      "dotBalls": 10,
      "isOut": true,
      "dismissedBy": {
        "id": "507f1f77bcf86cd799439050",
        "name": "Pat Cummins",
        "role": "BOWLER"
      }
    }
    // ... more batting cards
  ],
  "bowlingCard": [
    {
      "playerId": "507f1f77bcf86cd799439050",
      "ballsBowled": 24,
      "runsConceded": 35,
      "wicketsTaken": 2,
      "maidenOvers": 0,
      "dotBalls": 8,
      "widesConceded": 2
    }
    // ... more bowling cards
  ],
  "fallOfWickets": [
    {
      "wicketNumber": 1,
      "playerOut": {
        "id": "507f1f77bcf86cd799439011",
        "name": "Virat Kohli",
        "role": "BATSMAN"
      },
      "teamScoreAtFall": 45,
      "legalBallsAtFall": 32
    }
    // ... more wickets
  ],
  "overs": [
    {
      "id": "507f1f77bcf86cd799439400",
      "overNumber": 1,
      "inningsId": "507f1f77bcf86cd799439300",
      "bowler": "Pat Cummins",
      "balls": [
        {
          "ballNumberInOver": 1,
          "striker": "Rohit Sharma",
          "bowler": "Pat Cummins",
          "outcome": "DOT",
          "runScored": 0,
          "isWicket": false,
          "isExtra": false
        },
        {
          "ballNumberInOver": 2,
          "striker": "Rohit Sharma",
          "bowler": "Pat Cummins",
          "outcome": "FOUR",
          "runScored": 4,
          "isWicket": false,
          "isExtra": false
        }
        // ... more balls
      ],
      "runsInOver": 8,
      "wicketsInOver": 0
    }
    // ... more overs
  ],
  "isComplete": true
}
```

---

### 5. Get Match Result
Retrieves only the result of the match.

**Endpoint:** `GET /api/matches/{id}/result`

**Path Parameters:**
- `id` (string): Match ID

**Response:** `200 OK`
```json
{
  "winner": "India",
  "isTie": false,
  "winMarginRuns": 0,
  "winMarginWickets": 3,
  "summary": "India won by 3 wickets"
}
```

---

## 🎲 Match Simulation Engine

### Role-Based Ball Outcome Generator

The match simulation uses a sophisticated **Role-Biased Ball Outcome Generator** that produces realistic cricket outcomes based on the batsman's role. Each ball's outcome is determined using weighted probabilities that reflect the playing style of different player types.

#### How It Works

1. **Weighted Probability System**: Each player role has predefined weights for different ball outcomes
2. **Random Selection**: The system uses weighted random selection to determine the outcome of each ball
3. **Realistic Simulation**: Batsmen are more likely to score runs, while bowlers are more likely to get out

#### Ball Outcome Weights by Player Role

The weights represent the relative probability of each outcome occurring:

| Outcome | BATSMAN | BOWLER | ALL_ROUNDER | Description |
|---------|---------|--------|-------------|-------------|
| **DOT** | 15 | 30 | 22 | No runs scored |
| **ONE** | 25 | 15 | 20 | Single run |
| **TWO** | 15 | 8 | 12 | Two runs |
| **THREE** | 5 | 3 | 4 | Three runs |
| **FOUR** | 18 | 5 | 12 | Boundary (4 runs) |
| **FIVE** | 1 | 1 | 1 | Five runs (rare) |
| **SIX** | 12 | 3 | 7 | Over boundary (6 runs) |
| **WICKET** | 5 | 25 | 12 | Batsman dismissed |
| **WIDE** | 2 | 5 | 5 | Wide ball (extra) |
| **NO_BALL** | 2 | 5 | 5 | No ball (extra) |

#### Key Insights

**BATSMAN** (Aggressive Scoring):
- Higher probability of boundaries (18% for FOUR, 12% for SIX)
- More likely to score singles (25% for ONE)
- Lower chance of getting out (5% for WICKET)
- Fewer dot balls (15%)

**BOWLER** (Defensive Play):
- Highest probability of dot balls (30%)
- Much higher chance of getting out (25% for WICKET)
- Lower boundary scoring (5% for FOUR, 3% for SIX)
- More extras due to defensive approach

**ALL_ROUNDER** (Balanced):
- Balanced weights between batsman and bowler
- Moderate boundary scoring (12% for FOUR, 7% for SIX)
- Moderate wicket probability (12%)
- Balanced dot ball percentage (22%)

#### Implementation

The `RoleBiasedGenerator` class implements the weighted random selection algorithm:

```java
// Simplified algorithm
1. Get the weights array for the batsman's role
2. Calculate total weight (sum of all weights)
3. Generate a random number between 0 and total weight
4. Use cumulative weights to select the outcome
5. Return the corresponding BallOutcome
```

This ensures that each ball's outcome is realistic and reflects the skill level and playing style of the batsman facing the delivery.

---

## ⚠️ Error Handling

The API returns appropriate HTTP status codes and error messages:

### Common Error Responses

**400 Bad Request** - Validation errors
```json
{
  "timestamp": "2026-03-23T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "Player Name is required"
    }
  ]
}
```

**404 Not Found** - Resource not found
```json
{
  "timestamp": "2026-03-23T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Player not found with id: 507f1f77bcf86cd799439011"
}
```

**500 Internal Server Error** - Server error
```json
{
  "timestamp": "2026-03-23T10:30:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

### Validation Rules

#### Player
- Name: 2-30 characters, required
- Role: Must be BATSMAN, BOWLER, or ALL_ROUNDER, required

#### Team
- Name: 2-30 characters, required
- Type: Must be NATIONAL, FRANCHISE, or CLUB, required
- Player IDs: Exactly 11 valid player IDs, required

#### Match
- Team A ID: Required, must exist
- Team B ID: Required, must exist, must be different from Team A
- Max Overs: Minimum 1, required

---

## 🎮 Example Usage Flow

1. **Create Players**
   ```bash
   # Create 22 players (11 for each team)
   curl -X POST http://localhost:8080/api/players \
     -H "Content-Type: application/json" \
     -d '{"name": "Virat Kohli", "role": "BATSMAN"}'
   ```

2. **Create Teams**
   ```bash
   # Create Team A
   curl -X POST http://localhost:8080/api/teams \
     -H "Content-Type: application/json" \
     -d '{
       "name": "India",
       "type": "NATIONAL",
       "playerIds": ["id1", "id2", ..., "id11"]
     }'

   # Create Team B
   curl -X POST http://localhost:8080/api/teams \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Australia",
       "type": "NATIONAL",
       "playerIds": ["id12", "id13", ..., "id22"]
     }'
   ```

3. **Simulate Match**
   ```bash
   curl -X POST http://localhost:8080/api/matches \
     -H "Content-Type: application/json" \
     -d '{
       "teamAId": "teamAId",
       "teamBId": "teamBId",
       "maxOvers": 20
     }'
   ```

4. **View Results**
   ```bash
   # Get full match details
   curl http://localhost:8080/api/matches/{matchId}

   # Get scorecard
   curl http://localhost:8080/api/matches/{matchId}/scorecard

   # Get specific innings
   curl http://localhost:8080/api/matches/{matchId}/innings/1

   # Get result
   curl http://localhost:8080/api/matches/{matchId}/result
   ```

---

## 📝 License

This project is for educational and demonstration purposes.

## 👨‍💻 Author

Himanshu Rai

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

**Happy Cricket Simulating! 🏏**
