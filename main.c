#include "./node/node.h"
#include "stack/stack.h"

#include <stdlib.h>

void delete_int(void*);

void test_new_node(void);

void test_new_stack(void);

int main(void)
{
  test_new_node();

  test_new_stack();

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

void test_new_stack(void)
{
  Stack* stack = new_stack();

  int* a = malloc(sizeof(int));

  if (!a)
  {
    exit(1);
  }

  int* b = malloc(sizeof(int));

  if (!b)
  {
    free(a);

    exit(1);
  }

  int* c = malloc(sizeof(int));

  if (!c)
  {
    free(a);

    free(b);

    exit(1);
  }

  free(a);

  free(b);

  free(c);

  delete_stack(&stack, delete_int);
}
