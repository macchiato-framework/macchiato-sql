# macchiato-sql

Build status: 
[![CircleCI](https://circleci.com/gh/macchiato-framework/macchiato-sql.svg?style=svg)](https://circleci.com/gh/macchiato-framework/macchiato-sql)

Query parsing functions for Clojure on NodeJS

Initially intended for Macchiato as a port of [PREQL](https://github.com/NGPVAN/preql). We'll likely expand it into its own as we go.

This namespace only builds the queries as functions. Database access is expected to take place elsewhere, likely in an abstracted manner.  For an example, see [macchiato-db-scratchpad](https://github.com/macchiato-framework/macchiato-db-scratchpad/)

## Approach and limitations

- One query, one file.
- Queries functions are named after the file that stores them.
- Positional arguments only, doesn't yet take a HugSQL-like parameter map.

Assume any functions are currently a work-in-progress and may change before we hit 0.1.0. 

## Providing feeedback

Feedback welcome. You can find us on:

- [#macchiato on Matrix](https://riot.im/app/#/room/#macchiato:matrix.org), which mirrors
- [#macchiato on Clojurians](https://clojurians.slack.com/archives/macchiato)

## Usage

### Overview

Use `make-query-map` to generate query functions from a folder.

```clojure
(def queries (sql/make-query-map (sql/load-queries "your/sql-files")))
```

This will return a map where the query functions are indexed by the query's file name.

```clojure
(def db-insert (:insert-name queryies))
```

Each function will expect as its first parameter a first-order function that will actually execute the query, and then a list of values to pass.

For example, from [macchiato-db-scratchpad](https://github.com/macchiato-framework/macchiato-db-scratchpad/):

```clojure
(defn add-new-users []
  (db/with-transaction
    (fn [conn]
      (db-insert conn "joeBob" 21)
      (db-insert conn "jack" 25)
      (db-insert conn "Unnamed" nil)
      true)))
```

The project contains tests for query parsing and loading. See [macchiato-db-scratchpad](https://github.com/macchiato-framework/macchiato-db-scratchpad/) for examples of how to use them against a PostgreSQL database.


### Also, tests!

The namespace `macchiato.test.sql` contains more examples as test functions.


## License

Distributed under the [MIT License](https://tldrlegal.com/license/mit-license)