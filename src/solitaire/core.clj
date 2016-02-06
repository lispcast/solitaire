(ns solitaire.core
  (:require [solitaire.card :as card]))

;; Suit

:diamonds :clubs :spades :hearts

;; Value

1 2 3 4 5 6 7 8 9 10 11 12 13
:ace 2 3 4 5 6 7 8 9 10 :jack :queen :king
:A 2 3 4 5 6 7 8 9 10 :J :Q :K
:A :2 :3 :4 :5 :6 :7 :8 :9 :10 :J :Q :K

;; Card

{:suit :clubs
 :value :3}

{:suit :diamonds
 :value :6
 :type :card}

{:suit :spades
 :value :J
 :color :black}

(defrecord Card [suit value])

(deftype Card [suit value])

[:clubs :3]

[:card :clubs :Q]

;; Card indirection

(defn card [suit value]
  [suit value])

(defn card-suit [[suit _]]
  suit)

(defn card-value [[_ value]]
  value)

(defn value-numeric-value [value]
  (case value
    :A 1
    :2 2
    :3 3
    :4 4
    :5 5
    :6 6
    :7 7
    :8 8
    :9 9
    :10 10
    :J 11
    :Q 12
    :K 13))

(defn card-numeric-value [card]
  (value-numeric-value (card-value card)))

(defn suit-color [suit]
  (case suit
    :diamonds :red
    :hearts   :red
    :clubs    :black
    :spades   :black))

(defn card-color [card]
  (suit-color (card-suit card)))


(card/numeric-value [:spades :K])
