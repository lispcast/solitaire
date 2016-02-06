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
