(ns solitaire.core
  (:require [solitaire.card :as card]))

{:foundations [(list card) (list card) (list card) (list card)]
 :stock-down (list card)
 :stock-up (list card)
 :tableau-up [(list card) . . .]
 :tableau-down [(list card) . . .]}

{:foundations [(list card) ...]
 :stock {:down (list card)
         :up (list card)}
 :tableau {:down [(list card)...]
           :up [(list card)...]}}

{:foundations [(list card) ...]
 :stock {:down (list card)
         :up (list card)}
 :tableau [{:down (list card)
            :up (list card)} ...]}

;; stack
{:down (list card) :up (list card)}

{:foundations [(list card) ...]
 :stock stack
 :tableau [stack x7]}

{:foundations [(list card) ...]
 :stock   [stack x1]
 :tableau [stack x7]}

{:foundations [stack x4]
 :stock       [stack x1]
 :tableau     [stack x7]}

(first (get-in game [:foundations 0 :up]))

;; stack
(list {:card card :face :up} {:card card :face :down})
