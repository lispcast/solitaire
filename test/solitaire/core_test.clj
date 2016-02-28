(ns solitaire.core-test
  (:require [clojure.test :refer :all]
            [solitaire.core :refer :all]
            [solitaire.card :as card]))

(deftest tableau0-tableau1
  (let [game {:tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}
                        {:up (list
                               (card/make :spades :6))}]}
        game2 {:tableau [{:up (list)}
                         {:up (list
                                (card/make :diamonds :3)
                                (card/make :clubs :4)
                                (card/make :hearts :5)
                                (card/make :spades :6))}]}
        move (fn [game]
               (let [count 3
                     from [:tableau 0 :up]
                     to [:tableau 1 :up]
                     cards (take count (get-in game from))]
                 (-> game
                   (update-in from (partial drop count))
                   (update-in to into (reverse cards)))))]
    (is (= game2 (move game)))))

(deftest tableau0-foundation0
  (let [game {:foundations [{:up (list
                                   (card/make :diamonds :2)
                                   (card/make :diamonds :A))}]
              :tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}]}
        game2 {:foundations [{:up (list
                                    (card/make :diamonds :3)
                                    (card/make :diamonds :2)
                                    (card/make :diamonds :A))}]
               :tableau [{:up (list
                                (card/make :clubs :4)
                                (card/make :hearts :5))}]}
        move (fn [game]
               (let [count 1
                     from [:tableau 0 :up]
                     to [:foundations 0 :up]
                     cards (take count (get-in game from))]
                 (-> game
                   (update-in from (partial drop count))
                   (update-in to into (reverse cards)))))]
    (is (= game2 (move game)))))
