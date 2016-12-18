(ns macchiato.test.runner
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [macchiato.test.sql]))

(doo-tests 'macchiato.test.sql)
