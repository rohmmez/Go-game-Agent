# Go-game-Agent
This is a programming assignment that we develop our own AI agents based on some of the AI techniques for Search, Game Playing, and Reinforcement Learning.

● Used the Minimax algorithm and Utility Value Function to get the Game Playing Tree. Chose the best branch of the Game Playing Tree based on the Utility Value. Designed a Utility Value Function based on the number of our stones minus the number of their stones, the number of our stones’ liberty minus the number of their stones’ liberty and try to capture their stones as many as possible. Used Alpha-Beta Pruning to cut some branches of the Game Playing Tree.

● Used the Q-Learning algorithm and Q-Value Function to create Q-Learning Table. Used txt or JSON format to store the stone information and Q-Value in the Q- Learning Table. Chose the next step based on the stone information and Q-Value in the Q-Learning Table.

● Used Greedy algorithm Player to train our Go Game Agent. Used Minimax algorithm to choose next step if the Q-Learning Table is too large to search.
