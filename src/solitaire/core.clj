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

(def empty-stack {:up () :down ()})

(def empty-game
  {:foundations (vec (repeat 4 empty-stack))
   :stock       (vec (repeat 1 empty-stack))
   :tableau     (vec (repeat 7 empty-stack))})

(defn split-deck [n deck]
  [(take n deck) (drop n deck)])

(defn deal-game []
  (let [deck (shuffle card/deck)
        game empty-game
        [game deck] (reduce (fn [[game deck] t]
                              (let [[up   deck] (split-deck 1 deck)
                                    [down deck] (split-deck t deck)]
                                [(-> game
                                   (assoc-in [:tableau t :up]   up)
                                   (assoc-in [:tableau t :down] down))
                                 deck]))
                      [game deck] (range 7))]
    (assoc-in game [:stock 0 :down] deck)))

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
