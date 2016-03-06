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

(defn move [game count from to transform]
  (let [cards (take count (get-in game from))]
    (-> game
      (update-in from (partial drop count))
      (update-in to into (transform cards)))))

(defn transfer [game count from to]
  (move game
    count
    (conj (vec from) :up)
    (conj (vec to)   :up)
    reverse))

(defn flip [game count stack]
  (move game
    count
    (conj (vec stack) :down)
    (conj (vec stack)   :up)
    identity))

(defn apply-move [game [action & args]]
  (case action
    :move/transfer
    (apply transfer game args)

    :move/flip
    (apply flip game args)))
