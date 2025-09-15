# hayai

## Tests

To run the memory leak tests, just use:

```sh
gcc main.c ./node/node.c ./stack/stack.c -I ./node -I ./stack -o main.bin

valgrind --leak-check=full -v ./main.bin
```
