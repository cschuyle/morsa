# Morsor

A list of lists navigator

## Why?

The REAL goal: Vibe-code the whole thing. This is my first experience vibe-coding.

But, as for what the app DOES:

I'm a list-maker. I have a few dozen lists which I want to be able to easily browse, search and do some analysis on. That's what the app does.

## What's the name?

This is a re-write of a previous app I built, called Moocho.me.

I like Walruses.

I speak Spanish.

Morsa is Walrus in Spanish.

Moocho and Morsa both start with M.

I used Cursor for this.

Morsa + Cursor = Morsor.

I like Lord of the Rings. If you do too you know what Mordor is.

There is a distance of 1 between Mordor and Morsor in two pretty basic measurements:
- Levenshtein
- Between keys on most keyboards

# Features

- Search all troves (that's what I call a list), or a subset of troves.
- Find duplicate items (or, near-duplicates) across troves.
    - Example: I've got a couple troves: a list of movie favorites, and a list of movies which are available on Kanopy. Find stuff I like which is available on Kanopy.
- Conversely, find unique items within a trove, with respect to other troves.
    - Example: Same troves as the previous example. Find movies which I like but which I can't get on Kanopy. Then I can rent or buy those movies instead of getting them for free on Kanopy.

## Where do I get the data?

That's another story. Short answer: scripts and manual slogging.

## Requirements for local development or running

- Java 21
- Node (npm)

## How to run

### Option 1. Using the canned data:

In a terminal:
```
./gradlew bootRun
```

In another terminal:
```
cd frontend
npm install # Needed on the first run, or if dependencies change
npm run dev
```

## Option 2. You can use AWS S3 as a data store:

You'll have to put your trove data in place. See [DATA.md](./DATA.md) for some info in this. If you need help go ahead and contact me!

Once this is done, you'll need to set your AWS credentials, then do the same as a canned data run with two extra pieces of configuration:
```
SPRING_PROFILES_ACTIVE=prod MOOCHO_BUCKET_NAME=your-bucket ./gradlew bootRun
```

See [envrc-template](./envrc-template) for a description of the configuration environment variables.

## Build Docker Image

1. Build and run with canned data (dev profile):

```bash
docker build -t morsor .
```

Or, if you need a different architecture (which if you're on a Mac, you PROBABLY DO to deploy on a web host), maybe something like this:
```
docker build --platform linux/amd64 -t morsor .
```

This may not work because of "buildx / multi-platform issues" (sorry, no further details here).

A symptom of this would be your web host telling you that the architecture is incorrect. If this happens, AND the default builder supports multi-platform, you can use it:
```
docker buildx create --use --name multiarch  # only if needed
docker buildx build --platform linux/amd64 -t cschuyle/morsor:latest --push .
```

_Note_: The image is a multi-stage build: Node builds the frontend, then Gradle builds the Spring Boot jar (with `-PskipFrontendBuild=true` so the pre-built static is used), and the final image runs only the JAR on Eclipse Temurin 21 JRE.

2. Test it

To use the canned data:
```
docker run -p 8080:8080 morsor
```

To use S3 (prod profile) instead of canned data, then pass env vars:

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MOOCHO_BUCKET_NAME=your-bucket \
  -e AWS_ACCESS_KEY_ID=... \
  -e AWS_SECRET_ACCESS_KEY=... \
  -e AWS_REGION=... \
  morsor
```

After you run the image, open [http://localhost:8080](http://localhost:8080).

## Build and Push Docker Image all at once
```
docker buildx build --platform linux/amd64 -t your-username/morsor:latest --push .
```


---- Curate the following:

## Create User (for production or local Postgres)

```bash
pip install bcrypt   # or pip3 install bcrypt
python3 scripts/create_user.py
```

Paste the printed SQL into your database (e.g. via `psql`).

## Test auth with local PostgreSQL (docker-compose)

1. **Start Postgres**
   ```bash
   docker compose up -d
   ```

2. **Create schema and a user**
   ```bash
   # Load the auth tables (run once)
   PGPASSWORD=morsor psql -h localhost -U morsor -d morsor -f src/main/resources/schema.sql

   # Create a user (and optionally an API token)
   python3 scripts/create_user.py
   # Paste the printed SQL into psql, or run: PGPASSWORD=morsor psql -h localhost -U morsor -d morsor and paste.
   ```

3. **Run the app against Postgres**
   Use the `postgres` profile so the app uses Postgres instead of H2:
   ```bash
   SPRING_PROFILES_ACTIVE=postgres SPRING_DATASOURCE_PASSWORD=morsor ./gradlew bootRun
   ```
   Then run the frontend (`npm run dev` in `frontend/`) and open the app; log in with the user you created (or use the API token if you generated one).

Vite dev server (npm run dev, e.g. http://localhost:5173): import.meta.env.DEV is true → token is sent → no login.
Production build (e.g. served from Spring Boot at http://localhost:8080): import.meta.env.DEV is false → no token → login required.

Optional override
To use a different dev token, add to .env.local in the frontend:
VITE_DEV_API_TOKEN=your-dev-token



docker compose up -d



# 2. Create schema (once)
PGPASSWORD=morsor psql -h localhost -U morsor -d morsor -f src/main/resources/schema.sql

# 3. Create a user
python3 scripts/create-user.py
# Paste the printed SQL into:
PGPASSWORD=morsor psql -h localhost -U morsor -d morsor

# 4. Run the app against Postgres
SPRING_PROFILES_ACTIVE=postgres SPRING_DATASOURCE_PASSWORD=morsor ./gradlew bootRun