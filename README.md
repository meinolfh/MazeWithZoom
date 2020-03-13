# Generating a Maze with backtracking algorithm

Description: https://en.wikipedia.org/wiki/Maze_generation_algorithm

Steps:
 1. Make the initial cell the current cell and mark it as visited
 2. While there are unvisited cells
 3. If the current cell has any neighbours which have not been visited
 4. Choose randomly one of the unvisited neighbours
 5. Push the current cell to the stack
 6. Remove the wall between the current cell and the chosen cell
 7. Make the chosen cell the current cell and mark it as visited
 8. Else if the stack is not empty
 9. Pop a cell from the stack
10. Make it the current cell
