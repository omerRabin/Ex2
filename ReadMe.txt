In this project we improved the previous tasks so that the algrorithm can be used on directed graphs.
The nodes numbered by uniques keys. Each edge in the graph has a weight. Each edge connects 2 vertices in a way that can be moved on the edge.
![](https://user-images.githubusercontent.com/73130830/102702443-5c282200-426b-11eb-9efa-14a7f7bcbafa.png)
we created the graph using hash map because its very efficient in terms of runtimes and very suitable for the structure of a graph.
We can get a node in the graph, check if edge exists, get a specific edge, add node to the graph, connect between two nodes-make an edge,
 retrieval of the whole graph, retrieval neighbor group of specific node(all nodes that have a common edge with this node), remove node and remove edge.
In addition, our project contains many functions that implement sophisticated algorithms like bfs algorithm and dijkstra's algorithms. functions like: init the graph, copy the graph, check if the graph is connected(what means that there is a path from each node in the graph to each node in the graph), compute the shortest path in the graph between two given node and return the path's length, and option to save and load a specific graph to json file.
 In the second part of our project we implemented a-game of pokemons that include agents that their target is to catch pokemons that exist in the graph on a different locations. The speed of the agents changes when they eat more pokemons.
 If you want to run our project, you can do-git clone to our project, and then you need to open cmd, download the jar file of our project and then type java-jar Ex2.jar. And if you want to play you can insert yor userName and game level to the window that opened and see how the agents is mooving to the pokemons and eat them.

