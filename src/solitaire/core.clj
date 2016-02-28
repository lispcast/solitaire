(ns solitaire.core
  (:require [solitaire.card :as card]))

;; card
;; (card/make :diamonds :2)

;; stack
;; {:down (list card) :up (list card)}

;; game
;; {:foundations [stack x4]
;;  :stock       [stack x1]
;;  :tableau     [stack x7]}

;; move
;; (fn [game] ... new-game)
