# macchiato-sql

Query parsing functions for Clojure on NodeJS

Initially intended for Macchiato as a port of [PREQL](https://github.com/NGPVAN/preql). We'll likely expand it into its own as we go.

This namespace only builds the queries as functions. Database access is expected to take place elsewhere, likely in an abstracted manner.  For an example, see [macchiato-db-scratchpad](https://github.com/macchiato-framework/macchiato-db-scratchpad/)

## Approach and limitations

- One query, one file.
- Queries are named after the file that stores them.
- Positional arguments only, doesn't yet take a HugSQL-like parameter map.

Assume any functions are currently a work-in-progress and may change before we hit 0.1.0.

## Usage

The project contains tests for query parsing and loading. See [macchiato-db-scratchpad](https://github.com/macchiato-framework/macchiato-db-scratchpad/) for current examples of how to use them against a database.

## License

Distributed under the [MIT License](https://tldrlegal.com/license/mit-license)