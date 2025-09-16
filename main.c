#include "./node/node.h"
#include "stack/stack.h"

#include <stdlib.h>

typedef struct Foo Foo;

void delete_foo(void*);

void test_new_node(void);

void test_new_stack(void);

int main(void)
{
  test_new_node();

  test_new_stack();

  return 0;
}

typedef struct Foo
{
  char* bar;
} Foo;

void delete_foo(void* value)
{
  free(((Foo*) value)->bar);

  free(value);
}

void test_new_node(void)
{
  Foo* f = malloc(sizeof(Foo));

  if (!f)
  {
    exit(1);
  }

  f->bar = malloc(sizeof(char) * 10);

  if (!f->bar)
  {
    free(f);

    exit(1);
  }

  Node* node = new_node(f);

  if (!node)
  {
    free(f->bar);

    free(f);

    exit(1);
  }

  delete_node(&node, delete_foo);
}

void test_new_stack(void)
{
  Stack* stack = new_stack();

  if (!stack)
  {
    exit(1);
  }

  Foo* f1 = malloc(sizeof(Foo));

  if (!f1)
  {
    delete_stack(&stack, delete_foo);

    exit(1);
  }

  f1->bar = malloc(10);

  if (!f1->bar)
  {
    free(f1);

    delete_stack(&stack, delete_foo);

    exit(1);
  }

  Foo* f2 = malloc(sizeof(Foo));

  if (!f2)
  {
    free(f1->bar);

    free(f1);

    delete_stack(&stack, delete_foo);

    exit(1);
  }

  f2->bar = malloc(20);
  if (!f2->bar)
  {
    free(f2);

    free(f1->bar);

    free(f1);

    delete_stack(&stack, delete_foo);

    exit(1);
  }

  Foo* f3 = malloc(sizeof(Foo));

  if (!f3)
  {
    free(f2->bar);

    free(f2);

    free(f1->bar);

    free(f1);

    delete_stack(&stack, delete_foo);

    exit(1);
  }

  f3->bar = malloc(sizeof(char) * 10);

  if (!f3->bar)
  {
    free(f3);

    free(f2->bar);

    free(f2);

    free(f1->bar);

    free(f1);

    delete_stack(&stack, delete_foo);

    exit(1);
  }

  push(stack, f1);

  push(stack, f2);

  push(stack, f3);

  delete_stack(&stack, delete_foo);
}
