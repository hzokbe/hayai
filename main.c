#include "./node/node.h"

#include <stdlib.h>

void delete_int(void*);

void test_new_node(void);

int main(void)
{
  test_new_node();

  return 0;
}

void delete_int(void* value)
{
  free(value);
}

void test_new_node(void)
{
  int* i = malloc(sizeof(int));

  if (!i)
  {
    exit(1);
  }

  Node* node = new_node(i);

  if (!node)
  {
    free(i);

    exit(1);
  }

  delete_node(&node, delete_int);
}
