(ns macchiato.sql
  "Query parsing functions for Macchiato

  Initially a port of PREQL https://github.com/NGPVAN/preql
  We'll likely expand it into its own as we go.

  This namespace only builds the queries as functions. Database access is
  expected to take place elsewhere, likely in an abstracted manner.

  Current approach and limitations:
  - One query, one file.
  - Queries are named after the file that stores them.
  - Positional arguments only, doesn't yet take a HugSQL-like parameter map.

  Assume any functions are currently a work-in-progress and may change
  before we hit 0.1.0."
  (:require [clojure.string :as string :refer [ends-with? lower-case split trim]]
            [macchiato.fs :as fs]
            [macchiato.fs.path :as path]
            [schema.core :as s :include-macros true]))


(defn sql-file?
  "Checks if a file exists and its name ends with the SQL extension.

  If we have any problem whatsoever opening a file, we will catch the
  exception and just return false - something we have trouble opening
  can't be considered a SQL file."
  [file-name]
  (try
    (if (empty? file-name)
      false
      (and
        (fs/file? file-name)
        (ends-with? (lower-case file-name) ".sql")))
    (catch js/Error e
      false)))


(defn list-sql-files
  "Gets a folder and returns a list of all SQL files it finds"
  [dir-name]
  (let [path (path/with-separator dir-name)]
    (->> (fs/read-dir-sync path)
         ;; We prepend the path so that we can both check if the file name
         ;; exists and return it so that it can be opened as-is
         (map #(str path %))
         (filter sql-file?))))


(defn load-queries
  "Receives a folder name and returns a list of map containing a query
  name, and the contents of the corresponding file.

  It will not recursively descend into subfolders."
  [dir-name]
  (->> dir-name
       list-sql-files
       (map #(hash-map :name (-> (split % "/")              ; File name contains the folder
                                 last
                                 (string/replace ".sql" ""))
                       :query (trim (fs/slurp %))))))


(defn make-function
  "Receives a query string and returns a function that can be used to execute it.
  The first parameter of the result value will be the function used to query."
  [query-str]
  (fn [query-func & rest]
    (apply query-func query-str rest)))

(s/defschema Queries
             [{:name  s/Str
               :query s/Str}])


(defn make-query-map
  "Receives a collection of queries, which are expected to be maps of
  :name and :query.

  Returns a map that is indexed by the query name, and where the value
  is a query function."
  [query-list]
  (s/validate Queries query-list)
  (reduce #(assoc %1 (keyword (:name %2)) (make-function (:query %2))) {} query-list))

